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
package org.apache.rave.portal.web.context;


import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContextEvent;
import java.util.Map;

public class RaveContextLoaderListenerTest {
    private MockServletContext mockServletContext;
    private ServletContextEvent servletContextEvent;
    private RaveContextLoaderListener raveContextLoaderListener;

    @Before
    public void setup() {
        mockServletContext = new MockServletContext("test");
        mockServletContext.addInitParameter("contextConfigLocation", "../test-applicationContext.xml");
        servletContextEvent = new ServletContextEvent(mockServletContext);

        raveContextLoaderListener = new RaveContextLoaderListener();
    }

    @Test
    public void contextInitialized() {
        raveContextLoaderListener.contextInitialized(servletContextEvent);

        @SuppressWarnings("unchecked")
        Map<String, String> props = (Map<String, String>) servletContextEvent.getServletContext().getAttribute("applicationProperties");
        assertThat(props.size(), is(2));
        assertThat(props.get("portal.testprop1"), is("hello"));
        assertThat(props.get("portal.testprop2"), is("goodbye"));
    }
}
