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
import java.util.List;

@XmlTransient
public interface PortalPreference {
    /**
     * Gets the key of the preference, e.g. "availableFruit"
     *
     * @return name of the preference key
     */
    String getKey();

    void setKey(String key);

    /**
     * Gets a String array of the preference values, e.g. {"apple", "pear", "orange"}
     *
     * @return String array of the preference values
     */
    List<String> getValues();

    void setValues(List<String> values);

    /**
     * Helper method for the view layer to get a single value for a preference.
     * If there is no value, it returns {@literal null}.
     * If there is 1 value, it returns that value.
     *
     * @return the single value of the preference or {@literal null} if not set
     * @throws org.apache.rave.exception.NotSupportedException if the preference has multiple values
     */
    String getValue();

    /**
     * Sets a single value for a preference. Will overwrite any exisiting value(s)
     *
     * @param value String value of the preference
     */
    void setValue(String value);
}
