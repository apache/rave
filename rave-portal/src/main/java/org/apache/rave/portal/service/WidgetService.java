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

package org.apache.rave.portal.service;

import org.apache.rave.portal.model.Widget;

import java.util.List;

/**
 * Provides widget operations
 */
public interface WidgetService {

    /**
     * Gets a list of widgets that a user can add to their context
     * @return valid list of widgets
     */
    List<Widget> getAllWidgets();

    /**
     * Gets the detailed metadata for a widget
     * @param id the Id of the widget
     * @return a valid widget if one exists for the given id; null otherwise
     */
    Widget getWidget(long id);
}
