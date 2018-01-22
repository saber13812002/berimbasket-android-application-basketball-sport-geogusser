package ir.berimbasket.app.ui.home.main.stadium;

import android.content.Context;
import android.content.Intent;
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
import ir.berimbasket.app.data.network.model.Stadium;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.common.entity.StadiumBaseEntity;
import ir.berimbasket.app.ui.stadium.StadiumActivity;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StadiumFragment extends Fragment implements StadiumAdapter.StadiumListListener {


    private StadiumAdapter adapter;
    private ProgressBar progress;

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
        progress = view.findViewById(R.id.progressStadium);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerStadium);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StadiumAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        initStadiumList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

    @Override
    public void onStadiumItemClick(Stadium stadium) {
        Intent intent = new Intent(getContext(), StadiumActivity.class);
        StadiumBaseEntity stadiumBaseEntity = new StadiumBaseEntity(stadium.getId(), stadium.getTitle(),
                stadium.getLatitude(), stadium.getLongitude());
        intent.putExtra("stadiumDetail", stadiumBaseEntity);
        startActivity(intent);
    }

    private void initStadiumList() {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        String lang = LocaleManager.getLocale(getContext()).getLanguage();
        progress.setVisibility(View.VISIBLE);
        WebApiClient.getStadiumApi().getStadium(0, pusheId, userName, lang).enqueue(new Callback<List<Stadium>>() {
            @Override
            public void onResponse(Call<List<Stadium>> call, Response<List<Stadium>> response) {
                progress.setVisibility(View.INVISIBLE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Stadium> stadiums = response.body();
                    if (stadiums != null && getView() != null) {
                        adapter.swapDataSource(stadiums);
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Stadium>> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }
}
