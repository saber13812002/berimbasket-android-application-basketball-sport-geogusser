package ir.berimbasket.app.ui.home.main.match;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.entity.EntityMatchScore;
import ir.berimbasket.app.data.network.HttpFunctions;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.Redirect;

public class MatchFragment extends Fragment implements MatchAdapter.MatchListListener {

    private final static String MATCH_URL = "https://berimbasket.ir/bball/getScore.php";
    private MatchAdapter adapter;
    private ProgressBar progress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        progress = view.findViewById(R.id.progressMatch);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMatch);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MatchAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        new GetMatch().execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

    @Override
    public void onMatchItemClick(EntityMatchScore matchScore) {
        Redirect.sendToCustomTab(getActivity(), matchScore.getLink());
    }

    private class GetMatch extends AsyncTask<Void, Void, List<EntityMatchScore>> {

        private String userName;
        private String pusheId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            pusheId = Pushe.getPusheId(getContext());
            userName = new PrefManager(getContext()).getUserName();
        }

        @Override
        protected List<EntityMatchScore> doInBackground(Void... voids) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String urlParams = String.format("pusheid=%s&username=%s", pusheId, userName);
            String jsonStr = sh.makeServiceCall(MATCH_URL + "?" + urlParams);
            ArrayList<EntityMatchScore> matchList = new ArrayList<>();
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
                    // do nothing yet
                }
            }
            return matchList;
        }

        @Override
        protected void onPostExecute(List<EntityMatchScore> result) {
            super.onPostExecute(result);
            progress.setVisibility(View.INVISIBLE);
            if (getView() != null) {
                adapter.swapDataSource(result);
            }
        }
    }
}
