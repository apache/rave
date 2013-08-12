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

public class PageLayout {
    private String code;
    private Long numberOfRegions;
    private Long renderSequence;
    private boolean userSelectable;

    public PageLayout() {}

    public PageLayout(org.apache.rave.model.PageLayout source) {
        this.code = source.getCode();
        this.numberOfRegions = source.getNumberOfRegions();
        this.renderSequence = source.getRenderSequence();
        this.userSelectable = source.isUserSelectable();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getNumberOfRegions() {
        return numberOfRegions;
    }

    public void setNumberOfRegions(Long numberOfRegions) {
        this.numberOfRegions = numberOfRegions;
    }

    public Long getRenderSequence() {
        return renderSequence;
    }

    public void setRenderSequence(Long renderSequence) {
        this.renderSequence = renderSequence;
    }

    public boolean isUserSelectable() {
        return userSelectable;
    }

    public void setUserSelectable(boolean userSelectable) {
        this.userSelectable = userSelectable;
    }
}
