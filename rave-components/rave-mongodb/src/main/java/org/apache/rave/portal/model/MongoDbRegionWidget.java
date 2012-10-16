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


import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.WidgetRepository;

public class MongoDbRegionWidget extends RegionWidgetImpl {
    private long widgetId;
    private WidgetRepository widgetRepository;

    public long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(long widgetId) {
        this.widgetId = widgetId;
    }

    public WidgetRepository getWidgetRepository() {
        return widgetRepository;
    }

    public void setWidgetRepository(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Widget getWidget() {
        Widget widget = super.getWidget();
        if(widget == null) {
            widget = widgetRepository.get(widgetId);
            super.setWidget(widget);
        }
        return widget;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionWidget)) return false;
        RegionWidget that = (RegionWidget) o;
        return !(this.getId() != null ? !this.getId().equals(that.getId()) : that.getId() != null);

    }

    @Override
    public int hashCode() {
        return this.getId() != null ? this.getId().hashCode() : 0;
    }


}
