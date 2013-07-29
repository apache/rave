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
describe('User Resources', function(){
    var mockBackend, user;

    beforeEach(module('common.resources'));

    beforeEach(function(){
        //A function to compare objects which resources return
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(inject(function(_$httpBackend_, User){
        mockBackend = _$httpBackend_;
        user = User;
    }));

    describe('User get all', function(){
        it('returns a list of all users', function(){
            var mockDataResponse = [{id: 1, username: 'dgornstein', password: 'aHash', email: 'dgornstein@email.com', locked: 0, enabled: 1},
                {id: 2, username: 'jdoe', password: 'aHash', email: 'jdoe@email.com', locked: 1, enabled: 1}];

            mockBackend.expectGET('/users').
                respond({meta: {}, data: mockDataResponse});

            var users = user.query();

            mockBackend.flush();

            expect(users).toEqualData(mockDataResponse)
        })
    });

    describe('User get by id and update', function(){
        it('returns a single user and updates correctly', function(){
            var mockDataResponse = {id: 1, username: 'dgornstein', password: 'aHash', email: 'dgornstein@email.com', locked: 0, enabled: 1};
            var mockPostData = {id: 1, username: 'dgornstein', password: 'aHash', email: 'dgornstein@email.com', locked: 1, enabled: 0};

            mockBackend.expectGET('/users/1').
                respond({meta: {}, data: mockDataResponse});

            var users = user.get({id: 1});

            mockBackend.flush();

            expect(users).toEqualData(mockDataResponse)

            mockBackend.expectPOST('/users/1', mockPostData).
                respond({meta: {}, data: mockPostData});

            users.locked = 1;
            users.enabled = 0;

            users.$save();

            mockBackend.flush();

            expect(users).toEqualData(mockPostData);
        })
    });

    describe('post a new User', function(){
        it('creates a new user correctly', function(){
            var mockDataResponse = {id: 1, username: 'dgornstein', password: 'aHash', email: 'dgornstein@email.com', locked: 0, enabled: 1};
            var mockDataResponse2 = {id: 2, username: 'dgornstein', password: 'aHash', email: 'dgornstein@email.com', locked: 0, enabled: 1};
            var mockPostData = {username: 'dgornstein', password: 'aHash', email: 'dgornstein@email.com', locked: 0, enabled: 1};

            mockBackend.expectPOST('/users', mockPostData).
                respond({meta: {}, data: mockDataResponse});

            mockBackend.expectPOST('/users', mockPostData).
                respond({meta: {}, data: mockDataResponse2});

            var users = user.save(mockPostData);
            var users2 = user.save(mockPostData);

            mockBackend.flush();

            expect(users).toEqualData(mockDataResponse);
            expect(users2).toEqualData(mockDataResponse2);
        })
    });

})
