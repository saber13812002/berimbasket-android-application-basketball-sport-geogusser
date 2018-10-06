package ir.berimbasket.app.ui.home.match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchFragment extends Fragment implements MatchAdapter.MatchListListener {

    private static final int PAGE_COUNT = 20;
    private boolean loading;
    private boolean isLastPage;
    private int from;

    private MatchAdapter adapter;
    private ProgressBar circularProgressBar, horizontalProgressBar;
    private LinearLayoutManager layoutManager;

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
        circularProgressBar = view.findViewById(R.id.progressMatch);
        circularProgressBar.setVisibility(View.VISIBLE);
        horizontalProgressBar = view.findViewById(R.id.progressBarHorizontal);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMatch);

        Context context = view.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MatchAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(scrollListener);

        loadMatchList(0, PAGE_COUNT);
        from += PAGE_COUNT;

        return view;
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (lastVisibleItemPosition == adapter.getItemCount() - 1 && !loading && !isLastPage) {
                horizontalProgressBar.setVisibility(View.VISIBLE);
                loading = true;
                loadMatchList(from, PAGE_COUNT);
                Log.d("VarzeshBoard", "loading more");
                from += PAGE_COUNT;
            }
        }
    };

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

    private void loadMatchList(int from, int num) {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        String lang = LocaleManager.getLocale(getContext()).getLanguage();
        WebApiClient.getMatchApi(getContext()).getMatchesV2(from, num, "json", pusheId, userName, lang).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                loading = false;
                horizontalProgressBar.setVisibility(View.INVISIBLE);
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Match> matches = response.body();
                    if (matches != null && getView() != null && matches.size() != 0) {
                        adapter.addItems(matches);
                    } else {
                        isLastPage = true;
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                loading = false;
                circularProgressBar.setVisibility(View.INVISIBLE);
                horizontalProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
