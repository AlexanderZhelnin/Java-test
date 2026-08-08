package com.example.javatest.models;

/**
 * Ориентация для градиентной заливки
 */
public enum GradientStyle {
    /** Горизонтальная - Слева на право */
    LEFT2RIGHT,
    /** Горизонтальная - С право на лево */
    RIGHT2LEFT,
    /** Вертикальная - Снизу вверх */
    BOTTOM2TOP,
    /** Вертикальная - С верху вниз */
    TOP2BOTTOM,
    /** Диагональная от нижней левой точки к правой верхней */
    LEFT_BOTTOM2RIGHT_TOP,
    /** Диагональная от правой верхней к нижней левой точки */
    RIGHT_TOP2LEFT_BOTTOM,
    /** Диагональная от правой нижней к верхней левой */
    RIGHT_BOTTOM2LEFT_TOP,
    /** Диагональная от верхней левой к правой нижней */
    LEFT_TOP2RIGHT_BOTTOM
}
