package ir.berimbasket.app.ui.home.profile.specification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Profile;
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
        String lang = LocaleManager.getLocale(getContext()).getLanguage();
        String token = "Bearer " + pref.getToken();
        WebApiClient.getProfileApi(getContext()).getMe(pusheId, userName, lang, token).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Profile me = response.body();
                    if (me != null && getView() != null) {
                        initRecyclerPlayerSpec(getPlayerSpec(me));
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

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

    private ArrayList<String> getPlayerSpec(Profile me) {

        ArrayList<String> playerSpecList = new ArrayList<>();
        String specSeparator = getString(R.string.fragment_player_spec_separator);
        playerSpecList.add(me.getName());
        playerSpecList.add(String.valueOf(me.getAge()));
        playerSpecList.add(me.getCity());
        playerSpecList.add(String.valueOf(me.getHeight()));
        playerSpecList.add(String.valueOf(me.getWeight()));
        // TODO: 7/8/2018 add this parameter in api
//        playerSpecList.add(me.getAddress());
        playerSpecList.add("");
        playerSpecList.add(me.getExperience());
        // TODO: 7/8/2018 add this parameter in api
//        playerSpecList.add(me.getCoachName());
        playerSpecList.add("");
        playerSpecList.add(me.getTeamName());
        playerSpecList.add(me.getSlug());
        playerSpecList.add(String.valueOf(me.getPost()));
        //playerSpecList.add("" + entityPlayer.getProfileImage());
        playerSpecList.add(me.getTelegram());
        playerSpecList.add(me.getInstagram());
        playerSpecList.add(me.getTelegramPhone());
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
