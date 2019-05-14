package ir.berimbasket.app.ui.home.stadium;

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
import ir.berimbasket.app.data.network.model.Stadium;
import ir.berimbasket.app.ui.base.BaseItem;
import ir.berimbasket.app.ui.base.BaseViewHolder;
import ir.berimbasket.app.ui.common.DismissableCallback;
import ir.berimbasket.app.ui.common.DismissibleHolder;

class StadiumAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BaseItem> dataSource;
    private StadiumListListener listener;
    private DismissableCallback dismissableCallback;

    interface StadiumListListener {
        void onStadiumItemClick(Stadium stadium);
    }

    StadiumAdapter(List<BaseItem> items, StadiumListListener listener, DismissableCallback dismissableCallback) {
        dataSource = items;
        this.listener = listener;
        this.dismissableCallback = dismissableCallback;
    }

    StadiumAdapter(StadiumListListener adapterListener, DismissableCallback dismissableCallback) {
        this(new ArrayList<>(), adapterListener, dismissableCallback);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BaseItem.STADIUM_ITEM:
                View major = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stadium, parent, false);
                return new StadiumHolder(major);
            case BaseItem.DISMISSIBLE_INFO:
                View info = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dismissible_info, parent, false);
                DismissibleHolder dismissibleHolder = new DismissibleHolder(info);
                dismissibleHolder.setCallback(dismissableCallback);
                return dismissibleHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        holder.bind(dataSource.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSource.get(position).getViewType();
    }

    class StadiumHolder extends BaseViewHolder {
        View view;
        TextView txtStadiumName, txtStadiumPhone, txtStadiumAddress;
        ImageView imgStadiumImage;

        StadiumHolder(View view) {
            super(view);
            this.view = view;
            this.txtStadiumName = view.findViewById(R.id.txtStadiumName);
            this.txtStadiumPhone = view.findViewById(R.id.txtStadiumPhone);
            this.txtStadiumAddress = view.findViewById(R.id.txtStadiumAddress);
            this.imgStadiumImage = view.findViewById(R.id.imgStadiumImage);
        }

        @Override
        public void bind(BaseItem item, int position) {
            Stadium stadium = (Stadium) item;
            imgStadiumImage.setImageResource(R.drawable.stadium1);
            txtStadiumName.setText(String.valueOf(stadium.getTitle()));
            txtStadiumPhone.setText("-");
            txtStadiumAddress.setText(stadium.getAddress());
            if (stadium.getImages() != null && stadium.getImages().size() != 0) {
                Picasso.with(view.getContext())
                        .load(stadium.getImages().get(0))
                        .resize(130, 130)
                        .placeholder(R.drawable.stadium1)
                        .error(R.drawable.stadium1)
                        .centerInside()
                        .into(imgStadiumImage);
            }
            view.setOnClickListener(v -> {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onStadiumItemClick((Stadium) dataSource.get(this.getLayoutPosition()));
                }
            });
        }
    }


    void swapDataSource(List<BaseItem> list) {
        this.dataSource = list;
        notifyDataSetChanged();
    }

    void removeTop() {
        dataSource.remove(0);
        notifyItemRemoved(0);
    }

    void addToTop(BaseItem item) {
        dataSource.add(0, item);
        notifyItemInserted(0);
    }

    void addStadiums(List<Stadium> list) {
        int lastPosition = dataSource.size();
        dataSource.addAll(list);
        notifyItemRangeInserted(lastPosition, list.size());
    }
}
