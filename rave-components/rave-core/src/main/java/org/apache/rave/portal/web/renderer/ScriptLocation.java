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

/**
 * Enumeration of potential render locations for the script block
 */
public enum ScriptLocation {
    /**
     * Renders the given script in the head
     */
    HEAD,
    /**
     * Renders before any third party Rave dependencies
     */
    BEFORE_LIB,
    /**
     * Renders immediately after third party Rave dependencies
     */
    AFTER_LIB,
    /**
     * Renders immediately before the Rave script blocks
     */
    BEFORE_RAVE,
    /**
     * Renders immediately after the Rave script blocks
     */
    AFTER_RAVE
}
