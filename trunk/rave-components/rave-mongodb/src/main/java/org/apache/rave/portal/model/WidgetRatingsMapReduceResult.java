/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model;

import java.util.Map;

/**
*/
public class WidgetRatingsMapReduceResult {
    private String id;
    private WidgetStatisticsMapReduceResult value;

    public WidgetRatingsMapReduceResult() {  }

    public WidgetRatingsMapReduceResult(String id, WidgetStatisticsMapReduceResult value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WidgetStatisticsMapReduceResult getValue() {
        return value;
    }

    public void setValue(WidgetStatisticsMapReduceResult value) {
        this.value = value;
    }

    public static class WidgetStatisticsMapReduceResult {
        private Map<String, Long> userRatings;
        private Long like;
        private Long dislike;

        public WidgetStatisticsMapReduceResult() { }

        public WidgetStatisticsMapReduceResult(Map<String, Long> userRatings, Long like, Long dislike) {
            this.userRatings = userRatings;
            this.like = like;
            this.dislike = dislike;
        }

        public Map<String, Long> getUserRatings() {
            return userRatings;
        }

        public void setUserRatings(Map<String, Long> userRatings) {
            this.userRatings = userRatings;
        }

        public Long getLike() {
            return like;
        }

        public void setLike(Long like) {
            this.like = like;
        }

        public Long getDislike() {
            return dislike;
        }

        public void setDislike(Long dislike) {
            this.dislike = dislike;
        }
    }

}
