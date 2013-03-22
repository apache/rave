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

package org.apache.rave.portal.web.api.rpc;

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.service.OmdlService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/** */
public class PageApiTest {

    private final String PARAM_ERROR_MESSAGE = "Target Region does not exist";
    private final String INTERNAL_ERROR_MESSAGE = "Internal Error";
    private PageApi pageApi;
    private PageService pageService;
    private OmdlService omdlService;
    private final String REGION_WIDGET_ID = "35";
    private final int NEW_POSITION = 3;
    private final String PAGE_ID = "20";
    private final String PAGE_2_ID = "30";

    private Page page, page2;


    @Before
    public void setup() {
        pageService = createMock(PageService.class);
        omdlService = createMock(OmdlService.class);
        pageApi = new PageApi(pageService, omdlService);

        page = new PageImpl(PAGE_ID);
        page2 = new PageImpl(PAGE_2_ID);
    }

    @Test
    public void moveWidgetOnPage_validParams() {
        final String TO_REGION = "1";
        final String FROM_REGION = "2";

        expect(pageService.moveRegionWidget(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION)).andReturn(new RegionWidgetImpl());
        replay(pageService);
        RpcResult<RegionWidget> result = pageApi.moveWidgetOnPage(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(notNullValue()));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void moveWidgetOnPage_invalidParams() {
        final String TO_REGION = "-1";
        final String FROM_REGION = "2";

        expect(pageService.moveRegionWidget(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION)).andThrow(new IllegalArgumentException(PARAM_ERROR_MESSAGE));
        replay(pageService);

        RpcResult<RegionWidget> result = pageApi.moveWidgetOnPage(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INVALID_PARAMS));
        assertThat(result.getErrorMessage(), is(equalTo(PARAM_ERROR_MESSAGE)));
    }

    @Test
    public void moveWidgetOnPage_internalError() {
        final String TO_REGION = "1";
        final String FROM_REGION = "2";

        expect(pageService.moveRegionWidget(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION)).andThrow(new RuntimeException(INTERNAL_ERROR_MESSAGE));
        replay(pageService);

        RpcResult<RegionWidget> result = pageApi.moveWidgetOnPage(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INTERNAL_ERROR));
        assertThat(result.getErrorMessage(), is(equalTo(INTERNAL_ERROR_MESSAGE)));
    }

    @Test
    public void addWidget_validParams() {
        final String PAGE_ID = "1";
        final String WIDGET_ID = "2";

        expect(pageService.addWidgetToPage(PAGE_ID, WIDGET_ID)).andReturn(new RegionWidgetImpl());
        replay(pageService);
        RpcResult result = pageApi.addWidgetToPage(PAGE_ID, WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(notNullValue()));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void addWidget_invalidParams() {
        final String PAGE_ID = "1";
        final String WIDGET_ID = "2";

        expect(pageService.addWidgetToPage(PAGE_ID, WIDGET_ID)).andThrow(new IllegalArgumentException(PARAM_ERROR_MESSAGE));
        replay(pageService);
        RpcResult result = pageApi.addWidgetToPage(PAGE_ID, WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INVALID_PARAMS));
        assertThat(result.getErrorMessage(), is(equalTo(PARAM_ERROR_MESSAGE)));
    }

    @Test
    public void addWidget_internalError() {
        final String PAGE_ID = "1";
        final String WIDGET_ID = "2";

        expect(pageService.addWidgetToPage(PAGE_ID, WIDGET_ID)).andThrow(new RuntimeException(INTERNAL_ERROR_MESSAGE));
        replay(pageService);
        RpcResult result = pageApi.addWidgetToPage(PAGE_ID, WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INTERNAL_ERROR));
        assertThat(result.getErrorMessage(), is(equalTo(INTERNAL_ERROR_MESSAGE)));
    }

    @Test
    public void deleteWidget_validParams() {
        final String WIDGET_ID = "3";
        expect(pageService.removeWidgetFromPage(WIDGET_ID)).andReturn(new RegionImpl());
        replay(pageService);

        RpcResult<Region> result = pageApi.removeWidgetFromPage(WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(notNullValue()));
        assertThat(result.isError(), is(false));

    }

   @Test
    public void deleteWidget_invalidParams() {
        final String WIDGET_ID = "2";

        expect(pageService.removeWidgetFromPage(WIDGET_ID)).andThrow(new IllegalArgumentException(PARAM_ERROR_MESSAGE));
        replay(pageService);
        RpcResult result = pageApi.removeWidgetFromPage(WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INVALID_PARAMS));
        assertThat(result.getErrorMessage(), is(equalTo(PARAM_ERROR_MESSAGE)));
    }

    @Test
    public void deleteWidget_internalError() {
        final String WIDGET_ID = "2";

        expect(pageService.removeWidgetFromPage(WIDGET_ID)).andThrow(new RuntimeException(INTERNAL_ERROR_MESSAGE));
        replay(pageService);
        RpcResult result = pageApi.removeWidgetFromPage(WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INTERNAL_ERROR));
        assertThat(result.getErrorMessage(), is(equalTo(INTERNAL_ERROR_MESSAGE)));
    }

    @Test
    public void addPage_validParams() {
        final String PAGE_NAME = "My New Page";
        final String PAGE_LAYOUT_CODE = "layout1";

        expect(pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE)).andReturn(new PageImpl());
        replay(pageService);
        RpcResult result = pageApi.addPage(PAGE_NAME, PAGE_LAYOUT_CODE);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(notNullValue()));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void addPage_invalidParams() {
        final String PAGE_NAME = "My New Page";
        final String PAGE_LAYOUT_CODE = "layout1";

        expect(pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE)).andThrow(new IllegalArgumentException(PARAM_ERROR_MESSAGE));
        replay(pageService);
        RpcResult result = pageApi.addPage(PAGE_NAME, PAGE_LAYOUT_CODE);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INVALID_PARAMS));
        assertThat(result.getErrorMessage(), is(equalTo(PARAM_ERROR_MESSAGE)));
    }

    @Test
    public void addPage_internalError() {
        final String PAGE_NAME = "My New Page";
        final String PAGE_LAYOUT_CODE = "layout1";

        expect(pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE)).andThrow(new RuntimeException(INTERNAL_ERROR_MESSAGE));
        replay(pageService);
        RpcResult result = pageApi.addPage(PAGE_NAME, PAGE_LAYOUT_CODE);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INTERNAL_ERROR));
        assertThat(result.getErrorMessage(), is(equalTo(INTERNAL_ERROR_MESSAGE)));
    }

    @Test
    public void getPage() {
        expect(pageService.getPage(PAGE_ID)).andReturn(page);
        replay(pageService);

        RpcResult result = pageApi.getPage(PAGE_ID);

        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat((Page)result.getResult(), is (page));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is (RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }
    @Test
    public void updatePage() {
        String pageName = "name";
        String layoutName = "layout";
        expect(pageService.updatePage(PAGE_ID, pageName, layoutName)).andReturn(page);
        replay(pageService);

        RpcResult result = pageApi.updatePageProperties(PAGE_ID, pageName, layoutName);

        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat((Page)result.getResult(), is (page));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is (RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void movePage_nonNullMoveAfterPageId() {
        expect(pageService.movePage(PAGE_ID, PAGE_2_ID)).andReturn(page);
        replay(pageService);

        RpcResult result = pageApi.movePage(PAGE_ID, PAGE_2_ID);

        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat((Page)result.getResult(), is(page));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void movePage_nullMoveAfterPageId() {
        expect(pageService.movePageToDefault(PAGE_2_ID)).andReturn(page2);
        replay(pageService);

        RpcResult result = pageApi.movePage(PAGE_2_ID, null);

        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat((Page)result.getResult(), is(page2));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void moveWidgetToPage_validParams() {
        expect(pageService.moveRegionWidgetToPage(REGION_WIDGET_ID, PAGE_2_ID)).andReturn(new RegionWidgetImpl());
        replay(pageService);
        RpcResult<RegionWidget> result = pageApi.moveWidgetToPage(PAGE_2_ID, REGION_WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(notNullValue()));
        assertThat(result.isError(), is(false));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.NO_ERROR));
        assertThat(result.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void moveWidgetToPage_invalidParams() {
        expect(pageService.moveRegionWidgetToPage(REGION_WIDGET_ID, PAGE_2_ID)).andThrow(new IllegalArgumentException(PARAM_ERROR_MESSAGE));
        replay(pageService);

        RpcResult<RegionWidget> result = pageApi.moveWidgetToPage(PAGE_2_ID, REGION_WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INVALID_PARAMS));
        assertThat(result.getErrorMessage(), is(equalTo(PARAM_ERROR_MESSAGE)));
    }

    @Test
    public void moveWidgetToPage_internalError() {
        expect(pageService.moveRegionWidgetToPage(REGION_WIDGET_ID, PAGE_2_ID)).andThrow(new RuntimeException(INTERNAL_ERROR_MESSAGE));
        replay(pageService);

        RpcResult<RegionWidget> result = pageApi.moveWidgetToPage(PAGE_2_ID, REGION_WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INTERNAL_ERROR));
        assertThat(result.getErrorMessage(), is(equalTo(INTERNAL_ERROR_MESSAGE)));
    }
}
