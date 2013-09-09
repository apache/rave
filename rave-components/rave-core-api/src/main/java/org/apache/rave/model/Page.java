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
package org.apache.rave.model;

import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlTransient
public interface Page {

    String getId();
    void setId(String id);

    String getName();
    void setName(String name);

    /**
     * Represents the individual who is responsible for the page
     *
     * NOTE: This should be eventually deprecated in favor of context id
     * @return the {@link Person}'s ID who owns the page
     */
    String getOwnerId();
    void setOwnerId(String owner);

    /**
     * Represents the context identifier for the page.  This *MAY* be a Person ID depending on the context's needs
     * @return a string representing the logical context for which the page is to be rendered (portal user's ID, profile owner ID, etc)
     */
    String getContextId();
    void setContextId(String owner);

    PageLayout getPageLayout();
    void setPageLayout(PageLayout pageLayout);

    List<Region> getRegions();
    void setRegions(List<Region> regions);

    String getPageType();
    void setPageType(String pageType);

    Page getParentPage();
    void setParentPage(Page parentPage);

    List<Page> getSubPages();
    void setSubPages(List<Page> subPages);

    List<PageUser> getMembers();
    void setMembers(List<PageUser> members);
}
