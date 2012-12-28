/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.easymock.EasyMock.createMock;

/**
 * User: DSULLIVAN
 * Date: 12/7/12
 * Time: 2:46 PM
 */
public class MongoDbWidgetTagRepositoryTest {
    private MongoDbWidgetRepository tagRepository;
    private MongoWidgetOperations template;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        tagRepository = new MongoDbWidgetRepository();
        template = createMock(MongoWidgetOperations.class);
        tagRepository.setTemplate(template);
    }

/*
    @Test
    public void getByWidgetIdAndTag_Valid() {
        long widgetId = 3874;
        String keyword = "booger";
        Widget widget = new WidgetImpl();
        WidgetTag widgetTag = new WidgetTagImpl();
        Tag tag = new TagImpl();
        tag.setKeyword(keyword);
        widgetTag.setTag(tag);
        widget.setTags(Arrays.asList(widgetTag));
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        assertThat(tagRepository.getByWidgetIdAndTag(widgetId, keyword), is(sameInstance(widgetTag)));
    }

    @Test
    public void getByWidgetIdAndTag_Null() {
        long widgetId = 3874;
        String keyword = "booger";
        Widget widget = new WidgetImpl();
        widget.setTags(new ArrayList<WidgetTag>());
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        assertNull(tagRepository.getByWidgetIdAndTag(widgetId, keyword));
    }

    @Test
    public void getByWidgetIdAndTag_Diff_Keyword(){
        long widgetId = 3874;
        String keyword = "booger";
        Widget widget = new WidgetImpl();
        WidgetTag widgetTag = new WidgetTagImpl();
        Tag tag = new TagImpl();
        tag.setKeyword("blah");
        widgetTag.setTag(tag);
        widget.setTags(Arrays.asList(widgetTag));
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        assertNull(tagRepository.getByWidgetIdAndTag(widgetId, keyword));
    }

    @Test
    public void getType_Valid() {
        assertThat((Class<WidgetTag>) tagRepository.getType(), is(equalTo(WidgetTag.class)));
    }

    @Test
    public void get_Valid() {
        thrown.expect(NotSupportedException.class);
        tagRepository.get((long) 123);
    }

    @Test
    public void save_Valid_Tag_Valid(){
        WidgetTag item = new WidgetTagImpl();
        item.setWidgetId((long)123);
        Tag tag = new TagImpl();
        String keyword = "keyword";
        tag.setKeyword(keyword);
        item.setTag(tag);
        Widget widget = new WidgetImpl();
        widget.setTags(Arrays.asList(item));

        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(widget);
        replay(template);

        WidgetTag returned = tagRepository.save(item);
        assertThat(returned, is(sameInstance(item)));
    }

    @Test
    public void save_Valid_Tag_Null(){
        WidgetTag item = new WidgetTagImpl();
        item.setWidgetId((long)123);
        Tag tag = new TagImpl();
        String keyword = "keyword";
        tag.setKeyword(keyword);
        item.setTag(tag);
        Widget widget = new WidgetImpl();
        widget.setTags(new ArrayList<WidgetTag>());
        Widget saved = new WidgetImpl();
        saved.setTags(new ArrayList<WidgetTag>());

        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(saved);
        replay(template);

        WidgetTag returned = tagRepository.save(item);
        assertNull(returned);
    }

    @Test
    public void delete_Valid(){
        WidgetTag item = new WidgetTagImpl();
        Tag tag = new TagImpl();
        String keyword = "keyword";
        tag.setKeyword(keyword);
        item.setTag(tag);
        Widget widget = new WidgetImpl();
        ArrayList<WidgetTag> tags = new ArrayList<WidgetTag>();
        tags.add(item);
        widget.setTags(tags);
        long widgetId = 12354;
        item.setWidgetId(widgetId);

        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        tagRepository.delete(item);

        assertFalse(tags.contains(item));
        verify(template);
    }
    @Test
    public void delete_Null(){
        WidgetTag item = new WidgetTagImpl();
        Tag tag = new TagImpl();
        String keyword = "keyword";
        tag.setKeyword(keyword);
        item.setTag(tag);
        Widget widget = new WidgetImpl();
        ArrayList<WidgetTag> tags = new ArrayList<WidgetTag>();
        widget.setTags(tags);
        long widgetId = 12354;
        item.setWidgetId(widgetId);

        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        tagRepository.delete(item);
        verify(template);
    }

    @Test
    public void delete_Tag_NoMatch(){
        WidgetTag item = new WidgetTagImpl();
        Tag tag = new TagImpl();
        String keyword = "keyword";
        tag.setKeyword(keyword);
        item.setTag(tag);
        Widget widget = new WidgetImpl();
        ArrayList<WidgetTag> tags = new ArrayList<WidgetTag>();
        WidgetTag extra = new WidgetTagImpl();
        extra.setTag(new TagImpl("extra"));
        tags.add(extra);
        widget.setTags(tags);
        long widgetId = 12354;
        item.setWidgetId(widgetId);

        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        tagRepository.delete(item);
        verify(template);
    }

*/

}
