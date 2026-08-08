package com.example.javatest.service;

import org.springframework.stereotype.Service;

import com.example.javatest.models.DrawProperties;

/**
 * Сервис вычислений координат
 */
@Service
public class CalcService {

    /**
     * Преобразование в систему координат экрана
     */
    public void translate(double[] cs, DrawProperties pr) {
        for (int i = 0; i < cs.length; i += 2) {

            cs[i] = (cs[i] - pr.left) * pr.scale;
            cs[i + 1] = -(cs[i + 1] - pr.top) * pr.scale;
        }
    }

    /**
     * Удаление точек которые не влияют на внешний вид
     */
    public double[] optimize(double[] mas, double l) {
        int count = mas.length;
        if (count < 5)
            return mas;

        java.util.List<Double> coords = new java.util.ArrayList<>();

        double lastCoord1X = mas[0];
        double lastCoord1Y = mas[1];
        double lastCoord2X = mas[2];
        double lastCoord2Y = mas[3];

        coords.add(lastCoord1X);
        coords.add(lastCoord1Y);

        double lSq = l * l;

        for (int i = 4; i < count; i += 2) {
            if (!isPointOnLine(lastCoord1X, lastCoord1Y, lastCoord2X, lastCoord2Y, mas[i], mas[i + 1], lSq)) {
                lastCoord1X = mas[i - 2];
                lastCoord1Y = mas[i - 1];
                lastCoord2X = mas[i];
                lastCoord2Y = mas[i + 1];

                coords.add(lastCoord1X);
                coords.add(lastCoord1Y);
            }
        }

        coords.add(mas[count - 2]);
        coords.add(mas[count - 1]);

        return coords.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Находится ли следующая точка на линии с определённым допуском
     */
    public boolean isPointOnLine(double p1X, double p1Y, double p2X, double p2Y, double pX, double pY, double l) {
        double abX = pX - p1X;
        double abY = pY - p1Y;

        double cdX = p2X - p1X;
        double cdY = p2Y - p1Y;

        double lenSQ = cdX * cdX + cdY * cdY;

        double param = (lenSQ != 0)
                ? (abX * cdX + abY * cdY) / lenSQ
                : -1.0;

        double xx, yy;
        if (param < 0) {
            xx = p1X;
            yy = p1Y;
        } else if (param > 1) {
            xx = p2X;
            yy = p2Y;
        } else {
            xx = p1X + param * cdX;
            yy = p1Y + param * cdY;
        }

        double dx = pX - xx;
        double dy = pY - yy;

        return dx * dx + dy * dy < l;
    }
}
