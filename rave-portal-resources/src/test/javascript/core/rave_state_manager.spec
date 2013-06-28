/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License'); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


describe('state_manager', function(){
    beforeEach(function () {
        stateManager = testr('core/rave_state_manager');
        //spyOn(mock, 'ajax').andCallThrough();
        /*api = testr('core/rave_api', {
            './rave_ajax': mock.ajax,
            './rave_state_manager': stateManager,
            './rave_event_manager': eventManager
        })*/
        //spyOn(testScope, 'callback');
    });

    describe('set page', function(){
        it('sets the page correctly', function(){
            spyOn(stateManager, 'setPage').andCallThrough();

            stateManager.setPage('testPage');

            expect(stateManager.getPage()).toBe('testPage');
            expect(stateManager.setPage).toHaveBeenCalled()
            expect(stateManager.setPage.mostRecentCall.args[0]).toEqual('testPage')
        })
    })

    describe('get page', function(){
        it('gets the page correctly', function(){
            spyOn(stateManager, 'getPage').andCallThrough();

            stateManager.setPage('testPage');
            var output = stateManager.getPage();

            expect(output).toBe('testPage');
            expect(stateManager.getPage).toHaveBeenCalled()
            expect(stateManager.getPage.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set viewer', function(){
        it('sets the viewer correctly', function(){
            spyOn(stateManager, 'setViewer').andCallThrough();

            stateManager.setViewer('testViewer');

            expect(stateManager.getViewer()).toBe('testViewer');
            expect(stateManager.setViewer).toHaveBeenCalled();
            expect(stateManager.setViewer.mostRecentCall.args[0]).toEqual('testViewer')
        })
    })

    describe('get viewer', function(){
        it('gets the viewer correctly', function(){
            spyOn(stateManager, 'getViewer').andCallThrough();

            stateManager.setViewer('testViewer');
            var output = stateManager.getViewer();

            expect(output).toBe('testViewer');
            expect(stateManager.getViewer).toHaveBeenCalled()
            expect(stateManager.getViewer.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set owner', function(){
        it('sets the owner correctly', function(){
            spyOn(stateManager, 'setOwner').andCallThrough();

            stateManager.setOwner('testOwner');

            expect(stateManager.getOwner()).toBe('testOwner');
            expect(stateManager.setOwner).toHaveBeenCalled();
            expect(stateManager.setOwner.mostRecentCall.args[0]).toEqual('testOwner')
        })
    })

    describe('get owner', function(){
        it('gets the owner correctly', function(){
            spyOn(stateManager, 'getOwner').andCallThrough();

            stateManager.setOwner('testOwner');
            var output = stateManager.getOwner();

            expect(output).toBe('testOwner');
            expect(stateManager.getOwner).toHaveBeenCalled()
            expect(stateManager.getOwner.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set context', function(){
        it('sets the context correctly', function(){
            spyOn(stateManager, 'setContext').andCallThrough();

            stateManager.setContext('testContext');

            expect(stateManager.getContext()).toBe('testContext');
            expect(stateManager.setContext).toHaveBeenCalled();
            expect(stateManager.setContext.mostRecentCall.args[0]).toEqual('testContext')
        })
    })

    describe('get context', function(){
        it('gets the context correctly', function(){
            spyOn(stateManager, 'getContext').andCallThrough();

            stateManager.setContext('testContext');
            var output = stateManager.getContext();

            expect(output).toBe('testContext');
            expect(stateManager.getContext).toHaveBeenCalled()
            expect(stateManager.getContext.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set export enabled', function(){
        it('sets the export enabled correctly', function(){
            spyOn(stateManager, 'setExportEnabled').andCallThrough();

            stateManager.setExportEnabled(true);

            expect(stateManager.getExportEnabled()).toBe(true);
            expect(stateManager.setExportEnabled).toHaveBeenCalled();
            expect(stateManager.setExportEnabled.mostRecentCall.args[0]).toEqual(true)
        })
    })

    describe('get export enabled', function(){
        it('gets the export enabled correctly', function(){
            spyOn(stateManager, 'getExportEnabled').andCallThrough();

            stateManager.setExportEnabled(true);
            var output = stateManager.getExportEnabled();

            expect(output).toBe(true);
            expect(stateManager.getExportEnabled).toHaveBeenCalled()
            expect(stateManager.getExportEnabled.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set default height', function(){
        it('sets the default height correctly', function(){
            spyOn(stateManager, 'setDefaultHeight').andCallThrough();

            stateManager.setDefaultHeight(900);

            expect(stateManager.getDefaultHeight()).toBe(900);
            expect(stateManager.setDefaultHeight).toHaveBeenCalled();
            expect(stateManager.setDefaultHeight.mostRecentCall.args[0]).toEqual(900)
        })
    })

    describe('get default height', function(){
        it('gets the default height correctly', function(){
            spyOn(stateManager, 'getDefaultHeight').andCallThrough();

            var output = stateManager.getDefaultHeight();

            expect(output).toBe(200);
            expect(stateManager.getDefaultHeight).toHaveBeenCalled()
            expect(stateManager.getDefaultHeight.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set default width', function(){
        it('sets the default width correctly', function(){
            spyOn(stateManager, 'setDefaultWidth').andCallThrough();

            stateManager.setDefaultWidth(900);

            expect(stateManager.getDefaultWidth()).toBe(900);
            expect(stateManager.setDefaultWidth).toHaveBeenCalled();
            expect(stateManager.setDefaultWidth.mostRecentCall.args[0]).toEqual(900)
        })
    })

    describe('get default width', function(){
        it('gets the default width correctly', function(){
            spyOn(stateManager, 'getDefaultWidth').andCallThrough();

            var output = stateManager.getDefaultWidth();

            expect(output).toBe(320);
            expect(stateManager.getDefaultWidth).toHaveBeenCalled()
            expect(stateManager.getDefaultWidth.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set default view', function(){
        it('sets the default view correctly', function(){
            spyOn(stateManager, 'setDefaultView').andCallThrough();

            stateManager.setDefaultView('testView');

            expect(stateManager.getDefaultView()).toBe('testView');
            expect(stateManager.setDefaultView).toHaveBeenCalled();
            expect(stateManager.setDefaultView.mostRecentCall.args[0]).toEqual('testView')
        })
    })

    describe('get default view', function(){
        it('gets the default view correctly', function(){
            spyOn(stateManager, 'getDefaultView').andCallThrough();

            var output = stateManager.getDefaultView();

            expect(output).toBe('default');
            expect(stateManager.getDefaultView).toHaveBeenCalled()
            expect(stateManager.getDefaultView.mostRecentCall.args.length).toEqual(0)
        })
    })

    describe('set javascript debug mode', function(){
        it('sets the java script debug mode correctly', function(){
            spyOn(stateManager, 'setDebugMode').andCallThrough();

            stateManager.setDebugMode(true);

            expect(stateManager.getDebugMode()).toBe(true);
            expect(stateManager.setDebugMode).toHaveBeenCalled();
            expect(stateManager.setDebugMode.mostRecentCall.args[0]).toEqual(true)
        })
    })

    describe('get javascript debug mode', function(){
        it('gets the javascript debug mode correctly', function(){
            spyOn(stateManager, 'getDebugMode').andCallThrough();

            var output = stateManager.getDebugMode();

            expect(output).toBe(false);
            expect(stateManager.getDebugMode).toHaveBeenCalled()
            expect(stateManager.getDebugMode.mostRecentCall.args.length).toEqual(0)
        })
    })
})





