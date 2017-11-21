package ir.berimbasket.app.activity.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterMatch;
import ir.berimbasket.app.adapter.AdapterPlayer;
import ir.berimbasket.app.adapter.AdapterStadium;
import ir.berimbasket.app.entity.EntityMatchScore;
import ir.berimbasket.app.entity.EntityPlayer;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;

/**
 * Created by mohammad hosein on 5/1/2017.
 */

public class FragmentHome extends Fragment implements View.OnClickListener {

    private AppCompatButton btnMorePlayer, btnMoreStadium, btnMoreMatch;
    private ProgressBar progressHome;
    private static String PLAYER_URL = "https://berimbasket.ir/bball/getPlayers.php";
    private static String STADIUM_URL = "https://berimbasket.ir/bball/getPlayGroundJson.php";
    private static String MATCH_URL = "https://berimbasket.ir/bball/getScore.php";
    private static String MORE_MATCH_URL = "https://berimbasket.ir/bball/www/plays.php";
    private static String MORE_PLAYER_URL = "https://berimbasket.ir/bball/www/players.php";
    private static String MORE_STADIUM_URL = "https://berimbasket.ir/bball/www/mains.php";
    private int processCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupPlayerRecycler(view, playerList);
        btnMoreStadium = view.findViewById(R.id.btnMoreStadium);
        btnMoreMatch = view.findViewById(R.id.btnMorePlayer);
        btnMorePlayer = view.findViewById(R.id.btnMoreMatch);
        progressHome = view.findViewById(R.id.progressHome);

        btnMoreMatch.setOnClickListener(this);
        btnMorePlayer.setOnClickListener(this);
        btnMoreStadium.setOnClickListener(this);

        new GetPlayers().execute();
        new GetStadium().execute();
        new GetMatch().execute();

        return view;
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
        AdapterMatch adapterMatch = new AdapterMatch(view.getContext(), matchList);
        recyclerView.setAdapter(adapterMatch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setupPlayerRecycler(View view, ArrayList<EntityPlayer> playerList) {
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

    private ArrayList<EntityPlayer> playerList = new ArrayList<>();

    private class GetPlayers extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHome.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String pusheId = Pushe.getPusheId(getContext());
            String userName = new PrefManager(getContext()).getUserName();
            String urlParams = String.format("id=0&pusheid=%s&username=%s", pusheId, userName);
            String jsonStr = sh.makeServiceCall(PLAYER_URL + "?" + urlParams);
            if (jsonStr != null) {
                try {
                    JSONArray locations = new JSONArray(jsonStr);

                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String username = c.getString("username");
                        String namefa = c.getString("namefa");
                        String address = c.getString("address");
                        String uImage = c.getString("uImages");
                        String uInstagramId = c.getString("uInstagramId");
                        String uTelegramId = c.getString("uTelegramlId");
                        String height = c.getString("height");
                        String weight = c.getString("weight");
                        String city = c.getString("city");
                        String age = c.getString("age");
                        String coach = c.getString("coach");
                        String teamname = c.getString("teamname");
                        String experience = c.getString("experience");
                        String post = c.getString("post");
                        String telegramPhone = c.getString("telegramphone");

                        Log.i("name", id);

                        EntityPlayer entityPlayer = new EntityPlayer();
                        // adding each child node to HashMap key => value

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

                        playerList.add(entityPlayer);


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
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            progressCancel();
            setupPlayerRecycler(getView(), playerList);
        }
    }

    private void progressCancel() {
        processCount++;
        if (processCount == 3) {
            progressHome.setVisibility(View.INVISIBLE);
        }
    }

    ArrayList<EntityStadium> stadiumList = new ArrayList<>();

    private class GetStadium extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHome.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String pusheId = Pushe.getPusheId(getContext());
            String userName = new PrefManager(getContext()).getUserName();
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
                        entityStadium.setImages(images.split(".jpg"));
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
            progressCancel();
            setupStadiumRecycler(getView(), stadiumList);
        }
    }


    ArrayList<EntityMatchScore> matchList = new ArrayList<>();

    private class GetMatch extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHome.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String pusheId = Pushe.getPusheId(getContext());
            String userName = new PrefManager(getContext()).getUserName();
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
                        String date = c.getString("date");

                        EntityMatchScore entityMatchScore = new EntityMatchScore();

                        entityMatchScore.setId(Integer.parseInt(id));
                        entityMatchScore.setHomeName(homeName);
                        entityMatchScore.setAwayName(awayName);
                        entityMatchScore.setHomeLogo(homeLogo);
                        entityMatchScore.setAwayLogo(awayLogo);
                        entityMatchScore.setHomeScore(Integer.parseInt(homeScore));
                        entityMatchScore.setAwayScore(Integer.parseInt(awayScore));
                        entityMatchScore.setDate(date);


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
            progressCancel();
            setupMatchRecyclerView(getView(), matchList);
        }
    }
}
