package ir.berimbasket.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;

public class ActivityBrowser extends Activity {

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
            Intent intent = new Intent(ActivityBrowser.this, ActivityHome.class);
            startActivity(intent);
            Redirect.sendToCustomTab(ActivityBrowser.this, link);
            ActivityBrowser.this.finish();
        }
    }
}
