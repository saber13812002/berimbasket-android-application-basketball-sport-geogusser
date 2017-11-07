package ir.berimbasket.app.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.SendTo;
import ir.berimbasket.app.util.TypefaceManager;
import ir.berimbasket.app.view.AppBarStateChangeListener;

public class ActivitySettings extends AppCompatActivity {

    TextView title, subTitle;
    private AppBarLayout appBarLayout;
    private FloatingActionButton fabUploadPhoto;
    private static final String UPLOAD_PHOTO_BOT = "https://t.me/berimbasketUploadbot";

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        collapsingToolbarLayout.setTitleEnabled(false);
        Typeface typeface = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));
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

        fabUploadPhoto = findViewById(R.id.fabSettings);
        fabUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendTo.sendToTelegramChat(ActivitySettings.this, UPLOAD_PHOTO_BOT);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView("Settings Screen");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}