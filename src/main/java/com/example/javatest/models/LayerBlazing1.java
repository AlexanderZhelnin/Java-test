package com.example.javatest.models;

import java.util.List;

import com.example.javatest.arena.ChunkList;
import com.example.javatest.arena.Slice;
import com.example.javatest.serializers.SliceObrazesSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** Результирующий слой для отображения */
public class LayerBlazing1 {
    /** Уникальный идентификатор */
    public Long legendId;
    /** Координаты для отрисовки */
    @JsonSerialize(using = SliceObrazesSerializer.class)
    public Slice<ObrazBlazing1> obrazes;

    // public ChunkList<ObrazBlazing1> obrazes;
}
