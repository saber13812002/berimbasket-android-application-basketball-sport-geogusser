package ir.berimbasket.app.ui.login.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.VerifyOTP;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.util.AnalyticsHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileLoginVerifyFragment extends Fragment {

    private int timerSecondsLeft = 120;

    public static final String LOCAL_RECEIVER_ACTION = "Local Receiver";
    public static final String LOCAL_RECEIVER_OTP_EXTRA = "otp";
    public static final String INTENT_KEY_MOBILE = "mobile";

    private TextView verifyTimer;
    private EditText edtOTPVerifyCode;
    private Button btnVerify;
    private ProgressBar progress;
    private MobileVerifyListener listener;
    private String mobile;

    public interface MobileVerifyListener {
        void onTimerExpire();
    }

    private BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getExtras() != null && edtOTPVerifyCode != null && isAdded()) {
                String otp = intent.getStringExtra(LOCAL_RECEIVER_OTP_EXTRA);
                edtOTPVerifyCode.setText(otp);
                btnVerify.performClick();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (MobileVerifyListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile_verify, container, false);
        mobile = getArguments().getString(INTENT_KEY_MOBILE);
        verifyTimer = view.findViewById(R.id.txtVerifyTimer);
        btnVerify = view.findViewById(R.id.btnVerify);
        edtOTPVerifyCode = view.findViewById(R.id.edtOTPVerifyCode);
        progress = view.findViewById(R.id.progressMobileVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null && edtOTPVerifyCode.getText().toString().length() != 0) {
                    btnVerify.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    String pusheid = Pushe.getPusheId(getContext());
                    WebApiClient.getLoginApi(getContext()).verifyOTP(mobile, pusheid, edtOTPVerifyCode.getText().toString()).enqueue(new Callback<VerifyOTP>() {
                        @Override
                        public void onResponse(Call<VerifyOTP> call, Response<VerifyOTP> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                btnVerify.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.INVISIBLE);
                                VerifyOTP verifyOTP = response.body();
                                if (verifyOTP != null) {
                                    if (verifyOTP.getStatus() == 200) {
                                        PrefManager pref = new PrefManager(getActivity().getApplicationContext());
                                        Toast.makeText(getActivity(), R.string.activity_login_toast_successful, Toast.LENGTH_LONG).show();
                                        pref.putToken(verifyOTP.getToken());
                                        pref.putUserName(verifyOTP.getNickName());
                                        pref.putIsLoggedIn(true);
                                        // Tracking Event (Analytics)
                                        AnalyticsHelper.getInstance().trackEvent(getString(R.string.analytics_category_login),
                                                getString(R.string.analytics_action_login_mobile), "");
                                        getActivity().finish();
                                    } else if (verifyOTP.getStatus() == 300) {
                                        edtOTPVerifyCode.setError(getString(R.string.fragment_mobile_verify_wrong_code));
                                    } else if (verifyOTP.getStatus() == 400) {
                                        Toast.makeText(getActivity(), R.string.activity_mobile_login_api_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyOTP> call, Throwable t) {
                            btnVerify.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), R.string.activity_mobile_login_api_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    edtOTPVerifyCode.setError(getString(R.string.fragment_mobile_verify_empty_code));
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 4/30/2019 fix this sms receiver asap (it's not listening to sms now, maybe android 8.0+ API has breaking change
//        getActivity().registerReceiver(localReceiver, new IntentFilter(LOCAL_RECEIVER_ACTION));
    }

    @Override
    public void onStart() {
        super.onStart();
        runTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        // TODO: 4/30/2019 fix this sms receiver asap (it's not listening to sms now, maybe android 8.0+ API has breaking change
//        getActivity().unregisterReceiver(localReceiver);
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (timerSecondsLeft == 0 && getContext() != null) {
                    Toast.makeText(getContext(), R.string.fragment_mobile_verify_msg_timer_expire, Toast.LENGTH_LONG).show();
                    if (listener != null) {
                        listener.onTimerExpire();
                    }
                } else {
                    if (isAdded()) {
                        timerSecondsLeft -= 1;
                        verifyTimer.setText(timerSecondsLeft + " " + getString(R.string.fragment_mobile_verify_seconds_to_expire) + " ");
                        handler.postDelayed(this, 1000);  // execute run() in the future
                    }
                }
            }
        });
    }
}
