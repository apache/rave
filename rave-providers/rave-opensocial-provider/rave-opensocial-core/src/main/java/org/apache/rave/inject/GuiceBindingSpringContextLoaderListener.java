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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.shindig.common.servlet.GuiceServletContextListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Collection;

/**
 * Context Load listener that initializes the Spring application context and binds beans to the pre-existing Guice injector
 */
public class GuiceBindingSpringContextLoaderListener extends ContextLoaderListener{

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //Initialize the application context
        super.contextInitialized(event);
        ServletContext servletContext = event.getServletContext();
        //Get a list of Guice module Spring beans from the application context
        Collection<Module> modules = getModulesFromApplicationContext();
        //Override the current injector with the new child injector that includes all Spring managed Guice modules
        overrideInjector(Guice.createInjector(modules), servletContext);
    }

    /**
     * Clean up code that should run when the servlet context is destroyed
     *
     * @param event
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        // grab the Guice Injector object from the Servlet Context
        Injector injector = (Injector) context.getAttribute(GuiceServletContextListener.INJECTOR_ATTRIBUTE);
        if (injector != null) {
            // invoke the cleanup function for all Guice modules that implement the CleanupCapable interface
            // this will do things like remove MBeans that might be registered to the JVM
            GuiceServletContextListener.CleanupHandler cleanups = injector.getInstance(GuiceServletContextListener.CleanupHandler.class);
            cleanups.cleanup();
        }

        context.removeAttribute(GuiceServletContextListener.INJECTOR_ATTRIBUTE);
    }

    private static void overrideInjector(Injector injector, ServletContext context) {
        context.setAttribute(GuiceServletContextListener.INJECTOR_ATTRIBUTE, injector);
    }

    private static Collection<Module> getModulesFromApplicationContext() {
        ApplicationContext context = getCurrentWebApplicationContext();
        return context.getBeansOfType(Module.class).values();
    }
}
