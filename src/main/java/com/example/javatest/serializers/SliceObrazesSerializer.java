package com.example.javatest.serializers;

import java.io.IOException;

import com.example.javatest.models.IObrazBlazing1;
import com.example.javatest.models.ObrazBlazing1;
import com.example.javatest.arena.ChunkList;
import com.example.javatest.arena.Slice;
import com.example.javatest.arena.SliceDouble;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SliceObrazesSerializer extends
        JsonSerializer<Slice<ObrazBlazing1>> {

    @Override
    public void serialize(
            Slice<ObrazBlazing1> value, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider)
            throws IOException {

        if (value != null) {

            jsonGenerator.writeStartArray();

            var lastIndex = value.start + value.length;
            for (var i = value.start; i < lastIndex; i++) {
                var g = (IObrazBlazing1) value.array[i];

                if (g == null)
                    continue;
                jsonGenerator.writeStartObject();

                jsonGenerator.writeFieldName("name");
                jsonGenerator.writeString(g.getName());

                jsonGenerator.writeFieldName("coords");

                writeCoords(g.getCoords(), jsonGenerator);

                jsonGenerator.writeEndObject();
                // jsonGenerator.writeNumber(value.items.array[i]);
            }

            jsonGenerator.writeEndArray();
        } else {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeEndArray();
        }
    }

    private void writeCoords(SliceDouble value, JsonGenerator jsonGenerator) throws IOException {
        if (value != null) {

            jsonGenerator.writeStartArray();

            var lastIndex = value.start + value.length;
            for (var i = value.start; i < lastIndex; i++)
                jsonGenerator.writeNumber(value.array[i]);

            jsonGenerator.writeEndArray();
        } else {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeEndArray();
        }
    }

}
