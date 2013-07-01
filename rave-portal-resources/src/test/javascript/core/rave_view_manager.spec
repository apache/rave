/* * Licensed to the Apache Software Foundation (ASF) under one
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


describe('rave_view_manager', function () {
    var viewName = 'modal',
        viewObject,
        testScope = {};

    beforeEach(function(){
        ViewManager = testr('core/rave_view_manager');

        viewObject = {
            render: function () {
            },
            getWidgetSite: function () {
            },
            destroy: function () {
            }
        }

        _.each(viewObject, function (fn, key) {
            spyOn(viewObject, key);
        });

        testScope.ViewConstructor = function () {
            this.render = function () {
            };
            this.getWidgetSite = function () {
            };
            this.destroy = function () {
            };

            spyOn(this, 'render');
            spyOn(this, 'getWidgetSite');
            spyOn(this, 'destroy');
        }
        spyOn(testScope, 'ViewConstructor').andCallThrough();


    })

    afterEach(function () {
        ViewManager.reset();
    });

    describe('registerView / getView', function () {
        it('registers and retrieves views and is case insensitive', function () {
            expect(ViewManager.getView(viewName)).toBeUndefined();
            ViewManager.registerView(viewName, viewObject);
            expect(ViewManager.getView(viewName)).toBe(viewObject);
            expect(ViewManager.getView(viewName.toUpperCase())).toBe(viewObject);
        });
    });

    describe('renderView', function () {
        it('throws an error if you render an unregistered view', function () {
            expect(function () {
                ViewManager.renderView('asdf')
            }).toThrow();
        });

        it('invokes render on a view object', function () {
            ViewManager.registerView(viewName, viewObject);
            ViewManager.renderView(viewName);

            expect(viewObject.render).toHaveBeenCalled();
        });

        it('returns the view object', function () {
            ViewManager.registerView(viewName, viewObject);
            expect(ViewManager.renderView(viewName)).toBe(viewObject);
        });

        it('instantiates a view constructor and calls its render', function () {
            ViewManager.registerView(viewName, testScope.ViewConstructor);
            var viewInstance = ViewManager.renderView(viewName);

            expect(testScope.ViewConstructor).toHaveBeenCalled();
            expect(viewInstance.render).toHaveBeenCalled();
            expect(viewInstance.destroy).not.toHaveBeenCalled();
        });

        it('passes through any arbitrary arguments to the render function', function () {
            //with view object
            ViewManager.registerView(viewName, viewObject);
            ViewManager.renderView(viewName, 1, 'bird', {hello: 'world'});
            expect(viewObject.render).toHaveBeenCalledWith(1, 'bird', {hello: 'world'});

            //or view constructor
            ViewManager.registerView(viewName, viewObject);
            var viewInstance = ViewManager.renderView(viewName);
            expect(viewInstance.render).toHaveBeenCalledWith(1, 'bird', {hello: 'world'});
        });

        it('assigns a uid to a rendered view', function () {
            ViewManager.registerView(viewName, viewObject);
            var viewInstance = ViewManager.renderView(viewName);

            expect(viewInstance._uid).toBeDefined();
        });
    });

    describe('getRenderedView', function () {
        it('retrieves views that have been rendered', function () {
            ViewManager.registerView(viewName, viewObject);
            var viewInstance1 = ViewManager.renderView(viewName);
            var viewInstance2 = ViewManager.renderView(viewName);

            expect(ViewManager.getRenderedView(viewInstance1._uid)).toBe(viewInstance1);
            expect(ViewManager.getRenderedView(viewInstance2._uid)).toBe(viewInstance2);
            expect(ViewManager.getRenderedView('asdfasdf')).toBeUndefined();
        });
    });

    describe('destroyView', function () {
        it('invokes destroy on a view object', function () {
            ViewManager.registerView(viewName, viewObject);
            ViewManager.renderView(viewName);
            ViewManager.destroyView(viewObject);

            expect(viewObject.destroy).toHaveBeenCalled();
        });

        it('invokes destroy on a view instance', function () {
            ViewManager.registerView(viewName, viewObject);
            var viewInstance = ViewManager.renderView(viewName);
            ViewManager.destroyView(viewInstance);

            expect(viewInstance.destroy).toHaveBeenCalled();
        });

        it('invokes destroy on a view identified by uid', function () {
            ViewManager.registerView(viewName, viewObject);
            var viewInstance = ViewManager.renderView(viewName);
            ViewManager.destroyView(viewInstance._uid);

            expect(viewInstance.destroy).toHaveBeenCalled();
        });

        it('removes view from renderedViews registry', function () {
            ViewManager.registerView(viewName, viewObject);
            var viewInstance = ViewManager.renderView(viewName);
            expect(ViewManager.getRenderedView(viewInstance._uid)).toBe(viewInstance);

            ViewManager.destroyView(viewInstance._uid);
            expect(ViewManager.getRenderedView(viewInstance._uid)).toBeUndefined();
        });
    });
});
