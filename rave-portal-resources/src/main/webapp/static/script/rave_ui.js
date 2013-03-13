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

rave.ui = rave.ui || (function () {
    var exports = {};

    /*
     Register View Helpers
     */

    Handlebars.registerHelper('getClientMessage', function (key) {
        return rave.getClientMessage(key);
    })

    /*
     Register templates
     */
    var templates = exports.templates = {};
    $('[data-template-for]').each(function () {
        var key = $(this).data('template-for');
        var source = $(this).html();

        templates[key] = Handlebars.compile(source);
    });

    /*
     Build rave client side views
     */
    var views = exports.views = {};

    /*
     View for managing sharing / cloning of pages
     */
    var PageSharingModalView = rave.View.extend({
        template:templates['user-search-view'],
        //the bootstrap modal div
        modalDiv:$('#sharePageDialog'),
        //attach point for this view
        container:$('#sharePageDialogContent'),

        models:{
            page:rave.models.currentPage,
            users:rave.models.users
        },

        initialize:function () {
            var page = this.models.page;
            var users = this.models.users;

            this.constructor.__super__.initialize.apply(this);

            page.on('share', this.flash, this);
            page.on('error', this.handleError, this);

            //whenever the modal is displayed reset the view
            this.modalDiv.on('show', function () {
                users.fetchPage(1);
            });

            //extend users toViewModel function to include share properties
            this.models.users.toViewModel = _.wrap(this.models.users.toViewModel, function (toViewModel) {
                var model = toViewModel.apply(this);

                _.each(model.users, function (user) {
                    user.isOwner = page.isUserOwner(user.id);
                    user.hasShare = page.isUserMember(user.id);
                    user.hasEdit = page.isUserEditor(user.id);
                });

                return model;
            });

            this.container.html(this.$el);
        },

        events:{
            'click #shareSearchButton':'search',
            'keypress #searchTerm':'search',
            'click #clearSearchButton':'clearSearch',
            'click #pagingul a':'page',
            'click .searchResultRecord a':'shareAction'
        },

        search:function (e) {
            //allow search function to trigger from enter keypress or button click
            if (e.which == 13 || _.isUndefined(e.which)) {
                var term = $('#searchTerm', this.$el).val();

                this.models.users.filter(term);
            }
        },

        clearSearch:function (e) {
            this.models.users.filter(null);
        },

        page:function (e) {
            var page = $(e.target).data('pagenumber');

            this.models.users.fetchPage(page);
        },

        //manages any add / remove share, editor or clone actions
        shareAction:function (e) {
            var userId = $(e.target).data('userid');
            var action = $(e.target).data('action');

            this.models.page[action](userId);
        },

        //flash success messages
        flash:function (event, userId) {
            var eventsToMessages = {
                'member:add': 'create.share',
                'member:remove': 'revoke.share',
                'editor:add': '',
                'editor:remove': '',
                'clone':'success.clone.page'
            }

            var msg = eventsToMessages[event];

            if(msg){
                var message = rave.getClientMessage(msg);
                var user = this.models.users.get(userId);
                rave.showInfoMessage('('+user.get('username')+') '+message);
            }
        },

        //TODO: deal with errors better. Until we have a better api or there is another view for this modal,
        //manually manage the form
        handleError: function(errorCode, userId){
            var self = this;

            if (errorCode == 'DUPLICATE_ITEM') {
                this.modalDiv.modal('hide');
                $("#pageMenuDialogHeader").html(rave.getClientMessage("page.update"));
                $("#pageFormErrors").html(rave.getClientMessage("page.duplicate_name"));
                $("#pageLayoutGroup").hide();
                var $pageMenuUpdateButton = $("#pageMenuUpdateButton");
                $pageMenuUpdateButton.html(rave.getClientMessage("common.save"));
                // unbind the previous click event since we are sharing the
                // dialog between separate add/edit page actions
                $pageMenuUpdateButton.unbind('click');
                $pageMenuUpdateButton.click(function () {
                    var $pageForm = $("#pageForm");
                    var $tab_title_input = $("#tab_title");
                    if ($pageForm.valid()) {
                        self.models.page.cloneForUser(userId, $tab_title_input.val());
                        $("#pageMenuDialog").modal('hide');
                    }
                });
                $('#pageMenuDialog').on('shown', function () {
                    $("#tab_title").first().focus();
                });
                //
                $("#pageMenuDialog").modal('show');
            } else {
                $("#pageMenuDialog").modal('hide');
                alert(rave.getClientMessage("api.rpc.error.internal"));
            }
        }
    });

    views.pageSharingModal = new PageSharingModalView();

    //TODO: root app view, will be expanded significantly
    var App = rave.View.extend({
        models: {
            page: rave.models.currentPage
        },

        initialize: function(){
            this.models.page.on('acceptShare', rave.viewPage);
            this.models.page.on('declineShare', function(){
                //TODO: this doesn't actually work - but it's what current code did. Need to replace with a navigate to default page function
                document.location.href = '/';
            });
        }
    });

    views.app = new App();

    return exports;
})();