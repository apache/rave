/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.commoncontainer;

import com.google.inject.CreationException;
import com.google.inject.spi.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.PropertiesModule;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Injects everything from the a property file as a Named value.<br/>
 * Uses the {@literal shindig.properties} file unless the system property
 * {@literal shindig.override.properties} defines a different location.
 */
public class ConfigurablePropertiesModule extends PropertiesModule {

    private static final String DEFAULT_PROPERTIES = "shindig.properties";
    private static final String SHINDIG_OVERRIDE_PROPERTIES = "shindig.override.properties";

    private static final String CLASSPATH = "classpath:";

    private static final String CONTEXT_ROOT_PLACEHOLDER = "%contextRoot%";
    private static final String CONTEXT_ROOT_SYSTEM_PROPERTY = "shindig.contextroot";


    /**
     * Injects everything from the a property file as a Named value.
     * Only the entire properties file can be overridden.
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        install(new PropertiesModule(loadProperties()));
    }


    /**
     * If the system property {@literal shindig.override.properties} is set,
     * it will load the file from that location,
     * otherwise it will load the default Shindig {@literal shindig.properties} file
     *
     * @return {@link Properties} for the container
     * @throws CreationException if the Properties file cannot be read
     */
    protected Properties loadProperties() {
        Properties initProperties = new Properties();
        final String overrideProperty = System.getProperty(SHINDIG_OVERRIDE_PROPERTIES);

        Resource propertyResource;

        if (StringUtils.isBlank(overrideProperty)) {
            propertyResource = new ClassPathResource(DEFAULT_PROPERTIES);
        } else if (overrideProperty.startsWith(CLASSPATH)) {
            propertyResource = new ClassPathResource(
                    overrideProperty.trim().substring(CLASSPATH.length()));
        } else {
            propertyResource = new FileSystemResource(
                    overrideProperty.trim());
        }

        try {
            initProperties.load(propertyResource.getInputStream());
            for (Map.Entry entry : initProperties.entrySet()) {
                String value = (String) entry.getValue();
                if (value != null && value.contains(CONTEXT_ROOT_PLACEHOLDER)) {
                    initProperties.put(entry.getKey(),
                            value.replace((CONTEXT_ROOT_PLACEHOLDER), getContextRoot()));
                }
            }
        } catch (IOException e) {
            throw new CreationException(Arrays.asList(
                    new Message("Unable to load properties: " + overrideProperty)));
        }

        return initProperties;
    }

    /**
     * Should return the context root where the current web module is deployed with.
     * Useful for testing and working out of the box configs.
     * If not set uses fixed value of "".
     *
     * @return an context path as a string.
     */
    private String getContextRoot() {
        return System.getProperty(CONTEXT_ROOT_SYSTEM_PROPERTY) != null ?
                System.getProperty(CONTEXT_ROOT_SYSTEM_PROPERTY) : "";
    }


}
