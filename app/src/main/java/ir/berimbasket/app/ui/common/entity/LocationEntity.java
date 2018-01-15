package ir.berimbasket.app.ui.common.entity;

/**
 * Created by Mahdi on 9/20/2017.
 * used as a container for location items
 */

public class LocationEntity {

    private double latitude;
    private double longitude;

    public LocationEntity(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
