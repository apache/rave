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

define(["underscore", "backbone"], function(_, Backbone){
    /*
     Extend backbone's standard model and collection with some
     */
    var Model = Backbone.Model.extend({
        get: function(attr){
            //tweak model get so that array / object members are passed by value instead of reference
            //needed for managing deep objects
            return _.clone(this.attributes[attr]);
        },

        /*
         Overridable function that models can implement for serializing themselves for view rendering,
         since often a handlebars template needs explicit keys or booleans that don't make sense
         in a normal json representation of the model. By default will just return toJSON().
         */
        toViewModel: function () {
            return this.toJSON();
        }
    });

    var Collection = Backbone.Collection.extend({
        toViewModel: function () {
            return this.map(function (model) {
                return model.toViewModel();
            });
        }
    })


    /*
     rave.View is an extension of Backbone's view with some scaffolding put in place. The expectation is that a view
     will be declared with a hash of models (models or collections) that will be merged and fed to the view at render
     time. By default on any change to the models the view will be re-rendered. Also provides an implementation of
     render that probably will not need to be overrridden.
     */
    var View = Backbone.View.extend({
        initialize: function () {
            var self = this;
            _.bindAll(this);

            _.each(this.models, function (model) {
                model.on('change', self.render);
                model.on('reset', self.render);
            });
        },
        render: function () {
            var template = this.template;

            var viewData = {};
            _.each(this.models, function (model, key) {
                viewData[key] = model.toViewModel();
            });

            this.$el.html(template(viewData));
            return this;
        }
    });

    return{
        Model:Model,
        Collection:Collection,
        View:View
    }
})