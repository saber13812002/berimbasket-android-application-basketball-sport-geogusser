package ir.berimbasket.app.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivityLogin;
import ir.berimbasket.app.adapter.AdapterMission;
import ir.berimbasket.app.entity.EntityMission;
import ir.berimbasket.app.json.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mohammad hosein on 5/1/2017.
 */

public class FragmentProfile extends Fragment {

    TextView txtAccName, txtAccBadge, txtAccLevel, txtAccXp;
    private String _URL = "http://berimbasket.ir/bball/getMission.php";
    // FIXME: 9/22/2017 ship all SharedPreference to centralized PrefManager class (for ease and security reasons)
    private String PREFS_NAME = "BERIM_BASKET_PREF";
    private String ATTEMPT_LOGIN = "PREF_ATTEMPT_LOGIN";
    private static boolean isLoggedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = inflater.getContext();
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean(ATTEMPT_LOGIN, false);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/yekan.ttf");
        View rootView;
        if (isLoggedIn) {
            // user logged in
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            txtAccName = (TextView) rootView.findViewById(R.id.txtAccName);
            txtAccLevel = (TextView) rootView.findViewById(R.id.txtAccLevel);
            txtAccBadge = (TextView) rootView.findViewById(R.id.txtAccBadge);
            txtAccXp = (TextView) rootView.findViewById(R.id.txtAccXp);

            txtAccName.setTypeface(typeface);
            txtAccLevel.setTypeface(typeface);
            txtAccBadge.setTypeface(typeface);
            txtAccXp.setTypeface(typeface);

            new GetMissions().execute();
        } else {
            // user not logged in
            rootView = inflater.inflate(R.layout.fragment_profile_not_registered, container, false);
            TextView txtNotRegisteredMsg = (TextView) rootView.findViewById(R.id.txtFragmentProfile_userNotRegMsg);
            Button btnGoToLogin = (Button) rootView.findViewById(R.id.btnFragmentProfile_goToLogin);

            txtNotRegisteredMsg.setTypeface(typeface);
            btnGoToLogin.setTypeface(typeface);
            btnGoToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityLogin.class);
                    context.startActivity(intent);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView("Profile Fragment");
        // invalidate fragment when login status changes (eg. return from login activity)
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean loginStatus = preferences.getBoolean(ATTEMPT_LOGIN, false);
        if (loginStatus != isLoggedIn) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    private void setupXpRecycler(View view, ArrayList<EntityMission> missionList){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerXp);
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
            String jsonStr = sh.makeServiceCall(_URL);
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
                        // adding each child node to HashMap key => value

                        entityMission.setId(id != "null" ? Integer.parseInt(id) : -1);
                        entityMission.setTitle(title);
                        entityMission.setLink(link);
                        entityMission.setScore(score != "null" ? Integer.parseInt(score) : -1);
                        entityMission.setLevel(level != "null" ? Integer.parseInt(level) : -1);
                        entityMission.setLock(lock != "null" ? Integer.parseInt(lock) : -1);

                        // adding contact to contact list
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
            setupXpRecycler(getView(), missionList);
        }
    }
}
