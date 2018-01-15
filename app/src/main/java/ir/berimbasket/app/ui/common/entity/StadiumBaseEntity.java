package ir.berimbasket.app.ui.common.entity;

import java.io.Serializable;

/**
 * Created by Mahdi on 1/15/2018.
 */

public class StadiumBaseEntity implements Serializable{

    private int id;
    private String title;
    private String latitude;
    private String longitude;


    public StadiumBaseEntity(int id, String title, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
