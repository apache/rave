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
package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Page class.
 */
// TODO - remove ignore once Page is refactored into interface
@Ignore
public class PageTest {
	private Page page;
	private long id;
	private long parentId;
	private String testName;
	private User testOwner;
    private Page parentPage;
    private List<Page> subPages;
	private PageLayout pageLayout;
	private long renderSequence;
	private List<Region> regions;
	private PageUser pageUser;

    private final Long SUB_PAGE_1_ID = 666666L;
    private final Long SUB_PAGE_2_ID = 121212L;

	@Before
	public void setup(){
		page=new PageImpl();
		id=19191991L;
        parentId = 12345L;
		testName="testName";
		testOwner=new User(id);
        parentPage = new PageImpl(parentId);
        subPages = new ArrayList<Page>();

        Page subPage1 = new PageImpl();
        subPage1.setId(SUB_PAGE_1_ID);
        subPage1.setOwner(testOwner);
        Page subPage2 = new PageImpl();
        subPage2.setId(SUB_PAGE_2_ID);
        subPage2.setOwner(testOwner);

        List<PageUser> pageUsers1 = new ArrayList<PageUser>();
        PageUser pageUser1 = new PageUserImpl();
        pageUser1.setUser(testOwner);
        pageUser1.setPage(subPage1);
        pageUser1.setRenderSequence(2L);
        pageUsers1.add(pageUser1);
        subPage1.setMembers(pageUsers1);

        List<PageUser> pageUsers2 = new ArrayList<PageUser>();
        PageUser pageUser2 = new PageUserImpl();
        pageUser2.setUser(new User());
        pageUser2.setPage(subPage2);
        pageUser2.setRenderSequence(19L);
        pageUsers2.add(pageUser2);

        PageUser pageUser3 = new PageUserImpl();
        pageUser3.setUser(testOwner);
        pageUser3.setPage(subPage2);
        pageUser3.setRenderSequence(1L);
        pageUsers2.add(pageUser3);
        subPage2.setMembers(pageUsers2);

        subPages.add(subPage1);
        subPages.add(subPage2);
		pageLayout=new PageLayoutImpl();
		renderSequence=1223L;

		regions=new ArrayList<Region>();
		regions.add(new RegionImpl());
		regions.add(new RegionImpl());

		page.setId(id);
		page.setName(testName);
		page.setOwner(testOwner);
		page.setParentPage(parentPage);
		page.setSubPages(subPages);
		page.setPageLayout(pageLayout);
		page.setRegions(regions);

		pageUser = new PageUserImpl();
		pageUser.setPage(page);
		pageUser.setUser(testOwner);
		pageUser.setRenderSequence(renderSequence);
	}

	@After
	public void tearDown(){
		page=null;
	}

	@Test
	public void testAccessorMethods() {
		assertTrue(page.getId()==id);
		assertTrue(page.getName().equals(testName));
		assertTrue(page.getOwner().equals(testOwner));
		assertTrue(page.getParentPage().equals(parentPage));
        assertTrue(page.getSubPages().containsAll(subPages));
		assertTrue(page.getPageLayout().equals(pageLayout));
		assertTrue(pageUser.getRenderSequence()==renderSequence);
	}

	@Test
	public void testRegions(){
		assertTrue(page.getRegions().containsAll(regions));
	}

    @Test
	public void testSubPages(){
		assertTrue(page.getSubPages().containsAll(subPages));
	}

    @Test
    public void testSubPageComparator() {
        Long previousRenderSequence = -999L;
        List<Page> subPages = page.getSubPages();
        assertThat(subPages.get(0).getId(), is(SUB_PAGE_2_ID));
        assertThat(subPages.get(1).getId(), is(SUB_PAGE_1_ID));
    }
}
