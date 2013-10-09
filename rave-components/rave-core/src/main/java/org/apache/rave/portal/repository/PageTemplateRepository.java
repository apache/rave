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
package org.apache.rave.portal.repository;

import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageType;
import org.apache.rave.repository.Repository;

import java.util.List;

/**
 * Provides operations against the backing store for PageTemplate objects
 */
public interface PageTemplateRepository extends Repository<PageTemplate> {

    /**
     * Gets all page templates with the specified page type
     * @param pageType the page type used to filter templates
     * @return a list of all page templates for the type in the repository
     */
    List<PageTemplate> getAll(String pageType);

    /**
     * Gets the default page template for the given type
     * @param pageType teh page type
     * @return a valid page template or null if none exists for the type
     */
    PageTemplate getDefaultPage(String pageType);

}
