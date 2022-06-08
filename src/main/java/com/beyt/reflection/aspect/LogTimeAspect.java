package com.beyt.reflection.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogTimeAspect {

    @Around("@annotation(LogTime)")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long duration = System.nanoTime() - startTime;

        log.info("Method Name : {} Duration : {} nano sec", joinPoint.getSignature().getName(), duration);

        return result;
    }

}
