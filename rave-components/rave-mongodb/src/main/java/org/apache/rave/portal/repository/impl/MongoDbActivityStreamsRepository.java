package org.apache.rave.portal.repository.impl;

import org.apache.commons.lang.NotImplementedException;
import org.apache.rave.portal.model.ActivityStreamsEntry;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  Placeholder repository
 */
@Repository
public class MongoDbActivityStreamsRepository implements ActivityStreamsRepository {
    @Override
    public List<ActivityStreamsEntry> getAll() {
        throw new NotImplementedException();
    }

    @Override
    public List<ActivityStreamsEntry> getByUserId(String id) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public Class<? extends ActivityStreamsEntry> getType() {
        return ActivityStreamsEntry.class;
    }

    @Override
    public ActivityStreamsEntry get(String id) {
        throw new NotImplementedException();
    }

    @Override
    public ActivityStreamsEntry save(ActivityStreamsEntry item) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(ActivityStreamsEntry item) {
        throw new NotImplementedException();
    }
}
