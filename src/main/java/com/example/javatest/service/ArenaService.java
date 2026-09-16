package com.example.javatest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

// import org.springframework.context.annotation.Scope;
// import org.springframework.stereotype.Service;

import com.example.javatest.arena.*;
import com.example.javatest.models.*;

@Service
@RequestScope
public class ArenaService implements AutoCloseable {

    public Arenas arenas;

    public ArenaService() {

        System.out.println("Инициализация Арен");

        arenas = new Arenas();
        arenas.doubleAllocator = ArenaDoubleAllocator.get();
        arenas.obrazAllocator = ArenaAllocator.<ObrazBlazing1>get(ObrazBlazing1.class, 10000);
        // arenas.obrazesAllocator = ArenaAllocator.<ChunkList<ObrazBlazing1>>get(ChunkList.class, 10000);
        arenas.sliceObrazesAllocator = ArenaAllocator.<Slice<ObrazBlazing1>>get(Slice.class, 10000);
    }

    @Override
    public void close() {

        System.out.println("Закрытие Арен");
        arenas.doubleAllocator.close();
        arenas.obrazAllocator.close();
        // arenas.obrazesAllocator.close();
        arenas.sliceObrazesAllocator.close();

        arenas.doubleAllocator = null;
        arenas.obrazAllocator = null;
        // arenas.obrazesAllocator = null;
        arenas.sliceObrazesAllocator = null;

    }

}
