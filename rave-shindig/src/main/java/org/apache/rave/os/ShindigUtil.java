/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.rave.os;

import org.apache.shindig.protocol.conversion.BeanConverter;
import org.apache.shindig.protocol.conversion.BeanJsonConverter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * 
 */
public class ShindigUtil {

    class SocialApiGuiceModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(BeanConverter.class).annotatedWith(Names.named("shindig.bean.converter.json")).to(
                    BeanJsonConverter.class);
        }
    }

    public ShindigUtil() {
        Injector injector = Guice.createInjector(new SocialApiGuiceModule());
        injector.injectMembers(this);
    }

    @Inject
    @Named("shindig.bean.converter.json")
    private BeanConverter beanConverter;

    public BeanConverter getBeanConverter() {
        return beanConverter;
    }

    public void setBeanConverter(BeanConverter beanConverter) {
        this.beanConverter = beanConverter;
    }
}
