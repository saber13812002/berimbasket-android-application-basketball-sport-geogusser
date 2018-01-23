package ir.berimbasket.app.ui.player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ir.berimbasket.app.R;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

class SocialAccAdapter extends RecyclerView.Adapter<SocialAccAdapter.ViewHolderSocialAcc> {

    private List<SocialAccEntity> dataSource;
    private SocialAccListener listener;

    interface SocialAccListener {
        void onSocialItemClick(SocialAccEntity entity);
    }

    SocialAccAdapter(List<SocialAccEntity> dataSource, SocialAccListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    public ViewHolderSocialAcc onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_social_acc, parent, false);
        return new ViewHolderSocialAcc(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderSocialAcc holder, int position) {
        final SocialAccEntity socialAccEntity = dataSource.get(position);
        holder.btnSocialAcc.setImageResource(socialAccEntity.getImageResId());
        holder.btnSocialAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSocialItemClick(dataSource.get(holder.getLayoutPosition()));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class ViewHolderSocialAcc extends RecyclerView.ViewHolder {

        ImageView btnSocialAcc;

        ViewHolderSocialAcc(View itemView) {
            super(itemView);
            this.btnSocialAcc = itemView.findViewById(R.id.btnSocialAcc);
        }

    }
}
