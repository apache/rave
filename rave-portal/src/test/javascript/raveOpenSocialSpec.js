describe("Rave OpenSocial", function() {

    var container = null;

    beforeEach(function() {
        osapi = {
            container : {
                ServiceConfig : { API_PATH : 0 },
                ContainerConfig : {RENDER_DEBUG : 1},
                Container : function(args) {
                    var rpcHooks = {};

                    /** Mock usage machinery **/
                    this.args = function() { return args; };
                    this.rpcHooks = function() { return rpcHooks; };

                    /** OpenSocial Common Container functions **/
                    this.rpcRegister = function(string, func) {
                        rpcHooks[string] = func;
                    };
                    container = this;
                }
            }
        }
    });

    afterEach(function(){
        container = null;
        rave.opensocial.reset();
    });

    it("is OpenSocial", function() {
        expect(rave.opensocial.TYPE).toEqual("OpenSocial");
    });

    describe("Init", function() {

        it("initializes the OpenSocial container machinery", function() {
            rave.opensocial.init();

            expect(container.args()[0]).toEqual("/rpc");
            expect(container.args()[1]).toEqual("1");
        });

        it("Initializes the RPC Hooks Correctly", function() {
            rave.opensocial.init();
            hooks = container.rpcHooks();
            expect(hooks["resize_iframe"]).toBeDefined();
            expect(hooks["set_title"]).toBeDefined();
            expect(hooks["requestNavigateTo"]).toBeDefined();
        });

    });

    describe("Container", function(){
        it("initializes the container if not already done", function(){
            expect(container).toBeNull();
            var newContainer = rave.opensocial.container();
            expect(newContainer).toBe(container);
            expect(container).toBeDefined();
            expect(container.args()[0]).toEqual("/rpc");
        });

        it("returns the same reference if already initialized", function(){
            expect(container).toBeNull();
            var newContainer1 = rave.opensocial.container();
            var newContainer2 = rave.opensocial.container();
            expect(newContainer2).toBe(newContainer1);
        });
    });

    describe("RPC Hooks", function(){
        it("resizes Iframe if argument is less than height", function() {
            rave.opensocial.init();
            var hooks = container.rpcHooks();
            var mockArgs = {
                f : "frameId",
                a : 25
            };
            var mockDOM = {style: {height: "-1px"}};
            spyOn(document, "getElementById").andReturn(mockDOM);
            hooks["resize_iframe"](mockArgs);
            expect(mockDOM.style.height).toEqual("25px");
        });
    });

    describe("RPC Hooks", function(){
        it("resizes Iframe to max if height is greater than max", function() {
            rave.opensocial.init();
            var hooks = container.rpcHooks();
            var mockArgs = {
                f : "frameId",
                a : 2147483648
            };
            var mockDOM = {style: {height: "-1px"}};
            spyOn(document, "getElementById").andReturn(mockDOM);
            hooks["resize_iframe"](mockArgs);
            expect(mockDOM.style.height).toEqual("2147483647px");
        });
    });

    describe("RPC Hooks", function(){
        it("does not throw error if DOM element is null", function() {
            rave.opensocial.init();
            var hooks = container.rpcHooks();
            var mockArgs = {
                f : "frameId",
                a : 2147483648
            };
            var mockDOM = {style: {height: "-1px"}};
            spyOn(document, "getElementById").andReturn(null);
            hooks["resize_iframe"](mockArgs);
            //If we reach this point there was no error
            expect(true).toBeTruthy();
        });
    });
});