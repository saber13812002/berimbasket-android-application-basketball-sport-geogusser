package ir.berimbasket.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivityStadium;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.util.TypefaceManager;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class AdapterStadium extends RecyclerView.Adapter<AdapterStadium.StadiumViewHolder> {

    private final Context context;
    private final ArrayList<EntityStadium> stadiumList;
    private Typeface typeface;

    public AdapterStadium(ArrayList<EntityStadium> stadiumList, Context context) {
        this.stadiumList = stadiumList;
        this.context = context;
        typeface = TypefaceManager.get(context, context.getString(R.string.font_yekan));
    }

    @Override
    public StadiumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_stadium, parent, false);
        AdapterStadium.StadiumViewHolder holder = new AdapterStadium.StadiumViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterStadium.StadiumViewHolder holder, int position) {
        holder.setData(position);
        holder.txtStadiumName.setTypeface(typeface);
    }


    @Override
    public int getItemCount() {
        return stadiumList.size();
    }

    class StadiumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtStadiumName;
        CircleImageView imgStadiumImage;
        CardView cardStadiumItem;

        public StadiumViewHolder(View itemView) {
            super(itemView);
            this.txtStadiumName = (TextView) itemView.findViewById(R.id.txtStadiumName);
            this.imgStadiumImage = (CircleImageView) itemView.findViewById(R.id.imgStadiumImage);
            cardStadiumItem = (CardView) itemView.findViewById(R.id.cardStadiumItem);
            cardStadiumItem.setOnClickListener(this);
        }

        public void setData(int pos) {
            imgStadiumImage.setImageResource(R.drawable.stadium1);
            txtStadiumName.setText(String.valueOf(stadiumList.get(pos).getTitle()));
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.cardStadiumItem:
                    Intent intent = new Intent(context, ActivityStadium.class);
                    intent.putExtra("stadiumDetail", stadiumList.get(getLayoutPosition()));
                    context.startActivity(intent);
                    break;
            }

        }
    }

}
