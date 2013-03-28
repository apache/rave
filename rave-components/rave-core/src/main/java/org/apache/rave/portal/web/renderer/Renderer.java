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

/**
 * Provides rendering facilities for the specified type
 */
public interface Renderer<T> {

    /**
     * Gets the context under which this renderer can be used
     * @return String representation of the supported render context
     */
    String getSupportedContext();

    /**
     * Renders the item as a String
     *
     * @param item item to render
     * @param context
     * @return String representing the rendered item
     */
    @Deprecated
    String render(T item, RenderContext context);

    /**
     * Prepares the item for rendering
     * @param item the item to prepare
     * @return an instance of a prepared item
     */
    T prepareForRender(T item);
}
