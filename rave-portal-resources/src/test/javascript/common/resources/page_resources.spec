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

describe('Page Resources', function(){
    var mockBackend, page;

    beforeEach(module('common.resources'));

    beforeEach(function(){
        //A function to compare objects which resources return
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(inject(function(_$httpBackend_, Page){
        mockBackend = _$httpBackend_;
        page = Page;
    }));

    describe('Page get all', function(){
        it('returns a list of all pages', function(){
            var mockDataResponse = [{id: 1, name: 'portal page', ownerId: 3, pageType: '2_col', pageLayoutCode: 1},
                                    {id: 2, name: 'profile page', ownerId: 6, pageType: '2_col', pageLayoutCode: 3}];

            mockBackend.expectGET('/pages').
                respond({meta: {}, data: mockDataResponse});

            var pages = page.query();

            mockBackend.flush();

            expect(pages).toEqualData(mockDataResponse)
        })
    });

    describe('Page get by id and update', function(){
        it('returns a single page and updates correctly', function(){
            var mockDataResponse = {id: 1, name: 'portal page', ownerId: 3, pageType: '2_col', pageLayoutCode: 1};
            var mockPostData = {id: 1, name: 'portal page update', ownerId: 3, pageType: '2_col', pageLayoutCode: 1};

            mockBackend.expectGET('/pages/1').
                respond({meta: {}, data: mockDataResponse});

            var pages = page.get({id: 1});

            mockBackend.flush();

            expect(pages).toEqualData(mockDataResponse);

            mockBackend.expectPOST('/pages/1', mockPostData).
                respond({meta: {}, data: mockPostData});

            pages.name = 'portal page update';

            pages.$save();

            mockBackend.flush();

            expect(pages).toEqualData(mockPostData);
        })
    });

    describe('post a new Page', function(){
        it('creates a new page correctly', function(){
            var mockDataResponse = {id: 1, name: 'portal page', ownerId: 3, pageType: '2_col', pageLayoutCode: 1};
            var mockDataResponse2 = {id: 2, name: 'portal page', ownerId: 3, pageType: '2_col', pageLayoutCode: 1};
            var mockPostData = {name: 'portal page', ownerId: 3, pageType: '2_col', pageLayoutCode: 1};

            mockBackend.expectPOST('/pages', mockPostData).
                respond({meta: {}, data: mockDataResponse});

            mockBackend.expectPOST('/pages', mockPostData).
                respond({meta: {}, data: mockDataResponse2});

            var pages = page.save(mockPostData);
            var pages2 = page.save(mockPostData);

            mockBackend.flush();

            expect(pages).toEqualData(mockDataResponse);
            expect(pages2).toEqualData(mockDataResponse2);
        })
    });

})
