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

package org.apache.rave.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Proxy;
import java.util.regex.Pattern;

/**
 * Initializes a Spring application context and binds the beans from the context to Guice
 */
@Component
public class SpringBindingModule extends AbstractModule implements ApplicationContextAware {

    public static final String BASE_PACKAGE = "${shindig.spring.base-package}";

    private ApplicationContext applicationContext;
    private String basePackage;

    @Override
    protected void configure() {
        bindFromApplicationContext();
    }

    private void bindFromApplicationContext() {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            bindInterfaces(bean);
        }
    }

    @SuppressWarnings("unchecked")
    private void bindInterfaces(Object bean) {
        String fullClassName = Proxy.isProxyClass(bean.getClass()) ? bean.toString() : bean.getClass().getName();
        if (fullClassName.matches(basePackage + ".*")) {
            for (final Class clazz : bean.getClass().getInterfaces()) {
                bind(clazz).toProvider(new Provider() {
                    @Override
                    public Object get() {
                        return applicationContext.getBean(clazz);
                    }
                });
            }
        }
    }

    /**
     * Sets the package name to restrict bean binding to Guice
     * @param value the base package to bind all beans in sub-packages to Guice
     */
    @Value(BASE_PACKAGE)
    public void setBasePackage(String value) {
        this.basePackage = value.replace(".", "\\.");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
