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

xdescribe('Region Widget Resources', function(){
    var mockBackend, regionWidget;

    beforeEach(module('common.resources'));

    beforeEach(function(){
        //A function to compare objects which resources return
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(inject(function(_$httpBackend_, RegionWidgets){
        mockBackend = _$httpBackend_;
        regionWidget = RegionWidgets;
    }));

    describe('RegionWidget get all', function(){
        it('returns a list of all region widgets', function(){
            var mockDataResponse = [{id: 1, type: 'opensocial', widgetId: 12, widgetUrl: 'http://example.com/gadget.xml', regionId: '44', collapsed: 0, locked: 0, hideChrome: 1, ownerId: 99, userPrefs: ''},
                {id: 1, type: 'opensocial', widgetId: 15, widgetUrl: 'http://example.com/gadget2.xml', regionId: '92', collapsed: 1, locked: 0, hideChrome: 0, ownerId: 102, userPrefs: ''}];

            mockBackend.expectGET('/pages/123/regions/1/regionWidgets').
                respond({meta: {}, data: mockDataResponse});

            var regionWidgets = regionWidget.query({pageId: 123, regionId: 1});

            mockBackend.flush();

            expect(regionWidgets).toEqualData(mockDataResponse)
        })
    });

    describe('RegionWidget get by id and update', function(){
        it('returns returns a single region widget with updated data', function(){
            var mockDataResponse = {id: 1, type: 'opensocial', widgetId: 12, widgetUrl: 'http://example.com/gadget.xml', regionId: '44', collapsed: 0, locked: 0, hideChrome: 1, ownerId: 99, userPrefs: ''};
            var mockPostData = {id: 1, type: 'w3c', widgetId: 12, widgetUrl: 'http://example.com/gadget.xml', regionId: '44', collapsed: 0, locked: 0, hideChrome: 1, ownerId: 99, userPrefs: ''};

            mockBackend.expectGET('/pages/123/regions/1/regionWidgets/1').
                respond({meta: {}, data: mockDataResponse});

            var regionWidgets = regionWidget.get({pageId: 123, regionId: 1, id: 1});

            mockBackend.flush();

            expect(regionWidgets).toEqualData(mockDataResponse)

            mockBackend.expectPOST('/pages/123/regions/1/regionWidgets/1', mockPostData).
                respond({meta: {}, data: mockPostData});

            regionWidgets.type = 'w3c';

            regionWidgets.$save({pageId: 123, regionId: 1});

            mockBackend.flush();

            expect(regionWidgets).toEqualData(mockPostData);
        })
    });

    describe('post a new RegionWidget', function(){
        it('saves a new RegionWidget successfully', function(){
            var mockPostData = {type: 'opensocial', widgetId: 12, widgetUrl: 'http://example.com/gadget.xml', regionId: '44', collapsed: 0, locked: 0, hideChrome: 1, ownerId: 99, userPrefs: ''};
            var mockDataResponse = {id: 1, type: 'opensocial', widgetId: 12, widgetUrl: 'http://example.com/gadget.xml', regionId: '44', collapsed: 0, locked: 0, hideChrome: 1, ownerId: 99, userPrefs: ''};
            var mockDataResponse2 = {id: 2, type: 'opensocial', widgetId: 12, widgetUrl: 'http://example.com/gadget.xml', regionId: '44', collapsed: 0, locked: 0, hideChrome: 1, ownerId: 99, userPrefs: ''};

            mockBackend.expectPOST('/pages/123/regions/1/regionWidgets', mockPostData).
                respond({meta: {}, data: mockDataResponse});

            mockBackend.expectPOST('/pages/123/regions/1/regionWidgets', mockPostData).
                respond({meta: {}, data: mockDataResponse2});

            var regionWidgets = regionWidget.save({pageId: 123, regionId: 1}, mockPostData);
            var regionWidgets2 = regionWidget.save({pageId: 123, regionId: 1}, mockPostData);

            mockBackend.flush();

            expect(regionWidgets).toEqualData(mockDataResponse)
            expect(regionWidgets2).toEqualData(mockDataResponse2)
        })
    });
})
