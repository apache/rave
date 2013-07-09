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
public interface PageLayout {
    /**
     * Gets the code used by the rendering engine to identify the page layout
     *
     * @return Valid code known by rendering engine
     */
    String getCode();

    void setCode(String code);

    /**
     * Gets the number of regions supported by this page layout
     *
     * @return Valid number of regions > 0
     */
    Long getNumberOfRegions();

    void setNumberOfRegions(Long numberOfRegions);

    Long getRenderSequence();

    void setRenderSequence(Long renderSequence);

    boolean isUserSelectable();

    void setUserSelectable(boolean userSelectable);
}
