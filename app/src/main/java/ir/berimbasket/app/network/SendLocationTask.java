package ir.berimbasket.app.network;

import android.content.Context;
import android.os.AsyncTask;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.entity.EntityLocation;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;

/**
 * Created by Mahdi on 9/20/2017.
 * send user location to server
 */

public class SendLocationTask extends AsyncTask<EntityLocation, Void, Void> {

    private final static String URL_SET_LOCATION = "https://berimbasket.ir/bball/setLoc.php";
    @Override
    protected Void doInBackground(EntityLocation... params) {
        HttpFunctions httpFuncs = new HttpFunctions(HttpFunctions.RequestType.GET);
        Context context = ApplicationLoader.getInstance().getApplicationContext();
        if (context != null) {
            PrefManager pref = new PrefManager(context);
            String pusheId = Pushe.getPusheId(context);
            String userName = pref.getUserName();
            String url = URL_SET_LOCATION + String.format("?token=jkhfgkljhasfdlkh&lat=%s&long=%s&title=title&username=%s&pusheid=%s"
                    , params[0].getLatitude(), params[0].getLongitude(), userName, pusheId);
            httpFuncs.makeServiceCall(url);
        }
        return null;
    }
}
