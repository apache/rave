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

define(["rave", "portal/rave_templates", "jqueryUi"], function (rave, raveTemplates) {
    function displayUsersOfWidget(widgetId) {
        rave.api.rest.getUsersForWidget({widgetId: widgetId, successCallback: function (data) {

            //format data for display
            _.each(data, function (person) {
                person.name = person.displayName || person.preferredName || (person.givenName + " " + person.familyName);
            });

            var markup = raveTemplates.templates['users-of-widget']({
                users: data,
                //TODO: data from dom evil! should be using gadget object to get name
                widgetName: $("#widget-" + widgetId + "-title").text().trim()
            });

            //TODO: don't use jquery ui dialogs?
            $(markup).dialog({
                modal: true,
                buttons: [
                    {text: "Close", click: function () {
                        $(this).dialog("close");
                    }}
                ]
            });
        }});
    }

    return {
        displayUsersOfWidget: displayUsersOfWidget
    }
})
