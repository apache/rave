/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.model.util;

public class WidgetStatistics {
    private int totalLike;
    private int totalDislike;
    private int userRating;
    private int totalUserCount;
    
    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }
    public int getTotalLike() {
        return totalLike;
    }
    
    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }
    public int getTotalDislike() {
        return totalDislike;
    }
    
    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }
    public int getUserRating() {
        return userRating;
    }

    public int getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(int totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    @Override
    public String toString() {
        return "WidgetStatistics{" +
                "totalLike=" + totalLike +
                ", totalDislike=" + totalDislike + 
                ", userRating=" + userRating +
                ", totalUserCount=" + totalUserCount +
                '}';
    }
}
