package ir.berimbasket.app.ui.player;

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
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Player;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.common.PlayerSpecificationAdapter;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.Telegram;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerActivity extends BaseActivity implements SocialAccAdapter.SocialAccListener{

    // TODO: 12/13/2017 this class breaks oop rules (there are so many bound in fields and methods) 

    private static final String REPORT_PLAYER_BOT = "https://t.me/berimbasketreportbot?start=";
    private static final String EXTERNAL_WEB_PROFILE_URL = "http://berimbasket.ir/bball/www/player.php?id=";

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
        String lang = LocaleManager.getLocale(context).getLanguage();
        progress.setVisibility(View.VISIBLE);
        WebApiClient.getPlayerApi().getPlayers(id, pusheId, userName, lang).enqueue(new Callback<List<Player>>() {
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
                        Redirect.sendToTelegram(PlayerActivity.this, REPORT_PLAYER_BOT + player.getId(),
                                Telegram.DEFAULT_BOT);
                    }
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // to nothing yet
                }
            }
        });

        String profilePicUrl = "https://berimbasket.ir/" + player.getProfileImage();
        Picasso.with(PlayerActivity.this)
                .load(profilePicUrl)
                .resize(140, 140)
                .centerInside()
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into(imgProfileImageView);

        imgProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.sendToCustomTab(PlayerActivity.this, EXTERNAL_WEB_PROFILE_URL + player.getId());
            }
        });

        txtPlayerName.setText(player.getName());

        ArrayList<String> playerSpecList = getPlayerSpec(player);
        initRecyclerPlayerSpec(playerSpecList);
        initRecyclerSocialAcc();
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

        playerSpecList.add(player.getName());
        playerSpecList.add(String.valueOf(player.getAge()));
        playerSpecList.add(player.getCity());
        playerSpecList.add(String.valueOf(player.getHeight()));
        playerSpecList.add(String.valueOf(player.getWeight()));
        playerSpecList.add(player.getAddress());
        playerSpecList.add(player.getExperience());
        playerSpecList.add(player.getCoachName());
        playerSpecList.add(player.getTeamName());
        playerSpecList.add(player.getUsername());
        playerSpecList.add(String.valueOf(player.getPost()));
//        playerSpecList.add("" + entityPlayer.getProfileImage());


        playerSpecList.add(player.getTelegramId());
        SocialAccEntity entitySocialTelegram = new SocialAccEntity();
        entitySocialTelegram.setId(0);
        entitySocialTelegram.setImageResId(R.drawable.ic_social_telegram);
        entitySocialTelegram.setType(SocialAccEntity.SOCIAL_TYPE_TELEGRAM_USER);
        entitySocialTelegram.setLink("https://t.me/" + player.getTelegramId());
        socialAccList.add(entitySocialTelegram);

        playerSpecList.add(player.getInstagramId());
        SocialAccEntity entitySocialInstagram = new SocialAccEntity();
        entitySocialInstagram.setId(0);
        entitySocialInstagram.setImageResId(R.drawable.ic_social_instagram);
        entitySocialInstagram.setType(SocialAccEntity.SOCIAL_TYPE_INSTAGRAM);
        entitySocialInstagram.setLink("https://instagram.com/_u/" + player.getInstagramId());
        socialAccList.add(entitySocialInstagram);

        SocialAccEntity webExternalLink = new SocialAccEntity();
        webExternalLink.setId(0);
        webExternalLink.setImageResId(R.drawable.ic_external_link);
        webExternalLink.setType(SocialAccEntity.SOCIAL_TYPE_WEB_PROFILE);
        webExternalLink.setLink(EXTERNAL_WEB_PROFILE_URL + player.getId());
        socialAccList.add(webExternalLink);

        playerSpecList.add(player.getPhone());
        return playerSpecList;
    }

    private ArrayList<String> getPlayerSpecKey() {

        ArrayList<String> playerSpecListKey = new ArrayList<>();
        String specSeparator = getString(R.string.fragment_player_spec_separator);
        playerSpecListKey.add(getString(R.string.fragment_player_spec_name));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_age));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_city));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_height));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_weight));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_address));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_experience));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_head_coach));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_team));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_user_name));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_post));
        //playerSpecListKey.add("" + entityPlayer.getProfileImage());
        playerSpecListKey.add(getString(R.string.fragment_player_spec_telegram));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_instagram));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_phone_number));
        return playerSpecListKey;
    }

    ArrayList<SocialAccEntity> socialAccList = new ArrayList<>();

    private void initRecyclerPlayerSpec(ArrayList<String> playerSpecList) {

        RecyclerView recyclerView = findViewById(R.id.recyclerPlayerSpec);
        PlayerSpecificationAdapter playerSpecificationAdapter = new PlayerSpecificationAdapter(playerSpecList, getPlayerSpecKey(), this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(playerSpecificationAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initRecyclerSocialAcc() {

        RecyclerView recyclerSocialAcc = findViewById(R.id.recyclerSocialAcc);
        SocialAccAdapter socialAccAdapter = new SocialAccAdapter(socialAccList, this);
        recyclerSocialAcc.setNestedScrollingEnabled(false);
        recyclerSocialAcc.setAdapter(socialAccAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerSocialAcc.setLayoutManager(llm);

        recyclerSocialAcc.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onSocialItemClick(SocialAccEntity entity) {
        if (entity.getType() == SocialAccEntity.SOCIAL_TYPE_INSTAGRAM) {
            try {
                Redirect.sendToInstagram(getApplicationContext(), entity.getLink());  // https://instagram.com/_u/javaherisaber
            } catch (IllegalArgumentException unknownInstagramURL) {
                // do nothing yet
            }
        } else if (entity.getType() == SocialAccEntity.SOCIAL_TYPE_TELEGRAM_CHANNEL ||
                entity.getType() == SocialAccEntity.SOCIAL_TYPE_TELEGRAM_GROUP ||
                entity.getType() == SocialAccEntity.SOCIAL_TYPE_TELEGRAM_USER) {
            try {
                Redirect.sendToTelegram(getApplicationContext(), entity.getLink(), Telegram.CHAT);  // https://t.me/mamlekate
            } catch (IllegalArgumentException unknownTelegramURL) {
                // do nothing yet
            }
        } else if (entity.getType() == SocialAccEntity.SOCIAL_TYPE_WEB_PROFILE) {
            Redirect.sendToCustomTab(PlayerActivity.this, entity.getLink());
        }
    }
}
