package com.example.javatest.models;

/**
 * Стиль заливки
 */
public enum FillStyleEnum {
    /** Сплошная заливка */
    SOLID(1),
    /** Без заливки */
    TRANSPARENT(2),
    /** Заливка с использованием градиентной заливки */
    LINEAR_GRADIENT(3),
    /** Заливка с использованием текстуры */
    TEXTURED(4),
    /** Заливка с использованием штриховки */
    HATCH(5);

    private final int value;

    FillStyleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
