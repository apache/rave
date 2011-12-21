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
                        alert(widgetTitle + " has been added to your page");
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
                        // check to see if a duplicate page name error occured
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

        return {
            moveWidget : moveWidgetOnPage,
            addWidgetToPage : addWidgetToPage,
            removeWidget : deleteWidgetOnPage,
            addPage: addPage,
            updatePagePrefs: updatePagePrefs,
            getPagePrefs: getPagePrefs,
            movePage: movePage,
            moveWidgetToPage: moveWidgetToPage
        };

    })();

    return {
        rest : restApi,
        rpc : rpcApi
    };
})();

