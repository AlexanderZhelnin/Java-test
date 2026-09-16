package com.example.javatest.arena;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ArenaDoubleAllocator implements AutoCloseable {

    private static final ConcurrentLinkedDeque<ArenaDoubleAllocator> pools = new ConcurrentLinkedDeque<>();

    private ArenaAllocator<SliceDouble> arenaSliceDouble;
    private double[] buffer;

    private int count = 0;

    public ArenaDoubleAllocator() {
        this(1 << 18);
    }

    public ArenaDoubleAllocator(int copacity) {
        buffer = new double[copacity];
        arenaSliceDouble = ArenaAllocator.get(SliceDouble.class, 5000);
    }

    public static ArenaDoubleAllocator get() {
        return get(1 << 18);
    }

    public static ArenaDoubleAllocator get(int copacity) {

        var result = pools.poll();
        if (result == null)
            result = new ArenaDoubleAllocator(copacity);

        return result;
    }

    public SliceDouble alloc(int length) {

        var newCount = count + length;
        if (newCount > buffer.length)
            buffer = new double[growCap(buffer.length, newCount)];

        var result = arenaSliceDouble.alloc();

        result.array = buffer;
        result.start = count;
        result.length = length;

        count = newCount;

        return result;
    }

    private static int growCap(int oldCap, int need) {
        var newCap = Math.max(oldCap, 256);

        while (newCap < need)
            newCap *= 2;
        return newCap;
    }

    public void close() {

        count = 0;

        arenaSliceDouble.close(true);
        pools.add(this);
    }

}
