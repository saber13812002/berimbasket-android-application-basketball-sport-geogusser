package ir.berimbasket.app.ui.home.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Pair;
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
import ir.berimbasket.app.ui.common.PermissionsRequest;
import ir.berimbasket.app.ui.common.entity.StadiumBaseEntity;
import ir.berimbasket.app.ui.landmark.LandmarkActivity;
import ir.berimbasket.app.ui.stadium.StadiumActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    // default location is Tehran (when gps is not on)
    double latitude = 35.6891980;
    double longitude = 51.3889740;
    GoogleMap map;
    private LatLng currentMapViewCenter;
    private MapView mapView;
    private ClusterManager<MyClusterItem> clusterManager;
    private List<Pair<String, String>> markers;

    private static final int ACCESS_FINE_LOCATION = 1;
    private static final float GOOGLE_MAP_DEFAULT_ZOOM_LEVEL = 14.0f;
    private static final float GOOGLE_MAP_DEFAULT_RADIUS = 25000.0f;  // meters
    private static final float WEB_SERVICE_LOAD_ZOOM_LEVEL = 10.0f;
    private static final int WEB_SERVICE_RADIUS = 25000;  // meters

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        markers = new ArrayList<>();
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
        this.map = map;
        setupMapLocations();
    }

    private void setupMapLocations() {
        if (PermissionsRequest.checkAccessFineLocationPermission(getContext(), this, ACCESS_FINE_LOCATION)
                && map != null) {
            map.setMyLocationEnabled(true);
            clusterManager = new ClusterManager<>(getContext(), map);
            clusterManager.setRenderer(new MarkerIconRenderer(getContext(), map, clusterManager));
            map.setOnCameraIdleListener(clusterManager);
            map.setOnMarkerClickListener(clusterManager);
            clusterManager.setOnClusterItemClickListener(clusterItemClickListener);
            updateHomeLocation();
            setCameraLocation();
            currentMapViewCenter = new LatLng(this.latitude, this.longitude);
            getLocations(getContext(), currentMapViewCenter.latitude, currentMapViewCenter.longitude);
            map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    CameraPosition cameraPosition = map.getCameraPosition();
                    LatLng latLng = cameraPosition.target;
                    float[] distanceResults = new float[10];
                    Location.distanceBetween(currentMapViewCenter.latitude, currentMapViewCenter.longitude, latLng.latitude,
                            latLng.longitude, distanceResults);
                    if(cameraPosition.zoom >= WEB_SERVICE_LOAD_ZOOM_LEVEL && distanceResults[0] > GOOGLE_MAP_DEFAULT_RADIUS) {
                        currentMapViewCenter = latLng;
                        getLocations(getContext(), currentMapViewCenter.latitude, currentMapViewCenter.longitude);
                    }
                }
            });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION:
                setupMapLocations();
                break;
        }
    }

    private void getLocations(Context context, double lat, double lng) {
        String pusheId = Pushe.getPusheId(context);
        String userName = new PrefManager(context).getUserName();
        String lang = LocaleManager.getLocale(context).getLanguage();
        WebApiClient.getStadiumApi().getStadiumsV2ForMap(String.valueOf(lat), String.valueOf(lng), WEB_SERVICE_RADIUS,
                "json", pusheId, userName, lang).enqueue(new Callback<List<Stadium>>() {
            @Override
            public void onResponse(Call<List<Stadium>> call, Response<List<Stadium>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Stadium> stadiums = response.body();
                    if (stadiums != null && getView() != null) {
                        for (Stadium stadium : stadiums) {
                            MyClusterItem item = new MyClusterItem(Double.parseDouble(stadium.getLatitude()),
                                    Double.parseDouble(stadium.getLongitude()), stadium.getId(), stadium.getTitle());
                            Pair<String, String> marker = new Pair<>(stadium.getLatitude(), stadium.getLongitude());
                            if (!markers.contains(marker)) {
                                markers.add(marker);
                                clusterManager.addItem(item);
                                clusterManager.cluster();
                            }
                        }
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

    private void sendUserLocationToServer(double latitude, double longitude, Context context) {
        PrefManager pref = new PrefManager(context);
        String pusheId = Pushe.getPusheId(context);
        String userName = pref.getUserName();
        String lang = LocaleManager.getLocale(context).getLanguage();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int version = pInfo.versionCode;
            WebApiClient.getLocationApi().setLocation("jkhfgkljhasfdlkh", String.valueOf(latitude),
                    String.valueOf(longitude), "title", userName, pusheId, String.valueOf(version), lang)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });
        } catch (PackageManager.NameNotFoundException e) {
            // do nothing
        }
    }

    /**
     * Change map location based on city that selected in setting
     */
    private void setCameraLocation() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MapFragment.this.latitude, MapFragment.this.longitude), GOOGLE_MAP_DEFAULT_ZOOM_LEVEL));
    }

    private void updateHomeLocation() {
        PrefManager pref = new PrefManager(getContext());
        String latLong = pref.getSettingsPrefStateList();
        if (Double.parseDouble(latLong.split("a")[0]) != 0) {
            this.latitude = Double.parseDouble(latLong.split("a")[0]);
            this.longitude = Double.parseDouble(latLong.split("a")[1]);
        }

        GPSTracker gps = new GPSTracker(getActivity());
        // Check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            sendUserLocationToServer(latitude, longitude, getContext());
            if (Double.parseDouble(latLong.split("a")[0]) == 0) {
                this.latitude = latitude;
                this.longitude = longitude;
            }
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

    }
}
