package ir.berimbasket.app.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivitySetMarker;
import ir.berimbasket.app.map.GPSTracker;

/**
 * Created by mohammad hosein on 6/26/2017.
 */

public class FragmentSetMarker extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private double latitude = 35.723284;
    private double longitude = 51.441968;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_marker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        googleMap.setMyLocationEnabled(true);
//        Geocoder geocoder = new Geocoder(getActivity());
//        try {
//            List<Address> location = geocoder.getFromLocationName("تهران", 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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

        // FIXME: 10/18/2017 use better oop compatible way to approach this
        CameraPosition cameraPosition = ((ActivitySetMarker) getActivity()).getCameraPosition();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng point) {
//                googleMap.clear();
//                View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
//                TextView txtMarkerTitle = (TextView) customMarkerView.findViewById(R.id.markerTitle);
//                txtMarkerTitle.setText(getString(R.string.fragment_set_marker_your_location));
//                IconGenerator generator = new IconGenerator(getActivity());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    generator.setBackground(null);
//                } else {
//                    generator.setBackground(null);
//                }
//                generator.setContentView(customMarkerView);
//                Bitmap icon = generator.makeIcon();
//                googleMap.addMarker(new MarkerOptions().position(point).title("Custom location").icon(BitmapDescriptorFactory.fromBitmap(icon)));
//                latitude = point.latitude;
//                longitude = point.longitude;
//            }
//        });

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                // Get the center of the map
                LatLng center = googleMap.getCameraPosition().target;
                latitude = center.latitude;
                longitude = center.longitude;
            }
        });
    }
}
