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
package org.apache.rave.portal.service;

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Region;

import java.util.List;

public interface PageService {
    /**
     * Gets all pages for the given user.
     *
     * @param userId The user to retrieve pages for.
     * @return A non null possible empty list of pages for the given user.
     */
    List<Page> getAllPages(long userId);

    /**
     * Moves a Region widget's position in a region or across regions
     *
     * @param regionWidgetId the id of the moved RegionWidget
     * @param newPosition the new index of the RegionWidget within the target region (0 based index)
     * @param toRegion the id of the Region to move the RegionWidget to
     * @param fromRegion the id of the Region where the RegionWidget currently resides
     * @return the updated RegionWidget
     */
    RegionWidget moveRegionWidget(long regionWidgetId, int newPosition, long toRegion, long fromRegion);

    /**
     * Creates a new instance of a widget and adds it to the first position of the first region on the page
     * @param page_id the id of the page to add the widget to
     * @param widget_id the {@link org.apache.rave.portal.model.Widget} id to add
     * @return a valid widget instance
     */
    RegionWidget addWidgetToPage(long page_id, long widget_id);

	 /**
	  * Deletes the specified widget from the page.
	  *
      * @param regionWidgetId the id of the region widget to delete.\
      * @return the region from which the widget was deleted
      */
	 Region removeWidgetFromPage(long regionWidgetId);

	 /**
	  * Registers a new page.
	  * @param page the page object to register with the data management system.
	  */
	 void registerNewPage(Page page);
}