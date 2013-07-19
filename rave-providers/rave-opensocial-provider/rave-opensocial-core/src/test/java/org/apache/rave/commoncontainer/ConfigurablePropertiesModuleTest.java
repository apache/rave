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

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link ConfigurablePropertiesModule}
 */
public class ConfigurablePropertiesModuleTest {

    @Test
    public void testDefaultProperties() throws Exception {
        TestableConfigurablePropertiesModule propertiesModule = new TestableConfigurablePropertiesModule();
        Properties properties = propertiesModule.getProperties();

        assertEquals("Default container.js location", "res://containers/default/container.js",
                properties.getProperty("shindig.containers.default"));
        assertEquals("No contextRoot", "http://%authority%/gadgets/oauthcallback",
                properties.getProperty("shindig.signing.global-callback-url"));
    }

    @Test
    public void testDefaultProperties_WithReplacedContextRoot() throws Exception {
        System.setProperty("shindig.contextroot", "shindigcontext");

        TestableConfigurablePropertiesModule propertiesModule = new TestableConfigurablePropertiesModule();
        Properties properties = propertiesModule.getProperties();

        assertEquals("Default container.js location", "res://containers/default/container.js",
                properties.getProperty("shindig.containers.default"));
        assertEquals("Replaced contextRoot", "http://%authority%shindigcontext/gadgets/oauthcallback",
                properties.getProperty("shindig.signing.global-callback-url"));

        System.clearProperty("shindig.contextroot");
    }

    @Test
    public void testCustomProperties() throws Exception {
        System.setProperty("rave-shindig.override.properties", "classpath:rave.shindig.custom.properties");

        TestableConfigurablePropertiesModule propertiesModule = new TestableConfigurablePropertiesModule();
        Properties properties = propertiesModule.initProperties();

        assertEquals("Custom container.js location",
                "res://containers/default/container.js,res://containers/default/testcontainer.js",
                properties.getProperty("shindig.containers.default"));
        assertEquals("Custom contextRoot", "http://%authority%customContext/gadgets/oauthcallback",
                properties.getProperty("shindig.signing.global-callback-url"));
        assertEquals("Custom shindig host", "127.0.0.1",
                properties.getProperty("shindig.host"));

        System.clearProperty("rave-shindig.override.properties");
    }

    @Test
    public void testCustomProperties_WithReplacedContextRoot() throws Exception {
        System.setProperty("shindig.contextroot", "shindigcontext");
        System.setProperty("shindig.host", "127.0.0.2");
        System.setProperty("rave-shindig.override.properties", "classpath:rave.shindig.custom.properties");

        TestableConfigurablePropertiesModule propertiesModule = new TestableConfigurablePropertiesModule();
        Properties properties = propertiesModule.initProperties();

        assertEquals("Custom container.js location",
                "res://containers/default/container.js,res://containers/default/testcontainer.js",
                properties.getProperty("shindig.containers.default"));
        assertEquals("Replaced contextRoot", "http://%authority%shindigcontext/gadgets/oauthcallback",
                properties.getProperty("shindig.signing.global-callback-url"));
        assertEquals("Different shindig host", "127.0.0.2",
                properties.getProperty("shindig.host"));

        System.clearProperty("shindig.contextroot");
        System.clearProperty("shindig.host");
        System.clearProperty("rave-shindig.override.properties");
    }

    @Test
    public void testOverridePropertyValuesWithSystemProperties() throws Exception {
        final String keyCanBeOverridden = "can.be.overridden";
        final String keyCannotBeOverridden = "cannot.be.overridden";
        final String defaultValue = "defaultValue";
        final String systemValue = "systemValue";

        Properties properties = new Properties();
        properties.setProperty(keyCanBeOverridden, defaultValue);
        properties.setProperty(keyCannotBeOverridden, defaultValue);

        List<String> overridableProperties = new ArrayList<String>();
        overridableProperties.add(keyCanBeOverridden);

        System.setProperty(keyCanBeOverridden, systemValue);

        ConfigurablePropertiesModule.overridePropertyValuesWithSystemProperties(properties, overridableProperties);

        Assert.assertEquals(systemValue, properties.getProperty(keyCanBeOverridden));
        Assert.assertEquals(defaultValue, properties.getProperty(keyCannotBeOverridden));
        System.clearProperty(keyCanBeOverridden);
    }

    @Test
    public void replacePlaceholderWithValue() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("key1", "%placeholder%");
        properties.setProperty("key2", "Not to be replaced");
        properties.setProperty("key3", "Contains %placeholder% to replace");

        String placeholder = "%placeholder%";
        String replaceValue = "myValue";

        ConfigurablePropertiesModule.replacePlaceholderWithValue(properties, placeholder, replaceValue);

        Assert.assertEquals(replaceValue, properties.getProperty("key1"));
        Assert.assertEquals("Not to be replaced", properties.getProperty("key2"));
        Assert.assertEquals("Contains myValue to replace", properties.getProperty("key3"));
    }

    @Test
    public void testGetOverridablePropertyValue() throws Exception {
        final String propertyName = "dummyKey";

        Properties properties = new Properties();

        String value = ConfigurablePropertiesModule.getOverridablePropertyValue(propertyName, properties);
        Assert.assertEquals("", value);

        System.setProperty(propertyName, "systemValue");
        properties.setProperty(propertyName, "propertiesValue");
        value = ConfigurablePropertiesModule.getOverridablePropertyValue(propertyName, properties);
        Assert.assertEquals("systemValue", value);

        System.clearProperty(propertyName);
        value = ConfigurablePropertiesModule.getOverridablePropertyValue(propertyName, properties);
        Assert.assertEquals("propertiesValue", value);

        properties.remove(propertyName);
        value = ConfigurablePropertiesModule.getOverridablePropertyValue(propertyName, properties);
        Assert.assertEquals("", value);
    }

    /**
     * Inner private class to access {@link org.apache.rave.commoncontainer.ConfigurablePropertiesModule#getProperties()}
     */
    private class TestableConfigurablePropertiesModule extends ConfigurablePropertiesModule {

        @Override
        protected Properties getProperties() {
            return super.getProperties();
        }
    }
}
