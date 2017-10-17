package ir.berimbasket.app.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import ir.berimbasket.app.R;

/**
 * Created by Mahdi on 10/17/2017.
 * Set custom icon to map marker
 */

public class MarkerIconRenderer extends DefaultClusterRenderer<MyClusterItem> {

    private Context context;

    public MarkerIconRenderer(Context context, GoogleMap map, ClusterManager<MyClusterItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyClusterItem item, MarkerOptions markerOptions) {
        // create custom icon
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/yekan.ttf");
        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_map_marker, null);
        TextView txtMarkerTitle = (TextView) customMarkerView.findViewById(R.id.markerTitle);
        txtMarkerTitle.setText(item.getTitle());
        txtMarkerTitle.setTypeface(typeface);
        IconGenerator generator = new IconGenerator(context);
        generator.setBackground(null);
        generator.setContentView(customMarkerView);
        Bitmap icon = generator.makeIcon();
        // set icon and title
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
