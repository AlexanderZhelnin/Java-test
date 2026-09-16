package com.example.javatest.interceptors;

// import java.lang.foreign.Arena;

// import org.springframework.context.annotation.Bean;
// import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.javatest.arena.*;
import com.example.javatest.models.*;

// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ArenaInterceptor implements HandlerInterceptor {

    private boolean isNeedArena(Object handler) {
        if (handler instanceof HandlerMethod) {
            NeedArena needArena = ((HandlerMethod) handler).getMethodAnnotation(NeedArena.class);
            if (null == needArena) {
                needArena = ((HandlerMethod) handler).getMethod().getDeclaringClass()
                        .getAnnotation(NeedArena.class);
            }

            return null != needArena;
        }
        return false;
    }

    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (isNeedArena(handler)) {
            var arenas = new Arenas();
            arenas.doubleAllocator = ArenaDoubleAllocator.get();
            arenas.obrazAllocator = ArenaAllocator.<ObrazBlazing1>get(ObrazBlazing1.class, 10000);
            arenas.sliceObrazesAllocator = ArenaAllocator.<Slice<ObrazBlazing1>>get(Slice.class, 10000);

            request.setAttribute("Arena", arenas);
        }
        return true;
    }

    @SuppressWarnings("null")
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @SuppressWarnings("null")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        var a = (Arenas) request.getAttribute("Arena");
        if (a == null)
            return;

        a.doubleAllocator.close();
        a.obrazAllocator.close();
        // a.obrazesAllocator.close();
        a.sliceObrazesAllocator.close();

        a.doubleAllocator = null;
        a.obrazAllocator = null;
        // a.obrazesAllocator = null;
        a.sliceObrazesAllocator = null;
    }
}
