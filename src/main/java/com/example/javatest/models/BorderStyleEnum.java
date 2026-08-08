package com.example.javatest.models;

/**
 * Стиль границы
 */
public enum BorderStyleEnum {
    /** Простая */
    SOLID(0),
    /** Пунктирный */
    DASH(1),
    /** Без границы */
    TRANSPARENT(2),
    /** Текстурная */
    TEXTURED(3);

    private final int value;

    BorderStyleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
