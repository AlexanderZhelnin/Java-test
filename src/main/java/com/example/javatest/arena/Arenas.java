package com.example.javatest.arena;

import com.example.javatest.models.*;

public class Arenas {

    public ArenaDoubleAllocator doubleAllocator;
    public ArenaAllocator<ObrazBlazing1> obrazAllocator;
    // public ArenaAllocator<ChunkList<ObrazBlazing1>> obrazesAllocator;
    public ArenaAllocator<Slice<ObrazBlazing1>> sliceObrazesAllocator;

}
