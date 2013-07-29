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

describe('Category Resources', function(){
    var mockBackend, category;

    beforeEach(module('common.resources'));

    beforeEach(function(){
        //A function to compare objects which resources return
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(inject(function(_$httpBackend_, Category){
        mockBackend = _$httpBackend_;
        category = Category;
    }));

    describe('Category get all', function(){
        it('returns a list of all categories', function(){
            var mockDataResponse = [{id: 1, text: 'fake category', createdUserId: 3, createdDate: '01121990', lastModifiedUserId: 3, lastModifiedDate: '01122000'},
                                    {id: 2, text: 'another fake category', createdUserId: 1, createdDate: '01121990', lastModifiedUserId: 5, lastModifiedDate: '01122000'}];

            mockBackend.expectGET('/categories').
                respond({meta: {}, data: mockDataResponse});

            var categories = category.query();

            mockBackend.flush();

            expect(categories).toEqualData(mockDataResponse)
        })
    });

    describe('Category get by ID and update', function(){
        it('returns a single category and updates correctly', function(){
            var mockDataResponse = {id: 1, text: 'fake category', createdUserId: 3, createdDate: '01121990', lastModifiedUserId: 3, lastModifiedDate: '01122000'};
            var mockPostData = {id: 1, text: 'fake category change', createdUserId: 3, createdDate: '01121990', lastModifiedUserId: 3, lastModifiedDate: '01122000'};

            mockBackend.expectGET('/categories/1').
                respond({meta: {}, data: mockDataResponse});

            var categories = category.get({id: 1});

            mockBackend.flush();

            expect(categories).toEqualData(mockDataResponse)

            mockBackend.expectPOST('/categories/1', mockPostData).
                respond({meta: {}, data: mockPostData});

            categories.text = 'fake category change';

            categories.$save();

            mockBackend.flush()

            expect(categories).toEqualData(mockPostData);
        })
    });

    describe('post a new Cateogry', function(){
        it('saves a new category correctly', function(){
            var mockDataResponse = {id: 1, text: 'fake category', createdUserId: 3, createdDate: '01121990', lastModifiedUserId: 3, lastModifiedDate: '01122000'};
            var mockDataResponse2 = {id: 2, text: 'fake category', createdUserId: 3, createdDate: '01121990', lastModifiedUserId: 3, lastModifiedDate: '01122000'};
            var mockPostData = {text: 'fake category', createdUserId: 3, createdDate: '01121990', lastModifiedUserId: 3, lastModifiedDate: '01122000'};

            mockBackend.expectPOST('/categories', mockPostData).
                respond({meta: {}, data: mockDataResponse});

            mockBackend.expectPOST('/categories', mockPostData).
                respond({meta: {}, data: mockDataResponse2});

            var categories = category.save(mockPostData);
            var categories2 = category.save(mockPostData);

            mockBackend.flush();

            expect(categories).toEqualData(mockDataResponse);
            expect(categories2).toEqualData(mockDataResponse2);
        })
    });

})
