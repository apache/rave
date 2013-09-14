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

define(["jquery", "underscore", "rave",
    "portal/rave_portal", "portal/rave_backbone", "portal/rave_models", "portal/rave_templates",
    "jqueryUi"],
    function ($, _, rave, ravePortal, raveBackbone, raveModels, raveTemplates) {
        var exports = {};

        /*
         Build rave client side views
         */
        var views = exports.views = {};

        var App = raveBackbone.View.extend({
            models: {
                page: raveModels.currentPage
            },

            initialize: function () {
                this.models.page.on('acceptShare', ravePortal.viewPage);
                this.models.page.on('declineShare', function () {
                    document.location.href = rave.getContext();
                });
            }
        });

        views.app = new App();


        function initializePageSharing() {
            /*
             View for managing sharing / cloning of pages
             */
            var PageSharingModalView = raveBackbone.View.extend({
                template: raveTemplates.templates['user-search-view'],
                //the bootstrap modal div
                modalDiv: $('#sharePageDialog'),
                //attach point for this view
                container: $('#sharePageDialogContent'),

                models: {
                    page: raveModels.currentPage,
                    users: raveModels.users
                },

                initialize: function () {
                    var page = this.models.page;
                    var users = this.models.users;

                    this.constructor.__super__.initialize.apply(this);

                    page.on('share', this.flash, this);
                    page.on('error', this.handleError, this);

                    //whenever the modal is displayed reset the view
                    this.modalDiv.on('show', function () {
                        users.fetchPage(1);
                    });

                    //extend users toViewModel function to include share properties
                    this.models.users.toViewModel = _.wrap(this.models.users.toViewModel, function (toViewModel) {
                        var model = toViewModel.apply(this);

                        _.each(model.users, function (user) {
                            user.isOwner = page.isUserOwner(user.id);
                            user.hasShare = page.isUserMember(user.id);
                            user.hasEdit = page.isUserEditor(user.id);
                        });

                        return model;
                    });

                    this.container.html(this.$el);
                },

                events: {
                    'click #shareSearchButton': 'search',
                    'keypress #searchTerm': 'search',
                    'click #clearSearchButton': 'clearSearch',
                    'click #pagingul a': 'page',
                    'click .searchResultRecord a': 'shareAction'
                },

                search: function (e) {
                    //allow search function to trigger from enter keypress or button click
                    if (e.which == 13 || _.isUndefined(e.which)) {
                        var term = $('#searchTerm', this.$el).val();

                        this.models.users.filter(term);
                    }
                },

                clearSearch: function (e) {
                    this.models.users.filter(null);
                },

                page: function (e) {
                    var page = $(e.target).data('pagenumber');

                    this.models.users.fetchPage(page);
                },

                //manages any add / remove share, editor or clone actions
                shareAction: function (e) {
                    var userId = $(e.target).data('userid');
                    var action = $(e.target).data('action');

                    this.models.page[action](userId);
                },

                //flash success messages
                flash: function (event, userId) {
                    var eventsToMessages = {
                        'member:add': 'create.share',
                        'member:remove': 'revoke.share',
                        'editor:add': '',
                        'editor:remove': '',
                        'clone': 'success.clone.page'
                    }

                    var msg = eventsToMessages[event];

                    if (msg) {
                        var message = ravePortal.getClientMessage(msg);
                        var user = this.models.users.get(userId);
                        ravePortal.showInfoMessage('(' + user.get('username') + ') ' + message);
                    }
                },

                //TODO: deal with errors better. Until we have a better api or there is another view for this modal,
                //manually manage the form
                handleError: function (errorCode, userId) {
                    var self = this;

                    if (errorCode == 'DUPLICATE_ITEM') {
                        this.modalDiv.modal('hide');
                        $("#pageMenuDialogHeader").html(ravePortal.getClientMessage("page.update"));
                        $("#pageFormErrors").html(ravePortal.getClientMessage("page.duplicate_name"));
                        $("#pageLayoutGroup").hide();
                        var $pageMenuUpdateButton = $("#pageMenuUpdateButton");
                        $pageMenuUpdateButton.html(ravePortal.getClientMessage("common.save"));
                        // unbind the previous click event since we are sharing the
                        // dialog between separate add/edit page actions
                        $pageMenuUpdateButton.unbind('click');
                        $pageMenuUpdateButton.click(function () {
                            var $pageForm = $("#pageForm");
                            var $tab_title_input = $("#tab_title");
                            if ($pageForm.valid()) {
                                self.models.page.cloneForUser(userId, $tab_title_input.val());
                                $("#pageMenuDialog").modal('hide');
                            }
                        });
                        $('#pageMenuDialog').on('shown', function () {
                            $("#tab_title").first().focus();
                        });
                        //
                        $("#pageMenuDialog").modal('show');
                    } else {
                        $("#pageMenuDialog").modal('hide');
                        alert(ravePortal.getClientMessage("api.rpc.error.internal"));
                    }
                }
            });
            views.pageSharingModal = new PageSharingModalView();
        }

        function displayEmptyPageMessage() {
            $("#emptyPageMessageWrapper").removeClass("hidden");
        }

        exports.displayEmptyPageMessage = displayEmptyPageMessage;

        function showEmptyDisplay() {
            if (_.isEmpty(rave.getWidgets())) {
                displayEmptyPageMessage();
            }
        }

        function getNonLockedRegions() {
            return $(".region:not(.region-locked)");
        }

        function setupDragAndDrop() {
            var uiState = {
                widget: null,
                currentRegion: null,
                targetRegion: null,
                targetIndex: null
            };

            getNonLockedRegions().sortable({
                connectWith: '.region', // defines which regions are dnd-able
                scroll: true, // whether to scroll the window if the user goes outside the areas
                opacity: 0.5, // the opacity of the object being dragged
                revert: true, // smooth snap animation
                cursor: 'move', // the cursor to show while dnd
                handle: '.widget-title-bar-draggable', // the draggable handle
                forcePlaceholderSize: true, // size the placeholder to the size of the widget
                tolerance: 'pointer', // change dnd drop zone on mouse-over
                start: dragStart, // event listener for drag start
                stop: dragStop, // event listener for drag stop
                over: dragOver // event listener for drag over
            });

            function dragStart(event, ui) {
                adjustRowRegionsHeights();
                var $regions = getNonLockedRegions();
                // highlight the draggable regions
                $regions.addClass("regionDragging");
                // remove invisible border so nothing moves
                $regions.removeClass("regionNonDragging");

                var widgetEl = ui.item.children(".widget").get(0);
                uiState.widget = rave.getWidget(ravePortal.getObjectIdFromDomId(widgetEl.id));
                uiState.currentRegion = ravePortal.getObjectIdFromDomId(ui.item.parent().get(0).id);

                //for every drag operation, create an overlay for each iframe
                //to prevent the iframe from intercepting mouse events
                //which kills drag performance
                $(".widget").each(function (index, element) {
                    addOverlay($(element));
                });
            }

            function dragStop(event, ui) {
                var $regions = getNonLockedRegions();

                // reset padding to 0 after drag on all rows
                if ($(".widgetRow").length) {
                    var rows = $regions.find(".widgetRow");
                    rows.each(resetRowsRegionsHeight);
                }

                // remove the draggable regions visible border
                $regions.removeClass("regionDragging");
                // add an invisible border so nothing moves
                $regions.addClass("regionNonDragging");

                $(".dnd-overlay").remove();
                //Fixes a bug where the jQuery style attribute remains set in chrome
                ui.item.attr("style", "");
                uiState.targetRegion = ravePortal.getObjectIdFromDomId(ui.item.parent().get(0).id);
                uiState.targetIndex = ui.item.index();

                uiState.widget.moveToRegion(uiState.currentRegion, uiState.targetRegion, uiState.targetIndex);

                clearState();
            }

            function clearState() {
                uiState.currentRegion = null;
                uiState.targetRegion = null;
                uiState.targetIndex = null;
                uiState.widget = null;
            }

            function dragOver(event, ui) {
                adjustRowRegionsHeights();
            }

            // dynamically adjust heights of all regions
            function adjustRowRegionsHeights() {
                // handle region areas for upper rows
                if ($(".upperRow").length) {
                    var rows = $(".regions").find(".upperRow");
                    rows.each(adjustUpperRowRegionsHeight);
                }

                // handle region areas for the bottom row
                if ($(".bottomRow").length) {
                    var row = $(".regions").find(".bottomRow");
                    adjustBottomRowRegionsHeight(row)
                }
            }

            // adjusts the padding-bottom value of all regions in bottom row to either fill the empty space or
            // act as an upper row
            function adjustBottomRowRegionsHeight(row) {
                resetRowsRegionsHeight(row);
                var bodyHeight = $('body').outerHeight();
                var windowHeight = $(window).height();
                // Instances where no scroll bar currently exists
                if (windowHeight >= bodyHeight) {
                    var pageHeight = $("#pageContent").outerHeight();
                    var headerHeight = bodyHeight - pageHeight;
                    var upperRegionsMaxHeights = 0;
                    if ($(".upperRow").length) {
                        var rows = $(".regions").find(".upperRow");
                        for (var x = 0; x < rows.length; x++) {
                            var rowMaxHeight = getRowRegionsMaxHeight(rows.get(x));
                            upperRegionsMaxHeights = upperRegionsMaxHeights + rowMaxHeight;
                        }
                    }
                    // determine maximum size possible for bottom region
                    // 50 px of buffer also removed to prevent scroll-bar from appearing in any cases
                    var bottomPadding = (windowHeight - 50) - (upperRegionsMaxHeights + headerHeight);

                    setRowsRegionsHeight(row, bottomPadding);
                }
                // Instances where scroll bar currently exists, can default to upper row behavior
                else {
                    adjustUpperRowRegionsHeight(row);
                }
                // refresh sortables cached positions
                getNonLockedRegions().sortable("refreshPositions");
            }


            // adjusts the padding-bottom value of all regions in upper rows to match the value of the
            // tallest region in the row
            function adjustUpperRowRegionsHeight(row) {
                // when called by each, first argument is a number instead of a row value
                var row = (typeof row === 'number') ? $(this) : row;

                resetRowsRegionsHeight(row);

                // sets total region height to the height of tallest region
                setRowsRegionsHeight(row, getRowRegionsMaxHeight(row));

                // refresh sortables cached positions
                getNonLockedRegions().sortable("refreshPositions");
            }

            // Returns the height of the tallest region in row, minimum 100 px
            function getRowRegionsMaxHeight(row) {
                var rowChildren = $(row).children();
                var maxHeight = 100;
                for (var x = 0; x < rowChildren.length; x++) {
                    if ($(rowChildren.get(x)).outerHeight() > maxHeight) {
                        maxHeight = $(rowChildren.get(x)).outerHeight();
                    }
                }
                return maxHeight;
            }

            // Restores the padding-bottom value to the original for all regions in given row
            function resetRowsRegionsHeight(row) {
                // when called by each, first argument is a number instead of a row value
                var row = (typeof row === 'number') ? $(this) : row;

                var rowChildren = $(row).children();
                for (var x = 0; x < rowChildren.length; x++) {
                    // reset to 5, the initial value before dragging
                    $(rowChildren.get(x)).css("padding-bottom", 5);
                }
            }

            // Sets the padding-bottom value, so that the total height is the given value for all regions in given row
            function setRowsRegionsHeight(row, maxHeight) {
                var rowChildren = $(row).children();
                for (var x = 0; x < rowChildren.length; x++) {
                    if ($(rowChildren.get(x)).outerHeight() != maxHeight) {
                        var defaultPadding = parseInt($(rowChildren.get(x)).css("padding-bottom").replace("px", ""));
                        $(rowChildren.get(x)).css("padding-bottom", (defaultPadding + maxHeight - $(rowChildren.get(x)).outerHeight()));
                    }
                }
            }

            function addOverlay(jqElm) {
                var overlay = $('<div></div>');
                var styleMap = {
                    position: "absolute",
                    height: jqElm.height(),
                    width: jqElm.width(),
                    'z-index': 10,
                    opacity: 0.7,
                    background: "#FFFFFF"
                };

                // style it and give it the marker class
                $(overlay).css(styleMap);
                $(overlay).addClass("dnd-overlay");
                // add it to the dom before the iframe so it covers it
                jqElm.prepend(overlay[0]);
            }
        }

        function showWidgetPrefs(widget) {
            var WIDGET_PREFS_INPUT_REQUIRED_CLASS = "widget-prefs-input-required";
            var WIDGET_PREFS_INPUT_FAILED_VALIDATION = "widget-prefs-input-failed-validation";

            var PIPE_REGEX = /\|/g;

            function WIDGET_PREFS_CONTENT(regionWidgetId) {
                return "widget-" + regionWidgetId + "-prefs-content";
            }

            var widgetDefinition = widget;
            var userPrefs = widgetDefinition.metadata.userPrefs;
            var hasRequiredUserPrefs = false;

            if (widgetDefinition.metadata.views.preferences) {
                widget._view = rave.renderView('preferences', widgetDefinition);
            } else {
                //format the data for display
                _.each(userPrefs, function (pref) {
                    //find current value of each pref
                    pref.value = widgetDefinition.userPrefs[pref.name] || pref.defaultValue;

                    if (pref.required) {
                        hasRequiredUserPrefs = true;
                    }

                    //for ENUM preferences find which option should be selected
                    if (pref.dataType == 'ENUM') {
                        var selectedPref = _.find(pref.orderedEnumValues, function (option) {
                            return option.value == pref.value;
                        });
                        selectedPref.selected = true;
                    }
                    if (pref.dataType == 'LIST') {
                        pref.value = pref.value.replace(PIPE_REGEX, "\n");
                    }

                    //if pref.dataType = "LIST", add a flag pref.LIST to #if against in the template
                    pref[pref.dataType] = true;

                });

                var markup = raveTemplates.templates['widget-preferences']({
                    userPrefs: userPrefs,
                    hasRequiredUserPrefs: hasRequiredUserPrefs
                });
                var $markup = $(markup);

                $('.prefs-save-button', $markup).click({id: widgetDefinition.regionWidgetId},
                    saveEditPrefsAction);

                $('.prefs-cancel-button', $markup).click({id: widgetDefinition.regionWidgetId},
                    cancelEditPrefsAction);

                var prefsElement = $("#widget-" + widgetDefinition.regionWidgetId + "-prefs-content");
                prefsElement.html($markup);

                prefsElement.show();
            }

            function saveEditPrefsAction(args) {
                var definition = widget;
                var prefsElement = $("#" + WIDGET_PREFS_CONTENT(definition.regionWidgetId));

                var updatedPrefs = {};
                var hasValidationErrors = false;
                // note that validation of "required" prefs is only done for text and
                // textarea types, since those represent STRING and LIST inputs, and
                // are the only inputs that could potentially contain empty data
                prefsElement.find("*").filter(":input").each(function (index, element) {
                    switch (element.type) {
                        case "text":
                            if (!validatePrefInput(element)) {
                                hasValidationErrors = true;
                                $(element).addClass(WIDGET_PREFS_INPUT_FAILED_VALIDATION);
                            } else {
                                updatedPrefs[element.name] = $(element).val();
                                $(element).removeClass(WIDGET_PREFS_INPUT_FAILED_VALIDATION);
                            }
                            break;
                        case "select-one":
                        case "hidden":
                            updatedPrefs[element.name] = $(element).val();
                            break;
                        case "checkbox":
                            updatedPrefs[element.name] = $(element).is(':checked').toString();
                            break;
                        case "textarea":
                            if (!validatePrefInput(element)) {
                                hasValidationErrors = true;
                                $(element).addClass(WIDGET_PREFS_INPUT_FAILED_VALIDATION);
                            } else {
                                var valuesToPersist = [];
                                var textareaValues = $(element).val().split("\n");
                                for (var i = 0; i < textareaValues.length; i++) {
                                    var value = $.trim(textareaValues[i]);
                                    if (value.length > 0) {
                                        valuesToPersist.push(value);
                                    }
                                }
                                updatedPrefs[element.name] = valuesToPersist.join("|");
                                $(element).removeClass(WIDGET_PREFS_INPUT_FAILED_VALIDATION);
                            }
                            break;
                    }
                });

                // check to see if one or more input prefs had validation errors
                if (hasValidationErrors) {
                    // focus on the first input that has validation errors
                    prefsElement.find("." + WIDGET_PREFS_INPUT_FAILED_VALIDATION).first().focus();
                } else {
                    if (ravePortal.isPageEditor()) {
                        widget.savePreferences(updatedPrefs);
                        widget.render(widget._el);
                    }

                    prefsElement.html("");
                    prefsElement.hide();
                }
            }

            function cancelEditPrefsAction(args) {
                var prefsElement = $("#" + WIDGET_PREFS_CONTENT(args.data.id));
                prefsElement.html("");
                prefsElement.hide();
            }

            function validatePrefInput(element) {
                var isValid = true;
                var jqEl = $(element);
                // if the input is required verify it's trimmed input length is > 0
                if (jqEl.hasClass(WIDGET_PREFS_INPUT_REQUIRED_CLASS)) {
                    isValid = $.trim(jqEl.val()).length > 0;
                }

                return isValid;
            }
        }

        function registerHomeView() {

            rave.RegionWidget.extend({
                renderError: function (el, errors) {
                    el.innerHTML = ravePortal.getClientMessage("opensocial.render_error") + "<br /><br />" + errors;
                }
            });

            var HomeView = function (widget) {
                this.widget = widget;

                var regionWidgetId = widget.regionWidgetId;

                this.$chrome = $('#widget-' + regionWidgetId + '-wrapper');
                this.$minimizeIcon = $("#widget-" + regionWidgetId + "-min");
                this.$toggleCollapseIcon = $("#widget-" + regionWidgetId + "-collapse");
                this.$cogIcon = $('#widget-'+regionWidgetId+'-menu-button');
                this.$menuItemMove = $("#widget-" + regionWidgetId + "-menu-move-item");
                this.$menuItemDelete = $("#widget-" + regionWidgetId + "-menu-delete-item");
                this.$menuItemMaximize = $("#widget-" + regionWidgetId + "-menu-maximize-item");
                this.$menuItemAbout = $("#widget-" + regionWidgetId + "-menu-about-item");
                this.$menuItemComment = $("#widget-" + regionWidgetId + "-menu-comment-item");
                this.$menuItemRate = $("#widget-" + regionWidgetId + "-menu-rate-item");
                this.$menuItemEditPrefs = $("#widget-" + regionWidgetId + "-menu-editprefs-item");
                this.$widgetSite = $("#widget-" + regionWidgetId + "-body");
                this.$title = $("#widget-" + regionWidgetId + "-title");
            }

            HomeView.prototype.render = function (widget) {
                var regionWidgetId = this.widget.regionWidgetId;
                var widgetId = this.widget.widgetId;
                var self = this;

                doCollapseExpand();
                cleanupFromCanvas();

                function cleanupFromCanvas() {
                    $(".dnd-overlay, .canvas-overlay").remove();
                    getNonLockedRegions().sortable("option", "disabled", false);
                    // display the widget in normal view
                    $("#widget-" + regionWidgetId + "-wrapper").removeClass("widget-wrapper-canvas").addClass("widget-wrapper");
                    // hide the widget minimize button
                    $("#widget-" + regionWidgetId + "-min").hide();
                    // show the widget menu
                    $("#widget-" + regionWidgetId + "-widget-menu-wrapper").show();
                    // show the collapse/restore toggle icon
                    $("#widget-" + regionWidgetId + "-collapse").show();
                    // if the widget is collapsed execute the collapse function
                    // otherwise execute the minimize function
                    return false;
                }

                function addIframeOverlays(event) {
                    //e.preventDefault();

                    var i = 0;
                    $(".widget").each(function () {
                        var W = $(this).outerWidth(),
                            H = $(this).outerHeight(),
                            X = $(this).position().left,
                            Y = $(this).position().top;

                        $(this).after('<div class="iframe-overlay" />');
                        $(this).next('.iframe-overlay').css({
                            width: W,
                            height: H,
                            position: "absolute",
                            top: Y,
                            left: X
                        });

                    });

                    $('.iframe-overlay').click(function () {
                        self.$cogIcon.dropdown('toggle');
                        $(".iframe-overlay").remove();
                    })

                    // Remove any overlays onclick of all the things!!!
                    $("*:not(.dropdown-toggle)").on("click", function () {
                        $(".iframe-overlay").remove();
                    });
                }

                function minimize() {
                    self.widget.render('home');
                }

                function maximize() {
                    self.widget.render('canvas', {view: 'canvas'});
                }

                function toggleCollapse() {
                    var hidden = self.widget.collapsed
                    if (hidden) {
                        self.widget.show();
                    }
                    else {
                        self.widget.hide();
                    }
                    doCollapseExpand();
                    return false;
                }

                function doCollapseExpand() {
                    var expanded = !widget.collapsed
                    $(self.$widgetSite).toggle(expanded);
                    $('i', self.$toggleCollapseIcon).toggleClass('icon-chevron-down', expanded);
                    $('i', self.$toggleCollapseIcon).toggleClass('icon-chevron-up', !expanded);
                }

                function showMovePageDialog() {
                    // Clear the dropdown box; needing to do self may be a bug?
                    $('.dropdown').removeClass('open');
                    // Open the modal
                    $("#moveWidgetModal").data('regionWidgetId', regionWidgetId);
                    $("#moveWidgetModal").modal('show');

                    return false;
                }

                function deleteWidget() {
                    self.widget.close();
                    return false;
                }

                function aboutWidget() {
                    ravePortal.viewWidgetDetail(widgetId, rave.getPage().id);
                    return false;
                }

                function commentOnWidget() {
                    ravePortal.viewWidgetDetail(widgetId, rave.getPage().id, 'widgetComments');
                    return false;
                }

                function rateWidget() {
                    ravePortal.viewWidgetDetail(widgetId, rave.getPage().id, 'widgetRatings');
                    return false;
                }

                function showPrefsPane() {
                    if(widget._view.name !== "preferences") {
                        showWidgetPrefs(widget);
                    }
                    return false;
                }

                $('#widget-' + regionWidgetId + '-toolbar').mousedown(function (event) {
                    // don't allow drag and drop when self item is clicked
                    event.stopPropagation();
                });

                //bind widget menu items
                self.$minimizeIcon.click(minimize);
                self.$toggleCollapseIcon.click(toggleCollapse);
                self.$cogIcon.click(addIframeOverlays);
                self.$cogIcon.dropdown();
                if (!self.$menuItemMove.hasClass("menu-item-disabled")) {
                    self.$menuItemMove.click(getLaunchClosure(showMovePageDialog));
                }
                if (!self.$menuItemDelete.hasClass("menu-item-disabled")) {
                    self.$menuItemDelete.click(getLaunchClosure(deleteWidget));
                }
                if (!self.$menuItemMaximize.hasClass("menu-item-disabled")) {
                    self.$menuItemMaximize.click(getLaunchClosure(maximize));
                }
                if (!self.$menuItemAbout.hasClass("menu-item-disabled")) {
                    self.$menuItemAbout.click(getLaunchClosure(aboutWidget));
                }
                if (!self.$menuItemComment.hasClass("menu-item-disabled")) {
                    self.$menuItemComment.click(getLaunchClosure(commentOnWidget));
                }
                if (!self.$menuItemRate.hasClass("menu-item-disabled")) {
                    self.$menuItemRate.click(getLaunchClosure(rateWidget));
                }
                var metadata = self.widget.metadata;
                if (metadata && (metadata.hasPrefsToEdit || (metadata.views && metadata.views.preferences))) {
                    self.$menuItemEditPrefs.removeClass("menu-item-disabled");
                    self.$menuItemEditPrefs.click(getLaunchClosure(showPrefsPane));
                }

                function getLaunchClosure(fn) {
                    return function(evt) {
                        self.$cogIcon.dropdown();
                        fn(evt);
                    }
                }

            }
            HomeView.prototype.getWidgetSite = function () {
                return this.$widgetSite[0];
            }
            HomeView.prototype.destroy = function (widget) {
                this.$chrome.remove();
                if (ravePortal.isPageEmpty()) {
                    displayEmptyPageMessage();
                }
            }
            HomeView.prototype.expand = function () {
                this.$chrome.show();
            }
            HomeView.prototype.collapse = function () {
                this.$chrome.hide();
            }
            HomeView.prototype.setTitle = function(title) {
                this.$title.html(title);
            }

            rave.registerView('home', HomeView);
        }

        function registerPreferencesView() {
            var PreferencesView = function(widget) {
                this.widget = widget;
                this.name = "preferences";
            }

            PreferencesView.prototype.render = function () {
                var opts = {};
                _.extend(opts, this.widget.metadata.views.preferences, {view:"preferences"});
                this.view = rave.renderView("modal_dialog");
                var widgetSite = this.view.getWidgetSite();
                this.widget.render(widgetSite, opts);
            }

            PreferencesView.prototype.getWidgetSite = function() {
                return this.view.getWidgetSite();
            }

            PreferencesView.prototype.destroy = function () {
                this.widget._provider.closeWidget(this.widget);
                rave.destroyView(this.view);
            }
            rave.registerView('preferences', PreferencesView);
        }

        function registerCanvasView() {
            var CanvasView = function (widget) {
                this.widget = widget;
                this.$title = $("#widget-" + widget.regionWidgetId + "-title");
            }
            CanvasView.prototype.render = function (widget) {
                this.widget = widget;
                // display the widget in maximized view
                openFullScreenOverlay(widget.regionWidgetId);
                // due to widget list changing height of the window, we have to set the height of the sneeze-guard here.
                var overlayStyleMap = {
                    height: $('.wrapper').height() - $('.navbar').height() - $('.logo-wrapper').height()
                };
                $('.canvas-overlay').css(overlayStyleMap);

                widget.render(widget._el, {view: 'canvas'});

                function openFullScreenOverlay(widgetId) {
                    addCanvasOverlay($("#pageContent"));
                    getNonLockedRegions().sortable("option", "disabled", true);
                    $("#widget-" + widgetId + "-wrapper").removeClass("widget-wrapper").addClass("widget-wrapper-canvas");
                    // hide the widget menu
                    $("#widget-" + widgetId + "-widget-menu-wrapper").hide();
                    // display the widget minimize button
                    $("#widget-" + widgetId + "-min").show();
                    // hide the collapse/restore toggle icon in canvas mode
                    $("#widget-" + widgetId + "-collapse").hide();
                }

                function addCanvasOverlay(jqElm) {
                    if ($('.canvas-overlay').length > 0) {
                        return;
                    }
                    var overlay = $('<div></div>');
                    var styleMap = {
                        height: $('body').height() - 40
                    };

                    // style it and give it the marker class
                    $(overlay).css(styleMap);
                    $(overlay).addClass("canvas-overlay");
                    // add it to the dom before the iframe so it covers it
                    jqElm.prepend(overlay[0]);
                }
            }
            CanvasView.prototype.getWidgetSite = function () {
                return $("#widget-" + this.widget.regionWidgetId + "-body")[0];
            }
            CanvasView.prototype.destroy = function () {
                $(".dnd-overlay, .canvas-overlay").remove();
                getNonLockedRegions().sortable("option", "disabled", false);
                // display the widget in normal view
                $("#widget-" + widgetId + "-wrapper").removeClass("widget-wrapper-canvas").addClass("widget-wrapper");
                // hide the widget minimize button
                $("#widget-" + widgetId + "-min").hide();
                // show the widget menu
                $("#widget-" + widgetId + "-widget-menu-wrapper").show();
                // show the collapse/restore toggle icon
                $("#widget-" + widgetId + "-collapse").show();
                // if the widget is collapsed execute the collapse function
                // otherwise execute the minimize function
                return false;
            }
            CanvasView.prototype.setTitle  = function(title) {
                this.$title.html(title);
            }

            rave.registerView('canvas', CanvasView);
        }

        function registerPopups() {
            var POPUPS = {
                sidebar: {
                    name: "sidebar",
                    containerSelector: '.popup.slideout',
                    contentSelector: '.slideout-content',
                    template: 'popup-slideout',
                    initialize: function (container) {
                        var self = this;
                        var content = container.find(this.contentSelector);
                        content.data('popupType', this.name);
                        container.find('.close').click(function () {
                            self.cleanup(content);
                        });
                        container.show("slide", { direction: "right" }, 'fast');
                        $('body').addClass('modal-open');
                        $('body').append('<div class="modal-backdrop fade in"></div>');
                        // hide the main browser window's scrollbar to prevent possible "double scrollbar" rendering
                        // between it and an iframe vertical scrollbar
                        $('body').addClass('no-scroll');
                    },
                    cleanup: function (content) {
                        var container = content.parents(this.containerSelector);
                        container.hide("slide", { direction: "right" }, 'fast', function () {
                            container.remove();
                            $('body').removeClass('modal-open');
                            $('.modal-backdrop').remove();
                            // restore the main browser window's scrollbar
                            $('body').removeClass('no-scroll');
                        });
                    },
                    singleton: true
                },
                dialog: {
                    name: "dialog",
                    containerSelector: '.popup.dialog',
                    contentSelector: '.modal-body',
                    template: 'popup-dialog',
                    initialize: function (container) {
                        container.find(this.contentSelector).data('popupType', this.name);
                        var cfg = {
                        };
                        container.modal(cfg);

                        container.on('hidden', function () {
                            container.remove();
                        })
                    },
                    cleanup: function (content) {
                        var container = content.parents(this.containerSelector);
                        container.modal('hide');
                    },
                    singleton: false
                },
                modal_dialog: {
                    name: "modal_dialog",
                    containerSelector: '.popup.modal_dialog',
                    contentSelector: '.modal-body',
                    template: 'popup-modal',
                    initialize: function (container) {
                        container.find(this.contentSelector).data('popupType', this.name);
                        var cfg = {
                            keyboard: false,
                            backdrop: 'static',
                            show: true
                        };
                        container.modal(cfg);

                        container.on('hidden', function () {
                            container.remove();
                        })
                    },
                    cleanup: function (content) {
                        var container = content.parents(this.containerSelector);
                        container.modal('hide');
                    },
                    singleton: true
                }
            };

            _.each(POPUPS, function (target) {
                var $container,
                    $site;

                rave.registerView(target.name, {
                    render: function (prefs) {
                        $container = $(raveTemplates.templates[target.template]());
                        $site = $(target.contentSelector, $container);

                        var height = (prefs && prefs.preferredHeight);
                        var width = (prefs && prefs.preferredWidth);
                        if (height) {
                            $container.height(height);
                        }
                        if (width) {
                            $container.width(width);
                        }
                        $("#pageContent").prepend($container);

                        if (_.isFunction(target.initialize)) {
                            target.initialize($container)
                        }
                        return this;
                    },
                    getWidgetSite: function () {
                        return $site[0];
                    },
                    destroy: function () {
                        if (_.isFunction(target.cleanup)) {
                            target.cleanup($site);
                        }
                        else {
                            $container.remove();
                        }
                    }
                });
            });
        }

        function getActionElement(label, image, tooltip) {
            var elem;
            if(image) {
                if(image.indexOf("css") === 0) {
                    elem = '<i class="' + image.replace("css:","") + '" ></i>';
                } else {
                    elem = '<img src="' + image + '" />';
                }
            } else {
                elem = "<a>" + label + "</a>";
            }
            return $(elem).attr("tooltip", tooltip);
        }

        function insertWidgetToolbarAction(widgetId, label, image, tooltip, id, onSelected) {
            var $toolbar = $("#widget-" + widgetId + "-wrapper .widget-title-bar");
            var $action = $('<div class="widget-titlebar-action widget-toolbar" ></div>').append(getActionElement(label, image, tooltip));
            $action.attr("id", id).on('click', onSelected);
            $toolbar.append($action);
            return $action;
        }

        function registerActionsHandler() {
            var actions = {};
            rave.registerActionHandler({
                create: function(id, label, path, widgetId, image, tooltip, onSelected) {
                    if(!actions[id]) {
                        var segments = path.split("/");
                        //TODO Implement more paths and a better path routing system
                        if (segments.length === 2 && segments[0] === "widget" && segments[1] === "toolbar") {
                            actions[id] = insertWidgetToolbarAction(widgetId, label, image, tooltip, id, onSelected);
                        } else {
                            rave.log("Unsupported action path: '" + path + "'")
                        }
                    }
                },
                remove: function(id) {
                    if(actions[id]) actions[id].remove();
                }
            });
        }

        function init() {
            initializePageSharing();
            registerPreferencesView();
            registerHomeView();
            registerCanvasView();
            registerPopups();
            showEmptyDisplay();
            setupDragAndDrop();
            registerActionsHandler();
        }

        rave.registerOnInitHandler(init);

        return exports;
    })
