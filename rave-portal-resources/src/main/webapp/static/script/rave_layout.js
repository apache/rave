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
rave.layout = rave.layout || (function() {
    var MOVE_PAGE_DEFAULT_POSITION_IDX = -1;
    var $moveWidgetDialog;
    var $movePageDialog = $("#movePageDialog");
    var $pageForm = $("#pageForm");
    var $tab_title_input = $("#tab_title"),
        $tab_id = $("#tab_id"),
        $page_layout_input = $("#pageLayout");

    // page menu related functions
    var pageMenu = (function() {
        var $addPageButton = $("#addPageButton");
        var $menuItemEdit = $("#pageMenuEdit");
        var $menuItemDelete = $("#pageMenuDelete");
        var $menuItemMove = $("#pageMenuMove");
        var $menuItemShare = $("#pageMenuShare");
        var $menuItemRevokeShare = $("#pageMenuRevokeShare");

        /**
         * Initializes the private pageMenu closure
         * - binds click event handler to menu button
         * - binds menu item click event handlers
         * - binds body click event handler to close the menu
         */
        function init() {
            // initialize the page form validator overriding the
            // styles to wire in bootstrap styles
            $pageForm.validate({
                errorElement: 'span',
                errorClass: 'help-inline',
                highlight: function (element, errorClass) {
                    $(element).parent().parent().addClass('error');
                },
                unhighlight: function (element, errorClass) {
                    $(element).parent().parent().removeClass('error');
                }
            });

            // initialize the close button in the page menu dialog
            $("#pageMenuCloseButton").click(rave.layout.closePageDialog);

            // setup the edit page menu item
            $addPageButton.bind('click', function(event) {
                $("#pageMenuDialogHeader").html(rave.getClientMessage("page.add"));
                var $pageMenuUpdateButton = $("#pageMenuUpdateButton");
                $pageMenuUpdateButton.html(rave.getClientMessage("common.add"));
                // unbind the previous click event since we are sharing the
                // dialog between separate add/edit page actions
                $pageMenuUpdateButton.unbind('click');
                $pageMenuUpdateButton.click(rave.layout.addPage);
                $('#pageMenuDialog').on('shown', function () {
                    $("#tab_title").first().focus();
                });
                $("#pageMenuDialog").modal('show');
            });

            // setup the edit page menu item
            $menuItemEdit.bind('click', function(event) {
                 rave.api.rpc.getPagePrefs({pageId: getCurrentPageId(),
                                        successCallback: function(result) {
                                            $tab_title_input.val(result.result.name);
                                            $tab_id.val(result.result.id);
                                            $page_layout_input.val(result.result.pageLayout.code);
                                            $("#pageMenuDialogHeader").html(rave.getClientMessage("page.update"));
                                            var $pageMenuUpdateButton = $("#pageMenuUpdateButton");
                                            $pageMenuUpdateButton.html(rave.getClientMessage("common.save"));
                                            // unbind the previous click event since we are sharing the
                                            // dialog between separate add/edit page actions
                                            $pageMenuUpdateButton.unbind('click');
                                            $pageMenuUpdateButton.click(rave.layout.updatePage);
                                            $('#pageMenuDialog').on('shown', function () {
                                                $("#tab_title").first().focus();
                                            });
                                            $("#pageMenuDialog").modal('show');
                                        }
                });
            });

            // setup the delete page menu item if it is not disabled
            if (!$menuItemDelete.hasClass("menu-item-disabled")) {
                $menuItemDelete.bind('click', function(event) {
                    // send the rpc request to delete the page
                    rave.api.rest.deletePage({pageId: getCurrentPageId(), successCallback: rave.viewPage});
                });
            }

            // setup the move page menu item if it is not disabled
            if (!$menuItemMove.hasClass("menu-item-disabled")) {
                $menuItemMove.bind('click', function(event) {
                    $movePageDialog.modal('show');
                });
            }

            // setup the share page menu item
            if (!$menuItemShare.hasClass("menu-item-disabled")) {
                $menuItemShare.bind('click', function(event) {
                    rave.api.rpc.getUsers({offset: 0,
                        successCallback: function(result) {
                            rave.layout.searchHandler.dealWithUserResults(result);
                            $('#sharePageDialog').on('shown', function () {
                                $("#searchTerm").first().focus();
                            });
                            $("#sharePageDialog").modal('show');
                        }
                    });
                });
            }

            // setup the revoke share page menu item
            if (!$menuItemRevokeShare.hasClass("menu-item-disabled")) {
                $menuItemRevokeShare.bind('click', function(event) {
                    rave.layout.searchHandler.removeMemberFromPage(rave.layout.searchHandler.userId,
                        rave.layout.searchHandler.username);
                });
            }
        }

        return {
            init: init
        }
    })();

    // functions associated with the user search for sharing pages
    var searchHandler = (function() {
        var username;
        var userId;
        var pageId;
        var existingSharers;

        function setDefaults(username, userId, pageId, pageStatus){
            if(this.existingSharers == "undefined" || this.existingSharers == null){
                this.existingSharers = {};
            }
            this.username = username;
            this.userId = userId;
            this.pageId = pageId;
            if(pageStatus == "PENDING"){
                confirmPageShare();
            };
        }

        function addExistingMember(member, isEditor){
            if(this.existingSharers == "undefined" || this.existingSharers == null){
                this.existingSharers = {};
            }
            this.existingSharers[member] = isEditor;
        }

        function removeExistingMember(member){
            delete this.existingSharers[member];
        }

        function isUserAlreadyAdded(username){
            for(member in this.existingSharers){
                if(username == member){
                    return true;
                }
            }
            return false;
        }

        function isUserEditor(username){
            for(member in this.existingSharers){
                if(username == member){
                    if(this.existingSharers[member] == true){
                        return true;
                    }
                }
            }
            return false;
        }

        function removeMemberFromPage(userId, username){
            var answer;
            if(userId == rave.layout.searchHandler.userId){
                answer = confirm(rave.getClientMessage("revoke.share.current.user.confirm"));
            }else{
                answer = confirm(rave.getClientMessage("revoke.share.confirm") + " ("+username+")");
            }
            if(answer){
                $('#shareButtonHolder'+userId).hide();
                rave.api.rpc.removeMemberFromPage({pageId: this.pageId, userId: userId,
                    successCallback: function(result) {
                        rave.layout.searchHandler.removeExistingMember(username);
                        $('#pageEditorStatusHolder'+userId).empty();
                        $('#shareButtonHolder'+userId).empty();
                        $('#shareButtonHolder'+userId)
                            .append(
                                $("<a/>")
                                    .attr("href", "#")
                                    .attr("id", userId)
                                    .attr("onclick", "rave.layout.searchHandler.addMemberToPage("+userId+", '"+username+"');")
                                    .text(rave.getClientMessage("common.add"))
                            )
                            $('#shareButtonHolder'+userId).show();
                            if(userId == rave.layout.searchHandler.userId){
                                alert(rave.getClientMessage("revoke.share.current.user"));
                                // reload as page has been removed
                                document.location.href='/';
                            }else{
                                alert("(" + username + ") " + rave.getClientMessage("revoke.share"));
                            }
                    }
                });
            }
        }

        function addMemberToPage(userId, username){
            var answer = confirm(rave.getClientMessage("create.share.confirm") + " ("+username+")");
            if(answer){
                $('#shareButtonHolder'+userId).hide();
                rave.api.rpc.addMemberToPage({pageId: this.pageId, userId: userId,
                    successCallback: function(result) {
                        rave.layout.searchHandler.addExistingMember(username);
                        $('#shareButtonHolder'+userId).empty();
                        $('#shareButtonHolder'+userId)
                            .append(
                                    $("<a/>")
                                    .attr("href", "#")
                                    .attr("id", userId)
                                    .attr("onclick", "rave.layout.searchHandler.removeMemberFromPage(" +
                                        userId+", '" + username+"');")
                                    .text(rave.getClientMessage("common.remove"))
                            )
                        $('#shareButtonHolder'+userId).show();
                        $('#pageEditorStatusHolder'+userId).empty();
                        $('#pageEditorStatusHolder'+userId)
                                .append(
                                    $("<a/>")
                                    .attr("href", "#")
                                    .attr("id", userId)
                                    .attr("onclick", "rave.layout.searchHandler.addEditingRightsToMember("+userId+", '"+username+"');")
                                    .text(rave.getClientMessage("common.add"))
                                )
                        alert("(" + username + ") " + rave.getClientMessage("create.share"));
                    }
                });
            }
        }

        function removeEditingRightsFromMember(userId, username){
            var answer = confirm(rave.getClientMessage("revoke.editing.user.confirm") + " ("+username+")");
            if(answer){
                $('#pageEditorStatusHolder'+userId).hide();
                rave.api.rpc.updatePageEditingStatus({pageId: this.pageId, userId: userId, isEditor: false,
                    successCallback: function(result) {
                        rave.layout.searchHandler.existingSharers[username] = false;
                        $('#pageEditorStatusHolder'+userId).empty();
                        $('#pageEditorStatusHolder'+userId)
                                .append(
                                    $("<a/>")
                                    .attr("href", "#")
                                    .attr("id", userId)
                                    .attr("onclick", "rave.layout.searchHandler.addEditingRightsToMember("+userId+", '"+username+"');")
                                    .text(rave.getClientMessage("common.add"))
                                )
                         $('#pageEditorStatusHolder'+userId).show();
                    }
                });
            }
        }

        function addEditingRightsToMember(userId, username){
            var answer = confirm(rave.getClientMessage("grant.editing.user.confirm") + " ("+username+")");
            if(answer){
                $('#pageEditorStatusHolder'+userId).hide();
                rave.api.rpc.updatePageEditingStatus({pageId: this.pageId, userId: userId, isEditor: true,
                    successCallback: function(result) {
                        rave.layout.searchHandler.existingSharers[username] = true;
                        $('#pageEditorStatusHolder'+userId).empty();
                        $('#pageEditorStatusHolder'+userId)
                                .append(
                                    $("<a/>")
                                    .attr("href", "#")
                                    .attr("id", userId)
                                    .attr("onclick", "rave.layout.searchHandler.removeEditingRightsFromMember("+userId+", '"+username+"');")
                                    .text(rave.getClientMessage("common.remove"))
                                )
                            $('#pageEditorStatusHolder'+userId).show();
                    }
                });
            }
        }

        function acceptShare(){
            $('#confirmSharePageDialog').modal('hide');
            rave.api.rpc.updateSharedPageStatus({pageId: rave.layout.searchHandler.pageId, shareStatus: 'accepted',
                successCallback: function(result) {
                    //notification here?
                }
            });
        }

        function declineShare(){
            $('#confirmSharePageDialog').modal('hide');
            rave.api.rpc.updateSharedPageStatus({pageId: rave.layout.searchHandler.pageId, shareStatus: 'refused',
                successCallback: function(result) {
                    rave.api.rpc.removeMemberFromPage({pageId:rave.layout.searchHandler.pageId, userId:rave.layout.searchHandler.userId,
                        successCallback:function (result) {
                            document.location.href='/';
                        }});

                }
            });
        }

        function confirmPageShare(){
            $("#confirmSharePageDialog").modal('show');
        }

        function init() {
            // user clicks "search" in the find users dialog
            $("#shareSearchButton").click(function() {
                $('#shareSearchResults').empty();
                rave.api.rpc.searchUsers({searchTerm: $('#searchTerm').get(0).value, offset: 0,
                    successCallback: function(result) {
                        rave.layout.searchHandler.dealWithUserResults(result);
                    }
                });
            });

            // user clicks "clear search" in the find users dialog
            $("#clearSearchButton").click(function() {
                $('#searchTerm').get(0).value = "";
                $('#shareSearchResults').empty();
                rave.api.rpc.getUsers({offset: 0,
                    successCallback: function(result) {
                        rave.layout.searchHandler.dealWithUserResults(result);
                    }
                });
            });
        }//end of init()

        function updateParamsInString(i18nStr, itemsToReplace){
            for(var i=0;i<itemsToReplace.length;i++){
                var token = '{'+i+'}';
                i18nStr = i18nStr.replace(token, itemsToReplace[i]);
            }
            return i18nStr;
        }

        function paginate(userResults){
            var $pagingDiv = $('#shareSearchListPaging');
            $pagingDiv.empty();
            if(userResults.result.pageSize < userResults.result.totalResults){
                $pagingDiv.append('<div class="pagination"><ul id="pagingul" >');
                if(userResults.result.currentPage > 1){
                    offset = (userResults.result.currentPage - 2) * userResults.result.pageSize;
                    $('#pagingul').append('<li><a href="#" onclick="rave.api.rpc.getUsers({offset: ' +
                        offset+', successCallback: function(result)' +
                            ' {rave.layout.searchHandler.dealWithUserResults(result);}});">&lt;</a></li>');
                }
                for(var i=1;i<=userResults.result.numberOfPages;i++){
                    if(i == userResults.result.currentPage){
                        $('#pagingul').append('<li class="active"><a href="#">'+i+'</a></li>');
                    }else{
                        offset = (i - 1) * userResults.result.pageSize;
                        $('#pagingul').append('<li><a href="#" onclick="rave.api.rpc.getUsers({offset: ' +
                            offset + ', successCallback: function(result)' +
                                ' {rave.layout.searchHandler.dealWithUserResults(result);}});">' + i + '</a></li>');
                    }
                }
                if (userResults.result.currentPage < userResults.result.numberOfPages){
                    offset = (userResults.result.currentPage) * userResults.result.pageSize;
                    $('#pagingul').append('<li><a href="#" onclick="rave.api.rpc.getUsers({offset: ' +
                        offset + ', successCallback: function(result)' +
                            ' {rave.layout.searchHandler.dealWithUserResults(result);}});">&gt;</a></li>');
                }
                $pagingDiv.append('</ul></div>')
            }
        }

        function dealWithUserResults(userResults){
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
            $('#shareSearchListHeader').text(legend);
            var $targetDiv = $('#shareSearchResults');
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
                                            .text(rave.getClientMessage("common.sharing"))
                                        )
                                    )
                                    .append(
                                        $("<td/>")
                                        .addClass("booleancell")
                                        .append(
                                            $("<b/>")
                                            .text(rave.getClientMessage("common.editing.auth"))
                                        )
                                    )
                                )
                        .append(
                            $("<tbody/>")
                            .attr("id", "searchResultsBody")
                        )
                )

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
                            .attr("id", "shareButtonHolder" + this.id)
                        )
                        .append(
                            $("<td/>")
                            .attr("id", "pageEditorStatusHolder" + this.id)
                        )
                    )

                    if(this.username != rave.layout.searchHandler.username){
                        // check if already added
                        if(rave.layout.searchHandler.isUserAlreadyAdded(this.username)){
                            $('#shareButtonHolder'+this.id)
                            .append(
                                $("<a/>")
                                .attr("href", "#")
                                .attr("id", this.id)
                                .attr("onclick", "rave.layout.searchHandler.removeMemberFromPage("+this.id+", '"+this.username+"');")
                                .text(rave.getClientMessage("common.remove"))
                            )
                            if(rave.layout.searchHandler.isUserEditor(this.username)){
                                $('#pageEditorStatusHolder'+this.id)
                                .append(
                                    $("<a/>")
                                    .attr("href", "#")
                                    .attr("id", this.id)
                                    .attr("onclick", "rave.layout.searchHandler.removeEditingRightsFromMember("+this.id+", '"+this.username+"');")
                                    .text(rave.getClientMessage("common.remove"))
                                )
                            }
                            else{
                                $('#pageEditorStatusHolder'+this.id)
                                .append(
                                    $("<a/>")
                                    .attr("href", "#")
                                    .attr("id", this.id)
                                    .attr("onclick", "rave.layout.searchHandler.addEditingRightsToMember("+this.id+", '"+this.username+"');")
                                    .text(rave.getClientMessage("common.add"))
                                )
                            }
                        }else{
                            $('#shareButtonHolder'+this.id)
                            .append(
                                $("<a/>")
                                .attr("href", "#")
                                .attr("id", this.id)
                                .attr("onclick", "rave.layout.searchHandler.addMemberToPage("+this.id+", '"+this.username+"');")
                                .text(rave.getClientMessage("common.add"))
                            )
                        }
                    }

        	})//end jqueryeach
        }//end dealwithresults

        return {
            init: init,
            dealWithUserResults : dealWithUserResults,
            setDefaults : setDefaults,
            addExistingMember : addExistingMember,
            removeExistingMember : removeExistingMember,
            isUserAlreadyAdded : isUserAlreadyAdded,
            isUserEditor : isUserEditor,
            addMemberToPage : addMemberToPage,
            removeMemberFromPage : removeMemberFromPage,
            addEditingRightsToMember: addEditingRightsToMember,
            removeEditingRightsFromMember : removeEditingRightsFromMember,
            confirmPageShare : confirmPageShare,
            acceptShare : acceptShare,
            declineShare : declineShare
        }
    })();

    // widget menu related functions
    var widgetMenu = (function() {
        var $menu;
        var $menuItemMove;
        var $menuItemDelete;
        var $menuItemMaximize;

        /**
         * Hides the widget menu for a specific widget
         * @param widgetId the id of the widget to hide the menu for
         */
        function hideMenu(widgetId) {
            $("#widget-" + widgetId + "-menu").hide();
        }
        /**
         * Hides all widget menus
         */
        function hideAllMenus() {
            $(".widget-menu").hide();
        }
        /**
         * Shows the widget menu for a specific widget
         * @param widgetId the id of the widget to show the menu for
         */
        function showMenu(widgetId) {
            $("#widget-" + widgetId + "-menu").show();
        }

        /**
         * Initializes the private widgetMenu closure
         * - binds click event handler to menu button
         * - binds menu item click event handlers
         * - binds body click event handler to close the menu
         */
        function init() {
            // loop over each widget-menu and initialize the menu items
            // note: the edit prefs menu item is by default rendered disabled
            //       and it is up to the provider code for that widget to
            //       determine if the widget has preferences, and to enable
            //       the menu item
            $(".widget-menu").each(function(index, element){
                var widgetId = rave.getObjectIdFromDomId(element.id);

                // setup the move to page menu item
                $menuItemMove = $("#widget-" + widgetId + "-menu-move-item");
                if (!$menuItemMove.hasClass("menu-item-disabled")) {
                    $menuItemMove.bind('click', function(event) {
                        var regionWidgetId = rave.getObjectIdFromDomId(this.id);
                        // Clear the dropdown box; needing to do this may be a bug?
                        $('.dropdown').removeClass('open');
                        // Open the modal
                        $("#moveWidgetModal").data('regionWidgetId', regionWidgetId);
                        $("#moveWidgetModal").modal('show');

                        // prevent the menu button click event from bubbling up to parent
                        // DOM object event handlers such as the page tab click event
                        event.stopPropagation();
                    });
                }

                // setup the delete widget menu item
                $menuItemDelete  = $("#widget-" + widgetId + "-menu-delete-item");
                if (!$menuItemDelete.hasClass("menu-item-disabled")) {
                    $menuItemDelete.bind('click', function(event) {
                        var regionWidgetId = rave.getObjectIdFromDomId(this.id);

                        // invoke the rpc call to remove the widget from the page
                        rave.layout.deleteRegionWidget(regionWidgetId);

                        // prevent the menu button click event from bubbling up to parent
                        // DOM object event handlers such as the page tab click event
                        event.stopPropagation();
                    });
                }

                // setup the maximize widget menu item
                $menuItemMaximize  = $("#widget-" + widgetId + "-menu-maximize-item");
                if (!$menuItemMaximize.hasClass("menu-item-disabled")) {
                    $menuItemMaximize.bind('click', function(event) {
                        var regionWidgetId = rave.getObjectIdFromDomId(this.id);

                        // maximize the widget
                        rave.maximizeWidget({data: {id: regionWidgetId}});
                        // prevent the menu button click event from bubbling up to parent
                        // DOM object event handlers such as the page tab click event
                        event.stopPropagation();
                    });
                }

                // setup the about this widget menu item
                $menuItemAbout  = $("#widget-" + widgetId + "-menu-about-item");
                if (!$menuItemAbout.hasClass("menu-item-disabled")) {
                    $menuItemAbout.bind('click', function(event) {
                        var regionWidget = rave.getRegionWidgetById(rave.getObjectIdFromDomId(this.id));

                        // go to the widget detail page
                        rave.viewWidgetDetail(regionWidget.widgetId, getCurrentPageId());
                        // prevent the menu button click event from bubbling up to parent
                        // DOM object event handlers such as the page tab click event
                        event.stopPropagation();
                    });
                }
            });
        }

        /**
         * Enables the Edit Prefs including the Preferences view (Custom Edit Prefs)
         * menu item in the widget menu to be clicked.
         * Widget providers should use this function when initializing their
         * widgets after determining if the widget has preferences to modify.
         *
         * @param regionWidgetId the regionWidgetId of the regionWidget menu to enable
         * @param isPreferencesView boolean to indicate whether "preferences" view or the default prefs view
         */
        function enableEditPrefsMenuItem(regionWidgetId, isPreferencesView) {
            // setup the edit prefs widget menu item
            var $menuItemEditPrefs  = $("#widget-" + regionWidgetId + "-menu-editprefs-item");
            $menuItemEditPrefs.removeClass("menu-item-disabled");
            $menuItemEditPrefs.bind('click', function(event) {
                var regionWidgetId = rave.getObjectIdFromDomId(this.id);

                // show the regular edit prefs or the Custom Edit Prefs(preferences) region
                if ( isPreferencesView )
                    rave.editCustomPrefs({data: {id: regionWidgetId}});
                else
                    rave.editPrefs(regionWidgetId);
                // prevent the menu button click event from bubbling up to parent
                // DOM object event handlers such as the page tab click event
                event.stopPropagation();
            });
        }

        return {
            init: init,
            hideAll: hideAllMenus,
            hide: hideMenu,
            show: showMenu,
            enableEditPrefsMenuItem: enableEditPrefsMenuItem
        }
    })();

    /**
     * Submits the RPC call to move the widget to a new page
     */
    function moveWidgetToPage(regionWidgetId) {
        var toPageId = $("#moveToPageId").val();
        var args = { toPageId: toPageId,
                     regionWidgetId: regionWidgetId,
                     successCallback: function() { rave.viewPage(toPageId); }
                   };
        // send the rpc request to move the widget to new page
        rave.api.rpc.moveWidgetToPage(args);
    }

    /**
     * Submits the RPC call to add a new page if form validation passes
     */
    function addPage() {
        // if the form has passed validation submit the request
        if ($pageForm.valid()) {
            var newPageTitle = $tab_title_input.val();
            var newPageLayoutCode = $page_layout_input.val();

            // send the rpc request to create the new page
            rave.api.rpc.addPage({pageName: newPageTitle,
                                  pageLayoutCode: newPageLayoutCode,
                                  successCallback: function(result) {
                                      rave.viewPage(result.result.id);
                                  }
            });
        }
    }

    /**
     * Submits the RPC call to move the page to a new render sequence
     */
    function movePage() {
        var moveAfterPageId = $("#moveAfterPageId").val();
        var args = { pageId: $("#currentPageId").val(),
                     successCallback: function(result) { rave.viewPage(result.result.id); }
                   };

        if (moveAfterPageId != MOVE_PAGE_DEFAULT_POSITION_IDX) {
            args["moveAfterPageId"] = moveAfterPageId;
        }

        // send the rpc request to move the new page
        rave.api.rpc.movePage(args);
    }

    function updatePage() {
        if ($pageForm.valid()) {
            // send the rpc request to update the page
            rave.api.rpc.updatePagePrefs({pageId: $tab_id.val(),
                                            title: $tab_title_input.val(),
                                            layout: $page_layout_input.val(),
                                            successCallback: function(result) {
                                                rave.viewPage(result.result.id);
                                            }});
        }
    }

    function closePageDialog() {
        $pageForm[0].reset();
        $tab_id.val('');
        $("#pageMenuDialog").modal("hide");
    }

    /**
     * Invokes the RPC call to delete a regionWidget from a page
     *
     * @param regionWidgetId the regionWidgetId to delete
     */
    function deleteRegionWidget(regionWidgetId) {
        if (confirm(rave.getClientMessage("widget.remove_confirm"))) {
            rave.api.rpc.removeWidget({
                regionWidgetId: regionWidgetId,
                successCallback: function() {
                    // remove the widget from the dom and the internal memory map
                    $("#widget-" + this.regionWidgetId + "-wrapper").remove();
                    rave.removeWidgetFromMap(this.regionWidgetId);
                    if (rave.isPageEmpty()) {
                        rave.displayEmptyPageMessage();
                    }
                }
            });
        }
    }

    /**
     * Returns the pageId of the currently viewed page
     */
    function getCurrentPageId() {
        return $("#currentPageId").val();
    }

   /***
    * initializes the rave.layout namespace code
    */
    function init() {
        pageMenu.init();
        widgetMenu.init();
        searchHandler.init();
        // initialize the bootstrap dropdowns
        $(".dropdown-toggle").dropdown();
    }

    // public rave.layout API
    return {
        init: init,
        getCurrentPageId: getCurrentPageId,
        hideWidgetMenu: widgetMenu.hide,
        deleteRegionWidget: deleteRegionWidget,
        enableEditPrefsMenuItem: widgetMenu.enableEditPrefsMenuItem,
        addPage: addPage,
        updatePage: updatePage,
        movePage: movePage,
        closePageDialog: closePageDialog,
        moveWidgetToPage: moveWidgetToPage,
        searchHandler : searchHandler
    };
})();
