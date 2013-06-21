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

define(['underscore'], function(_){
    //hash of registered views by name
    var registeredViews = {}
    //hash of registered views that have been rendered by a generated uid
    var renderedViews = {}

    var exports = {};

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

    exports.getRenderedView = function (_uid) {
        return renderedViews[_uid];
    }

    exports.destroyView = function (view) {
        var args = _.toArray(arguments).slice(1);

        //accept view object or view _uid
        if (_.isString(view)) {
            view = exports.getRenderedView(view);
        }
        delete renderedViews[view._uid];
        view.destroy(args);
    }

    //reset internal data - used for testing cleanup
    exports.reset = function () {
        registeredViews = {};
    }

    return exports;
})
