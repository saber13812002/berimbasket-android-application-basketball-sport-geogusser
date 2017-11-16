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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterPlayerSpecification;
import ir.berimbasket.app.entity.EntityPlayer;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.SendTo;
import ir.berimbasket.app.util.TypefaceManager;

public class ActivityPlayer extends AppCompatActivity {

    TextView txtPlayerName;
    private ImageView btnReportPlayer;
    private static final String REPORT_PLAYER_BOT = "https://t.me/berimbasketreportbot?start=";
    EntityPlayer entityPlayer;
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
        entityPlayer = (EntityPlayer) getIntent().getSerializableExtra("MyClass");

        Log.i("nameFa", entityPlayer.getName());

        View imgProfileImageView = findViewById(R.id.imgPlayerProfile);
        View txtPlayerNameView = findViewById(R.id.txtPlayerName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgProfileImageView.setTransitionName("image");
            txtPlayerNameView.setTransitionName("name");
        }
        String profilePicUrl = "https://berimbasket.ir/" + getIntent().getStringExtra("ProfilePic");
        Picasso.with(ActivityPlayer.this)
                .load(profilePicUrl)
                .resize(140, 140)
                .centerInside()
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into((ImageView) imgProfileImageView);

        txtPlayerName.setText(entityPlayer.getName());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<String> playerSpecList = getPlayerSpec(entityPlayer);
        setupMatchRecyclerView(playerSpecList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView("Player Screen");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    private void initViews() {
        txtPlayerName = findViewById(R.id.txtPlayerName);
        btnReportPlayer = findViewById(R.id.btnReportPlayer);

        typeface = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));


        btnReportPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendTo.sendToTelegramChat(ActivityPlayer.this, REPORT_PLAYER_BOT + entityPlayer.getId());
            }
        });
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

}
