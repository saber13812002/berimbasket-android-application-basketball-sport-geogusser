package ir.berimbasket.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivityPlayer;
import ir.berimbasket.app.entity.EntityPlayer;
import ir.berimbasket.app.util.TypefaceManager;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class AdapterPlayer extends RecyclerView.Adapter<AdapterPlayer.PlayerViewHolder> {

    private final Context context;
    private final ArrayList<EntityPlayer> playerList;
    private Activity activity;

    public AdapterPlayer(ArrayList<EntityPlayer> playerList, Context context, Activity activity) {
        this.playerList = playerList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return playerList.size();
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {

        TextView txtPlayerName;
        CircleImageView imgPlayerProfile;
        View imgProfileView;
        View txtNameView;

        PlayerViewHolder(final View itemView) {
            super(itemView);
            this.txtPlayerName = (TextView) itemView.findViewById(R.id.txtPlayerName);
            this.txtNameView = itemView.findViewById(R.id.txtPlayerName);
            this.imgPlayerProfile = (CircleImageView) itemView.findViewById(R.id.imgPlayerProfile);
            this.imgProfileView = itemView.findViewById(R.id.imgPlayerProfile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityPlayer.class);
                    intent.putExtra("MyClass", playerList.get(getLayoutPosition()));
                    intent.putExtra("ProfilePic", playerList.get(getLayoutPosition()).getProfileImage());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txtNameView.setTransitionName("name");
                        imgProfileView.setTransitionName("image");
                        Pair<View, String> p1 = Pair.create(txtNameView, "name");
                        Pair<View, String> p2 = Pair.create(imgProfileView, "image");
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2);
                        context.startActivity(intent, options.toBundle());
                    }
                    else {
                        context.startActivity(intent);
                    }

                }
            });
        }

        public void setData(int pos) {
            imgPlayerProfile.setImageResource(R.drawable.profile_default);
            txtPlayerName.setText(playerList.get(pos).getName());
            EntityPlayer entityPlayer = playerList.get(getLayoutPosition());
            Picasso.with(context)
                    .load("https://berimbasket.ir/" + entityPlayer.getProfileImage())
                    .resize(120, 120)
                    .centerInside()
                    .placeholder(R.drawable.profile_default)
                    .error(R.drawable.profile_default)
                    .into(imgPlayerProfile);
        }

    }
}
