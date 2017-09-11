package ir.berimbasket.app.Entity;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class EntityStadium {

    private int id;
    private String title;
    private String latitude;
    private String longitude;
    private String type;
    private int zoomLevel;
    private String address;
    private String[] images;
    private String telegramChannelId;
    private String instagramId;
    private String telegramGroupId;
    private String telegramAdminId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getTelegramChannelId() {
        return telegramChannelId;
    }

    public void setTelegramChannelId(String telegramChannelId) {
        this.telegramChannelId = telegramChannelId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(String instagramId) {
        this.instagramId = instagramId;
    }

    public String getTelegramGroupId() {
        return telegramGroupId;
    }

    public void setTelegramGroupId(String telegramGroupId) {
        this.telegramGroupId = telegramGroupId;
    }

    public String getTelegramAdminId() {
        return telegramAdminId;
    }

    public void setTelegramAdminId(String telegramAdminId) {
        this.telegramAdminId = telegramAdminId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
