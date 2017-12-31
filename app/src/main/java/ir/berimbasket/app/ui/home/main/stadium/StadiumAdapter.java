package ir.berimbasket.app.ui.home.main.stadium;

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
import ir.berimbasket.app.data.entity.EntityStadium;

class StadiumAdapter extends RecyclerView.Adapter<StadiumAdapter.ViewHolder> {

    private List<EntityStadium> dataSource;
    private StadiumListListener listener;

    interface StadiumListListener {
        void onStadiumItemClick(EntityStadium stadium);
    }

    StadiumAdapter(List<EntityStadium> items, StadiumListListener listener) {
        dataSource = items;
        this.listener = listener;
    }

    StadiumAdapter(StadiumListListener listener) {
        this(new ArrayList<EntityStadium>(), listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stadium, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.imgStadiumImage.setImageResource(R.drawable.stadium1);
        holder.txtStadiumName.setText(String.valueOf(dataSource.get(position).getTitle()));
        holder.txtStadiumPhone.setText("-");
        holder.txtStadiumAddress.setText(dataSource.get(position).getAddress());
        EntityStadium entityStadium = dataSource.get(position);
        Picasso.with(holder.view.getContext())
                .load(entityStadium.getThumbnail())
                .resize(130, 130)
                .placeholder(R.drawable.stadium1)
                .error(R.drawable.stadium1)
                .centerInside()
                .into(holder.imgStadiumImage);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onStadiumItemClick(dataSource.get(holder.getLayoutPosition()));
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
        TextView txtStadiumName, txtStadiumPhone, txtStadiumAddress;
        ImageView imgStadiumImage;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            this.txtStadiumName = view.findViewById(R.id.txtStadiumName);
            this.txtStadiumPhone = view.findViewById(R.id.txtStadiumPhone);
            this.txtStadiumAddress = view.findViewById(R.id.txtStadiumAddress);
            this.imgStadiumImage = view.findViewById(R.id.imgStadiumImage);
        }
    }

    void swapDataSource(List<EntityStadium> list) {
        this.dataSource = list;
        notifyDataSetChanged();
    }
}
