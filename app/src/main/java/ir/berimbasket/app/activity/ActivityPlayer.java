package ir.berimbasket.app.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterPlayerSpecification;
import ir.berimbasket.app.adapter.AdapterSocialAcc;
import ir.berimbasket.app.api.WebApiClient;
import ir.berimbasket.app.api.model.Player;
import ir.berimbasket.app.entity.EntitySocialAcc;
import ir.berimbasket.app.exception.UnknownTelegramURL;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.BaseActivity;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPlayer extends BaseActivity {

    // TODO: 12/13/2017 this class breaks oop rules (there are so many bound in fields and methods) 

    private static final String REPORT_PLAYER_BOT = "https://t.me/berimbasketreportbot?start=";

    private ProgressBar progress;
    private TextView txtPlayerName;
    private ImageView btnReportPlayer;
    private ImageView imgProfileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_player);

        // init views
        txtPlayerName = findViewById(R.id.txtPlayerName);
        progress = findViewById(R.id.progressPlayer);
        btnReportPlayer = findViewById(R.id.btnReportPlayer);
        imgProfileImageView = findViewById(R.id.imgPlayerProfile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgProfileImageView.setTransitionName("image");
            txtPlayerName.setTransitionName("name");
        }

        // init toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init content
        String pushePlayerId = getIntent().getStringExtra("pushe_activity_extra");
        if (pushePlayerId != null) {
            txtPlayerName.setText(R.string.activity_player_txt_player_name_loading);
            initPlayer(Integer.parseInt(pushePlayerId), this);
        } else {
            Player entityPlayer = (Player) getIntent().getSerializableExtra("MyClass");
            loadActivity(entityPlayer);
        }

    }

    private void initPlayer(int id, Context context) {
        String pusheId = Pushe.getPusheId(context);
        String userName = new PrefManager(context).getUserName();
        progress.setVisibility(View.VISIBLE);
        WebApiClient.getPlayerApi().getPlayers(id, pusheId, userName).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Player> players = response.body();
                    if (players != null) {
                        loadActivity(players.get(0));
                    }
                } else {
                    // http call with incorrect params or other network error
                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void loadActivity(final Player player) {
        btnReportPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (player != null) {
                        Redirect.sendToTelegram(ActivityPlayer.this, REPORT_PLAYER_BOT + player.getId());
                    }
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // to nothing yet
                }
            }
        });

        String profilePicUrl = "https://berimbasket.ir/" + player.getProfileImage();
        Picasso.with(ActivityPlayer.this)
                .load(profilePicUrl)
                .resize(140, 140)
                .centerInside()
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into(imgProfileImageView);

        txtPlayerName.setText(player.getName());

        ArrayList<String> playerSpecList = getPlayerSpec(player);
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


    private ArrayList<String> getPlayerSpec(Player player) {

        ArrayList<String> playerSpecList = new ArrayList<>();
        String specSeparator = getString(R.string.activity_player_spec_separator);
        playerSpecList.add(getString(R.string.activity_player_spec_name) + " " + specSeparator + " " + player.getName());
        playerSpecList.add(getString(R.string.activity_player_spec_age) + " " + specSeparator + " " + String.valueOf(player.getAge()));
        playerSpecList.add(getString(R.string.activity_player_spec_city) + " " + specSeparator + player.getCity());
        playerSpecList.add(getString(R.string.activity_player_spec_height) + " " + specSeparator + " " + String.valueOf(player.getHeight()));
        playerSpecList.add(getString(R.string.activity_player_spec_weight) + " " + specSeparator + " " + String.valueOf(player.getWeight()));
        playerSpecList.add(getString(R.string.activity_player_spec_address) + " " + specSeparator + " " + player.getAddress());
        playerSpecList.add(getString(R.string.activity_player_spec_experience) + " " + specSeparator + " " + player.getExperience());
        playerSpecList.add(getString(R.string.activity_player_spec_head_coach) + " " + specSeparator + " " + player.getCoachName());
        playerSpecList.add(getString(R.string.activity_player_spec_team) + " " + specSeparator + " " + player.getTeamName());
        playerSpecList.add(getString(R.string.activity_player_spec_user_name) + " " + specSeparator + " " + player.getUsername());
        playerSpecList.add(getString(R.string.activity_player_spec_game_post) + " " + specSeparator + " " + String.valueOf(player.getPost()));
//        playerSpecList.add("" + entityPlayer.getProfileImage());


        playerSpecList.add(getString(R.string.activity_player_spec_telegram) + " " + specSeparator + " " + player.getTelegramId());
        EntitySocialAcc entitySocialTelegram = new EntitySocialAcc();
        entitySocialTelegram.setId(0);
        entitySocialTelegram.setImageResId(R.drawable.ic_social_telegram);
        entitySocialTelegram.setType(EntitySocialAcc.SOCIAL_TYPE_TELEGRAM_USER);
        entitySocialTelegram.setLink("https://t.me/" + player.getTelegramId());
        socialAccList.add(entitySocialTelegram);

        playerSpecList.add(getString(R.string.activity_player_spec_instagram) + " " + specSeparator + " " + player.getInstagramId());
        EntitySocialAcc entitySocialInstagram = new EntitySocialAcc();
        entitySocialInstagram.setId(0);
        entitySocialInstagram.setImageResId(R.drawable.ic_social_instagram);
        entitySocialInstagram.setType(EntitySocialAcc.SOCIAL_TYPE_INSTAGRAM);
        entitySocialInstagram.setLink("https://instagram.com/_u/" + player.getInstagramId());
        socialAccList.add(entitySocialInstagram);

        playerSpecList.add(getString(R.string.activity_player_spec_phone_number) + " " + specSeparator + " " + player.getPhone());
        return playerSpecList;
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
