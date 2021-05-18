package com.porejemplo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // * before com.porejemplo stands for access type like public, private etc. * means any type.
    @Pointcut("execution(* com.porejemplo.controller..*.*(..))")
    private void getPointcut() {

    }

    @Before("getPointcut()")
    public void logBefore(JoinPoint joinPoint) {

        logger.info("Log aspect triggered with joinpoint: {}", joinPoint);
    }

    @Around("@annotation(com.porejemplo.aspect.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        logger.info("Time spent by {} is {} ms", joinPoint, System.currentTimeMillis() - start);

        return result;
    }

}
