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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.Group;
import org.apache.rave.portal.model.JpaGroup;
import org.apache.rave.model.Person;
import org.apache.rave.portal.model.impl.GroupImpl;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-applicationContext.xml","classpath:test-dataContext.xml"} )
public class JpaGroupConverterTest {

    @Autowired
    JpaGroupConverter converter;

    private Group group;
    private String description = "Test Group";
    private Long entityId = Long.valueOf(400);
    private String title = "GroupTitle";
    private Person owner = null;
    private List<String> members = null;

    private static final String ownerDisplayName = "Bob";
    private static final String ownerGivenName = "Smith";
    private static final String ownerId = "12345";

    @Before
    public void setup() {
        members = new ArrayList<String>();
        owner = new PersonImpl();
        owner.setDisplayName(ownerDisplayName);
        owner.setGivenName(ownerGivenName);
        members.add(ownerId);

        group = new GroupImpl();
        group.setDescription(description);
        group.setTitle(title);
        group.setOwnerId(ownerId);
        group.setMemberIds(members);
    }

    @Test
    public void testNoConversion() {
        JpaGroup group = new JpaGroup();
        assertThat(converter.convert(group), is(sameInstance(group)));
    }

    @Test
    public void nullConversion() {
        Group category = null;
        assertThat(converter.convert(category), is(nullValue()));
    }

    @Test
    public void testConvertGroupToJpaGroup() {
        JpaGroup jpaGroup = converter.convert(group);

        assertThat(jpaGroup, is(not(sameInstance(group))));
        assertThat(jpaGroup, is(instanceOf(JpaGroup.class)));
        assertEquals(description, jpaGroup.getDescription());
        assertEquals(title, jpaGroup.getTitle());
        assertEquals(ownerId, jpaGroup.getOwnerId());
        assertEquals(members.size(), jpaGroup.getMemberIds().size());
    }

}
