package ir.berimbasket.app.ui.browser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.HttpFunctions;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.home.HomeActivity;
import ir.berimbasket.app.util.Redirect;

public class BrowserActivity extends BaseActivity {

    private static final String LINK_URL = "https://berimbasket.ir/bball/getWebPageLinkByGeneralIntentByPusheId.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        new GetLink().execute();
    }


    private class GetLink extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);

            PrefManager pref = new PrefManager(getApplicationContext());
            String pusheId = Pushe.getPusheId(getApplicationContext());
            String userName = pref.getUserName();
            String jsonStr = sh.makeServiceCall(LINK_URL + "?username=" + userName + "&pusheid=" + pusheId);
            String link = null;
            if (jsonStr != null) {
                try {
                    JSONArray locations = new JSONArray(jsonStr);
                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);
                        link = c.getString("link");
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
            return link;
        }

        @Override
        protected void onPostExecute(String link) {
            super.onPostExecute(link);
            Intent intent = new Intent(BrowserActivity.this, HomeActivity.class);
            startActivity(intent);
            Redirect.sendToCustomTab(BrowserActivity.this, link);
            BrowserActivity.this.finish();
        }
    }
}
