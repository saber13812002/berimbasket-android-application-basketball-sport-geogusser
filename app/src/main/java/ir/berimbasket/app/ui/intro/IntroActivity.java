package ir.berimbasket.app.ui.intro;

import android.os.Bundle;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.base.BaseActivity;

public class IntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameIntroContainer, new IntroAdapter())
                .commit();
    }
}
