package ir.berimbasket.app.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.Entity.EntityMatchScore;

public class AdapterMatch extends RecyclerView.Adapter<AdapterMatch.MatchViewHolder> {

    ArrayList<EntityMatchScore> matchScores;
    Typeface typeface;
    private Context context;
    private LayoutInflater inflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterMatch(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        matchScores = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            EntityMatchScore matchScore = new EntityMatchScore();
            matchScore.setAwayName("استقلال");
            matchScore.setHomeName("پرسپولیس");
            matchScore.setAwayScore(1);
            matchScore.setHomeScore(3);
            matchScore.setScoreStatus("پایان");
            matchScores.add(matchScore);
        }

        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/yekan.ttf");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_match_score, parent, false);
        MatchViewHolder matchViewHolder = new MatchViewHolder(view);
        return matchViewHolder;
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        holder.setData();
        holder.txtAwayName.setTypeface(typeface);
        holder.txtStatus.setTypeface(typeface);
        holder.txtHomeScore.setTypeface(typeface);
        holder.txtAwayScore.setTypeface(typeface);
        holder.txtHomeName.setTypeface(typeface);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return matchScores.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtHomeName, txtAwayName, txtHomeScore, txtAwayScore, txtStatus;
        ImageView imgHomeLogo, imgAwayLogo;

        public MatchViewHolder(View itemView) {
            super(itemView);

            this.imgAwayLogo = (ImageView) itemView.findViewById(R.id.imgAwayLogo);
            this.imgHomeLogo = (ImageView) itemView.findViewById(R.id.imgHomeLogo);
            this.txtHomeName = (TextView) itemView.findViewById(R.id.txtHomeName);
            this.txtAwayName = (TextView) itemView.findViewById(R.id.txtَAwayName);
            this.txtHomeScore = (TextView) itemView.findViewById(R.id.txtHomeScore);
            this.txtAwayScore = (TextView) itemView.findViewById(R.id.txtAwayScore);
            this.txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);

        }

        public void setData() {
            imgAwayLogo.setImageResource(R.drawable.manchester);
            imgHomeLogo.setImageResource(R.drawable.chelsea);
            txtHomeName.setText("پرسپولیس");
            txtAwayName.setText("استقلال");
            txtHomeScore.setText("3");
            txtAwayScore.setText("1");
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
            }

        }
    }
}