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

package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Test class for {@link DefaultPortalPreferenceService}
 */
public class DefaultPortalPreferenceServiceTest {

    PortalPreferenceService service;
    PortalPreferenceRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = createMock(PortalPreferenceRepository.class);
        service = new DefaultPortalPreferenceService(repository);
    }

    @Test
    public void testGetPreferencesAsMap() {
        expect(repository.getAll()).andReturn(portalPreferenceList());
        replay(repository);

        final Map<String, PortalPreference> map = service.getPreferencesAsMap();
        verify(repository);

        assertEquals(2, map.size());
        assertEquals("Rave", map.get("title").getValue());

        final List<String> colors = map.get("colors").getValues();
        assertTrue(colors.contains("red"));
        assertTrue(colors.contains("yellow"));
        assertTrue(colors.contains("blue"));
    }

    @Test
    public void testGetPreferencesAsMap_noPreferences() {
        expect(repository.getAll()).andReturn(Collections.<PortalPreference>emptyList());
        replay(repository);

        final Map<String, PortalPreference> map = service.getPreferencesAsMap();
        verify(repository);

        assertTrue(map.isEmpty());
    }

    @Test
    public void testGetPreference() {
        final String key = "title";
        expect(repository.getByKey(key)).andReturn(titlePreference());
        replay(repository);

        final PortalPreference preference = service.getPreference(key);
        verify(repository);

        assertEquals("Rave", preference.getValue());
    }

    @Test
    public void testGetPreference_notExisting() {
        final String key = "foo";
        expect(repository.getByKey(key)).andReturn(null);
        replay(repository);

        final PortalPreference preference = service.getPreference(key);
        verify(repository);

        assertNull(preference);
    }

    @Test
    public void testSaveKeyValue_new() {
        final String key = "foo";
        final String value = "bar";
        PortalPreference fooBar = new PortalPreference(key, value);
        PortalPreference fooBarSaved = new PortalPreference(key, value);
        fooBarSaved.setEntityId(123L);

        expect(repository.getByKey(key)).andReturn(null).once();
        expect(repository.save(fooBar)).andReturn(fooBarSaved).once();
        replay(repository);
        service.savePreference(key, value);
        verify(repository);
    }

    @Test
    public void testSaveKeyValue_existing() {
        final String key = "foo";
        final String value = "bar";
        final String newValue = "baz";
        PortalPreference fooBar = new PortalPreference(key, value);
        fooBar.setEntityId(123L);
        PortalPreference fooBarSaved = new PortalPreference(key, newValue);
        fooBarSaved.setEntityId(123L);

        expect(repository.getByKey(key)).andReturn(fooBar).once();
        expect(repository.save(fooBar)).andReturn(fooBarSaved).once();
        replay(repository);
        service.savePreference(key, value);
        verify(repository);
    }

    @Test
    public void testSaveKeyValues_new() {
        final String key = "foo";
        List<String> values = new ArrayList<String>();
        values.add("bar");
        values.add("baz");
        PortalPreference fooBar = new PortalPreference(key, values);
        PortalPreference fooBarSaved = new PortalPreference(key, values);
        fooBarSaved.setEntityId(123L);

        expect(repository.getByKey(key)).andReturn(null).once();
        expect(repository.save(fooBar)).andReturn(fooBarSaved).once();
        replay(repository);
        service.savePreference(key, values);
        verify(repository);
    }

    @Test
    public void testSaveKeyValues_existing() {
        final String key = "foo";
        List<String> values = new ArrayList<String>();
        values.add("bar");
        values.add("baz");
        List<String> newValues = new ArrayList<String>();
        values.add("bar2");
        values.add("baz2");
        PortalPreference fooBar = new PortalPreference(key, values);
        fooBar.setEntityId(123L);
        PortalPreference fooBarSaved = new PortalPreference(key, newValues);
        fooBarSaved.setEntityId(123L);

        expect(repository.getByKey(key)).andReturn(fooBar).once();
        expect(repository.save(fooBar)).andReturn(fooBarSaved).once();
        replay(repository);
        service.savePreference(key, values);
        verify(repository);
    }

    @Test
    public void testSavePreference() {
        PortalPreference title = titlePreference();
        PortalPreference savedTitle = new PortalPreference("title", "Rave");
        savedTitle.setEntityId(123L);

        expect(repository.save(title)).andReturn(savedTitle).once();
        replay(repository);

        service.savePreference(title);
        verify(repository);
    }

    private static List<PortalPreference> portalPreferenceList() {
        PortalPreference title = titlePreference();

        List<String> colors = new ArrayList<String>();
        colors.add("red");
        colors.add("yellow");
        colors.add("blue");
        PortalPreference colorPref = new PortalPreference("colors", colors);

        List<PortalPreference> preferences = new ArrayList<PortalPreference>();
        preferences.add(title);
        preferences.add(colorPref);

        return preferences;
    }

    private static PortalPreference titlePreference() {
        return new PortalPreference("title", "Rave");
    }
}
