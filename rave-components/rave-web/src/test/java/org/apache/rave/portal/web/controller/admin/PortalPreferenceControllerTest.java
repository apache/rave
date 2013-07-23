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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.model.PortalPreferenceForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.PortalPreferenceFormValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link PortalPreferenceController}
 */
public class PortalPreferenceControllerTest {

    private PortalPreferenceController controller;
    private PortalPreferenceService service;
    private String validToken;
    private static final String REFERRER_ID = "35";

    @Before
    public void setUp() {
        controller = new PortalPreferenceController();
        service = createMock(PortalPreferenceService.class);
        controller.setPreferenceService(service);
        controller.setFormValidator(new PortalPreferenceFormValidator());
        validToken = AdminControllerUtil.generateSessionToken();
    }

    @Test
    public void testViewPreferences() {
        Model model = new ExtendedModelMap();
        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();

        expect(service.getPreferencesAsMap()).andReturn(preferenceMap);
        replay(service);

        String view = controller.viewPreferences(null,REFERRER_ID, model);

        assertEquals(ViewNames.ADMIN_PREFERENCES, view);
        assertEquals(preferenceMap, model.asMap().get("preferenceMap"));
        assertFalse(model.containsAttribute("actionresult"));
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));

        verify(service);
    }

    @Test

    public void testViewPreferences_afterUpdate() {
        Model model = new ExtendedModelMap();
        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();

        expect(service.getPreferencesAsMap()).andReturn(preferenceMap);
        replay(service);

        final String action = "update";
        String view = controller.viewPreferences(action,REFERRER_ID, model);

        assertEquals(ViewNames.ADMIN_PREFERENCES, view);
        assertEquals(preferenceMap, model.asMap().get("preferenceMap"));
        assertEquals(action, model.asMap().get("actionresult"));
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));

        verify(service);
    }

    @Test
    public void testEditPreferences() {
        Model model = new ExtendedModelMap();

        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();

        expect(service.getPreferencesAsMap()).andReturn(preferenceMap);
        replay(service);
        String view = controller.editPreferences(model,REFERRER_ID);
        assertEquals(ViewNames.ADMIN_PREFERENCE_DETAIL, view);

        assertTrue(model.asMap().get("preferenceForm") instanceof PortalPreferenceForm);
        assertTrue(model.containsAttribute(ModelKeys.TOKENCHECK));
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void testUpdatePreferences_valid() {
        ModelMap model = new ExtendedModelMap();
        PortalPreferenceForm form = new PortalPreferenceForm(new HashMap<String, PortalPreference>());
        final BindingResult errors = new BeanPropertyBindingResult(form, "form");
        SessionStatus sessionStatus = createMock(SessionStatus.class);

        final Set<Map.Entry<String, PortalPreference>> entries = form.getPreferenceMap().entrySet();

        for (Map.Entry<String, PortalPreference> entry : entries) {
            service.savePreference(entry.getValue());
        }
        sessionStatus.setComplete();

        expectLastCall();
        replay(service, sessionStatus);
        String view = controller.updatePreferences(form, errors, validToken, validToken,REFERRER_ID, model, sessionStatus);

        assertEquals("redirect:/app/admin/preferences?action=update&referringPageId=" + REFERRER_ID, view);
        assertTrue("Model has been cleared", model.isEmpty());

        verify(service, sessionStatus);
    }
    
    @Test(expected = SecurityException.class)
    public void testUpdatePreferences_invalidToken() {
        ModelMap model = new ExtendedModelMap();
        String invalidToken = AdminControllerUtil.generateSessionToken();
        PortalPreferenceForm form = new PortalPreferenceForm(new HashMap<String, PortalPreference>());
        final BindingResult errors = new BeanPropertyBindingResult(form, "form");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        sessionStatus.setComplete();
        
        expectLastCall();
        replay(service, sessionStatus);
        controller.updatePreferences(form, errors, validToken, invalidToken,REFERRER_ID, model, sessionStatus);

        assertFalse("Should not end up here", true);
        verify(service, sessionStatus);
    }

    @Test
    public void testUpdatePreferences_invalidPageSizeValue() {
        ModelMap model = new ExtendedModelMap();
        HashMap<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();
        PortalPreference pageSizePref = new PortalPreferenceImpl(PortalPreferenceKeys.PAGE_SIZE, "invalid");
        preferenceMap.put(PortalPreferenceKeys.PAGE_SIZE, pageSizePref);
        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        final BindingResult errors = new BeanPropertyBindingResult(form, "form");
        SessionStatus sessionStatus = createMock(SessionStatus.class);

        replay(service, sessionStatus);
        String view = controller.updatePreferences(form, errors, validToken, validToken,REFERRER_ID, model, sessionStatus);

        assertEquals(ViewNames.ADMIN_PREFERENCE_DETAIL, view);
        assertTrue(errors.hasErrors());
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertFalse("Model has not been cleared", model.isEmpty());

        verify(service, sessionStatus);
    }

}
