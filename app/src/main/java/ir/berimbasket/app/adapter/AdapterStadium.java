package ir.berimbasket.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivityStadium;
import ir.berimbasket.app.entity.EntityStadium;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class AdapterStadium extends RecyclerView.Adapter<AdapterStadium.StadiumViewHolder> {

    private final Context context;
    private final ArrayList<EntityStadium> stadiumList;

    public AdapterStadium(ArrayList<EntityStadium> stadiumList, Context context) {
        this.stadiumList = stadiumList;
        this.context = context;
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
    }


    @Override
    public int getItemCount() {
        return stadiumList.size();
    }

    class StadiumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtStadiumName;
        ImageView imgStadiumImage;
        CardView cardStadiumItem;

        private StadiumViewHolder(View itemView) {
            super(itemView);
            this.txtStadiumName = itemView.findViewById(R.id.txtStadiumName);
            this.imgStadiumImage = itemView.findViewById(R.id.imgStadiumImage);
            cardStadiumItem = itemView.findViewById(R.id.cardStadiumItem);
            cardStadiumItem.setOnClickListener(this);
        }

        public void setData(int pos) {
            imgStadiumImage.setImageResource(R.drawable.stadium1);
            txtStadiumName.setText(String.valueOf(stadiumList.get(pos).getTitle()));
            EntityStadium entityStadium = stadiumList.get(getLayoutPosition());
            Picasso.with(context)
                    .load("https://berimbasket.ir/" + entityStadium.getImages()[0])
                    .resize(130, 130)
                    .placeholder(R.drawable.stadium1)
                    .error(R.drawable.stadium1)
                    .centerInside()
                    .into(imgStadiumImage);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.cardStadiumItem:
                    Intent intent = new Intent(context, ActivityStadium.class);
                    intent.putExtra("stadiumDetail", stadiumList.get(getLayoutPosition()));
                    intent.putExtra("stadiumLogoUrlPath", stadiumList.get(getLayoutPosition()).getImages()[0]);
                    context.startActivity(intent);
                    break;
            }

        }
    }

}
