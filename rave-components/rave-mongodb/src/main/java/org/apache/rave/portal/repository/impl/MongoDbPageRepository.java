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

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.MongoDbConverter;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        hydratePages(pages);
        return CollectionUtils.<Page>toBaseTypedList(pages);
    }

    @Override
    public int deletePages(Long userId, PageType pageType) {
        Query query = new Query(where("pageType").is(pageType).andOperator(where("ownerId").is(userId)));
        int count = (int)mongoTemplate.count(query, MongoDbPage.class);
        mongoTemplate.remove(query, MongoDbPage.class);
        return count;
    }

    @Override
    public Page createPageForUser(User user, PageTemplate pt) {
        return save(convertTemplate(pt, user));
    }

    @Override
    public boolean hasPersonPage(long userId) {
        return mongoTemplate.count(new Query(where("pageType").is(PageType.PERSON_PROFILE).andOperator(where("ownerId").is(userId))), MongoDbPage.class) > 0;
    }

    @Override
    public List<PageUser> getPagesForUser(Long userId, PageType pageType) {
        List<MongoDbPage> pages = mongoTemplate.find(new Query(where("members").elemMatch(where("userId").is(userId)).andOperator(where("pageType").is(pageType))), MongoDbPage.class);
        List<PageUser> userList = Lists.newArrayList();
        for(MongoDbPage page : pages) {
            converter.hydrate(page, Page.class);
            userList.add(findPageUser(userId, page));
        }
        return userList;
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
        MongoDbPage fromDb = mongoTemplate.findById(id, MongoDbPage.class);
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

    private void hydratePages(List<MongoDbPage> pages) {
        for(MongoDbPage page : pages) {
            converter.hydrate(page, Page.class);
        }
    }

    /**
     * convertTemplate: PageTemplate, User -> Page
     * Converts the PageTemplate for Person Profiles into a Person Profile Page
     * @param pt PageTemplate
     * @param user User
     * @return Page
     */
    private Page convertTemplate(PageTemplate pt, User user) {
        Page p = new PageImpl();
        p.setName(pt.getName());
        p.setPageType(pt.getPageType());
        p.setOwner(user);
        PageUser pageUser = new PageUserImpl(user, p, pt.getRenderSequence());
        pageUser.setPageStatus(PageInvitationStatus.OWNER);
        pageUser.setEditor(true);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(pageUser);
        p.setMembers(members);

        p.setPageLayout(pt.getPageLayout());
        p.setRegions(convertRegions(pt.getPageTemplateRegions(), p));
        p.setSubPages(convertPages(pt.getSubPageTemplates(), p));
        return p;
    }

    /**
     * convertRegions: List of PageTemplateRegion, Page -> List of Regions
     * Converts the JpaRegion Templates of the Page Template to Regions for the page
     * @param pageTemplateRegions List of PageTemplateRegion
     * @param page Page
     * @return list of JpaRegion
     */
    private List<Region> convertRegions(List<PageTemplateRegion> pageTemplateRegions, Page page){
        List<Region> regions = new ArrayList<Region>();
        for (PageTemplateRegion ptr : pageTemplateRegions){
            Region region = new RegionImpl();
            region.setRenderOrder((int) ptr.getRenderSequence());
            region.setPage(page);
            region.setLocked(ptr.isLocked());
            region.setRegionWidgets(convertWidgets(ptr.getPageTemplateWidgets(), region));
            regions.add(region);
        }
        return regions;
    }

    /**
     * convertWidgets: List of PageTemplateWidget, JpaRegion -> List of RegionWidget
     * Converts the Page Template Widgets to RegionWidgets for the given JpaRegion
     * @param pageTemplateWidgets List of PageTemplateWidget
     * @param region JpaRegion
     * @return List of RegionWidget
     */
    private List<RegionWidget> convertWidgets(List<PageTemplateWidget> pageTemplateWidgets, Region region){
        List<RegionWidget> widgets = new ArrayList<RegionWidget>();
        for (PageTemplateWidget ptw : pageTemplateWidgets){
            RegionWidget regionWidget = new RegionWidgetImpl();
            regionWidget.setRegion(region);
            regionWidget.setCollapsed(false);
            regionWidget.setLocked(ptw.isLocked());
            regionWidget.setHideChrome(ptw.isHideChrome());
            regionWidget.setRenderOrder((int) ptw.getRenderSeq());
            regionWidget.setWidget(ptw.getWidget());
            widgets.add(regionWidget);
        }
        return widgets;
    }

    /**
     * convertPages: List of PageTemplate, Page -> List of Page
     * Converts the template subpages in to a list of Pages for the given page object
     * This is a recursive function. A sub page could have a list of sub pages.
     * @param pageTemplates List of PageTemplate
     * @param page Page
     * @return list of Page
     */
    private List<Page> convertPages(List<PageTemplate> pageTemplates, Page page){
        List<Page> pages = new ArrayList<Page>();
        for(PageTemplate pt : pageTemplates){
            Page lPage = new PageImpl();
            lPage.setName(pt.getName());
            lPage.setPageType(pt.getPageType());
            lPage.setOwner(page.getOwner());
            lPage.setPageLayout(pt.getPageLayout());
            lPage.setParentPage(page);
            lPage.setRegions(convertRegions(pt.getPageTemplateRegions(), lPage));

            // create new pageUser tuple
            PageUser pageUser = new PageUserImpl(lPage.getOwner(), lPage, pt.getRenderSequence());
            pageUser.setPageStatus(PageInvitationStatus.OWNER);
            pageUser.setEditor(true);
            List<PageUser> members = new ArrayList<PageUser>();
            members.add(pageUser);
            lPage.setMembers(members);
            // recursive call
            lPage.setSubPages((pt.getSubPageTemplates() == null || pt.getSubPageTemplates().isEmpty()) ? null : convertPages(pt.getSubPageTemplates(), lPage));
            pages.add(lPage);
        }
        return pages;
    }

    private PageUser findPageUser(Long userId, MongoDbPage page) {
        for(PageUser user : page.getMembers()) {
            if(user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

}
