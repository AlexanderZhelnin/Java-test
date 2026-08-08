package com.example.javatest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Тип графического образа
 */
public enum GrTypeEnum {
    /** Отсутствует */
    @SerializedName("0")
    EMPTY(0),
    /** Линия */
    @SerializedName("1")
    LINE(1),
    /** Полигон */
    @SerializedName("2")
    POLYGON(2),
    /** Условное обозначение */
    @SerializedName("3")
    BLOCK(3),
    /** Кривая типа сплайн */
    SPLINE(4),
    /** Кривая типа безье */
    BEZIER(5),
    /** Эллипс */
    ELLIPSE(6),
    /** Сектор */
    PIE(7),
    /** Путь(составной, может состоять из различных графических примитивов) */
    PATH(8),
    /** Замкнутый залитый путь */
    FILL_PATH(9),
    /** Надпись */
    TEXT(10),
    /** Контрол */
    VISUAL(12),
    /** Круг */
    CIRCLE(13);

    private final int value;

    GrTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GrTypeEnum fromValue(int value) {
        for (GrTypeEnum type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown GrTypeEnum value: " + value);
    }
}
