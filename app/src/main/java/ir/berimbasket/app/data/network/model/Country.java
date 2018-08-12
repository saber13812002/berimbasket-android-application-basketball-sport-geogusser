package ir.berimbasket.app.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Country implements Serializable {

    @SerializedName("callingCodes")
    private String[] code;

    @SerializedName("name")
    private String name;

    @SerializedName("flag")
    private String image;

    @SerializedName("nativeName")
    private String nativeName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getCode() {
        return "+" + code[0];
    }

    public void setCode(String code) {
        String[] array = new String[1];
        array[0] = code;
        this.code = array;
    }
}
