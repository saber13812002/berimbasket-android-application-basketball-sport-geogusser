package ir.berimbasket.app.activity.fragment;


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

import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterMission;
import ir.berimbasket.app.entity.EntityMission;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.PrefManager;

public class FragmentMissions extends Fragment {

    private String MISSION_URL = "http://berimbasket.ir/bball/getMission.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_missions, container, false);
        new GetMissions().execute();
        return view;
    }

    private void initRecyclerMission(View view, ArrayList<EntityMission> missionList) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMissions);
        recyclerView.setNestedScrollingEnabled(false);
        AdapterMission adapterPlayer = new AdapterMission(missionList, view.getContext());
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
        return MISSION_URL + "?user=" + getActiveUsername();
    }

    private String getActiveUsername() {
        PrefManager pref = new PrefManager(getContext());
        return pref.getUserName();
    }
}
