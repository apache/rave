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
import java.util.Map;

/**
 */
@XmlTransient
public interface PageTemplateRegion {

    String getId();

    long getRenderSequence();

    void setRenderSequence(long renderSequence);

    PageTemplate getPageTemplate();

    void setPageTemplate(PageTemplate pageTemplate);

    List<PageTemplateWidget> getPageTemplateWidgets();

    void setPageTemplateWidgets(List<PageTemplateWidget> pageTemplateWidgets);

    boolean isLocked();

    void setLocked(boolean locked);

    /**
     * Generic property bag for extension of the region object.
     *
     * Rave makes no attempt to understand the shape of properties in the bag.
     *
     * @return a valid Map of String to JSON Serializable Object.
     */
    Map<String, Object> getProperties();

    /**
     * Overrides the current properties with a new set.
     *
     * @param properties a non-null map of string to JSON serializable objects
     */
    void setProperties(Map<String, Object> properties);
}
