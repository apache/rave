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
import org.apache.rave.portal.service.RegionService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
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
    private static final RpcOperation.Type MOVE = RpcOperation.Type.MOVE;
    private PageApi pageApi;
    private RegionService regionService;
    private static final long REGION_WIDGET_ID = 35;
    private static final int NEW_POSITION = 3;


    @Before
    public void setup() {
        regionService = createNiceMock(RegionService.class);
        pageApi = new PageApi(regionService);
    }

    @Test
    public void moveWidget_validParams() {
        final RpcOperation.Type OPERATION  = MOVE;
        final int TO_REGION = 1;
        final long FROM_REGION = 2;

        expect(regionService.moveRegionWidget(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION)).andReturn(new RegionWidget());
        replay(regionService);
        RpcResult<RegionWidget> result = pageApi.moveRegionWidget(REGION_WIDGET_ID, OPERATION, NEW_POSITION, TO_REGION, FROM_REGION);
        verify(regionService);
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

        expect(regionService.moveRegionWidget(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION)).andThrow(new IllegalArgumentException(PARAM_ERROR_MESSAGE));
        replay(regionService);

        RpcResult<RegionWidget> result = pageApi.moveRegionWidget(REGION_WIDGET_ID, MOVE, NEW_POSITION, TO_REGION, FROM_REGION);
        verify(regionService);
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

        expect(regionService.moveRegionWidget(REGION_WIDGET_ID, NEW_POSITION, TO_REGION, FROM_REGION)).andThrow(new RuntimeException(INTERNAL_ERROR_MESSAGE));
        replay(regionService);

        RpcResult<RegionWidget> result = pageApi.moveRegionWidget(REGION_WIDGET_ID, MOVE, NEW_POSITION, TO_REGION, FROM_REGION);
        verify(regionService);
        assertThat(result, is(notNullValue()));
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.isError(), is(true));
        assertThat(result.getErrorCode(), is(RpcResult.ErrorCode.INTERNAL_ERROR));
        assertThat(result.getErrorMessage(), is(equalTo(INTERNAL_ERROR_MESSAGE)));
    }
}
