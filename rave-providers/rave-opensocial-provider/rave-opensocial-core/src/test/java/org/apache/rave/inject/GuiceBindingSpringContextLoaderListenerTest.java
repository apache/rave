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

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.shindig.common.servlet.GuiceServletContextListener;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link GuiceBindingSpringContextLoaderListener}
 */
public class GuiceBindingSpringContextLoaderListenerTest {
    @Test
    public void initialize_test() {
        Injector injector = Guice.createInjector(createNiceMock(Module.class));

        MockServletContext mockServletContext = new MockServletContext();
        mockServletContext.addInitParameter("contextConfigLocation",
                "classpath:rave-shindig-test-applicationContext.xml, classpath:rave-shindig-test-dataContext.xml");
        mockServletContext.setAttribute(GuiceServletContextListener.INJECTOR_ATTRIBUTE, injector);

        GuiceBindingSpringContextLoaderListener listener = new GuiceBindingSpringContextLoaderListener();
        ServletContextEvent event = createNiceMock(ServletContextEvent.class);
        expect(event.getServletContext()).andReturn(mockServletContext).anyTimes();
        replay(event);

        listener.contextInitialized(event);
        assertThat((Injector)mockServletContext.getAttribute(GuiceServletContextListener.INJECTOR_ATTRIBUTE), is(not(sameInstance(injector))));

    }
}
