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
 * Namespace that provides client access to Rave server APIs
 */
rave.api = rave.api || (function() {
    var rpcApi = (function() {
        var path = "api/rpc/";

        function moveWidgetOnPage(args) {
            $.post(rave.getContext() + path + "page/regionWidget/" + rave.getObjectIdFromDomId(args.widget.id) + "/move",
                    {
                        newPosition: args.targetIndex,
                        toRegion: rave.getObjectIdFromDomId(args.targetRegion.id),
                        fromRegion: rave.getObjectIdFromDomId(args.currentRegion.id)
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
                        }
                    }).error(handleError);
        }

        function handleError(jqXhr, status, error) {
            alert("Rave encountered an error while trying to contact the server.  Please reload the page and try again. error: " + error);
        }

        //TODO: Create a more robust error handling system and interrogation of RPC results
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
            console.log(rpcResult.errorMessage);
        }

        return {
            moveWidget : moveWidgetOnPage,
            addWidgetToPage : addWidgetToPage
        }

    })();

    return {
        rpc : rpcApi
    };
})();

