package ir.berimbasket.app.ui.home.profile.specification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Player;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.common.PlayerSpecificationAdapter;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecificationFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_specification, container, false);
        this.view = view;
        initPlayerList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

    private void initPlayerList() {
        PrefManager pref = new PrefManager(getContext());
        String pusheId = Pushe.getPusheId(getContext());
        String userName = pref.getUserName();
        int userId = pref.getUserId();
        String lang = LocaleManager.getLocale(getContext()).getLanguage();
        WebApiClient.getPlayerApi().getPlayers(userId, pusheId, userName, lang).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Player> players = response.body();
                    if (players != null && getView() != null) {
                        initRecyclerPlayerSpec(getPlayerSpec(players.get(0)));
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerPlayerSpec(ArrayList<String> playerSpecList) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerPlayerSpec);
        PlayerSpecificationAdapter playerSpecificationAdapter = new PlayerSpecificationAdapter(playerSpecList, getPlayerSpecKey(), getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(playerSpecificationAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private ArrayList<String> getPlayerSpec(Player entityPlayer) {

        ArrayList<String> playerSpecList = new ArrayList<>();
        String specSeparator = getString(R.string.fragment_player_spec_separator);
        playerSpecList.add(entityPlayer.getName());
        playerSpecList.add(String.valueOf(entityPlayer.getAge()));
        playerSpecList.add(entityPlayer.getCity());
        playerSpecList.add(String.valueOf(entityPlayer.getHeight()));
        playerSpecList.add(String.valueOf(entityPlayer.getWeight()));
        playerSpecList.add(entityPlayer.getAddress());
        playerSpecList.add(entityPlayer.getExperience());
        playerSpecList.add(entityPlayer.getCoachName());
        playerSpecList.add(entityPlayer.getTeamName());
        playerSpecList.add(entityPlayer.getUsername());
        playerSpecList.add(String.valueOf(entityPlayer.getPost()));
        //playerSpecList.add("" + entityPlayer.getProfileImage());
        playerSpecList.add(entityPlayer.getTelegramId());
        playerSpecList.add(entityPlayer.getInstagramId());
        playerSpecList.add(entityPlayer.getPhone());
        return playerSpecList;
    }

    private ArrayList<String> getPlayerSpecKey() {

        ArrayList<String> playerSpecListKey = new ArrayList<>();
        String specSeparator = getString(R.string.fragment_player_spec_separator);
        playerSpecListKey.add(getString(R.string.fragment_player_spec_name));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_age));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_city));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_height));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_weight));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_address));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_experience));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_head_coach));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_team));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_user_name));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_post));
        //playerSpecListKey.add("" + entityPlayer.getProfileImage());
        playerSpecListKey.add(getString(R.string.fragment_player_spec_telegram));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_instagram));
        playerSpecListKey.add(getString(R.string.fragment_player_spec_phone_number));
        return playerSpecListKey;
    }

}
