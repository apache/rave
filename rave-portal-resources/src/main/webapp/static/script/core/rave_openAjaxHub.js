define(['opensocial', './rave_log'], function(log){
    var  openAjaxHub;

    return function(){
        if (!openAjaxHub) {
            if (_.isUndefined(OpenAjax)) {
                throw new Error("No implementation of OpenAjax found.  " +
                    "Please ensure that an implementation has been included in the page.");
            }
            openAjaxHub = new OpenAjax.hub.ManagedHub({
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
        return openAjaxHub;
    }
});