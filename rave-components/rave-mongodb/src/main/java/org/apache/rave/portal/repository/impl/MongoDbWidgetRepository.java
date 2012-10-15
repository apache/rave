package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 */
@Repository
public class MongoDbWidgetRepository implements WidgetRepository {

    @Autowired
    private MongoOperations template;

    @Override
    public List<Widget> getAll() {
        return null;
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        return null;
    }

    @Override
    public int getCountAll() {
        //TODO replace with native query
        return template.findAll(MongoDbWidget.class).size();
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        return null;
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        return 0;
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        return null;
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm, int offset, int pageSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Widget getByUrl(String widgetUrl) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Long, WidgetRating> getUsersWidgetRatings(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int unassignWidgetOwner(long userId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends Widget> getType() {
        return Widget.class;
    }

    @Override
    public Widget get(long id) {
        return template.findById(id, MongoDbWidget.class);
    }

    @Override
    public Widget save(Widget item) {
        template.save(item);
        return item;
    }

    @Override
    public void delete(Widget item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
