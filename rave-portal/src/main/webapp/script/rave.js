var rave = rave || (function(){

    function mapWidgetsByType(widgets) {
        var map = {};
        for(var i=0; i < widgets.length; i++) {
            var widget = widgets[i];
            if(!map[widget.type]) {
                map[widget.type] = [];
            }
            map[widget.type].push(widget);
        }
        return map;
    }

    /**
     * Public API
     */
    return {
        /**
         * Creates a map of widgets by their type
         *
         * @param widgets list of widgets to map by type
         */
        createWidgetMap : mapWidgetsByType
    }

})();