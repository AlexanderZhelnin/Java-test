package com.example.javatest.arena;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class ArenaAllocator<T> {

    public static int countArenas = 0;
    private static final HashMap<Class<?>, ConcurrentLinkedDeque<ArenaAllocator<?>>> pools = new HashMap<>(20);
    private Object[] buffer;

    private int count = 0;
    private Class<?> c;
    private Constructor<?> tConstructor;

    public ArenaAllocator() {
        this(1 << 18);
    }

    public ArenaAllocator(int copacity) {
        buffer = new Object[copacity];
    }

    public static <T> ArenaAllocator<T> get(Class<?> c) {
        return ArenaAllocator.<T>get(c, 1 << 18);
    }

    public static <T> ArenaAllocator<T> get(Class<?> c, int copacity) {

        ConcurrentLinkedDeque<ArenaAllocator<?>> query = getPool(c);

        @SuppressWarnings("unchecked")
        var arena = (ArenaAllocator<T>) query.poll();
        if (arena == null) {
            countArenas++;
            arena = new ArenaAllocator<T>(copacity);
            arena.c = c;
            try {
                arena.tConstructor = c.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
            }
        }
        return arena;
    }

    private static ConcurrentLinkedDeque<ArenaAllocator<?>> getPool(Class<?> c) {
        ConcurrentLinkedDeque<ArenaAllocator<?>> query;
        synchronized (pools) {
            query = pools.get(c);
            if (query == null)
                pools.put(c, query = new ConcurrentLinkedDeque<>());
        }
        return query;
    }

    public T alloc() {

        var newCount = count + 1;
        checkBufferSize(newCount);

        @SuppressWarnings("unchecked")
        T result = (T) buffer[count];

        // Если ещё не было создано объекта в массиве
        if (result == null) {
            try {
                @SuppressWarnings("unchecked")
                T result1 = (T) tConstructor.newInstance();
                result = result1;

            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            buffer[count] = result;
        }
        count++;

        return result;
    }

    public Slice<T> alloc(int length, ArenaAllocator<Slice<T>> sliceAllocator) {

        var newCount = count + length;
        checkBufferSize(newCount);

        var result = sliceAllocator.alloc();

        @SuppressWarnings("unchecked")
        var array = (T[]) buffer;
        result.array = array;

        result.start = count;
        result.length = length;
        count = newCount;

        // Инициализируем элементы Среза
        initItems(result.start, count);

        return result;
    }

    private static int growCap(int oldCap, int need) {
        var newCap = Math.max(oldCap, 256);

        while (newCap < need)
            newCap *= 2;
        return newCap;
    }

    private final void checkBufferSize(int needed) {
        if (needed > buffer.length) {
            buffer = new Object[growCap(buffer.length, needed)];
        }
    }

    private void initItems(int start, int end) {
        for (var i = start; i < end; i++) {
            @SuppressWarnings("unchecked")
            var resultItem = (T) buffer[i];

            if (resultItem == null) {
                try {
                    @SuppressWarnings("unchecked")
                    var result1 = (T) tConstructor.newInstance();
                    resultItem = result1;

                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
                buffer[i] = resultItem;
            }
        }
    }

    public boolean increase(Slice<T> slice, int need) {

        if (!(count == slice.start + slice.length && slice.start + need < buffer.length
                && buffer == (Object[]) slice.array))
            return false;

        slice.length = need;
        count = slice.start + need;

        initItems(slice.start, slice.start + slice.length);

        return true;
    }

    public void close() {
        close(false);
    }

    public void close(boolean onlyFields) {

        count = 0;

        if (onlyFields)
            return;

        getPool(c).add(this);
    }
}
