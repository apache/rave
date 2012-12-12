package org.apache.rave.portal.repository.impl;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.model.impl.WidgetTagImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/7/12
 * Time: 2:46 PM
 */
public class MongoDbWidgetTagRepositoryTest {
    private MongoDbWidgetTagRepository tagRepository;
    private MongoWidgetOperations template;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        tagRepository = new MongoDbWidgetTagRepository();
        template = createMock(MongoWidgetOperations.class);
        tagRepository.setTemplate(template);
    }

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


}
