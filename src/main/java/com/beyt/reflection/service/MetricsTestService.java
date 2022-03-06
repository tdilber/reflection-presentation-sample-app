package com.beyt.reflection.service;

import com.beyt.reflection.aspect.LogTime;
import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

@Service
public class MetricsTestService {

    public static final int LOOP_COUNT = 1000000;

    private Method method;

    public MetricsTestService() throws NoSuchMethodException {
        method = MetricsTestService.class.getDeclaredMethod("randomCalculation");
    }

    @LogTime
    public void empty() {

    }

    @LogTime
    public void normallyRun() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            randomCalculation();
        }
    }

    @LogTime
    public void reflectionFindAllTime() {
        try {
            for (int i = 0; i < LOOP_COUNT; i++) {
                getClass().getDeclaredMethod("randomCalculation").invoke(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @LogTime
    public void reflectionFindOnce() {
        try {
            Method method = getClass().getDeclaredMethod("randomCalculation");
            for (int i = 0; i < LOOP_COUNT; i++) {
                method.invoke(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @LogTime
    public void reflectionOnlyCall() {
        try {
            for (int i = 0; i < LOOP_COUNT; i++) {
                method.invoke(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void randomCalculation() {
        double result = 1;
        for (int i = 0; i < 150; i++) {
            double random = Math.random();
            result *= random + 19;
            result /= random + 1;
        }
    }

}
