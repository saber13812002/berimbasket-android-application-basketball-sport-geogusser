package ir.berimbasket.app.entity;

/**
 * Created by mohammad hosein on 21/09/2017.
 */

public class EntityStadiumGallery {
    private int id;
    // FIXME: 21/09/2017 when Stadium detail web service connected change url data type to String
    private int url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }
}
