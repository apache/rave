var mock = {
    ajax: function(args){
        args.success({})
    }
}

var eventManager = {
    registerOnInitHandler: function(){}
}

var stateManager = {
    getContext: function(){}
}




describe('rave_api', function(){
    beforeEach(function () {
        testScope = {
            callback: function () {
            }
        }

        spyOn(mock, 'ajax').andCallThrough();
        api = testr('core/rave_api', {
            './rave_ajax': mock.ajax,
            './rave_state_manager': stateManager,
            './rave_event_manager': eventManager
        })
        spyOn(testScope, 'callback');
    });

    //test helper function
    function expectAjax(type, url, data) {
        var args = mock.ajax.mostRecentCall.args[0];

        expect(mock.ajax).toHaveBeenCalled();
        expect(args.type).toEqual(type);
        expect(args.url).toEqual(url);
        if (data) {
            expect(args.data).toEqual(data);
        }
        if (args.successCallback) {
            expect(testScope.callback).toHaveBeenCalled();
        }
    }

    describe('rest', function(){
        describe('saveWidgetPreferences', function () {
            it('makes the correct api call', function () {

                api.rest.saveWidgetPreferences({regionWidgetId: 1, userPrefs: {"color": "blue", "speed": "fast"},
                    successCallback: testScope.callback});

                expectAjax(
                    'PUT',
                    'api/rest/regionWidgets/1/preferences',
                    JSON.stringify({preferences: [
                        {name: 'color', value: 'blue'},
                        {name: 'speed', value: 'fast'}
                    ]})
                );
            });
        });
    })
})
