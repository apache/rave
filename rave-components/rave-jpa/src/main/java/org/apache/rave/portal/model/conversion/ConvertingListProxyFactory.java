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
package org.apache.rave.portal.model.conversion;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a {@link java.util.List} proxy that converts the added object to an entity
 */
@Component
public class ConvertingListProxyFactory {

    @SuppressWarnings("unchecked")
    public static <E, T extends E> List<E> createProxyList(Class<E> targetType, List<T> underlyingList) {
        // ensure the list is not null by creating an empty list to prevent unnecessary downstream NullPointerExceptions
        if (underlyingList == null) {
            underlyingList = new ArrayList<T>();
        }
        return (List) Proxy.newProxyInstance(ConvertingListProxyFactory.class.getClassLoader(),
                new Class<?>[]{List.class},
                new ConvertingListInvocationHandler<E, T>(underlyingList, targetType));
    }

    public static class ConvertingListInvocationHandler<S,T> implements InvocationHandler {

        public static final String ADD_METHOD = "add";
        public static final String SET_METHOD = "set";
        public static final String ADD_ALL_METHOD = "addAll";

        private List<T> underlying;
        private Class<S> targetClass;

        public ConvertingListInvocationHandler(List<T> underlying, Class<S> targetClass) {
            this.underlying = underlying;
            this.targetClass = targetClass;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
            String methodName = method.getName();
            int convertIndex = method.getParameterTypes().length == 1 ? 0 : 1;
            if(ADD_METHOD.equals(methodName) || SET_METHOD.equals(methodName)) {
                parameters[convertIndex] = JpaConverter.getInstance().convert((S)parameters[convertIndex], targetClass);
            } else if(ADD_ALL_METHOD.equals(methodName)) {
                convertAll((List)parameters[convertIndex]);
            }
            return method.invoke(underlying, parameters);
        }

        @SuppressWarnings("unchecked")
        private void convertAll(List<S> parameter) {
            for (int i = 0; i < parameter.size(); i++) {
                parameter.set(i, (S) JpaConverter.getInstance().convert(parameter.get(i), targetClass));
            }
        }
    }
}
