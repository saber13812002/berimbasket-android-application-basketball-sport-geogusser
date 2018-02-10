package ir.berimbasket.app.ui.notification;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.model.Notification;
import ir.berimbasket.app.util.Time;

/**
 * Created by mohammad hosein on 09/02/2018.
 */

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private List<Notification> dataSource;
    private NotificationListListener listener;
    private Locale locale;
    private static final String LOCALE_EN = "en";
    private static final String LOCALE_FA = "fa";

    interface NotificationListListener {
        void onNotificationItemClick(Notification notification);
    }

    NotificationAdapter(NotificationListListener listener, Locale locale) {
        this(new ArrayList<Notification>(), listener, locale);
    }

    NotificationAdapter(List<Notification> dataSource, NotificationListListener listener, Locale locale) {
        this.listener = listener;
        this.dataSource = dataSource;
        this.locale = locale;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = dataSource.get(position);
        holder.txtNotificationTitle.setText(notification.getTitle());
        boolean isJalaliLang = locale.getLanguage().equals(LOCALE_FA);
        String timestamp = Time.getLocalTimeStamp(notification.getTimestamp(), isJalaliLang);
        holder.txtNotificationDate.setText(timestamp);
        holder.txtNotificationContent.setText(notification.getContent());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNotificationItemClick(dataSource.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView txtNotificationTitle, txtNotificationDate, txtNotificationContent;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.txtNotificationTitle = itemView.findViewById(R.id.txtNotificationTitle);
            this.txtNotificationDate = itemView.findViewById(R.id.txtNotificationDate);
            this.txtNotificationContent = itemView.findViewById(R.id.txtNotificationContent);
        }
    }

    void swapDataSource(List<Notification> list) {
        dataSource = list;
        notifyDataSetChanged();
    }

    void addItems(List<Notification> list) {
        int lastPosition = dataSource.size();
        dataSource.addAll(list);
        notifyItemRangeInserted(lastPosition, list.size());
    }
}
