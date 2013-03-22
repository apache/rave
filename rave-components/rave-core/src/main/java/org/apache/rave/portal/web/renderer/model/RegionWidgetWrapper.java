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

package org.apache.rave.portal.web.renderer.model;


import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.Widget;

public class RegionWidgetWrapper {
    private Widget widget;
    private RegionWidget regionWidget;

    public RegionWidgetWrapper() { }

    public RegionWidgetWrapper(Widget widget, RegionWidget regionWidget) {
        this.widget = widget;
        this.regionWidget = regionWidget;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public RegionWidget getRegionWidget() {
        return regionWidget;
    }

    public void setRegionWidget(RegionWidget regionWidget) {
        this.regionWidget = regionWidget;
    }
}
