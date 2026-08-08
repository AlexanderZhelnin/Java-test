package com.example.javatest.controller;

import com.example.javatest.models.*;
import com.example.javatest.service.DrawerService;
import com.example.javatest.service.InitService;
import com.example.javatest.service.StringsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * REST API контроллер для картографических операций
 */
@RestController
// @RequestMapping("/")
public class TestController {

    private final InitService initService;
    private final DrawerService drawerService;
    private final StringsService stringsService;

    // Константы из Init.cs
    private static final double DEFAULT_LEFT = 1200;
    private static final double DEFAULT_BOTTOM = 50;
    private static final double DEFAULT_RIGHT = 4000;
    private static final double DEFAULT_TOP = 2850;
    private static final double DEFAULT_SCALE = 0.37037037037037035;

    public TestController(InitService initService, DrawerService drawerService, StringsService stringsService) {
        this.initService = initService;
        this.drawerService = drawerService;
        this.stringsService = stringsService;
    }

    /**
     * Приветственное сообщение
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello World Java!";
    }

    /**
     * Чтение файла data.txt
     */
    @GetMapping("/readfile")
    public String readFile() throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("data.txt")));
    }

    /**
     * Вычисление числа Фибоначчи (тест производительности)
     */
    @GetMapping("/fibonacci")
    public double fibonacci() {
        double a = 0.0;
        double b = 1.0;
        for (int i = 2; i < 2_000_000; i++) {
            double temp = a + b;
            a = b;
            b = temp;
        }
        return a;
    }

    /**
     * Получение преобразованных геоданных (тест без реального ответа)
     */
    @GetMapping("/map")
    public int map(@RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "0") double y) {
        x /= 100;
        y /= 100;

        DrawProperties pr = new DrawProperties();
        pr.mashtab = 100;
        pr.scale = DEFAULT_SCALE;
        pr.left = DEFAULT_LEFT + x;
        pr.top = DEFAULT_TOP + y;

        Rect rect = new Rect();
        rect.left = DEFAULT_LEFT + x;
        rect.top = DEFAULT_TOP + y;
        rect.bottom = DEFAULT_BOTTOM;
        rect.right = DEFAULT_RIGHT;

        ILayer[] result = drawerService.build(this.initService.ls, pr, rect);

        return result.length;
    }

    /**
     * Получение преобразованных геоданных, возвращается реальный результат
     */
    @GetMapping("/mapjson")
    public ILayer[] mapJSON(@RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "0") double y) {
        x /= 100;
        y /= 100;

        DrawProperties pr = new DrawProperties();
        pr.mashtab = 100;
        pr.scale = DEFAULT_SCALE;
        pr.left = DEFAULT_LEFT + x;
        pr.top = DEFAULT_TOP + y;

        Rect rect = new Rect();
        rect.left = DEFAULT_LEFT + x;
        rect.top = DEFAULT_TOP + y;
        rect.bottom = DEFAULT_BOTTOM;
        rect.right = DEFAULT_RIGHT;

        ILayer[] result = drawerService.build(this.initService.ls, pr, rect);

        if (result.length < 5)
            return result;
        else
            return Arrays.copyOf(result, 5);
    }

    /**
     * Натуральное сравнение строк (10000 пар строк)
     */
    @GetMapping("/naturalsort")
    public int naturalSort() {
        String STR1 = "asrgfsadf12421";
        String STR2 = "asrgfsadf12321";

        int result = 0;
        for (int i = 0; i < 10_000; i++) {
            result += stringsService.compare(STR1 + i, STR2 + i);
        }

        return result;
    }
}
