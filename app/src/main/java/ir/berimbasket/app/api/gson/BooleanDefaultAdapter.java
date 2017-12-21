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

public class BooleanDefaultAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {


    @Override
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            // Defined as Boolean type , If the background returns "" perhaps null, Then return false
            if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                return false;
            }
        } catch (Exception ignore) {
        }
        return json.getAsBoolean();
    }

    @Override
    public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
