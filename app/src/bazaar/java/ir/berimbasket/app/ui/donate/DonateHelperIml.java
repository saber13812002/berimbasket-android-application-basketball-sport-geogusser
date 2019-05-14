package ir.berimbasket.app.ui.donate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ir.berimbasket.app.util.inapp.IabHelper;
import ir.berimbasket.app.util.inapp.IabResult;
import ir.berimbasket.app.util.inapp.Inventory;
import ir.berimbasket.app.util.inapp.Purchase;

public class DonateHelperIml implements DonateHelper{
    // Debug tag, for logging
    private static final String TAG = "BazaarDonate";

    private static final String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDWNyB00uWux6+0ofqicovKZ3KPn4weWSx0xnENELsXea7Xmy/FnPbfkiPKqoWInTp5iPWpHX//LGm2kMJ0rkGMJCZ1VzQBYGzPSNhmX2tIAHx0KXVyOaFiHVVbeim1s4DqAiMjBKd93hwJPAY7R+kD78nGsVP2O50EwdMSC9UGsQIwngV7zE525gwZu8iliVX5QtZGAHj7j/EaYfgCDME8w4rOzRdFa+rL6miGl8kCAwEAAQ==";

    private DonateCallback donateCallback;

    // The helper object
    IabHelper mHelper;

    @Override
    public void initialize(Context context) {
        // You can find it in your Bazaar console, in the Dealers section.
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
            mHelper.queryInventoryAsync(this::queryInventoryFinished); // check if consume wasn't failed and fix it here
        });
    }

    public void queryInventoryFinished(IabResult result, Inventory inventory) {
        // Have we been disposed of in the meantime? If so, quit.
        if (mHelper == null) return;

        Log.d(TAG, "Query inventory finished.");
        if (result.isFailure()) {
            Log.d(TAG, "Failed to query inventory: " + result);
            return;
        }
        else {
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
            mHelper.consumeAsync(inventory.getPurchase(sku), this::OnConsumeFinished);
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

        mHelper.launchPurchaseFlow(activity, donate.getSku(), donate.getRequestCode(), this::onIabPurchaseFinished);
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
            mHelper.consumeAsync(purchase, this::OnConsumeFinished);
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
            mHelper.dispose();
        }
        donateCallback = null;
        mHelper = null;
    }
}