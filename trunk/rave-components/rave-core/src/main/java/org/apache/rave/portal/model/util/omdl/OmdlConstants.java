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
package org.apache.rave.portal.model.util.omdl;

public interface OmdlConstants {
    public final String DEFAULT_STATUS = ""; // no meaning in rave
    public final String DEFAULT_DESCRIPTION = ""; // no meaning in rave

    // Vertical alignment properties
    public final String POSITION_TOP = "TOP";// use for vertical alignment
    public final String POSITION_MIDDLE = "MIDDLE"; // use for vertical alignment
    public final String POSITION_BOTTOM = "BOTTOM";// use for vertical alignment

    // Horizontal alignment properties
    public final String POSITION_LEFT = "LEFT"; // use for horizontal alignment
    public final String POSITION_CENTER = "CENTER"; // use for horizontal alignment
    public final String POSITION_RIGHT = "RIGHT"; // use for horizontal alignment

    // Default Layouts
    public final String GRID_LAYOUT = "GRID";
    public final String FLOW_LAYOUT = "FLOW";

    // Layout properties
    public final String COLUMNS = "COLUMNS";
    public final String ROWS = "ROWS";

    // Width properties
    public final String WIDE = "WIDE";
    public final String NARROW = "NARROW";

    // Column numbers - currently only support up to 4
    public final String NUMBER_ONE = "ONE";
    public final String NUMBER_TWO = "TWO";
    public final String NUMBER_THREE = "THREE";
    public final String NUMBER_FOUR = "FOUR";

    public final String SPACE = " ";

    public final String APP_TYPE_W3C = "application/widget";
    public final String APP_TYPE_OPENSOCIAL = "application/vnd-opensocial+xml";
    public final String APP_TYPE_UNKNOWN = "application/unknown";
    public final String REL_TYPE = "source";
    public final String RAVE_APP_TYPE_OPENSOCIAL = "OpenSocial";
    public final String RAVE_APP_TYPE_W3C = "W3C";

    // XML Element and attributes
    public final String NAMESPACE = "http://omdl.org";
    public final String WORKSPACE = "workspace";
    public final String TITLE = "title";
    public final String LAYOUT = "layout";
    public final String APP = "app";
    public final String LINK = "link";
    public final String POSITION = "position";
    public final String ID_ATTRIBUTE = "id";
    public final String TYPE_ATTRIBUTE = "type";
    public final String HREF = "href";

    public final String DEFAULT_LAYOUT = FLOW_LAYOUT;

    public final String UNKNOWN_VALUE = "unknown";

}
