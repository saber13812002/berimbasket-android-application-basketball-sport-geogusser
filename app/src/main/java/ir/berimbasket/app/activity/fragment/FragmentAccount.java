package ir.berimbasket.app.activity.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.berimbasket.app.Adapter.AdapterXp;
import ir.berimbasket.app.R;
import ir.berimbasket.app.bundle.BundleXp;
import ir.berimbasket.app.json.HttpHandler;

/**
 * Created by mohammad hosein on 5/1/2017.
 */

public class FragmentAccount extends Fragment {

    TextView txtAccName, txtAccBadge, txtAccLevel, txtAccXp;
    private String _URL = "http://imenservice.com/bball/getMission.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/yekan.ttf");

        txtAccName = (TextView) view.findViewById(R.id.txtAccName);
        txtAccLevel = (TextView) view.findViewById(R.id.txtAccLevel);
        txtAccBadge = (TextView) view.findViewById(R.id.txtAccBadge);
        txtAccXp = (TextView) view.findViewById(R.id.txtAccXp);

        txtAccName.setTypeface(typeface);
        txtAccLevel.setTypeface(typeface);
        txtAccBadge.setTypeface(typeface);
        txtAccXp.setTypeface(typeface);

        new GetMissions().execute();

        return view;
    }

    private void setupXpRecycler(View view, ArrayList<BundleXp> missionList){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerXp);
        recyclerView.setNestedScrollingEnabled(false);
        AdapterXp adapterPlayer = new AdapterXp(missionList, view.getContext());
        recyclerView.setAdapter(adapterPlayer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    ArrayList<BundleXp> missionList = new ArrayList<>();

    private class GetMissions extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler(HttpHandler.RequestType.GET);

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

                        BundleXp bundleXp = new BundleXp();
                        // adding each child node to HashMap key => value

                        bundleXp.setId(id != "null" ? Integer.parseInt(id) : -1);
                        bundleXp.setTitle(title);
                        bundleXp.setLink(link);
                        bundleXp.setScore(score != "null" ? Integer.parseInt(score) : -1);
                        bundleXp.setLevel(level != "null" ? Integer.parseInt(level) : -1);
                        bundleXp.setLock(lock != "null" ? Integer.parseInt(lock) : -1);

                        // adding contact to contact list
                        missionList.add(bundleXp);


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
