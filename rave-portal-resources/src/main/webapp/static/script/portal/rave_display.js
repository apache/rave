/**
 * Created with IntelliJ IDEA.
 * User: DGORNSTEIN
 * Date: 6/14/13
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
define(["core/rave_api"], function(api){
    function displayUsersOfWidget(widgetId) {
        api.rest.getUsersForWidget({widgetId: widgetId, successCallback: function (data) {

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
