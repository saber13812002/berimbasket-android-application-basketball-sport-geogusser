package ir.berimbasket.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterFragmentIntro;
import ir.berimbasket.app.util.LocalChanger;
import ir.berimbasket.app.util.PrefManager;

public class ActivityIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefManager pref = new PrefManager(getApplicationContext());
        LocalChanger localChanger = new LocalChanger();
        localChanger.changeLocal(pref.getSettingsPrefLangList(), getApplicationContext());
        setContentView(R.layout.activity_intro);
        AdapterFragmentIntro adapter = new AdapterFragmentIntro();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameIntroContainer, adapter)
                .commit();
    }
}
