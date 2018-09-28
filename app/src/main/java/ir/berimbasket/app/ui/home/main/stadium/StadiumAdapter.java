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
import ir.berimbasket.app.data.network.model.Stadium;
import ir.berimbasket.app.ui.base.BaseItem;
import ir.berimbasket.app.ui.common.model.DismissibleInfo;

class StadiumAdapter extends RecyclerView.Adapter<StadiumAdapter.BaseViewHolder> {

    private List<BaseItem> dataSource;
    private StadiumListListener listener;

    interface StadiumListListener {
        void onStadiumItemClick(Stadium stadium);
        void onDismissibleActionClick(DismissibleInfo dismissibleInfo);
        void onDismissibleSkipClick(DismissibleInfo dismissibleInfo);
    }

    StadiumAdapter(List<BaseItem> items, StadiumListListener listener) {
        dataSource = items;
        this.listener = listener;
    }

    StadiumAdapter(StadiumListListener listener) {
        this(new ArrayList<>(), listener);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BaseItem.STADIUM_ITEM:
                View major = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stadium, parent, false);
                return new StadiumHolder(major);
            case BaseItem.DISMISSIBLE_INFO:
                View info = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dismissible_info, parent, false);
                return new DismissibleHolder(info);
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

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View v) {
            super(v);
        }

        public abstract void bind(BaseItem item, int position);
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

    class DismissibleHolder extends BaseViewHolder {
        View view;
        TextView header, message, action, skip;

        DismissibleHolder(View view) {
            super(view);
            this.view = view;
            this.header = view.findViewById(R.id.txtDismissibleHeader);
            this.message = view.findViewById(R.id.txtDismissibleMessage);
            this.action = view.findViewById(R.id.txtDismissibleAction);
            this.skip = view.findViewById(R.id.txtDismissibleSkip);
        }

        @Override
        public void bind(BaseItem item, int position) {
            DismissibleInfo dismissible = (DismissibleInfo) item;
            header.setText(dismissible.getHeader());
            message.setText(dismissible.getMessage());
            action.setText(dismissible.getAction());
            skip.setText(dismissible.getSkip());
            action.setOnClickListener( v -> {
                if (listener != null) {
                    listener.onDismissibleActionClick(dismissible);
                }
            });
            skip.setOnClickListener( v -> {
                if (listener != null) {
                    listener.onDismissibleSkipClick(dismissible);
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
