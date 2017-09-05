package ir.berimbasket.app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import ir.berimbasket.app.Adapter.AdapterMatchBoard;
import ir.berimbasket.app.Adapter.AdapterPlayerSpecification;
import ir.berimbasket.app.bundle.BundlePlayer;

public class ActivityPlayer extends AppCompatActivity {

    TextView txtPlayerName, txtPlayerLevel;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_player);
        initViews();
        BundlePlayer bundlePlayer = (BundlePlayer) getIntent().getSerializableExtra("MyClass");
        Log.i("nameFa", bundlePlayer.getName());

        View imgProfileImageView = findViewById(R.id.imgPlayerProfile);
        View txtPlayerNameView = findViewById(R.id.txtPlayerName);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgProfileImageView.setTransitionName("image");
            txtPlayerNameView.setTransitionName("name");
        }

        txtPlayerName.setText(bundlePlayer.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeCollapsingToolbar();

        ArrayList<String> playerSpecList = getPlayerSpec(bundlePlayer);
        setupMatchRecyclerView(playerSpecList);
    }

    private ArrayList<String> getPlayerSpec(BundlePlayer bundlePlayer) {

        ArrayList<String> playerSpecList = new ArrayList<>();
        playerSpecList.add("نام : " + bundlePlayer.getName());
        playerSpecList.add("سن : " + String.valueOf(bundlePlayer.getAge()));
        playerSpecList.add("شهر : " + bundlePlayer.getCity());
        playerSpecList.add("قد : " + String.valueOf(bundlePlayer.getHeight()));
        playerSpecList.add("وزن : " + String.valueOf(bundlePlayer.getWeight()));
        playerSpecList.add("آدرس : " + bundlePlayer.getAddress());
        playerSpecList.add("میزان تجربه : " + bundlePlayer.getExperience());
        playerSpecList.add("سرمربی : " + bundlePlayer.getCoachName());
        playerSpecList.add("تیم : " + bundlePlayer.getTeamName());
        playerSpecList.add("نام کاربری : " + bundlePlayer.getUsername());
        playerSpecList.add("پست بازی : " + String.valueOf(bundlePlayer.getPost()));
//        playerSpecList.add("" + bundlePlayer.getProfileImage());
        playerSpecList.add("تلگرام : " + bundlePlayer.getTelegramId());
        playerSpecList.add("اینستاگرام : " + bundlePlayer.getInstagramId());
        playerSpecList.add("شماره تلفن : " + bundlePlayer.getPhone());
        return playerSpecList;
    }

    private void initViews(){
        txtPlayerName = (TextView) findViewById(R.id.txtPlayerName);
        txtPlayerLevel = (TextView) findViewById(R.id.txtPlayerLevel);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");

        txtPlayerName.setTypeface(typeface);
        txtPlayerLevel.setTypeface(typeface);
    }

    private void setupMatchRecyclerView(ArrayList<String> playerSpecList) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerPlayerSpec);
        AdapterPlayerSpecification adapterPlayerSpecification = new AdapterPlayerSpecification(playerSpecList, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterPlayerSpecification);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public void changeCollapsingToolbar() {
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("جردن باروز");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(250, 250, 250));
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
    }
}
