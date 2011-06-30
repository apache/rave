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

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link ConfigurablePropertiesModule}
 */
public class ConfigurablePropertiesModuleTest {

    @Test
    public void testDefaultProperties() throws Exception {
        System.setProperty("shindig.contextroot", "shindigcontext");

        TestableConfigurablePropertiesModule propertiesModule = new TestableConfigurablePropertiesModule();
        Properties properties = propertiesModule.getProperties();

        assertEquals("Default container.js location", "res://containers/default/container.js",
                properties.getProperty("shindig.containers.default"));
        assertEquals("Replaced contextRoot", "shindigcontext/gadgets/proxy?container=default&url=",
                properties.getProperty("shindig.content-rewrite.proxy-url"));
    }

    @Test
    public void testCustomProperties() throws Exception {
        System.setProperty("shindig.contextroot", "shindigcontext");
        System.setProperty("shindig.override.properties", "classpath:shindig.test.properties");

        TestableConfigurablePropertiesModule propertiesModule = new TestableConfigurablePropertiesModule();
        Properties properties = propertiesModule.loadProperties();

        assertEquals("Custom container.js location",
                "res://containers/default/container.js,res://containers/default/testcontainer.js",
                properties.getProperty("shindig.containers.default"));
        assertEquals("Replaced contextRoot", "shindigcontext/gadgets/proxy?container=default&url=",
                properties.getProperty("shindig.content-rewrite.proxy-url"));
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
