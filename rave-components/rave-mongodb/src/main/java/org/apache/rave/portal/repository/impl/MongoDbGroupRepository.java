package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.Group;
import org.apache.rave.portal.model.MongoDbGroup;
import org.apache.rave.portal.repository.MongoGroupOperations;
import org.apache.rave.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbGroupRepository implements GroupRepository {

    @Autowired
    private MongoGroupOperations template;

    @Override
    public Group findByTitle(String title) {
        return template.findOne(query(where("title").is(title)));
    }

    @Override
    public Class<? extends Group> getType() {
        return MongoDbGroup.class;
    }

    @Override
    public Group get(String id) {
        return template.get(id);
    }

    @Override
    public List<Group> getAll() {
        return template.find(new Query());
    }

    @Override
    public Group save(Group item) {
        return template.save(item);
    }

    @Override
    public void delete(Group item) {
        template.remove(query(where("_id").is(item.getId())));
    }

    public void setTemplate(MongoGroupOperations template) {
        this.template = template;
    }
}
