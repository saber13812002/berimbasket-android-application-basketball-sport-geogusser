package ir.berimbasket.app.ui.home.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.entity.EntityMatchScore;
import ir.berimbasket.app.util.Redirect;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private ArrayList<EntityMatchScore> matchList;
    private Context context;
    private LayoutInflater inflater;
    private Activity activity;

    public MatchAdapter(Activity activity, Context context, ArrayList<EntityMatchScore> matchList) {
        this.context = context;
        this.matchList = matchList;
        this.activity = activity;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_match_score, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView txtHomeName, txtAwayName, txtHomeScore, txtAwayScore, txtDate, txtScoreDate,txtMatchStatus;
        ImageView imgHomeLogo, imgAwayLogo;

        MatchViewHolder(final View itemView) {
            super(itemView);

            this.imgAwayLogo = itemView.findViewById(R.id.imgAwayLogo);
            this.imgHomeLogo = itemView.findViewById(R.id.imgHomeLogo);
            this.txtHomeName = itemView.findViewById(R.id.txtHomeName);
            this.txtAwayName = itemView.findViewById(R.id.txtَAwayName);
            this.txtHomeScore = itemView.findViewById(R.id.txtHomeScore);
            this.txtAwayScore = itemView.findViewById(R.id.txtAwayScore);
            this.txtDate = itemView.findViewById(R.id.txtDate);
            this.txtScoreDate = itemView.findViewById(R.id.txtScoreDate);
            this.txtMatchStatus = itemView.findViewById(R.id.txtMatchStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Redirect.sendToCustomTab(activity, matchList.get(getLayoutPosition()).getLink());
                }
            });

        }

        void setData() {
            EntityMatchScore entityMatchScore = matchList.get(getLayoutPosition());
            this.txtHomeName.setText(entityMatchScore.getHomeName());
            this.txtAwayName.setText(entityMatchScore.getAwayName());
            this.txtHomeScore.setText(String.valueOf(entityMatchScore.getHomeScore()));
            this.txtAwayScore.setText(String.valueOf(entityMatchScore.getAwayScore()));
            this.txtScoreDate.setText(entityMatchScore.getScoreDate());
            this.txtMatchStatus.setText(entityMatchScore.getStatus());
            Picasso.with(context)
                    .load("https://berimbasket.ir/" + entityMatchScore.getAwayLogo())
                    .resize(50, 50)
                    .centerInside()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgAwayLogo);

            Picasso.with(context)
                    .load("https://berimbasket.ir/" + entityMatchScore.getHomeLogo())
                    .resize(50, 50)
                    .centerInside()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgHomeLogo);
        }
    }
}