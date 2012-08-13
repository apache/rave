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
rave.personprofile = rave.personprofile || (function() {
    // map of [subpage id, boolean] tracking whether or not a sub page has been viewed at least once
    var subPagesViewedStatus = {};
     function initSubPages() {
        //Make first tab active (Twitter Bootstrap)
        $('.nav-tabs a:first').tab('show');
        $('#personProfileSubPages a[data-toggle="tab"]').on('shown', function(event, ui) {
            // refresh the widgets on the sub page when selected to ensure proper sizing
            var subPageId = $( $(this).attr("href") ).attr("id");
            if (subPagesViewedStatus[subPageId] == false) {
                $("#" + subPageId + " .widget-wrapper").each(function(){
                    var regionWidget = rave.getRegionWidgetById(rave.getObjectIdFromDomId(this.id));
                    regionWidget.restore();
                });
                // mark that this sub page has been viewed at least once and there is no need to refresh
                // the widgets in future views
                subPagesViewedStatus[subPageId] = true;
            }
        });
        // build the subPageViewedStatus map to track if a given sub page has been viewed yet to determine if we need
        // to refresh the widgets upon first viewing to ensure they are sized properly.  Set the default active tab to
        // true since it will be rendered and sized properly as part of the initial page load
        var activeSubPageId = $("#personProfileSubPages .tab-pane.active")[0].id;
        $("#personProfileSubPages .tab-pane").each(function(){
            subPagesViewedStatus[this.id] = (this.id == activeSubPageId);
        });
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
             legend = rave.layout.searchHandler.updateParamsInString(rave.getClientMessage("search.list.result.x.to.y"),
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
                                         .text("Friend Status")
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
                         .attr("id", "friendStatusButtonHolder" + this.entityId)
                     )
                 );

                 if(this.username != currentUser){
                     // check if already added
                     if(rave.personprofile.isUserAlreadyFriend(this.username)){
                         $('#friendStatusButtonHolder'+this.entityId)
                         .append(
                             $("<a/>")
                             .attr("href", "#")
                             .attr("id", this.entityId)
                             .attr("onclick", "rave.personprofile.removeFriend("+this.entityId+", '"+this.username+"');")
                             .text(rave.getClientMessage("common.remove"))
                         );
                     // check if already send friend request
                     }else if(rave.personprofile.hasUserAlreadySentRequest(this.username)){
                         $('#friendStatusButtonHolder'+this.entityId)
                         .append(
                             $("<a/>")
                             .attr("href", "#")
                             .attr("id", this.entityId)
                             .attr("onclick", "rave.personprofile.removeFriendRequest("+this.entityId+", '"+this.username+"');")
                             .text(rave.getClientMessage("common.cancel.request"))
                         );
                     }else {
                         $('#friendStatusButtonHolder'+this.entityId)
                         .append(
                             $("<a/>")
                             .attr("href", "#")
                             .attr("id", this.entityId)
                             .attr("onclick", "rave.personprofile.addFriend("+this.entityId+", '"+this.username+"');")
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
             rave.api.rpc.addFriend({friendId : userId,
                 successCallback: function(result) {
                     rave.personprofile.addExistingFriendRequest(username);
                     $('#friendStatusButtonHolder'+userId).empty();
                     $('#friendStatusButtonHolder'+userId)
                         .append(
                                 $("<a/>")
                                 .attr("href", "#")
                                 .attr("id", userId)
                                 .attr("onclick", "rave.personprofile.removeFriendRequest(" +
                                     userId+", '" + username+"');")
                                 .text(rave.getClientMessage("common.cancel.request"))
                         );
                     $('#friendStatusButtonHolder'+userId).show();
                 }
             });
     }

     // Remove a friend of the current user
     function removeFriend(userId, username){
    	 var message = rave.layout.searchHandler.updateParamsInString(rave.getClientMessage("remove.friend.confirm"),
                 new Array(username));
         if(confirm(message)){
	         $('#friendStatusButtonHolder'+userId).hide();
	         rave.api.rpc.removeFriend({friendId : userId,
	             successCallback: function(result) {
	                 rave.personprofile.removeExistingFriend(username);
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
     function removeFriendRequest(userId, username){
    	 var message = rave.layout.searchHandler.updateParamsInString(rave.getClientMessage("remove.friend.request.confirm"),
                 new Array(username));
         if(confirm(message)){
	         $('#friendStatusButtonHolder'+userId).hide();
	         rave.api.rpc.removeFriend({friendId : userId,
	             successCallback: function(result) {
	                 rave.personprofile.removeExistingFriendRequest(username);
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
     // List with existing friend requests (maintained for the UI)
     function addExistingFriendRequest(username){
    	 rave.personprofile.existingRequests.push(username);
     }

     // Remove a friend from the list of existing friends(maintained for the UI)
     function removeExistingFriend(friendUsername){
         rave.personprofile.existingFriends.splice(rave.personprofile.existingFriends.indexOf(friendUsername),1);
     }

     // Remove a friend request from the list of existing friend requests(maintained for the UI)
     function removeExistingFriendRequest(friendUsername){
         rave.personprofile.existingRequests.splice(rave.personprofile.existingRequests.indexOf(friendUsername),1);
     }

     // Check if the user is already a friend
     function isUserAlreadyFriend(username){
         if(rave.personprofile.existingFriends.indexOf(username)>=0){
             return true;
         } else {
        	 return false;
         }
     }

     // Check if a friend request is already sent to a particular user
     function hasUserAlreadySentRequest(username){
    	 if(rave.personprofile.existingRequests.indexOf(username)>=0){
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
    }

    // Gets the list of friends from the DB when ever the user
    function getFriends(args) {
   		rave.personprofile.existingFriends = new Array();
    	rave.personprofile.existingRequests = new Array();
    	rave.api.rpc.getFriends({
            successCallback: function(result) {
            	jQuery.each(result.result.accepted, function() {
            		if(!rave.personprofile.isUserAlreadyFriend(this.username))
            			rave.personprofile.existingFriends.push(this.username);
            	});
            	jQuery.each(result.result.pending, function() {
            		if(!rave.personprofile.hasUserAlreadySentRequest(this.username))
            			rave.personprofile.existingRequests.push(this.username);
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
        removeExistingFriend : removeExistingFriend,
        removeFriendRequest : removeFriendRequest,
        addExistingFriendRequest : addExistingFriendRequest,
        removeExistingFriendRequest : removeExistingFriendRequest,
        isUserAlreadyFriend : isUserAlreadyFriend,
        hasUserAlreadySentRequest :hasUserAlreadySentRequest,
        getFriends : getFriends
    };
}());
