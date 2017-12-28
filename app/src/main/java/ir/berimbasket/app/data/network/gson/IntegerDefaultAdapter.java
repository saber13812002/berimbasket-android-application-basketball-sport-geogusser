package ir.berimbasket.app.data.network.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by Mahdi on 12/21/2017.
 * handle null and empty values in JSON element
 */

public class IntegerDefaultAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {


    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            // Defined as int type , If the background returns "" perhaps null, Then return 0
            if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                return 0;
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsInt();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
