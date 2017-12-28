package ir.berimbasket.app.data.entity;

/**
 * Created by mohammad hosein on 16/11/2017.
 */

public class EntitySocialAcc {

    public static final int SOCIAL_TYPE_INSTAGRAM = 1;
    public static final int SOCIAL_TYPE_TELEGRAM_CHANNEL = 2;
    public static final int SOCIAL_TYPE_TELEGRAM_GROUP = 3;
    public static final int SOCIAL_TYPE_TELEGRAM_USER = 4;

    private int id;
    private int imageResId;
    private String link;
    private boolean visibility;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
