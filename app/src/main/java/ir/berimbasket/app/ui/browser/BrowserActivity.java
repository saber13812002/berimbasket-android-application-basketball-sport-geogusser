package ir.berimbasket.app.ui.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.net.HttpURLConnection;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.GeneralIntent;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.home.HomeActivity;
import ir.berimbasket.app.util.Redirect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        getLink(getApplicationContext());
    }

    private void getLink(final Context context) {
        PrefManager pref = new PrefManager(context);
        String userName = pref.getUserName();
        String pusheId = Pushe.getPusheId(context);
        WebApiClient.getGeneralIntentApi().getGeneralIntent(userName, pusheId).enqueue(new Callback<List<GeneralIntent>>() {
            @Override
            public void onResponse(Call<List<GeneralIntent>> call, Response<List<GeneralIntent>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<GeneralIntent> generalIntents = response.body();
                    if (generalIntents != null) {
                        generalIntentAction(BrowserActivity.this, generalIntents.get(0).getLink());
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<GeneralIntent>> call, Throwable t) {

            }
        });
    }

    private void generalIntentAction(Activity activity, String link) {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        Redirect.sendToCustomTab(activity, link);
        activity.finish();
    }
}
