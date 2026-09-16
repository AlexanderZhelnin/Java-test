package com.example.javatest.models;

import com.example.javatest.arena.SliceDouble;
import com.example.javatest.serializers.SliceDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** Данные для отображения (графический образ) */
public class ObrazBlazing1 implements IObrazBlazing1 {
    /** Имя графического образа */
    public String name;
    /** Координаты графического образа */
    @JsonSerialize(using = SliceDoubleSerializer.class)
    public SliceDouble coords;

    @Override
    public String getName() {
        return name;
    }
    @Override
    public SliceDouble getCoords() {
        return coords;
    }
}
