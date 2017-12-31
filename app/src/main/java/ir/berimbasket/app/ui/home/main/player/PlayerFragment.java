package ir.berimbasket.app.ui.home.main.player;

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
import ir.berimbasket.app.data.network.model.Player;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.player.PlayerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerFragment extends Fragment implements PlayerAdapter.PlayerListListener {

    private PlayerAdapter adapter;
    private ProgressBar progress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_list, container, false);
        progress = view.findViewById(R.id.progressPlayer);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerPlayer);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PlayerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        initPlayerList();

        return view;
    }

    @Override
    public void onPlayerItemClick(Player player) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra("MyClass", player);
        startActivity(intent);
    }

    private void initPlayerList() {
        String pusheId = Pushe.getPusheId(getContext());
        String userName = new PrefManager(getContext()).getUserName();
        progress.setVisibility(View.VISIBLE);
        WebApiClient.getPlayerApi().getPlayers(0, pusheId, userName).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                progress.setVisibility(View.INVISIBLE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Player> players = response.body();
                    if (players != null && getView() != null) {
                        adapter.swapDataSource(players);
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }
}
