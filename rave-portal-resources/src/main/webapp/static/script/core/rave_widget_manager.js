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
 * Manages region widgets on a page, including registration and retrieval
 *
 * @module rave_widget_manager
 */
define(['underscore', 'core/rave_widget'], function (_, regionWidget) {
    var regionWidgets = {};

    var exports = {};

    /**
     * @typedef RegionWidget
     * @see module:rave_widget
     */

    /**
     * Accepts a regionWidget definition object. Instantiates a new RegionWidget object and registers it with rave.
     * @param regionId {(string | number)}
     * @param definition {object} json object defining the regionWidget
     * @return {RegionWidget}
     */
    //TODO: regionId isn't really needed, but the script text is hard coded and I don't want to mess with it yet
    exports.registerWidget = function (regionId, definition) {
        //make regionId optional
        if (!definition) {
            definition = regionId;
        } else {
            //TODO: until api can be updated, attach regionid as an attribute on the widget so that we can filter on this
            definition.regionId = regionId;
        }
        regionWidgets[definition.regionWidgetId] = new regionWidget(definition)
        return regionWidgets[definition.regionWidgetId];
    }

    //uregister a regionwidget, identified by a RegionWidget object, a widget definition, or just an id
    /**
     * Unregister a regionWidget from rave. Accepts a reference to the RegionWidget object, or the regionWidet's id.
     * @param widget {(RegionWidget | string)} The currently registered regionWidget. Must be a reference to the
     * RegionWidget object, or a valid id of a registered regionWidget.
     */
    exports.unregisterWidget = function (widget) {
        var regionWidgetId = widget.regionWidgetId || widget;

        delete regionWidgets[regionWidgetId];
    }

    /**
     * Convenience method to render all registered regionWidgets. Delegates to {@link module:rave_widget#render}. In most cases,
     * you would pass el as a string, rendering each widget into a new instance of a registered view.
     * @param el {(HTMLElement | string)} Element or view to render all widgets into.
     * @param opts {object} options to pass to the regionWidgets' render method.
     */
    exports.renderWidgets = function (el, opts) {
        _.invoke(exports.getWidgets(), 'render', el, opts);
    }

    /**
     * Get registered regionWidget by id.
     * @param regionWidgetId {string}
     * @return {RegionWidget}
     */
    exports.getWidget = function (regionWidgetId) {
        return regionWidgets[regionWidgetId];
    }

    /**
     * Get all registered regionWidgets, optionally filtered by a query object.
     * @param [filter] {object} Filter object may have any number of key / value pairs. Method will return only those
     * regionWidgets that match ALL key / value pairs on the filter. Does not support nested properties.
     * @return {Array.<RegionWidget>}
     * @example
     *
     * registerWidget('1', {id: '1', regionId: '1'});
     * registerWidget('2', {id: '2', regionId: '2'});
     *
     * getWidgets();  //returns both regionWidgets
     * getWidgets({regionId: '2'}); //returns only the second regionWidget
     */
    exports.getWidgets = function (filter) {
        var widgets = _.toArray(regionWidgets);
        if (filter) {
            widgets = _.where(widgets, filter);
        }
        return widgets;
    }

    return exports;
})
