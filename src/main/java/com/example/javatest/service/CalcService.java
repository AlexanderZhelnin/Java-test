package com.example.javatest.service;

import java.lang.foreign.*;
import java.util.ArrayList;
// import java.util.List;

import org.springframework.stereotype.Service;

import com.example.javatest.arena.ArenaDoubleAllocator;
import com.example.javatest.arena.SliceDouble;
import com.example.javatest.models.DrawProperties;

/** Сервис вычислений координат */
@Service
public final class CalcService {

    /** Преобразование в систему координат экрана */
    public void translate(double[] cs, DrawProperties pr) {
        for (var i = 0; i < cs.length; i += 2) {

            cs[i] = (cs[i] - pr.left) * pr.scale;
            cs[i + 1] = -(cs[i + 1] - pr.top) * pr.scale;
        }
    }

    /** Преобразование в систему координат экрана */
    public void translate(MemorySegment cs, DrawProperties pr) {

        var byteSize = cs.byteSize();
        var doubleCount = byteSize / ValueLayout.JAVA_DOUBLE.byteSize();

        for (var i = 0; i < doubleCount; i += 2) {

            cs.setAtIndex(ValueLayout.JAVA_DOUBLE, i, (cs.getAtIndex(
                    ValueLayout.JAVA_DOUBLE, i) - pr.left) * pr.scale);

            cs.setAtIndex(ValueLayout.JAVA_DOUBLE, i + 1, -(cs.getAtIndex(
                    ValueLayout.JAVA_DOUBLE, i + 1) - pr.top) * pr.scale);

        }
    }

    /** Преобразование в систему координат экрана */
    public void translate(SliceDouble cs, DrawProperties pr) {

        var lastIndex = cs.start + cs.length;
        var array = cs.array;

        for (var i = cs.start; i < lastIndex; i += 2) {
            array[i] = (array[i] - pr.left) * pr.scale;
            array[i + 1] = -(array[i + 1] - pr.top) * pr.scale;
        }
    }

    /** Удаление точек которые не влияют на внешний вид */
    public double[] optimize(double[] mas, double l) {
        var count = mas.length;
        if (count < 5)
            return mas;

        var coords = new ArrayList<Double>(mas.length);

        var lastCoord1X = mas[0];
        var lastCoord1Y = mas[1];
        var lastCoord2X = mas[2];
        var lastCoord2Y = mas[3];

        coords.add(lastCoord1X);
        coords.add(lastCoord1Y);

        var lSq = l * l;

        for (var i = 4; i < count; i += 2) {
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

    /** Удаление точек которые не влияют на внешний вид */
    public MemorySegment optimize(Arena arena, double[] mas, double l) {
        var count = mas.length;
        if (count < 5)
            return MemorySegment.ofArray(mas);

        var coords = arena.allocate(ValueLayout.JAVA_DOUBLE, mas.length);

        var lastCoord1X = mas[0];
        var lastCoord1Y = mas[1];
        var lastCoord2X = mas[2];
        var lastCoord2Y = mas[3];

        var index = 0;
        coords.setAtIndex(ValueLayout.JAVA_DOUBLE, index++, lastCoord1X);
        coords.setAtIndex(ValueLayout.JAVA_DOUBLE, index++, lastCoord1Y);

        var lSq = l * l;

        for (var i = 4; i < count; i += 2) {
            if (!isPointOnLine(lastCoord1X, lastCoord1Y, lastCoord2X, lastCoord2Y,
                    mas[i], mas[i + 1], lSq)) {
                lastCoord1X = mas[i - 2];
                lastCoord1Y = mas[i - 1];
                lastCoord2X = mas[i];
                lastCoord2Y = mas[i + 1];

                coords.setAtIndex(ValueLayout.JAVA_DOUBLE, index++, lastCoord1X);
                coords.setAtIndex(ValueLayout.JAVA_DOUBLE, index++, lastCoord1Y);
            }
        }

        coords.setAtIndex(ValueLayout.JAVA_DOUBLE, index++, mas[count - 2]);
        coords.setAtIndex(ValueLayout.JAVA_DOUBLE, index++, mas[count - 1]);

        return coords.asSlice(0, index * ValueLayout.JAVA_DOUBLE.byteSize());
    }

    /** Удаление точек которые не влияют на внешний вид */
    public SliceDouble optimize(ArenaDoubleAllocator arena, double[] mas, double l) {
        var count = mas.length;
        if (count < 5) {
            var result = arena.alloc(mas.length);
            System.arraycopy(mas, 0, result.array, result.start, mas.length);
            return result;
        }

        var coords = arena.alloc(mas.length);

        var lastCoord1X = mas[0];
        var lastCoord1Y = mas[1];
        var lastCoord2X = mas[2];
        var lastCoord2Y = mas[3];

        var index = coords.start;

        coords.array[index++] = lastCoord1X;
        coords.array[index++] = lastCoord1Y;

        var lSq = l * l;

        for (var i = 4; i < count; i += 2) {
            if (!isPointOnLine(lastCoord1X, lastCoord1Y, lastCoord2X, lastCoord2Y,
                    mas[i], mas[i + 1], lSq)) {
                lastCoord1X = mas[i - 2];
                lastCoord1Y = mas[i - 1];
                lastCoord2X = mas[i];
                lastCoord2Y = mas[i + 1];

                coords.array[index++] = lastCoord1X;
                coords.array[index++] = lastCoord1Y;
            }
        }

        coords.array[index++] = mas[count - 2];
        coords.array[index++] = mas[count - 1];

        coords.length = index - coords.start;
        return coords;
    }

    /** Находится ли следующая точка на линии с определённым допуском */
    public boolean isPointOnLine(double p1X, double p1Y, double p2X, double p2Y, double pX, double pY, double l) {
        var abX = pX - p1X;
        var abY = pY - p1Y;

        var cdX = p2X - p1X;
        var cdY = p2Y - p1Y;

        var lenSQ = cdX * cdX + cdY * cdY;

        var param = (lenSQ != 0)
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

        var dx = pX - xx;
        var dy = pY - yy;

        return dx * dx + dy * dy < l;
    }
}
