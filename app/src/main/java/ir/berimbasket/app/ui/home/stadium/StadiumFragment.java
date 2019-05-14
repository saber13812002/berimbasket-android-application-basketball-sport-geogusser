package ir.berimbasket.app.ui.home.stadium;

import android.content.Context;
import android.content.Intent;
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
import ir.berimbasket.app.data.network.model.Stadium;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.service.GPSTracker;
import ir.berimbasket.app.ui.common.DismissableCallback;
import ir.berimbasket.app.ui.common.model.DismissibleInfo;
import ir.berimbasket.app.ui.common.model.StadiumBase;
import ir.berimbasket.app.ui.contact.DeveloperContactActivity;
import ir.berimbasket.app.ui.stadium.StadiumActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StadiumFragment extends Fragment implements StadiumAdapter.StadiumListListener, DismissableCallback {

    private static final int PAGE_COUNT = 20;
    private boolean loading;
    private boolean isLastPage;
    private int from;

    private StadiumAdapter adapter;
    private ProgressBar circularProgressBar, horizontalProgressBar;
    private LinearLayoutManager layoutManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StadiumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stadium_list, container, false);
        circularProgressBar = view.findViewById(R.id.progressStadium);
        circularProgressBar.setVisibility(View.VISIBLE);
        horizontalProgressBar = view.findViewById(R.id.progressBarHorizontal);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerStadium);

        Context context = view.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StadiumAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(scrollListener);

        // initialize first page
        addDismissibleToList();
        loadStadiumList(0, PAGE_COUNT);
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
                loadStadiumList(from, PAGE_COUNT);
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
    public void onStadiumItemClick(Stadium stadium) {
        Intent intent = new Intent(getContext(), StadiumActivity.class);
        StadiumBase stadiumBase = new StadiumBase(stadium.getId(), stadium.getTitle(),
                stadium.getLatitude(), stadium.getLongitude());
        intent.putExtra("stadiumDetail", stadiumBase);
        startActivity(intent);
    }

    @Override
    public void onDismissibleActionClick(DismissibleInfo dismissibleInfo) {
        Intent intent = new Intent(getContext(), DeveloperContactActivity.class);
        adapter.removeTop();
        new PrefManager(getContext()).putStartMessagePassed(true);
        startActivity(intent);
    }

    @Override
    public void onDismissibleSkipClick(DismissibleInfo dismissibleInfo) {
        new PrefManager(getContext()).putStartMessagePassed(true);
        adapter.removeTop();
    }

    private void loadStadiumList(int from, int num) {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        String lang = LocaleManager.getLocale(getContext()).getLanguage();
        String latitude = "0";
        String longitude = "0";
        GPSTracker gps = new GPSTracker(getActivity());
        // Check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
        }

        WebApiClient.getStadiumApi(getContext()).getStadiumsV2List(latitude, longitude, from, num, "json", pusheId, userName, lang).enqueue(new Callback<List<Stadium>>() {
            @Override
            public void onResponse(Call<List<Stadium>> call, Response<List<Stadium>> response) {
                loading = false;
                horizontalProgressBar.setVisibility(View.INVISIBLE);
                if (circularProgressBar.getVisibility() == View.VISIBLE) {
                    circularProgressBar.setVisibility(View.GONE);
                }
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Stadium> stadiums = response.body();
                    if (stadiums != null && getView() != null && stadiums.size() != 0) {
                        adapter.addStadiums(stadiums);
                    } else {
                        isLastPage = true;
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Stadium>> call, Throwable t) {
                loading = false;
                circularProgressBar.setVisibility(View.INVISIBLE);
                horizontalProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void addDismissibleToList() {
        if (!new PrefManager(getContext()).getStartMessagePassed()) {
            DismissibleInfo info = new DismissibleInfo(getString(R.string.general_dismissible_start_header),
                    getString(R.string.general_dismissible_start_message),
                    getString(R.string.general_dismissible_start_action),
                    getString(R.string.general_dismissible_start_skip));
            adapter.addToTop(info);
        }
    }
}
