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

import java.util.List;

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.Widget;

public interface WidgetRepository extends Repository<Widget> {
    /**
     * Gets a list of all widgets in the repository
     *
     * @return a valid List
     */
    List<Widget> getAll();

    /**
     * @return the total number of {@link Widget}'s in the repository. Useful for paging.
     */
    int getCountAll();

    /**
     * Gets a List of {@link Widget}'s by performing a free text search
     *
     * @param searchTerm free text search term
     * @param offset     start point within the resultset (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return valid list of widgets, can be empty
     */
    List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize);

    /**
     * Counts the total number of {@link Widget}'s that match the search term. Useful for paging.
     *
     * @param searchTerm free text search term
     * @return total number of {@link Widget}'s that match the search term
     */
    int getCountFreeTextSearch(String searchTerm);

}