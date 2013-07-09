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

import org.apache.rave.model.Tag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.portal.service.TagService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Test for {@link DefaultTagService}
 */
public class DefaultTagServiceTest {

    private TagService service;
    private TagRepository repository;

    @Before
    public void setup() {
        repository = createMock(TagRepository.class);
        service = new DefaultTagService(repository);
    }

    @Test
    public void getTagById() {
        Tag tag = createTag("test");
        expect(repository.get("1")).andReturn(tag);
        replay(repository);
        Tag sTag = service.getTagById("1");
        assertEquals(sTag, tag);
        verify(repository);
    }


    private static Tag createTag(String keyword) {
        TagImpl tag = new TagImpl(keyword);
        return tag;
    }

    @Test
    public void getTagById_NotFound() {
        final String entityId = "456";
        expect(repository.get(entityId)).andReturn(null);
        replay(repository);
        Tag sTag = service.getTagById(entityId);
        assertNull(sTag);

        verify(repository);
    }

    @Test
    public void allTags() {
        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = createTag("test");
        tags.add(tag);
        expect(repository.getAll()).andReturn(tags);
        replay(repository);
        List<Tag> allTags = service.getAllTags();
        verify(repository);
        assertTrue(allTags.size() > 0);
    }

    @Test
    public void getByKeyword() {
        Tag tag = createTag("test");
        expect(repository.getByKeyword("test")).andReturn(tag);
        expect(repository.getByKeyword("TEST")).andReturn(tag);
        expect(repository.getByKeyword(" test")).andReturn(tag);

    }


}
