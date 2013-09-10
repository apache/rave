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
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.apache.rave.portal.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 */
@Repository
public class MongoDbPageRepository implements PageRepository {

    @Autowired
    private MongoPageOperations template;

    @Override
    public List<Page> getAllPagesForUserType(String userId, String pageType) {
        return sort(template.find(query(where("pageType").is(getString(pageType)).andOperator(where("members").elemMatch(where("userId").is(userId))))), userId);
    }

    @Override
    public List<Page> getPagesForContextType(String contextId, String pageType) {
        return template.find(query(where("pageType").is(getString(pageType)).andOperator(where("contextId").is(contextId))));
    }

    @Override
    public int deletePages(String userId, String pageType) {
        Query query = query(where("pageType").is(pageType).andOperator(where("ownerId").is(userId)));
        int count = (int)template.count(query);
        template.remove(query);
        return count;
    }

    @Override
    public Page createPageForUser(User user, PageTemplate pt) {
        return save(convertTemplate(pt, user));
    }

    @Override
    public boolean hasPersonPage(String userId) {
        return template.count(query(where("pageType").is(PageType.PERSON_PROFILE.toString().toUpperCase()).andOperator(where("ownerId").is(userId)))) > 0;
    }

    @Override
    public boolean hasPage(String userId, String pageType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<PageUser> getPagesForUser(String userId, String pageType) {
        List<Page> pages = template.find(query(where("members").elemMatch(where("userId").is(userId)).andOperator(where("pageType").is(getString(pageType)))));
        List<PageUser> userList = Lists.newArrayList();
        for(Page page : pages) {
            userList.add(findPageUser(userId, page));
        }
        return sortUsers(userList, userId);
    }

    @Override
    public PageUser getSingleRecord(String userId, String pageId) {
        Page page = get(pageId);
        for(PageUser user : page.getMembers()) {
            if(user.getUserId().equals(userId))
                return user;
        }
        return null;
    }

    @Override
    public Class<? extends Page> getType() {
        return MongoDbPage.class;
    }

    @Override
    public Page get(String  id) {
        return template.get(id);
    }

    @Override
    public Page save(Page item) {
        return template.save(item);
    }

    @Override
    public void delete(Page item) {
        template.remove(query(where("_id").is(item.getId())));
    }

    private List<Page> sort(List<Page> pages, final String  userId) {
        Collections.sort(pages, new Comparator<Page>() {
            @Override
            public int compare(Page page, Page page1) {
                return getRenderOrder(page, userId) - getRenderOrder(page1, userId);
            }
        });
        return pages;
    }

    private List<PageUser> sortUsers(List<PageUser> userList, String userId) {
        Collections.sort(userList, new Comparator<PageUser>() {
            @Override
            public int compare(PageUser pageUser, PageUser pageUser1) {
                return getRenderOrder(pageUser) - getRenderOrder(pageUser1);
            }
        });
        return userList;
    }

    private int getRenderOrder(PageUser pageUser) {
        return pageUser == null || pageUser.getRenderSequence() == null ? Integer.MAX_VALUE : pageUser.getRenderSequence().intValue();
    }

    private int getRenderOrder(Page page, String  userId) {
        for(PageUser user : page.getMembers()) {
            if(user.getUserId().equals(userId)) {
                return getRenderOrder(user);
            }
        }
        return -1;
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
        p.setOwnerId(user.getId());
        PageUser pageUser = new PageUserImpl(user.getId(), p, pt.getRenderSequence());
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
            regionWidget.setWidgetId(ptw.getWidgetId());
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
            lPage.setOwnerId(page.getOwnerId());
            lPage.setPageLayout(pt.getPageLayout());
            lPage.setParentPage(page);
            lPage.setRegions(convertRegions(pt.getPageTemplateRegions(), lPage));

            // create new pageUser tuple
            PageUser pageUser = new PageUserImpl(lPage.getOwnerId(), lPage, pt.getRenderSequence());
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

    private PageUser findPageUser(String userId, Page page) {
        for(PageUser user : page.getMembers()) {
            if(user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    private String getString(String pageType) {
        return pageType.toUpperCase();
    }

    public MongoPageOperations getTemplate() {
        return template;
    }

    public void setTemplate(MongoPageOperations template) {
        this.template = template;
    }


    @Override
    public List<Page> getAll() {
        return template.find(new Query());
    }

    @Override
    public List<Page> getLimitedList(int offset, int pageSize) {
        return template.find(new Query().skip(offset).limit(pageSize));
    }

    @Override
    public int getCountAll() {
        return (int) template.count(new Query());
    }
}
