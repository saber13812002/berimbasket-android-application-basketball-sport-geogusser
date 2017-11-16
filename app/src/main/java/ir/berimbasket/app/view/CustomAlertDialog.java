package ir.berimbasket.app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.TypefaceManager;

/**
 * Created by mohammad on 5/17/2016.
 */
public class CustomAlertDialog {

    Typeface tfSans;
    Context context;

    public CustomAlertDialog(Context context){
        this.context = context;
        tfSans = TypefaceManager.get(context, "fonts/yekan.ttf");
    }

    public AlertDialog MaterialAlert(String title){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        TextView message = (TextView) dialog.findViewById(android.R.id.message);

        TextView dialogTitle =  new TextView(context);
        dialogTitle.setText(title);
        dialogTitle.setGravity(Gravity.CENTER);
        dialogTitle.setTextSize(30);
        dialogTitle.setBackgroundColor(Color.GRAY);
        dialogTitle.setTextColor(Color.WHITE);
        dialog.setCustomTitle(dialogTitle);
        message.setTypeface(tfSans);

        Button btnPositive = dialog.getButton(Dialog.BUTTON_POSITIVE);
        btnPositive.setTypeface(tfSans);

        Button btnNegative = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        btnNegative.setTypeface(tfSans);

        return null;
    }

    public TextView getTitleText(String title){
        TextView dialogTitle =  new TextView(context);
        dialogTitle.setText(title);
        dialogTitle.setGravity(Gravity.START);
        dialogTitle.setTextSize(25);
        dialogTitle.setPadding(50,25,50,25);
        dialogTitle.setTypeface(tfSans);
        dialogTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        dialogTitle.setTextColor(Color.WHITE);
        return dialogTitle;
    }

    public void setDialogStyle(AlertDialog alertDialog){
        TextView message = (TextView) alertDialog.findViewById(android.R.id.message);
        if(message != null){
            message.setTypeface(tfSans);
            message.setTextSize(18);
        }

        Button btnPositive = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
        btnPositive.setTypeface(tfSans);
        btnPositive.setTextSize(18);

        Button btnNegative = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
        btnNegative.setTypeface(tfSans);
        btnNegative.setTextSize(18);

        Button btnNeutral = alertDialog.getButton(Dialog.BUTTON_NEUTRAL);
        btnNeutral.setTypeface(tfSans);
        btnNeutral.setTextSize(18);

    }
}
