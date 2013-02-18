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
var rave = rave || (function () {
    var providerMap = {};
    var widgetByIdMap = {};
    var widgetsByRegionIdArray = [];

    var context = "";
    var clientMessages = {};
    var openAjaxHub;
    // variable to store whether or not some
    // UI actions should be propagated back to the server
    var pageEditor = true;
    var pageViewer = {
        username: "Unknown",
        id: -1
    };
    var pageOwner = {
        username: "Unknown",
        id: -1
    };
    // JS debug mode is off by default
    var javaScriptDebugMode = 0;
    //Assigning a default value of 250 if unable to get value from the DB
    var defaultWidgetHeight = 250;
    var onWidgetsInitializedHandlers = [];
    var onProvidersInitializedHandlers = [];
    var onUIInitializedHandlers = [];
    var onPageInitializedHandlers = [];

    /**
     * Separate sub-namespace for isolating UI functions and state management
     *
     * NOTE: The UI implementation has dependencies on jQuery and jQuery UI
     */
    var ui = (function () {
        var WIDGET_TOGGLE_DISPLAY_COLLAPSED_HTML = '<i class="icon-chevron-down"></i>';
        var WIDGET_TOGGLE_DISPLAY_NORMAL_HTML = '<i class="icon-chevron-up"></i>';

        var WIDGET_PREFS_INPUT_REQUIRED_CLASS = "widget-prefs-input-required";
        var WIDGET_PREFS_INPUT_FAILED_VALIDATION = "widget-prefs-input-failed-validation";

        var POPUPS = {
            sidebar: {
                name: "sidebar",
                containerSelector: '.popup.slideout',
                contentSelector: '.slideout-content',
                template: 'popup-slideout',
                initialize: function (container) {
                    var content = container.find(this.contentSelector);
                    content.data('popupType', this.name);
                    container.find('.close').click(function(){ destroyPopup(content);});
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
                        container.detach();
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
                        container.detach();
                    })
                },
                cleanup: function (content) {
                    var container = content.parents(this.containerSelector);

                    container.modal('hide');
                },
                singleton: true
            },
            "float": false,
            tab: false
        };

        // variable to store whether or not the
        // client is a mobile device
        var mobileState = false;

        function WIDGET_PREFS_CONTENT(regionWidgetId) {
            return "widget-" + regionWidgetId + "-prefs-content";
        }

        var uiState = {
            widget: null,
            currentRegion: null,
            targetRegion: null,
            targetIndex: null
        };

        function setMobileState(mobileState) {
            this.mobileState = mobileState;
        }

        function getMobileState() {
            return this.mobileState;
        }

        function init() {
            // initialize the sortable regions
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
            initWidgetUI();

            if (onUIInitializedHandlers !== null && onUIInitializedHandlers.length > 0) {
                for (var i = 0, j = onUIInitializedHandlers.length; i < j; ++i) {
                    try {
                        onUIInitializedHandlers[i]();
                    } catch (ex) {
                        gadgets.warn("Could not fire onUIInitializedHandler " + ex.message);
                    }
                }
            }
            onUIInitializedHandlers = null;  // No need to hold these references anymore.
        }

        function dragStart(event, ui) {
            adjustRowRegionsHeights();
            var $regions = getNonLockedRegions();
            // highlight the draggable regions
            $regions.addClass("regionDragging");
            // remove invisible border so nothing moves
            $regions.removeClass("regionNonDragging");

            uiState.widget = ui.item.children(".widget").get(0);
            uiState.currentRegion = ui.item.parent().get(0);

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
            uiState.targetRegion = ui.item.parent().get(0);
            uiState.targetIndex = ui.item.index();
            rave.api.rpc.moveWidget(uiState);
            clearState();
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

        function clearState() {
            uiState.currentRegion = null;
            uiState.targetRegion = null;
            uiState.targetIndex = null;
            uiState.widget = null;
        }

        /**
         * Takes care of the UI part of the widget rendering. Depends heavily on the HTML structure
         */
        function initWidgetUI() {
            $(".widget-wrapper").each(function () {
                var widgetId = extractObjectIdFromElementId($(this).attr("id"));
                styleWidgetButtons(widgetId);
            });
        }

        function initMobileWidgetUI() {
            $(".widget-wrapper").each(function () {
                var widgetId = extractObjectIdFromElementId($(this).attr("id"));
                var widget = rave.getRegionWidgetById(widgetId);

                // init the collapse/restore toggle for the title bar
                $(this).find(".widget-title-bar-mobile").click({id: widgetId}, toggleCollapseAction);
            });
        }

        function toggleMobileWidget(regionWidgetId) {
            var args = {};
            args.data = {};
            args.data.id = regionWidgetId;
            toggleCollapseAction(args);
        }

        function maximizeAction(args, view_params) {
            var regionWidgetId = args.data.id;
            // display the widget in maximized view
            openFullScreenOverlay(regionWidgetId);
            var widget = rave.getRegionWidgetById(regionWidgetId);
            if (typeof widget != "undefined" && isFunction(widget.maximize)) {
                widget.maximize(view_params, args.data.view);
                // due to widget list changing height of the window, we have to set the height of the sneeze-guard here.
                var overlayStyleMap = {
                    height: $('.wrapper').height() - $('.navbar').height() - $('.logo-wrapper').height()
                };
                $('.canvas-overlay').css(overlayStyleMap);
            }
        }

        function minimizeAction(args, view_params) {
            var regionWidgetId = args.data.id;
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
            var widget = rave.getRegionWidgetById(regionWidgetId);
            // if the widget is collapsed execute the collapse function
            // otherwise execute the minimize function
            if (typeof widget != "undefined") {
                if (widget.collapsed && isFunction(widget.collapse)) {
                    widget.collapse();
                } else if (isFunction(widget.minimize)) {
                    widget.minimize(view_params, args.data.view);
                }
            }
        }

        function hideAction(args) {
            var regionWidgetId = args.data.id;
            var widget = rave.getRegionWidgetById(regionWidgetId);

            widget.hide();
        }

        function showAction(args) {
            var regionWidgetId = args.data.id;
            var widget = rave.getRegionWidgetById(regionWidgetId);

            widget.show();
        }

        function createPopup(popupType, prefs) {
            var height = (prefs && prefs.preferredHeight);
            var width = (prefs && prefs.preferredWidth);

            var target = POPUPS[popupType.toLowerCase()];

            if (!target) {
                return rave.log('The popup view requested is not implemented by rave');
            }

            if (target.singleton && $(target.containerSelector).length > 0) {
                return $(target.contentSelector).get(0);
            }

            var container = $(rave.ui.templates[target.template]());
            if (height) {
                container.height(height);
            }
            if (width) {
                container.width(width);
            }
            $("#pageContent").prepend(container);

            if ($.type(target.initialize) == 'function') target.initialize(container);

            return container.find(target.contentSelector).get(0);
        }

        function destroyPopup(element) {
            element = $(element);
            var target = POPUPS[element.data('popupType')];

            if (!target) {
                rave.log('Rave has detected a destroy request for a popup whose type cannot be detected.');
                return element.detach();
            }

            if ($.type(target.cleanup) == 'function') target.cleanup(element);
        }

        function editCustomPrefsAction(args) {
            var regionWidgetId = args.data.id;

            // display the custom edit prefs for the widget in maximized view
            openFullScreenOverlay(regionWidgetId);
            var widget = rave.getRegionWidgetById(regionWidgetId);
            if (typeof widget != "undefined") {
                widget.editCustomPrefs();
            }
        }

        function toggleCollapseAction(args) {
            var regionWidgetId = args.data.id;
            var widget = getRegionWidgetById(regionWidgetId);
            // toggle the collapse state of the widget
            var newCollapsedValue = !widget.collapsed;
            var functionArgs = {"regionWidgetId": regionWidgetId, "collapsed": newCollapsedValue};

            // if this type of widget has a collapse or restore callback invoke it upon
            // successful persistence
            if (typeof widget != "undefined") {
                // if this is a collapse action, and the widget has a collapse implementation function,
                // attach it as a callback function
                if (newCollapsedValue && isFunction(widget.collapse)) {
                    functionArgs.successCallback = widget.collapse;
                }
                // if this is a restore action, and the widget has a restore implementation function,
                // attach it as a callback function
                else if (!newCollapsedValue && isFunction(widget.restore)) {
                    functionArgs.successCallback = widget.restore;
                }
            }

            // don't persist the collapse / restore state if we are in
            // mobile mode because we defaulted all widgets to collapsed
            // when initially rendering the mobile view
            if (rave.isMobile() || !rave.isPageEditor()) {
                rave.doWidgetUiCollapse(functionArgs);
            } else {
                rave.api.rest.saveWidgetCollapsedState(functionArgs);
            }
        }

        function doWidgetUiCollapse(args) {
            // update the in-memory widget with the new collapsed status
            rave.getRegionWidgetById(args.regionWidgetId).collapsed = args.collapsed;

            // toggle the collapse/restore icon
            rave.toggleCollapseWidgetIcon(args.regionWidgetId, args.collapsed);

            // if the widget has supplied a collapse or restore
            // function, invoke it so each widget provider
            // can handle the collapse / restore action independently
            if (typeof args.successCallback == 'function') {
                args.successCallback();
            }
        }

        /**
         * Utility function to validate a userPref input element
         */
        function validatePrefInput(element) {
            var isValid = true;
            var jqEl = $(element);
            // if the input is required verify it's trimmed input length is > 0
            if (jqEl.hasClass(WIDGET_PREFS_INPUT_REQUIRED_CLASS)) {
                isValid = $.trim(jqEl.val()).length > 0;
            }

            return isValid;
        }

        /*
         Displays the edit preferences panel for a gadet
         */
        function editPrefsAction(regionWidgetId) {
            var regionWidget = getRegionWidgetById(regionWidgetId);
            var userPrefs = regionWidget.metadata.userPrefs;
            var hasRequiredUserPrefs = false;

            //format the data for display
            _.each(userPrefs, function (pref) {
                //find current value of each pref
                pref.value = regionWidget.userPrefs[pref.name] || pref.defaultValue;

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

            var markup = rave.ui.templates['widget-preferences']({
                userPrefs: userPrefs,
                hasRequiredUserPrefs: hasRequiredUserPrefs
            });
            var $markup = $(markup);

            $('.prefs-save-button', $markup).click({id: regionWidget.regionWidgetId},
                saveEditPrefsAction);

            $('.prefs-cancel-button', $markup).click({id: regionWidget.regionWidgetId},
                cancelEditPrefsAction);

            var prefsElement = $("#" + WIDGET_PREFS_CONTENT(regionWidget.regionWidgetId));
            prefsElement.html($markup);

            prefsElement.show();
        }

        function saveEditPrefsAction(args) {
            var regionWidget = getRegionWidgetById(args.data.id);
            var prefsElement = $("#" + WIDGET_PREFS_CONTENT(regionWidget.regionWidgetId));

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
                if (isFunction(regionWidget.savePreferences)) {
                    regionWidget.savePreferences(updatedPrefs);
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

        function addCanvasOverlay(jqElm) {
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

        function openFullScreenOverlay(regionWidgetId) {
            addCanvasOverlay($("#pageContent"));
            getNonLockedRegions().sortable("option", "disabled", true);
            $("#widget-" + regionWidgetId + "-wrapper").removeClass("widget-wrapper").addClass("widget-wrapper-canvas");
            // hide the widget menu
            $("#widget-" + regionWidgetId + "-widget-menu-wrapper").hide();
            // display the widget minimize button
            $("#widget-" + regionWidgetId + "-min").show();
            // hide the collapse/restore toggle icon in canvas mode
            $("#widget-" + regionWidgetId + "-collapse").hide();
        }

        /**
         * Applies styling to the several buttons in the widget toolbar
         * @param widgetId identifier of the region widget
         */
        function styleWidgetButtons(widgetId) {
            var widget = rave.getRegionWidgetById(widgetId);

            // init the widget minimize button which is hidden by default
            // and only renders when widget is in maximized view
            $("#widget-" + widgetId + "-min").click({id: widgetId}, rave.minimizeWidget);

            // init the collapse/restore toggle
            // conditionally style the icon and setup the event handlers
            var $toggleCollapseIcon = $("#widget-" + widgetId + "-collapse");
            $toggleCollapseIcon.html((widget.collapsed) ? WIDGET_TOGGLE_DISPLAY_COLLAPSED_HTML : WIDGET_TOGGLE_DISPLAY_NORMAL_HTML);
            $toggleCollapseIcon
                .click({id: widgetId}, toggleCollapseAction)
                .mousedown(function (event) {
                    // don't allow drag and drop when this item is clicked
                    event.stopPropagation();
                });


            $('#widget-' + widgetId + '-toolbar').mousedown(function (event) {
                // don't allow drag and drop when this item is clicked
                event.stopPropagation();
            });
        }

        /**
         * Toggles the display of the widget collapse/restore icon.
         * @param widgetId the widgetId of the rendered widget to toggle the icon for
         */
        function toggleCollapseWidgetIcon(widgetId, collapsed) {
            var $toggleIcon = $("#widget-" + widgetId + "-collapse");
            if (collapsed) {
                $toggleIcon.html(WIDGET_TOGGLE_DISPLAY_COLLAPSED_HTML);
            } else {
                $toggleIcon.html(WIDGET_TOGGLE_DISPLAY_NORMAL_HTML);
            }
        }

        /**
         * Displays the "empty page" message on the page
         */
        function displayEmptyPageMessage() {
            $("#emptyPageMessageWrapper").removeClass("hidden");
        }

        function displayUsersOfWidget(widgetId) {
            rave.api.rest.getUsersForWidget({widgetId: widgetId, successCallback: function (data) {

                //format data for display
                _.each(data, function (person) {
                    person.name = person.displayName || person.preferredName || (person.givenName + " " + person.familyName);
                });

                var markup = rave.ui.templates['users-of-widget']({
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

        //TODO: rename as flash?
        function showInfoMessage(message) {

            var markup = rave.ui.templates['info-message']({
                message: message
            });

            $(markup).hide()
                .prependTo("body")
                //TODO: add a class for flash box and attach these styles
                .css({ position: 'fixed', top: 0, left: 0, width: 'auto', 'max-width': '60%', 'font-size': '1.25em', padding: '.6em 1em', 'z-index': 9999, 'border-radius': '0 0 4px 0'})
                .fadeIn('fast').delay(8000)
                .fadeOut(function () {
                    $(this).remove();
                });
        }

        function getNonLockedRegions() {
            return $(".region:not(.region-locked)");
        }

        function registerPopup(id, obj) {
            POPUPS[id] = obj;
        }

        return {
            init: init,
            initMobile: initMobileWidgetUI,
            toggleCollapseWidgetIcon: toggleCollapseWidgetIcon,
            maximizeAction: maximizeAction,
            minimizeAction: minimizeAction,
            hideAction: hideAction,
            showAction: showAction,
            createPopup: createPopup,
            destroyPopup: destroyPopup,
            editPrefsAction: editPrefsAction,
            editCustomPrefsAction: editCustomPrefsAction,
            setMobileState: setMobileState,
            getMobileState: getMobileState,
            doWidgetUiCollapse: doWidgetUiCollapse,
            toggleMobileWidget: toggleMobileWidget,
            displayEmptyPageMessage: displayEmptyPageMessage,
            displayUsersOfWidget: displayUsersOfWidget,
            showInfoMessage: showInfoMessage,
            registerPopup: registerPopup,
            styleWidgetButtons: styleWidgetButtons
        };

    })();

    function getClientMessage(key) {
        return clientMessages[key];
    }

    function addClientMessage(key, message) {
        return clientMessages[key] = message;
    }

    function registerWidget(regionId, widget) {
        // first find the region in the array, if it exists
        var regionExists = false;
        for (var i = 0; i < widgetsByRegionIdArray.length; i++) {
            var tempRegion = widgetsByRegionIdArray[i];
            if (tempRegion.regionId === regionId) {
                // add the widget to the existing region object
                regionExists = true;
                tempRegion.widgets.push(widget);
                break;
            }
        }

        // if this region doesn't exist yet add it to the widgetsByRegionIdArray
        if (!regionExists) {
            widgetsByRegionIdArray.push({regionId: regionId, widgets: [widget]});
        }
    }

    function getWidgetsByRegionIdArray() {
        return widgetsByRegionIdArray;
    }

    function clearWidgetsByRegionIdArray() {
        widgetsByRegionIdArray = [];
    }

    function initializeProviders(pageType) {
        //Current providers are rave.wookie and rave.opensocial.
        //Providers register themselves when loaded, so
        //JavaScript library importing order is important.
        //See page.jsp for example.
        for (var key in providerMap) {
            providerMap[key].init(pageType);
        }

        if (onProvidersInitializedHandlers !== null && onProvidersInitializedHandlers.length > 0) {
            for (var i = 0, j = onProvidersInitializedHandlers.length; i < j; ++i) {
                try {
                    onProvidersInitializedHandlers[i]();
                } catch (ex) {
                    gadgets.warn("Could not fire onProvidersInitializedHandler " + ex.message);
                }
            }
        }
        onProvidersInitializedHandlers = null;  // No need to hold these references anymore.
    }

    function createNewOpenAjaxHub() {
        if (typeof OpenAjax == "undefined") {
            throw "No implementation of OpenAjax found.  " +
                "Please ensure that an implementation has been included in the page.";
        }
        return new OpenAjax.hub.ManagedHub({
            onSubscribe: function (topic, container) {
                log((container == null ? "Container" : container.getClientID()) + " subscribes to this topic '" + topic + "'");
                return true;
            },
            onUnsubscribe: function (topic, container) {
                log((container == null ? "Container" : container.getClientID()) + " unsubscribes from this topic '" + topic + "'");
                return true;
            },
            onPublish: function (topic, data, pcont, scont) {
                log((pcont == null ? "Container" : pcont.getClientID()) + " publishes '" + data + "' to topic '" + topic + "' subscribed by " + (scont == null ? "Container" : scont.getClientID()));
                return true;
            }
        });
    }

    function getOpenAjaxHubInstance() {
        if (typeof openAjaxHub == "undefined" || openAjaxHub == null) {
            openAjaxHub = createNewOpenAjaxHub();
        }
        return openAjaxHub;
    }

    function resetOpenAjaxHubInstance() {
        openAjaxHub = null;
    }

    function renderNewWidget(regionWidgetId, init, regionId) {
        // When run as the callback argument supplied to rave.api.rpc.addWidgetToPage
        // or rave.api.rpc.addWidgetToPageRegion
        // this method will render the widget in the current page.
        // load widget into a placeholder element
        var placeholder = document.createElement("div");
        $(placeholder).load(rave.getContext() + "api/rest/regionwidget/" + regionWidgetId, function () {
            var region = null;
            if (regionId != undefined) {
                // get specified region
                region = $("#region-" + regionId + "-id");
            }
            else {
                var $firstRegion = $(".region:not(.region-locked):first")
                var firstRegionId = ($firstRegion).attr('id');
                // prepend to first region
                region = $("#" + firstRegionId);
            }
            region.prepend(placeholder);
            // remove the placeholder around the widget-wrapper
            region.children(":first").children(":first").unwrap();

            if (init) {
                initializeWidgets();
                rave.styleWidgetButtons(regionWidgetId);
                rave.layout.bindWidgetMenu(regionWidgetId);
            }
        });
    }

    function initializeWidgets() {
        //The code below converts the widgetsByRegionIdArray into a flat array
        //of widgets with all the top widgets in each region first, then the seconds widgets in each region, then the
        //third, etc until we have all widgets in the array.  This allows us to render widgets from left to right and
        //top to bottom to give the best user experience possible (rendering the top widgets first).
        //However this should at least get us to render the top "row" first, then the second "row", ... in any browser.
        var widgets = [];
        var regionWidget;
        for (var i = 0; ; i++) {
            var foundWidgets = false;
            for (var x = 0; x < widgetsByRegionIdArray.length; x++) {
                region = widgetsByRegionIdArray[x];
                if (region.widgets.length > i) {
                    foundWidgets = true;
                    regionWidget = region.widgets[i];
                    // if client is viewing in mobile mode
                    // default to collapsed state
                    if (rave.isMobile()) {
                        regionWidget.collapsed = true;
                    }
                    widgets.push(regionWidget);
                }
            }

            if (!foundWidgets) {
                break;
            }
        }

        if (widgets.length == 0) {
            rave.displayEmptyPageMessage();
        } else {
            //Initialize the widgets for supported providers
            for (var j = 0; j < widgets.length; j++) {
                var widget = widgets[j];
                initializeWidget(widget);
                widgetByIdMap[widget.regionWidgetId] = widget;
            }
        }

        if (onWidgetsInitializedHandlers !== null && onWidgetsInitializedHandlers.length > 0) {
            for (var i = 0, j = onWidgetsInitializedHandlers.length; i < j; ++i) {
                try {
                    onWidgetsInitializedHandlers[i]();
                } catch (ex) {
                    gadgets.warn("Could not fire onWidgetInitializedHandler " + ex.message);
                }
            }
        }
        onWidgetsInitializedHandlers = null;  // No need to hold these references anymore.
    }

    function initializeWidget(widget) {
        // Widget has been deleted on the page but not removed from list
        var widgetBody = $(["#widget-", widget.regionWidgetId, "-body"].join(""));
        if (widgetBody.length === 0) {
            return;
        } else {
            // Widget has already been initialized
            if (typeof widgetBody.children !== "undefined" && widgetBody.children().length !== 0) {
                return;
            }
        }

        if (widget.type == "DISABLED") {
            renderDisabledWidget(widget.regionWidgetId, unescape(widget.disabledMessage));
            return;
        }
        var provider = providerMap[widget.type];
        if (typeof provider == "undefined") {
            renderErrorWidget(widget.regionWidgetId, rave.getClientMessage("widget.provider.error"));
        } else {
            provider.initWidget(widget);
        }
    }

    function addProviderToList(provider) {
        if (provider.hasOwnProperty("init")) {
            providerMap[provider.TYPE] = provider;
        } else {
            throw "Attempted to register invalid provider";
        }
    }

    function extractObjectIdFromElementId(elementId) {
        var tokens = elementId.split("-");
        return tokens.length > 2 && tokens[0] == "widget" || tokens[0] == "region" ? tokens[1] : null;
    }

    function renderErrorWidget(id, message) {
        $("#widget-" + id + "-body").html(message);
    }

    function renderDisabledWidget(id, message) {
        $("#widget-" + id + "-body").html(message);
    }

    function updateContext(contextPath) {
        context = contextPath;
    }

    function getContext() {
        return context;
    }

    function setJavaScriptDebugMode(debugMode) {
        javaScriptDebugMode = debugMode;
    }

    function getJavaScriptDebugMode() {
        return javaScriptDebugMode;
    }

    function setDefaultWidgetHeight(widgetHeight) {
        defaultWidgetHeight = widgetHeight;
    }

    function getDefaultWidgetHeight() {
        return defaultWidgetHeight;
    }

    function setPageViewer(viewer) {
        pageViewer = viewer;
    }

    function getPageViewer() {
        return pageViewer;
    }

    function setPageOwner(owner) {
        pageOwner = owner;
    }

    function getPageOwner() {
        return pageOwner;
    }

    function getRegionWidgetById(regionWidgetId) {
        return widgetByIdMap[regionWidgetId];
    }

    function viewPage(pageId) {
        var fragment = (pageId != null) ? ("/" + pageId) : "";
        window.location.href = rave.getContext() + "page/view" + fragment;
    }

    function viewWidgetDetail(widgetId, referringPageId, jumpToId) {
        if (jumpToId) {
            jumpToId = '#' + jumpToId;
        }
        else {
            jumpToId = '';
        }
        window.location.href = rave.getContext() + "store/widget/" + widgetId + "?referringPageId=" + referringPageId + jumpToId;
    }

    /**
     * Utility function to determine if a javascript object is a function
     * @param obj the object to check
     * @return true if object is a function, false otherwise
     */
    function isFunction(obj) {
        return (typeof obj == "function");
    }

    /**
     * Determines if a page is empty (has zero widgets)
     */
    function isPageEmpty() {
        return $.isEmptyObject(widgetByIdMap);
    }

    /**
     * Removes a regionWidgetId from the internal widget map
     * @param regionWidgetId the region widget id to remove
     */
    function removeWidgetFromMap(regionWidgetId) {
        delete widgetByIdMap[regionWidgetId];
    }

    /**
     * Logs to a console object if it exists
     * @param message the message to log
     */
    function log(message) {
        if (typeof console != "undefined" && console.log) {
            console.log(message);
        }
    }

    function initPageEditorStatus(status) {
        if (status != "undefined") {
            this.pageEditor = status;
        }
    }

    function isPageEditor() {
        return this.pageEditor;
    }

    function registerOnWidgetsInitizalizedHandler(callback) {
        if (onWidgetsInitializedHandlers !== null) {
            onWidgetsInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };

    function registerOnProvidersInitizalizedHandler(callback) {
        if (onProvidersInitializedHandlers !== null) {
            onProvidersInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };

    function registerOnUIInitizalizedHandler(callback) {
        if (onUIInitializedHandlers !== null) {
            onUIInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };

    function registerOnPageInitizalizedHandler(callback) {
        if (onPageInitializedHandlers !== null) {
            onPageInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };


    /**
     * Internal method should only be called from the page.jsp
     */
    function runOnPageInitializedHandlers() {
        if (onPageInitializedHandlers !== null && onPageInitializedHandlers.length > 0) {
            for (var i = 0, j = onPageInitializedHandlers.length; i < j; ++i) {
                try {
                    onPageInitializedHandlers[i]();
                } catch (ex) {
                    gadgets.warn("Could not fire onPageInitializedHandler " + ex.message);
                }
            }
        }
        onPageInitializedHandlers = null;  // No need to hold these references anymore.
    }

    /**
     * Public API
     */
    return {
        /**
         * Registers the specified widget into the widgetsByRegionIdArray under the specified regionId.
         * @param regionId The regionId.
         * @param widget The widget.
         */
        registerWidget: registerWidget,

        /**
         * Render a newly-added widget in the page
         * @param regionWidgetId the regionWidgetId of the widget to render
         */
        renderNewWidget: renderNewWidget,

        /**
         * Initialize all of the registered providers
         */
        initProviders: initializeProviders,

        /**
         * Initializes the given set of widgets
         * @param widgets a map of widgets by regionId
         *
         * NOTE: widget object must have at a minimum the following properties:
         *      type,
         *      regionWidgetId
         */
        initWidgets: initializeWidgets,

        /**
         * Initialize Rave's drag and drop facilities
         */
        initUI: ui.init,

        /**
         * Initialize the mobile UI
         */
        initMobileUI: ui.initMobile,

        /**
         * Parses the given string conforming to a rave object's DOM element ID and return
         * the RegionWidget id
         *
         * NOTE: assumes convention of widget-RegionWidgetId-body|title|chrome|etc
         *       Supported rave objects:
         *          Region
         *          RegionWidget
         *
         * @param elementId the ID of the DOM element containing the widget
         */
        getObjectIdFromDomId: extractObjectIdFromElementId,

        /**
         * Registers a new provider with Rave.  All providers MUST have init and initWidget functions as well as a
         * TYPE property exposed in its public API
         *
         * @param provider a valid Rave widget provider
         */
        registerProvider: addProviderToList,

        /**
         * Renders an error in place of the widget
         *
         * @param id the RegionWidgetId of the widget to render in error mode
         * @param message The message to display to the user
         */
        errorWidget: renderErrorWidget,

        /**
         * Sets the context path for the Rave web application
         *
         * @param contextPath the context path of the rave webapp
         */
        setContext: updateContext,

        /**
         * Gets the current context
         */
        getContext: getContext,

        /**
         * Gets the value of the JavaScriptDebugMode flag
         * 0 = off
         * 1 = on
         */
        getJavaScriptDebugMode: getJavaScriptDebugMode,

        /**
         * Sets the value of the JavaScriptDebugMode flag
         * 0 = off
         * 1 = on
         */
        setJavaScriptDebugMode: setJavaScriptDebugMode,

        /*Gets the value of the Default Widget Height*/
        getDefaultWidgetHeight: getDefaultWidgetHeight,

        /*Sets the value of the Default Widget Height*/
        setDefaultWidgetHeight: setDefaultWidgetHeight,

        /**
         * Sets the authenticated page viewer for the Rave web application
         *
         * @param viewer an object representing the authenticated user viewing the page {username:"bob", id:"1"}
         */
        setPageViewer: setPageViewer,

        /**
         * Gets the current viewer
         */
        getPageViewer: getPageViewer,

        /**
         * Sets the owner of the current page
         *
         * @param owner an object representing the owner of the page
         */
        setPageOwner: setPageOwner,

        /**
         * Gets the page owner
         */
        getPageOwner: getPageOwner,

        /**
         * Gets a regionwidget by region widget id
         */
        getRegionWidgetById: getRegionWidgetById,

        /**
         * View a page
         *
         * @param pageId the pageId to view, or if null, the user's default page
         */
        viewPage: viewPage,

        /**
         * View the widget detail page of a widget
         *
         * @param widgetId to widgetId to view
         * @param referringPageId the id of the page the call is coming from
         */
        viewWidgetDetail: viewWidgetDetail,

        /**
         * Toggles the collapse/restore icon of the rendered widget
         *
         * @param widgetId the widgetId of the rendered widget to toggle
         */
        toggleCollapseWidgetIcon: ui.toggleCollapseWidgetIcon,

        /***
         * Utility function to determine if a javascript object is a function or not
         *
         * @param obj the object to check
         * @return true if obj is a function, false otherwise
         */
        isFunction: isFunction,

        /***
         * Maximize the widget view
         *
         * @param args the argument object
         */
        maximizeWidget: ui.maximizeAction,

        /***
         * Minimize the widget view (render in non full-screen mode)
         *
         * @param args the argument object
         */
        minimizeWidget: ui.minimizeAction,

        /**
         * Hide the widget and its chrome
         *
         * @param args the argument object
         */
        hideWidget: ui.hideAction,

        /**
         * Show the widget and its chrome
         *
         * @param args the argument object
         */
        showWidget: ui.showAction,

        /***
         * Create a new popup in the rave container
         *
         * @param popupType the type of popup that will be created
         * @return the new dom element created
         */
        createPopup: ui.createPopup,

        /***
         * Destroy a popup currently active in the rave container
         *
         * @param element the popup dom element
         */
        destroyPopup: ui.destroyPopup,

        /***
         * Display the inline edit prefs section for widget preferences inside
         * the widget.
         *
         * @param regionWidgetId the regionWidgetId of the widget
         *
         */
        editPrefs: ui.editPrefsAction,

        /***
         * "Preferences" view
         *
         * @param args the argument object
         */
        editCustomPrefs: ui.editCustomPrefsAction,

        /***
         * Get the mobile state - used by the UI to render mobile or normal content
         *
         */
        isMobile: ui.getMobileState,

        /***
         * Set the mobile state - used by the UI to render mobile or normal content
         *
         * @param mobileState boolean to represent the mobile state
         *
         */
        setMobile: ui.setMobileState,

        /**
         * Performs the client side work of collapsing/restoring a widget
         * @param args
         */
        doWidgetUiCollapse: ui.doWidgetUiCollapse,

        /**
         * Toggles a mobile widget collapse/restore
         * @param args
         */
        toggleMobileWidget: ui.toggleMobileWidget,

        /**
         * Determines if a page is empty (has zero widgets)
         * @param widgetByIdMap the map of widgets on the page
         */
        isPageEmpty: isPageEmpty,

        /**
         * Removes a regionWidgetId from the internal widget map
         * @param regionWidgetId the region widget id to remove
         */
        removeWidgetFromMap: removeWidgetFromMap,

        /**
         * Displays the "empty page" message on the page
         */
        displayEmptyPageMessage: ui.displayEmptyPageMessage,

        /**
         * Displays the users of a supplied widgetId in a dialog box
         */
        displayUsersOfWidget: ui.displayUsersOfWidget,

        /**
         * Displays an info message at the top of the page.
         * @param message The message to display.
         */
        showInfoMessage: ui.showInfoMessage,

        styleWidgetButtons: ui.styleWidgetButtons,

        /**
         * Returns a language specific message based on the supplied key
         *
         * @param key the key of the message
         */
        getClientMessage: getClientMessage,

        /**
         * Adds a message to the internal client message map
         *
         * @param key
         * @param message
         */
        addClientMessage: addClientMessage,

        /**
         * Gets the singleton Managed OpenAJAX 2.0 Hub
         */
        getManagedHub: getOpenAjaxHubInstance,

        /**
         * Resets the managed hub
         */
        resetManagedHub: resetOpenAjaxHubInstance,

        /**
         * Logs a message to a central logging facility (console by default)
         *
         * @param message the message to log
         */
        log: log,

        /**
         * Returns the widgetsByRegionIdArray
         */
        getWidgetsByRegionIdArray: getWidgetsByRegionIdArray,

        /**
         * Clears the widgetsByRegionIdArray.  Useful for testing.
         */
        clearWidgetsByRegionIdArray: clearWidgetsByRegionIdArray,

        /**
         * Registers a new popup definition
         */
        registerPopup: ui.registerPopup,

        /**
         * Set if user of a page has editing permission
         * Used to stop sending UI events back to the server, rather
         * than actually implementing any permission rules
         * (which are set on the server)
         */
        initPageEditorStatus: initPageEditorStatus,

        /**
         * Returns a boolean indicating if the user
         * should be treated as an page editor or not
         */
        isPageEditor: isPageEditor,

        /**
         * Registration methods for initialization events
         */
        registerOnWidgetsInitizalizedHandler: registerOnWidgetsInitizalizedHandler,
        registerOnProvidersInitizalizedHandler: registerOnProvidersInitizalizedHandler,
        registerOnUIInitizalizedHandler: registerOnUIInitizalizedHandler,
        registerOnPageInitizalizedHandler: registerOnPageInitizalizedHandler,
        runOnPageInitializedHandlers: runOnPageInitializedHandlers
    }
})();
