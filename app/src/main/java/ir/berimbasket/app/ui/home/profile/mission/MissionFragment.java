package ir.berimbasket.app.ui.home.profile.mission;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.HttpURLConnection;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Mission;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_missions, container, false);
        initMissions();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

    private void initRecyclerMission(View view, List<Mission> missionList) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMissions);
        recyclerView.setNestedScrollingEnabled(false);
        MissionAdapter adapterPlayer = new MissionAdapter(missionList, view.getContext());
        recyclerView.setAdapter(adapterPlayer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initMissions() {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        String lang = LocaleManager.getLocale(getContext()).getLanguage();
        WebApiClient.getMissionApi(getContext()).getMissions(userName, userName, pusheId, lang).enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Mission> missions = response.body();
                    if (missions != null && getView() != null) {
                        initRecyclerMission(getView(), missions);
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {

            }
        });
    }
}
