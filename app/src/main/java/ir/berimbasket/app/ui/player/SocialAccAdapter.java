package ir.berimbasket.app.ui.player;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.ui.common.entity.SocialAccEntity;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.Telegram;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class SocialAccAdapter extends RecyclerView.Adapter<SocialAccAdapter.ViewHolderSocialAcc> {

    private final Context context;
    private final ArrayList<SocialAccEntity> socialAccList;

    public SocialAccAdapter(ArrayList<SocialAccEntity> socialAccList, Context context) {
        this.socialAccList = socialAccList;
        this.context = context;
    }

    @Override
    public ViewHolderSocialAcc onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_social_acc, parent, false);
        ViewHolderSocialAcc holder = new ViewHolderSocialAcc(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderSocialAcc holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return socialAccList.size();
    }

    class ViewHolderSocialAcc extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView btnSocialAcc;

        ViewHolderSocialAcc(View itemView) {
            super(itemView);
            this.btnSocialAcc = itemView.findViewById(R.id.btnSocialAcc);
        }

        public void setData(int position) {
            final SocialAccEntity socialAccEntity = socialAccList.get(position);
            btnSocialAcc.setImageResource(socialAccEntity.getImageResId());
            btnSocialAcc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (socialAccEntity.getType() == SocialAccEntity.SOCIAL_TYPE_INSTAGRAM) {
                        try {
                            Redirect.sendToInstagram(context, socialAccEntity.getLink());  // https://instagram.com/_u/javaherisaber
                        } catch (IllegalArgumentException unknownInstagramURL) {
                            // do nothing yet
                        }
                    } else if (socialAccEntity.getType() == SocialAccEntity.SOCIAL_TYPE_TELEGRAM_CHANNEL ||
                            socialAccEntity.getType() == SocialAccEntity.SOCIAL_TYPE_TELEGRAM_GROUP ||
                            socialAccEntity.getType() == SocialAccEntity.SOCIAL_TYPE_TELEGRAM_USER) {
                        try {
                            Redirect.sendToTelegram(context, socialAccEntity.getLink(), Telegram.CHAT);  // https://t.me/mamlekate
                        } catch (IllegalArgumentException unknownTelegramURL) {
                            // do nothing yet
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }
}
