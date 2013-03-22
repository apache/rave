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

package org.apache.rave.provider.w3c.repository;

import org.apache.rave.model.Widget;

public interface W3CWidgetMetadataRepository {
    /**
     * Fetches widget metadata for the widget via
     * either an internal widget key or by
     * using a w3cs widgets guid
     *
     * @param widgetGuid The widget to fetch metadata for.
     * @return The string response from the w3c widget server.
     */
    public Widget getWidgetMetadata(String widgetGuid);
    
    /**
     * Fetches widget metadata for all available widgets
     * @return an array of Widget objects representing available W3C widgets
     */
    public Widget[] getWidgetMetadata();
    
    /**
     * Publishes a remote widget to the target environment
     * @param url - url of remote widget
     * @return
     */
    Widget publishRemote(String url);
}
