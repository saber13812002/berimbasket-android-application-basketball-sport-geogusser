package ir.berimbasket.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.berimbasket.app.R;


/**
 * Created by mohammad hosein on 5/24/2017.
 */

public class AdapterAccountGallery extends RecyclerView.Adapter<AdapterAccountGallery.ViewHolderGallery> {

    private ArrayList<Integer> imagesId;
    private LayoutInflater inflater;

    public AdapterAccountGallery(ArrayList<Integer> imagesId, Context context) {
        this.imagesId = imagesId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderGallery onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_account_gallery, parent, false);
        ViewHolderGallery viewHolderGallery = new ViewHolderGallery(view);
        return viewHolderGallery;
    }

    @Override
    public void onBindViewHolder(ViewHolderGallery holder, int position) {
        holder.imgGallery.setImageResource(R.drawable.imagetest);
    }

    @Override
    public int getItemCount() {
        return imagesId.size();
    }

    public class ViewHolderGallery extends RecyclerView.ViewHolder {

        ImageView imgGallery;

        public ViewHolderGallery(View itemView) {
            super(itemView);
            imgGallery = (ImageView) itemView.findViewById(R.id.imgGallery);
        }
    }

}
