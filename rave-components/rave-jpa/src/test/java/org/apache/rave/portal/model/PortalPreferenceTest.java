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

package org.apache.rave.portal.model;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.PortalPreference;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Test for {@link JpaPortalPreference}
 */
public class PortalPreferenceTest {
    private static final String KEY = "foo";
    private static final List<String> VALUES = getValueList();

    @Test
    public void testGettersSetters() throws Exception {
        Long entityId = 1L;
        JpaPortalPreference preference = new JpaPortalPreference();

        assertNull(preference.getValue());

        preference.setEntityId(entityId);
        preference.setKey(KEY);
        preference.setValues(VALUES);

        assertEquals(entityId, preference.getEntityId());
        assertEquals(KEY, preference.getKey());
        assertEquals(VALUES, preference.getValues());
    }

    @Test
    public void singleValuePreference() throws Exception {
        PortalPreference preference = new JpaPortalPreference(KEY, "bar");
        assertEquals(KEY, preference.getKey());
        assertEquals("bar", preference.getValue());
    }

    @Test
    public void testGetValues() throws Exception {
        PortalPreference preference = new JpaPortalPreference(KEY, VALUES);
        assertEquals(KEY, preference.getKey());
        assertEquals(VALUES, preference.getValues());
    }

    @Test
    public void testSetValues() throws Exception {
        PortalPreference preference = new JpaPortalPreference(KEY, VALUES);
        assertEquals(KEY, preference.getKey());
        assertEquals(VALUES, preference.getValues());
        preference.setValue("tree");
        assertEquals("tree", preference.getValue());
        assertEquals(1, preference.getValues().size());
    }


    @Test(expected = NotSupportedException.class)
    public void getValueFailsForMultiValue() {
        PortalPreference preference = new JpaPortalPreference(KEY, VALUES);
        preference.getValue();
        assertFalse("Expected exception", true);

    }

    private static List<String> getValueList() {
        List<String> values = new ArrayList<String>();
        values.add("bar");
        values.add("baz");
        return values;
    }
}
