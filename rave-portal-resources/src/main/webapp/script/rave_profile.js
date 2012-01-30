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

rave.profile = rave.profile || (function() {
	function init() {
		
		$(".flip").click(function() {
			rave.api.handler.userProfileTabHandler(this);
		});
				
		//display default tag page is set to true
		var defaultTagPage = document.getElementById("defaultTagPage");
		
		if(defaultTagPage.value != null) {
			rave.api.handler.userProfileTagHandler(null, "#" + defaultTagPage.value);
		}
		//Hiding and showing event of tag pages on click of tag
		$(".profile-tag").click(function() {
			rave.api.handler.userProfileTagHandler(this, null);			
		});
		
		//edit event on click of edit button
		$("#profileEdit").click(function() {
			rave.api.handler.userProfileEditHandler(true);
		});
		
		//cancel editing phase on click of cancel button
		$("#cancelEdit").click(function() {
			rave.api.handler.userProfileEditHandler(false);
		});
	}
	
	return {
        init : init
    };
    
}());
