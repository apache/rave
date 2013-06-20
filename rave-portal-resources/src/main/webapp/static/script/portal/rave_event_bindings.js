/**
 * Created with IntelliJ IDEA.
 * User: DGORNSTEIN
 * Date: 6/19/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
define(["jquery", 'core/rave_api', 'core/rave_store', 'portal/rave_models', 'portal/rave_layout', 'portal/rave_display', 'portal/rave_portal'],
    function($, raveApi, raveStore, raveModels, raveLayout, raveDisplay, ravePortal){

        var pageNameToFunctionMap = {
            "addWidget.jsp": addWidgetEventBindings,
            "addwidget.marketplace.jsp": addWidgetMarketplaceEventBindings,
            "mobile_home.jsp": mobileHomeEventBindings,
            "page.jsp": pageJspEventBindings,
            "error.jsp": errorPageBindings,
            "store.jsp": storeEventBindings,
            "widget.jsp": widgetEventBindings,
            "widget.marketplace.jsp": widgetMarketplaceEventBindings,
            "widgetdetail.jsp": widgetDetailEventBindings,
            "region_widget.tag": regionWidgetEventBindings

        };

        /********************************
            View Event Bindings
         ********************************/
        function addWidgetEventBindings(){
            $('#fetchMetadataButton').click(function(){
                raveApi.rpc.getWidgetMetadata({
                    url: $('#url').get(0).value,
                    providerType: 'OpenSocial',
                    successCallback: function(result) {
                        var widget = result.result;
                        $('#title').val(widget.title);
                        $('#description').val(widget.description);
                        $('#thumbnailUrl').val(widget.thumbnailUrl);
                        $('#screenshotUrl').val(widget.screenshotUrl);
                        $('#titleUrl').val(widget.titleUrl);
                        $('#author').val(widget.author);
                        $('#authorEmail').val(widget.authorEmail);
                        $('#addWidgetForm').show();
                        $('#addWidgetFormSubmit').show();
                    },
                    errorCallback: function(){
                        alert(ravePortal.getClientMessage("api.widget_metadata.parse_error"));
                    },
                    alertInvalidParams: function(){
                        alert(ravePortal.getClientMessage("api.widget_metadata.invalid_params"));
                    }
                })
            })
        };

        function addWidgetMarketplaceEventBindings(){
            $("#marketplaceWidgetList").on("click", "button.widgetAddButton", function(event){
                raveStore.confirmAddFromMarketplace($(this).data('widget-url'), $(this).data('widget-type'));
            });
        };

        function mobileHomeEventBindings(){
            $("#pageContent").on("click", ".widget-title-bar-mobile", function(event){
                //TODO:This function is undefined, must re-define before un-commenting out
                //rave.toggleMobileWidget($(this).data('regionWidget-id'));
                console.log("rave.toggleMobileWidget function is undefined");
            });
        };

        function pageJspEventBindings(){
            $('#acceptShareLink').click(function(){
                raveModels.currentPage.acceptShare();
            });

            $('#declineShareLink').click(function(){
                raveModels.currentPage.declineShare();
            });

            $('#movePageButton').click(function(){
                raveLayout.movePage();
            })

            $('#moveWidgetToPageButton').click(function(){
                raveLayout.moveWidgetToPage($('#moveWidgetModal').data('regionWidgetId'));
            })
        };

        function errorPageBindings(){
            $("#showErrorStack").click(function(){
                document.getElementById('errorStack').style.display = 'block';
            })

            $("#hideErrorStack").click(function(){
                document.getElementById('errorStack').style.display = 'none';
            })
        };

        function storeEventBindings(){
            $("#storeItems").on("click", "button.widgetAddButton", function(event){
                var element = $(this);
                raveApi.rpc.addWidgetToPage({widgetId: element.data('widget-id'),
                    pageId: element.data('referring-page-id'), buttonId: element.attr('id'),
                    successCallback: addWidgetToPageCallback
                });
            });

            $("#storeItems").on("click", "a.displayUsersLink", function(event){
                var element = $(this);
                raveDisplay.displayUsersOfWidget(element.data('widget-id'));
            });
        };

        function widgetEventBindings(){
            $('.widgetJspAddWidgetButton').click(function(){
                var element = $(this);
                var widgetId = element.data('widget-id');
                var pageId = element.data('page-id');
                raveApi.addWidgetToPage({widgetId: widgetId, pageId: pageId, redirectAfterAdd: true, successCallback: addWidgetToPageCallback})
            })

            $('#displayUsersOfWidgetLink').click(function(){
                raveDisplay.displayUsersOfWidget($(this).data('widget-id'))
            })
        };

        function widgetMarketplaceEventBindings(){
            $('#widgetMarketplaceConfirmAddButton').click(function(){
                var element = $(this);
                raveStore.confirmAddFromMarketplace(element.data('widget-url'), element.data('widget-type'));
            })
        };

        function widgetDetailEventBindings(){
            $('#fetchMetadataButton').click(function(){
                raveApi.rpc.getWidgetMetadata({
                    url: $('#url').get(0).value,
                    providerType: $('input:radio[name=type]:checked').val(),
                    successCallback: function(result) {
                        var widget = result.result;
                        $('#title').val(widget.title);
                        $('#description').val(widget.description);
                        $('#thumbnailUrl').val(widget.thumbnailUrl);
                        $('#screenshotUrl').val(widget.screenshotUrl);
                        $('#titleUrl').val(widget.titleUrl);
                        $('#author').val(widget.author);
                        $('#authorEmail').val(widget.authorEmail);
                    },
                    errorCallback: function(){
                        alert(ravePortal.getClientMessage("api.widget_metadata.parse_error"));
                    },
                    alertInvalidParams: function(){
                        alert(ravePortal.getClientMessage("api.widget_metadata.invalid_params"));
                    }
                })
            })
        };

        function regionWidgetEventBindings(){
            $('.addIframeOverlaysLink').click(function(event){
                raveLayout.addIframeOverlays(event)
            })
        };

        /********************************
         Helper Functions
         ********************************/
        function addWidgetToPageCallback (result){
            var widgetTitle = ravePortal.getClientMessage("widget.add_prefix");
            var addedWidget = result != undefined ? result.widgetId : undefined;

            if (addedWidget != undefined && addedWidget.title != undefined && addedWidget.title.length > 0) {
                widgetTitle = addedWidget.title;
            }
            ravePortal.showInfoMessage(widgetTitle + ' ' + ravePortal.getClientMessage("widget.add_suffix"));

            // Update Add Widget button to reflect status
            var addWidgetButton = "#addWidget_" + args.widgetId;
            var addedText = '<i class="icon icon-ok icon-white"></i> ' + $(addWidgetButton).data('success');

            $(addWidgetButton).removeClass("btn-primary").addClass("btn-success").html(addedText);
        }




        return {
            bindEvents: function(pageName){
                var bindEventsForPage = pageNameToFunctionMap[pageName];
                bindEventsForPage();
            }
        }
})
