package ir.berimbasket.app.ui.notification;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.model.Notification;
import ir.berimbasket.app.ui.base.BaseActivity;

public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initToolbar();
        initNotificationList();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initNotificationList() {

        RecyclerView recyclerNotificationList = findViewById(R.id.recyclerNotificationList);
        LinearLayoutManager llm = new LinearLayoutManager(NotificationActivity.this);
        ArrayList<Notification> notificationList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Notification notification = new Notification(1, "اعلان تستی",
                    "محتوای اعلان تستی",
                    "تاریخ اعلان", "http://google.com",
                    NotificationAdapter.NOTIFICATION_TYPE_BROWSER);
            notificationList.add(notification);
        }

        recyclerNotificationList.setLayoutManager(llm);
        NotificationAdapter notificationAdapter = new NotificationAdapter(NotificationActivity.this, NotificationActivity.this, notificationList);
        recyclerNotificationList.setAdapter(notificationAdapter);
    }

}
