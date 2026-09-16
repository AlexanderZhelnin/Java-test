package com.example.javatest.serializers;

import java.io.IOException;

import com.example.javatest.arena.SliceDouble;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SliceDoubleSerializer extends
        JsonSerializer<SliceDouble> {

    @Override
    public void serialize(
            SliceDouble value, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider)
            throws IOException {

        if (value != null) {

            jsonGenerator.writeStartArray();

            int lastIndex = value.start + value.length;
            for (var i = value.start; i < lastIndex; i++)
                jsonGenerator.writeNumber(value.array[i]);

            jsonGenerator.writeEndArray();
        } else {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeEndArray();
        }
    }

    // @Override
    // public Class<MemorySegment> handledType() {
    // return MemorySegment.class;
    // }
}
