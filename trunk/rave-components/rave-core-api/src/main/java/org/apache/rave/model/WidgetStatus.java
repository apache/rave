/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.model;

import java.util.HashMap;
import java.util.Map;

/**
 * (Workflow) status of a Widget.
 */
public enum WidgetStatus {
    PUBLISHED("published"),
    PREVIEW("preview");

    private String widgetStatus;

    private static final Map<String, WidgetStatus> lookup = new HashMap<String, WidgetStatus>();

    static {
        for (WidgetStatus ws : WidgetStatus.values()) {
            lookup.put(ws.toString(), ws);
        }
    }

    private WidgetStatus(String widgetStatus) {
        this.widgetStatus = widgetStatus;
    }

    public String getWidgetStatus() {
        return widgetStatus;
    }

    @Override
    public String toString() {
        return widgetStatus;
    }

    public static WidgetStatus get(String status) {
        return lookup.get(status);
    }

    
}
