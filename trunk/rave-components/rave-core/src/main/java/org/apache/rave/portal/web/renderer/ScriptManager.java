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

package org.apache.rave.portal.web.renderer;

import org.apache.rave.portal.web.renderer.model.RenderContext;

import java.util.List;

/**
 * Manages the registration & retrieval of script blocks for the given context
 */
public interface ScriptManager {
    /**
     * Retrieves all script blocks registered for the current location in order of their registration
     *
     *
     * @param location where the script blocks are intended to be rendered
     * @param context the context under which to operate
     * @return a list of strings representing all script blocks for the location
     */
    List<String> getScriptBlocks(ScriptLocation location, RenderContext context);

    /**
     * Registers a global script block for inclusion in every page
     * @param key
     * @param script the string representation of the script block
     * @param location the location in the page to render the script
     */
    void registerScriptBlock(String key, String script, ScriptLocation location);

    /**
     * Registers a script block for inclusion in the page
     * @param key
     * @param script the string representation of the script block
     * @param location the location in the page to render the script
     * @param scope indicates the scope under which this script should be registered
     * @param context the context under which to register the script
     */
    void registerScriptBlock(String key, String script, ScriptLocation location, RenderScope scope, RenderContext context);
}
