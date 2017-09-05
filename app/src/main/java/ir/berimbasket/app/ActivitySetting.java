package ir.berimbasket.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.view.AppBarStateChangeListener;

public class ActivitySetting extends AppCompatActivity {

    TextView title, subTitle;
    private AppBarLayout appBarLayout;

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        collapsingToolbarLayout.setTitleEnabled(false);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.subTitle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title.setTypeface(typeface);
        subTitle.setTypeface(typeface);
        final CircleImageView imgProfileImage = (CircleImageView) findViewById(R.id.imgProfileImage);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(convertDpToPixel(46, getApplicationContext()), convertDpToPixel(46, getApplicationContext()));
        final LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(convertDpToPixel(64, getApplicationContext()), convertDpToPixel(64, getApplicationContext()));

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if (state.name().equals("COLLAPSED")) {
                    imgProfileImage.setLayoutParams(params);
                } else {
                    imgProfileImage.setLayoutParams(params2);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
