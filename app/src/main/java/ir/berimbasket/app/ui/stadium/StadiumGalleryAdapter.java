package ir.berimbasket.app.ui.stadium;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.berimbasket.app.R;

/**
 * Created by mohammad hosein on 21/09/2017.
 */

class StadiumGalleryAdapter extends RecyclerView.Adapter<StadiumGalleryAdapter.ViewHolderStadiumGallery> {

    private List<String> dataSource;
    private StadiumGalleryListener listener;

    interface StadiumGalleryListener {
        void onGalleryItemClick(String imageUrl);
    }

    StadiumGalleryAdapter(List<String> dataSource, StadiumGalleryListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }


    @Override
    public ViewHolderStadiumGallery onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stadium_gallery, parent, false);
        if (view.getLayoutParams().width == RecyclerView.LayoutParams.MATCH_PARENT) {
            view.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
        }
        return new ViewHolderStadiumGallery(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderStadiumGallery holder, int position) {
        Picasso.with(holder.imgGalleryImage.getContext())
                .load(dataSource.get(position))
                .resize(120, 120)
                .centerInside()
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into(holder.imgGalleryImage);
        holder.imgGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onGalleryItemClick(dataSource.get(holder.getLayoutPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }


    class ViewHolderStadiumGallery extends RecyclerView.ViewHolder {

        ImageView imgGalleryImage;

        ViewHolderStadiumGallery(View itemView) {
            super(itemView);
            imgGalleryImage = itemView.findViewById(R.id.imgGalleryImage);
        }
    }
}
