package com.beyt.reflection.service;

import com.beyt.reflection.aspect.LogTime;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

@Slf4j
@Service
public class MetricsTestService {

    public static final int LOOP_COUNT = 100;

    private final Method method;
    private final Method methodFreeRunner;
    private final DecimalFormat formatter;

    public MetricsTestService() throws NoSuchMethodException {
        method = MetricsTestService.class.getDeclaredMethod("randomCalculation");
        methodFreeRunner = MetricsTestService.class.getDeclaredMethod("freeRunner", Long.class);
        formatter = new DecimalFormat("#,###");
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


    public void freeRunNormal() {
        Long time = System.currentTimeMillis();
        Long counter = 0L;
        while (System.currentTimeMillis() - time < 1000) {
            counter = freeRunner(counter);
        }
        log.info("Free Runner Normal Count : {}", formatter.format(counter));
    }

    @SneakyThrows
    public void freeRunReflectionOnlyCall() {
        Long time = System.currentTimeMillis();
        Long counter = 0L;
        while (System.currentTimeMillis() - time < 1000) {
            counter = (Long) methodFreeRunner.invoke(this, counter);
        }
        log.info("Free Runner ReflectionOnlyCall Count : {}", formatter.format(counter));
    }

    @SneakyThrows
    public void freeRunReflectionFindOnce() {
        Long time = System.currentTimeMillis();
        Long counter = 0L;
        Method methodFreeRunner = MetricsTestService.class.getDeclaredMethod("freeRunner", Long.class);
        while (System.currentTimeMillis() - time < 1000) {
            counter = (Long) methodFreeRunner.invoke(this, counter);
        }
        log.info("Free Runner ReflectionFindOnce Count : {}", formatter.format(counter));
    }

    @SneakyThrows
    public void freeRunReflectionFindAllTime() {
        Long time = System.currentTimeMillis();
        Long counter = 0L;
        while (System.currentTimeMillis() - time < 1000) {
            counter = (Long) MetricsTestService.class.getDeclaredMethod("freeRunner", Long.class).invoke(this, counter);
        }
        log.info("Free Runner ReflectionFindAllTime Count : {}", formatter.format(counter));
    }

    @SneakyThrows
    public void instanceCreateNormal() {
        Long time = System.currentTimeMillis();
        Long counter = 0L;
        while (System.currentTimeMillis() - time < 1000) {
            MetricsTestService metricsTestService = new MetricsTestService();
            counter++;
        }
        log.info("Instance Create Normal Count : {}", formatter.format(counter));
    }

    @SneakyThrows
    public void instanceCreateReflection() {
        Long time = System.currentTimeMillis();
        Long counter = 0L;
        while (System.currentTimeMillis() - time < 1000) {
            MetricsTestService metricsTestService = MetricsTestService.class.getConstructor().newInstance();
            counter++;
        }
        log.info("Instance Create Reflection Count : {}", formatter.format(counter));
    }

    private void randomCalculation() {
        double result = 1;
        for (int i = 0; i < 150; i++) {
            double random = Math.random();
            result *= random + 19;
            result /= random + 1;
        }
    }

    private Long freeRunner(Long count) {
        return count + 1;
    }
}
