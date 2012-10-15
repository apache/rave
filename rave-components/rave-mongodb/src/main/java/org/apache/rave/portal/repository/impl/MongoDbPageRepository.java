package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.MongoDbConverter;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 */
@Repository
public class MongoDbPageRepository implements PageRepository {

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private MongoDbConverter converter;

    @Override
    public List<Page> getAllPages(Long userId, PageType pageType) {
        List<MongoDbPage> pages = mongoTemplate.find(new Query(where("pageType").is(pageType).andOperator(where("ownerId").is(userId))), MongoDbPage.class);
        for(MongoDbPage page : pages) {
            converter.hydrate(page, Page.class);
        }
        return CollectionUtils.<Page>toBaseTypedList(pages);
    }

    @Override
    public int deletePages(Long userId, PageType pageType) {
        int count = getAllPages(userId, pageType).size();
        mongoTemplate.remove(new Query(where("pageType").is(pageType).andOperator(where("ownerId").is(userId))), MongoDbPage.class);
        return count;
    }

    @Override
    public Page createPageForUser(User user, PageTemplate pt) {
        //TODO
        return null;
    }

    @Override
    public boolean hasPersonPage(long userId) {
        return getAllPages(userId, PageType.PERSON_PROFILE).size() > 0;
    }

    @Override
    public List<PageUser> getPagesForUser(Long userId, PageType pageType) {
        //TODO
        return null;
    }

    @Override
    public PageUser getSingleRecord(Long userId, Long pageId) {
        Page page = get(pageId);
        for(PageUser user : page.getMembers()) {
            if(user.getUser().getId().equals(userId))
                return user;
        }
        return null;
    }

    @Override
    public Class<? extends Page> getType() {
        return MongoDbPage.class;
    }

    @Override
    public Page get(long id) {
        MongoDbPage fromDb = mongoTemplate.findOne(new Query(where("_id").is(id)), MongoDbPage.class);
        if(fromDb == null) {
            throw new IllegalStateException("Could not find requested page: " + id);
        }
        converter.hydrate(fromDb, Page.class);
        return fromDb;
    }

    @Override
    public Page save(Page item) {
        MongoDbPage converted = converter.convert(item, Page.class);
        mongoTemplate.save(converted);
        converter.hydrate(converted, Page.class);
        return converted;
    }

    @Override
    public void delete(Page item) {
        mongoTemplate.remove(new Query(where("id").is(item.getId())), MongoDbPage.class);
    }
}
