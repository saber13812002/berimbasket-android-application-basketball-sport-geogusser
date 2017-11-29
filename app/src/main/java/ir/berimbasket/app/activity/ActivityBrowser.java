package ir.berimbasket.app.activity;

import android.app.Activity;
import android.os.Bundle;

import ir.berimbasket.app.util.Redirect;

public class ActivityBrowser extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String _URL = getIntent().getStringExtra("pushe_activity_extra");
        Redirect.sendToCustomTab(this, _URL);
    }
}
