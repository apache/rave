/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.exception.DataSerializationException;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.JpaPageConverter;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.util.CollectionUtils;
import org.apache.rave.util.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaPageRepository implements PageRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaPageConverter pageConverter;

    @Override
    public Class<? extends Page> getType() {
        return JpaPage.class;
    }

    @Override
    public Page get(String id) {
        return expandProperties(manager.find(JpaPage.class, Long.parseLong(id)));
    }

    @Override
    public Page save(Page item) {
        JpaPage page = pageConverter.convert(item);
        page.serializeData();
        return saveOrUpdate(page.getEntityId(), manager, page);
    }

    @Override
    public void delete(Page item) {
        JpaPage jpaPage = item instanceof JpaPage ? (JpaPage)item : (JpaPage)get(item.getId());
        for(Page p : jpaPage.getSubPages()) {
            delete(p);
        }
        //Must remove the page users from the page in order for OpenJpa to persist change
        removePageUsers(jpaPage);
        jpaPage.setParentPage(null);
        manager.flush();
        manager.remove(jpaPage);
    }

    @Override
    public List<Page> getAllPagesForUserType(String userId, String pageType) {
        TypedQuery<JpaPage> query = manager.createNamedQuery(JpaPageUser.GET_BY_USER_ID_AND_PAGE_TYPE, JpaPage.class);
        query.setParameter("userId", userId);
        query.setParameter("pageType", pageType.toUpperCase());
        return expandProperties(CollectionUtils.<Page>toBaseTypedList(query.getResultList()));
    }

    @Override
    public List<Page> getPagesForContextType(String contextId, String pageType) {
        TypedQuery<JpaPage> query = manager.createNamedQuery(JpaPage.GET_BY_CONTEXT_AND_PAGE_TYPE, JpaPage.class);
        query.setParameter("contextId", contextId);
        query.setParameter("pageType", pageType.toUpperCase());
        return expandProperties(CollectionUtils.<Page>toBaseTypedList(query.getResultList()));
    }

    @Override
    public int deletePages(String userId, String pageType) {
        List<Page> pages = getAllPagesForUserType(userId, pageType);
        int pageCount = pages.size();
        for(Page page : pages) {
            if(page.getOwnerId().equals(userId)){
                delete(page);
            }else{
                // remove any pageUser entries for this user on 
                // this page as it is a shared page
                PageUser pageUserToRemove = null;
                for(PageUser pageUser : page.getMembers()){
                    if(pageUser.getUserId().equals(userId)){
                        pageUserToRemove = pageUser;
                        break;
                    }
                }
                if(pageUserToRemove != null){
                    page.getMembers().remove(pageUserToRemove);
                    save(page);
                }
            }
        }
        return pageCount;
    }

    @Override
    public boolean hasPersonPage(String userId){
        TypedQuery<Long> query = manager.createNamedQuery(JpaPage.USER_HAS_PERSON_PAGE, Long.class);
        query.setParameter("userId", userId);
        query.setParameter("pageType", PageType.PERSON_PROFILE.toString().toUpperCase());
        return query.getSingleResult() > 0;
    }

    @Override
    public boolean hasPage(String contextId, String pageType) {
        TypedQuery<Long> query = manager.createNamedQuery(JpaPage.HAS_CONTEXT_PAGE, Long.class);
        query.setParameter("contextId", contextId);
        query.setParameter("pageType", pageType);
        return query.getSingleResult() > 0;
    }

    @Override
    public List<PageUser> getPagesForUser(String userId, String pageType) {
        TypedQuery<JpaPageUser> query = manager.createNamedQuery(JpaPageUser.GET_PAGES_FOR_USER, JpaPageUser.class);
        query.setParameter("userId", userId);
        query.setParameter("pageType", pageType.toUpperCase());
        return expandPageUserProperties(CollectionUtils.<PageUser>toBaseTypedList(query.getResultList()));
    }

    @Override
    public PageUser getSingleRecord(String userId, String pageId){
        TypedQuery<JpaPageUser> query = manager.createNamedQuery(JpaPageUser.GET_SINGLE_RECORD, JpaPageUser.class);
        query.setParameter("userId", userId);
        query.setParameter("pageId", pageId == null ? null : Long.parseLong(pageId));
        return expandPageUserProperties(query.getSingleResult());
    }

    @Override
    public Page createPageForUser(User user, PageTemplate pt) {
        return convert(pt, user);
    }

    @Override
    public List<Page> getAll() {
        TypedQuery<Page> query = manager.createNamedQuery(JpaPage.GET_ALL, Page.class);
        return expandProperties(CollectionUtils.<Page>toBaseTypedList(query.getResultList()));
    }

    @Override
    public List<Page> getLimitedList(int offset, int limit) {
        TypedQuery<Page> query = manager.createNamedQuery(JpaPage.GET_ALL, Page.class);
        return expandProperties(CollectionUtils.<Page>toBaseTypedList(getPagedResultList(query, offset, limit)));
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaPage.GET_COUNT);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    private void removePageUsers(JpaPage item) {
        for(PageUser user : item.getMembers()) {
            user.setPage(null);
            user.setUserId(null);
            manager.flush();
            manager.remove(user);
        }
    }

    private JpaPage expandProperties(JpaPage page) {
        if(page != null) {
            page.deserializeData();
        }
        return page;
    }

    private List<Page> expandProperties(List<Page> pages) {
        for(Page page : pages) {
            if(page instanceof JpaPage){
                expandProperties((JpaPage)page);
            }
        }
        return pages;
    }

    private JpaPageUser expandPageUserProperties(JpaPageUser pageUser) {
        Page page = pageUser.getPage();
        if(page != null && page instanceof JpaPage) {
            ((JpaPage)page).deserializeData();
        }
        return pageUser;
    }

    private List<PageUser> expandPageUserProperties(List<PageUser> pageUsers) {
        for(PageUser page : pageUsers) {
            if(page instanceof JpaPageUser) {
                expandPageUserProperties((JpaPageUser)page);
            }
        }
        return pageUsers;
    }

    /**
     * convert: PageTemplate, User -> Page
     * Converts the PageTemplate for Person Profiles into a Person Profile Page
     * @param pt PageTemplate
     * @param user User
     * @return Page
     */
    private Page convert(PageTemplate pt, User user) {
        Page p = new JpaPage();
        p.setName(pt.getName());
        p.setPageType(pt.getPageType());
        p.setOwnerId(user.getId());
        PageUser pageUser = new JpaPageUser(user, p, pt.getRenderSequence());
        pageUser.setPageStatus(PageInvitationStatus.OWNER);
        pageUser.setEditor(true);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(pageUser);
        p.setMembers(members);

        p.setPageLayout(pt.getPageLayout());
        p.setRegions(convertRegions(pt.getPageTemplateRegions(), p));
        //Workaround for an issue with OpenJPA where the transaction is applied in order of save methods and if
        //the parent page doesn't have an id yet, it will throw a referential integrity error
        p = save(p);
        p.setSubPages(convertPages(pt.getSubPageTemplates(), p));
        p = save(p);
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
            JpaRegion region = new JpaRegion();
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
    private List<RegionWidget> convertWidgets(List<PageTemplateWidget> pageTemplateWidgets, JpaRegion region){
        List<RegionWidget> widgets = new ArrayList<RegionWidget>();
        for (PageTemplateWidget ptw : pageTemplateWidgets){
            RegionWidget regionWidget = new JpaRegionWidget();
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
            Page lPage = new JpaPage();
            lPage.setName(pt.getName());
            lPage.setPageType(pt.getPageType());
            lPage.setOwnerId(page.getOwnerId());
            lPage.setPageLayout(pt.getPageLayout());
            lPage.setParentPage(page);
            lPage.setRegions(convertRegions(pt.getPageTemplateRegions(), lPage));

            // create new pageUser tuple
            PageUser pageUser = new JpaPageUser(lPage.getOwnerId(), lPage, pt.getRenderSequence());
            pageUser.setPageStatus(PageInvitationStatus.OWNER);
            pageUser.setEditor(true);
            List<PageUser> members = new ArrayList<PageUser>();
            members.add(pageUser);
            lPage.setMembers(members);
            // recursive call
            //Workaround for an issue with OpenJPA where the transaction is applied in order of save methods and if
            //the parent page doesn't have an id yet, it will throw a referential integrity error
            lPage = save(lPage);
            lPage.setSubPages((pt.getSubPageTemplates() == null || pt.getSubPageTemplates().isEmpty()) ? null : convertPages(pt.getSubPageTemplates(), lPage));
            lPage = save(lPage);
            pages.add(lPage);
        }
        return pages;
    }
}