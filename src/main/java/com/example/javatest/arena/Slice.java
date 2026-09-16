package com.example.javatest.arena;

public class Slice<T> {

    public Object[] array;
    public int start;
    public int length;

    public Slice() {

    }

    public Slice(T[] array, int start, int length) {
        this.array = array;
        this.start = start;
        this.length = length;
    }
}
