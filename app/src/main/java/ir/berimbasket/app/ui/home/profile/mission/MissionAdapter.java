package ir.berimbasket.app.ui.home.profile.mission;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.model.Mission;

/**
 * Created by mohammad hosein on 7/22/2017.
 */

class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionViewHolder> {

    private final Context context;
    private final List<Mission> dataSource;

    MissionAdapter(List<Mission> xpList, Context context) {
        this.dataSource = xpList;
        this.context = context;
    }

    @Override
    public MissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_mission, parent, false);
        return new MissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MissionViewHolder holder, final int position) {

        holder.txtMissionTitle.setText(dataSource.get(position).getTitle());
        holder.txtMissionBadge.setText(String.valueOf(dataSource.get(position).getLevel()));
        holder.txtMissionScore.setText(String.valueOf(dataSource.get(position).getScore())
                + " " + context.getString(R.string.adapter_mission_point) + " ");
        if (dataSource.get(position).getLock() != 0) {
            holder.imgMissionLock.setVisibility(View.VISIBLE);
            holder.txtMissionScore.setVisibility(View.GONE);

        } else {
            holder.imgMissionLock.setVisibility(View.INVISIBLE);
            holder.txtMissionScore.setVisibility(View.VISIBLE);
        }

        holder.cardMissionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataSource.get(holder.getLayoutPosition()).getLock() == 0) {
                    missionClickAction(dataSource.get(holder.getLayoutPosition()).getLink());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class MissionViewHolder extends RecyclerView.ViewHolder {

        TextView txtMissionTitle, txtMissionBadge, txtMissionScore;
        ImageView imgMissionLock;
        CardView cardMissionItem;

        MissionViewHolder(View itemView) {
            super(itemView);
            this.txtMissionTitle = itemView.findViewById(R.id.txtMissionTitle);
            this.txtMissionBadge = itemView.findViewById(R.id.txtMissionBadge);
            this.txtMissionScore = itemView.findViewById(R.id.txtMissionScore);
            this.imgMissionLock = itemView.findViewById(R.id.imgMissionLock);
            cardMissionItem = itemView.findViewById(R.id.cardMissionItem);

        }
    }

    private void missionClickAction(String missionLink) {
        Uri missionUri = Uri.parse(missionLink);
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(context, missionUri);
    }

}
