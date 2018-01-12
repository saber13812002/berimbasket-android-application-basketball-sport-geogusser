package ir.berimbasket.app.ui.home.main.match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.net.HttpURLConnection;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Match;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.Redirect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchFragment extends Fragment implements MatchAdapter.MatchListListener {

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

        initMatchList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

    @Override
    public void onMatchItemClick(Match matchScore) {
        Redirect.sendToCustomTab(getActivity(), matchScore.getLink());
    }

    private void initMatchList() {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        progress.setVisibility(View.VISIBLE);
        WebApiClient.getMatchApi().getMatches(pusheId, userName).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                progress.setVisibility(View.INVISIBLE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Match> matches = response.body();
                    if (matches != null && getView() != null) {
                        adapter.swapDataSource(matches);
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }
}
