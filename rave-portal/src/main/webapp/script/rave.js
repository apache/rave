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
    var context = "";

    /**
     * Separate sub-namespace for isolating UI functions and state management
     *
     * NOTE: The UI implementation has dependencies on jQuery and jQuery UI
     */
    var ui = (function() {

        var uiState = {
            widget: null,
            currentRegion: null,
            targetRegion: null,
            targetIndex: null
        };

        function init() {
            $(".region").disableSelection();
            // initialize the sortable regions
            $(".region").sortable({
                        connectWith: '.region', // defines which regions are dnd-able
                        scroll: true, // don't scroll the window if the user goes outside the areas
                        opacity: 0.5, // the opacity of the object being dragged
                        revert: true, // smooth snap animation
                        cursor: 'move', // the cursor to show while dnd
                        handle: '.widget-title-bar', // the draggable handle
                        forcePlaceholderSize: true, // size the placeholder to the size of the gadget
                        start: dragStart,
                        stop : dragStop
            });
            initGadgetUI();
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
                // create the iframe overlay and size it to the iframe it will be covering
                var overlay = $('<div></div>');
                var jqElm = $(element);
                var styleMap = {
                    position: "absolute",
                    height : jqElm.height(),
                    width : jqElm.width(),
                    opacity : 0.7,
                    background : "#FFFFFF"
                };

                // style it and give it the marker class
                $(overlay).css(styleMap);
                $(overlay).addClass("dnd-overlay");
                // add it to the dom before the iframe so it covers it
                jqElm.prepend(overlay[0]);
            });
        }

        function dragStop(event, ui) {
            $(".dnd-overlay").remove();
            $(".region-dragging").removeClass("region-dragging");
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
        function initGadgetUI() {
            $("div[id^='region-']").each(function(regionIndex) {
                var regionElement = $(this);
                var regionId = regionElement.attr("id");
                regionId = regionId.substring("region-".length, regionId.lastIndexOf('-'));
                regionElement.children("div[id^='widget-wrapper-']").each(function(wrapperIndex) {
                    var widgetElement = $(this);
                    var widgetId = widgetElement.attr("id").substr("widget-wrapper-".length);
                    styleGadgetButtons(widgetId);
                });
            });
        }

        function maximizeAction(args) {
            alert("Maximize button not yet implemented");
        }

        function deleteAction(args) {
            if (confirm("Are you sure you want to remove this gadget from your page")) {
                rave.api.rpc.removeWidget({
                    regionWidgetId: args.data.id,
                    successCallback: function() {
                        $("#widget-wrapper-" + args.data.id).remove();
                    }
                });
            }
        }

        /**
         * Applies styling to the several buttons in the widget / gadget toolbar
         * @param widgetId identifier of the widget / gadget
         */
        function styleGadgetButtons(widgetId) {
            $("#widget-" + widgetId + "-max").button({
                text: false,
                icons: {
                    primary: "ui-icon-arrow-4-diag"
                }
            }).click({id: widgetId}, maximizeAction);

            $("#widget-" + widgetId + "-remove").button({
                text: false,
                icons: {
                    primary: "ui-icon-close"
                }
            }).click({id: widgetId},deleteAction);
        }

        return {
          init : init
        };

    })();

    function initializeProviders() {
        //Current providers are rave.wookie and rave.opensocial.
        //Providers register themselves when loaded, so
        //JavaScript library importing order is important.
        //See home.jsp for example.
        for (var key in providerMap) {
            providerMap[key].init();
        }
    }

    function initializeWidgets(widgets) {
        //Initialize the widgets for supported providers
        for(var i=0; i<widgets.length; i++) {
            initializeWidget(widgets[i]);
        }
    }

    function initializeWidget(widget) {
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

    function mapWidgetsByType(widgets) {
        var map = {};
        for (var i = 0; i < widgets.length; i++) {
            var widget = widgets[i];
            var type = widget.type;
            if (!type) {
                type = "Unknown";
            }
            if (!map[type]) {
                map[type] = [];
            }
            map[type].push(widget);
        }
        return map;
    }

    function extractObjectIdFromElementId(elementId) {
        var tokens = elementId.split("-");
        return tokens.length > 2 && tokens[0] == "widget" || tokens[0] == "region" ? tokens[1] : null;
    }

    function renderErrorWidget(id, message) {
        $("#widget-" + id + "-body").html(message);
    }

    function updateContext(contextPath) {
        context = contextPath;
    }

    function getContext() {
        return context;
    }


    /**
     * Public API
     */
    return {
        /**
         * Initialize all of the registered providers
         */
        initProviders : initializeProviders,

        /**
         * Initializes the given set of widgets
         * @param widgets a map of widgets by type
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
         * Creates a map of widgets by their type
         *
         * @param widgets list of widgets to map by type
         */
        createWidgetMap : mapWidgetsByType,

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
         * Renders an error in place of the gadget
         *
         * @param id the RegionWidgetId of the widget to render in error mode
         * @param message The message to display to the suer
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
        getContext: getContext
    }
})();
