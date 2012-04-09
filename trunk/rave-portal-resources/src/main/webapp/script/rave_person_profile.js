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
        // setup the sub page tabs
        $("#personProfileSubPages").tabs({
                select: function(event, ui) {
                    // refresh the widgets on the sub page when selected to ensure proper sizing
                    var subPageId = ui.panel.id;
                    if (subPagesViewedStatus[subPageId] == false) {
                        $("#" + subPageId + " .widget-wrapper").each(function(){
                            var regionWidget = rave.getRegionWidgetById(rave.getObjectIdFromDomId(this.id));
                            regionWidget.restore();
                        });
                        // mark that this sub page has been viewed at least once and there is no need to refresh
                        // the widgets in future views
                        subPagesViewedStatus[subPageId] = true;
                    }
                }
            }
        );

        // build the subPageViewedStatus map to track if a given sub page has been viewed yet to determine if we need
        // to refresh the widgets upon first viewing to ensure they are sized properly.  Set the default active tab to
        // true since it will be rendered and sized properly as part of the initial page load
        var activeSubPageId = $("#personProfileSubPages .ui-tabs-panel:not(.ui-tabs-hide)")[0].id;
        $("#personProfileSubPages .ui-tabs-panel").each(function(){
            subPagesViewedStatus[this.id] = (this.id == activeSubPageId);
        });
    }

    function initButtons() {
        // setup the edit button if it exists
        var $editButton = $("#profileEdit");
        if ($editButton) {
            $editButton.click(function() {
                rave.api.handler.userProfileEditHandler(true);
            });
        }

        // setup the cancel button if it exists
        var $cancelButton = $("#cancelEdit");
        if ($cancelButton) {
            $cancelButton.click(function() {
                rave.api.handler.userProfileEditHandler(false);
            });
        }
    }

	function init() {
        initSubPages();
        initButtons();
    }
	
	return {
        init : init
    };
}());
