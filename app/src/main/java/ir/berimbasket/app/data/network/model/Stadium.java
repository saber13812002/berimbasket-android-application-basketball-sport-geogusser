package ir.berimbasket.app.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ir.berimbasket.app.data.env.UrlConstants;
import ir.berimbasket.app.ui.base.BaseItem;

public final class Stadium implements Serializable, BaseItem {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("PlaygroundLatitude")
    private String latitude;
    @SerializedName("PlaygroundLongitude")
    private String longitude;
    @SerializedName("PlaygroundType")
    private String type;
    @SerializedName("ZoomLevel")
    private Integer zoomLevel;
    @SerializedName("address")
    private String address;
    @SerializedName("images")
    private List<String> images;
    @SerializedName("PgInstagramId")
    private String instagramId;
    @SerializedName("PgTlgrmChannelId")
    private String telegramChannelId;
    @SerializedName("PgTlgrmGroupJoinLink")
    private String telegramGroupId;
    @SerializedName("PgTlgrmGroupAdminId")
    private String telegramAdminId;
    @SerializedName("roof")
    private String roof;
    @SerializedName("distance2parking")
    private String distance2parking;
    @SerializedName("rimHeight")
    private String rimHeight;
    @SerializedName("rimNumber")
    private String rimNumber;
    @SerializedName("spotlight")
    private String spotlight;
    @SerializedName("fence")
    private String fence;
    @SerializedName("parking")
    private String parking;
    @SerializedName("basketnet")
    private String basketNet;
    @SerializedName("scoreline")
    private String scoreline;
    @SerializedName("lines")
    private String lines;

    public Stadium(int id, String title, String latitude, String longitude, String type, Integer zoomLevel, String address,
                   List<String> images, String instagramId, String telegramChannelId, String telegramGroupId,
                   String telegramAdminId, String roof, String distance2parking, String rimHeight, String rimNumber,
                   String spotlight, String fence, String parking, String basketNet, String scoreline, String lines) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.zoomLevel = zoomLevel;
        this.address = address;
        this.images = images;
        this.instagramId = instagramId;
        this.telegramChannelId = telegramChannelId;
        this.telegramGroupId = telegramGroupId;
        this.telegramAdminId = telegramAdminId;
        this.roof = roof;
        this.distance2parking = distance2parking;
        this.rimHeight = rimHeight;
        this.rimNumber = rimNumber;
        this.spotlight = spotlight;
        this.fence = fence;
        this.parking = parking;
        this.basketNet = basketNet;
        this.scoreline = scoreline;
        this.lines = lines;
    }

    public final int getId() {
        return this.id;
    }

    public final void setId(int var1) {
        this.id = var1;
    }

    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(String var1) {
        this.title = var1;
    }

    public final String getLatitude() {
        return this.latitude;
    }

    public final void setLatitude(String var1) {
        this.latitude = var1;
    }

    public final String getLongitude() {
        return this.longitude;
    }

    public final void setLongitude(String var1) {
        this.longitude = var1;
    }

    public final String getType() {
        return this.type;
    }

    public final void setType(String var1) {
        this.type = var1;
    }

    public final Integer getZoomLevel() {
        return this.zoomLevel;
    }

    public final void setZoomLevel(Integer var1) {
        this.zoomLevel = var1;
    }

    public final String getAddress() {
        return this.address;
    }

    public final void setAddress(String var1) {
        this.address = var1;
    }

    public final List<String> getImages() {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < this.images.size(); i++) {
            images.add(UrlConstants.Base.Root + this.images.get(i));
        }
        return images;
    }

    public final void setImages(List<String> var1) {
        this.images = var1;
    }

    public final String getInstagramId() {
        return this.instagramId;
    }

    public final void setInstagramId(String var1) {
        this.instagramId = var1;
    }

    public final String getTelegramChannelId() {
        return this.telegramChannelId;
    }

    public final void setTelegramChannelId(String var1) {
        this.telegramChannelId = var1;
    }

    public final String getTelegramGroupId() {
        return this.telegramGroupId;
    }

    public final void setTelegramGroupId(String var1) {
        this.telegramGroupId = var1;
    }

    public final String getTelegramAdminId() {
        return this.telegramAdminId;
    }

    public final void setTelegramAdminId(String var1) {
        this.telegramAdminId = var1;
    }

    public final String getRoof() {
        return this.roof;
    }

    public final void setRoof(String var1) {
        this.roof = var1;
    }

    public final String getDistance2parking() {
        return this.distance2parking;
    }

    public final void setDistance2parking(String var1) {
        this.distance2parking = var1;
    }

    public final String getRimHeight() {
        return this.rimHeight;
    }

    public final void setRimHeight(String var1) {
        this.rimHeight = var1;
    }

    public final String getRimNumber() {
        return this.rimNumber;
    }

    public final void setRimNumber(String var1) {
        this.rimNumber = var1;
    }

    public final String getSpotlight() {
        return this.spotlight;
    }

    public final void setSpotlight(String var1) {
        this.spotlight = var1;
    }

    public final String getFence() {
        return this.fence;
    }

    public final void setFence(String var1) {
        this.fence = var1;
    }

    public final String getParking() {
        return this.parking;
    }

    public final void setParking(String var1) {
        this.parking = var1;
    }

    public final String getBasketNet() {
        return this.basketNet;
    }

    public final void setBasketNet(String var1) {
        this.basketNet = var1;
    }

    public final String getScoreline() {
        return this.scoreline;
    }

    public final void setScoreline(String var1) {
        this.scoreline = var1;
    }

    public final String getLines() {
        return this.lines;
    }

    public final void setLines(String var1) {
        this.lines = var1;
    }

    @Override
    public int getViewType() {
        return BaseItem.STADIUM_ITEM;
    }
}