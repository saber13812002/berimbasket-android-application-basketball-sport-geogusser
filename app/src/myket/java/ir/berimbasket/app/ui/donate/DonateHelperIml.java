package ir.berimbasket.app.ui.donate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ir.berimbasket.app.util.inapp.IabHelper;
import ir.berimbasket.app.util.inapp.IabResult;
import ir.berimbasket.app.util.inapp.Inventory;
import ir.berimbasket.app.util.inapp.Purchase;

public class DonateHelperIml implements DonateHelper {
    // Debug tag, for logging
    private static final String TAG = "MyketDonate";

    private static final String base64EncodedPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmd+y292Mxovj1Wrv7BGWoegu2nui+Y8WlZ3wKkkvT5EMCNCvazaQ/BQoBJxrU6nB0f+ogsagMoY80ZZt+rEhmUlV56I2+sLRvd7D81zbHvJ12g4U+8Y2NbH841J4IQoaY7cZazJrOFhjtA+5z5Il/Zqx+14A/iAlM6BYiwt00WQIDAQAB";

    private DonateCallback donateCallback;

    // The helper object
    IabHelper mHelper;

    @Override
    public void initialize(Context context) {
        // You can find it in your Myket console, in the Dealers section.
        // It is recommended to add more security than just pasting it in your source code;
        mHelper = new IabHelper(context, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(result -> {
            Log.d(TAG, "Setup finished.");

            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                Log.d(TAG, "Problem setting up In-app Billing: " + result);
            }
            // Hooray, IAB is fully set up!
            try {
                mHelper.queryInventoryAsync(this::queryInventoryFinished);
            } catch (IabHelper.IabAsyncInProgressException e) {
                Log.e(TAG, "Error querying inventory. Another async operation in progress.");
            }
        });
    }

    private void queryInventoryFinished(IabResult result, Inventory inventory) {
        // Have we been disposed of in the meantime? If so, quit.
        if (mHelper == null) return;

        Log.d(TAG, "Query inventory finished.");
        if (result.isFailure()) {
            Log.d(TAG, "Failed to query inventory: " + result);
            return;
        } else {
            Log.d(TAG, "Query inventory was successful.");

            String sku = Donate.PRICE_1_THOUSAND_TOMAN.getSku();
            checkForConsumable(inventory, sku);
            sku = Donate.PRICE_2_THOUSAND_TOMAN.getSku();
            checkForConsumable(inventory, sku);
            sku = Donate.PRICE_5_THOUSAND_TOMAN.getSku();
            checkForConsumable(inventory, sku);
            sku = Donate.PRICE_10_THOUSAND_TOMAN.getSku();
            checkForConsumable(inventory, sku);
        }
        Log.d(TAG, "Initial inventory query finished; enabling main UI.");
    }

    private void checkForConsumable(Inventory inventory, String sku) {
        // if we were disposed of in the meantime, quit.
        if (mHelper == null) return;

        if (inventory.hasPurchase(sku)) {
            try {
                mHelper.consumeAsync(inventory.getPurchase(sku), this::OnConsumeFinished);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void donate(Activity activity, Donate donate, DonateCallback callback) {
        this.donateCallback = callback;
        // if we were disposed of in the meantime, quit.
        if (mHelper == null) {
            donateCallback.onDonateFailure();
            return;
        }

        try {
            mHelper.launchPurchaseFlow(activity, donate.getSku(), donate.getRequestCode(), this::onIabPurchaseFinished);
        } catch (IabHelper.IabAsyncInProgressException e) {
            donateCallback.onDonateFailure();
        }
    }

    private void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        // if we were disposed of in the meantime, quit.
        if (mHelper == null) {
            if (donateCallback != null) {
                donateCallback.onDonateFailure();
            }
            return;
        }

        if (result.isFailure()) {
            Log.d(TAG, "Error purchasing: " + result);
            if (donateCallback != null) {
                donateCallback.onDonateFailure();
            }
        } else {
            // consume donate so that user can donate again later
            try {
                mHelper.consumeAsync(purchase, this::OnConsumeFinished);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    private void OnConsumeFinished(Purchase purchase, IabResult result) {
        if (result.isSuccess()) {
            // provision the in-app purchase to the user
            // Say thank you for your donate
            if (donateCallback != null) {
                donateCallback.onDonateSuccess();
            }
        }
    }

    @Override
    public boolean isActivityResultHandled(int requestCode, int resultCode, Intent data) {
        // if we were disposed of in the meantime, quit.
        if (mHelper == null) return true;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            return false; // do this on call site super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
            return true;
        }
    }

    @Override
    public void disposeService() {
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
        }
        donateCallback = null;
        mHelper = null;
    }
}
