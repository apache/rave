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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Initializes a Spring application context and binds the beans from the context to Guice
 */
@Component
public class SpringBindingModule extends AbstractModule implements ApplicationContextAware {

    public static final String BASE_PACKAGE = "${shindig.spring.base-package}";
    public static final String DELEMITER = ":";

    private ApplicationContext applicationContext;
    private List<String> basePackages;
    private Set<Class<?>> mappedClasses;

    @Override
    protected void configure() {
        mappedClasses = new HashSet<Class<?>>();
        bindFromApplicationContext();
    }

    /**
     * Sets the package name to restrict bean binding to Guice
     * @param value the base package to bind all beans in sub-packages to Guice
     */
    @Value(BASE_PACKAGE)
    public void setBasePackage(String value) {
        String replace = value.replace(".", "\\.");
        this.basePackages = Arrays.asList(replace.split(DELEMITER));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void bindFromApplicationContext() {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            if(bean != null) {
                bindInterfaces(bean);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void bindInterfaces(Object bean) {
        String fullClassName = Proxy.isProxyClass(bean.getClass()) ? bean.toString() : bean.getClass().getName();
        if (isAddable(fullClassName)) {
            for (final Class clazz : bean.getClass().getInterfaces()) {
                //Check to see if we have already bound a provider for this interface.  If we have,
                //then don't attempt to bind it again as the provider will pull from the application context by type
                //and any multi-bean errors will be handled at injection time
                if(!mappedClasses.contains(clazz)) {
                    bind(clazz).toProvider(new SpringContextProvider(clazz, applicationContext));
                    mappedClasses.add(clazz);
                }
            }
        }
    }

    private boolean isAddable(String fullClassName) {
        for(String basePackage : basePackages) {
            if(fullClassName.matches(basePackage + ".*")) {
                return true;
            }
        }
        return false;
    }


}
