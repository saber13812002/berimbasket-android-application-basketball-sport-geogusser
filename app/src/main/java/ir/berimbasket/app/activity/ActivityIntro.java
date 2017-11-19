package ir.berimbasket.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterFragmentIntro;

public class ActivityIntro extends AppCompatActivity {

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
