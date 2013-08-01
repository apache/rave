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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.events.RaveEventManager;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test class for {@link DefaultPortalPreferenceService}
 */
public class DefaultPortalPreferenceServiceTest {

    PortalPreferenceService service;
    PortalPreferenceRepository repository;
    RaveEventManager manager;

    @Before
    public void setUp() throws Exception {
        repository = createMock(PortalPreferenceRepository.class);
        manager = createMock(RaveEventManager.class);
        service = new DefaultPortalPreferenceService(repository, manager);
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
    public void getAll() {
        List<PortalPreference> portalPreferences = new ArrayList<PortalPreference>();
        expect(repository.getCountAll()).andReturn(1);
        expect(repository.getAll()).andReturn(portalPreferences);
        replay(repository);

        List<PortalPreference> result = service.getAll().getResultSet();
        assertThat(result, is(sameInstance(portalPreferences)));

        verify(repository);
    }

    @Test
    public void getLimitedList() {
        PortalPreference pp1 = new PortalPreferenceImpl("key1", "value1");
        PortalPreference pp2 = new PortalPreferenceImpl("key2", "value1");
        List<PortalPreference> portalPreferences = new ArrayList<PortalPreference>();
        portalPreferences.add(pp1);
        portalPreferences.add(pp2);
        final int pageSize = 10;
        expect(repository.getCountAll()).andReturn(2);
        expect(repository.getLimitedList(0, pageSize)).andReturn(portalPreferences);
        replay(repository);

        SearchResult<PortalPreference> result = service.getLimitedList(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(portalPreferences, result.getResultSet());
        verify(repository);
    }

    @Test
    public void testSaveKeyValue_new() {
        final String key = "foo";
        final String value = "bar";
        PortalPreference fooBar = new PortalPreferenceImpl(key, value);
        PortalPreferenceImpl fooBarSaved = new PortalPreferenceImpl(key, value);

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
        PortalPreferenceImpl fooBar = new PortalPreferenceImpl(key, value);
        PortalPreferenceImpl fooBarSaved = new PortalPreferenceImpl(key, newValue);
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
        PortalPreference fooBar = new PortalPreferenceImpl(key, values);
        PortalPreferenceImpl fooBarSaved = new PortalPreferenceImpl(key, values);

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
        PortalPreferenceImpl fooBar = new PortalPreferenceImpl(key, values);
        PortalPreferenceImpl fooBarSaved = new PortalPreferenceImpl(key, newValues);

        expect(repository.getByKey(key)).andReturn(fooBar).once();
        expect(repository.save(fooBar)).andReturn(fooBarSaved).once();
        replay(repository);
        service.savePreference(key, values);
        verify(repository);
    }

    @Test
    public void testSavePreference() {
        PortalPreference title = titlePreference();
        PortalPreferenceImpl savedTitle = new PortalPreferenceImpl("title", "Rave");

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
        PortalPreference colorPref = new PortalPreferenceImpl("colors", colors);

        List<PortalPreference> preferences = new ArrayList<PortalPreference>();
        preferences.add(title);
        preferences.add(colorPref);

        return preferences;
    }

    private static PortalPreference titlePreference() {
        return new PortalPreferenceImpl("title", "Rave");
    }
}
