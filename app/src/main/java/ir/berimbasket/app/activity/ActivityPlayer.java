package ir.berimbasket.app.activity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import ir.berimbasket.app.adapter.AdapterSocialAcc;
import ir.berimbasket.app.entity.EntityPlayer;
import ir.berimbasket.app.entity.EntitySocialAcc;
import ir.berimbasket.app.exception.UnknownTelegramURL;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.Redirect;
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
        initRecyclerPlayerSpec(playerSpecList);
        initRecyclerSocialAcc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_player));

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
        String specSeparator = getString(R.string.activity_player_spec_separator);
        playerSpecList.add(getString(R.string.activity_player_spec_name) + " " +  specSeparator + " " +  entityPlayer.getName());
        playerSpecList.add(getString(R.string.activity_player_spec_age) + " " +  specSeparator + " " +  String.valueOf(entityPlayer.getAge()));
        playerSpecList.add(getString(R.string.activity_player_spec_city) + " " +  specSeparator + entityPlayer.getCity());
        playerSpecList.add(getString(R.string.activity_player_spec_height) + " " +  specSeparator + " " +  String.valueOf(entityPlayer.getHeight()));
        playerSpecList.add(getString(R.string.activity_player_spec_weight) + " " +  specSeparator + " " +  String.valueOf(entityPlayer.getWeight()));
        playerSpecList.add(getString(R.string.activity_player_spec_address) + " " +  specSeparator + " " +  entityPlayer.getAddress());
        playerSpecList.add(getString(R.string.activity_player_spec_experience) + " " +  specSeparator + " " +  entityPlayer.getExperience());
        playerSpecList.add(getString(R.string.activity_player_spec_head_coach) + " " +  specSeparator + " " +  entityPlayer.getCoachName());
        playerSpecList.add(getString(R.string.activity_player_spec_team) + " " +  specSeparator + " " +  entityPlayer.getTeamName());
        playerSpecList.add(getString(R.string.activity_player_spec_user_name) + " " +  specSeparator + " " +  entityPlayer.getUsername());
        playerSpecList.add(getString(R.string.activity_player_spec_game_post) + " " +  specSeparator + " " +  String.valueOf(entityPlayer.getPost()));
//        playerSpecList.add("" + entityPlayer.getProfileImage());


        playerSpecList.add(getString(R.string.activity_player_spec_telegram) + " " +  specSeparator + " " +  entityPlayer.getTelegramId());
        EntitySocialAcc entitySocialTelegram = new EntitySocialAcc();
        entitySocialTelegram.setId(0);
        entitySocialTelegram.setImageResId(R.drawable.ic_social_telegram);
        entitySocialTelegram.setType(EntitySocialAcc.SOCIAL_TYPE_TELEGRAM_USER);
        entitySocialTelegram.setLink("https://t.me/" + entityPlayer.getTelegramId());
        socialAccList.add(entitySocialTelegram);

        playerSpecList.add(getString(R.string.activity_player_spec_instagram) + " " +  specSeparator + " " +  entityPlayer.getInstagramId());
        EntitySocialAcc entitySocialInstagram = new EntitySocialAcc();
        entitySocialInstagram.setId(0);
        entitySocialInstagram.setImageResId(R.drawable.ic_social_instagram);
        entitySocialInstagram.setType(EntitySocialAcc.SOCIAL_TYPE_INSTAGRAM);
        entitySocialInstagram.setLink("https://instagram.com/_u/" + entityPlayer.getInstagramId());
        socialAccList.add(entitySocialInstagram);

        playerSpecList.add(getString(R.string.activity_player_spec_phone_number) + " " +  specSeparator + " " +  entityPlayer.getPhone());
        return playerSpecList;
    }

    private void initViews() {
        txtPlayerName = findViewById(R.id.txtPlayerName);
        btnReportPlayer = findViewById(R.id.btnReportPlayer);
        typeface = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));


        btnReportPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Redirect.sendToTelegram(ActivityPlayer.this, REPORT_PLAYER_BOT + entityPlayer.getId());
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // to nothing yet
                }
            }
        });
    }


    ArrayList<EntitySocialAcc> socialAccList = new ArrayList<>();

    private void initRecyclerPlayerSpec(ArrayList<String> playerSpecList) {

        RecyclerView recyclerView = findViewById(R.id.recyclerPlayerSpec);
        AdapterPlayerSpecification adapterPlayerSpecification = new AdapterPlayerSpecification(playerSpecList, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterPlayerSpecification);

        GridLayoutManager glm = new GridLayoutManager(this, 2);
        glm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(glm);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initRecyclerSocialAcc() {

        RecyclerView recyclerSocialAcc = findViewById(R.id.recyclerSocialAcc);
        AdapterSocialAcc adapterSocialAcc = new AdapterSocialAcc(socialAccList, this);
        recyclerSocialAcc.setNestedScrollingEnabled(false);
        recyclerSocialAcc.setAdapter(adapterSocialAcc);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerSocialAcc.setLayoutManager(llm);

        recyclerSocialAcc.setItemAnimator(new DefaultItemAnimator());

    }

}
