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
import ir.berimbasket.app.ui.base.BaseActivity;
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
                if (isEmpty(edtContent) || isEmpty(edtMobile) || isEmpty(edtSubject)) {
                    Toast.makeText(DeveloperContactActivity.this, R.string.activity_developer_contact_toast_message_fill_edittext, Toast.LENGTH_LONG).show();
                } else {
                    Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("mobile", edtMobile.getText().toString());
                    requestBody.put("subject", edtSubject.getText().toString());
                    requestBody.put("content", edtContent.getText().toString());
                    String pusheid = Pushe.getPusheId(getApplicationContext());
                    WebApiClient.postFeedbackApi().sendFeedback(pusheid, requestBody).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                Toast.makeText(DeveloperContactActivity.this, R.string.activity_developer_contact_toast_message_successfull_send, Toast.LENGTH_LONG).show();
                                emptyEditText(edtContent, edtMobile, edtSubject);
                            } else {
                                Toast.makeText(DeveloperContactActivity.this, R.string.activity_developer_contact_toast_message_try_again, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(DeveloperContactActivity.this, R.string.activity_developer_contact_toast_message_webservice_fail, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getString(R.string.developer_email_uri), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, edtSubject.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, edtContent.getText().toString());
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private void emptyEditText(EditText... edt) {
        for (EditText editText : edt) {
            editText.setText("");
        }
    }
}
