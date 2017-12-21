package ir.berimbasket.app.api.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Mahdi on 12/21/2017.
 * handle null and empty values in JSON element
 */

public class StringDefaultAdapter implements JsonSerializer<String>, JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            // Defined as String type , If the background returns "" perhaps null, Then return ""
            if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                return "";
            }
        } catch (Exception ignore) {
        }
        return json.getAsString();
    }

    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
