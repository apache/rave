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
var rave = rave || {};
/**
 * Namespace that provides client access to Rave server APIs.
 * Note required jquery libraries must be imported by the containing page.
 */
rave.api = rave.api || (function () {
    //stores virtual host context of application execution
    var context = "";

    function handleError(jqXhr, status, error) {
        alert(rave.getClientMessage("api.error") + error);
    }

    var restApi = (function () {
        //Base path to RPC services
        var path = "api/rest/";

        function saveWidgetPreferences(args) {
            var preferencesData = {"preferences": []};
            for (var prefName in args.userPrefs) {
                preferencesData.preferences.push({"name": prefName, "value": args.userPrefs[prefName]});
            }

            rave.ajax({
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
            rave.ajax({
                type: 'PUT',
                url: context + path + "regionWidgets/" + args.regionWidgetId + "/preferences/" + args.userPref.prefName,
                data: JSON.stringify({"name": args.userPref.prefName, "value": args.userPref.prefValue}),
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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

        //This method is implemented by PageApi.java.
        //TODO: should be deprecated and replaced by moveWidgetToRegion
        function moveWidgetOnPage(args) {
            var widgetObjectId = rave.getObjectIdFromDomId(args.widget.id);
            var toRegionObjectId = rave.getObjectIdFromDomId(args.targetRegion.id);
            var fromRegionObjectId = rave.getObjectIdFromDomId(args.currentRegion.id);
            //Note context must be set outside this library.  See page.jsp for example.
            rave.ajax({
                type: 'POST',
                url: context + path + "page/regionWidget/" + widgetObjectId + "/move",
                data: {
                    newPosition: args.targetIndex,
                    toRegion: toRegionObjectId,
                    fromRegion: fromRegionObjectId
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

        function moveWidgetToRegion(args) {
            rave.ajax({
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
            rave.ajax({
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
                        //TODO: get rid of any dom manipulation here!
                        //TODO: this is breaking core dependency chain
                        var widgetTitle = rave.getClientMessage("widget.add_prefix");
                        var addedWidget = result.result != undefined ? result.result.widgetId : undefined;

                        if (addedWidget != undefined && addedWidget.title != undefined && addedWidget.title.length > 0) {
                        widgetTitle = addedWidget.title;
                        }
                        // if a callback is supplied, invoke it with the regionwidget id
                        if (args.successCallback && addedWidget != undefined) {
                            args.successCallback(result.result.id);
                        }
                        rave.showInfoMessage(widgetTitle + ' ' + rave.getClientMessage("widget.add_suffix"));

                        // Update Add Widget button to reflect status
                        var addWidgetButton = "#addWidget_" + args.widgetId;
                        var addedText = '<i class="icon icon-ok icon-white"></i> ' + $(addWidgetButton).data('success');

                        $(addWidgetButton).removeClass("btn-primary").addClass("btn-success").html(addedText);
                    }
                },
                error: handleError
            });
        }

        function addWidgetToPageRegion(args) {
            rave.ajax({
                type: 'POST',
                url: context + path + "page/" + args.pageId + "/widget/add/region/" + args.regionId,
                data: {
                    widgetId: args.widgetId
                },
                dataType: 'json',
                success: function (result) {
                    if (result.error) {
                        handleRpcError(result);
                    } else {
                        //TODO: move this logic
                        var widgetTitle = rave.getClientMessage("widget.add_prefix");
                        var addedWidget = result.result != undefined ? result.result.widgetId : undefined;

                        if (addedWidget != undefined && addedWidget.title != undefined && addedWidget.title.length > 0) {
                            widgetTitle = addedWidget.title;
                        }
                        // if a callback is supplied, invoke it with the regionwidget id
                        if (args.successCallback && addedWidget != undefined) {
                            args.successCallback(result.result.id);
                        }
                        rave.showInfoMessage(widgetTitle + ' ' + rave.getClientMessage("widget.add_suffix"));
                    }
                },
                error: handleError
            });
        }

        function deleteWidgetOnPage(args) {
            rave.ajax({
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

            rave.ajax({
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
                            //TODO: git rid of dom manipulation
                            $("#" + args.errorLabel).html(rave.getClientMessage("page.duplicate_name"));
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
            rave.ajax({
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

            rave.ajax({
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

            rave.ajax({
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
            rave.ajax({
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
                            //TODO: git rid of dom manipulation
                            $("#" + args.errorLabel).html(rave.getClientMessage("page.duplicate_name"));
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
            rave.ajax({
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
        function handleRpcError(rpcResult) {
            switch (rpcResult.errorCode) {
                case "NO_ERROR" :
                    break;
                case "INVALID_PARAMS":
                    alert(rave.getClientMessage("api.rpc.error.invalid_params"));
                    break;
                case "INTERNAL_ERROR":
                    alert(rave.getClientMessage("api.rpc.error.internal"));
                    break;
            }
        }

        function getWidgetMetadata(args) {
            var url = args.url;
            var providerType = args.providerType;
            if (url == null || providerType == null) {
                alert(rave.getClientMessage("api.widget_metadata.invalid_params"));
                return;
            }

            rave.ajax({
                type: 'POST',
                url: context + path + "widget/metadata/get",
                data: {
                    "url": url,
                    "type": providerType
                },
                dataType: 'json',
                success: function (result) {
                    if (result.error) {
                        alert(rave.getClientMessage("api.widget_metadata.parse_error"));
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
            if (url == null || providerType == null) {
                alert(rave.getClientMessage("api.widget_metadata.invalid_params"));
                return;
            }

            rave.ajax({
                type: 'POST',
                url: context + path + "widget/metadatagroup/get",
                data: {
                    "url": url,
                    "type": providerType
                },
                dataType: 'json',
                success: function (result) {
                    if (result.error) {
                        alert(rave.getClientMessage("api.widget_metadata.parse_error"));
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
            rave.ajax({
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

            rave.ajax({
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
            if (searchTerm == null || searchTerm == "") {
                alert(rave.getClientMessage("api.rpc.empty.search.term"));
                return;
            }

            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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

            rave.ajax({
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

            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            rave.ajax({
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
            moveWidget: moveWidgetOnPage,
            moveWidgetToRegion: moveWidgetToRegion,
            addWidgetToPage: addWidgetToPage,
            addWidgetToPageRegion: addWidgetToPageRegion,
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

    /*
     *	Handler functions to handle modifications on user events
     */
    var handlerApi = (function () {

        //function to handle widget rating changes
        function widgetRatingHandler(widgetRating) {

            //retrieving the current total likes
            var likeTotalLabel = document.getElementById("totalLikes-" + widgetRating.widgetId);
            var likeTotal = likeTotalLabel.getAttribute("data-rave-widget-likes");

            //retrieving the current total dislikes
            var dislikeTotalLabel = document.getElementById("totalDislikes-" + widgetRating.widgetId);
            var dislikeTotal = dislikeTotalLabel.getAttribute("data-rave-widget-dislikes");

            //initializing temporary variables
            var incrementingTotal = -1;
            var decrementingTotal = -1;
            var curButton = "";
            var prevButton = "";
            var prevRating = -1;

            //check if like rating needs to be updated
            if (widgetRating.isLike) {

                //set incrementing total to like total
                incrementingTotal = likeTotal;

                //set the incrementing total label to like total label
                incrementingTotalLabel = likeTotalLabel;

                //set decrementing total to dislike total
                decrementingTotal = dislikeTotal;

                //set the decrementing total label to dislike total label
                decrementingTotalLabel = dislikeTotalLabel;

                //set the current clicked button to like button
                curButton = widgetRating.widgetLikeButton;

                //set the previous clicked button to dislike button
                prevButton = widgetRating.widgetDislikeButton;

                //set the previous rating to 0 to check if dislike was clicked earlier
                prevRating = 0;
            }

            //check if dislike rating needs to be updated
            else {

                //set incrementing total to dislike total
                incrementingTotal = dislikeTotal;

                //set the incrementing total label to dislike total label
                incrementingTotalLabel = dislikeTotalLabel;

                //set decrementing total to like total
                decrementingTotal = likeTotal;

                //set the decrementing total label to like total label
                decrementingTotalLabel = likeTotalLabel;

                //set the current clicked button to dislike button
                curButton = widgetRating.widgetDislikeButton;

                //set the previous clicked button to like button
                prevButton = widgetRating.widgetLikeButton;

                //set the previous rating to 10 to check if like was clicked earlier
                prevRating = 10;
            }

            //update incrementing total
            incrementingTotal = parseInt(incrementingTotal) + 1;
            if (incrementingTotalLabel == likeTotalLabel) {
                incrementingTotalLabel.setAttribute("data-rave-widget-likes", incrementingTotal);
                incrementingTotalLabel.innerHTML = incrementingTotal;
            }
            else {
                incrementingTotalLabel.setAttribute("data-rave-widget-dislikes", incrementingTotal);
                incrementingTotalLabel.innerHTML = incrementingTotal;
            }

            //get the value of hidden user rating
            var hiddenButton = document.getElementById("rate-" + widgetRating.widgetId);
            var userPrevRate = hiddenButton.value;

            //if the other button in this pair was checked then ajdust its total, except in IE where
            //the button has already toggled BEFORE the 'change' event in which case we have to assume
            //that the user had a contrary selection prior to the change event
            if (prevButton.get(0).getAttribute("checked") == "true" || curButton.checked == true) {
                prevButton.get(0).setAttribute("checked", "false");

                //remove the previous rating made by the user if any by checking change in userRating
                if (parseInt(userPrevRate) == prevRating) {

                    //update decrementing total
                    if (parseInt(decrementingTotal) - 1 > -1) {
                        decrementingTotal = parseInt(decrementingTotal) - 1;
                        if (decrementingTotalLabel == likeTotalLabel) {
                            decrementingTotalLabel.setAttribute("data-rave-widget-likes", decrementingTotal);
                            decrementingTotalLabel.innerHTML = decrementingTotal;
                        }
                        else {
                            decrementingTotalLabel.setAttribute("data-rave-widget-dislikes", decrementingTotal);
                            decrementingTotalLabel.innerHTML = decrementingTotal;
                        }
                    }
                }

            }

            //flag this element as the currently checked one
            curButton.setAttribute("checked", "true");

            //set the user rating of the hidden field
            if (widgetRating.isLike) {
                hiddenButton.value = "10";
            }
            else {
                hiddenButton.value = "0";
            }
        }

        //TODO: git rid of dom manipulation
        //function to toggle sliding down and up of user profile tabs
        function userProfileTabHandler(profileTab) {
            //parsing profile tab id to obtain panel id
            var panelId = "#" + profileTab.id.substring(0, profileTab.id.indexOf("Tab")) + "Panel";
            $(panelId).slideToggle("slow");
        }

        //function to toggle hide and show of tag pages
        function userProfileTagHandler(profileTag, defaultTagPage) {

            //first close any tag pages if open
            $(".profile-tag-page").hide();

            //show default tag page is set true
            if (defaultTagPage != null) {
                $(defaultTagPage).show();
            }

            else {
                //extract the tag page id from profile tag id
                var tagPageId = "#" + profileTag.id + "Page";
                //show the requested tag page
                $(tagPageId).show();
            }
        }

        function userProfileEditHandler(isEdit) {
            //get the edit element
            var profileInfo = document.getElementById("profileInfo");
            //extract hidden fields through their class
            var hiddenFields = "." + profileInfo.value + "-hidden";
            //extract labels through their class
            var visibleFields = "." + profileInfo.value + "-visible";

            if (isEdit) {
                //make hidden fields visible
                $(hiddenFields).show();
                //make visible fields invisible
                $(visibleFields).hide();
            }

            else {
                //make hidden fields invisible
                $(hiddenFields).hide();
                //make visible fields visible
                $(visibleFields).show();
            }
        }

        return {
            widgetRatingHandler: widgetRatingHandler,
            userProfileTabHandler: userProfileTabHandler,
            userProfileTagHandler: userProfileTagHandler,
            userProfileEditHandler: userProfileEditHandler
        };

    })();

    function setContext(ctx) {
        context = ctx;
    }

    return {
        rest: restApi,
        rpc: rpcApi,
        handler: handlerApi,
        setContext: setContext
    };
})();

