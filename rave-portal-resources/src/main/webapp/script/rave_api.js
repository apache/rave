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
rave.api = rave.api || (function() {
    function handleError(jqXhr, status, error) {
        alert("Rave encountered an error while trying to contact the server.  Please reload the page and try again. error: " + error);
    }

    var restApi = (function() {
        //Base path to RPC services
        var path = "api/rest/";

        function saveWidgetPreferences(args) {
            var preferencesData = {"preferences": []};
            for (var prefName in args.userPrefs) {
                preferencesData.preferences.push({"name":prefName, "value":args.userPrefs[prefName]});
            }

            $.ajax({
                type: 'PUT',
                url: rave.getContext() + path + "regionWidgets/" + args.regionWidgetId + "/preferences",
                data: JSON.stringify(preferencesData),
                contentType: 'application/json',
                dataType: 'json',
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            });
        }
        
        function saveWidgetPreference(args) {
            $.ajax({
                type: 'PUT',
                url: rave.getContext() + path + "regionWidgets/" + args.regionWidgetId + "/preferences/" + args.userPref.prefName,
                data: JSON.stringify({"name":args.userPref.prefName, "value": args.userPref.prefValue}),
                contentType: 'application/json',
                dataType: 'json',
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            });
        }
        
        function saveWidgetCollapsedState(args) {
            $.ajax({
                type: 'PUT',
                url: rave.getContext() + path + "regionWidgets/" + args.regionWidgetId + "/collapsed",
                data: JSON.stringify(args.collapsed),  
                contentType: 'application/json',
                dataType: 'json',
                success: function(result) {                    
                    rave.doWidgetUiCollapse({"regionWidgetId": result.entityId, 
                                             "collapsed": result.collapsed, 
                                             "successCallback": args.successCallback});                    
                },
                error: handleError
            });                        
        }
        
        function deletePage(args) {
            $.ajax({
                type: 'DELETE',
                url: rave.getContext() + path + "page/" + args.pageId,                              
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            });            
        }
        
        function deleteWidgetRating(args) {
            $.ajax({
                type: 'DELETE',
                url: rave.getContext() + path + "widgets/" + args.widgetId + "/rating",
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            })
        }
        
        function updateWidgetRating(args) {
            $.ajax({
                type: 'POST',
                url: rave.getContext() + path + "widgets/" + args.widgetId + "/rating?score=" + args.score,
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            })
        }
        
        function createWidgetComment(args) {
            $.ajax({
                type: 'POST',
                url: rave.getContext() + path + "widgets/" + args.widgetId + "/comments?text=" + escape(args.text),
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            })
        }
        
        function deleteWidgetComment(args) {
            $.ajax({
                type: 'DELETE',
                url: rave.getContext() + path + "widgets/" + args.widgetId + "/comments/" + args.commentId,
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            })
        }
        
        function updateWidgetComment(args) {
            $.ajax({
                type: 'POST',
                url: rave.getContext() + path + "widgets/" + args.widgetId + "/comments/" + args.commentId + "?text=" + escape(args.text),
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback();
                    }
                },
                error: handleError
            })
        }

        function getUsersForWidget(args) {
            $.ajax({
                type: 'GET',
                url: rave.getContext() + path + "widgets/" + args.widgetId + "/users",
                success: function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback(result);
                    }
                },
                error: handleError
            })
        }

        return {
            updateWidgetRating: updateWidgetRating,
            deleteWidgetRating: deleteWidgetRating,
            saveWidgetPreferences : saveWidgetPreferences,
            saveWidgetPreference : saveWidgetPreference,
            saveWidgetCollapsedState : saveWidgetCollapsedState,
            createWidgetComment : createWidgetComment,
            updateWidgetComment : updateWidgetComment,
            deleteWidgetComment : deleteWidgetComment,
            deletePage : deletePage,
            getUsersForWidget: getUsersForWidget
        };
    })();

    var rpcApi = (function() {
        //Base path to RPC services
        var path = "api/rpc/";

        //This method is implemented by PageApi.java.
        function moveWidgetOnPage(args) {
            var widgetObjectId = rave.getObjectIdFromDomId(args.widget.id);
            var toRegionObjectId = rave.getObjectIdFromDomId(args.targetRegion.id);
            var fromRegionObjectId = rave.getObjectIdFromDomId(args.currentRegion.id);
            //Note context must be set outside this library.  See page.jsp for example.
            $.post(rave.getContext() + path + "page/regionWidget/" + widgetObjectId + "/move",
                {
                    newPosition: args.targetIndex,
                    toRegion: toRegionObjectId,
                    fromRegion: fromRegionObjectId
                },
                function(result) {
                    if (result.error) {
                        handleRpcError(result);
                    }
                }
            ).error(handleError);
        }

        function addWidgetToPage(args) {
            $.post(rave.getContext() + path + "page/" + args.pageId + "/widget/add",
                {
                    widgetId: args.widgetId
                },
                function(result) {
                    if (result.error) {
                        handleRpcError(result);
                    } else {
                        var widgetTitle = "The widget";
                        var addedWidget = result.result != undefined ? result.result.widget : undefined;

                        if (addedWidget != undefined && addedWidget.title != undefined && addedWidget.title.length > 0) {
                            widgetTitle = addedWidget.title;
                        }
                        rave.showInfoMessage(widgetTitle + " has been added to your page");
                    }
                }).error(handleError);
        }

        function deleteWidgetOnPage(args) {
            $.post(rave.getContext() + path + "page/regionWidget/" + args.regionWidgetId + "/delete",
                null,
                function(result) {
                    if (result.error) {
                        handleRpcError(result);
                    } else {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback();
                        }
                    }
                }).error(handleError);
        }
        
        function addPage(args) {
            $.post(rave.getContext() + path + "page/add",
                {
                    pageName: args.pageName,
                    pageLayoutCode: args.pageLayoutCode
                },
                function(result) {
                    if (result.error) {
                        // check to see if a duplicate page name error occurred
                        if (result.errorCode == 'DUPLICATE_ITEM') {                        
                            $("#pageFormErrors").html("A page with that name already exists");
                        } else {                        
                            handleRpcError(result);
                        }
                    } else {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                        }
                    }
                }).error(handleError);
        }
        
        function movePage(args) {
            // the moveAfterPageId attribute could be undefined if moving
            // to the first position. In that case don't send a moveAfterPageId
            // post parameter
            var data = {};
            if (args.moveAfterPageId) {
                data["moveAfterPageId"] = args.moveAfterPageId;
            }
            
            $.post(rave.getContext() + path + "page/" + args.pageId + "/move",
                data,
                function(result) {
                    if (result.error) {                   
                        handleRpcError(result);                        
                    } else {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                        }
                    }
                }).error(handleError);
        }

        function moveWidgetToPage(args) {
            var data = {};
            if (args.toPageId) {
                data["toPageId"] = args.toPageId;
                data["regionWidgetId"] = args.regionWidgetId;
            }

            $.post(rave.getContext() + path + "page/" + args.toPageId + "/moveWidget",
                data,
                function(result) {
                    if (result.error) {
                        handleRpcError(result);
                    } else {
                        if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                        }
                    }
                }).error(handleError);
        }

        function updatePagePrefs(args) {
            $.post(rave.getContext() + path + "page/" + args.pageId + "/update",
               {"name": args.title, "layout": args.layout},
               function(result) {
                   if (result.error) {
                       handleRpcError(result);
                   }
                   else {
                       if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                       }
                   }
               }).error(handleError);
        }

        function getPagePrefs(args) {
            $.get(rave.getContext() + path + "page/get?pageId="+args.pageId,
                  null,
                  function(result) {
                    if (typeof args.successCallback == 'function') {
                        args.successCallback(result);
                    }
                  }
            );
        }

        //TODO RAVE-228: Create a more robust error handling system and interrogation of RPC results
        function handleRpcError(rpcResult) {
            switch (rpcResult.errorCode) {
                case "NO_ERROR" :
                    break;
                case "INVALID_PARAMS":
                    alert("Rave attempted to update the server with your recent changes, " +
                        " but the changes were rejected by the server as invalid.");
                    break;
                case "INTERNAL_ERROR":
                    alert("Rave attempted to update the server with your recent changes, " +
                        " but the server encountered an internal error.");
                    break;                
            }
        }

        function getWidgetMetadata(args) {
            var url = args.url;
            var providerType = args.providerType;
            if ( url == null || providerType == null ) {
                alert('Both url and type are needed to get the metadata');
                return;
            }
            $.post(rave.getContext() + path + "widget/metadata/get",
               {"url": url, "type": providerType},
               function(result) {
                   if (result.error) {
                       alert("Unable to parse Widget for its metadata.\n\nPlease verify that the url is pointing to a valid Widget of the type specified.");
                   }
                   else {
                       if (typeof args.successCallback == 'function') {
                            args.successCallback(result);
                       }
                   }
               }).error(handleError);
        }

        return {
            moveWidget : moveWidgetOnPage,
            addWidgetToPage : addWidgetToPage,
            removeWidget : deleteWidgetOnPage,
            addPage: addPage,
            updatePagePrefs: updatePagePrefs,
            getPagePrefs: getPagePrefs,
            movePage: movePage,
            moveWidgetToPage: moveWidgetToPage,
            getWidgetMetadata: getWidgetMetadata
        };

    })();

    /*
     *	Handler functions to handle modifications on user events 
     */
    var handlerApi = (function() {
    	
    	//function to handle widget rating changes
    	function widgetRatingHandler(widgetRating) {
    		
    		//retrieving the current total likes
    		var likeTotalLabel = document.getElementById("totalLikes-"+ widgetRating.widgetId);
    		var likeTotal = likeTotalLabel.getAttribute("data-rave-widget-likes"); 
    		
    		//retrieving the current total dislikes
    		var dislikeTotalLabel = document.getElementById("totalDislikes-"+ widgetRating.widgetId);
			var dislikeTotal = dislikeTotalLabel.getAttribute("data-rave-widget-dislikes"); 
    		
			//initializing temporary variables
			var incrementingTotal = -1;
			var decrementingTotal = -1;
			var curButton = "";
			var prevButton = "";
			var prevRating = -1;
			
			//check if like rating needs to be updated
    		if(widgetRating.isLike) {
    			
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
        	if(incrementingTotalLabel == likeTotalLabel) {
        		incrementingTotalLabel.setAttribute("data-rave-widget-likes", incrementingTotal);
        		incrementingTotalLabel.innerHTML = incrementingTotal;
        	}
        	else {
        		incrementingTotalLabel.setAttribute("data-rave-widget-dislikes", incrementingTotal);
        		incrementingTotalLabel.innerHTML = incrementingTotal;
        	}
        	
        	//get the value of hidden user rating 
        	var hiddenButton = document.getElementById("rate-"+ widgetRating.widgetId);
        	var userPrevRate = hiddenButton.value;

        	//if the other button in this pair was checked then ajdust its total, except in IE where
        	//the button has already toggled BEFORE the 'change' event in which case we have to assume
        	//that the user had a contrary selection prior to the change event
        	if (prevButton.get(0).getAttribute("checked") == "true" || curButton.checked == true) {
        		prevButton.get(0).setAttribute("checked", "false");
			
        		//remove the previous rating made by the user if any by checking change in userRating
        		if(parseInt(userPrevRate) == prevRating) {
        			       				
        			//update decrementing total
        			if(parseInt(decrementingTotal) - 1 > -1) {
        				decrementingTotal = parseInt(decrementingTotal) - 1;
        				if(decrementingTotalLabel == likeTotalLabel) {
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
        	if(widgetRating.isLike) {
        		hiddenButton.value = "10";
        	}
        	else{
        		hiddenButton.value = "0";
        	}
    	}
    	
    	return {
    		widgetRatingHandler : widgetRatingHandler
    	};
    	
    })();

    return {
        rest : restApi,
        rpc : rpcApi,
        handler : handlerApi
    };
})();

