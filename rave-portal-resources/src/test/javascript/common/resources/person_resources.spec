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

describe('Person Resources', function(){
    var mockBackend, person, friend;

    beforeEach(module('common.resources'));

    beforeEach(function(){
        //A function to compare objects which resources return
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(inject(function(_$httpBackend_, Person, Friend){
        mockBackend = _$httpBackend_;
        person = Person;
        friend = Friend;
    }));

    //Testing person resources

    describe('Person get all', function(){
        it('returns a list of all people', function(){
            var mockDataResponse = [{id: 1, username: 'dgornstein', displayName: 'Dan', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'},
                {id: 1, username: 'jdoe', displayName: 'John', email: 'jdoe@email.com', aboutMe: 'You enjoy myself.', preferredName: 'Johnny'}];

            mockBackend.expectGET('/people').
                respond({meta: {}, data: mockDataResponse});

            var people = person.query();

            mockBackend.flush();

            expect(people).toEqualData(mockDataResponse)
        })
    });

    describe('Person get by id and update', function(){
        it('returns a single person and updates correctly', function(){
            var mockDataResponse = {id: 1, username: 'dgornstein', displayName: 'Dan', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'};
            var mockPostData = {id: 1, username: 'dgornstein', displayName: 'Daniel', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'};

            mockBackend.expectGET('/people/1').
                respond({meta: {}, data: mockDataResponse});

            var people = person.get({id: 1});

            mockBackend.flush();

            expect(people).toEqualData(mockDataResponse)

            mockBackend.expectPOST('/people/1', mockPostData).
                respond({meta: {}, data: mockPostData});

            people.displayName = 'Daniel';

            people.$save();

            mockBackend.flush();

            expect(people).toEqualData(mockPostData);

        })
    });

    describe('post a new Person', function(){
        it('creates a new person correctly', function(){
            var mockDataResponse = {id: 1, username: 'dgornstein', displayName: 'Dan', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'};
            var mockDataResponse2 = {id: 2, username: 'dgornstein', displayName: 'Dan', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'};
            var mockPostData = {username: 'dgornstein', displayName: 'Dan', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'};

            mockBackend.expectPOST('/people', mockPostData).
                respond({meta: {}, data: mockDataResponse});

            mockBackend.expectPOST('/people', mockPostData).
                respond({meta: {}, data: mockDataResponse2});

            var people = person.save(mockPostData);
            var people2 = person.save(mockPostData);

            mockBackend.flush();

            expect(people).toEqualData(mockDataResponse);
            expect(people2).toEqualData(mockDataResponse2);
        })
    });

    //Testing person's friends resources

    describe("Person's friends get all", function(){
        it('returns a list of all friends of a person', function(){
            var mockDataResponse = [{id: 1, username: 'dgornstein', displayName: 'Dan', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'},
                {id: 1, username: 'jdoe', displayName: 'John', email: 'jdoe@email.com', aboutMe: 'You enjoy myself.', preferredName: 'Johnny'}];

            mockBackend.expectGET('/people/3/friends').
                respond({meta: {}, data: mockDataResponse});

            var friends = friend.query({personId: 3});

            mockBackend.flush();

            expect(friends).toEqualData(mockDataResponse)
        })
    });

    describe("Person's friends get by id", function(){
        it('returns a single friend correctly', function(){
            var mockDataResponse = {id: 1, username: 'dgornstein', displayName: 'Dan', email: 'dgornstein@email.com', aboutMe: 'A blurb about me.', preferredName: 'Danny'};

            mockBackend.expectGET('/people/3/friends/1').
                respond({meta: {}, data: mockDataResponse});

            var friends = friend.get({personId: 3, id: 1});

            mockBackend.flush();

            expect(friends).toEqualData(mockDataResponse);
        })
    });

   //TODO: Test making a new relationship between two people so they are friends


    //TODO: Test requests of persons once we know what the object will look like.
})
