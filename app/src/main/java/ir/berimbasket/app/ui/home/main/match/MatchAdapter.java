package ir.berimbasket.app.ui.home.main.match;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.env.UrlConstants;
import ir.berimbasket.app.data.network.model.Match;

class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private List<Match> dataSource;
    private MatchListListener listener;

    interface MatchListListener {
        void onMatchItemClick(Match matchScore);
    }

    MatchAdapter(List<Match> items, MatchListListener listener) {
        dataSource = items;
        this.listener = listener;
    }

    MatchAdapter(MatchListListener listener) {
        this(new ArrayList<Match>(), listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Match entityMatchScore = dataSource.get(position);
        holder.txtHomeName.setText(entityMatchScore.getHomeName());
        holder.txtAwayName.setText(entityMatchScore.getAwayName());
        holder.txtHomeScore.setText(String.valueOf(entityMatchScore.getHomeScore()));
        holder.txtAwayScore.setText(String.valueOf(entityMatchScore.getAwayScore()));
        holder.txtScoreDate.setText(entityMatchScore.getScoreDate());
        holder.txtMatchStatus.setText(entityMatchScore.getStatus());
        Picasso.with(holder.view.getContext())
                .load(UrlConstants.Base.Root + "/" + entityMatchScore.getAwayLogo())
                .resize(50, 50)
                .centerInside()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgAwayLogo);

        Picasso.with(holder.view.getContext())
                .load(UrlConstants.Base.Root + "/" + entityMatchScore.getHomeLogo())
                .resize(50, 50)
                .centerInside()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgHomeLogo);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onMatchItemClick(dataSource.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        TextView txtHomeName, txtAwayName, txtHomeScore, txtAwayScore, txtDate, txtScoreDate,txtMatchStatus;
        ImageView imgHomeLogo, imgAwayLogo;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            this.imgAwayLogo = itemView.findViewById(R.id.imgAwayLogo);
            this.imgHomeLogo = itemView.findViewById(R.id.imgHomeLogo);
            this.txtHomeName = itemView.findViewById(R.id.txtHomeName);
            this.txtAwayName = itemView.findViewById(R.id.txtَAwayName);
            this.txtHomeScore = itemView.findViewById(R.id.txtHomeScore);
            this.txtAwayScore = itemView.findViewById(R.id.txtAwayScore);
            this.txtDate = itemView.findViewById(R.id.txtDate);
            this.txtScoreDate = itemView.findViewById(R.id.txtScoreDate);
            this.txtMatchStatus = itemView.findViewById(R.id.txtMatchStatus);
        }
    }

    void swapDataSource(List<Match> dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    void addItems(List<Match> list) {
        int lastPosition = dataSource.size();
        dataSource.addAll(list);
        notifyItemRangeInserted(lastPosition, list.size());
    }
}
