package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * User: DSULLIVAN
 * Date: 12/7/12
 * Time: 1:24 PM
 */
public class MongoDbWidgetRatingRepositoryTest {
    private MongoDbWidgetRatingRepository ratingRepository;
    private MongoWidgetOperations template;

    @Before
    public void setup() {
        template = createMock(MongoWidgetOperations.class);
        ratingRepository = new MongoDbWidgetRatingRepository();
        ratingRepository.setTemplate(template);
    }

    @Test
    public void getByWidgetIdAndUserId_Valid(){
        long widgetId = 222;
        long userId = 333;
        Widget widget = new WidgetImpl();
        WidgetRating widgetRating = new WidgetRatingImpl();
        widgetRating.setUserId(userId);
        widget.setRatings(Arrays.asList(widgetRating));
        expect(template.get(widgetId)).andReturn(widget);
                   replay(template);
        assertThat(ratingRepository.getByWidgetIdAndUserId(widgetId,userId), is(sameInstance(widgetRating)));
    }

    @Test
    public void getByWidgetIdAndUserId_Null(){
        long widgetId = 222;
        long userId = 333;
        Widget widget = new WidgetImpl();
        widget.setRatings(new ArrayList<WidgetRating>());
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);
        assertNull(ratingRepository.getByWidgetIdAndUserId(widgetId, userId));
    }

    @Test
    public void getByWidgetIdAndUserId_Diff_Id(){
        long widgetId = 222;
        long userId = 334;
        Widget widget = new WidgetImpl();
        WidgetRating widgetRating = new WidgetRatingImpl();
        widgetRating.setUserId((long)444);
        widget.setRatings(Arrays.asList(widgetRating));
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);
        assertNull(ratingRepository.getByWidgetIdAndUserId(widgetId,userId));
    }

    @Test
    public void deleteAll_Valid(){
        long userId = 233;
        List<Widget> widgets = new ArrayList<Widget>();
        Widget delete = new WidgetImpl();
        WidgetRating rating = new WidgetRatingImpl();
        List<WidgetRating> ratings= new ArrayList<WidgetRating>();
        ratings.add(rating);
        delete.setRatings(ratings);
        rating.setUserId(userId);
        widgets.add(delete);
        expect(template.find(query(where("ratings").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        expect(template.save(delete)).andReturn(null);
        replay(template);

        int count = ratingRepository.deleteAll(userId);
        assertFalse(ratings.contains(rating));
        assertThat(count, is(1));
    }

    @Test
    public void deleteAll_Diff_Id(){
        long userId = 111;
        Widget widget = new WidgetImpl();
        List<Widget> widgets = Arrays.asList(widget);
        WidgetRating rating = new WidgetRatingImpl();
        rating.setUserId((long)222);
        widget.setRatings(Arrays.asList(rating));
        expect(template.find(query(where("ratings").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        replay(template);

        int count = ratingRepository.deleteAll(userId);
        assertThat(count, is(0));
    }

    @Test
    public void getType_Valid(){
        assertThat((Class<WidgetRating>)ratingRepository.getType(),is(equalTo(WidgetRating.class)));
    }

    @Test
    public void get_Valid(){
        long id = 342;
        Widget found = new WidgetImpl();
        WidgetRating widgetRating = new WidgetRatingImpl();
        widgetRating.setId(id);
        found.setRatings(Arrays.asList(widgetRating));
        expect(template.findOne(query(where("ratings").elemMatch(where("_id").is(id))))).andReturn(found);
        replay(template);

        assertThat(ratingRepository.get(id), is(sameInstance(widgetRating)));
    }

    @Test
    public void get_Null(){
        long id = 123;
        Widget widget = new WidgetImpl();
        expect(template.findOne(query(where("ratings").elemMatch(where("_id").is(id))))).andReturn(widget);
        replay(template);
        widget.setRatings(new ArrayList<WidgetRating>());

        assertNull(ratingRepository.get(id));
    }

    @Test
    public void get_Diff_Id(){
          Widget widget = new WidgetImpl();
        long id = 555;
        WidgetRating rating = new WidgetRatingImpl();
        rating.setId((long)444);
        widget.setRatings(Arrays.asList(rating));
        expect(template.findOne(query(where("ratings").elemMatch(where("_id").is(id))))).andReturn(widget);
        replay(template);

        assertNull(ratingRepository.get(id));
    }

    @Test
    public void save_Id_Valid(){
        WidgetRating item = new WidgetRatingImpl();
        WidgetRating lookup = new WidgetRatingImpl();
        long widgetId = 2134;
        long userId = 3245;
        long id = 3245;
        int score = 838;
        item.setUserId(userId);
        item.setScore(score);
        item.setWidgetId(widgetId);
        item.setId(id);
        lookup.setId(id);
        Widget widget = new WidgetImpl();
        widget.setRatings(Arrays.asList(lookup));

        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(widget);
        replay(template);

        WidgetRating widgetRating = ratingRepository.save(item);

        assertThat(item.getScore(), is(sameInstance(lookup.getScore())));
        assertThat(item.getUserId(), is(sameInstance(lookup.getUserId())));
        assertThat(item.getScore(), is(sameInstance(lookup.getScore())));
        assertThat(widgetRating, is(sameInstance(lookup)));
    }

    @Test
    public void save_Id_Null(){
        WidgetRating item = new WidgetRatingImpl();
        long widgetId = 5544;
        item.setWidgetId(widgetId);
        Widget widget = new WidgetImpl();
        widget.setRatings(new ArrayList<WidgetRating>());
        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(widget);
        replay(template);

        WidgetRating widgetRating = ratingRepository.save(item);
        assertNotNull(item.getId());
        assertTrue(widget.getRatings().contains(item));
        assertThat(widgetRating, is(sameInstance(item)));
    }

    @Test
    public void save_Null(){
        WidgetRating item = new WidgetRatingImpl();
        Widget widget = new WidgetImpl();
        widget.setRatings(new ArrayList<WidgetRating>());
        long id = 123;
        long widgetId = 321;
        Widget saved = new WidgetImpl();
        saved.setRatings(new ArrayList<WidgetRating>());
        item.setWidgetId(widgetId);
        item.setId(id);
        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(saved);
        replay(template);

        assertNull(ratingRepository.save(item));
    }

    @Test
    public void save_Diff_Id(){
          WidgetRating item = new WidgetRatingImpl();
        item.setWidgetId((long)3333);
        item.setId((long)123);
        Widget widget = new WidgetImpl();
        WidgetRating exist = new WidgetRatingImpl();
        exist.setId((long)4444);
        widget.setRatings(Arrays.asList(exist));

        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(widget);
        replay(template);

        assertNull(ratingRepository.save(item));
    }

    @Test
    public void delete_Valid(){
        WidgetRating item = new WidgetRatingImpl();
        long widgetId = 387383;
        long id = 234;
        item.setWidgetId(widgetId);
        item.setId(id);
        Widget widget = new WidgetImpl();
        ArrayList<WidgetRating> ratings = new ArrayList<WidgetRating>();
        ratings.add(item);
        widget.setRatings(ratings);
        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(null);
        replay(template);

        ratingRepository.delete(item);
        assertFalse(ratings.contains(item));
        verify(template);
    }

    @Test
    public void delete_Id_Not_Equals(){
         WidgetRating item = new WidgetRatingImpl();
        item.setWidgetId((long)32323);
        item.setId((long)333333);
        Widget widget = new WidgetImpl();
        WidgetRating exist = new WidgetRatingImpl();
        exist.setId((long) 323);
        widget.setRatings(Arrays.asList(exist));
        expect(template.get(item.getWidgetId())).andReturn(widget);
        expect(template.save(widget)).andReturn(null);
        replay(template);

        ratingRepository.delete(item);
        verify(template);
    }
}
