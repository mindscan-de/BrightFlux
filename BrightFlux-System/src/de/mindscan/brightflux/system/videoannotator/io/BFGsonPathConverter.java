package de.mindscan.brightflux.system.videoannotator.io;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * License for this code is unknown.
 * 
 * https://stackoverflow.com/questions/36964995/how-to-serialize-java-nio-file-path-with-gson
 * https://github.com/google/gson/issues/595
 * https://github.com/google/gson/issues/595#issuecomment-586208896
 */
public class BFGsonPathConverter implements JsonDeserializer<Path>, JsonSerializer<Path> {

    @Override
    public Path deserialize( JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException {
        return Paths.get( jsonElement.getAsString() );
    }

    @Override
    public JsonElement serialize( Path path, Type type, JsonSerializationContext jsonSerializationContext ) {
        return new JsonPrimitive( path.toString() );
    }
}
