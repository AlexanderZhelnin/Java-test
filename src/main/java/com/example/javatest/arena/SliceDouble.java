package com.example.javatest.arena;

public class SliceDouble {

    public double[] array;
    public int start;
    public int length;

    public SliceDouble() {
    }

    public SliceDouble(double[] array, int start, int length) {
        this.array = array;
        this.start = start;
        this.length = length;
    }
}
