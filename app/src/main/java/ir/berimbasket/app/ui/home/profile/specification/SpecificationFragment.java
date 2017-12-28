package ir.berimbasket.app.ui.home.profile.specification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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

    private void initPlayerList() {
        PrefManager pref = new PrefManager(getContext());
        String pusheId = Pushe.getPusheId(getContext());
        String userName = pref.getUserName();
        int userId = pref.getUserId();
        WebApiClient.getPlayerApi().getPlayers(userId, pusheId, userName).enqueue(new Callback<List<Player>>() {
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
        PlayerSpecificationAdapter playerSpecificationAdapter = new PlayerSpecificationAdapter(playerSpecList, getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(playerSpecificationAdapter);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        glm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(glm);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private ArrayList<String> getPlayerSpec(Player entityPlayer) {

        ArrayList<String> playerSpecList = new ArrayList<>();
        String specSeparator = getString(R.string.fragment_player_spec_separator);
        playerSpecList.add(getString(R.string.fragment_player_spec_name) + " " + specSeparator + " " + entityPlayer.getName());
        playerSpecList.add(getString(R.string.fragment_player_spec_age) + " " + specSeparator + " " + String.valueOf(entityPlayer.getAge()));
        playerSpecList.add(getString(R.string.fragment_player_spec_city) + " " + specSeparator + " " + entityPlayer.getCity());
        playerSpecList.add(getString(R.string.fragment_player_spec_height) + " " + specSeparator + " " + String.valueOf(entityPlayer.getHeight()));
        playerSpecList.add(getString(R.string.fragment_player_spec_weight) + " " + specSeparator + " " + String.valueOf(entityPlayer.getWeight()));
        playerSpecList.add(getString(R.string.fragment_player_spec_address) + " " + specSeparator + " " + entityPlayer.getAddress());
        playerSpecList.add(getString(R.string.fragment_player_spec_experience) + " " + specSeparator + " " + entityPlayer.getExperience());
        playerSpecList.add(getString(R.string.fragment_player_spec_head_coach) + " " + specSeparator + " " + entityPlayer.getCoachName());
        playerSpecList.add(getString(R.string.fragment_player_spec_team) + " " + specSeparator + " " + entityPlayer.getTeamName());
        playerSpecList.add(getString(R.string.fragment_player_spec_user_name) + " " + specSeparator + " " + entityPlayer.getUsername());
        playerSpecList.add(getString(R.string.fragment_player_spec_post) + " " + specSeparator + " " + String.valueOf(entityPlayer.getPost()));
//        playerSpecList.add("" + entityPlayer.getProfileImage());
        playerSpecList.add(getString(R.string.fragment_player_spec_telegram) + " " + specSeparator + " " + entityPlayer.getTelegramId());
        playerSpecList.add(getString(R.string.fragment_player_spec_instagram) + " " + specSeparator + " " + entityPlayer.getInstagramId());
        playerSpecList.add(getString(R.string.fragment_player_spec_phone_number) + " " + specSeparator + " " + entityPlayer.getPhone());
        return playerSpecList;
    }


}
