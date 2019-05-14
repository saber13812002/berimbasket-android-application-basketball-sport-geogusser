package ir.berimbasket.app.ui.home.player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.env.UrlConstants;
import ir.berimbasket.app.data.network.model.Player;
import ir.berimbasket.app.ui.base.BaseItem;
import ir.berimbasket.app.ui.base.BaseViewHolder;
import ir.berimbasket.app.ui.common.DismissableCallback;
import ir.berimbasket.app.ui.common.DismissibleHolder;

class PlayerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BaseItem> dataSource;
    private PlayerListListener listener;
    private DismissableCallback dismissableCallback;

    interface PlayerListListener {
        void onPlayerItemClick(Player player);
    }

    PlayerAdapter(List<BaseItem> items, PlayerListListener listener, DismissableCallback dismissableCallback) {
        dataSource = items;
        this.listener = listener;
        this.dismissableCallback = dismissableCallback;
    }

    PlayerAdapter(PlayerListListener listener, DismissableCallback dismissableCallback) {
        this(new ArrayList<>(), listener, dismissableCallback);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BaseItem.PLAYER_ITEM:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_player, parent, false);
                return new PlayerHolder(view);
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

    class PlayerHolder extends BaseViewHolder {
        View view;
        TextView txtPlayerName, txtPost, txtTeam;
        ImageView imgCoach;
        CircleImageView imgPlayerProfile;

        PlayerHolder(View view) {
            super(view);
            this.view = view;
            this.txtPlayerName = view.findViewById(R.id.txtPlayerName);
            this.txtPost = view.findViewById(R.id.txtPlayerPost);
            this.txtTeam = view.findViewById(R.id.txtPlayerTeam);
            this.imgPlayerProfile = view.findViewById(R.id.imgPlayerProfile);
            this.imgCoach = view.findViewById(R.id.imgCoach);
        }

        @Override
        public void bind(BaseItem item, int position) {
            Player player = (Player) item;
            imgPlayerProfile.setImageResource(R.drawable.profile_default);
            txtPlayerName.setText(player.getName());
            txtTeam.setText(player.getTeamName());
            txtPost.setText(player.getPost());
            String profilePic = player.getProfileImage();
            Picasso.with(view.getContext())
                    .load(UrlConstants.Base.Root + "/" + profilePic)
                    .resize(120, 120)
                    .centerInside()
                    .placeholder(R.drawable.profile_default)
                    .error(R.drawable.profile_default)
                    .into(imgPlayerProfile);
            if (player.getPriority() > 6) {
                imgCoach.setVisibility(View.VISIBLE);
            }
            view.setOnClickListener(v -> {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onPlayerItemClick((Player) dataSource.get(this.getLayoutPosition()));
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

    void addItems(List<Player> list) {
        int lastPosition = dataSource.size();
        dataSource.addAll(list);
        notifyItemRangeInserted(lastPosition, list.size());
    }
}
