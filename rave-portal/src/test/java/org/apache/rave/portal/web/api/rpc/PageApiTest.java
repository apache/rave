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

import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/** */
public class PageApiTest {

    private static final String PARAM_ERROR_MESSAGE = "Target Region does not exist";
    private static final String INTERNAL_ERROR_MESSAGE = "Internal Error";
    private PageApi pageApi;
    private PageService pageService;
    private static final long REGION_WIDGET_ID = 35;
    private static final int NEW_POSITION = 3;


    @Before
    public void setup() {
        pageService = createNiceMock(PageService.class);
        pageApi = new PageApi(pageService);
    }

    @Test
    public void moveWidget_validParams() {
        final long TO_REGION = 1;
        final long FROM_REGION = 2;

        expect(pageService.moveRegionWidget(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION)).andReturn(new RegionWidget());
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
    public void moveWidget_invalidParams() {
        final long TO_REGION = -1;
        final long FROM_REGION = 2;

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
    public void moveWidget_internalError() {
        final long TO_REGION = 1;
        final long FROM_REGION = 2;

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
        final int PAGE_ID = 1;
        final long WIDGET_ID = 2;

        expect(pageService.addWidgetToPage(PAGE_ID, WIDGET_ID)).andReturn(new RegionWidget());
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
        final int PAGE_ID = 1;
        final long WIDGET_ID = 2;

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
        final int PAGE_ID = 1;
        final long WIDGET_ID = 2;

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
        final long WIDGET_ID = 3;
        pageService.removeWidgetFromPage(WIDGET_ID);
        expectLastCall();
        RpcResult result = pageApi.removeWidgetFromPage(WIDGET_ID);
        verify(pageService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(false));

    }

   @Test
    public void deleteWidget_invalidParams() {
        final int PAGE_ID = 1;
        final long WIDGET_ID = 2;

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
    public void deleteWidget_internalError() {
        final int PAGE_ID = 1;
        final long WIDGET_ID = 2;

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
}
