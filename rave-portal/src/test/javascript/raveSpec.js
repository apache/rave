describe("Rave", function() {

    describe("createWidgetMap", function() {

        it("builds a map keyed by type", function() {
            var widgets = [
                {regionWidgetId: 0, type: "OpenSocial"},
                {regionWidgetId: 1, type: "OpenSocial"},
                {regionWidgetId: 2, type: "W3C"},
                {regionWidgetId: 3, type: "W3C"}
            ];

            var map = rave.createWidgetMap(widgets);

            expect(map["OpenSocial"].length).toEqual(2);
            expect(map["W3C"].length).toEqual(2);
            expect(map["OpenSocial"]).toContain(widgets[1]);
            expect(map["W3C"]).toContain(widgets[3]);
        });

        it("builds a map that has widgets under an Unknown key", function() {
            var widgets = [
                {regionWidgetId: 0, type: "OpenSocial"},
                {regionWidgetId: 1, type: "OpenSocial"},
                {regionWidgetId: 2, type: "W3C"},
                {regionWidgetId: 3, type: "W3C"},
                {regionWidgetId: 4 },
                {regionWidgetId: 5, type: null }
            ];

            var map = rave.createWidgetMap(widgets);

            expect(map["OpenSocial"].length).toEqual(2);
            expect(map["W3C"].length).toEqual(2);
            expect(map["Unknown"].length).toEqual(2);
            expect(map["OpenSocial"]).toContain(widgets[1]);
            expect(map["W3C"]).toContain(widgets[3]);
            expect(map["Unknown"]).toContain(widgets[4]);
        });

        it("builds a map with no entries", function(){
            var widgets = [];
            var map = rave.createWidgetMap(widgets);
            var count = 0;
            for(i in map) {count++;}
            expect(count).toEqual(0);
        });

    });
});