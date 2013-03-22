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

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public interface RegionWidgetPreference {
    /**
     * Gets the ID of the RegionWidget this preference is for
     * @return The ID of the RegionWidget this preference is for
     */
    String getRegionWidgetId();

    void setRegionWidgetId(String regionWidgetId);

    /**
     * Gets the name of the preference
     *
     * @return The name of the preference
     */
    String getName();

    void setName(String name);

    /**
     * Gets the value of this preference
     *
     * @return The value of this preference
     */
    String getValue();

    void setValue(String value);
}
