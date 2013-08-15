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

/**
 * Helper functions for interacting with the rave rpc and rest apis.
 *
 * @module rave_api
 * @requires rave_ajax
 * @requires rave_state_manager
 * @requires rave_event_manager
 */
define(['underscore', 'core/rave_ajax', 'core/rave_state_manager', 'core/rave_event_manager'],
    function (_, ajax, stateManager, eventManager) {

        var context = '';
        eventManager.registerOnInitHandler(function () {
            context = stateManager.getContext();
        });

        function handleError(jqXhr, status, error) {
            alert(error);
        }

        var restApi = (function () {
            //Base path to RPC services
            var path = "api/rest/";

            function saveWidgetPreferences(args) {
                var preferencesData = {"preferences": []};
                for (var prefName in args.userPrefs) {
                    preferencesData.preferences.push({"name": prefName, "value": args.userPrefs[prefName]});
                }

                ajax({
                    type: 'PUT',
                    url: context + path + "regionWidgets/" + args.regionWidgetId + "/preferences",
                    data: JSON.stringify(preferencesData),
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                });
            }

            function saveWidgetPreference(args) {
                ajax({
                    type: 'PUT',
                    url: context + path + "regionWidgets/" + args.regionWidgetId + "/preferences/" + args.prefName,
                    data: JSON.stringify({"name": args.prefName, "value": args.prefValue}),
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                });
            }

            function saveWidgetCollapsedState(args) {
                ajax({
                    type: 'PUT',
                    url: context + path + "regionWidgets/" + args.regionWidgetId + "/collapsed",
                    data: JSON.stringify(args.collapsed),
                    contentType: 'application/json',
                    dataType: 'json',
                    success: args.successCallback,
                    error: handleError
                });
            }

            function deletePage(args) {
                ajax({
                    type: 'DELETE',
                    url: context + path + "page/" + args.pageId,
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                });
            }

            function deleteWidgetRating(args) {
                ajax({
                    type: 'DELETE',
                    url: context + path + "widgets/" + args.widgetId + "/rating",
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                })
            }

            function updateWidgetRating(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "widgets/" + args.widgetId + "/rating?score=" + args.score,
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                })
            }

            function createWidgetComment(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "widgets/" + args.widgetId + "/comments?text=" + escape(args.text),
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                })
            }

            function deleteWidgetComment(args) {
                ajax({
                    type: 'DELETE',
                    url: context + path + "widgets/" + args.widgetId + "/comments/" + args.commentId,
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                })
            }

            function updateWidgetComment(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "widgets/" + args.widgetId + "/comments/" + args.commentId + "?text=" + escape(args.text),
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                })
            }

            function getUsersForWidget(args) {
                ajax({
                    type: 'GET',
                    url: context + path + "widgets/" + args.widgetId + "/users",
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                        }
                    },
                    error: handleError
                })
            }

            function createWidgetTag(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "widgets/" + args.widgetId + "/tags?tagText=" + escape(args.text),
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    },
                    error: handleError
                })
            }

            function getTags(args) {
                ajax({
                    type: 'GET',
                    url: context + path + "widgets/" + args.widgetId + "/tags",
                    dataType: "json",
                    success: function (data) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(data);
                        }
                    }
                });
            }

            function getSecurityToken(args) {
                ajax({
                    type: 'GET',
                    url: context + "api/rest/opensocial/gadget?url=" + args.url + "&pageid=" + args.pageid,
                    dataType: "json",
                    success: function (data) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(data);
                        }
                    }
                });
            }

            return {
                updateWidgetRating: updateWidgetRating,
                deleteWidgetRating: deleteWidgetRating,
                saveWidgetPreferences: saveWidgetPreferences,
                saveWidgetPreference: saveWidgetPreference,
                saveWidgetCollapsedState: saveWidgetCollapsedState,
                createWidgetComment: createWidgetComment,
                updateWidgetComment: updateWidgetComment,
                deleteWidgetComment: deleteWidgetComment,
                deletePage: deletePage,
                getUsersForWidget: getUsersForWidget,
                createWidgetTag: createWidgetTag,
                getTags: getTags,
                getSecurityToken: getSecurityToken
            };
        })();

        var rpcApi = (function () {
            //Base path to RPC services
            var path = "api/rpc/";

            function moveWidgetToRegion(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/regionWidget/" + args.regionWidgetId + "/move",
                    data: {
                        newPosition: args.toIndex,
                        toRegion: args.toRegionId,
                        fromRegion: args.fromRegionId
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                    },
                    error: handleError
                });
            }

            function addWidgetToPage(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/widget/add",
                    data: {
                        widgetId: args.widgetId
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        } else {
                            // if a callback is supplied, invoke it with the regionwidget id
                            var addedWidget = result.result != undefined ? result.result.widgetId : undefined;

                            if (args.successCallback && addedWidget != undefined) {
                                args.successCallback(result.result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function deleteWidgetOnPage(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/regionWidget/" + args.regionWidgetId + "/delete",
                    data: null,
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        } else {
                            if (_.isFunction(args.successCallback)) {
                                args.successCallback();
                            }
                        }
                    },
                    error: handleError
                });
            }

            function addPage(args) {

                ajax({
                    type: 'POST',
                    url: context + path + "page/add",
                    data: {
                        pageName: args.pageName,
                        pageLayoutCode: args.pageLayoutCode
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            // check to see if a duplicate page name error occurred
                            if (result.errorCode == 'DUPLICATE_ITEM') {
                                if (typeof args.errorCallback == 'function') {
                                    args.errorCallback(args.errorLabel);
                                }
                            } else {
                                handleRpcError(result);
                            }
                        } else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            //allows us to examine the page layout, number of regions etc in js
            function getPage(args) {
                ajax({
                    type: 'GET',
                    url: context + path + "page/get",
                    data: {
                        pageId: args.pageId
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        } else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function movePage(args) {
                // the moveAfterPageId attribute could be undefined if moving
                // to the first position. In that case don't send a moveAfterPageId
                // post parameter
                var data = {};
                if (args.moveAfterPageId) {
                    data["moveAfterPageId"] = args.moveAfterPageId;
                }

                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/move",
                    data: data,
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        } else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function moveWidgetToPage(args) {
                var data = {};
                if (args.toPageId) {
                    data["toPageId"] = args.toPageId;
                    data["regionWidgetId"] = args.regionWidgetId;
                }

                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.toPageId + "/moveWidget",
                    data: data,
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        } else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function updatePagePrefs(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/update",
                    data: {
                        "name": args.title,
                        "layout": args.layout
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            if (result.errorCode == 'DUPLICATE_ITEM') {
                                if (typeof args.errorCallback == 'function') {
                                    args.errorCallback(args.errorLabel);
                                }
                            } else {
                                handleRpcError(result);
                            }
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function getPagePrefs(args) {
                ajax({
                    type: 'GET',
                    url: context + path + "page/get?pageId=" + args.pageId,
                    dataType: 'json',
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                        }
                    },
                    error: handleError
                });
            }

            //TODO RAVE-228: Create a more robust error handling system and interrogation of RPC results
            //TODO: Move dom alerts out of core code when going to angular branch
            function handleRpcError(rpcResult) {
                switch (rpcResult.errorCode) {
                    case "NO_ERROR" :
                        break;
                    case "INVALID_PARAMS":
                        alert(rpcResult);
                        break;
                    case "INTERNAL_ERROR":
                        alert(rpcResult);
                        break;
                }
            }

            function getWidgetMetadata(args) {
                var url = args.url;
                var providerType = args.providerType;

                if ((url == null || providerType == null) && typeof args.alertInvalidParams == 'function') {
                    args.alertInvalidParams();
                    return;
                }

                ajax({
                    type: 'POST',
                    url: context + path + "widget/metadata/get",
                    data: {
                        "url": url,
                        "type": providerType
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error && typeof args.errorCallback == 'function') {
                            args.errorCallback();
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function getWidgetMetadataGroup(args) {
                var url = args.url;
                var providerType = args.providerType;
                if ((url == null || providerType == null) && typeof args.alertInvalidParams == 'function') {
                    args.alertInvalidParams();
                    return;
                }

                ajax({
                    type: 'POST',
                    url: context + path + "widget/metadatagroup/get",
                    data: {
                        "url": url,
                        "type": providerType
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error && typeof args.errorCallback == 'function') {
                            args.errorCallback();
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function getAllWidgets(args) {
                ajax({
                    type: 'GET',
                    url: context + path + "widget/getall",
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function getUsers(args) {
                var offset = args.offset;

                ajax({
                    type: 'GET',
                    url: context + path + "person/get",
                    data: {"offset": offset},
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function searchUsers(args) {
                var searchTerm = args.searchTerm;
                var offset = args.offset;
                if ((searchTerm == null || searchTerm == "") && typeof args.alertEmptySearch == 'function') {
                    args.alertEmptySearch();
                    return;
                }

                ajax({
                    type: 'GET',
                    url: context + path + "person/search",
                    data: {"searchTerm": searchTerm, "offset": offset},
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function clonePageForUser(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/clone",
                    data: {
                        "userId": args.userId,
                        "pageName": args.pageName
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                        }
                    },
                    error: handleError
                });
            }

            function addMemberToPage(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/addmember",
                    data: {
                        "userId": args.userId
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function removeMemberFromPage(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/removemember",
                    data: {
                        "userId": args.userId
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function updateSharedPageStatus(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/sharestatus",
                    data: {
                        "shareStatus": args.shareStatus
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function updatePageEditingStatus(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "page/" + args.pageId + "/editstatus",
                    data: {
                        "userId": args.userId,
                        "isEditor": args.isEditor
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function addFriend(args) {
                var user = encodeURIComponent(encodeURIComponent(args.friendUsername));

                ajax({
                    type: 'POST',
                    url: context + path + "person/" + user + "/addfriend",
                    data: null,
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function removeFriend(args) {
                var user = encodeURIComponent(encodeURIComponent(args.friendUsername));

                ajax({
                    type: 'POST',
                    url: context + path + "person/" + user + "/removefriend",
                    data: null,
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function getFriends(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "person/getFriends",
                    data: null,
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function acceptFriendRequest(args) {
                var user = encodeURIComponent(encodeURIComponent(args.friendUsername));
                ajax({
                    type: 'POST',
                    url: context + path + "person/" + user + "/acceptfriendrequest",
                    data: null,
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            function addWidgetFromMarketplace(args) {
                ajax({
                    type: 'POST',
                    url: context + path + "marketplace/add",
                    data: {"url": args.url,
                        providerType: args.providerType},
                    dataType: 'json',
                    success: function (result) {
                        if (result.error) {
                            handleRpcError(result);
                        }
                        else {
                            if (typeof args.successCallback == 'function') {
                                args.successCallback(result);
                            }
                        }
                    },
                    error: handleError
                });
            }

            return {
                moveWidgetToRegion: moveWidgetToRegion,
                addWidgetToPage: addWidgetToPage,
                removeWidget: deleteWidgetOnPage,
                addPage: addPage,
                getPage: getPage,
                updatePagePrefs: updatePagePrefs,
                getPagePrefs: getPagePrefs,
                movePage: movePage,
                moveWidgetToPage: moveWidgetToPage,
                getWidgetMetadata: getWidgetMetadata,
                getWidgetMetadataGroup: getWidgetMetadataGroup,
                getUsers: getUsers,
                searchUsers: searchUsers,
                clonePageForUser: clonePageForUser,
                addMemberToPage: addMemberToPage,
                removeMemberFromPage: removeMemberFromPage,
                updateSharedPageStatus: updateSharedPageStatus,
                updatePageEditingStatus: updatePageEditingStatus,
                addFriend: addFriend,
                removeFriend: removeFriend,
                getFriends: getFriends,
                acceptFriendRequest: acceptFriendRequest,
                addWidgetFromMarketplace: addWidgetFromMarketplace,
                getAllWidgets: getAllWidgets
            };

        })();

        var exports = {
            /**
             * Rest api namespace
             */
            rest: restApi,
            /**
             * Rpc namespace
             */
            rpc: rpcApi
        };

        return exports;
    })