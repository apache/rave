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

import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Defines RPC operations for a Page or its components
 */
@Controller
@RequestMapping(value = "/api/rpc/page/*")
public class PageApi {

	private final PageService pageService;

	@Autowired
	public PageApi(PageService pageService) {
		this.pageService = pageService;
	}

	/**
	 * Adds a widget to the given page
	 * 
	 * @param pageId
	 *            the ID of the {@link org.apache.rave.portal.model.Page} to add
	 *            the widget to
	 * @param widgetId
	 *            the ID of the {@link org.apache.rave.portal.model.Widget} to
	 *            add do the page
	 * @return a {@link RpcOperation} containing the new widget instance (
	 *         {@link org.apache.rave.portal.model.RegionWidget }) or any errors
	 *         encountered while performing the RPC operation
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "{pageId}/widget/add")
	public RpcResult<RegionWidget> addWidgetToPage(
			@PathVariable final long pageId, @RequestParam final long widgetId) {

		return new RpcOperation<RegionWidget>() {
			@Override
			public RegionWidget execute() {
				return pageService.addWidgetToPage(pageId, widgetId);
			}
		}.getResult();
	}

	/**
	 * Moves a widget to a new location
	 * <p/>
	 * Moves can take place within a region, region to region, or between pages
	 * 
	 * @param regionWidgetId
	 *            the ID of the
	 *            {@link org.apache.rave.portal.model.RegionWidget} to move
	 * @param newPosition
	 *            the 0 based index within the region where the RegionWidget
	 *            will now be located
	 * @param toRegion
	 *            the Id of the {@link org.apache.rave.portal.model.Region }
	 *            where the widget will now be located
	 * @param fromRegion
	 *            the Id of the {@link org.apache.rave.portal.model.Region }
	 *            where the widget is currently located
	 * @return a {@link RpcOperation} containing the updated widget instance (
	 *         {@link org.apache.rave.portal.model.RegionWidget }) or any errors
	 *         encountered while performing the RPC operation
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "regionWidget/{regionWidgetId}/move")
	public RpcResult<RegionWidget> moveWidgetOnPage(
			@PathVariable final long regionWidgetId,
			@RequestParam final int newPosition,
			@RequestParam final long toRegion,
			@RequestParam final long fromRegion) {

		return new RpcOperation<RegionWidget>() {
			@Override
			public RegionWidget execute() {
				return pageService.moveRegionWidget(regionWidgetId,
						newPosition, toRegion, fromRegion);
			}
		}.getResult();
	}

	/**
	 * Deletes a widget
	 * 
	 * @param pageId
	 *            the ID of the {@link org.apache.rave.portal.model.Page}
	 *            containing the widget region
	 * @param regionId
	 *            the ID of the {@link org.apache.rave.portal.model.Region}
	 *            containing the widget
	 * @param widgetId
	 *            the ID of the {@link org.apache.rave.portal.model.Widget} to
	 *            delete
	 * @return an {@link RpcOperation} containing the updated region or any
	 *         errors encountered.
	 */
	@ResponseBody
    @RequestMapping(method=RequestMethod.POST, value="{pageId}/widget/delete")	
    public RpcResult<Region> removeWidgetFromPage(@RequestParam final long widgetId, 
																  @RequestParam final long regionId) {
		return new RpcOperation<Region>() {
			@Override
			public Region execute() {
				 return pageService.removeWidgetFromPage(widgetId, regionId);
			}
		}.getResult();
    }

}
