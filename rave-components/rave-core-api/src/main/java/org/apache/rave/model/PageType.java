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

import java.util.HashMap;
import java.util.Map;

/**
 * The type of a Page object
 *
 * Deprecated in favor of generic context mapping & top-level page checks.
 */
@Deprecated
public enum PageType {
    USER("user"),
    PERSON_PROFILE("person_profile"),
    SUB_PAGE("sub_page");

    private String pageType;
    private static final Map<String, PageType> lookup = new HashMap<String, PageType>();

    static {
        for (PageType pt : PageType.values()) {
            lookup.put(pt.toString(), pt);
        }
    }

    private PageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPageType() {
        return pageType;
    }

    @Override
    public String toString() {
        return pageType;
    }

    public static PageType get(String pageType) {
        return lookup.get(pageType);
    }
}