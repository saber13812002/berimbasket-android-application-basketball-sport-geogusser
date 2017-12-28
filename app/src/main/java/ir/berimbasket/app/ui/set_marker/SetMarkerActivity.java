package ir.berimbasket.app.ui.set_marker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.HttpFunctions;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.ApplicationLoader;
import ir.berimbasket.app.ui.base.BaseActivity;

public class SetMarkerActivity extends BaseActivity {

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
        setContentView(R.layout.activity_set_marker);
        double latitude = getIntent().getDoubleExtra("camera_latitude", 35.723284);
        double longitude = getIntent().getDoubleExtra("camera_longitude", 51.441968);
        float zoom = getIntent().getFloatExtra("camera_zoom", 14.0f);
        float bearing = getIntent().getFloatExtra("camera_bearing", 0);
        float tilt = getIntent().getFloatExtra("camera_tilt", 0);
        cameraPosition = new CameraPosition(new LatLng(latitude, longitude), zoom, tilt, bearing);

        initViews();
        initListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_set_marker));
    }

    private void initViews() {
        btnSendLocation = (ImageView) findViewById(R.id.btnSendMarker);
        edtLocationName = (AppCompatEditText) findViewById(R.id.edtLocationName);
    }

    private void initListeners() {
        btnSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mapFrag);
                SetMarkerFragment setMarkerFragment = (SetMarkerFragment) fragment;
                double latitude = setMarkerFragment.getLatitude();
                double longitude = setMarkerFragment.getLongitude();
                Log.i("message", String.valueOf(latitude));
                if (!edtLocationName.getText().toString().equals("")) {
                    PrefManager pref = new PrefManager(getApplicationContext());
                    String pusheId = Pushe.getPusheId(getApplicationContext());
                    String userName = pref.getUserName();
                    String Url = "http://berimbasket.ir/bball/set.php?token=jkhfgkljhasfdlkh&lat="
                            + latitude + "&long=" + longitude + "&title=" + edtLocationName.getText() + "&username=" + userName
                            + "&pusheid=" + pusheId;
                    Url.replace(" ", "%20");
                    // Tracking Event (Analytics)
                    ApplicationLoader.getInstance().trackEvent(getString(R.string.analytics_category_map),
                            getString(R.string.analytics_action_set_new_marker),
                            String.format("Location : lat=%s , long=%s", latitude, longitude));
                    new PostLocation().execute(Url);

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.activity_set_marker_toast_stadium_name_error), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class PostLocation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SetMarkerActivity.this);
            pDialog.setMessage(getString(R.string.general_please_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.POST);
            // Making a request to url and getting response
            String hg = sh.makeServiceCall(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.cancel();
            Toast.makeText(getApplicationContext(), getString(R.string.activity_set_marker_toast_marker_successful), Toast.LENGTH_SHORT).show();
            SetMarkerActivity.this.finish();
        }
    }
}
