package com.example.javatest.service;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.example.javatest.models.ILegend;
import com.example.javatest.models.Rect;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** Сервис инициализации данных */
@Service
public class DataService {

    public ILegend[] ls = new ILegend[0];
    public Rect r;
    public double cx;
    public double cy;

    public DataService() {

        Gson gson = new Gson();
        Path path = Path.of("data.json");
        String content = "[]";
        try {
            content = Files.readString(path);

            this.ls = gson.fromJson(content, new TypeToken<ILegend[]>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        r = new Rect();
        r.left = 1200;
        r.bottom = 50;
        r.right = 4000;
        r.top = 2850;

        cx = (r.right + r.left) / 2;
        cy = (r.bottom + r.top) / 2;
    }
}
