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

var rave = rave || {};

/*
 Rave RegionWidget Interface

 Dependencies:
 rave.ui
 rave.api
 */

rave.RegionWidget = (function () {
    /*
     rave widget constructor
     */
    var Widget = function (definition) {
        var provider = definition.type;

        _.extend(this, definition);

        this._provider = rave.getProvider(provider);

        if (!this._provider) {
            throw new Error('Cannot render widget ' + definition.widgetUrl + '. ' +
                'Provider ' + provider + ' is not registered.');
        }

        this._provider.initWidget(this);
    }

    Widget.defaultView = 'default';
    Widget.defaultWidth = 320;
    Widget.defaultHeight = 200;

    Widget.extend = function (mixin) {
        _.extend(this.prototype, mixin);
    }

    Widget.prototype.render = function (el, opts) {
        //if we receive only one argument, and the first arg is not a string or dom element, assume it is an opts object
        //and el should default to the widgets current render element
        if (!opts && !(_.isString(el) || (el instanceof HTMLElement))) {
            opts = el;
            el = this._el;
        }
        //if el is a string, go to rave's view system
        if (_.isString(el)) {
            //TODO: potential memory leak - rendering a widget into new views does not force cleanup of current view
            var view = rave.renderView(el, this);
            el = view.getWidgetSite();
            this._view = view;
        }
        //at this point el must be a valid dom element. if not, throw an error
        if (!(el instanceof HTMLElement)) {
            throw new Error('Cannot render widget. You must provide an el to render the view into');
        }
        this._el = el;
        this._provider.renderWidget(this, el, opts);
        return this;
    }

    Widget.prototype.renderError = function (el, errors) {
        el.innerHTML = 'Error rendering widget.' + "<br /><br />" + errors;
    }

    Widget.prototype.hide = function () {
        this.collapsed = true;

        rave.api.rest.saveWidgetCollapsedState({
            regionWidgetId: this.id,
            collapsed: this.collapsed
        });
    }

    Widget.prototype.show = function () {
        this.collapsed = false;

        rave.api.rest.saveWidgetCollapsedState({
            regionWidgetId: this.id,
            collapsed: this.collapsed
        });
    }

    Widget.prototype.close = function (opts) {
        this._provider.closeWidget(this, opts);
        if (this._view) {
            rave.destroyView(this._view);
        }

        rave.api.rpc.removeWidget({
            regionWidgetId: this.id
        });
    }

    Widget.prototype.moveToPage = function (toPageId, cb) {
        rave.api.rpc.moveWidgetToPage({
            toPageId: toPageId,
            regionWidgetId: this.id,
            successCallback: cb
        });
    }

    Widget.prototype.moveToRegion = function (fromRegionId, toRegionId, toIndex) {
        rave.api.rpc.moveWidgetToRegion({
            regionWidgetId: this.id,
            fromRegionId: fromRegionId,
            toRegionId: toRegionId,
            toIndex: toIndex
        });
    }

    Widget.prototype.getPrefs = function () {
        var self = this;
        var combined = [];
        //TODO: I think this is opensocial specific - need to investigate wookie and possibly delegate to providers
        _.each(self.metadata.userPrefs, function (data) {
            var value = self.userPrefs[data.name];
            data = _.clone(data);
            data.value = value || data.defaultValue;
            data.displayName = data.displayName || data.name;
            combined.push(data);
        });
        return _.isEmpty(combined)?undefined:combined;
    }

    Widget.prototype.setPrefs = function (name, val) {
        if (_.isObject(name)) {
            var updatedPrefs = name;
            this.userPrefs = _.object(_.pluck(updatedPrefs,'name'), _.pluck(updatedPrefs, 'value'));
            rave.api.rest.saveWidgetPreferences({regionWidgetId: this.id, userPrefs: this.userPrefs});
        } else {
            this.userPrefs[name] = val;
            rave.api.rest.saveWidgetPreference({regionWidgetId: this.id, prefName: name, prefValue: val});
        }
    }

    return Widget;

})();