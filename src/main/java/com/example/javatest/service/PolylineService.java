package com.example.javatest.service;

import com.example.javatest.models.Primitive;
import com.example.javatest.models.Rect;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/** Сервис отсечения полилиний */
@Service
public final class PolylineService {

    private List<double[]> clipLeft(double[] coords, double left) {

        var res = new ArrayList<double[]>();
        if (coords.length == 0)
            return res;

        var pl = new ArrayList<Double>();

        var px1 = coords[0];
        var py1 = coords[1];

        if (px1 >= left) {
            pl.add(px1);
            pl.add(py1);
        }

        for (var i = 2; i < coords.length; i += 2) {
            var px2 = coords[i];
            var py2 = coords[i + 1];

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

                res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());
                pl = new ArrayList<>();
            }
            px1 = px2;
            py1 = py2;
        }
        if (!pl.isEmpty())
            res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());
        return res;
    }

    private List<double[]> clipRight(double[] coords, double right) {
        var res = new ArrayList<double[]>();
        if (coords.length == 0)
            return res;

        var pl = new ArrayList<Double>();

        var px1 = coords[0];
        var py1 = coords[1];

        if (px1 <= right) {
            pl.add(px1);
            pl.add(py1);
        }

        for (var i = 2; i < coords.length; i += 2) {
            var px2 = coords[i];
            var py2 = coords[i + 1];

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

                res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());

                pl = new ArrayList<>();
            }
            px1 = px2;
            py1 = py2;
        }
        if (!pl.isEmpty())
            res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());
        return res;
    }

    private List<double[]> clipBottom(double[] coords, double bottom) {
        var res = new ArrayList<double[]>();
        if (coords.length == 0)
            return res;

        var pl = new ArrayList<Double>();

        var px1 = coords[0];
        var py1 = coords[1];

        if (py1 >= bottom) {
            pl.add(px1);
            pl.add(py1);
        }

        for (var i = 2; i < coords.length; i += 2) {
            var px2 = coords[i];
            var py2 = coords[i + 1];

            if (py1 >= bottom && py2 >= bottom) {
                pl.add(px2);
                pl.add(py2);
            } else if (py1 < bottom && py2 > bottom) {
                pl.add((bottom - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(bottom);

                pl.add(px2);
                pl.add(py2);
            } else if (py1 > bottom && py2 < bottom) {
                pl.add(px1);
                pl.add(py1);

                pl.add((bottom - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(bottom);

                res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());

                pl = new ArrayList<>();
            }
            px1 = px2;
            py1 = py2;
        }
        if (!pl.isEmpty())
            res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());
        return res;
    }

    private List<double[]> clipTop(double[] coords, double top) {
        var res = new ArrayList<double[]>();
        if (coords.length == 0)
            return res;

        var pl = new ArrayList<Double>();

        var px1 = coords[0];
        var py1 = coords[1];

        if (py1 <= top) {
            pl.add(px1);
            pl.add(py1);
        }

        for (var i = 2; i < coords.length; i += 2) {
            var px2 = coords[i];
            var py2 = coords[i + 1];

            if (py1 <= top && py2 <= top) {
                pl.add(px2);
                pl.add(py2);
            } else if (py1 < top && py2 > top) {
                pl.add(px1);
                pl.add(py1);

                pl.add((top - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(top);

                res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());
                pl = new ArrayList<>();
            } else if (py1 > top && py2 < top) {
                pl.add((top - py1) * (px2 - px1) / (py2 - py1) + px1);
                pl.add(top);

                pl.add(px2);
                pl.add(py2);
            }
            px1 = px2;
            py1 = py2;
        }
        if (!pl.isEmpty())
            res.add(pl.stream().mapToDouble(Double::doubleValue).toArray());
        return res;
    }

    /** Отсечение полилинии по прямоугольнику */
    public List<double[]> clipPolyline(Primitive g, Rect rect) {
        List<double[]> res = (g.rect.left < rect.left)
                ? clipLeft(g.coords, rect.left)
                : List.of(g.coords.clone());

        if (g.rect.bottom < rect.bottom) {
            var tmp = new ArrayList<double[]>();
            for (var cs : res)
                tmp.addAll(clipBottom(cs, rect.bottom));
            res = tmp;
        }

        if (g.rect.right > rect.right) {
            var tmp = new ArrayList<double[]>();
            for (var cs : res)
                tmp.addAll(clipRight(cs, rect.right));
            res = tmp;
        }

        if (g.rect.top > rect.top) {
            var tmp = new ArrayList<double[]>();
            for (var cs : res)
                tmp.addAll(clipTop(cs, rect.top));
            res = tmp;
        }

        return res;
    }
}
