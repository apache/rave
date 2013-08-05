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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Region", propOrder = {
        "locked", "regionWidgets"
})
@XmlRootElement(name = "Region")
public class Region  implements RestEntity{
    @XmlElement(name = "locked")
    private boolean  locked;

    private String id;
    @XmlElementWrapper(name = "regionWidgets")
    @XmlElement(name="RegionWidget")
    private List<RegionWidget> regionWidgets;

    public Region() { }

    public Region(org.apache.rave.model.Region source) {
        this.locked = source.isLocked();
        this.regionWidgets = createRegionWidgets(source);
        this.id = source.getId();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RegionWidget> getRegionWidgets() {
        return regionWidgets;
    }

    public void setRegionWidgets(List<RegionWidget> regionWidgets) {
        this.regionWidgets = regionWidgets;
    }

    private List<RegionWidget> createRegionWidgets(org.apache.rave.model.Region source) {
        List<RegionWidget> created = null;
        List<org.apache.rave.model.RegionWidget> widgets = source.getRegionWidgets();
        if(widgets != null) {
            created = new ArrayList<RegionWidget>();
            for(org.apache.rave.model.RegionWidget widget : widgets) {
                created.add(new RegionWidget(widget));
            }
        }
        return created;
    }
}
