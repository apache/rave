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
package org.apache.rave.portal.model.impl;

import org.apache.rave.model.PageLayout;

public class PageLayoutImpl implements PageLayout {
    private String code;
    private Long numberOfRegions;
    private Long renderSequence;
    private boolean userSelectable;

    public PageLayoutImpl() {  }

    public PageLayoutImpl(String code) {
        this.code = code;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageLayoutImpl)) return false;

        PageLayoutImpl that = (PageLayoutImpl) o;

        if (userSelectable != that.userSelectable) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (numberOfRegions != null ? !numberOfRegions.equals(that.numberOfRegions) : that.numberOfRegions != null)
            return false;
        if (renderSequence != null ? !renderSequence.equals(that.renderSequence) : that.renderSequence != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (numberOfRegions != null ? numberOfRegions.hashCode() : 0);
        result = 31 * result + (renderSequence != null ? renderSequence.hashCode() : 0);
        result = 31 * result + (userSelectable ? 1 : 0);
        return result;
    }
}
