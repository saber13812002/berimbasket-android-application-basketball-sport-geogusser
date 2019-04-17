package ir.berimbasket.app.ui.landmark;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.net.HttpURLConnection;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandmarkActivity extends BaseActivity {

    private ImageView btnSendLocation;
    private AppCompatEditText edtLocationName;
    private ProgressDialog pDialog;
    private CameraPosition cameraPosition;

    public CameraPosition getCameraPosition() {
        return cameraPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark);
        double latitude = getIntent().getDoubleExtra("camera_latitude", 35.723284);
        double longitude = getIntent().getDoubleExtra("camera_longitude", 51.441968);
        float zoom = getIntent().getFloatExtra("camera_zoom", 14.0f);
        float bearing = getIntent().getFloatExtra("camera_bearing", 0);
        float tilt = getIntent().getFloatExtra("camera_tilt", 0);
        cameraPosition = new CameraPosition(new LatLng(latitude, longitude), zoom, tilt, bearing);

        initViews();
        initListeners();

    }

    private void initViews() {
        btnSendLocation = findViewById(R.id.btnSendMarker);
        edtLocationName = findViewById(R.id.edtLocationName);
    }

    private void initListeners() {
        btnSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mapFrag);
                LandmarkFragment landmarkFragment = (LandmarkFragment) fragment;
                double latitude = landmarkFragment.getLatitude();
                double longitude = landmarkFragment.getLongitude();
                Log.i("message", String.valueOf(latitude));
                if (!edtLocationName.getText().toString().equals("")) {
                    // Tracking Event (Analytics)
                    AnalyticsHelper.getInstance().trackEvent(getString(R.string.analytics_category_map),
                            getString(R.string.analytics_action_set_new_marker),
                            String.format("Location : lat=%s , long=%s", latitude, longitude));
                    setMarker(String.valueOf(latitude), String.valueOf(longitude), edtLocationName.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.activity_set_marker_toast_stadium_name_error), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setMarker(String latitude, String longitude, String title) {
        pDialog = new ProgressDialog(LandmarkActivity.this);
        pDialog.setMessage(getString(R.string.general_please_wait));
        pDialog.setCancelable(false);
        pDialog.show();
        PrefManager pref = new PrefManager(getApplicationContext());
        String pusheId = Pushe.getPusheId(getApplicationContext());
        String userName = pref.getUserName();
        String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
        WebApiClient.getSetMarkerApi(getApplicationContext()).setMarker("jkhfgkljhasfdlkh", latitude, longitude, title, userName, pusheId, lang)
                .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    pDialog.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.activity_set_marker_toast_marker_successful), Toast.LENGTH_SHORT).show();
                    LandmarkActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                pDialog.cancel();
            }
        });
    }
}
