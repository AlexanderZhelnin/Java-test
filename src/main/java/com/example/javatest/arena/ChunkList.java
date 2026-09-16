package com.example.javatest.arena;

public class ChunkList<T> {

    private ArenaAllocator<T> allocator;
    private ArenaAllocator<Slice<T>> sliceAllocator;

    public Slice<T> items;
    public int count = 0;

    public void setAllocator(ArenaAllocator<T> allocator) {
        this.allocator = allocator;
    }

    public void setSliceAllocator(ArenaAllocator<Slice<T>> allocator) {
        sliceAllocator = allocator;
        items = getItems(10000);
    }

    private Slice<T> getItems(int length) {
        return allocator.alloc(length, sliceAllocator);
    }

    public ChunkList() {
    }

    int growCap(int oldCap, int need) {
        var minGrow = 256;
        var newCap = Math.max(oldCap, minGrow);
        while (newCap < need)
            newCap *= 2;

        return newCap;
    }

    public T add() {

        var newCount = count + 1;
        if (newCount > items.length)
            allocator.increase(items, growCap(count, newCount));

        @SuppressWarnings("unchecked")
        var result = (T) items.array[count++];

        return result;
    }
}
