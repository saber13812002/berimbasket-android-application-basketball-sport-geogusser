package ir.berimbasket.app.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterPlayerSpecification;
import ir.berimbasket.app.adapter.AdapterSocialAcc;
import ir.berimbasket.app.entity.EntityPlayer;
import ir.berimbasket.app.entity.EntitySocialAcc;
import ir.berimbasket.app.exception.UnknownTelegramURL;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;

public class ActivityPlayer extends AppCompatActivity {

    // TODO: 12/13/2017 this class breaks oop rules (there are so many bound in fields and methods) 

    private static final String REPORT_PLAYER_BOT = "https://t.me/berimbasketreportbot?start=";
    private static final String PLAYER_URL = "https://berimbasket.ir/bball/getPlayers.php";

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
        String pusheStadiumId = getIntent().getStringExtra("pushe_activity_extra");
        if (pusheStadiumId != null) {
            txtPlayerName.setText(R.string.activity_player_txt_player_name_loading);
            new GetPlayers().execute(pusheStadiumId);
        } else {
            EntityPlayer entityPlayer = (EntityPlayer) getIntent().getSerializableExtra("MyClass");
            loadActivity(entityPlayer);
        }

    }


    private void loadActivity(final EntityPlayer entityPlayer) {
        btnReportPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (entityPlayer != null) {
                        Redirect.sendToTelegram(ActivityPlayer.this, REPORT_PLAYER_BOT + entityPlayer.getId());
                    }
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // to nothing yet
                }
            }
        });

        String profilePicUrl = "https://berimbasket.ir/" + entityPlayer.getProfileImage();
        Picasso.with(ActivityPlayer.this)
                .load(profilePicUrl)
                .resize(140, 140)
                .centerInside()
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into(imgProfileImageView);

        txtPlayerName.setText(entityPlayer.getName());

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
        playerSpecList.add(getString(R.string.activity_player_spec_name) + " " + specSeparator + " " + entityPlayer.getName());
        playerSpecList.add(getString(R.string.activity_player_spec_age) + " " + specSeparator + " " + String.valueOf(entityPlayer.getAge()));
        playerSpecList.add(getString(R.string.activity_player_spec_city) + " " + specSeparator + entityPlayer.getCity());
        playerSpecList.add(getString(R.string.activity_player_spec_height) + " " + specSeparator + " " + String.valueOf(entityPlayer.getHeight()));
        playerSpecList.add(getString(R.string.activity_player_spec_weight) + " " + specSeparator + " " + String.valueOf(entityPlayer.getWeight()));
        playerSpecList.add(getString(R.string.activity_player_spec_address) + " " + specSeparator + " " + entityPlayer.getAddress());
        playerSpecList.add(getString(R.string.activity_player_spec_experience) + " " + specSeparator + " " + entityPlayer.getExperience());
        playerSpecList.add(getString(R.string.activity_player_spec_head_coach) + " " + specSeparator + " " + entityPlayer.getCoachName());
        playerSpecList.add(getString(R.string.activity_player_spec_team) + " " + specSeparator + " " + entityPlayer.getTeamName());
        playerSpecList.add(getString(R.string.activity_player_spec_user_name) + " " + specSeparator + " " + entityPlayer.getUsername());
        playerSpecList.add(getString(R.string.activity_player_spec_game_post) + " " + specSeparator + " " + String.valueOf(entityPlayer.getPost()));
//        playerSpecList.add("" + entityPlayer.getProfileImage());


        playerSpecList.add(getString(R.string.activity_player_spec_telegram) + " " + specSeparator + " " + entityPlayer.getTelegramId());
        EntitySocialAcc entitySocialTelegram = new EntitySocialAcc();
        entitySocialTelegram.setId(0);
        entitySocialTelegram.setImageResId(R.drawable.ic_social_telegram);
        entitySocialTelegram.setType(EntitySocialAcc.SOCIAL_TYPE_TELEGRAM_USER);
        entitySocialTelegram.setLink("https://t.me/" + entityPlayer.getTelegramId());
        socialAccList.add(entitySocialTelegram);

        playerSpecList.add(getString(R.string.activity_player_spec_instagram) + " " + specSeparator + " " + entityPlayer.getInstagramId());
        EntitySocialAcc entitySocialInstagram = new EntitySocialAcc();
        entitySocialInstagram.setId(0);
        entitySocialInstagram.setImageResId(R.drawable.ic_social_instagram);
        entitySocialInstagram.setType(EntitySocialAcc.SOCIAL_TYPE_INSTAGRAM);
        entitySocialInstagram.setLink("https://instagram.com/_u/" + entityPlayer.getInstagramId());
        socialAccList.add(entitySocialInstagram);

        playerSpecList.add(getString(R.string.activity_player_spec_phone_number) + " " + specSeparator + " " + entityPlayer.getPhone());
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

    private class GetPlayers extends AsyncTask<String, Void, Void> {

        EntityPlayer entityPlayer = new EntityPlayer();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... playerId) {

            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String pusheId = Pushe.getPusheId(getApplicationContext());
            String userName = new PrefManager(getApplicationContext()).getUserName();
            String urlParams = String.format("id=%s&pusheid=%s&username=%s",playerId[0], pusheId, userName);
            String jsonStr = sh.makeServiceCall(PLAYER_URL + "?" + urlParams);
            if (jsonStr != null) {
                try {
                    JSONArray rootArray = new JSONArray(jsonStr);

                    JSONObject player = rootArray.getJSONObject(0);

                    String id = player.getString("id");
                    String username = player.getString("username");
                    String namefa = player.getString("namefa");
                    String address = player.getString("address");
                    String uImage = player.getString("uImages");
                    String uInstagramId = player.getString("uInstagramId");
                    String uTelegramId = player.getString("uTelegramlId");
                    String height = player.getString("height");
                    String weight = player.getString("weight");
                    String city = player.getString("city");
                    String age = player.getString("age");
                    String coach = player.getString("coach");
                    String teamname = player.getString("teamname");
                    String experience = player.getString("experience");
                    String post = player.getString("post");
                    String telegramPhone = player.getString("telegramphone");

                    entityPlayer.setId(id != "null" ? Integer.parseInt(id) : -1);
                    entityPlayer.setUsername(username);
                    entityPlayer.setName(namefa);
                    entityPlayer.setAddress(address);
                    entityPlayer.setProfileImage(uImage);
                    entityPlayer.setInstagramId(uInstagramId);
                    entityPlayer.setTelegramId(uTelegramId);
                    entityPlayer.setHeight(height != "null" ? Integer.parseInt(height) : -1);
                    entityPlayer.setWeight(weight != "null" ? Integer.parseInt(weight) : -1);
                    entityPlayer.setCity(city);
                    entityPlayer.setAge(age != "null" ? Integer.parseInt(age) : -1);
                    entityPlayer.setCoachName(coach);
                    entityPlayer.setTeamName(teamname);
                    entityPlayer.setExperience(experience);
                    entityPlayer.setPhone(telegramPhone);

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setVisibility(View.INVISIBLE);
            loadActivity(this.entityPlayer);
        }
    }

}
