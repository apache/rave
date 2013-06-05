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
define(['underscore', './rave_widget', './rave_log'], function (_, RegionWidget, log) {
    var rave = {};

    var INITIALIZED = false,
    //hash of widgets by regionWidgetId
        regionWidgets = {},
    //hash of registered views by name
        registeredViews = {},
    //hash of registered views that have been rendered by a generated uid
        renderedViews = {},
    //event handlers to fire on rave init
        initHandlers = [],
    //oajaxhub to support pubsub. only initialized if needed
        openAjaxHub;

    //TODO: regionId isn't really needed, but the script text is hard coded and I don't want to mess with it yet
    rave.registerWidget = function (regionId, definition) {
        //make regionId optional
        if (!definition) {
            definition = regionId;
        } else {
            //TODO: until api can be updated, attach regionid as an attribute on the widget so that we can filter on this
            definition.regionId = regionId;
        }
        regionWidgets[definition.id] = definition;
        if (INITIALIZED) {
            regionWidgets[definition.id] = new RegionWidget(definition)
        }
        return regionWidgets[definition.id];
    }

    //uregister a regionwidget, identified by a RegionWidget object, a widget definition, or just an id
    rave.unregisterWidget = function (widget) {
        var id = widget.id || widget;

        delete regionWidgets[id];
    }

    //get registered widget by id
    rave.getWidget = function (id) {
        return regionWidgets[id];
    }

    rave.getWidgets = function (filter) {
        var widgets = _.toArray(regionWidgets);
        if (filter) {
            widgets = _.where(widgets, filter);
        }
        return widgets;
    }

    /*
     key: view name
     used to register view surfaces and view targets
     view: any object that manages and renders a view. At minimum must have render and destroy methods. render should return 'this'
     */
    rave.registerView = function (key, view) {
        registeredViews[key.toLowerCase()] = view;
    }

    rave.getView = function (key) {
        return registeredViews[key.toLowerCase()];
    }

    rave.renderView = function (key, el, scope) {
        //apply remaining arguments to the view function - you know best!
        var args = _.toArray(arguments).slice(1);

        var view = rave.getView(key);
        if (!view) {
            throw new Error('Attempted to render undefined view: ' + key);
        }

        //TODO: ignore renderviews and cleanup for a moment
        return view.render(el, scope);
//
//        //if registered view is a constructor, create a new instance
//        if (_.isFunction(view)) {
//            //TODO: this makes sure that the constructor gets a widget object, but it's cheesy. Should clean it up.
//            view = new view(args[0]);
//        }
//        view.render.apply(view, args);
//        view._uid = _.uniqueId('rave_view_');
//        renderedViews[view._uid] = view;
//        return view;
    }

    rave.getRenderedView = function (_uid) {
        return renderedViews[_uid];
    }

    rave.destroyView = function (view) {
        var args = _.toArray(arguments).slice(1);

        //accept view object or view _uid
        if (_.isString(view)) {
            view = rave.getRenderedView(view);
        }
        delete renderedViews[view._uid];
        delete view._uid;
        view.destroy(args);
    }

    rave.getManagedHub = function () {
        if (!openAjaxHub) {
            if (_.isUndefined(OpenAjax)) {
                throw new Error("No implementation of OpenAjax found.  " +
                    "Please ensure that an implementation has been included in the page.");
            }
            openAjaxHub = new OpenAjax.hub.ManagedHub({
                onSubscribe: function (topic, container) {
                    log((container == null ? "Container" : container.getClientID()) + " subscribes to this topic '" + topic + "'");
                    return true;
                },
                onUnsubscribe: function (topic, container) {
                    log((container == null ? "Container" : container.getClientID()) + " unsubscribes from this topic '" + topic + "'");
                    return true;
                },
                onPublish: function (topic, data, pcont, scont) {
                    log((pcont == null ? "Container" : pcont.getClientID()) + " publishes '" + data + "' to topic '" + topic + "' subscribed by " + (scont == null ? "Container" : scont.getClientID()));
                    return true;
                }
            });
        }
        return openAjaxHub;
    }

    rave.registerOnInitHandler = function (handler) {
        if (!_.isFunction(handler)) {
            throw new Error('Init event handler must be a function');
        }
        if (INITIALIZED) {
            return handler();
        }
        initHandlers.push(handler);
    }

    rave.init = function () {
        INITIALIZED = true;
        _.invoke(providers, 'init');
        _.each(regionWidgets, function (definition) {
            regionWidgets[definition.id] = new RegionWidget(definition)
        });
        _.each(initHandlers, function (fn) {
            fn();
        });
    }

    rave.log = log;

    //reset internal data - used for testing cleanup
    rave.reset = function () {
        INITIALIZED = false;
        regionWidgets = {};
        registeredViews = {};
        renderedViews = {};
        initHandlers = [];
        openAjaxHub;
    }

    return rave;

});