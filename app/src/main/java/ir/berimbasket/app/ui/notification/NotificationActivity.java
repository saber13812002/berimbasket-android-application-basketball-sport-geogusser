package ir.berimbasket.app.ui.notification;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.net.HttpURLConnection;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Notification;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity implements NotificationAdapter.NotificationListListener {

    private static final String NOTIFICATION_TYPE_BROWSER = "browser";
    private static final int PAGE_COUNT = 20;
    private boolean loading;
    private boolean isLastPage;
    private int from;

    private ProgressBar circularProgressBar, horizontalProgressBar;
    private NotificationAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initToolbar();
        initViews(getApplicationContext(), this);
        loadNotificationList(0, PAGE_COUNT);
        from += PAGE_COUNT;
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initViews(Context context, NotificationAdapter.NotificationListListener listener) {
        circularProgressBar = findViewById(R.id.progressNotification);
        circularProgressBar.setVisibility(View.VISIBLE);
        horizontalProgressBar = findViewById(R.id.progressBarHorizontal);
        RecyclerView recyclerNotificationList = findViewById(R.id.recyclerNotificationList);
        layoutManager = new LinearLayoutManager(context);
        recyclerNotificationList.setLayoutManager(layoutManager);
        adapter = new NotificationAdapter(listener, LocaleManager.getLocale(getApplicationContext()));
        recyclerNotificationList.setAdapter(adapter);
        recyclerNotificationList.addOnScrollListener(scrollListener);
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (lastVisibleItemPosition == adapter.getItemCount() - 1 && !loading && !isLastPage) {
                horizontalProgressBar.setVisibility(View.VISIBLE);
                loading = true;
                loadNotificationList(from, PAGE_COUNT);
                from += PAGE_COUNT;
            }
        }
    };

    private void loadNotificationList(int from, int num) {
        PrefManager pref = new PrefManager(getApplicationContext());
        String pusheId = Pushe.getPusheId(getApplicationContext());
        String username = pref.getUserName();
        String lang = LocaleManager.getLocale(getApplicationContext()).getLanguage();
        WebApiClient.getNotificationApi().getNotificationHistory(from, num, "json", pusheId, username, lang).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                loading = false;
                horizontalProgressBar.setVisibility(View.INVISIBLE);
                if (circularProgressBar.getVisibility() == View.VISIBLE) {
                    circularProgressBar.setVisibility(View.GONE);
                }
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Notification> notificationList = response.body();
                    if (notificationList != null && notificationList.size() != 0) {
                        adapter.addItems(notificationList);
                    } else {
                        isLastPage = true;
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                loading = false;
                horizontalProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onNotificationItemClick(Notification notification) {
        if (notification.getType().equals(NOTIFICATION_TYPE_BROWSER)) {
            Redirect.sendToCustomTab(this, notification.getLink());
        }
    }
}
