package ir.berimbasket.app.ui.notification;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.model.Notification;
import ir.berimbasket.app.util.Redirect;

/**
 * Created by mohammad hosein on 09/02/2018.
 */

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public static final String NOTIFICATION_TYPE_BROWSER = "browser";

    private Context context;
    private ArrayList<Notification> notificationList;
    private Activity activity;


    public NotificationAdapter(Activity activity, Context context, ArrayList<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        this.activity = activity;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.txtNotificationTitle.setText(notification.getTitle());
        holder.txtNotificationDate.setText(notification.getDate());
        holder.txtNotificationContent.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNotificationTitle, txtNotificationDate, txtNotificationContent;

        ViewHolder(View itemView) {
            super(itemView);
            txtNotificationTitle = itemView.findViewById(R.id.txtNotificationTitle);
            txtNotificationDate = itemView.findViewById(R.id.txtNotificationDate);
            txtNotificationContent = itemView.findViewById(R.id.txtNotificationContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notification notification = notificationList.get(getLayoutPosition());
                    if (notification.getType().equals(NOTIFICATION_TYPE_BROWSER)) {
                        Redirect.sendToCustomTab(activity, notification.getLink());
                    }
                }
            });
        }
    }
}
