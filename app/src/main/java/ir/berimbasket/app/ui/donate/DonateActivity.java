package ir.berimbasket.app.ui.donate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import ir.berimbasket.app.BuildConfig;
import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.util.Redirect;

public class DonateActivity extends BaseActivity {

    DonateHelper donateHelper = new DonateHelperIml();

    private Button btnDonate;
    private ProgressBar progress;
    private RadioGroup radioGroupPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        try {
            donateHelper.initialize(this);
        } catch (IllegalStateException e) {
            installStoreApp();
        }

        btnDonate = findViewById(R.id.button_activityDonate_pay);
        progress = findViewById(R.id.progressbar_activity_donate);
        radioGroupPrices = findViewById(R.id.radiogroup_activity_donate_prices);

        btnDonate.setOnClickListener(v ->
        {
            Donate donate;
            switch (radioGroupPrices.getCheckedRadioButtonId()) {
                case R.id.radiobutton_activity_donate_one:
                    donate = Donate.PRICE_1_THOUSAND_TOMAN;
                    break;
                case R.id.radiobutton_activity_donate_two:
                    donate = Donate.PRICE_2_THOUSAND_TOMAN;
                    break;
                case R.id.radiobutton_activity_donate_five:
                    donate = Donate.PRICE_5_THOUSAND_TOMAN;
                    break;
                case R.id.radiobutton_activity_donate_ten:
                    donate = Donate.PRICE_10_THOUSAND_TOMAN;
                    break;
                default:
                    Toast.makeText(this, getString(R.string.activity_donate_select_one_item_first), Toast.LENGTH_SHORT).show();
                    return;
            }
            flipProgressState();
            try {
                donateHelper.donate(this, donate, donateCallback);
            } catch (IllegalStateException e) {
                installStoreApp();
                flipProgressState();
            }
        });
    }

    private void installStoreApp() {
        if (BuildConfig.FLAVOR.equals(getString(R.string.general_flavor_name_myket))) {
            Redirect.sendToCustomTab(this, getString(R.string.general_myket_download_apk),
                    getString(R.string.general_dialog_message_myket_not_found), this);
        } else if (BuildConfig.FLAVOR.equals(getString(R.string.general_flavor_name_bazaar))) {
            Redirect.sendToCustomTab(this, getString(R.string.general_bazaar_download_apk),
                    getString(R.string.general_dialog_message_bazaar_not_found), this);
        }
    }

    private void flipProgressState() {
        if (progress.getVisibility() == View.VISIBLE) {
            progress.setVisibility(View.INVISIBLE);
            btnDonate.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.VISIBLE);
            btnDonate.setVisibility(View.INVISIBLE);
        }
    }

    private DonateCallback donateCallback = new DonateCallback() {
        @Override
        public void onDonateSuccess() {
            Toast.makeText(DonateActivity.this, getString(R.string.activity_donate_payment_successful), Toast.LENGTH_LONG).show();
            DonateActivity.this.finish();
        }

        @Override
        public void onDonateFailure() {
            Toast.makeText(DonateActivity.this, getString(R.string.activity_donate_payment_failed), Toast.LENGTH_SHORT).show();
            flipProgressState();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!donateHelper.isActivityResultHandled(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            donateHelper.disposeService();
        } catch (IllegalArgumentException e1) {
            // do nothing
        }
    }
}
