package com.example.javatest.models;

/**
 * Выравнивание текста
 */
public enum TextPositionEnum {
    /** По умолчанию Автоматическое расположение текста с анализом */
    DEFAULT(0),
    /** Слева внизу с анализом */
    LEFT_BOTTOM(1),
    /** Лева в центре по вертикали с анализом */
    LEFT_MIDDLE(2),
    /** Слева вверху с анализом */
    LEFT_TOP(3),
    /** Вверху в центре по горизонтали с анализом */
    TOP_MIDDLE(4),
    /** Сверху справа с анализом */
    TOP_RIGHT(5),
    /** Справа в центре по вертикали с анализом */
    RIGHT_MIDDLE(6),
    /** Справа снизу с анализом */
    RIGHT_BOTTOM(7),
    /** Снизу в центре по вертикали с анализом */
    BOTTOM_MIDDLE(8),
    /** Произвольное положение текста явно заданное с анализом */
    POSITION_SETS(9),

    /** Слева внизу с анализом */
    LEFT_BOTTOM_INNER(-1),
    /** Лева в центре по вертикали с анализом */
    LEFT_MIDDLE_INNER(-2),
    /** Слева вверху с анализом */
    LEFT_TOP_INNER(-3),
    /** Вверху в центре по горизонтали с анализом */
    TOP_MIDDLE_INNER(-4),
    /** Сверху справа с анализом */
    TOP_RIGHT_INNER(-5),
    /** Справа в центре по вертикали с анализом */
    RIGHT_MIDDLE_INNER(-6),
    /** Справа снизу с анализом */
    RIGHT_BOTTOM_INNER(-7),
    /** Снизу в центре по вертикали с анализом */
    BOTTOM_MIDDLE_INNER(-8);

    private final int value;

    TextPositionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
