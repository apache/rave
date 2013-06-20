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
 *//*


describe('Rave API', function () {

    var testScope;

    */
/*
    Start stubs
     *//*

    //TODO: there are several bad dependencies in rave_api on portal functionality - stub the calls until I can fix
    rave.getClientMessage = function () {
    };
    rave.showInfoMessage = function () {
    }
    $ = function () {
        return {
            data: function () {
                return this;
            },
            removeClass: function () {
                return this;
            },
            addClass: function () {
                return this;
            },
            html: function () {
                return this;
            }
        }
    }
    */
/*
     End stubs
     *//*


    beforeEach(function () {
        rave.ajax = function (args) {
            args.success({});
        }
        testScope = {
            callback: function () {
            }
        }

        spyOn(rave, 'ajax').andCallThrough();
        spyOn(testScope, 'callback');
    });

    //test helper function
    function expectAjax(type, url, data) {
        var args = rave.ajax.mostRecentCall.args[0];

        expect(rave.ajax).toHaveBeenCalled();
        expect(args.type).toEqual(type);
        expect(args.url).toEqual(url);
        if (data) {
            expect(args.data).toEqual(data);
        }
        if (args.successCallback) {
            expect(testScope.callback).toHaveBeenCalled();
        }
    }

    describe('rest', function () {

        describe('saveWidgetPreferences', function () {
            it('makes the correct api call', function () {

                rave.api.rest.saveWidgetPreferences({regionWidgetId: 1, userPrefs: {"color": "blue", "speed": "fast"},
                    successCallback: testScope.callback});

                expectAjax(
                    'PUT',
                    'api/rest/regionWidgets/1/preferences',
                    JSON.stringify({preferences: [
                        {name: 'color', value: 'blue'},
                        {name: 'speed', value: 'fast'}
                    ]})
                );
            });
        });

        describe('saveWidgetPreference', function () {
            it('makes the correct api call', function () {

                rave.api.rest.saveWidgetPreference({regionWidgetId: 1, userPref: {prefName: 'color', prefValue: 'blue'},
                    successCallback: testScope.callback});

                expectAjax(
                    'PUT',
                    'api/rest/regionWidgets/1/preferences/color',
                    JSON.stringify({name: 'color', value: 'blue'})
                );
            });
        });

        describe('saveWidgetCollapsedState', function () {
            it('makes the correct api call', function () {

                rave.api.rest.saveWidgetCollapsedState({regionWidgetId: 1, collapsed: true,
                    successCallback: testScope.callback});

                expectAjax(
                    'PUT',
                    'api/rest/regionWidgets/1/collapsed',
                    JSON.stringify(true)
                );
            });
        });

        describe('deletePage', function () {
            it('makes the correct api call', function () {

                rave.api.rest.deletePage({pageId: 1,
                    successCallback: testScope.callback});

                expectAjax(
                    'DELETE',
                    'api/rest/page/1');
            });
        });

        describe('deleteWidgetRating', function () {
            it('makes the correct api call', function () {

                rave.api.rest.deleteWidgetRating({widgetId: 1, collapsed: true,
                    successCallback: testScope.callback});

                expectAjax(
                    'DELETE',
                    'api/rest/widgets/1/rating');
            });
        });


        describe('updateWidgetRating', function () {
            it('makes the correct api call', function () {

                rave.api.rest.updateWidgetRating({widgetId: 1, score: 2,
                    successCallback: testScope.callback});

                expectAjax(
                    'POST',
                    'api/rest/widgets/1/rating?score=2');
            });
        });

        describe('createWidgetComment', function () {
            it('makes the correct api call', function () {

                var text = 'my comment'

                rave.api.rest.createWidgetComment({widgetId: 1, text: text,
                    successCallback: testScope.callback});

                expectAjax(
                    'POST',
                    'api/rest/widgets/1/comments?text=' + escape(text));
            });
        });

        describe('deleteWidgetComment', function () {
            it('makes the correct api call', function () {

                rave.api.rest.deleteWidgetComment({widgetId: 1, commentId: 2,
                    successCallback: testScope.callback});

                expectAjax(
                    'DELETE',
                    'api/rest/widgets/1/comments/2');
            });
        });

        describe('updateWidgetComment', function () {
            it('makes the correct api call', function () {

                var text = 'my comment'

                rave.api.rest.updateWidgetComment({widgetId: 1, commentId: 2, text: text,
                    successCallback: testScope.callback});

                expectAjax(
                    'POST',
                    'api/rest/widgets/1/comments/2?text=' + escape(text));
            });
        });

        describe('getUsersForWidget', function () {
            it('makes the correct api call', function () {

                rave.api.rest.getUsersForWidget({widgetId: 1,
                    successCallback: testScope.callback});

                expectAjax(
                    'GET',
                    'api/rest/widgets/1/users');
            });
        });


        describe('createWidgetTag', function () {
            it('makes the correct api call', function () {

                var text = 'my tag'

                rave.api.rest.createWidgetTag({widgetId: 1, text: text,
                    successCallback: testScope.callback});

                expectAjax(
                    'POST',
                    'api/rest/widgets/1/tags?tagText=' + escape(text));
            });
        });

        describe('getTags', function () {
            it('makes the correct api call', function () {

                rave.api.rest.getTags({widgetId: 1,
                    successCallback: testScope.callback});

                expectAjax(
                    'GET',
                    'api/rest/widgets/1/tags');
            });
        });
    });

    describe('rpc', function () {

        describe('moveWidgetToRegion', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.moveWidgetToRegion({regionWidgetId: 1, toIndex: 2, toRegionId: 3, fromRegionId: 4,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/regionWidget/1/move',
                    {
                        newPosition: 2,
                        toRegion: 3,
                        fromRegion: 4
                    }
                );
            });
        });

        describe('addWidgetToPage', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.addWidgetToPage({pageId: 1, widgetId: 2,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/widget/add',
                    {
                        widgetId: 2
                    }
                );
            });
        });

        describe('addWidgetToPageRegion', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.addWidgetToPageRegion({pageId: 1, widgetId: 2, regionId: 3,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/widget/add/region/3',
                    {
                        widgetId: 2
                    }
                );
            });
        });

        describe('deleteWidgetOnPage', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.removeWidget({regionWidgetId: 1,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/regionWidget/1/delete'
                );
            });
        });

        describe('addPage', function () {
            it('makes the correct api call', function () {
                var pageName = "tom's page"

                rave.api.rpc.addPage({pageName: pageName, pageLayoutCode: 1,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/add',
                    {pageName: pageName,
                        pageLayoutCode: 1}
                );
            });
        });

        describe('getPage', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.getPage({
                    successCallback: testScope.callback
                });

                expectAjax(
                    'GET',
                    'api/rpc/page/get'
                );
            });
        });

        describe('movePage', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.movePage({moveAfterPageId: 1, pageId: 2,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/2/move',
                    {
                        moveAfterPageId: 1
                    }
                );
            });
        });

        describe('moveWidgetToPage', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.moveWidgetToPage({toPageId: 1, regionWidgetId: 2,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/moveWidget',
                    {
                        toPageId: 1,
                        regionWidgetId: 2
                    }
                );
            });
        });

        describe('updatePagePrefs', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.updatePagePrefs({pageId: 1, title: 'test title', layout: 2,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/update',
                    {
                        name: 'test title',
                        layout: 2
                    }
                );
            });
        });

        describe('getPagePrefs', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.getPagePrefs({pageId: 1,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'GET',
                    'api/rpc/page/get?pageId=1'
                );
            });
        });

        describe('getWidgetMetadata', function () {
            it('makes the correct api call', function () {
                var url = 'http://www.example.com/path?with=args#hash'

                rave.api.rpc.getWidgetMetadata({url: url, providerType: 'opensocial',
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/widget/metadata/get',
                    {
                        url: url,
                        type: 'opensocial'
                    }
                );
            });
        });

        describe('getWidgetMetadataGroup', function () {
            it('makes the correct api call', function () {
                var url = 'http://www.example.com/path?with=args#hash'

                rave.api.rpc.getWidgetMetadataGroup({url: url, providerType: 'opensocial',
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/widget/metadatagroup/get',
                    {
                        url: url,
                        type: 'opensocial'
                    }
                );
            });
        });

        describe('getAllWidgets', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.getAllWidgets({
                    successCallback: testScope.callback
                });

                expectAjax(
                    'GET',
                    'api/rpc/widget/getall'
                );
            });
        });

        describe('getUsers', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.getUsers({offset: 1,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'GET',
                    'api/rpc/person/get',
                    {offset: 1}
                );
            });
        });

        describe('searchUsers', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.searchUsers({ searchTerm: 'term', offset: 1,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'GET',
                    'api/rpc/person/search',
                    {searchTerm: 'term',
                        offset: 1}
                );
            });
        });

        describe('clonePageForUser', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.clonePageForUser({ pageId: 1, userId: 2, pageName: 'page name',
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/clone',
                    {
                        userId: 2,
                        pageName: 'page name'
                    }
                );
            });
        });

        describe('addMemberToPage', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.addMemberToPage({ pageId: 1, userId: 2,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/addmember',
                    {
                        userId: 2
                    }
                );
            });
        });

        describe('removeMemberFromPage', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.removeMemberFromPage({ pageId: 1, userId: 2,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/removemember',
                    {
                        userId: 2
                    }
                );
            });
        });

        describe('updateSharedPageStatus', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.updateSharedPageStatus({ pageId: 1, shareStatus: 2,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/sharestatus',
                    {
                        shareStatus: 2
                    }
                );
            });
        });

        describe('updatePageEditingStatus', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.updatePageEditingStatus({ pageId: 1, userId: 2, isEditor: true,
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/page/1/editstatus',
                    {
                        userId: 2,
                        isEditor: true
                    }
                );
            });
        });

        describe('addFriend', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.addFriend({ friendUsername: 'ted',
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/person/ted/addfriend'
                );
            });
        });

        describe('removeFriend', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.removeFriend({ friendUsername: 'ted',
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/person/ted/removefriend'
                );
            });
        });

        describe('getFriends', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.getFriends({
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/person/getFriends'
                );
            });
        });

        describe('acceptFriendRequest', function () {
            it('makes the correct api call', function () {
                rave.api.rpc.acceptFriendRequest({ friendUsername: 'ted',
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/person/ted/acceptfriendrequest'
                );
            });
        });

        describe('addWidgetFromMarketplace', function () {
            it('makes the correct api call', function () {
                var url = 'http://www.example.com/path?with=args#hash'

                rave.api.rpc.addWidgetFromMarketplace({ url: url, providerType: 'opensocial',
                    successCallback: testScope.callback
                });

                expectAjax(
                    'POST',
                    'api/rpc/marketplace/add',
                    {
                        url: url,
                        providerType: 'opensocial'
                    }
                );
            });
        });
    });
});*/
