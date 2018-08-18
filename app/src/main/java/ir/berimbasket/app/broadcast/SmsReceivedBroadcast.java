package ir.berimbasket.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.berimbasket.app.data.env.ConfigConstants;
import ir.berimbasket.app.ui.login.mobile.MobileLoginVerifyFragment;

public class SmsReceivedBroadcast extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get(SMS_BUNDLE);
                if (pdusObj != null) {
                    for (Object aPdusObj : pdusObj) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        String senderNum = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();
                        if (senderNum.equals(ConfigConstants.PANEL_SMS_NUMBER)) {
                            Pattern pattern = Pattern.compile("[0-9]+");
                            Matcher matcher = pattern.matcher(message);
                            if (matcher.find()) {
                                String otp = matcher.group(0);
                                Intent i = new Intent(MobileLoginVerifyFragment.LOCAL_RECEIVER_ACTION);
                                i.putExtra(MobileLoginVerifyFragment.LOCAL_RECEIVER_OTP_EXTRA, otp);
                                context.sendBroadcast(i);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
            System.out.println(e);
        }
    }
}
