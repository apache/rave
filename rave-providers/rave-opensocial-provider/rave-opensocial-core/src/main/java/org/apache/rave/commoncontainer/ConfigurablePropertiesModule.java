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

import com.google.inject.AbstractModule;
import com.google.inject.CreationException;
import com.google.inject.name.Names;
import com.google.inject.spi.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Injects everything from the a property file as a Named value.<br/>
 * Uses the {@literal rave.shindig.properties} file unless the system property
 * {@literal rave-shindig.override.properties} defines a different location.
 */
public class ConfigurablePropertiesModule extends AbstractModule {

    private static final String DEFAULT_PROPERTIES = "rave.shindig.properties";
    private static final String SHINDIG_OVERRIDE_PROPERTIES = "rave-shindig.override.properties";

    private static final String CLASSPATH = "classpath:";

    private final Properties properties;


    public ConfigurablePropertiesModule() {
        super();
        this.properties = initProperties();
    }

    /**
     * Injects everything from the a property file as a Named value.
     * Only the entire properties file can be overridden.
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        bindPropertiesAsConstants();
        bindNonConstantProperties();
    }

    private void bindPropertiesAsConstants() {
        for(String propertyName: constantGuiceProperties()) {
            this.binder().bindConstant().annotatedWith(Names.named(propertyName))
                    .to(getProperties().getProperty(propertyName));
        }
    }

    private void bindNonConstantProperties() {
        Properties p = getProperties();
        for (String overridableProperty : constantGuiceProperties()) {
            p.remove(overridableProperty);
        }
        Names.bindProperties(this.binder(), p);
    }

    /**
     * Initializes the Properties. Reads the properties file, overrides values by System properties if set.
     * Replaces placeholders with actual values.
     *
     * @return {@link Properties} for the container
     * @throws com.google.inject.CreationException
     *          if the Properties file cannot be read
     */
    protected Properties initProperties() {
        Resource propertyResource = getPropertyResource();
        try {
            Properties initProperties = loadPropertiesFromPropertyResource(propertyResource);
            overridePropertyValuesWithSystemProperties(initProperties, overridableProperties());
            replacePlaceholderWithValue(initProperties, "%contextRoot%", getContextRootValue(initProperties));
            return initProperties;
        } catch (IOException e) {
            throw new CreationException(Arrays.asList(
                    new Message("Unable to load properties from resource"
                            + ". " + e.getMessage())
            ));
        }
    }

    /**
     * Reads properties from the given {@link Resource}
     *
     * @param propertyResource with the properties
     * @return {@link Properties}
     * @throws java.io.IOException if the Resource cannot be read
     */
    private Properties loadPropertiesFromPropertyResource(Resource propertyResource) throws IOException {
        Properties properties = new Properties();
        properties.load(propertyResource.getInputStream());
        return properties;
    }

    /**
     * Returns a Resource that contains property key-value pairs.
     * If no system property is set for the resource location, the default location is used
     *
     * @return the {@link Resource} with the
     */
    private Resource getPropertyResource() {
        final String overrideProperty = System.getProperty(SHINDIG_OVERRIDE_PROPERTIES);
        if (StringUtils.isBlank(overrideProperty)) {
            return new ClassPathResource(DEFAULT_PROPERTIES);
        } else if (overrideProperty.startsWith(CLASSPATH)) {
            return new ClassPathResource(overrideProperty.trim().substring(CLASSPATH.length()));
        } else {
            return new FileSystemResource(overrideProperty.trim());
        }
    }

    /**
     * If a property is overridable and a system property has been set, the value of the system
     * property overrides the property that was set in the properties file.
     *
     * @param properties               with some property values replaced by system property values
     * @param overridablePropertyNames List of property names which value can be overridden by a system property
     *                                 with the same name
     */
    static void overridePropertyValuesWithSystemProperties(Properties properties,
                                                                  List<String> overridablePropertyNames) {
        for (String propertyName : overridablePropertyNames) {
            properties.setProperty(propertyName, getOverridablePropertyValue(propertyName, properties));
        }
    }

    /**
     * Replaces the placeholder for contextRoot with the actual value
     *
     * @param properties  {@link java.util.Properties}
     * @param placeholder (part of) property value that should be replaced
     * @param value       that replaces the placeholder
     */
    static void replacePlaceholderWithValue(Properties properties, String placeholder, String value) {
        for (Map.Entry entry : properties.entrySet()) {
            String propertyValue = (String) entry.getValue();
            if (propertyValue != null && propertyValue.contains(placeholder)) {
                properties.put(entry.getKey(),
                        propertyValue.replace((placeholder), value));
            }
        }
    }

    /**
     * Returns the value of a property. First checks if there is a system property set,
     * if not, then checks if a {@link Properties} contains this property. Otherwise it
     * returns an empty String.
     *
     * @param propertyKey name of the property
     * @param properties  {@link Properties} that may contain a key by this name
     * @return value for this propertyKey, can be an empty String
     */
    static String getOverridablePropertyValue(String propertyKey, Properties properties) {
        if (System.getProperty(propertyKey) != null) {
            return System.getProperty(propertyKey);
        } else if (properties.get(propertyKey) != null) {
            return properties.getProperty(propertyKey);
        } else {
            return "";
        }
    }
    
    /**
     * Looks up the context root property value in a {@link Properties}
     *
     * @param properties {@link Properties}
     * @return value of the context root property, can be {@literal null}
     */
    private String getContextRootValue(Properties properties) {
        return properties.getProperty("shindig.contextroot");
    }

    /**
     * @return {@link Properties}
     */
    protected Properties getProperties() {
        return properties;
    }

    /**
     *
     * @return List of property keys that can be overridden by system properties
     */
    private static List<String> overridableProperties() {
        List<String> propertyNames = new ArrayList<String>();
        propertyNames.add("shindig.host");
        propertyNames.add("shindig.port");
        propertyNames.add("shindig.contextroot");
        return propertyNames;
    }

    /**
     *
     * @return List of property keys that should be bound as constants in Guice
     */
    private static List<String> constantGuiceProperties() {
        List<String> propertyNames = new ArrayList<String>();
        propertyNames.add("shindig.host");
        propertyNames.add("shindig.port");
        propertyNames.add("shindig.contextroot");
        return propertyNames;
    }

}
