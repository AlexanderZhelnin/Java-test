package com.example.javatest.controller;

import java.io.IOException;
// import java.lang.foreign.Arena;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.example.javatest.models.*;
import com.example.javatest.service.*;

import jakarta.annotation.PreDestroy;

// import jakarta.annotation.PreDestroy;

import com.example.javatest.arena.*;
import com.example.javatest.interceptors.*;

/** REST API контроллер для картографических операций */
@RestController
@RequestMapping("/")
// @RequestScope
public class TestController {

    private final DataService initService;
    private final DrawerService drawerService;
    private final StringsService stringsService;

    private static final double DEFAULT_LEFT = 1200;
    private static final double DEFAULT_BOTTOM = 50;
    private static final double DEFAULT_RIGHT = 4000;
    private static final double DEFAULT_TOP = 2850;
    private static final double DEFAULT_SCALE = 0.37037037037037035;

    // private ArenaService arenaService = null;

    public TestController(
            DataService initService,
            DrawerService drawerService,
            StringsService stringsService) {
        this.initService = initService;
        this.drawerService = drawerService;
        this.stringsService = stringsService;

        // System.out.println("Инициализация контроллера");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World Java!";
    }

    @GetMapping("/readfile")
    public String readFile() throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("data.txt")));
    }

    @GetMapping("/fibonacci")
    public double fibonacci() {
        var a = 0.0;
        var b = 1.0;
        for (var i = 2; i < 2_000_000; i++) {
            var temp = a + b;
            a = b;
            b = temp;
        }
        return a;
    }

    /** Получение преобразованных геоданных (тест без реального ответа) */
    @GetMapping("/map")
    public int map(@RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "0") double y) {
        x /= 100;
        y /= 100;

        var pr = new DrawProperties();
        pr.mashtab = 100;
        pr.scale = DEFAULT_SCALE;
        pr.left = DEFAULT_LEFT + x;
        pr.top = DEFAULT_TOP + y;

        var rect = new Rect();
        rect.left = DEFAULT_LEFT + x;
        rect.top = DEFAULT_TOP + y;
        rect.bottom = DEFAULT_BOTTOM;
        rect.right = DEFAULT_RIGHT;

        var result = drawerService.build(this.initService.ls, pr, rect);

        return result.size();
    }

    @NeedArena()
    @GetMapping("/mapBlazing")
    public int mapBlazing(
        @RequestAttribute("Arena") Arenas arenas,
        @RequestParam(defaultValue = "0") double x,
        @RequestParam(defaultValue = "0") double y) {
        x /= 100;
        y /= 100;

        var pr = new DrawProperties();
        pr.mashtab = 100;
        pr.scale = DEFAULT_SCALE;
        pr.left = DEFAULT_LEFT + x;
        pr.top = DEFAULT_TOP + y;

        var rect = new Rect();
        rect.left = DEFAULT_LEFT + x;
        rect.top = DEFAULT_TOP + y;
        rect.bottom = DEFAULT_BOTTOM;
        rect.right = DEFAULT_RIGHT;

        var result = drawerService.build(arenas, this.initService.ls, pr, rect);

        return result.size();
    }

    /** Получение преобразованных геоданных, возвращается реальный результат */
    @GetMapping("/mapjson")
    public List<Layer> mapJSON(
            @RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "0") double y) {

        x /= 100;
        y /= 100;

        var pr = new DrawProperties();
        pr.mashtab = 100;
        pr.scale = DEFAULT_SCALE;
        pr.left = DEFAULT_LEFT + x;
        pr.top = DEFAULT_TOP + y;

        var rect = new Rect();
        rect.left = DEFAULT_LEFT + x;
        rect.top = DEFAULT_TOP + y;
        rect.bottom = DEFAULT_BOTTOM;
        rect.right = DEFAULT_RIGHT;

        var result = drawerService.build(this.initService.ls, pr, rect);

        if (result.size() < 5)
            return result;
        else
            return result.subList(0, 5);
    }

    /** Получение преобразованных геоданных, возвращается реальный результат */
    @NeedArena()
    @GetMapping("/mapjsonBlazing")
    public List<LayerBlazing1> mapJSONBlazing(
            @RequestAttribute("Arena") Arenas arenas,
            // @Autowired ArenaService arenaService,
            @RequestParam(defaultValue = "0") double x,
            @RequestParam(defaultValue = "0") double y) {
        x /= 100;
        y /= 100;

        var pr = new DrawProperties();
        pr.mashtab = 100;
        pr.scale = DEFAULT_SCALE;
        pr.left = DEFAULT_LEFT + x;
        pr.top = DEFAULT_TOP + y;

        var rect = new Rect();
        rect.left = DEFAULT_LEFT + x;
        rect.top = DEFAULT_TOP + y;
        rect.bottom = DEFAULT_BOTTOM;
        rect.right = DEFAULT_RIGHT;

        var result = drawerService.build(arenas, this.initService.ls,
                pr, rect);

        if (result.size() < 5)
            return result;
        else
            return result.subList(0, 5);
    }

    /** Натуральное сравнение строк (10000 пар строк) */
    @GetMapping("/naturalsort")
    public int naturalSort() {
        var STR1 = "asrgfsadf12421";
        var STR2 = "asrgfsadf12321";

        var result = 0;
        for (var i = 0; i < 10_000; i++) {
            result += stringsService.compare(STR1 + i, STR2 + i);
        }

        return result;
    }

    @GetMapping("/countArenas")
    public int countArenas() {
        return  ArenaAllocator.countArenas;
    }
}
