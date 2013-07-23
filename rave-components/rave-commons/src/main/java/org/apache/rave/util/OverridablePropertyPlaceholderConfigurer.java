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

package org.apache.rave.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Reads property from the default location unless a system property is set.
 * The name of the system property is set by the parameter {@literal systemPropertyName}.
 */
public class OverridablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final String CLASSPATH = "classpath:";
    private String systemPropertyName;
    private Map<String, String> resolvedProps;

    @Override
    public void setLocation(Resource location) {
        if (StringUtils.isBlank(getSystemPropertyName())) {
            throw new IllegalArgumentException("Missing value for 'systemPropertyName'.");
        }
        Resource locationResource;
        final String overrideProperty = System.getProperty(getSystemPropertyName());
        
        if (StringUtils.isBlank(overrideProperty)) {
            locationResource = location;
        } else if (overrideProperty.startsWith(CLASSPATH)) {
            locationResource = new ClassPathResource(overrideProperty.substring(CLASSPATH.length()));
        } else {
            locationResource = new FileSystemResource(overrideProperty);
        }
        super.setLocation(locationResource);
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

        // load the application properties into a map that is exposed via public getter
        resolvedProps = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            resolvedProps.put(keyStr, resolvePlaceholder(keyStr, props, SYSTEM_PROPERTIES_MODE_OVERRIDE));
        }
    }

    public void setSystemPropertyName(String systemPropertyName) {
        this.systemPropertyName = systemPropertyName;
    }

    public String getSystemPropertyName() {
        return systemPropertyName;
    }

    public Map<String, String> getResolvedProps() {
        return Collections.unmodifiableMap(resolvedProps);
    }
}
