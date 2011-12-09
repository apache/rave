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
var rave = rave || (function() {
    var WIDGET_PROVIDER_ERROR = "This widget type is currently unsupported.  Check with your administrator and be sure the correct provider is registered.";

    var providerMap = {};
    var widgetByIdMap = {};
    var context = "";

    /**
     * Separate sub-namespace for isolating UI functions and state management
     *
     * NOTE: The UI implementation has dependencies on jQuery and jQuery UI
     */
    var ui = (function() {
        var TEXT_FIELD_TEMPLATE = "<tr>{prefLabelTemplate}<td><input type='text' id='{name}' name='{name}' value='{value}' class='{class}'></td></tr>";
        var CHECKBOX_TEMPLATE = "<tr>{prefLabelTemplate}<td><input type='checkbox' id='{name}' name='{name}' class='{class}' {checked}></td></tr>";
        var SELECT_FIELD_TEMPLATE = "<tr>{prefLabelTemplate}<td><select id='{name}' name='{name}' class='{class}'>{options}</select></td></tr>";
        var SELECT_OPTION_TEMPLATE = "<option value='{value}' {selected}>{displayValue}</option>";
        var TEXTAREA_TEMPLATE = "<tr>{prefLabelTemplate}<td><textarea id='{name}' name='{name}' rows='5' cols='12' class='{class}'>{value}</textarea></td></tr>";
        var HIDDEN_FIELD_TEMPLATE = "<input type='hidden' id='{name}' name='{name}' value='{value}'>";
        var PREFS_SAVE_BUTTON_TEMPLATE = "<button type='button' id='{elementId}'>Save</button>";
        var PREFS_CANCEL_BUTTON_TEMPLATE = "<button type='button' id='{elementId}'>Cancel</button>";

        var NAME_REGEX = /{name}/g;        
        var VALUE_REGEX = /{value}/g;
        var OPTIONS_REGEX = /{options}/g;
        var SELECTED_REGEX = /{selected}/g;
        var CHECKED_REGEX = /{checked}/g;
        var DISPLAY_VALUE_REGEX = /{displayValue}/g;
        var PIPE_REGEX = /\|/g;
        var ELEMENT_ID_REGEX = /{elementId}/g;
        var PREF_LABEL_TEMPLATE_REGEX = /{prefLabelTemplate}/g;
        var CLASS_REGEX = /{class}/g;
        
        var WIDGET_PREFS_LABEL_CLASS = "widget-prefs-label";
        var WIDGET_PREFS_LABEL_REQUIRED_CLASS = "widget-prefs-label-required";
        var WIDGET_PREFS_INPUT_CLASS = "widget-prefs-input";
        var WIDGET_PREFS_INPUT_REQUIRED_CLASS = "widget-prefs-input-required";
        var WIDGET_PREFS_INPUT_FAILED_VALIDATION = "widget-prefs-input-failed-validation";
        
        var WIDGET_ICON_BASE_CLASS = "ui-icon";        
        var WIDGET_BTN_MINIMIZE_CLASS = "ui-icon-arrowthick-1-sw";
        var WIDGET_TOGGLE_DISPLAY_COLLAPSED = "ui-icon-triangle-1-e";
        var WIDGET_TOGGLE_DISPLAY_NORMAL = "ui-icon-triangle-1-s";             

        // variable to store whether or not the 
        // client is a mobile device
        var mobileState = false;

        function WIDGET_PREFS_EDIT_BUTTON(regionWidgetId) {
            return "widget-" + regionWidgetId + "-prefs";
        }

        function WIDGET_PREFS_SAVE_BUTTON(regionWidgetId) {
            return "widget-" + regionWidgetId + "-prefs-save-button";
        }

        function WIDGET_PREFS_CANCEL_BUTTON(regionWidgetId) {
            return "widget-" + regionWidgetId + "-prefs-cancel-button";
        }

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
            $(".region").sortable({
                        connectWith: '.region', // defines which regions are dnd-able
                        scroll: true, // whether to scroll the window if the user goes outside the areas
                        opacity: 0.5, // the opacity of the object being dragged
                        revert: true, // smooth snap animation
                        cursor: 'move', // the cursor to show while dnd
                        handle: '.widget-title-bar', // the draggable handle
                        forcePlaceholderSize: true, // size the placeholder to the size of the widget
                        start: dragStart, // event listener for drag start
                        stop : dragStop // event listener for drag stop
            });
            initWidgetUI();
        }

        function dragStart(event, ui) {
            // highlight the draggable regions
            $(".region").addClass("region-dragging");
            uiState.widget = ui.item.children(".widget").get(0);
            uiState.currentRegion = ui.item.parent().get(0);

            //for every drag operation, create an overlay for each iframe
            //to prevent the iframe from intercepting mouse events
            //which kills drag performance
            $(".widget").each(function(index, element) {
                addOverlay($(element));
            });
        }

        function dragStop(event, ui) {
            $(".dnd-overlay").remove();
            $(".region-dragging").removeClass("region-dragging");
            //Fixes a bug where the jQuery style attribute remains set in chrome
            ui.item.attr("style", "");
            uiState.targetRegion = ui.item.parent().get(0);
            uiState.targetIndex = ui.item.index();
            rave.api.rpc.moveWidget(uiState);
            clearState();
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
            $(".widget-wrapper").each(function(){
                var widgetId = extractObjectIdFromElementId($(this).attr("id"));
                styleWidgetButtons(widgetId);
            });
        }
        
        function initMobileWidgetUI() {
            $(".widget-wrapper").each(function(){
                var widgetId = extractObjectIdFromElementId($(this).attr("id"));                
                var widget = rave.getWidgetById(widgetId);
                            
                // init the collapse/restore toggle for the title bar                
                $(this).find(".widget-title-bar-mobile").click({id: widgetId}, toggleCollapseAction); 
                $(this).find(".widget-title-bar-mobile").bind( "touchstart", function(e){alert('Span Clicked!')});
            });
        }
        
        function toggleMobileWidget(regionWidgetId) {
            var args = {};
            args.data = {};            
            args.data.id = regionWidgetId;
            toggleCollapseAction(args);
        }

        function maximizeAction(args) {
            var regionWidgetId = args.data.id;            
            // display the widget in maximized view
            addOverlay($("#pageContent"));
            $(".region" ).sortable( "option", "disabled", true );
            $("#widget-" + regionWidgetId + "-wrapper").removeClass("widget-wrapper").addClass("widget-wrapper-canvas");            
            // hide the widget menu
            $("#widget-" + regionWidgetId + "-widget-menu-wrapper").hide();            
            // display the widget minimize button
            $("#widget-" + regionWidgetId + "-min").show();                                    
            // hide the collapse/restore toggle icon in canvas mode
            $("#widget-" + regionWidgetId + "-collapse").hide();
            var widget = rave.getWidgetById(regionWidgetId);
            if(typeof widget != "undefined" && isFunction(widget.maximize)) {
                widget.maximize();
            }
        }

        function minimizeAction(args) {
            var regionWidgetId = args.data.id;     
            $(".dnd-overlay").remove();
            $(".region" ).sortable( "option", "disabled", false );
            // display the widget in normal view
            $("#widget-" + regionWidgetId + "-wrapper").removeClass("widget-wrapper-canvas").addClass("widget-wrapper");
            // hide the widget minimize button
            $("#widget-" + regionWidgetId + "-min").hide(); 
            // show the widget menu
            $("#widget-" + regionWidgetId + "-widget-menu-wrapper").show();                                                                                            
            // show the collapse/restore toggle icon
            $("#widget-" + regionWidgetId + "-collapse").show();
            var widget = rave.getWidgetById(regionWidgetId);
            // if the widget is collapsed execute the collapse function
            // otherwise execute the minimize function
            if(typeof widget != "undefined"){
                if (widget.collapsed && isFunction(widget.collapse)) {
                    widget.collapse();
                } else if (isFunction(widget.minimize)) {
                    widget.minimize();
                }
            }
        }
      
        function toggleCollapseAction(args) {     
            var regionWidgetId = args.data.id;
            var widget = getWidgetById(regionWidgetId);    
            // toggle the collapse state of the widget
            var newCollapsedValue = !widget.collapsed;
            var functionArgs = {"regionWidgetId": regionWidgetId, "collapsed": newCollapsedValue};
            
            // if this type of widget has a collapse or restore callback invoke it upon
            // successfull persistence
            if(typeof widget != "undefined") {                
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
            if (rave.isMobile()) {
                rave.doWidgetUiCollapse(functionArgs);           
            } else {
                rave.api.rest.saveWidgetCollapsedState(functionArgs);           
            }
        }      
        
        function doWidgetUiCollapse(args) {
            // update the in-memory widget with the new collapsed status
            rave.getWidgetById(args.regionWidgetId).collapsed = args.collapsed;

            // toggle the collapse/restore icon
            rave.toggleCollapseWidgetIcon(args.regionWidgetId);

            // if the widget has supplied a collapse or restore 
            // function, invoke it so each widget provider
            // can handle the collapse / restore action independently
            if (typeof args.successCallback == 'function') {
                args.successCallback();
            }
        }
        
        /**
         * Utility function to generate the html label for a userPref
         * based on if it is required or not
         */
        function generatePrefLabelMarkup(userPref) {
            var markup = [];
            var prefLabel = (userPref.required) ? "* " + userPref.displayName : userPref.displayName;            
            markup.push("<td class='");
            markup.push(WIDGET_PREFS_LABEL_CLASS);
            if (userPref.required) {
                markup.push(" ");
                markup.push(WIDGET_PREFS_LABEL_REQUIRED_CLASS);
            }
            markup.push("'>");
            markup.push(prefLabel);
            markup.push("</td>");
            return markup.join("");
        }
        
        /**
         * Utility function to generate the css class(es) of a userPref input
         * field based on if it is required or not
         */
        function generatePrefInputClassMarkup(userPref) {
            var markup = [];
            markup.push(WIDGET_PREFS_INPUT_CLASS);            
            if (userPref.required) {
                markup.push(" ");
                markup.push(WIDGET_PREFS_INPUT_REQUIRED_CLASS);
            }
            return markup.join("");
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

        function editPrefsAction(regionWidgetId) {
            var regionWidget = getWidgetById(regionWidgetId);
            var userPrefs = regionWidget.metadata.userPrefs;
            var hasRequiredUserPrefs = false;
            
            var prefsFormMarkup = [];
            prefsFormMarkup.push("<table>");

            for (var prefName in userPrefs) {
                var userPref = userPrefs[prefName];
                var currentPrefValue = regionWidget.userPrefs[userPref.name];
                var prefLabelMarkup = generatePrefLabelMarkup(userPref);
                var prefInputClassMarkup = generatePrefInputClassMarkup(userPref);
                if (userPref.required) {
                    hasRequiredUserPrefs = true;
                }

                switch (userPref.dataType) {
                    case "STRING":
                        prefsFormMarkup.push(TEXT_FIELD_TEMPLATE.replace(PREF_LABEL_TEMPLATE_REGEX, prefLabelMarkup)
                                .replace(CLASS_REGEX, prefInputClassMarkup)
                                .replace(NAME_REGEX, userPref.name)
                                .replace(VALUE_REGEX, typeof currentPrefValue != "undefined" ? currentPrefValue :
                                userPref.defaultValue));
                        break;
                    case "BOOL":
                        var checked = typeof currentPrefValue != "undefined" ?
                                currentPrefValue === 'true' || currentPrefValue === true :
                                userPref.defaultValue === 'true' || userPref.defaultValue === true;

                        prefsFormMarkup.push(CHECKBOX_TEMPLATE.replace(PREF_LABEL_TEMPLATE_REGEX, prefLabelMarkup)
                                .replace(CLASS_REGEX, prefInputClassMarkup)
                                .replace(NAME_REGEX, userPref.name)
                                .replace(CHECKED_REGEX, checked ? "checked" : ""));
                        break;
                    case "ENUM":
                        var options = [];

                        for (var i = 0; i < userPref.orderedEnumValues.length; i++) {
                            var option = userPref.orderedEnumValues[i];
                            var selected = currentPrefValue == option.value || (typeof currentPrefValue == "undefined" &&
                                    option.value == userPref.defaultValue);
                            options.push(SELECT_OPTION_TEMPLATE.replace(VALUE_REGEX, option.value)
                                    .replace(DISPLAY_VALUE_REGEX, option.displayValue)
                                    .replace(SELECTED_REGEX, selected ? "selected" : ""));
                        }

                        prefsFormMarkup.push(SELECT_FIELD_TEMPLATE.replace(PREF_LABEL_TEMPLATE_REGEX, prefLabelMarkup)
                                .replace(CLASS_REGEX, prefInputClassMarkup)
                                .replace(NAME_REGEX, userPref.name)
                                .replace(OPTIONS_REGEX, options.join("")));
                        break;
                    case "LIST":
                        var values = typeof currentPrefValue != "undefined" ? currentPrefValue : userPref.defaultValue;
                        values = values.replace(PIPE_REGEX, "\n");
                        prefsFormMarkup.push(TEXTAREA_TEMPLATE.replace(PREF_LABEL_TEMPLATE_REGEX, prefLabelMarkup)
                                .replace(CLASS_REGEX, prefInputClassMarkup)
                                .replace(NAME_REGEX, userPref.name)
                                .replace(VALUE_REGEX, values));
                        break;
                    default:
                        prefsFormMarkup.push(HIDDEN_FIELD_TEMPLATE.replace(NAME_REGEX, userPref.name)
                                .replace(VALUE_REGEX, typeof currentPrefValue != "undefined" ? currentPrefValue :
                                userPref.defaultValue));
                }
            }
            
            // if this widget has one or more required inputs display the helper message
            if (hasRequiredUserPrefs) {
                prefsFormMarkup.push("<tr><td colspan='2' class='widget-prefs-required-text'>* indicates a required input</td></tr>");
            }

            prefsFormMarkup.push("<tr><td colspan='2'>");
            prefsFormMarkup.push(PREFS_SAVE_BUTTON_TEMPLATE.replace(ELEMENT_ID_REGEX,
                    WIDGET_PREFS_SAVE_BUTTON(regionWidget.regionWidgetId)));
            prefsFormMarkup.push(PREFS_CANCEL_BUTTON_TEMPLATE.replace(ELEMENT_ID_REGEX,
                    WIDGET_PREFS_CANCEL_BUTTON(regionWidget.regionWidgetId)));
            prefsFormMarkup.push("</td></tr>");

            prefsFormMarkup.push("</table>");

            var prefsElement = $("#" + WIDGET_PREFS_CONTENT(regionWidget.regionWidgetId));
            prefsElement.html(prefsFormMarkup.join(""));

            $("#" + WIDGET_PREFS_SAVE_BUTTON(regionWidget.regionWidgetId)).click({id:regionWidget.regionWidgetId},
                    saveEditPrefsAction);
            $("#" + WIDGET_PREFS_CANCEL_BUTTON(regionWidget.regionWidgetId)).click({id:regionWidget.regionWidgetId},
                    cancelEditPrefsAction);

            prefsElement.show();
        }

        function saveEditPrefsAction(args) {
            var regionWidget = getWidgetById(args.data.id);
            var prefsElement = $("#" + WIDGET_PREFS_CONTENT(regionWidget.regionWidgetId));

            var updatedPrefs = {};
            var hasValidationErrors = false;
            // note that validation of "required" prefs is only done for text and
            // textarea types, since those represent STRING and LIST inputs, and
            // are the only inputs that could potentially contain empty data
            prefsElement.find("*").filter(":input").each(function(index, element) {
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
                if(isFunction(regionWidget.savePreferences)) {
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
                height : jqElm.height(),
                width : jqElm.width(),
                'z-index': 10,
                opacity : 0.7,
                background : "#FFFFFF"
            };

            // style it and give it the marker class
            $(overlay).css(styleMap);
            $(overlay).addClass("dnd-overlay");
            // add it to the dom before the iframe so it covers it
            jqElm.prepend(overlay[0]);
        }

        /**
         * Applies styling to the several buttons in the widget toolbar
         * @param widgetId identifier of the region widget
         */
        function styleWidgetButtons(widgetId) {
            var widget = rave.getWidgetById(widgetId);
            
            // init the widget minimize button which is hidden by default
            // and only renders when widget is in maximized view
            $("#widget-" + widgetId + "-min").button({
                text: false,
                icons: {
                    primary: WIDGET_BTN_MINIMIZE_CLASS
                }
            }).click({id: widgetId}, rave.minimizeWidget);
                
            // init the collapse/restore toggle
            // conditionally style the icon and setup the event handlers
            var $toggleCollapseIcon = $("#widget-" + widgetId + "-collapse");
            $toggleCollapseIcon.hide();
            $toggleCollapseIcon.addClass(WIDGET_ICON_BASE_CLASS);
            $toggleCollapseIcon.addClass((widget.collapsed) ? WIDGET_TOGGLE_DISPLAY_COLLAPSED : WIDGET_TOGGLE_DISPLAY_NORMAL);                        
            $toggleCollapseIcon
                .click({id: widgetId}, toggleCollapseAction)
                .mousedown(function(event) {
                    // don't allow drag and drop when this item is clicked
                    event.stopPropagation();
                });       
            $toggleCollapseIcon.show();
        }
                
        /**
         * Toggles the display of the widget collapse/restore icon.
         * @param widgetId the widgetId of the rendered widget to toggle the icon for
         */
        function toggleCollapseWidgetIcon(widgetId) {
            var $toggleIcon = $("#widget-" + widgetId + "-collapse");
            if ($toggleIcon.hasClass(WIDGET_TOGGLE_DISPLAY_COLLAPSED)) {
                $toggleIcon.removeClass(WIDGET_TOGGLE_DISPLAY_COLLAPSED);
                $toggleIcon.addClass(WIDGET_TOGGLE_DISPLAY_NORMAL);
            } else {
                $toggleIcon.removeClass(WIDGET_TOGGLE_DISPLAY_NORMAL);
                $toggleIcon.addClass(WIDGET_TOGGLE_DISPLAY_COLLAPSED);
            }
        }        
        
        /**
         * Displays the "empty page" message on the page
         */
        function displayEmptyPageMessage() {            
            $("#emptyPageMessageWrapper").removeClass("hidden");
        }

        return {
          init : init,       
          initMobile: initMobileWidgetUI,
          toggleCollapseWidgetIcon: toggleCollapseWidgetIcon,
          maximizeAction: maximizeAction,
          minimizeAction: minimizeAction,
          editPrefsAction: editPrefsAction,
          setMobileState: setMobileState,
          getMobileState: getMobileState,
          doWidgetUiCollapse: doWidgetUiCollapse,
          toggleMobileWidget: toggleMobileWidget,
          displayEmptyPageMessage: displayEmptyPageMessage
        };

    })();

    function registerWidget(widgetsByRegionIdMap, regionId, widget) {
        if (!widgetsByRegionIdMap.hasOwnProperty(regionId)) {
            widgetsByRegionIdMap[regionId] = [];
        }
        widgetsByRegionIdMap[regionId].push(widget);
    }

    function initializeProviders() {
        //Current providers are rave.wookie and rave.opensocial.
        //Providers register themselves when loaded, so
        //JavaScript library importing order is important.
        //See home.jsp for example.
        for (var key in providerMap) {
            providerMap[key].init();
        }
    }

    function initializeWidgets(widgetsByRegionIdMap) {                           
        //We get the widget objects in a map keyed by region ID.  The code below converts that map into a flat array
        //of widgets with all the top widgets in each region first, then the seconds widgets in each region, then the
        //third, etc until we have all widgets in the array.  This allows us to render widgets from left to right and
        //top to bottom to give the best user experience possible (rendering the top widgets first).
        //Note that this strategy relies on the javascript implementation to enumerate object properties in order:
        //http://stackoverflow.com/questions/280713/elements-order-in-a-for-in-loop
        //However this should at least get us to render the top "row" first, then the second "row", ... in any browser.
        //If this turns out to be a major concern we can change the widgetsByRegionIdMap to a 2D array instead of a map.
        var widgets = [];
        var regionWidget;
        for (var i = 0; ; i++) {
            var foundGadgets = false;
            for (var regionWidgets in widgetsByRegionIdMap) {
                regionWidgets = widgetsByRegionIdMap[regionWidgets];
                if (regionWidgets.length > i) {
                    foundGadgets = true;
                    regionWidget = regionWidgets[i];
                    // if client is viewing in mobile mode
                    // default to collapsed state
                    if (rave.isMobile()) {
                        regionWidget.collapsed = true;
                    }
                    widgets.push(regionWidget);
                }
            }

            if (!foundGadgets) {
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
    }

    function initializeWidget(widget) {
        if (widget.type == "DISABLED") {
            renderDisabledWidget(widget.regionWidgetId, unescape(widget.disabledMessage));
            return;
        }
        var provider = providerMap[widget.type];
        if (typeof provider == "undefined") {
            renderErrorWidget(widget.regionWidgetId, WIDGET_PROVIDER_ERROR);
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

    function getWidgetById(regionWidgetId) {
        return widgetByIdMap[regionWidgetId];
    }

    function viewPage(pageId) {                
        var fragment = (pageId != null) ? ("/" + pageId) : "";
        window.location.href = rave.getContext() + "page/view" + fragment;      
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
     * Public API
     */
    return {
        /**
         * Registers the specified widget into the widgetsByRegionIdMap under the specified regionId.
         * @param widgetsByRegionIdMap The map.
         * @param regionId The regionId.
         * @param widget The widget.
         */
        registerWidget : registerWidget,

        /**
         * Initialize all of the registered providers
         */
        initProviders : initializeProviders,

        /**
         * Initializes the given set of widgets
         * @param widgets a map of widgets by regionId
         *
         * NOTE: widget object must have at a minimum the following properties:
         *      type,
         *      regionWidgetId
         */
        initWidgets : initializeWidgets,

        /**
         * Initialize Rave's drag and drop facilities
         */
        initUI : ui.init,
        /**
         * Initialize the mobile UI
         */
        initMobileUI : ui.initMobile,

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
        getObjectIdFromDomId : extractObjectIdFromElementId,

        /**
         * Registers a new provider with Rave.  All providers MUST have init and initWidget functions as well as a
         * TYPE property exposed in its public API
         *
         * @param provider a valid Rave widget provider
         */
        registerProvider : addProviderToList,

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
        setContext : updateContext,

        /**
         * Gets the current context
         */
        getContext: getContext,
        
        /**
         * Gets a widget by id
         */
        getWidgetById: getWidgetById,
        
        /**
         * View a page
         * 
         * @param pageId the pageId to view, or if null, the user's default page
         */
        viewPage: viewPage,
        
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
        
        /***
         * Display the inline edit prefs section for widget preferences inside
         * the widget.
         * 
         * @param regionWidgetId the regionWidgetId of the widget
         * 
         */
        editPrefs: ui.editPrefsAction,
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
        displayEmptyPageMessage: ui.displayEmptyPageMessage
    }
})();
