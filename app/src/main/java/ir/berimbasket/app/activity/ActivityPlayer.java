package ir.berimbasket.app.activity;

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

import ir.berimbasket.app.Adapter.AdapterPlayerSpecification;
import ir.berimbasket.app.Entity.EntityPlayer;
import ir.berimbasket.app.R;

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
        EntityPlayer entityPlayer = (EntityPlayer) getIntent().getSerializableExtra("MyClass");
        Log.i("nameFa", entityPlayer.getName());

        View imgProfileImageView = findViewById(R.id.imgPlayerProfile);
        View txtPlayerNameView = findViewById(R.id.txtPlayerName);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgProfileImageView.setTransitionName("image");
            txtPlayerNameView.setTransitionName("name");
        }

        txtPlayerName.setText(entityPlayer.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeCollapsingToolbar();

        ArrayList<String> playerSpecList = getPlayerSpec(entityPlayer);
        setupMatchRecyclerView(playerSpecList);
    }

    private ArrayList<String> getPlayerSpec(EntityPlayer entityPlayer) {

        ArrayList<String> playerSpecList = new ArrayList<>();
        playerSpecList.add("نام : " + entityPlayer.getName());
        playerSpecList.add("سن : " + String.valueOf(entityPlayer.getAge()));
        playerSpecList.add("شهر : " + entityPlayer.getCity());
        playerSpecList.add("قد : " + String.valueOf(entityPlayer.getHeight()));
        playerSpecList.add("وزن : " + String.valueOf(entityPlayer.getWeight()));
        playerSpecList.add("آدرس : " + entityPlayer.getAddress());
        playerSpecList.add("میزان تجربه : " + entityPlayer.getExperience());
        playerSpecList.add("سرمربی : " + entityPlayer.getCoachName());
        playerSpecList.add("تیم : " + entityPlayer.getTeamName());
        playerSpecList.add("نام کاربری : " + entityPlayer.getUsername());
        playerSpecList.add("پست بازی : " + String.valueOf(entityPlayer.getPost()));
//        playerSpecList.add("" + entityPlayer.getProfileImage());
        playerSpecList.add("تلگرام : " + entityPlayer.getTelegramId());
        playerSpecList.add("اینستاگرام : " + entityPlayer.getInstagramId());
        playerSpecList.add("شماره تلفن : " + entityPlayer.getPhone());
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
