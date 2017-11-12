package ir.berimbasket.app.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import ir.berimbasket.app.R;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.util.TypefaceManager;

/**
 * Created by mohammad hosein on 21/09/2017.
 */

public class FragmentStadiumMap extends Fragment implements OnMapReadyCallback {

    double latitude = 35.723284;
    double longitude = 51.441968;
    String title;
    GoogleMap map;
    private MapView mapView;
    private LocationManager locationManager;
    EntityStadium entityStadium;



    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        entityStadium = (EntityStadium) args.getSerializable("stadiumDetail");
        latitude = Double.parseDouble(entityStadium.getLatitude());
        longitude = Double.parseDouble(entityStadium.getLongitude());
        title = entityStadium.getTitle();
    }

    public FragmentStadiumMap() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stadium_map, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapStadiumMark);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
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
            setStadiumMarker();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setStadiumMarker() {
        Typeface typeface = TypefaceManager.get(getContext(), getString(R.string.font_yekan));
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
        TextView txtMarkerTitle = (TextView) customMarkerView.findViewById(R.id.markerTitle);
        txtMarkerTitle.setText(title);
        txtMarkerTitle.setTypeface(typeface);
        IconGenerator generator = new IconGenerator(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            generator.setBackground(null);
        } else {
            generator.setBackground(null);
        }
        generator.setContentView(customMarkerView);
        Bitmap icon = generator.makeIcon();


        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .title(title));

        marker.setTag(10);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(FragmentStadiumMap.this.latitude, FragmentStadiumMap.this.longitude), 15.0f));
    }





    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
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

}
