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

rave = (function () {
    var INITIALIZED = false,
    //providers - opensocial, wookie...
        providers = {},
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

    var exports = {};

    exports.registerProvider = function (name, provider) {
        providers[name.toLowerCase()] = provider;
        if (INITIALIZED) {
            provider.init();
        }
        return provider;
    }

    exports.getProvider = function (name) {
        return providers[name.toLowerCase()];
    }

    //TODO: regionId isn't really needed, but the script text is hard coded and I don't want to mess with it yet
    exports.registerWidget = function (regionId, definition) {
        //make regionId optional
        if (!definition) {
            definition = regionId;
        } else {
            //TODO: until api can be updated, attach regionid as an attribute on the widget so that we can filter on this
            definition.regionId = regionId;
        }
        regionWidgets[definition.regionWidgetId] = definition;
        if (INITIALIZED) {
            regionWidgets[definition.regionWidgetId] = new rave.RegionWidget(definition)
        }
        return regionWidgets[definition.regionWidgetId];
    }

    //uregister a regionwidget, identified by a RegionWidget object, a widget definition, or just an id
    exports.unregisterWidget = function (widget) {
        var regionWidgetId = widget.regionWidgetId || widget;

        delete regionWidgets[regionWidgetId];
    }

    //convenience method to render all registered widgets
    exports.renderWidgets = function(el, opts) {
        _.invoke(rave.getWidgets(), 'render', el, opts);
    }

    //get registered widget by regionWidgetId
    exports.getWidget = function (regionWidgetId) {
        return regionWidgets[regionWidgetId];
    }

    exports.getWidgets = function (filter) {
        var widgets = _.toArray(regionWidgets);
        if(filter) {
            widgets = _.where(widgets, filter);
        }
        return widgets;
    }

    /*
     key: view name
     view: any object that manages and renders a view. At minimum must have render and destroy methods. render should return 'this'
     */
    exports.registerView = function (key, view) {
        registeredViews[key.toLowerCase()] = view;
    }

    exports.getView = function (key) {
        return registeredViews[key.toLowerCase()];
    }

    exports.renderView = function (key) {
        //apply remaining arguments to the view function - you know best!
        var args = _.toArray(arguments).slice(1);

        var view = rave.getView(key);
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

    exports.getRenderedView = function (_uid) {
        return renderedViews[_uid];
    }

    exports.destroyView = function (view) {
        var args = _.toArray(arguments).slice(1);

        //accept view object or view _uid
        if (_.isString(view)) {
            view = rave.getRenderedView(view);
        }
        delete renderedViews[view._uid];
        view.destroy(args);
    }

    exports.getManagedHub = function () {
        if (!openAjaxHub) {
            if (_.isUndefined(OpenAjax)) {
                throw new Error("No implementation of OpenAjax found.  " +
                    "Please ensure that an implementation has been included in the page.");
            }
            openAjaxHub = new OpenAjax.hub.ManagedHub({
                    onSubscribe:function (topic, container) {
                        rave.log((container == null ? "Container" : container.getClientID()) + " subscribes to this topic '" + topic + "'");
                        return true;
                    },
                    onUnsubscribe:function (topic, container) {
                        rave.log((container == null ? "Container" : container.getClientID()) + " unsubscribes from this topic '" + topic + "'");
                        return true;
                    },
                    onPublish:function (topic, data, pcont, scont) {
                        rave.log((pcont == null ? "Container" : pcont.getClientID()) + " publishes '" + data + "' to topic '" + topic + "' subscribed by " + (scont == null ? "Container" : scont.getClientID()));
                        return true;
                    }
            });
        }
        return openAjaxHub;
    }

    exports.registerOnInitHandler = function (handler) {
        if (!_.isFunction(handler)) {
            throw new Error('Init event handler must be a function');
        }
        if (INITIALIZED) {
            return handler();
        }
        initHandlers.push(handler);
    }

    exports.init = function () {
        INITIALIZED = true;
        _.invoke(providers, 'init');
        _.each(regionWidgets, function (definition) {
            regionWidgets[definition.regionWidgetId] = new rave.RegionWidget(definition)
        });
        _.each(initHandlers, function (fn) {
            fn();
        });
    }

    //wrap a safe version of console.log
    exports.log = function(msg){
        if  (console && console.log) {
            console.log(msg);
        }
    }

    //reset internal data - used for testing cleanup
    exports.reset = function () {
        INITIALIZED = false;
        providers = {};
        regionWidgets = {};
        registeredViews = {};
        renderedViews = {};
        initHandlers = [];
        openAjaxHub;
    }

    return exports;

})();