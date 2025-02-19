package adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(duration.toString());
    }

    @Override
    public Duration deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Duration.parse(json.getAsString());
    }
}