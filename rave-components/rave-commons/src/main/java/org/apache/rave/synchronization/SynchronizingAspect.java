/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.synchronization;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.rave.service.LockService;
import org.apache.rave.synchronization.annotation.Synchronized;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * Aspect to apply synchronization around methods decorated with the @Synchronized annotation.
 * <p/>
 * This implementation was heavily influenced by the (awesome) Cache Abstraction included in Spring Framework 3.1M2 - the
 * following classes were particularly useful as a point of reference:
 * <p/>
 * https://src.springframework.org/svn/spring-framework/tags/spring-framework-3.1.0.M2/org.springframework.context/src/main/java/org/springframework/cache/interceptor/ExpressionEvaluator.java
 * https://src.springframework.org/svn/spring-framework/tags/spring-framework-3.1.0.M2/org.springframework.context/src/main/java/org/springframework/cache/interceptor/LazyParamAwareEvaluationContext.java
 */
@Aspect
@Component
public class SynchronizingAspect {
    private static Logger logger = LoggerFactory.getLogger(SynchronizingAspect.class);

    private LockService lockService;

    private SpelExpressionParser parser = new SpelExpressionParser();
    private ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Map<Method, Expression> conditionCache = new ConcurrentHashMap<Method, Expression>();
    private Map<Method, Expression> discriminatorCache = new ConcurrentHashMap<Method, Expression>();
    private Map<Method, Expression> idCache = new ConcurrentHashMap<Method, Expression>();
    private Map<Method, Method> targetMethodCache = new ConcurrentHashMap<Method, Method>();

    @Autowired
    public SynchronizingAspect(LockService lockService) {
        this.lockService = lockService;
    }

    @Pointcut("@annotation(org.apache.rave.synchronization.annotation.Synchronized)")
    public void synchronizePointcut() {
    }

    @Around("synchronizePointcut()")
    public Object synchronizeInvocation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object target = proceedingJoinPoint.getTarget();
        Object[] args = proceedingJoinPoint.getArgs();
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        Synchronized annotation = getAnnotation(targetClass, method);
        Validate.notNull(annotation, "Could not find @Synchronized annotation!");

        Lock lock = getLock(targetClass, method, args, annotation);
        if (lock == null) {
            logger.debug("No lock obtained for call [{}] on targetClass [{}] - proceeding without synchronization on " +
                    "thread {}", new Object[]{method.getName(), targetClass.getName(), Thread.currentThread().getId()});
            return proceedingJoinPoint.proceed();
        } else {
            try {
                logger.debug("Lock obtained for call [{}] on targetClass [{}] - proceeding with synchronization on thread {}",
                        new Object[]{method.getName(), targetClass.getName(), Thread.currentThread().getId()});
                lock.lock();
                return proceedingJoinPoint.proceed();
            } finally {
                lock.unlock();
                lockService.returnLock(lock);
            }
        }
    }

    private Lock getLock(Class<?> targetClass, Method method, Object[] args, Synchronized annotation) {
        logger.debug("Fetching lock for call [{}] on targetClass [{}] with SpEl condition [{}], SpEl discriminator " +
                "[{}], and SpEl id [{}] on thread {}", new Object[]{method.getName(), targetClass.getName(),
                annotation.condition(), annotation.discriminator(), annotation.id(), Thread.currentThread().getId()});

        EvaluationContext context = getEvaluationContext(targetClass, method, args);
        if (conditionPasses(annotation, method, context)) {
            logger.debug("Condition check passes for SpEl condition [{}] on thread {}", annotation.condition(),
                    Thread.currentThread().getId());
            String discriminator = getDiscriminator(annotation, method, context);
            String id = getId(annotation, method, context);
            logger.debug("Fetching lock with discriminator [{}] and id [{}] on thread {}", new Object[]{discriminator,
                    id, Thread.currentThread().getId()});
            return lockService.borrowLock(discriminator, id);
        }

        logger.debug("Condition check fails for SpEl condition [{}] on thread {}", annotation.condition(),
                Thread.currentThread().getId());
        return null;
    }

    private boolean conditionPasses(Synchronized annotation, Method method, EvaluationContext context) {
        String condition = annotation.condition();
        if (StringUtils.isNotBlank(condition)) {
            return parseConditionExpression(method, annotation).getValue(context, Boolean.class);
        }
        return true;
    }

    private String getDiscriminator(Synchronized annotation, Method method, EvaluationContext context) {
        return parseDiscriminatorExpression(method, annotation).getValue(context, String.class);
    }

    private String getId(Synchronized annotation, Method method, EvaluationContext context) {
        return parseIdExpression(method, annotation).getValue(context, String.class);
    }

    private Expression parseConditionExpression(Method method, Synchronized annotation) {
        Expression expression = conditionCache.get(method);
        if (expression == null) {
            expression = parser.parseExpression(annotation.condition());
            conditionCache.put(method, expression);
        }
        return expression;
    }

    private Expression parseDiscriminatorExpression(Method method, Synchronized annotation) {
        Expression expression = discriminatorCache.get(method);
        if (expression == null) {
            expression = parser.parseExpression(annotation.discriminator());
            discriminatorCache.put(method, expression);
        }
        return expression;
    }

    private Expression parseIdExpression(Method method, Synchronized annotation) {
        Expression expression = idCache.get(method);
        if (expression == null) {
            expression = parser.parseExpression(annotation.id());
            idCache.put(method, expression);
        }
        return expression;
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        Method targetMethod = targetMethodCache.get(method);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            targetMethodCache.put(method, targetMethod);
        }
        return targetMethod;
    }

    //this method was roughly based on the implementation of:
    //org.springframework.cache.interceptor.LazyParamAwareEvaluationContext#loadArgsAsVariables(...)
    private EvaluationContext getEvaluationContext(Class<?> targetClass, Method method, Object[] args) {
        EvaluationContext context = new StandardEvaluationContext();
        if (ArrayUtils.isEmpty(args)) {
            return context;
        }

        Method targetMethod = getTargetMethod(targetClass, method);
        for (int i = 0; i < args.length; i++) {
            context.setVariable("p" + i, args[i]);
        }

        String[] parameterNames = paramNameDiscoverer.getParameterNames(targetMethod);
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }

        return context;
    }

    private Synchronized getAnnotation(Class<?> targetClass, Method method) {
        Method specificMethod = getTargetMethod(targetClass, method);
        return AnnotationUtils.getAnnotation(specificMethod, Synchronized.class);
    }
}