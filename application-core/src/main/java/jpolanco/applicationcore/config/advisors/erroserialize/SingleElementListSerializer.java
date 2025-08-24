package jpolanco.applicationcore.config.advisors.erroserialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * Custom serializer to handle serialization of a list of DetailErrorResponse.
 * If the list contains only one element, it serializes it as a single object.
 * If the list is empty or null, it serializes it as an empty array.
 * Otherwise, it serializes the list as an array of objects.
 */
public class SingleElementListSerializer extends JsonSerializer<List<DetailErrorResponse>> {

    @Override
    public void serialize(List<DetailErrorResponse> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            gen.writeStartArray();
            gen.writeEndArray();
        } else if (value.size() == 1) {
            gen.writeObject(value.getFirst());
        } else {
            gen.writeStartArray();
            for (DetailErrorResponse error : value) {
                gen.writeObject(error);
            }
            gen.writeEndArray();
        }
    }
}
