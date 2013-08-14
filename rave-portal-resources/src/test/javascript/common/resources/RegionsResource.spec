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

xdescribe('Region Resources', function(){
    var mockBackend, region;

    beforeEach(module('common.resources'));

    beforeEach(function(){
        //A function to compare objects which resources return
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(inject(function(_$httpBackend_, Regions){
        mockBackend = _$httpBackend_;
        region = Regions;
    }));

    describe('Region get all', function(){
        it('returns a list of all regions', function(){
            var mockDataResponse = [{id: 1, locked: 0, renderOrder: 0}, {id: 2, locked: 1, renderOrder: 1}];

            mockBackend.expectGET('/pages/123/regions').
                respond({meta: {}, data: mockDataResponse});

            var regions = region.query({pageId: 123});

            mockBackend.flush();

            expect(regions).toEqualData(mockDataResponse)
        })
    });

    describe('Region get by id and update', function(){
        it('correctly returns a single region and updates it', function(){
            var mockDataResponse = {id: 1, locked: 0, renderOrder: 0};
            var mockPostData = {id: 1, locked: 1, renderOrder: 0};

            mockBackend.expectGET('/pages/123/regions/1').
                respond({meta: {}, data: mockDataResponse});

            var regions = region.get({pageId: 123, id: 1});

            mockBackend.flush();

            expect(regions).toEqualData(mockDataResponse)

            mockBackend.expectPOST('/pages/123/regions/1', mockPostData).
                respond({meta: {}, data: mockPostData});

            regions.locked = 1
            regions.$save({pageId: 123});

            mockBackend.flush();

            expect(regions).toEqualData(mockPostData);
        })
    });

    describe('post a new Region', function(){
        it('saves a new Region correctly', function(){
            var mockPostData = {locked: 0, renderOrder: 0};
            var mockDataResponse = {id: 1, locked: 0, renderOrder: 0};
            var mockDataResponse2 = {id: 2, locked: 0, renderOrder: 0};

            mockBackend.expectPOST('/pages/123/regions').
                respond({meta: {}, data: mockDataResponse});

            mockBackend.expectPOST('/pages/123/regions').
                respond({meta: {}, data: mockDataResponse2});

            var regions = region.save({pageId: 123}, mockPostData);
            var regions2 = region.save({pageId: 123}, mockPostData);

            mockBackend.flush();

            expect(regions).toEqualData(mockDataResponse)
            expect(regions2).toEqualData(mockDataResponse2)

        })
    });

})
