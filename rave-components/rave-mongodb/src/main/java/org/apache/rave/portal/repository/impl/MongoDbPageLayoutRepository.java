package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoDbPageLayoutRepository implements PageLayoutRepository {
    @Override
    public PageLayout getByPageLayoutCode(String codename) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<PageLayout> getAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<PageLayout> getAllUserSelectable() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends PageLayout> getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PageLayout get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PageLayout save(PageLayout item) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(PageLayout item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
