package com.example.javatest.service;

import com.example.javatest.models.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Сервис отрисовки (аналог Drawer.cs)
 */
@Service
public class DrawerService {

    private final PolygonService polygonService;
    private final PolylineService polylineService;
    private final CalcService calcService;

    public DrawerService(PolygonService polygonService, PolylineService polylineService, CalcService calcService) {
        this.polygonService = polygonService;
        this.polylineService = polylineService;
        this.calcService = calcService;
    }

    /**
     * Отсечение графических образов по прямоугольнику
     */
    private List<IObraz> clipPrimitives(ILegend l, Rect rect) {
        List<IObraz> result = new ArrayList<>();

        for (IPrimitive g : l.primitives) {
            if (g.rect.left >= rect.left && g.rect.bottom >= rect.bottom && g.rect.right <= rect.right && g.rect.top <= rect.top) {
                // Целиком лежит внутри прямоугольника
                IObraz obraz = new IObraz();
                obraz.name = g.name;
                obraz.coords = g.coords.clone();
                result.add(obraz);
            } else if (g.rect.left < rect.right && g.rect.bottom < rect.top && g.rect.right > rect.left && g.rect.top > rect.bottom) {
                // Необходимо отсекать
                switch (l.type) {
                    case LINE:
                        List<double[]> csList = polylineService.clipPolyline(g, rect);
                        for (double[] cs : csList) {
                            IObraz obraz = new IObraz();
                            obraz.name = g.name;
                            obraz.coords = cs;
                            result.add(obraz);
                        }
                        break;
                    case POLYGON:
                        double[] cs = polygonService.clipPolygon(g, rect);
                        if (cs.length > 0) {
                            IObraz obraz = new IObraz();
                            obraz.name = g.name;
                            obraz.coords = cs;
                            result.add(obraz);
                        }
                        break;
                }
            }
        }

        return result;
    }

    /**
     * Подготовка данных для отрисовки
     */
    public List<ILayer> build(ILegend[] ls, DrawProperties pr, Rect rect) {
        List<ILayer> result = new ArrayList<>();

        double mashtab = 1 / pr.scale;

        for (ILegend l : ls) {
            if (l.mashtabRange.min > pr.mashtab ||
                    l.mashtabRange.max < pr.mashtab)
                continue;

            List<IObraz> mas = new ArrayList<>();


            for (IObraz obraz : clipPrimitives(l, rect)) {
                double[] csOpt = calcService.optimize(obraz.coords, mashtab);
                calcService.translate(csOpt, pr);

                IObraz newObraz = new IObraz();
                newObraz.name = obraz.name;
                newObraz.coords = csOpt;
                mas.add(newObraz);
            }

            ILayer layer = new ILayer();
            layer.legendId = l.id;
            layer.obrazes = mas;
            result.add(layer);
        }

        return result;
    }
}
