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

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.service.RegionService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Provides RPC functions via the API.
 */
@Controller
@RequestMapping(value = "/api/rpc/page/*")
public class PageApi {

    private final RegionService regionService;

    @Autowired
    public PageApi(RegionService regionService) {
        this.regionService = regionService;
    }

    //Generic method for RegionWidget RPC ops
    @RequestMapping(method = RequestMethod.POST, value = "/regionWidget/{region_widget_id}")
    public RpcResult<RegionWidget> moveRegionWidget(@PathVariable final long region_widget_id,
                                                    @RequestParam final RpcOperation.Type operation,
                                                    @RequestParam final int new_position,
                                                    @RequestParam final long to_region,
                                                    @RequestParam final long from_region) {
        RpcResult<RegionWidget> result;
        switch (operation) {
            case MOVE: {
                result = new RpcOperation<RegionWidget>() {
                    @Override
                    public RegionWidget execute() {
                        return regionService.moveRegionWidget(region_widget_id, new_position, to_region, from_region);
                    }
                }.getResult();
                break;
            }
            case DELETE:
                result = new RpcOperation<RegionWidget>() {
                    @Override
                    public RegionWidget execute() {
                        throw new NotSupportedException("Not Supported");
                    }
                }.getResult();
                break;
            default: {
                result = new RpcResult<RegionWidget>(true, "Invalid Operation Specified: " + operation, RpcResult.ErrorCode.INVALID_PARAMS);
            }
        }

        return result;
    }
}
