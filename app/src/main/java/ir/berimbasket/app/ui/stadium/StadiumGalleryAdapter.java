package ir.berimbasket.app.ui.stadium;

import android.content.Context;
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

public class StadiumGalleryAdapter extends RecyclerView.Adapter<StadiumGalleryAdapter.ViewHolderStadiumGallery> {

    List<String> dataSource;
    Context context;

    public StadiumGalleryAdapter(List<String> dataSource, Context context) {
        this.dataSource = dataSource;
        this.context = context;
    }


    @Override
    public ViewHolderStadiumGallery onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_stadium_gallery, parent, false);
        if (view.getLayoutParams().width == RecyclerView.LayoutParams.MATCH_PARENT) {
            view.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
        }
        return new ViewHolderStadiumGallery(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderStadiumGallery holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }


    public class ViewHolderStadiumGallery extends RecyclerView.ViewHolder {

        ImageView imgGalleryImage;

        ViewHolderStadiumGallery(View itemView) {
            super(itemView);
            imgGalleryImage = itemView.findViewById(R.id.imgGalleryImage);
        }

        public void setData(int position) {
            Picasso.with(context)
                    .load(dataSource.get(position))
                    .resize(120, 120)
                    .centerInside()
                    .placeholder(R.drawable.profile_default)
                    .error(R.drawable.profile_default)
                    .into(imgGalleryImage);

        }
    }
}
