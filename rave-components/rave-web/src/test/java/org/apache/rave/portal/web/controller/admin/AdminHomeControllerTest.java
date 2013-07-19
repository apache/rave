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

package org.apache.rave.portal.web.controller.admin;

import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link AdminHomeController}
 */
public class AdminHomeControllerTest {

    private static final String TABS = "tabs";
    private static final String REFERRER_ID = "35";

    private AdminHomeController controller;

    @Test
    public void adminHome() throws Exception {
        Model model = new ExtendedModelMap();
        String homeView = controller.viewDefault(model, REFERRER_ID);
        assertEquals(ViewNames.ADMIN_HOME, homeView);
        assertTrue(model.containsAttribute(TABS));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Before
    public void setUp() throws Exception {
        controller = new AdminHomeController();
    }

}
