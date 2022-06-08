package com.beyt.reflection;


import com.beyt.reflection.service.MetricsTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("metrics")
public class ReflectionMetricsTest implements ApplicationRunner {

    private final MetricsTestService metricsTestService;

    public ReflectionMetricsTest(MetricsTestService metricsTestService) {
        this.metricsTestService = metricsTestService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        timedTest();
    }

    protected void timedTest() {
        log.info("--------------- Metrics Starting ------------------");
        metricsTestService.empty();
        metricsTestService.empty();
        log.info("---------------------------------");
        log.info("-------------- Load Test -------------------");
        metricsTestService.normallyRun();
        metricsTestService.normallyRun();
        metricsTestService.normallyRun();
        log.info("---------------------------------");
        metricsTestService.reflectionOnlyCall();
        metricsTestService.reflectionOnlyCall();
        metricsTestService.reflectionOnlyCall();
        log.info("---------------------------------");
        metricsTestService.reflectionFindOnce();
        metricsTestService.reflectionFindOnce();
        metricsTestService.reflectionFindOnce();
        log.info("---------------------------------");
        metricsTestService.reflectionFindAllTime();
        metricsTestService.reflectionFindAllTime();
        metricsTestService.reflectionFindAllTime();
        log.info("---------------------------------");
        log.info("-------------- Free Runners -------------------");
        metricsTestService.freeRunNormal();
        log.info("---------------------------------");
        metricsTestService.freeRunReflectionOnlyCall();
        log.info("---------------------------------");
        metricsTestService.freeRunReflectionFindOnce();
        log.info("---------------------------------");
        metricsTestService.freeRunReflectionFindAllTime();
        log.info("---------------------------------");
        log.info("----------------Instance Create -----------------");
        metricsTestService.instanceCreateNormal();
        log.info("---------------------------------");
        metricsTestService.instanceCreateReflection();
        log.info("--------------- Metrics End ------------------");
    }
}
