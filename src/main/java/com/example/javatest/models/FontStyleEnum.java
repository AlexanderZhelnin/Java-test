package com.example.javatest.models;

/**
 * Стиль шрифта
 */
public enum FontStyleEnum {
    REGULAR(0x0),
    BOLD(0x1),
    ITALIC(0x2),
    UNDERLINE(0x4),
    STRIKEOUT(0x8);

    private final int value;

    FontStyleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
