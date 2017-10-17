package ir.berimbasket.app.map;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Mahdi on 10/17/2017.
 * used in map cluster manager in order to group marker items
 */

public class MyClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private String id;
    private String mTitle;
    private String mSnippet;

    public MyClusterItem(double lat, double lng, String id) {
        mPosition = new LatLng(lat, lng);
        this.id = id;
    }

    public MyClusterItem(double lat, double lng, String id, String title) {
        this(lat, lng, id);
        mTitle = title;
    }

    public MyClusterItem(double lat, double lng, String id, String title, String snippet) {
        this(lat, lng, id);
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public String getId() {
        return id;
    }
}
