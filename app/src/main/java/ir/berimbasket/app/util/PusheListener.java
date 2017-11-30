package ir.berimbasket.app.util;

import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

public class PusheListener extends PusheListenerService {
    @Override
    public void onMessageReceived(JSONObject message, JSONObject content) {
        /*try {
            String activityName = message.getString("ActivityName");
            String activityParameter = message.getString("ActivityParameter");
            Log.i("parameter", activityName);
            Log.i("parameter", activityParameter);
            if (activityName.equals("ActivityBrowser")) {
                Intent intent = new Intent(getApplicationContext(), ActivityBrowser.class);
                intent.putExtra("pushe_activity_extra", activityParameter);
                getApplicationContext().startActivity(intent);
            } else if (activityName.equals("ActivityPlayer")) {

            } else if (activityName.equals("ActivityStadium")) {

            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}