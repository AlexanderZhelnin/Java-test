package com.example.javatest.serializers;

import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MemorySegmentDoubleSerializer extends
        JsonSerializer<MemorySegment> {

    @Override
    public void serialize(MemorySegment value, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider)
            throws IOException {

        if (value != null) {

            var byteSize = value.byteSize();
            var doubleCount = byteSize / ValueLayout.JAVA_DOUBLE.byteSize();

            jsonGenerator.writeStartArray();

            try {
                for (var i = 0; i < doubleCount; i++)
                    jsonGenerator.writeNumber(value.getAtIndex(ValueLayout.JAVA_DOUBLE, i));
            } catch (Exception e) {

                System.out.print("Ошибка доступа");
                // System.out.print(e);
            }

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
