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

define(['underscore', 'core/rave_widget'], function (_, regionWidget) {
    //hash of widgets by regionWidgetId
    var regionWidgets = {};

    var rave = {};

    //TODO: regionId isn't really needed, but the script text is hard coded and I don't want to mess with it yet
    rave.registerWidget = function (regionId, definition) {
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
    rave.unregisterWidget = function (widget) {
        var regionWidgetId = widget.regionWidgetId || widget;

        delete regionWidgets[regionWidgetId];
    }

    //convenience method to render all registered widgets
    rave.renderWidgets = function (el, opts) {
        _.invoke(rave.getWidgets(), 'render', el, opts);
    }

    //get registered widget by regionWidgetId
    rave.getWidget = function (regionWidgetId) {
        return regionWidgets[regionWidgetId];
    }

    rave.getWidgets = function (filter) {
        var widgets = _.toArray(regionWidgets);
        if (filter) {
            widgets = _.where(widgets, filter);
        }
        return widgets;
    }

    //TODO: I killed registerInitHandlers! Tell people.

    //TODO: reset isn't going to work well with individual modules holding private data
    return rave;
})
