package com.edu.ulab.app.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP implementation of "Logs everywhere" concept
 * before - methodname + args, after - methodname + result
 */
@Aspect
@Component
public class YlabLog {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.edu.ulab.app.facade.UserDataFacade.*(..)) " +
            "|| execution(public * com.edu.ulab.app.service.impl.*.*(..)) " +
            "|| execution(public * com.edu.ulab.app.mapper.*.*(..))")
    public void callAtMyServicePublic() { }

    @Before("callAtMyServicePublic()")
    public void beforeCallAtMethod1(JoinPoint jp) {
        String methodName = jp.getSignature().toShortString();
        String args = Arrays.deepToString(jp.getArgs());
        logger.info("started {} args=[{}]", methodName, args);
    }

    @AfterReturning(pointcut = "callAtMyServicePublic()", returning = "result")
    public void afterCallAt(JoinPoint jp, Object result) {
        String methodName = jp.getSignature().toShortString();
        String resultString = "no result";
        if(result != null)
            resultString = result.toString();

        logger.info("result of {}: {}", methodName, resultString);
    }
}
