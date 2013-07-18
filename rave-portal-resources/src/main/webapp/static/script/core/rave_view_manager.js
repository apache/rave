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
 * Manages rave the registering and rendering of view objects.
 * @module rave_view_manager
 */
define(['underscore'], function(_){
    //hash of registered views by name
    var registeredViews = {}
    //hash of registered views that have been rendered by a generated uid
    var renderedViews = {}

    var exports = {};

    /**
     *
     * @typedef View
     * @desc A view object may be either an object literal with the following properties, or a
     * constructor for an object that has these properties on its prototype.
     * @property render {function} When invoked will display the view on the page.
     * Must return a reference to 'this'.
     * @property [getWidgetSite] {function} Optional. Required if the view will be used as a container
     * for regionWidgets. Must return an HTMLElement into which the regionWidget will be rendered.
     * @property destroy {function} When invoked will hide the view on the page and do any
     * necessary clean up.
     * @see module:rave_view_manager
     * @see module:rave_widget
     *
     * @example
     * view1 = {
     *      render: function(){
     *          $('#mydiv').show();
     *          return this;
     *      },
     *      getWidgetSite: function(){
     *          return $('#myDiv')[0];
     *      },
     *      destroy: function(){
     *          $('#mydiv').hide();
     *      }
     * }
     *
     * view2 = function(){
     *     this.div = $('<div>').addClass('popup');
     * }
     * view2.prototype.render = function(){
     *     this.div.show();
     *     return this;
     * }
     * view2.prototype.getWidgetSite = function(){
     *     return this.div[0];
     * }
     * view2.prototype.destroy = function(){
     *      this.div.remove();
     * }
     */

    /**
     * Registers a view object by its name.
     * @param key {string} name of view
     * @param view {View}
     */
    exports.registerView = function (key, view) {
        registeredViews[key.toLowerCase()] = view;
    }

    /**
     * Gets a registered view object
     * @param key {string}
     * @return {View}
     */
    exports.getView = function (key) {
        return registeredViews[key.toLowerCase()];
    }

    /**
     * Invokes the render function of a registered view. Any remaining arguments are applied to
     * the render function. A _uid property is set on the view object, which can also be used to retrieve
     * view object.
     * @param key {string}
     * @param args [...] {*} Any arguments following the key will be applied to the view's render function.
     * @return {View} A reference to the rendered view object
     */
    exports.renderView = function (key) {
        var args = _.toArray(arguments).slice(1);

        var view = exports.getView(key);
        if (!view) {
            throw new Error('Attempted to render undefined view: ' + key);
        }
        //if registered view is a constructor, create a new instance
        if (_.isFunction(view)) {
            //TODO: this makes sure that the constructor gets a widget object, but it's cheesy. Should clean it up.
            view = new view(args[0]);
        }
        view.render.apply(view, args);
        view._uid = _.uniqueId('rave_view_');
        renderedViews[view._uid] = view;
        return view;
    }

    /**
     * Retrieves a rendered view based on it's _uid property.
     * @param _uid {string}
     * @return {View}
     */
    exports.getRenderedView = function (_uid) {
        return renderedViews[_uid];
    }

    /**
     * Invokes the destroy method on a registered view object
     * @param view {View|string} Accepts a reference to the view object
     * or its _uid property, by which the view object is retrieved and destroyed
     */
    exports.destroyView = function (view) {
        var args = _.toArray(arguments).slice(1);

        //accept view object or view _uid
        if (_.isString(view)) {
            view = exports.getRenderedView(view);
        }
        delete renderedViews[view._uid];
        view.destroy(args);
    }

    /**
     * Resets the view registry. Intended only for testing internal use.
     * @private
     */
    exports.reset = function () {
        registeredViews = {};
    }

    return exports;
})
