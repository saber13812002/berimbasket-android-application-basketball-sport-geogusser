package ir.berimbasket.app.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterMatch;
import ir.berimbasket.app.adapter.AdapterPlayer;
import ir.berimbasket.app.adapter.AdapterStadium;
import ir.berimbasket.app.api.WebApiClient;
import ir.berimbasket.app.api.model.Player;
import ir.berimbasket.app.entity.EntityMatchScore;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohammad hosein on 5/1/2017.
 */

public class FragmentHome extends Fragment implements View.OnClickListener {

    private AppCompatButton btnMorePlayer, btnMoreStadium, btnMoreMatch;
    private ProgressBar progressHome;
    private static String STADIUM_URL = "https://berimbasket.ir/bball/getPlayGroundJson.php";
    private static String MATCH_URL = "https://berimbasket.ir/bball/getScore.php";
    private static String MORE_MATCH_URL = "http://www.espn.com/nba/scoreboard";
    private static String MORE_PLAYER_URL = "https://berimbasket.ir/bball/www/players.php";
    private static String MORE_STADIUM_URL = "https://berimbasket.ir/bball/www/mains.php";
    private static final String STADIUM_PHOTO_BASE_URL = "https://berimbasket.ir/bball/bots/playgroundphoto/";
    private int processCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnMoreStadium = view.findViewById(R.id.btnMoreStadium);
        btnMoreMatch = view.findViewById(R.id.btnMorePlayer);
        btnMorePlayer = view.findViewById(R.id.btnMoreMatch);
        progressHome = view.findViewById(R.id.progressHome);

        btnMoreMatch.setOnClickListener(this);
        btnMorePlayer.setOnClickListener(this);
        btnMoreStadium.setOnClickListener(this);

        initPlayerList();
        new GetStadium().execute();
        new GetMatch().execute();

        return view;
    }

    private void initPlayerList() {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        progressHome.setVisibility(View.VISIBLE);
        WebApiClient.getPlayerApi().getPlayers(0, pusheId, userName).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Player> players = response.body();
                    if (players != null && getView() != null) {
                        progressCancel();
                        setupPlayerRecycler(getView(), players);
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {

            }
        });
    }

    private void initStadiumList() {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        progressHome.setVisibility(View.VISIBLE);
        WebApiClient.getPlayerApi().getPlayers(0, pusheId, userName).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Player> players = response.body();
                    if (players != null && getView() != null) {
                        progressCancel();
                        setupPlayerRecycler(getView(), players);
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_fragment_home));
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btnMorePlayer:
                Redirect.sendToCustomTab(getActivity(), MORE_PLAYER_URL);
                break;
            case R.id.btnMoreMatch:
                Redirect.sendToCustomTab(getActivity(), MORE_MATCH_URL);
                break;
            case R.id.btnMoreStadium:
                Redirect.sendToCustomTab(getActivity(), MORE_STADIUM_URL);
                break;
        }
    }

    private void setupMatchRecyclerView(View view, ArrayList<EntityMatchScore> matchList) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMatchBoard);
        AdapterMatch adapterMatch = new AdapterMatch(getActivity(), view.getContext(), matchList);
        recyclerView.setAdapter(adapterMatch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setupPlayerRecycler(View view, List<Player> playerList) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerPlayer);
        AdapterPlayer adapterPlayer = new AdapterPlayer(playerList, view.getContext(), getActivity());
        recyclerView.setAdapter(adapterPlayer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupStadiumRecycler(View view, ArrayList<EntityStadium> stadiumList) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerStadium);
        AdapterStadium adapterStadium = new AdapterStadium(stadiumList, view.getContext());
        recyclerView.setAdapter(adapterStadium);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void progressCancel() {
        processCount++;
        if (processCount == 3) {
            progressHome.setVisibility(View.INVISIBLE);
        }
    }

    ArrayList<EntityStadium> stadiumList = new ArrayList<>();

    private class GetStadium extends AsyncTask<Void, Void, Void> {

        private String pusheId;
        private String userName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHome.setVisibility(View.VISIBLE);
            pusheId = Pushe.getPusheId(getContext());
            userName = new PrefManager(getContext()).getUserName();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String urlParams = String.format("id=0&pusheid=%s&username=%s", pusheId, userName);
            String jsonStr = sh.makeServiceCall(STADIUM_URL + "?" + urlParams);
            if (jsonStr != null) {
                try {
                    JSONArray locations = new JSONArray(jsonStr);

                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String latitude = c.getString("PlaygroundLatitude");
                        String longitude = c.getString("PlaygroundLongitude");
                        String type = c.getString("PlaygroundType");
                        String zoomLevel = c.getString("ZoomLevel");
                        String address = c.getString("address");
                        String images = c.getString("PgImages");
                        String thumb = null;
                        if (images.contains("png")) {
                            thumb = STADIUM_PHOTO_BASE_URL + images.split(".png")[0] + ".png";
                        } else {
                            thumb = STADIUM_PHOTO_BASE_URL + images.split(".jpg")[0] + ".jpg";
                        }
                        String instagramId = c.getString("PgInstagramId");
                        String telegramChannelId = c.getString("PgTlgrmChannelId");
                        String telegramGroupId = c.getString("PgTlgrmGroupJoinLink");
                        String telegramAdminId = c.getString("PgTlgrmGroupAdminId");

                        EntityStadium entityStadium = new EntityStadium();

                        entityStadium.setId(id != "null" ? Integer.parseInt(id) : -1);
                        entityStadium.setTitle(title);
                        entityStadium.setLatitude(latitude);
                        entityStadium.setLongitude(longitude);
                        entityStadium.setAddress(address);
                        entityStadium.setTelegramGroupId(telegramGroupId);
                        entityStadium.setTelegramChannelId(telegramChannelId);
                        entityStadium.setTelegramAdminId(telegramAdminId);
                        entityStadium.setInstagramId(instagramId);
                        entityStadium.setImages(images.split("\\.[a-z]{3}"));
                        if (images.contains("png")){
                            entityStadium.setImageType(EntityStadium.IMAGE_TYPE_PNG);
                        } else if (images.contains("jpg")) {
                            entityStadium.setImageType(EntityStadium.IMAGE_TYPE_JPG);
                        }
                        entityStadium.setThumbnail(thumb);
                        entityStadium.setType(type);
                        entityStadium.setZoomLevel(zoomLevel != "null" ? Integer.parseInt(zoomLevel) : -1);

                        stadiumList.add(entityStadium);


                    }
                } catch (final JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (getView() != null) {
                progressCancel();
                setupStadiumRecycler(getView(), stadiumList);
            }
        }
    }


    ArrayList<EntityMatchScore> matchList = new ArrayList<>();

    private class GetMatch extends AsyncTask<Void, Void, Void> {

        private String userName;
        private String pusheId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHome.setVisibility(View.VISIBLE);
            pusheId = Pushe.getPusheId(getContext());
            userName = new PrefManager(getContext()).getUserName();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String urlParams = String.format("pusheid=%s&username=%s", pusheId, userName);
            String jsonStr = sh.makeServiceCall(MATCH_URL + "?" + urlParams);
            if (jsonStr != null) {
                try {
                    JSONArray locations = new JSONArray(jsonStr);

                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String homeName = c.getString("teamTitleA");
                        String awayName = c.getString("teamTitleB");
                        String homeLogo = c.getString("logoTitleA");
                        String awayLogo = c.getString("logoTitleB");
                        String homeScore = c.getString("scoreA");
                        String awayScore = c.getString("scoreB");
                        String scoreDate = c.getString("scoreDate");
                        String status = c.getString("status");
                        String link = c.getString("link");

                        EntityMatchScore entityMatchScore = new EntityMatchScore();

                        entityMatchScore.setId(Integer.parseInt(id));
                        entityMatchScore.setHomeName(homeName);
                        entityMatchScore.setAwayName(awayName);
                        entityMatchScore.setHomeLogo(homeLogo);
                        entityMatchScore.setAwayLogo(awayLogo);
                        entityMatchScore.setHomeScore(Integer.parseInt(homeScore));
                        entityMatchScore.setAwayScore(Integer.parseInt(awayScore));
                        entityMatchScore.setStatus(status);
                        entityMatchScore.setScoreDate(scoreDate);
                        entityMatchScore.setLink(link);


                        matchList.add(entityMatchScore);


                    }
                } catch (final JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressHome.setVisibility(View.INVISIBLE);
            if (getView() != null) {
                progressCancel();
                setupMatchRecyclerView(getView(), matchList);
            }
        }
    }
}
