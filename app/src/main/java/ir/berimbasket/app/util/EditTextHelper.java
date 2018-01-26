package ir.berimbasket.app.util;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.contact.DeveloperContactActivity;

/**
 * Created by mohammad hosein on 26/01/2018.
 */

public class EditTextHelper {
    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public static void emptyEditText(EditText... edt) {
        for (EditText editText : edt) {
            editText.setText("");
        }
    }

    public static void promptEmpty(Context context) {
        Toast.makeText(context, R.string.activity_developer_contact_toast_message_fill_edittext, Toast.LENGTH_LONG).show();
    }

    public static void promptTryAgain(Context context) {
        Toast.makeText(context, R.string.activity_developer_contact_toast_message_try_again, Toast.LENGTH_SHORT).show();
    }

    public static void promptTryLater(Context context) {
        Toast.makeText(context, R.string.activity_developer_contact_toast_message_webservice_fail, Toast.LENGTH_LONG).show();
    }
}
