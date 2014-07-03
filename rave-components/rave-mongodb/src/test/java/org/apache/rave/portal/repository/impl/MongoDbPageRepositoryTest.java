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
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoDbPageRepositoryTest {

    private MongoPageOperations template;
    private MongoDbPageRepository repo;


    @Before
    public void setUp(){
        template = createMock(MongoPageOperations.class);
        repo = new MongoDbPageRepository();
        repo.setTemplate(template);
    }

    @Test
    public void getAllPages(){
        String userId = "1234L";
        String userId1 = "9999L";
        PageType pageType = PageType.USER;
        PageUserImpl user = new PageUserImpl();
        user.setId("3333L");
        user.setUserId(userId1);
        PageUserImpl user2 = new PageUserImpl();
        user2.setId(userId);
        user2.setUserId(userId);
        List<Page> pages = Lists.newArrayList();
        List<PageUser> page_users = Lists.newArrayList();
        List<PageUser> page2_users = Lists.newArrayList();
        page_users.add(user);
        page2_users.add(user2);
        Page page = new PageImpl();
        page.setMembers(page_users);
        Page page2 = new PageImpl();
        page2.setMembers(page2_users);
        pages.add(page);
        pages.add(page2);

        expect(template.find(isA(Query.class))).andReturn(pages);
        replay(template);

        List<Page> result = repo.getAllPagesForUserType(userId, pageType.toString());
        assertNotNull(result);

    }
    @Test
    public void getByContext(){
        String contextId = "1234L";
        String userId1 = "9999L";
        String pageType = PageType.USER.toString();
        PageUserImpl user = new PageUserImpl();
        user.setId("3333L");
        user.setUserId(userId1);
        List<Page> pages = Lists.newArrayList();
        List<PageUser> page_users = Lists.newArrayList();
        page_users.add(user);
        Page page = new PageImpl();
        page.setMembers(page_users);
        pages.add(page);

        expect(template.find(isA(Query.class))).andReturn(pages);
        replay(template);

        List<Page> result = repo.getPagesForContextType(contextId, pageType);
        assertNotNull(result);
    }

    @Test
    public void getType(){
        Class<? extends Page> result = repo.getType();
        assertNotNull(result);
    }

    @Test
    public void createPageForUser(){
        Page result = new PageImpl();
        String userId = "2424L";
        User user = new UserImpl(userId);
        PageTemplate pt = new PageTemplateImpl();
        PageLayout layout = new PageLayoutImpl();
        List<PageTemplateRegion> regions = Lists.newArrayList();
        List<PageTemplateRegion> subRegions = Lists.newArrayList();
        List<PageTemplateWidget> widgets = Lists.newArrayList();
        List<PageTemplate> subPageTemplates = Lists.newArrayList();

        PageTemplate sub = new PageTemplateImpl();
        sub.setName("sub");
        PageTemplateRegion subRegion = new PageTemplateRegionImpl();
        subRegion.setRenderSequence(5555L);
        subRegion.setLocked(true);
        subRegion.setPageTemplateWidgets(widgets);
        subRegions.add(subRegion);
        sub.setPageTemplateRegions(subRegions);
        sub.setName("sub");
        sub.setPageType(PageType.SUB_PAGE.toString());
        sub.setPageLayout(layout);
        sub.setPageTemplateRegions(subRegions);
        sub.setRenderSequence(2000L);

        PageTemplateWidget widget = new PageTemplateWidgetImpl();
        Widget w = new WidgetImpl("1234");
        widget.setLocked(true);
        widget.setHideChrome(true);
        widget.setRenderSeq(2000L);
        widget.setWidgetId(w.getId());
        widgets.add(widget);

        PageTemplateRegion region = new PageTemplateRegionImpl();
        region.setRenderSequence(1111L);
        region.setPageTemplateWidgets(widgets);
        region.setLocked(true);
        regions.add(region);

        pt.setName("carol");
        pt.setPageType(PageType.USER.toString());
        pt.setPageLayout(layout);
        subPageTemplates.add(sub);
        pt.setSubPageTemplates(subPageTemplates);
        pt.setPageTemplateRegions(regions);
        pt.setRenderSequence(2000L);

        expect(template.save(isA(Page.class))).andReturn(result);
        replay(template);

        result = repo.createPageForUser(user, pt);
        assertNotNull(result);

    }

    @Test
    public void delete(){
        Page page = new PageImpl("1234L");

        template.remove(query(where("_id").is("1234L")));
        expectLastCall();
        replay(template);

        repo.delete(page);
        verify(template);
    }

    @Test
    public void deletePages(){
        int resultCount;
        String userID = "1111L";

        expect((int)template.count(isA(Query.class))).andReturn(1);
        template.remove(isA(Query.class));
        expectLastCall();
        replay(template);

        resultCount = repo.deletePages(userID, PageType.USER.toString().toString());
        assertThat(resultCount, is(equalTo(1)));
        verify(template);
    }

    @Test
    public void hasPersonPage_true(){
        String userId = "1234L";
        User user = new UserImpl(userId);
        Page page = new PageImpl();
        page.setPageType(PageType.PERSON_PROFILE.toString());
        page.setOwnerId(userId);

        expect(template.count(query(where("pageType").is("PERSON_PROFILE").andOperator(where("ownerId").is(userId))))).andReturn(1L);
        replay(template);

        boolean result = repo.hasPersonPage(userId);
        assertTrue(result);

    }

    @Test
    public void hasPersonPage_false(){
        String userId = "1234L";

        expect(template.count(query(where("pageType").is("PERSON_PROFILE").andOperator(where("ownerId").is(userId))))).andReturn(0L);
        replay(template);

        boolean result = repo.hasPersonPage(userId);
        assertFalse(result);

    }

    @Test
    public void getPagesForUser(){
        Page p = new PageImpl();
        PageUser user1 = new PageUserImpl("2222L");
        PageUser user3 = new PageUserImpl("2222L");
        User user2 = new UserImpl("2222L");
        user1.setUserId(user2.getId());
        List<PageUser> pageUser = Lists.newArrayList();
        pageUser.add(user1);
        pageUser.add(user3);
        p.setMembers(pageUser);

        Page p2 = new PageImpl();
        PageUser user4 = new PageUserImpl("2222L");
        PageUser user5 = new PageUserImpl("2222L");
        User user6 = new UserImpl("2222L");
        user4.setUserId(user6.getId());
        List<PageUser> pageUser2 = Lists.newArrayList();
        pageUser.add(user4);
        pageUser.add(user5);
        p2.setMembers(pageUser2);
        List<Page> pages = Lists.newArrayList(p);
        pages.add(p2);

        List<PageUser> result;
        String userId = "2222L";

        expect(template.find(query(where("members").elemMatch(where("userId").is(userId)).andOperator(where("pageType").is("USER"))))).andReturn(pages);
        replay(template);
        result = repo.getPagesForUser(userId, PageType.USER.toString().toString());

        assertThat(result.get(0).getUserId(), is(equalTo(user2.getId())));
        assertThat(result.size(), is(equalTo(2)));
        assertThat(result.get(0).getUserId(), is(equalTo("2222L")));

    }

    @Test
    public void getPagesForUser_false(){
        Page p = new PageImpl();
        PageUser user1 = new PageUserImpl("2222L");
        PageUser user3 = new PageUserImpl("2222L");
        user1.setRenderSequence(1L);
        user3.setRenderSequence(1L);
        User user2 = new UserImpl("2222L");
        user1.setUserId("2222L");
        List<PageUser> pageUser = Lists.newArrayList();
        pageUser.add(user1);
        pageUser.add(user3);
        p.setMembers(pageUser);

        Page p2 = new PageImpl();
        PageUser user4 = new PageUserImpl("2222L");
        PageUser user5 = new PageUserImpl("2222L");
        user4.setRenderSequence(1L);
        user5.setRenderSequence(1L);
        user4.setUserId("2222L");
        List<PageUser> pageUser2 = Lists.newArrayList();
        pageUser.add(user4);
        pageUser.add(user5);
        p2.setMembers(pageUser2);
        List<Page> pages = Lists.newArrayList(p);
        pages.add(p2);

        List<PageUser> result;
        String userId = "2222L";

        expect(template.find(query(where("members").elemMatch(where("userId").is(userId)).andOperator(where("pageType").is("USER"))))).andReturn(pages);
        replay(template);
        result = repo.getPagesForUser(userId, PageType.USER.toString().toString());

        assertThat(result.get(0).getUserId(), is(equalTo("2222L")));
        assertThat(result.size(), is(equalTo(2)));
        assertThat(result.get(0).getUserId(), is(equalTo("2222L")));

    }

    @Test
    public void getPagesForUser_null(){
        Page p = new PageImpl();
        PageUser user1 = new PageUserImpl("1111L");
        user1.setUserId("2222L");
        List<PageUser> pageUser = Lists.newArrayList();
        pageUser.add(user1);
        p.setMembers(pageUser);
        List<Page> pages = Lists.newArrayList(p);

        List<PageUser> result;
        String userId = "3333L";

        expect(template.find(query(where("members").elemMatch(where("userId").is(userId)).andOperator(where("pageType").is("USER"))))).andReturn(pages);
        replay(template);

        result = repo.getPagesForUser(userId, PageType.USER.toString().toString());
        assertThat(result.size(), is(equalTo(1)));

    }

    @Test
    public void getSingleRecord_valid(){
        String userId = "1111L";
        String pageId = "2222L";

        Page testPage = new PageImpl("2222L");
        PageUser pu = new PageUserImpl("3333L");
        pu.setUserId("1111L");
        List<PageUser> users = Lists.newArrayList();
        users.add(pu);
        testPage.setMembers(users);
        PageUser result;

        expect(template.get(pageId)).andReturn(testPage);
        replay(template);

        result = repo.getSingleRecord(userId, pageId);
        assertThat(result, is(sameInstance(pu)));
        assertThat(result.getId(), is(equalTo("3333L")));
        assertThat(result.getUserId(), is(equalTo("1111L")));

    }

    @Test
    public void getSingleRecord_null(){
        String userId = "1111L";
        String pageId = "2222L";

        Page testPage = new PageImpl(pageId);
        PageUser pu = new PageUserImpl("3333L");
        pu.setUserId("1234L");
        List<PageUser> users = Lists.newArrayList();
        users.add(pu);
        testPage.setMembers(users);
        PageUser result;

        expect(template.get(pageId)).andReturn(testPage);
        replay(template);

        result = repo.getSingleRecord(userId, pageId);
        assertNull(result);
    }

    @Test
    public void getAll(){
        List<Page> pages = new ArrayList<Page>();
        expect(template.find(isA(Query.class))).andReturn(pages);
        replay(template);

        assertThat(pages, is(sameInstance(repo.getAll())));
    }

    @Test
    public void getLimitedList(){
        int offset = 234;
        int pageSize = 123;
        List<Page> found = new ArrayList<Page>();
        expect(template.find(isA(Query.class))).andReturn(found);
        replay(template);
        assertThat(found, is(sameInstance(repo.getLimitedList(offset, pageSize))));
    }

    @Test
    public void getCountAll_Valid(){
        long doubleOseven = 007;
        expect(template.count(new Query())).andReturn(doubleOseven);
        replay(template);
        assertThat((int)doubleOseven, is(sameInstance(repo.getCountAll())));
    }

}
