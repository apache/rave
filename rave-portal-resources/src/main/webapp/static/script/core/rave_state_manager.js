/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License'); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


/**
 * Manages the state of page-level variables.
 * @module rave_state_manager
 */
define([], function () {

    var page,
        viewer,
        owner,
        context,
        exportEnabled,
        defaultHeight = 200,
        defaultWidth = 320,
        defaultView = 'default',
        debugMode = false;

    var exports = {};

    /**
     * @typedef Page
     * @type {object}
     * @property {string} id - the page's id.
     * @property {string} ownerId - the id of the page owner
     * @property {number} viewerId - the id of the page viewer
     */

    /**
     * Sets the current page
     * @param {Page} p
     */
    exports.setPage = function(p){
        page = p;
    }

    /**
     * Gets the current page
     * @return {Page}
     */
    exports.getPage = function(){
        return page;
    }

    /**
     * @typedef User
     * @type {object}
     * @property {string} id - the user's id.
     * @property {string} username - the user's name
     */

    /**
     * Sets the currently authenticated user (the page viewer)
     * @param v {User}
     */
    exports.setViewer = function(v){
        viewer = v;
    }

    /**
     * Gets the currently authenticated user (the page viewer)
     * @return {User}
     */
    exports.getViewer = function(){
        return viewer;
    }

    /**
     * Sets the current page owner
     * @param o {User}
     */
    exports.setOwner = function(o){
        owner = o;
    }

    /**
     * Gets the current page owner
     * @return {User}
     */
    exports.getOwner = function(){
        return owner;
    }

    /**
     * Sets the base url that the rave api is being hosted from.
     * @example If the api is hosted at localhost:8080/portal/app/api, then the context is "/portal/app/"
     *
     * @param ctx {string}
     */
    exports.setContext = function(ctx){
        context = ctx;
    }

    /**
     * Gets the base url that the rave api is being hosted from.
     * @return {string}
     */
    exports.getContext = function(){
        return context;
    }

    /**
     * Sets whether export is enabled
     * @param e {boolean}
     */
    exports.setExportEnabled = function(e){
        exportEnabled = e;
    }

    /**
     * Gets whether export is enabled
     * @return {boolean}
     */
    exports.getExportEnabled = function(){
        return exportEnabled;
    }

    /**
     * Sets the default height of widgets on a page
     * @param height {number}
     */
    exports.setDefaultHeight = function(height){
        defaultHeight = height;
    }

    /**
     * Gets the default height of widgets on a page
     * @return {number}
     */
    exports.getDefaultHeight = function(){
        return defaultHeight;
    }

    /**
     * Sets default widget of widgets on a page
     * @param width {number}
     */
    exports.setDefaultWidth = function(width){
        defaultWidth = width;
    }

    /**
     * Gets default widget of widgets on a page
     * @return {number}
     */
    exports.getDefaultWidth = function(){
        return defaultWidth;
    }

    /**
     * Sets the default view that widgets will render into on a page. In opensocial this
     * corresponds to the "view" render param. In wookie this feature is not yet supported.
     * @see http://opensocial-resources.googlecode.com/svn/spec/trunk/Core-Gadget.xml#gadgets.views.ViewType
     * @param view {string}
     */
    exports.setDefaultView = function(view){
        defaultView = view;
    }

    /**
     * Sets the default view that widgets will render into on a page
     * @return {string}
     */
    exports.getDefaultView = function(){
        return defaultView;
    }

    /**
     * Sets the javascript debug mode. This determines whether the
     * scripts from opensocial features are concatenated and minified.
     * @param b {boolean}
     */
    exports.setDebugMode = function(b) {
        debugMode = b;
    }

    /**
     * Gets the javascript debug mode.
     * @return {boolean}
     */
    exports.getDebugMode = function(){
        return debugMode;
    }

    return exports;

});
