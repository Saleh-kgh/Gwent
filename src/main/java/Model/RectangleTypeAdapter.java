package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;

import javafx.scene.shape.Rectangle;

import java.lang.reflect.Type;

public class RectangleTypeAdapter implements JsonSerializer<Rectangle>, JsonDeserializer<Rectangle> {

    @Override
    public JsonElement serialize(Rectangle src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("x", new JsonPrimitive(src.getX()));
        jsonObject.add("y", new JsonPrimitive(src.getY()));
        jsonObject.add("width", new JsonPrimitive(src.getWidth()));
        jsonObject.add("height", new JsonPrimitive(src.getHeight()));
        return jsonObject;
    }

    @Override
    public Rectangle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double width = jsonObject.get("width").getAsDouble();
        double height = jsonObject.get("height").getAsDouble();
        return new Rectangle(x, y, width, height);
    }
}