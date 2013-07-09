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

package org.apache.rave.portal.web.model;

import org.apache.rave.model.RegionWidgetPreference;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A simple wrapper class so that JAXB can properly marshall and unmarshall a list of RegionWidgetPreference.
 */
@XmlRootElement(name = "regionWidgetPreferences")
public class RegionWidgetPreferenceListWrapper {
    private List<RegionWidgetPreference> preferences;

    public RegionWidgetPreferenceListWrapper() {
    }

    public RegionWidgetPreferenceListWrapper(List<RegionWidgetPreference> preferences) {
        this.preferences = preferences;
    }

    @XmlElement(name = "regionWidgetPreference")
    public List<RegionWidgetPreference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<RegionWidgetPreference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public String toString() {
        return "RegionWidgetPreferenceListWrapper{" +
                "preferences=" + preferences +
                '}';
    }
}