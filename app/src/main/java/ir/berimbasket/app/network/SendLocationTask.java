package ir.berimbasket.app.network;

import android.os.AsyncTask;

import ir.berimbasket.app.entity.EntityLocation;

/**
 * Created by Mahdi on 9/20/2017.
 * send user location to server
 */

public class SendLocationTask extends AsyncTask<EntityLocation, Void, Void> {

    private final static String URL_SET_LOCATION = "https://berimbasket.ir/bball/setLoc.php";
    @Override
    protected Void doInBackground(EntityLocation... params) {
        HttpFunctions httpFuncs = new HttpFunctions(HttpFunctions.RequestType.GET);
        String url = URL_SET_LOCATION + String.format("?token=jkhfgkljhasfdlkh&lat=%s&long=%s&title=title"
                , params[0].getLatitude(), params[0].getLongitude());
        httpFuncs.makeServiceCall(url);
        return null;
    }
}
