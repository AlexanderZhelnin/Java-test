package com.example.javatest.service;

import com.example.javatest.models.IPrimitive;
import com.example.javatest.models.Rect;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Сервис отсечения полигонов
 */
@Service
public class PolygonService {

    private int getNextIndex(int curIndex, int len) {
        curIndex += 2;
        if (curIndex >= len)
            curIndex = 0;
        return curIndex;
    }

    private double[] clipLeft(double[] coords, double left) {
        if (coords.length == 0)
            return coords;

        List<Double> pl = new ArrayList<>();
        int curIndex = 0;

        double px1 = coords[0];
        double py1 = coords[1];

        if (px1 >= left) {
            pl.add(px1);
            pl.add(py1);
        }

        int len = coords.length / 2;
        for (int i = 1; i <= len; i++) {
            curIndex = getNextIndex(curIndex, coords.length);
            double px2 = coords[curIndex];
            double py2 = coords[curIndex + 1];

            if (px1 >= left && px2 >= left) {
                pl.add(px2);
                pl.add(py2);
            } else if (px1 < left && px2 > left) {
                pl.add(left);
                pl.add((left - px1) * (py2 - py1) / (px2 - px1) + py1);

                pl.add(px2);
                pl.add(py2);
            } else if (px1 > left && px2 < left) {
                pl.add(left);
                pl.add((left - px1) * (py2 - py1) / (px2 - px1) + py1);
            }
            px1 = px2;
            py1 = py2;
        }

        return pl.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private double[] clipRight(double[] coords, double right) {
        if (coords.length == 0)
            return coords;

        List<Double> pl = new ArrayList<>();
        int curIndex = 0;

        double px1 = coords[0];
        double py1 = coords[1];

        if (px1 <= right) {
            pl.add(px1);
            pl.add(py1);
        }
        int len = coords.length / 2;

        for (int i = 0; i < len; i++) {
            curIndex = getNextIndex(curIndex, coords.length);

            double px2 = coords[curIndex];
            double py2 = coords[curIndex + 1];

            if (px1 <= right && px2 <= right) {
                pl.add(px2);
                pl.add(py2);
            } else if (px1 > right && px2 < right) {
                pl.add(right);
                pl.add((right - px1) * (py2 - py1) / (px2 - px1) + py1);

                pl.add(px2);
                pl.add(py2);
            } else if (px1 < right && px2 > right) {
                pl.add(right);
                pl.add((right - px1) * (py2 - py1) / (px2 - px1) + py1);
            }
            px1 = px2;
            py1 = py2;
        }

        return pl.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private double[] clipBottom(double[] coords, double bottom) {
        if (coords.length == 0)
            return coords;

        List<Double> pl = new ArrayList<>();
        int curIndex = 0;

        double px1 = coords[0];
        double py1 = coords[1];

        if (py1 >= bottom) {
            pl.add(px1);
            pl.add(py1);
        }

        int len = coords.length / 2;
        for (int i = 0; i < len; i++) {
            curIndex = getNextIndex(curIndex, coords.length);
            double px2 = coords[curIndex];
            double py2 = coords[curIndex + 1];

            if (py1 >= bottom && py2 >= bottom) {
                pl.add(px2);
                pl.add(py2);
            } else if (py1 < bottom && py2 > bottom) {
                pl.add((bottom - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(bottom);

                pl.add(px2);
                pl.add(py2);
            } else if (py1 > bottom && py2 < bottom) {
                pl.add((bottom - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(bottom);
            }
            px1 = px2;
            py1 = py2;
        }

        return pl.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private double[] clipTop(double[] coords, double top) {
        if (coords.length == 0)
            return coords;

        List<Double> pl = new ArrayList<>();
        int curIndex = 0;

        double px1 = coords[0];
        double py1 = coords[1];

        if (py1 <= top) {
            pl.add(px1);
            pl.add(py1);
        }

        int len = coords.length / 2;
        for (int i = 0; i < len; i++) {
            curIndex = getNextIndex(curIndex, coords.length);
            double px2 = coords[curIndex];
            double py2 = coords[curIndex + 1];

            if (py1 <= top && py2 <= top) {
                pl.add(px2);
                pl.add(py2);
            } else if (py1 > top && py2 < top) {
                pl.add((top - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(top);

                pl.add(px2);
                pl.add(py2);
            } else if (py1 < top && py2 > top) {
                pl.add((top - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(top);
            }

            px1 = px2;
            py1 = py2;
        }

        return pl.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Отсечение полигона по прямоугольнику
     */
    public double[] clipPolygon(IPrimitive g, Rect rect) {
        double[] res = (g.rect.left < rect.left)
                ? clipLeft(g.coords, rect.left)
                : g.coords.clone();

        if (g.rect.bottom < rect.bottom)
            res = clipBottom(res, rect.bottom);

        if (g.rect.right > rect.right)
            res = clipRight(res, rect.right);

        if (g.rect.top > rect.top)
            res = clipTop(res, rect.top);

        return res;
    }
}
