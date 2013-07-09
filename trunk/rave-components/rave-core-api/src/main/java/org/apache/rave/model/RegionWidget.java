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

package org.apache.rave.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlTransient
public interface RegionWidget {
    String getId();

    String getWidgetId();

    void setWidgetId(String widgetId);

    @JsonBackReference
    Region getRegion();

    void setRegion(Region region);

    String getRenderPosition();

    void setRenderPosition(String renderPosition);

    int getRenderOrder();

    void setRenderOrder(int renderOrder);

    boolean isCollapsed();

    void setCollapsed(boolean collapsed);

    List<RegionWidgetPreference> getPreferences();

    void setPreferences(List<RegionWidgetPreference> preferences);

    boolean isLocked();

    void setLocked(boolean locked);

    boolean isHideChrome();

    void setHideChrome(boolean hideChrome);
}
