package ir.berimbasket.app.ui.create_stadium;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.base.ApplicationLoader;
import ir.berimbasket.app.ui.base.BaseActivity;


public class CreateStadiumActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stadium);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_create_stadium));
    }
}
