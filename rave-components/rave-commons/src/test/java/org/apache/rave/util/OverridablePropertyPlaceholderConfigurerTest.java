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

import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test for {@link OverridablePropertyPlaceholderConfigurer}
 */
public class OverridablePropertyPlaceholderConfigurerTest {
    
    @Test
    public void setLocation()  {
        System.setProperty("portal.override.properties", "classpath:portal-test.properties");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-test.xml");
        final StringBuffer testBean = (StringBuffer) context.getBean("testBean");
        assertEquals("Dummy value", testBean.toString());
    }

    @Test
    public void testGetResolvedProps() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-test.xml");
        final OverridablePropertyPlaceholderConfigurer portalPropertyPlaceholder = (OverridablePropertyPlaceholderConfigurer) context.getBean("portalPropertyPlaceholder");

        assertThat(portalPropertyPlaceholder.getResolvedProps().get("portal.opensocial_engine.root"), is("127.0.0.1:8080"));
        assertThat(portalPropertyPlaceholder.getResolvedProps().get("dummy.key"), is("Dummy value"));
    }
}