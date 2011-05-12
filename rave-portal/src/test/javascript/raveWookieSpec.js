describe("Rave Wookie", function() {

     it("Is W3C Widget", function() {
        expect(rave.wookie.TYPE).toEqual("W3C");
     });

     describe("Verify null widgets results in clean return", function() {
	     it("Tests null argument", function() {
           var wookieMap=rave.wookie.initWidgets(null);
			  //Lifted from raveSpec.js.  Must be other ways to test this.
			  var count=0;
           for (i in wookieMap) {count++;}
           expect(count).toEqual(0);
        });

	     it("Tests uncreated list", function() {
           var widgets;		     
           var wookieMap=rave.wookie.initWidgets(widgets);
			  //Lifted from raveSpec.js.  Must be other ways to test this.
			  var count=0;
           for (i in wookieMap) {count++;}
           expect(count).toEqual(0);
        });

	     it("Tests empty list", function() {
           var widgets=[];		     
           var wookieMap=rave.wookie.initWidgets(widgets);
			  //Lifted from raveSpec.js.  Must be other ways to test this.
			  var count=0;
           for (i in wookieMap) {count++;}
           expect(count).toEqual(0);
        });
     });

     describe("Create Wookie Widgets",function() {
        //This test is currently incomplete because we need to find
        //a way to handle the document.getElementID() call within rave_wookie.js
	     it("initializes wookie widgets", function() {
            var widgets = [
                {regionWidgetId: 0, type: "W3C"},
                {regionWidgetId: 1, type: "null"},
                {regionWidgetId: 2}
            ];
			  var wookieMap=rave.wookie.initWidgets(widgets[rave.wookie.TYPE]);
        });
     });

});