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

import org.apache.rave.service.LockService;
import org.apache.rave.synchronization.annotation.Synchronized;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.TargetClassAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SynchronizingAspectTest {
    private LockService lockService;
    private SynchronizingAspect aspect;

    @Before
    public void setup() {
        lockService = createMock(LockService.class);
        aspect = new SynchronizingAspect(lockService);
    }

    @Test
    public void testStaticDiscriminatorStaticIdEmptyCondition() throws Throwable {
        String expectedDiscriminator = "StaticDiscriminator";
        String expectedId = "staticId";
        String expectedResult = "testStaticDiscriminatorStaticIdEmptyCondition";
        TestService service = new DefaultTestService();
        Method expectedMethod = service.getClass().getDeclaredMethod("testStaticDiscriminatorStaticIdEmptyCondition",
                TestObject.class);
        TestObject argument = new TestObject(1L, "Jesse");
        Object[] joinPointArgs = {argument};

        ProceedingJoinPoint joinPoint = prepareJoinPoint(expectedDiscriminator, expectedId, service,
                expectedMethod, argument, joinPointArgs);

        String result = (String) aspect.synchronizeInvocation(joinPoint);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testStaticDiscriminatorStaticIdEmptyConditionJdkProxy() throws Throwable {
        //Test to get coverage over code paths hit when a proxy is being used for the target class
        String expectedDiscriminator = "StaticDiscriminator";
        String expectedId = "staticId";
        String expectedResult = "testStaticDiscriminatorStaticIdEmptyCondition";
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("getTargetClass")) {
                    return DefaultTestService.class;
                } else if (method.getName().equals("testStaticDiscriminatorStaticIdEmptyCondition")) {
                    return "testStaticDiscriminatorStaticIdEmptyCondition";
                } else {
                    throw new RuntimeException();
                }
            }
        };

        TestService service = (TestService) Proxy.newProxyInstance(DefaultTestService.class.getClassLoader(),
                new Class[]{TestService.class, TargetClassAware.class}, handler);

        Method expectedMethod = service.getClass().getDeclaredMethod("testStaticDiscriminatorStaticIdEmptyCondition",
                TestObject.class);
        TestObject argument = new TestObject(1L, "Jesse");
        Object[] joinPointArgs = {argument};

        ProceedingJoinPoint joinPoint = prepareJoinPoint(expectedDiscriminator, expectedId, service,
                expectedMethod, argument, joinPointArgs);

        String result = (String) aspect.synchronizeInvocation(joinPoint);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testStaticDiscriminatorDynamicIdDynamicCondition() throws Throwable {
        String expectedDiscriminator = "StaticDiscriminator";
        String expectedId = String.valueOf(1L);
        String expectedResult = "testStaticDiscriminatorDynamicIdDynamicCondition";
        TestService service = new DefaultTestService();
        Method expectedMethod = service.getClass().getDeclaredMethod("testStaticDiscriminatorDynamicIdDynamicCondition",
                TestObject.class);
        TestObject argument = new TestObject(1L, "Jesse");
        Object[] joinPointArgs = {argument};

        ProceedingJoinPoint joinPoint = prepareJoinPoint(expectedDiscriminator, expectedId, service,
                expectedMethod, argument, joinPointArgs);

        String result = (String) aspect.synchronizeInvocation(joinPoint);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testStaticDiscriminatorDynamicIdDynamicConditionConditionFails() throws Throwable {
        String expectedDiscriminator = "StaticDiscriminator";
        String expectedId = String.valueOf(1L);
        String expectedResult = "testStaticDiscriminatorDynamicIdDynamicCondition";
        TestService service = new DefaultTestService();
        Method expectedMethod = service.getClass().getDeclaredMethod("testStaticDiscriminatorDynamicIdDynamicCondition",
                TestObject.class);
        TestObject argument = new TestObject(-1L, "Jesse");
        Object[] joinPointArgs = {argument};

        ProceedingJoinPoint joinPoint = prepareJoinPoint(expectedDiscriminator, expectedId, service,
                expectedMethod, argument, joinPointArgs);

        String result = (String) aspect.synchronizeInvocation(joinPoint);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void synchronizePointcutCoverageTest() {
        aspect.synchronizePointcut();
    }

    @Test
    public void expressionCacheBranchesCoverageTest() throws Throwable {
        //calling twice in a row should warm up the expression caches and cover those branches
        testStaticDiscriminatorDynamicIdDynamicCondition();
        reset(lockService);
        testStaticDiscriminatorDynamicIdDynamicCondition();
    }

    private ProceedingJoinPoint prepareJoinPoint(String expectedDiscriminator, String expectedId, TestService service,
                                                 Method expectedMethod, TestObject argument, Object[] joinPointArgs)
            throws Throwable {
        MethodSignature methodSignature = createMock(MethodSignature.class);
        expect(methodSignature.getMethod()).andReturn(expectedMethod);
        replay(methodSignature);

        ProceedingJoinPoint joinPoint = createMock(ProceedingJoinPoint.class);
        expect(joinPoint.getSignature()).andReturn(methodSignature);
        expect(joinPoint.getTarget()).andReturn(service);
        expect(joinPoint.getArgs()).andReturn(joinPointArgs);
        expect(joinPoint.proceed()).andReturn(expectedMethod.invoke(service, argument));
        replay(joinPoint);

        Lock lock = new ReentrantLock();
        expect(lockService.borrowLock(expectedDiscriminator, expectedId)).andReturn(lock);
        lockService.returnLock(lock);
        replay(lockService);
        return joinPoint;
    }

    private interface TestService {
        public String testStaticDiscriminatorStaticIdEmptyCondition(TestObject testObject);

        public String testStaticDiscriminatorDynamicIdDynamicCondition(TestObject testObject);
    }

    private class DefaultTestService implements TestService {
        @Synchronized(discriminator = "'StaticDiscriminator'", id = "'staticId'")
        public String testStaticDiscriminatorStaticIdEmptyCondition(TestObject testObject) {
            return "testStaticDiscriminatorStaticIdEmptyCondition";
        }

        @Synchronized(discriminator = "'StaticDiscriminator'", id = "#testObject.id",
                condition = "#testObject.id >= 0")
        public String testStaticDiscriminatorDynamicIdDynamicCondition(TestObject testObject) {
            return "testStaticDiscriminatorDynamicIdDynamicCondition";
        }
    }

    private class TestObject {
        private long id;
        private String name;

        private TestObject() {
        }

        private TestObject(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}