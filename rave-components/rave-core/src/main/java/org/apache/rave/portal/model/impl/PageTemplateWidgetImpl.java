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

import org.apache.rave.model.PageTemplateRegion;
import org.apache.rave.model.PageTemplateWidget;

public class PageTemplateWidgetImpl implements PageTemplateWidget {
    private String id;
    private PageTemplateRegion pageTemplateRegion;
    private long renderSequence;
    private String widgetId;
    private boolean locked;
    private boolean hideChrome;

    public PageTemplateWidgetImpl() {

    }

    public PageTemplateWidgetImpl(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PageTemplateRegion getPageTemplateRegion() {
        return pageTemplateRegion;
    }

    public void setPageTemplateRegion(PageTemplateRegion pageTemplateRegion) {
        this.pageTemplateRegion = pageTemplateRegion;
    }

    public long getRenderSeq() {
        return renderSequence;
    }

    public void setRenderSeq(long renderSequence) {
        this.renderSequence = renderSequence;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isHideChrome() {
        return hideChrome;
    }

    public void setHideChrome(boolean hideChrome) {
        this.hideChrome = hideChrome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageTemplateWidgetImpl)) return false;

        PageTemplateWidgetImpl that = (PageTemplateWidgetImpl) o;

        if (hideChrome != that.hideChrome) return false;
        if (locked != that.locked) return false;
        if (renderSequence != that.renderSequence) return false;
        if (pageTemplateRegion != null ? !pageTemplateRegion.equals(that.pageTemplateRegion) : that.pageTemplateRegion != null)
            return false;
        if (widgetId != null ? !widgetId.equals(that.widgetId) : that.widgetId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageTemplateRegion != null ? pageTemplateRegion.hashCode() : 0;
        result = 31 * result + (int) (renderSequence ^ (renderSequence >>> 32));
        result = 31 * result + (widgetId != null ? widgetId.hashCode() : 0);
        result = 31 * result + (locked ? 1 : 0);
        result = 31 * result + (hideChrome ? 1 : 0);
        return result;
    }
}
