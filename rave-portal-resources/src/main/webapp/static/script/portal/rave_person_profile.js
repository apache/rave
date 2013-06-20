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

// All set!!

define(["jquery", "./rave_portal", "core/rave_api"], function($, ravePortal, api){
    // map of {subpage name, boolean} tracking whether or not a sub page has been viewed at least once
    var subPagesViewedStatus = {};
    var friends = new Array();
    var requestsSent = new Array();
    var friendRequestsReceived = new Array()

    function initSubPages() {
        var $tabs = $('#personProfileSubPages a[data-toggle="tab"]');
        //If the implementation does not use bootstrap tabs / subpages, skip this method
        if($tabs.length == 0){
            return;
        }

        //Make the tab identified by hash url active, defaulting to the first tab (Twitter Bootstrap)
        var activeSubPage = decodeURIComponent(location.hash).slice(1);
        activeSubPage = activeSubPageExists(activeSubPage, $tabs) ? activeSubPage : "";
        if (activeSubPage===''){
            activeSubPage = $tabs.first().text();
            location.hash = encodeURIComponent(activeSubPage);
        }

        $tabs.on('shown', function(event, ui) {
            //on tab click, change the url hash
            var page = $(this).text();
            var target = $(this).attr('href');
            var regionId = target.split('-')[1];
            location.hash = encodeURIComponent(page);

            // refresh the widgets on the sub page when selected to ensure proper sizing
            if (subPagesViewedStatus[page] == false) {
                // mark that this sub page has been viewed at least once and there is no need to refresh
                // the widgets in future views
                subPagesViewedStatus[page] = true;
            }
        });

        // build the subPageViewedStatus map to track if a given sub page has been viewed yet to determine if we need
        // to refresh the widgets upon first viewing to ensure they are sized properly.  Set the default active tab to
        // true since it will be rendered and sized properly as part of the initial page load
        $.each($tabs, function(i, el){
            var $tab = $(el);
            var page = $tab.text();
            var isActive =  (page == activeSubPage);
            subPagesViewedStatus[page] = false;
            //show the initial tab
            if(isActive){
                $tab.tab('show');
            }
        });
    }

    function activeSubPageExists(activeSubPage, tabs){
        var exists = false;
        $.each(tabs, function(i, el){
            if(el.innerHTML === activeSubPage){
                exists = true;
            }
        });

        return exists;
    }

    function updateParamsInString(i18nStr, itemsToReplace){
        for(var i=0;i<itemsToReplace.length;i++){
            var token = '{'+i+'}';
            i18nStr = i18nStr.replace(token, itemsToReplace[i]);
        }
        return i18nStr;
    }

    function dealWithUserResults(userResults){
        var currentUser = $("#addRemoveFriend").get(0).value;
        var searchTerm = $('#searchTerm').get(0).value;
        if(searchTerm == undefined || searchTerm == ""){
            $('#clearSearchButton').hide();
        }else{
            $('#clearSearchButton').show();
        }
        var legend;
        if(userResults.result.resultSet.length < 1){
            legend = ravePortal.getClientMessage("no.results.found");
        }else{
            legend = updateParamsInString(ravePortal.getClientMessage("search.list.result.x.to.y"),
                new Array(userResults.result.offset + 1, userResults.result.resultSet.length
                    + userResults.result.offset, userResults.result.totalResults));
        }
        // show the listheader
        $('#userSearchListHeader').text(legend);
        var $targetDiv = $('#userSearchResults');
        $targetDiv.empty();
        // show the paginator
        paginate(userResults);
        //now build the content
        $targetDiv
            .append(
            $("<table/>")
                .addClass("searchdialogcontent")
                .append(
                $("<tr/>")
                    .append(
                    $("<td/>")
                        .addClass("textcell")
                        .append(
                        $("<b/>")
                            .text(ravePortal.getClientMessage("common.username"))
                    )
                )
                    .append(
                    $("<td/>")
                        .addClass("booleancell")
                        .append(
                        $("<b/>")
                            .text(ravePortal.getClientMessage("common.friend.status"))
                    )
                )
            )
                .append(
                $("<tbody/>")
                    .attr("id", "searchResultsBody")
            )
        );

        $.each(userResults.result.resultSet, function() {
            $('#searchResultsBody')
                .append(
                $("<tr/>")
                    .attr("id", "searchResultRecord")
                    .append(
                    $("<td/>")
                        .text(this.username)
                )
                    .append(
                    $("<td/>")
                        .attr("id", "friendStatusButtonHolder" + this.id)
                )
            );

            if(this.username != currentUser){
                // check if already added
                if(isUserAlreadyFriend(this.username)){
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", this.entityId)
                            .click(function(){removeFriend(this.id, this.username)})
                            .text(ravePortal.getClientMessage("common.remove"))
                    );
                    // check if already sent friend request
                }else if(isFriendRequestSent(this.username)){
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", this.entityId)
                            .click(function(){removeFriendRequestSent(this.id, this.username)})
                            .text(ravePortal.getClientMessage("common.cancel.request"))
                    );
                }else if(isFriendRequestReceived(this.username)){
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", this.entityId)
                            .click(function(){acceptFriendRequest(this.entityId, this.username)})
                            .text(ravePortal.getClientMessage("common.accept")),
                        ' / ',
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", this.id)
                            .click(function(){declineFriendRequest(this.id, this.username)})
                            .text(ravePortal.getClientMessage("common.decline"))
                    );
                }else {
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", this.id)
                            .click(function(){addFriend(this.id, this.username)})
                            .text(ravePortal.getClientMessage("common.add"))
                    );
                }
            }

        });
    }

    function paginate(userResults){
        var $pagingDiv = $('#userSearchListPaging');
        $pagingDiv.empty();
        if(userResults.result.pageSize < userResults.result.totalResults){
            $pagingDiv.append('<div class="pagination"><ul id="pagingul" >');
            if(userResults.result.currentPage > 1){
                offset = (userResults.result.currentPage - 2) * userResults.result.pageSize;
                $('#pagingul').append('<li><a href="#" class="paginationLink" data-offset="' + offset + '">' + '&lt;</a></li>');
            }
            for(var i=1;i<=userResults.result.numberOfPages;i++){
                if(i == userResults.result.currentPage){
                    $('#pagingul').append('<li class="active"><a href="#">'+i+'</a></li>');
                }else{
                    offset = (i - 1) * userResults.result.pageSize;
                    $('#pagingul').append('<li><a href="#" class="paginationLink" data-offset="' + offset + '">' + i + '</a></li>');
                }
            }
            if (userResults.result.currentPage < userResults.result.numberOfPages){
                offset = (userResults.result.currentPage) * userResults.result.pageSize;
                $('#pagingul').append('<li><a href="#" class="paginationLink" data-offset="' + offset + '">' + '&gt;</a></li>');
            }
            $pagingDiv.append('</ul></div>');
        }

        $('.paginationLink').click(function(){
            api.rpc.getUsers({
                offset: $(this).data('offset'),
                successCallback: function(result){
                    dealWithUserResults(result);
                }
            })
        })
    }

    // Add a friend to the current user
    function addFriend(userId, username){
        $('#friendStatusButtonHolder'+userId).hide();
        api.rpc.addFriend({friendUsername : username,
            successCallback: function(result) {
                addFriendRequestUI(username);
                $('#friendStatusButtonHolder'+userId).empty();
                $('#friendStatusButtonHolder'+userId)
                    .append(
                    $("<a/>")
                        .attr("href", "#")
                        .attr("id", userId)
                        .click(function(){removeFriendRequestSent(userId, username)})
                        .text(ravePortal.getClientMessage("common.cancel.request"))
                );
                $('#friendStatusButtonHolder'+userId).show();
            }
        });
    }

    // Remove a friend of the current user
    function removeFriend(userId, username){
        var message = updateParamsInString(ravePortal.getClientMessage("remove.friend.confirm"),
            new Array(username));
        if(confirm(message)){
            $('#friendStatusButtonHolder'+userId).hide();
            api.rpc.removeFriend({friendUsername : username,
                successCallback: function(result) {
                    removeFriendUI(username);
                    $('#friendStatusButtonHolder'+userId).empty();
                    $('#friendStatusButtonHolder'+userId)
                        .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", userId)
                            .click(function(){addFriend(userId, username)})
                            .text(ravePortal.getClientMessage("common.add"))
                    );
                    $('#friendStatusButtonHolder'+userId).show();
                }
            });
        }
    }

    // Cancel the friend request already sent to a user
    function removeFriendRequestSent(userId, username){
        var message = updateParamsInString(ravePortal.getClientMessage("remove.friend.request.confirm"),
            new Array(username));
        if(confirm(message)){
            $('#friendStatusButtonHolder'+userId).hide();
            api.rpc.removeFriend({friendUsername : username,
                successCallback: function(result) {
                    removeFriendRequestSentUI(username);
                    $('#friendStatusButtonHolder'+userId).empty();
                    $('#friendStatusButtonHolder'+userId)
                        .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", userId)
                            .click(function(){addFriend(userId, username)})
                            .text(ravePortal.getClientMessage("common.add"))
                    );
                    $('#friendStatusButtonHolder'+userId).show();
                }
            });
        }
    }

    // Accept the friend request received by user
    function acceptFriendRequest(userId, username){
        $('#friendStatusButtonHolder'+userId).hide();
        api.rpc.acceptFriendRequest({friendUsername : username,
            successCallback: function(result) {
                removeFriendRequestReceivedUI(username);
                $('#friendStatusButtonHolder'+userId).empty();
                $('#friendStatusButtonHolder'+userId)
                    .append(
                    $("<a/>")
                        .attr("href", "#")
                        .attr("id", userId)
                        .click(function(){removeFriend(userId, username)})
                        .text(ravePortal.getClientMessage("common.remove"))
                );
                $('#friendStatusButtonHolder'+userId).show();
            }
        });
    }

    // Decline the friend request received by user
    function declineFriendRequest(userId, username){
        $('#friendStatusButtonHolder'+userId).hide();
        api.rpc.removeFriend({friendUsername : username,
            successCallback: function(result) {
                removeFriendRequestReceivedUI(username);
                $('#friendStatusButtonHolder'+userId).empty();
                $('#friendStatusButtonHolder'+userId)
                    .append(
                    $("<a/>")
                        .attr("href", "#")
                        .attr("id", userId)
                        .click(function(){addFriend(userId, username)})
                        .text(ravePortal.getClientMessage("common.add"))
                );
                $('#friendStatusButtonHolder'+userId).show();
            }
        });
    }
    // Add an item to the List of friend requests sent(maintained for the UI)
    function addFriendRequestUI(username){
        requestsSent.push(username);
    }

    // Remove a friend from the list of friends(maintained for the UI)
    function removeFriendUI(friendUsername){
        friends.splice(friends.indexOf(friendUsername),1);
    }

    // Remove a friend request from the list of friend requests sent(maintained for the UI)
    function removeFriendRequestSentUI(friendUsername){
        requestsSent.splice(requestsSent.indexOf(friendUsername),1);
    }

    // Remove a friend request from the list of friend requests received(maintained for the UI)
    function removeFriendRequestReceivedUI(friendUsername){
        friendRequestsReceived.splice(friendRequestsReceived.indexOf(friendUsername),1);
    }

    // Check if the user is already a friend
    function isUserAlreadyFriend(username){
        if(friends.indexOf(username)>=0){
            return true;
        } else {
            return false;
        }
    }

    // Check if a friend request is already sent to a particular user
    function isFriendRequestSent(username){
        if(requestsSent.indexOf(username)>=0){
            return true;
        } else {
            return false;
        }
    }

    // Check if a friend request is received from a particular user
    function isFriendRequestReceived(username){
        if(friendRequestsReceived.indexOf(username)>=0){
            return true;
        } else {
            return false;
        }
    }

    function initButtons() {
        // setup the edit button if it exists
        var $editButton = $("#profileEdit");
        if ($editButton) {
            $editButton.click(function() {
                api.handler.userProfileEditHandler(true);
            });
        }
        //user clicks add/remove friend button in the profile page
        var $friendButton = $("#addRemoveFriend");
        if ($friendButton) {
            $friendButton.click(function() {
                getFriends({successCallback : function() {
                    api.rpc.getUsers({offset: 0,
                        successCallback: function(result) {
                            dealWithUserResults(result);
                            $("#userDialog").modal('show');
                        }
                    });
                }});
            });
        }

        // user clicks "search" in the find users dialog
        $("#userSearchButton").click(function() {
            $('#userSearchResults').empty();
            api.rpc.searchUsers({searchTerm: $('#searchTerm').get(0).value, offset: 0,
                successCallback: function(result) {
                    dealWithUserResults(result);
                },
                alertEmptySearch: function(){alert(ravePortal.getClientMessage("api.rpc.empty.search.term"));}
            });
        });

        // user clicks "clear search" in the find users dialog
        $("#clearSearchButton").click(function() {
            $('#searchTerm').get(0).value = "";
            $('#userSearchResults').empty();
            api.rpc.getUsers({offset: 0,
                successCallback: function(result) {
                    dealWithUserResults(result);
                }
            });
        });

        // setup the cancel button if it exists
        var $cancelButton = $("#cancelEdit");
        if ($cancelButton) {
            $cancelButton.click(function() {
                api.handler.userProfileEditHandler(false);
            });
        }

        // When the user accepts a friend request
        var $acceptFriend = $(".acceptFriendRequest");
        if($acceptFriend) {
            $acceptFriend.click(function(e) {
                api.rpc.acceptFriendRequest({friendUsername : this.id});
                var listRequestItem = $(this).parents('.requestItem');
                var friendRequestMenu = $(listRequestItem).parent();
                $(listRequestItem).remove();
                $('.friendRequestDropdown').append('<li class="message">'+ravePortal.getClientMessage("common.accepted")+'</li>');
                $('.message').fadeOut(2000, function() {
                    $('.message').remove();
                    var childItems = $(friendRequestMenu).children('li');
                    $('.friendRequestDropdownLink').html(''+ravePortal.getClientMessage("person.profile.friend.requests")+' ('+childItems.size()+')');
                    if(childItems.size()==0)
                        $('.friendRequestDropdown').append('<li>'+ravePortal.getClientMessage("person.profile.friend.requests.none")+'</li>');
                });
                e.stopPropagation();
            });
        }

        // When the user declines a friend request
        var $declineFriend = $(".declineFriendRequest");
        if($declineFriend) {
            $declineFriend.click(function(e) {
                api.rpc.removeFriend({friendUsername : this.id});
                var listRequestItem = $(this).parents('.requestItem');
                var friendRequestMenu = $(listRequestItem).parent();
                $(listRequestItem).remove();
                $('.friendRequestDropdown').append('<li class="message">'+ravePortal.getClientMessage("common.declined")+'</li>');
                $('.message').fadeOut(2000, function() {
                    $('.message').remove();
                    var childItems = $(friendRequestMenu).children('li');
                    $('.friendRequestDropdownLink').html(''+ravePortal.getClientMessage("person.profile.friend.requests")+' ('+childItems.size()+')');
                    if(childItems.size()==0)
                        $('.friendRequestDropdown').append('<li>'+ravePortal.getClientMessage("person.profile.friend.requests.none")+'</li>');
                });
                e.stopPropagation();
            });
        }
    }

    // Gets the list of friends from the DB
    function getFriends(args) {
        api.rpc.getFriends({
            successCallback: function(result) {
                $.each(result.result.accepted, function() {
                    if(!isUserAlreadyFriend(this.username))
                        friends.push(this.username);
                });
                $.each(result.result.sent, function() {
                    if(!isFriendRequestSent(this.username))
                        requestsSent.push(this.username);
                });
                $.each(result.result.received, function() {
                    if(!isFriendRequestReceived(this.username))
                        friendRequestsReceived.push(this.username);
                });
                if(result !=null && typeof args.successCallback == 'function') {
                    args.successCallback();
                }
            }
        });
    }

    function init() {
        initSubPages();
        initButtons();
    }

    return {
        init : init,
        dealWithUserResults : dealWithUserResults,
        addFriend : addFriend,
        removeFriend : removeFriend,
        removeFriendUI : removeFriendUI,
        removeFriendRequestSent : removeFriendRequestSent,
        addFriendRequestUI : addFriendRequestUI,
        removeFriendRequestSentUI : removeFriendRequestSentUI,
        isUserAlreadyFriend : isUserAlreadyFriend,
        isFriendRequestSent : isFriendRequestSent,
        isFriendRequestReceived : isFriendRequestReceived,
        removeFriendRequestReceivedUI : removeFriendRequestReceivedUI,
        getFriends : getFriends,
        acceptFriendRequest : acceptFriendRequest,
        declineFriendRequest : declineFriendRequest
    };
})

/*
var rave = rave || {};
rave.personprofile = rave.personprofile || (function() {
    // map of {subpage name, boolean} tracking whether or not a sub page has been viewed at least once
    var subPagesViewedStatus = {};
    function initSubPages() {
        var $tabs = $('#personProfileSubPages a[data-toggle="tab"]');
        //If the implementation does not use bootstrap tabs / subpages, skip this method
        if($tabs.length == 0){
            return;
        }

        //Make the tab identified by hash url active, defaulting to the first tab (Twitter Bootstrap)
        var activeSubPage = decodeURIComponent(location.hash).slice(1);
        activeSubPage = activeSubPageExists(activeSubPage, $tabs) ? activeSubPage : "";
        if (activeSubPage===''){
            activeSubPage = $tabs.first().text();
            location.hash = encodeURIComponent(activeSubPage);
        }

        $tabs.on('shown', function(event, ui) {
            //on tab click, change the url hash
            var page = $(this).text();
            var target = $(this).attr('href');
            var regionId = target.split('-')[1];
            location.hash = encodeURIComponent(page);

            // refresh the widgets on the sub page when selected to ensure proper sizing
            if (subPagesViewedStatus[page] == false) {
                // mark that this sub page has been viewed at least once and there is no need to refresh
                // the widgets in future views
                subPagesViewedStatus[page] = true;
            }
        });

        // build the subPageViewedStatus map to track if a given sub page has been viewed yet to determine if we need
        // to refresh the widgets upon first viewing to ensure they are sized properly.  Set the default active tab to
        // true since it will be rendered and sized properly as part of the initial page load
        $.each($tabs, function(i, el){
            var $tab = $(el);
            var page = $tab.text();
            var isActive =  (page == activeSubPage);
            subPagesViewedStatus[page] = false;
            //show the initial tab
            if(isActive){
                $tab.tab('show');
            }
        });
    }

    function activeSubPageExists(activeSubPage, tabs){
        var exists = false;
        $.each(tabs, function(i, el){
            if(el.innerHTML === activeSubPage){
                exists = true;
            }
        });

        return exists;
    }

    function updateParamsInString(i18nStr, itemsToReplace){
        for(var i=0;i<itemsToReplace.length;i++){
            var token = '{'+i+'}';
            i18nStr = i18nStr.replace(token, itemsToReplace[i]);
        }
        return i18nStr;
    }

    function dealWithUserResults(userResults){
        var currentUser = $("#addRemoveFriend").get(0).value;
        var searchTerm = $('#searchTerm').get(0).value;
        if(searchTerm == undefined || searchTerm == ""){
            $('#clearSearchButton').hide();
        }else{
            $('#clearSearchButton').show();
        }
        var legend;
        if(userResults.result.resultSet.length < 1){
            legend = rave.getClientMessage("no.results.found");
        }else{
            legend = updateParamsInString(rave.getClientMessage("search.list.result.x.to.y"),
                new Array(userResults.result.offset + 1, userResults.result.resultSet.length
                    + userResults.result.offset, userResults.result.totalResults));
        }
        // show the listheader
        $('#userSearchListHeader').text(legend);
        var $targetDiv = $('#userSearchResults');
        $targetDiv.empty();
        // show the paginator
        paginate(userResults);
        //now build the content
        $targetDiv
            .append(
                $("<table/>")
                    .addClass("searchdialogcontent")
                    .append(
                        $("<tr/>")
                            .append(
                                $("<td/>")
                                    .addClass("textcell")
                                    .append(
                                        $("<b/>")
                                            .text(rave.getClientMessage("common.username"))
                                    )
                            )
                            .append(
                                $("<td/>")
                                    .addClass("booleancell")
                                    .append(
                                        $("<b/>")
                                            .text(rave.getClientMessage("common.friend.status"))
                                    )
                            )
                    )
                    .append(
                        $("<tbody/>")
                            .attr("id", "searchResultsBody")
                    )
            );

        jQuery.each(userResults.result.resultSet, function() {
            $('#searchResultsBody')
                .append(
                    $("<tr/>")
                        .attr("id", "searchResultRecord")
                        .append(
                            $("<td/>")
                                .text(this.username)
                        )
                        .append(
                            $("<td/>")
                                .attr("id", "friendStatusButtonHolder" + this.id)
                        )
                );

            if(this.username != currentUser){
                // check if already added
                if(rave.personprofile.isUserAlreadyFriend(this.username)){
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                            $("<a/>")
                                .attr("href", "#")
                                .attr("id", this.entityId)
                                .attr("onclick", "rave.personprofile.removeFriend("+this.id+", '"+this.username+"');")
                                .text(rave.getClientMessage("common.remove"))
                        );
                    // check if already sent friend request
                }else if(rave.personprofile.isFriendRequestSent(this.username)){
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                            $("<a/>")
                                .attr("href", "#")
                                .attr("id", this.entityId)
                                .attr("onclick", "rave.personprofile.removeFriendRequestSent("+this.id+", '"+this.username+"');")
                                .text(rave.getClientMessage("common.cancel.request"))
                        );
                }else if(rave.personprofile.isFriendRequestReceived(this.username)){
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                            $("<a/>")
                                .attr("href", "#")
                                .attr("id", this.entityId)
                                .attr("onclick", "rave.personprofile.acceptFriendRequest("+this.entityId+", '"+this.username+"');")
                                .text(rave.getClientMessage("common.accept")),
                            ' / ',
                            $("<a/>")
                                .attr("href", "#")
                                .attr("id", this.id)
                                .attr("onclick", "rave.personprofile.declineFriendRequest("+this.id+", '"+this.username+"');")
                                .text(rave.getClientMessage("common.decline"))
                        );
                }else {
                    $('#friendStatusButtonHolder'+this.id)
                        .append(
                            $("<a/>")
                                .attr("href", "#")
                                .attr("id", this.id)
                                .attr("onclick", "rave.personprofile.addFriend("+this.id+", '"+this.username+"');")
                                .text(rave.getClientMessage("common.add"))
                        );
                }
            }

        });
    }

    function paginate(userResults){
        var $pagingDiv = $('#userSearchListPaging');
        $pagingDiv.empty();
        if(userResults.result.pageSize < userResults.result.totalResults){
            $pagingDiv.append('<div class="pagination"><ul id="pagingul" >');
            if(userResults.result.currentPage > 1){
                offset = (userResults.result.currentPage - 2) * userResults.result.pageSize;
                $('#pagingul').append('<li><a href="#" onclick="rave.api.rpc.getUsers({offset: ' +
                    offset+', successCallback: function(result)' +
                    ' {rave.personprofile.dealWithUserResults(result);}});">&lt;</a></li>');
            }
            for(var i=1;i<=userResults.result.numberOfPages;i++){
                if(i == userResults.result.currentPage){
                    $('#pagingul').append('<li class="active"><a href="#">'+i+'</a></li>');
                }else{
                    offset = (i - 1) * userResults.result.pageSize;
                    $('#pagingul').append('<li><a href="#" onclick="rave.api.rpc.getUsers({offset: ' +
                        offset + ', successCallback: function(result)' +
                        ' {rave.personprofile.dealWithUserResults(result);}});">' + i + '</a></li>');
                }
            }
            if (userResults.result.currentPage < userResults.result.numberOfPages){
                offset = (userResults.result.currentPage) * userResults.result.pageSize;
                $('#pagingul').append('<li><a href="#" onclick="rave.api.rpc.getUsers({offset: ' +
                    offset + ', successCallback: function(result)' +
                    ' {rave.personprofile.dealWithUserResults(result);}});">&gt;</a></li>');
            }
            $pagingDiv.append('</ul></div>');
        }
    }

    // Add a friend to the current user
    function addFriend(userId, username){
        $('#friendStatusButtonHolder'+userId).hide();
        rave.api.rpc.addFriend({friendUsername : username,
            successCallback: function(result) {
                rave.personprofile.addFriendRequestUI(username);
                $('#friendStatusButtonHolder'+userId).empty();
                $('#friendStatusButtonHolder'+userId)
                    .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", userId)
                            .attr("onclick", "rave.personprofile.removeFriendRequestSent(" +
                                userId+", '" + username+"');")
                            .text(rave.getClientMessage("common.cancel.request"))
                    );
                $('#friendStatusButtonHolder'+userId).show();
            }
        });
    }

    // Remove a friend of the current user
    function removeFriend(userId, username){
        var message = updateParamsInString(rave.getClientMessage("remove.friend.confirm"),
            new Array(username));
        if(confirm(message)){
            $('#friendStatusButtonHolder'+userId).hide();
            rave.api.rpc.removeFriend({friendUsername : username,
                successCallback: function(result) {
                    rave.personprofile.removeFriendUI(username);
                    $('#friendStatusButtonHolder'+userId).empty();
                    $('#friendStatusButtonHolder'+userId)
                        .append(
                            $("<a/>")
                                .attr("href", "#")
                                .attr("id", userId)
                                .attr("onclick", "rave.personprofile.addFriend(" +
                                    userId+", '" + username+"');")
                                .text(rave.getClientMessage("common.add"))
                        );
                    $('#friendStatusButtonHolder'+userId).show();
                }
            });
        }
    }

    // Cancel the friend request already sent to a user
    function removeFriendRequestSent(userId, username){
        var message = updateParamsInString(rave.getClientMessage("remove.friend.request.confirm"),
            new Array(username));
        if(confirm(message)){
            $('#friendStatusButtonHolder'+userId).hide();
            rave.api.rpc.removeFriend({friendUsername : username,
                successCallback: function(result) {
                    rave.personprofile.removeFriendRequestSentUI(username);
                    $('#friendStatusButtonHolder'+userId).empty();
                    $('#friendStatusButtonHolder'+userId)
                        .append(
                            $("<a/>")
                                .attr("href", "#")
                                .attr("id", userId)
                                .attr("onclick", "rave.personprofile.addFriend(" +
                                    userId+", '" + username+"');")
                                .text(rave.getClientMessage("common.add"))
                        );
                    $('#friendStatusButtonHolder'+userId).show();
                }
            });
        }
    }

    // Accept the friend request received by user
    function acceptFriendRequest(userId, username){
        $('#friendStatusButtonHolder'+userId).hide();
        rave.api.rpc.acceptFriendRequest({friendUsername : username,
            successCallback: function(result) {
                rave.personprofile.removeFriendRequestReceivedUI(username);
                $('#friendStatusButtonHolder'+userId).empty();
                $('#friendStatusButtonHolder'+userId)
                    .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", userId)
                            .attr("onclick", "rave.personprofile.removeFriend(" +
                                userId+", '" + username+"');")
                            .text(rave.getClientMessage("common.remove"))
                    );
                $('#friendStatusButtonHolder'+userId).show();
            }
        });
    }

    // Decline the friend request received by user
    function declineFriendRequest(userId, username){
        $('#friendStatusButtonHolder'+userId).hide();
        rave.api.rpc.removeFriend({friendUsername : username,
            successCallback: function(result) {
                rave.personprofile.removeFriendRequestReceivedUI(username);
                $('#friendStatusButtonHolder'+userId).empty();
                $('#friendStatusButtonHolder'+userId)
                    .append(
                        $("<a/>")
                            .attr("href", "#")
                            .attr("id", userId)
                            .attr("onclick", "rave.personprofile.addFriend(" +
                                userId+", '" + username+"');")
                            .text(rave.getClientMessage("common.add"))
                    );
                $('#friendStatusButtonHolder'+userId).show();
            }
        });
    }
    // Add an item to the List of friend requests sent(maintained for the UI)
    function addFriendRequestUI(username){
        rave.personprofile.requestsSent.push(username);
    }

    // Remove a friend from the list of friends(maintained for the UI)
    function removeFriendUI(friendUsername){
        rave.personprofile.friends.splice(rave.personprofile.friends.indexOf(friendUsername),1);
    }

    // Remove a friend request from the list of friend requests sent(maintained for the UI)
    function removeFriendRequestSentUI(friendUsername){
        rave.personprofile.requestsSent.splice(rave.personprofile.requestsSent.indexOf(friendUsername),1);
    }

    // Remove a friend request from the list of friend requests received(maintained for the UI)
    function removeFriendRequestReceivedUI(friendUsername){
        rave.personprofile.friendRequestsReceived.splice(rave.personprofile.friendRequestsReceived.indexOf(friendUsername),1);
    }

    // Check if the user is already a friend
    function isUserAlreadyFriend(username){
        if(rave.personprofile.friends.indexOf(username)>=0){
            return true;
        } else {
            return false;
        }
    }

    // Check if a friend request is already sent to a particular user
    function isFriendRequestSent(username){
        if(rave.personprofile.requestsSent.indexOf(username)>=0){
            return true;
        } else {
            return false;
        }
    }

    // Check if a friend request is received from a particular user
    function isFriendRequestReceived(username){
        if(rave.personprofile.friendRequestsReceived.indexOf(username)>=0){
            return true;
        } else {
            return false;
        }
    }

    function initButtons() {
        // setup the edit button if it exists
        var $editButton = $("#profileEdit");
        if ($editButton) {
            $editButton.click(function() {
                rave.api.handler.userProfileEditHandler(true);
            });
        }
        //user clicks add/remove friend button in the profile page
        var $friendButton = $("#addRemoveFriend");
        if ($friendButton) {
            $friendButton.click(function() {
                rave.personprofile.getFriends({successCallback : function() {
                    rave.api.rpc.getUsers({offset: 0,
                        successCallback: function(result) {
                            dealWithUserResults(result);
                            $("#userDialog").modal('show');
                        }
                    });
                }});
            });
        }

        // user clicks "search" in the find users dialog
        $("#userSearchButton").click(function() {
            $('#userSearchResults').empty();
            rave.api.rpc.searchUsers({searchTerm: $('#searchTerm').get(0).value, offset: 0,
                successCallback: function(result) {
                    rave.personprofile.dealWithUserResults(result);
                }
            });
        });

        // user clicks "clear search" in the find users dialog
        $("#clearSearchButton").click(function() {
            $('#searchTerm').get(0).value = "";
            $('#userSearchResults').empty();
            rave.api.rpc.getUsers({offset: 0,
                successCallback: function(result) {
                    rave.personprofile.dealWithUserResults(result);
                }
            });
        });

        // setup the cancel button if it exists
        var $cancelButton = $("#cancelEdit");
        if ($cancelButton) {
            $cancelButton.click(function() {
                rave.api.handler.userProfileEditHandler(false);
            });
        }

        // When the user accepts a friend request
        var $acceptFriend = $(".acceptFriendRequest");
        if($acceptFriend) {
            $acceptFriend.click(function(e) {
                rave.api.rpc.acceptFriendRequest({friendUsername : this.id});
                var listRequestItem = $(this).parents('.requestItem');
                var friendRequestMenu = $(listRequestItem).parent();
                $(listRequestItem).remove();
                $('.friendRequestDropdown').append('<li class="message">'+rave.getClientMessage("common.accepted")+'</li>');
                $('.message').fadeOut(2000, function() {
                    $('.message').remove();
                    var childItems = $(friendRequestMenu).children('li');
                    $('.friendRequestDropdownLink').html(''+rave.getClientMessage("person.profile.friend.requests")+' ('+childItems.size()+')');
                    if(childItems.size()==0)
                        $('.friendRequestDropdown').append('<li>'+rave.getClientMessage("person.profile.friend.requests.none")+'</li>');
                });
                e.stopPropagation();
            });
        }

        // When the user declines a friend request
        var $declineFriend = $(".declineFriendRequest");
        if($declineFriend) {
            $declineFriend.click(function(e) {
                rave.api.rpc.removeFriend({friendUsername : this.id});
                var listRequestItem = $(this).parents('.requestItem');
                var friendRequestMenu = $(listRequestItem).parent();
                $(listRequestItem).remove();
                $('.friendRequestDropdown').append('<li class="message">'+rave.getClientMessage("common.declined")+'</li>');
                $('.message').fadeOut(2000, function() {
                    $('.message').remove();
                    var childItems = $(friendRequestMenu).children('li');
                    $('.friendRequestDropdownLink').html(''+rave.getClientMessage("person.profile.friend.requests")+' ('+childItems.size()+')');
                    if(childItems.size()==0)
                        $('.friendRequestDropdown').append('<li>'+rave.getClientMessage("person.profile.friend.requests.none")+'</li>');
                });
                e.stopPropagation();
            });
        }
    }

    // Gets the list of friends from the DB
    function getFriends(args) {
        rave.personprofile.friends = new Array();
        rave.personprofile.requestsSent = new Array();
        rave.personprofile.friendRequestsReceived = new Array();
        rave.api.rpc.getFriends({
            successCallback: function(result) {
                jQuery.each(result.result.accepted, function() {
                    if(!rave.personprofile.isUserAlreadyFriend(this.username))
                        rave.personprofile.friends.push(this.username);
                });
                jQuery.each(result.result.sent, function() {
                    if(!rave.personprofile.isFriendRequestSent(this.username))
                        rave.personprofile.requestsSent.push(this.username);
                });
                jQuery.each(result.result.received, function() {
                    if(!rave.personprofile.isFriendRequestReceived(this.username))
                        rave.personprofile.friendRequestsReceived.push(this.username);
                });
                if(result !=null && typeof args.successCallback == 'function') {
                    args.successCallback();
                }
            }
        });
    }

    function init() {
        initSubPages();
        initButtons();
    }

    return {
        init : init,
        dealWithUserResults : dealWithUserResults,
        addFriend : addFriend,
        removeFriend : removeFriend,
        removeFriendUI : removeFriendUI,
        removeFriendRequestSent : removeFriendRequestSent,
        addFriendRequestUI : addFriendRequestUI,
        removeFriendRequestSentUI : removeFriendRequestSentUI,
        isUserAlreadyFriend : isUserAlreadyFriend,
        isFriendRequestSent : isFriendRequestSent,
        isFriendRequestReceived : isFriendRequestReceived,
        removeFriendRequestReceivedUI : removeFriendRequestReceivedUI,
        getFriends : getFriends,
        acceptFriendRequest : acceptFriendRequest,
        declineFriendRequest : declineFriendRequest
    };
}());
*/
