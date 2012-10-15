package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 */
@Repository
public class MongoDbPageRepository implements PageRepository {

    @Autowired
    private MongoOperations mongoTemplate;

    @Override
    public List<Page> getAllPages(Long userId, PageType pageType) {
        List<MongoDbPage> pages = mongoTemplate.find(new Query(where("pageType").is(pageType).andOperator(where("owner.id").is(userId))), MongoDbPage.class);
        return CollectionUtils.<Page>toBaseTypedList(pages);
    }

    @Override
    public int deletePages(Long userId, PageType pageType) {
        int count = getAllPages(userId, pageType).size();
        mongoTemplate.remove(new Query(where("pageType").is(pageType).andOperator(where("owner.id").is(userId))), MongoDbPage.class);
        return count;
    }

    @Override
    public Page createPageForUser(User user, PageTemplate pt) {
        return null;
    }

    @Override
    public boolean hasPersonPage(long userId) {
        return getAllPages(userId, PageType.PERSON_PROFILE).size() > 0;
    }

    @Override
    public List<PageUser> getPagesForUser(Long userId, PageType pageType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PageUser getSingleRecord(Long userId, Long pageId) {
        return null;
    }

    @Override
    public Class<? extends Page> getType() {
        return MongoDbPage.class;
    }

    @Override
    public Page get(long id) {
        return mongoTemplate.findOne(new Query(where("id").is(id)), MongoDbPage.class);
    }

    @Override
    public Page save(Page item) {
        MongoDbPage page;
        if(item.getId() == null) {
            page = new MongoDbPage();
            page.setId(new Random().nextLong());
        } else {
            page = (MongoDbPage) get(item.getId());
            page.setId(item.getId());
        }
        page.setMembers(item.getMembers());
        page.setName(item.getName());
        page.setOwner(item.getOwner());
        page.setPageLayout(item.getPageLayout());
        page.setParentPage(item.getParentPage());
        page.setRegions(item.getRegions());
        page.setSubPages(item.getSubPages());
        mongoTemplate.save(page);
        return page;
    }

    @Override
    public void delete(Page item) {
        mongoTemplate.remove(new Query(where("id").is(item.getId())), MongoDbPage.class);
    }
}
