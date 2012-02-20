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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Tests the Page class.
 */
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
	
	@Before
	public void setup(){
		page=new Page();
		id=19191991L;
        parentId = 12345L;
		testName="testName";
		testOwner=new User(id);
        parentPage = new Page(parentId);
        subPages = new ArrayList<Page>();
        subPages.add(new Page());
        subPages.add(new Page());
		pageLayout=new PageLayout();
		renderSequence=1223L;
		
		regions=new ArrayList<Region>();
		regions.add(new Region());
		regions.add(new Region());

		page.setEntityId(id);
		page.setName(testName);
		page.setOwner(testOwner);
		page.setParentPage(parentPage);
		page.setSubPages(subPages);
		page.setPageLayout(pageLayout);
		page.setRenderSequence(renderSequence);
		page.setRegions(regions);
	}
	
	@After
	public void tearDown(){
		page=null;
	}
	
	@Test
	public void testAccessorMethods() {
		assertTrue(page.getEntityId()==id);
		assertTrue(page.getName().equals(testName));
		assertTrue(page.getOwner().equals(testOwner));
		assertTrue(page.getParentPage().equals(parentPage));
		assertTrue(page.getSubPages().equals(subPages));
		assertTrue(page.getPageLayout().equals(pageLayout));
		assertTrue(page.getRenderSequence()==renderSequence);
	}

	@Test
	public void testRegions(){
		assertTrue(page.getRegions().containsAll(regions));
	}

    @Test
	public void testSubPages(){
		assertTrue(page.getSubPages().containsAll(subPages));
	}
}
