package com.example.javatest.service;

import com.example.javatest.arena.*;
import com.example.javatest.models.*;

// import java.lang.foreign.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

/** Сервис отрисовки */
@Service
public final class DrawerService {

    private final PolygonService polygonService;
    private final PolylineService polylineService;
    private final CalcService calcService;

    public DrawerService(PolygonService polygonService, PolylineService polylineService, CalcService calcService) {
        this.polygonService = polygonService;
        this.polylineService = polylineService;
        this.calcService = calcService;
    }

    /** Отсечение графических образов по прямоугольнику */
    private void clipPrimitives(ILegend l, Rect rect, Consumer<Primitive> emit) {

        for (var g : l.primitives) {
            if (g.rect.left >= rect.left && g.rect.bottom >= rect.bottom && g.rect.right <= rect.right
                    && g.rect.top <= rect.top) {
                // Целиком лежит внутри прямоугольника
                emit.accept(g);
            } else if (g.rect.left < rect.right && g.rect.bottom < rect.top && g.rect.right > rect.left
                    && g.rect.top > rect.bottom) {
                // Необходимо отсекать
                switch (l.type) {
                    case LINE:
                        var csList = polylineService.clipPolyline(g, rect);
                        for (var cs : csList) {
                            var obraz = new Primitive();
                            obraz.name = g.name;
                            obraz.coords = cs;
                            emit.accept(obraz);
                        }
                        break;
                    case POLYGON:
                        var cs = polygonService.clipPolygon(g, rect);
                        if (cs.length > 0) {
                            var obraz = new Primitive();
                            obraz.name = g.name;
                            obraz.coords = cs;
                            emit.accept(obraz);
                        }
                        break;
                }
            }
        }
    }

    /** Подготовка данных для отрисовки */
    public List<Layer> build(ILegend[] ls, DrawProperties pr, Rect rect) {
        var result = new ArrayList<Layer>();

        var mashtab = 1 / pr.scale;

        for (ILegend l : ls) {
            if (l.mashtabRange.min > pr.mashtab || l.mashtabRange.max < pr.mashtab)
                continue;

            var mas = new ArrayList<Obraz>(l.primitives.length);

            clipPrimitives(l, rect, (obraz) -> {
                var csOpt = calcService.optimize(obraz.coords.clone(), mashtab);
                calcService.translate(csOpt, pr);

                var newObraz = new Obraz();
                newObraz.name = obraz.name;
                newObraz.coords = csOpt;
                mas.add(newObraz);
            });

            var layer = new Layer();
            layer.legendId = l.id;
            layer.obrazes = mas;
            result.add(layer);
        }

        return result;
    }

    /** Подготовка данных для отрисовки */
    public List<LayerBlazing1> build(Arenas arenas, ILegend[] ls, DrawProperties pr,
            Rect rect) {

        var result = new ArrayList<LayerBlazing1>();

        var mashtab = 1 / pr.scale;

        for (ILegend l : ls) {
            if (l.mashtabRange.min > pr.mashtab ||
                    l.mashtabRange.max < pr.mashtab)
                continue;

            var obrazes = arenas.obrazAllocator.alloc(l.primitives.length, arenas.sliceObrazesAllocator);

            var array = obrazes.array;
            // var i = obrazes.start;
             var i = new Indexer(obrazes.start);

            clipPrimitives(l, rect, (obraz) -> {

                var csOpt = calcService.optimize(arenas.doubleAllocator, obraz.coords, mashtab);
                calcService.translate(csOpt, pr);

                var newObraz = (ObrazBlazing1) array[i.i++];
                newObraz.name = obraz.name;
                newObraz.coords = csOpt;
            });

            var layer = new LayerBlazing1();
            layer.legendId = l.id;
            layer.obrazes = obrazes;
            result.add(layer);
        }

        return result;
    }

}
