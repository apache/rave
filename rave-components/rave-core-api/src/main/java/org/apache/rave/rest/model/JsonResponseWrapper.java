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

package org.apache.rave.rest.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: erinnp
 * Date: 7/19/13
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonResponseWrapper {

    private HashMap<String, String> metadata;
    private Object data;

    public JsonResponseWrapper(SearchResult searchResult) {
        this.metadata = new HashMap<String, String>();
        this.data = searchResult.getResultSet();

        buildPaginationData(searchResult.getPageSize(), searchResult.getOffset(), searchResult.getTotalResults());
    }

    public JsonResponseWrapper(List list) {
        this.metadata = new HashMap<String, String>();
        this.data = list;

        buildPaginationData(0, 0, list.size());
    }

    //constructor for single resource objects
    public JsonResponseWrapper(Object data) {
        this.metadata = new HashMap<String, String>();
        this.data = data;
    }

    //constructor for list objects
    public JsonResponseWrapper(Object data, Integer limit, Integer offset, Integer count) {
        this.metadata = new HashMap<String, String>();
        this.data = data;

        buildPaginationData(limit, offset, count);
    }

    private void buildPaginationData(Integer limit, Integer offset, Integer count) {
        Integer prevOffset = null;
        Integer nextOffset = null;

        //if limit == 0, then we return full data set and there is no pagination
        if (limit > 0) {
            //build prev offset
            if (offset > 0) {
                prevOffset = offset - limit;
                if (prevOffset < 0) {
                    prevOffset = 0;
                }
            }
            //build next offset
            if (limit + offset < count) {
                nextOffset = limit + offset;
            }
        }

        if (prevOffset != null) {
            this.metadata.put("prev", "?limit=" + limit + "&offset=" + prevOffset);
        }
        if (nextOffset != null) {
            this.metadata.put("next", "?limit=" + limit + "&offset=" + nextOffset);
        }

        this.metadata.put("limit", limit.toString());
        this.metadata.put("offset", offset.toString());
        this.metadata.put("count", count.toString());
    }

    public HashMap<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(HashMap<String, String> metadata) {
        this.metadata = metadata;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
