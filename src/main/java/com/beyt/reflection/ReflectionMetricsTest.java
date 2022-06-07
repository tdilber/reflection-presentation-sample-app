package com.beyt.reflection;


import com.beyt.reflection.service.MetricsTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReflectionMetricsTest implements ApplicationRunner {

    private final MetricsTestService metricsTestService;

    public ReflectionMetricsTest(MetricsTestService metricsTestService) {
        this.metricsTestService = metricsTestService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //timedTest();
    }

    protected void timedTest() {
        log.info("--------------- Metrics Starting ------------------");
        metricsTestService.empty();
        metricsTestService.empty();
        log.info("---------------------------------");
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
        log.info("--------------- Metrics End ------------------");
    }
}
