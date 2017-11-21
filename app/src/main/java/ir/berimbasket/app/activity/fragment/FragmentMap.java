package ir.berimbasket.app.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivityHome;
import ir.berimbasket.app.activity.ActivitySetMarker;
import ir.berimbasket.app.activity.ActivityStadium;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.map.GPSTracker;
import ir.berimbasket.app.map.MarkerIconRenderer;
import ir.berimbasket.app.map.MyClusterItem;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;

public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private static String _URL = "https://berimbasket.ir/bball/get.php";
    double latitude = 35.723284;
    double longitude = 51.441968;
    GoogleMap map;
    private MapView mapView;
    private LocationManager locationManager;
    private ArrayList<EntityStadium> locationList;
    private String TAG = ActivityHome.class.getSimpleName();
    private ClusterManager<MyClusterItem> clusterManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabAddLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySetMarker.class);
                CameraPosition position = map.getCameraPosition();
                double cameraLat = position.target.latitude;
                double cameraLong = position.target.longitude;
                float cameraZoom = position.zoom;
                float cameraBearing = position.bearing;
                float cameraTilt = position.tilt;
                intent.putExtra("camera_latitude", cameraLat);
                intent.putExtra("camera_longitude", cameraLong);
                intent.putExtra("camera_zoom", cameraZoom);
                intent.putExtra("camera_bearing", cameraBearing);
                intent.putExtra("camera_tilt", cameraTilt);
                getActivity().startActivity(intent);
            }
        });

        updateHomeLocation();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationList = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_fragment_map));
        if (map != null) {
            // change camera location only when city filter is changed
            LatLng pre = new LatLng(this.latitude, this.longitude);
            updateHomeLocation();
            LatLng after = new LatLng(this.latitude, this.longitude);
            if (!pre.equals(after)) {
                setCameraLocation();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {

            this.map = map;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            clusterManager = new ClusterManager<>(getContext(), map);
            clusterManager.setRenderer(new MarkerIconRenderer(getContext(), map, clusterManager));
            map.setOnCameraIdleListener(clusterManager);
            map.setOnMarkerClickListener(clusterManager);
            clusterManager.setOnClusterItemClickListener(clusterItemClickListener);
            new GetLocations().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ClusterManager.OnClusterItemClickListener<MyClusterItem> clusterItemClickListener =
            new ClusterManager.OnClusterItemClickListener<MyClusterItem>() {
                @Override
                public boolean onClusterItemClick(MyClusterItem myClusterItem) {
                    try {
                        EntityStadium stadium = new EntityStadium();
                        LatLng latLng = myClusterItem.getPosition();
                        stadium.setLatitude(String.valueOf(latLng.latitude));
                        stadium.setLongitude(String.valueOf(latLng.longitude));
                        stadium.setTitle(myClusterItem.getTitle());
                        stadium.setId(Integer.parseInt(myClusterItem.getId()));
                        Intent intent = new Intent(getActivity(), ActivityStadium.class);
                        intent.putExtra("stadiumDetail", stadium);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            };

    private class GetLocations extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            GPSTracker gps = new GPSTracker(getActivity());

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);

            // Making a request to _URL and getting response
            String pusheId = Pushe.getPusheId(getContext());
            String userName = new PrefManager(getContext()).getUserName();
            String urlParams = String.format("id=0&pusheid=%s&username=%s", pusheId, userName);
            String jsonStr = sh.makeServiceCall(_URL + "?" + urlParams);
            Log.e(TAG, "Response from _URL: " + jsonStr);

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray locations = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String latitude = c.getString("PlaygroundLatitude");
                        String longitude = c.getString("PlaygroundLongitude");
                        String type = c.getString("PlaygroundType");

                        // tmp hash map for single contact
                        EntityStadium entityStadium = new EntityStadium();

                        // adding each child node to HashMap key => value
                        entityStadium.setId(Integer.parseInt(id));
                        entityStadium.setTitle(title);
                        entityStadium.setLatitude(latitude);
                        entityStadium.setLongitude(longitude);
                        entityStadium.setType(type);

                        // adding contact to contact list
                        locationList.add(entityStadium);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /**
             * Updating parsed JSON data into ListView
             * */
            for (int i = 0; i < locationList.size(); i++) {
                EntityStadium entityStadium = locationList.get(i);
                String id = String.valueOf(entityStadium.getId());
                String title = entityStadium.getTitle();
                String latitude = entityStadium.getLatitude();
                String longitude = entityStadium.getLongitude();

                MyClusterItem item = new MyClusterItem(Double.parseDouble(latitude), Double.parseDouble(longitude), id, title);
                clusterManager.addItem(item);
            }

            setCameraLocation();

        }

    }

    /**
     * Change map location based on city that selected in setting
     */
    private void setCameraLocation() {
        updateHomeLocation();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(FragmentMap.this.latitude, FragmentMap.this.longitude), 14.0f));
    }

    private void updateHomeLocation() {
        PrefManager pref = new PrefManager(getContext());
        String latLong = pref.getSettingsPrefStateList();

        if (Double.parseDouble(latLong.split("a")[0]) != 0) {
            FragmentMap.this.latitude = Double.parseDouble(latLong.split("a")[0]);
            FragmentMap.this.longitude = Double.parseDouble(latLong.split("a")[1]);
        }
    }
}
