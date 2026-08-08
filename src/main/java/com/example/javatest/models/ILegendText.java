package com.example.javatest.models;

/**
 * Графические свойства надписи
 */
public class ILegendText {
    /** Диапазон видимости */
    public IMashtabRange mashtabRange;
    /** Опорный масштаб */
    public double mashtabBase;
    /** Масштабируемость */
    public boolean scaled;
    /** Положение */
    public TextPositionEnum position;
    /** Цвет */
    public String color;
    /** Цвет фона */
    public String backColor;
    /** Шрифт */
    public ILegendFont font;
    public boolean isAnalyze;
}
