/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

function friendsWrapper() {
/*
 * Loads the owner, the viewer, the owner's friends, and the viewer's
 * friends and mutual friends between owner and viewer.  Response data is put into the variables owner, viewer,
 * ownerFriends, and viewerFriends, mutualFriends respectively.
 *
 *
 */

	this.loadFriends = function(){
		var req = opensocial.newDataRequest();
		req.add(req.newFetchPersonRequest(opensocial.IdSpec.PersonId.VIEWER), 'viewer');
		req.add(req.newFetchPersonRequest(opensocial.IdSpec.PersonId.OWNER), 'owner');
		var viewerFriends = opensocial.newIdSpec({ "userId" : "VIEWER", "groupId" : "FRIENDS" });
		var ownerFriends = opensocial.newIdSpec({ "userId" : "OWNER", "groupId" : "FRIENDS" });
		var opt_params = {};
        opt_params[opensocial.DataRequest.PeopleRequestFields.MAX] = 100;
        req.add(req.newFetchPeopleRequest(viewerFriends, opt_params), 'viewerFriends');
        req.add(req.newFetchPeopleRequest(ownerFriends, opt_params), 'ownerFriends');
		var params = {};
        params[opensocial.DataRequest.PeopleRequestFields.MAX] = 100;
        // Usage of isFriendsWith filter to get mutual friends. filterValue should be set to the friend with whom mutual friends is to be found.
        params[opensocial.DataRequest.PeopleRequestFields.FILTER] = opensocial.DataRequest.FilterType.IS_FRIENDS_WITH;
        params["filterValue"] = opensocial.IdSpec.PersonId.VIEWER;
        req.add(req.newFetchPeopleRequest(ownerFriends, params), 'mutualFriends');
        var app_params = {};
        app_params[opensocial.DataRequest.PeopleRequestFields.MAX] = 100;
        // Usage of hasApp filter to get list of friends who use this app.
        app_params[opensocial.DataRequest.PeopleRequestFields.FILTER] = opensocial.DataRequest.FilterType.HAS_APP;
        req.add(req.newFetchPeopleRequest(ownerFriends, app_params), 'friendsUsingApp');

        req.send(displayFriends);
	};

    function displayFriends(data) {
    	var viewer = data.get('viewer').getData();
    	var viewerFriends = data.get('viewerFriends').getData();
        var owner = data.get('owner').getData();
        var ownerFriends = data.get('ownerFriends').getData();

        html = new Array();
        html.push(owner.getDisplayName() + '\'s Friends(',ownerFriends.size(),') <br>');
        html.push('<ul>');
        ownerFriends.each(function(person) {
        	if (person.getId()) {
        		html.push('<li>', person.getDisplayName(), '</li>');
        	}
        });
        html.push('</ul>');
        if(owner.getDisplayName()!=viewer.getDisplayName()) {
        	var mutualFriends = data.get('mutualFriends').getData();
        	html.push('Mutual Friends with ',viewer.getDisplayName(),'(',mutualFriends.size(),') <br>');
        	html.push('<ul>');
        	mutualFriends.each(function(person) {
            	if (person.getId()) {
            		html.push('<li>', person.getDisplayName(), '</li>');
            	}
            });
            html.push('</ul>');
        }
        var friendsUsingApp = data.get('friendsUsingApp').getData();
    	html.push('Friends using this Widget (',friendsUsingApp.size(),') <br>');
    	html.push('<ul>');
    	friendsUsingApp.each(function(person) {
        	if (person.getId()) {
        		html.push('<li>', person.getDisplayName(), '</li>');
        	}
        });
        html.push('</ul>');
        document.getElementById('friends').innerHTML = html.join('');
        gadgets.window.adjustHeight();
    }
}