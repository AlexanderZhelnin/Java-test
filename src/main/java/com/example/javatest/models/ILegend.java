package com.example.javatest.models;

/**
 * Легенда для отображения данных
 */
public class ILegend {
    /** Уникальный идентификатор */
    public Long id;
    /** Тип графического образа */
    public GrTypeEnum type;
    /** Диапазон видимости */
    public IMashtabRange mashtabRange;
    /** Приоритет */
    public int priority;
    /** Условное обозначение */
    public ILegendBlock block;
    /** Заливка */
    public ILegendFill fill;
    /** Граница */
    public ILegendBorder border;
    /** Надпись */
    public ILegendText text;
    /** Графические примитивы */
    public IPrimitive[] primitives;
}
