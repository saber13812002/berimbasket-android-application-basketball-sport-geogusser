package ir.berimbasket.app.activity;

import android.os.Bundle;

import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterFragmentIntro;
import ir.berimbasket.app.util.BaseActivity;

public class ActivityIntro extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        AdapterFragmentIntro adapter = new AdapterFragmentIntro();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameIntroContainer, adapter)
                .commit();
    }
}
