package ir.berimbasket.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.entity.EntityMatchScore;
import ir.berimbasket.app.util.TypefaceManager;

public class AdapterMatch extends RecyclerView.Adapter<AdapterMatch.MatchViewHolder> {

    private ArrayList<EntityMatchScore> matchList;
    private Typeface typeface;
    private Context context;
    private LayoutInflater inflater;

    public AdapterMatch(Context context, ArrayList<EntityMatchScore> matchList) {
        this.context = context;
        this.matchList = matchList;
        this.inflater = LayoutInflater.from(context);
        typeface = TypefaceManager.get(context, context.getString(R.string.font_yekan));
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_match_score, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        holder.setData();
        holder.txtAwayName.setTypeface(typeface);
        holder.txtDate.setTypeface(typeface);
        holder.txtHomeScore.setTypeface(typeface);
        holder.txtAwayScore.setTypeface(typeface);
        holder.txtHomeName.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView txtHomeName, txtAwayName, txtHomeScore, txtAwayScore, txtDate;
        ImageView imgHomeLogo, imgAwayLogo;

        MatchViewHolder(View itemView) {
            super(itemView);

            this.imgAwayLogo = (ImageView) itemView.findViewById(R.id.imgAwayLogo);
            this.imgHomeLogo = (ImageView) itemView.findViewById(R.id.imgHomeLogo);
            this.txtHomeName = (TextView) itemView.findViewById(R.id.txtHomeName);
            this.txtAwayName = (TextView) itemView.findViewById(R.id.txtَAwayName);
            this.txtHomeScore = (TextView) itemView.findViewById(R.id.txtHomeScore);
            this.txtAwayScore = (TextView) itemView.findViewById(R.id.txtAwayScore);
            this.txtDate = (TextView) itemView.findViewById(R.id.txtDate);

        }

        void setData() {
            EntityMatchScore entityMatchScore = matchList.get(getLayoutPosition());
            this.txtHomeName.setText(entityMatchScore.getHomeName());
            this.txtAwayName.setText(entityMatchScore.getAwayName());
            this.txtHomeScore.setText(String.valueOf(entityMatchScore.getHomeScore()));
            this.txtAwayScore.setText(String.valueOf(entityMatchScore.getAwayScore()));
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