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

import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageTemplateRegion;
import org.apache.rave.model.PageTemplateWidget;

import java.util.List;

public class PageTemplateRegionImpl implements PageTemplateRegion {
    private String id;
    private long renderSequence;
    private PageTemplate pageTemplate;
    private List<PageTemplateWidget> pageTemplateWidgets;
    private boolean locked;

    public PageTemplateRegionImpl() {

    }

    public PageTemplateRegionImpl(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getRenderSequence() {
        return renderSequence;
    }

    public void setRenderSequence(long renderSequence) {
        this.renderSequence = renderSequence;
    }

    public PageTemplate getPageTemplate() {
        return pageTemplate;
    }

    public void setPageTemplate(PageTemplate pageTemplate) {
        this.pageTemplate = pageTemplate;
    }

    public List<PageTemplateWidget> getPageTemplateWidgets() {
        return pageTemplateWidgets;
    }

    public void setPageTemplateWidgets(List<PageTemplateWidget> pageTemplateWidgets) {
        this.pageTemplateWidgets = pageTemplateWidgets;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageTemplateRegionImpl)) return false;

        PageTemplateRegionImpl that = (PageTemplateRegionImpl) o;

        if (locked != that.locked) return false;
        if (renderSequence != that.renderSequence) return false;
        if (pageTemplate != null ? !pageTemplate.equals(that.pageTemplate) : that.pageTemplate != null) return false;
        if (pageTemplateWidgets != null ? !pageTemplateWidgets.equals(that.pageTemplateWidgets) : that.pageTemplateWidgets != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (renderSequence ^ (renderSequence >>> 32));
        result = 31 * result + (pageTemplate != null ? pageTemplate.hashCode() : 0);
        result = 31 * result + (pageTemplateWidgets != null ? pageTemplateWidgets.hashCode() : 0);
        result = 31 * result + (locked ? 1 : 0);
        return result;
    }
}
