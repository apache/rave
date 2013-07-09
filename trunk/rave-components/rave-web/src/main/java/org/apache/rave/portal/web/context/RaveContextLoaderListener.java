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
package org.apache.rave.portal.web.context;

import org.apache.rave.util.OverridablePropertyPlaceholderConfigurer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

/**
 * A custom extension to the Spring ContextLoaderListener class that allows us to access the application's
 * properties file from the Servlet / JSP layer
 *
 */
public class RaveContextLoaderListener extends ContextLoaderListener {
    private final String APPLICATION_PROPERTIES_KEY = "applicationProperties";
    private final String PORTAL_PROPERTY_PLACEHOLDER_BEAN_NAME = "portalPropertyPlaceholder";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);

        // expose the application's properties to the servlet (and consequently to the JSP layer)
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
        OverridablePropertyPlaceholderConfigurer configurer = (OverridablePropertyPlaceholderConfigurer) context.getBean(PORTAL_PROPERTY_PLACEHOLDER_BEAN_NAME);
        event.getServletContext().setAttribute(APPLICATION_PROPERTIES_KEY, configurer.getResolvedProps());
    }
}