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

/**
 * Manages rave the registering and rendering of menu and toolbar actions.
 * @module rave_action_manager
 */
define(['underscore'], function(_){
    /**
     * @typedef RegionWidget
     * @see module:rave_widget
     */
    var actionHandlers = [];
    var exports = {};

    /**
     * Registers a new action with the action_manager and calls all actionHandlers' create function
     * @param path {string} path to the action /gadget/toolbar, /gadget/menu, /container/menu, etc
     * @param widgetId {string} the id of the widget contributing the action
     * @param fnAction function to call when the action is selected
     * @param tooltip
     * @param image the image to display, either a relative path, URL or css class name in format of css:<css_type>:<className> eg. class:boostrap:icon-fun
     * @param label the label of the action
     * @param id unique ID of the action
     */
    exports.createAction = function(id, label, path, widgetId, image, tooltip, fnAction) {
        _.invoke(actionHandlers, 'create', id, label, path, widgetId, image, tooltip, fnAction);
    }

    /**
     * Removes an action by calling all actionHandlers remove function
     * @param {string} id Unique identifier of the action
     */
    exports.removeAction = function(id) {
        _.invoke(actionHandlers, 'remove', id);
    }

    /**
     * Registers a UI manager for rendering and hiding UI elements for actions contributed by widgets
     * @param {object} manager structure: { createAction : function(path, widget), removeAction: function(path, widget)}
     */
    exports.registerActionHandler = function(manager) {
        actionHandlers.push(manager);
    }
    return exports;
})