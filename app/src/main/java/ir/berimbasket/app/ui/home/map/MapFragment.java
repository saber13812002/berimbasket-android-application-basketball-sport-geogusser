package ir.berimbasket.app.ui.home.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Stadium;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.service.GPSTracker;
import ir.berimbasket.app.ui.common.entity.StadiumBaseEntity;
import ir.berimbasket.app.ui.landmark.LandmarkActivity;
import ir.berimbasket.app.ui.stadium.StadiumActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    double latitude = 35.723284;
    double longitude = 51.441968;
    GoogleMap map;
    private MapView mapView;
    private LocationManager locationManager;
    private ArrayList<Stadium> locationList;
    private ClusterManager<MyClusterItem> clusterManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fabAddLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LandmarkActivity.class);
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
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
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
            getLocations(getContext());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ClusterManager.OnClusterItemClickListener<MyClusterItem> clusterItemClickListener =
            new ClusterManager.OnClusterItemClickListener<MyClusterItem>() {
                @Override
                public boolean onClusterItemClick(MyClusterItem myClusterItem) {
                    try {
                        LatLng latLng = myClusterItem.getPosition();
                        String latitude = String.valueOf(latLng.latitude);
                        String longitude = String.valueOf(latLng.longitude);
                        String title = myClusterItem.getTitle();
                        int id = myClusterItem.getId();
                        StadiumBaseEntity stadium = new StadiumBaseEntity(id, title, latitude, longitude);
                        Intent intent = new Intent(getActivity(), StadiumActivity.class);
                        intent.putExtra("stadiumDetail", stadium);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            };

    private void getLocations(Context context) {
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

        String pusheId = Pushe.getPusheId(context);
        String userName = new PrefManager(context).getUserName();
        WebApiClient.getStadiumApi().getStadium(0, pusheId, userName).enqueue(new Callback<List<Stadium>>() {
            @Override
            public void onResponse(Call<List<Stadium>> call, Response<List<Stadium>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Stadium> stadiums = response.body();
                    if (stadiums != null && getView() != null) {
                        for (Stadium stadium : stadiums) {
                            MyClusterItem item = new MyClusterItem(Double.parseDouble(stadium.getLatitude()),
                                    Double.parseDouble(stadium.getLongitude()), stadium.getId(), stadium.getTitle());
                            clusterManager.addItem(item);
                        }
                        setCameraLocation();
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Stadium>> call, Throwable t) {

            }
        });
    }

    /**
     * Change map location based on city that selected in setting
     */
    private void setCameraLocation() {
        updateHomeLocation();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MapFragment.this.latitude, MapFragment.this.longitude), 14.0f));
    }

    private void updateHomeLocation() {
        PrefManager pref = new PrefManager(getContext());
        String latLong = pref.getSettingsPrefStateList();

        if (Double.parseDouble(latLong.split("a")[0]) != 0) {
            MapFragment.this.latitude = Double.parseDouble(latLong.split("a")[0]);
            MapFragment.this.longitude = Double.parseDouble(latLong.split("a")[1]);
        }
    }
}
