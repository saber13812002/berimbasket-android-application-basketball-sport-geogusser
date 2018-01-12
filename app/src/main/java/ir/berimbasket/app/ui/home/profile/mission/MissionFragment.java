package ir.berimbasket.app.ui.home.profile.mission;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.entity.EntityMission;
import ir.berimbasket.app.data.network.HttpFunctions;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.util.AnalyticsHelper;

public class MissionFragment extends Fragment {

    private String MISSION_URL = "http://berimbasket.ir/bball/getMission.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_missions, container, false);
        new GetMissions().execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

    private void initRecyclerMission(View view, ArrayList<EntityMission> missionList) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMissions);
        recyclerView.setNestedScrollingEnabled(false);
        MissionAdapter adapterPlayer = new MissionAdapter(missionList, view.getContext());
        recyclerView.setAdapter(adapterPlayer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    ArrayList<EntityMission> missionList = new ArrayList<>();

    private class GetMissions extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(completeMissionUrl());
            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray locations = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String link = c.getString("link");
                        String score = c.getString("score");
                        String level = c.getString("level");
                        String lock = c.getString("lock");

                        Log.i("name", String.valueOf(Integer.parseInt(level)));

                        EntityMission entityMission = new EntityMission();

                        entityMission.setId(id != "null" ? Integer.parseInt(id) : -1);
                        entityMission.setTitle(title);
                        entityMission.setLink(link);
                        entityMission.setScore(score != "null" ? Integer.parseInt(score) : -1);
                        entityMission.setLevel(level != "null" ? Integer.parseInt(level) : -1);
                        entityMission.setIsLock(lock != "null" ? Integer.parseInt(lock) : -1);

                        missionList.add(entityMission);


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
            initRecyclerMission(getView(), missionList);
        }
    }

    private String completeMissionUrl() {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        String urlParams = String.format("pusheid=%s&username=%s", pusheId, userName);
        return MISSION_URL + "?user=" + userName + "&" + urlParams;
    }
}
