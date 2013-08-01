/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.rest.model;

import java.util.List;

/**
 * Wrapper for search results.
 */
public class SearchResult<T> {
    private List<T> resultSet;
    private int pageSize;
    private int offset;
    private int totalResults;

    public SearchResult(List<T> resultset, int totalResults) {
        this.resultSet = resultset;
        this.totalResults = totalResults;
        this.pageSize = 0;
        this.offset = 0;
    }

    /**
     * @return a List of beans matching the original search query
     */
    public List<T> getResultSet() {
        return resultSet;
    }

    /**
     * The total number of items that match the query.
     * <p/>
     * Is useful for paging when the resultset is smaller than the total possible matches.
     *
     * @return total number of items that match the query
     */
    public int getTotalResults() {
        return totalResults;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNumberOfPages() {
        if (pageSize == 0) {
            return 0;
        }
        int numberOfPages = totalResults / pageSize;
        if (totalResults % pageSize > 0) {
            numberOfPages++;
        }
        return numberOfPages;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCurrentPage() {
        if (isFirstPage()) {
            return 1;
        }
        return (offset / pageSize) + 1;
    }

    private boolean isFirstPage() {
        return offset == 0 || pageSize == 0 || totalResults < pageSize;
    }
}
