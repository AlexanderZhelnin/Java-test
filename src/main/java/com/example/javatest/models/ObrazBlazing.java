package com.example.javatest.models;

import java.lang.foreign.MemorySegment;

import com.example.javatest.serializers.MemorySegmentDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** Данные для отображения (графический образ) */
public class ObrazBlazing {
    /** Имя графического образа */
    public String name;
    /** Координаты графического образа */
    @JsonSerialize(using = MemorySegmentDoubleSerializer.class)
    public MemorySegment coords;
}
