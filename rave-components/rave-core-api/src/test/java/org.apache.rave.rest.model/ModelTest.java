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

package org.apache.rave.rest.model;

import org.junit.Test;

public class ModelTest {

    public static final String USER_ID = "USER";
    public static final String PAGE_ID = "ID";
    public static final String PAGE_NAME = "NAME";
    public static final String LAYOUT_CODE = "LAYOUT";

    @Test
    public void testConversion() {
      /*  org.apache.rave.model.Page toConvert = new PageImpl(PAGE_ID, USER_ID);
        toConvert.setName(PAGE_NAME);
        toConvert.setPageLayout(new PageLayoutImpl(LAYOUT_CODE));
        toConvert.setPageType(PageType.PERSON_PROFILE);
        toConvert.setRegions(Arrays.<org.apache.rave.model.Region>asList(new RegionImpl(PAGE_ID)));
        toConvert.setMembers(Arrays.<org.apache.rave.model.PageUser>asList(new PageUserImpl(USER_ID)));
        toConvert.setSubPages(Arrays.<org.apache.rave.model.Page>asList(new PageImpl(PAGE_NAME)));

        Page page = new Page(toConvert);
        assertThat(page.getId(), equalTo(toConvert.getId()));
        assertThat(page.getName(), equalTo(toConvert.getName()));
        assertThat(page.getOwnerId(), equalTo(toConvert.getOwnerId()));
        assertThat(page.getPageLayoutCode(), equalTo(LAYOUT_CODE));
        assertThat(page.getPageType(), equalTo(PageType.PERSON_PROFILE.toString()));
        assertThat(page.getMembers().size(), equalTo(1));
        assertThat(page.getRegions().size(), equalTo(1));
        assertThat(page.getSubPages().size(), equalTo(1));
        assertThat(page.getSubPages().get(0).getId(), equalTo(PAGE_NAME));*/
    }

}
