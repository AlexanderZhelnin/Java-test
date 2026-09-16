package com.example.javatest;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Главный класс приложения Java Test REST API */
@SpringBootApplication
public class JavaTestApplication {

    public static void main(String[] args) {

        SpringApplication.run(JavaTestApplication.class, args);
        System.out.println("Java Test REST API запущен!");
    }
}
