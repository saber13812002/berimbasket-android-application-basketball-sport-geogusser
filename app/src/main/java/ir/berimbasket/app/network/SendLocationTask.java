package ir.berimbasket.app.network;

import android.os.AsyncTask;

import ir.berimbasket.app.entity.EntityLocation;

/**
 * Created by Mahdi on 9/20/2017.
 * send user location to server
 */

public class SendLocationTask extends AsyncTask<EntityLocation, Void, Void> {
    @Override
    protected Void doInBackground(EntityLocation... params) {
        // TODO: 9/20/2017 send user location with specified webservice here
        return null;
    }
}
