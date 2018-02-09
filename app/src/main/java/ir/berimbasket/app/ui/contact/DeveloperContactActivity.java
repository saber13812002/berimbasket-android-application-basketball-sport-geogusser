package ir.berimbasket.app.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.util.EditTextHelper;
import ir.berimbasket.app.util.LocaleManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeveloperContactActivity extends BaseActivity {

    EditText edtMobile, edtSubject, edtContent;
    AppCompatButton btnSendMessage;
    TextView btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_contact);
        edtContent = findViewById(R.id.edtContent);
        edtSubject = findViewById(R.id.edtSubject);
        edtMobile = findViewById(R.id.edtMobile);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendEmail = findViewById(R.id.btnSendEmail);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EditTextHelper.isEmpty(edtContent) || EditTextHelper.isEmpty(edtMobile) || EditTextHelper.isEmpty(edtSubject)) {
                    Toast.makeText(DeveloperContactActivity.this, R.string.activity_developer_contact_toast_message_fill_edittext, Toast.LENGTH_LONG).show();
                    EditTextHelper.promptEmpty(DeveloperContactActivity.this);
                } else {
                    Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("mobile", edtMobile.getText().toString());
                    requestBody.put("subject", edtSubject.getText().toString());
                    requestBody.put("content", edtContent.getText().toString());
                    String pusheid = Pushe.getPusheId(getApplicationContext());
                    String username = new PrefManager(getApplicationContext()).getUserName();
                    String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
                    WebApiClient.postFeedbackApi().sendFeedback(pusheid, username, requestBody, lang).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                Toast.makeText(DeveloperContactActivity.this, R.string.activity_developer_contact_toast_message_successful_send, Toast.LENGTH_LONG).show();
                                EditTextHelper.emptyEditText(edtContent, edtMobile, edtSubject);
                            } else {
                                EditTextHelper.promptTryAgain(DeveloperContactActivity.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            EditTextHelper.promptTryLater(DeveloperContactActivity.this);
                        }
                    });
                }
            }
        });

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getString(R.string.activity_developer_email_uri), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, edtSubject.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, edtContent.getText().toString());
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }

}
