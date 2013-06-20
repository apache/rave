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

//All set!!

define(["underscore", "./rave_backbone", "core/rave_api"], function(_, raveBackbone, api){
    /*
     User model. Further implementation pending.
     */
    var User = raveBackbone.Model.extend({

    });

    /*
     Collection of users. Currently used for the share page users search.
     */
    var Users = raveBackbone.Collection.extend({
        model: User,
        pageSize: 10,

        //empty pagination data, is filled in on requests
        paginationData: {
            start:0,
            finish: 0,
            total: 0,
            prevLink: {},
            nextLink: {},
            pages: []
        },

        initialize: function(){
            //ensure that parse is always invoked in the context of this object
            _.bindAll(this, 'parse');
        },

        //filter collection with a search term
        filter: function (term) {
            this.searchTerm = term;

            if (this.searchTerm) {
                api.rpc.searchUsers({searchTerm: this.searchTerm, offset: 0, successCallback: this.parse });
            }
            else {
                api.rpc.getUsers({offset: 0, successCallback: this.parse });
            }
        },

        //used for pagination
        fetchPage: function (page) {
            var self = this;

            var offset = page?(page-1):0;
            offset *= this.pageSize;

            if (this.searchTerm) {
                api.rpc.searchUsers({searchTerm: this.searchTerm, offset: offset, successCallback: this.parse });
            }
            else {
                api.rpc.getUsers({offset: offset, successCallback: this.parse });
            }
        },

        //parse return data from the rpc call into a usable data model
        parse: function (data) {
            var result = data.result;
            this.pageSize = result.pageSize || 10;

            this.paginationData = {
                start: result.offset + 1,
                finish: result.resultSet.length + result.offset,
                total: result.totalResults,
                pageSize: result.pageSize,
                prevLink: {
                    show: result.currentPage > 1 ? true : false,
                    pageNumber: result.currentPage - 1
                },
                nextLink: {
                    show: result.currentPage < result.numberOfPages ? true : false,
                    pageNumber: result.currentPage + 1
                },
                //pages will be an array of objects from 1 to number of pages
                pages: _.map(_.range(1, result.numberOfPages + 1), function (pageNumber) {
                    return {
                        pageNumber: pageNumber,
                        current: pageNumber == result.currentPage
                    }
                })
            }

            this.reset(result.resultSet);
        },

        //When toViewModel is invoked, also provide pagination and filter data
        toViewModel: function () {
            return {
                searchTerm: this.searchTerm,
                pagination: this.paginationData,
                users: this.constructor.__super__.toViewModel.apply(this)
            }
        }
    });

    /*
     Page model. Used for managing most of the sharing functionality.
     */
    var Page = raveBackbone.Model.extend({

        defaults: {
            members: {}
        },

        /*
         TODO: currently this is used to silently bootstrap the page model from the page view. Once
         the jsp views are lightened up we should be able to provide a full representation of the page
         model to pass to .set() and this should not be needed.
         */
        addInitData: function (userId, isEditor) {
            var members = this.get('members');

            members[userId] = {
                userId: userId,
                editor: isEditor
            }

            this.set('members', members, {silent:true});
        },

        isUserOwner: function (userId) {
            return userId == this.get('ownerId');
        },

        isUserView: function(userId) {
            return userId == this.get('viewerId');
        },

        isUserMember: function (userId) {
            return this.get('members')[userId] ? true : false;
        },

        isUserEditor: function (userId) {
            var member = this.get('members')[userId];
            return member && member.editor;
        },

        addMember: function (userId) {
            var self = this;

            api.rpc.addMemberToPage({pageId: self.get('id'), userId: userId,
                successCallback: function (result) {
                    var members = self.get('members');

                    members[userId] = {
                        userId: userId,
                        editor: false
                    }

                    self.set('members', members);
                    /*
                     The model does not manage or care about views. Instead it fires events
                     that views can subscribe to for ui representation.
                     TODO: solidify and document eventing model
                     */
                    self.trigger('share', 'member:add', userId);
                }
            });

        },

        removeMember: function (userId) {
            var self = this;

            api.rpc.removeMemberFromPage({pageId: self.get('id'), userId: userId,
                successCallback: function (result) {
                    var members = self.get('members');

                    delete members[userId];

                    self.set('members', members);
                    self.trigger('share', 'member:remove', userId);
                }
            });
        },

        removeForSelf: function(){
            var self = this;

            api.rpc.removeMemberFromPage({pageId: self.get('id'), userId: self.get('viewerId'),
                successCallback: function () {
                    self.trigger('declineShare', self.get('id'));
                }
            });
        },

        addEditor: function (userId) {
            var self = this;
            //updatePageEditingStatus
            api.rpc.updatePageEditingStatus({pageId: self.get('id'), userId: userId, isEditor: true,
                successCallback: function () {
                    var members = self.get('members');

                    members[userId] = {
                        userId: userId,
                        editor: true
                    }

                    self.set('members', members);
                    self.trigger('share', 'editor:add', userId);
                }
            });
        },

        removeEditor: function (userId) {
            var self = this;
            api.rpc.updatePageEditingStatus({pageId: self.get('id'), userId: userId, isEditor: false,
                successCallback: function () {

                    var members = self.get('members');

                    members[userId] = {
                        userId: userId,
                        editor: false
                    }

                    self.set('members', members);
                    self.trigger('share', 'editor:remove', userId);
                }
            });
        },

        cloneForUser: function (userId, pageName) {
            pageName = pageName || null;
            var self =this;
            api.rpc.clonePageForUser({pageId: this.get('id'), userId: userId, pageName: pageName,
                successCallback: function(result){
                    if(result.error) {
                        /*
                         TODO: this is a weird error handling condition used by clone to catch duplicate
                         named pages. Firing an event and letting the view handle for now, but the api
                         should be managing errors better.
                         */
                        return self.trigger('error', result.errorCode, userId);
                    }
                    self.trigger('share', 'clone', userId);
                }
            });
        },

        acceptShare: function(){
            var self = this;
            api.rpc.updateSharedPageStatus({pageId: this.get('id'), shareStatus: 'accepted',
                successCallback: function (result) {
                    self.trigger('acceptShare', self.get('id'));
                }
            });
        },

        declineShare: function(){
            var self = this;

            api.rpc.updateSharedPageStatus({pageId: this.get('id'), shareStatus: 'refused',
                successCallback: function (result) {
                    api.rpc.removeMemberFromPage({pageId: self.get('id'), userId: self.get('viewerId'),
                        successCallback: function (result) {
                            self.trigger('declineShare', self.get('id'));
                        }
                    });
                }
            })
        }
    });

    return {
        currentPage: new Page(),
        users: new Users()
    }
})

/*
var rave = rave || {};

rave.models = (function () {

    */
/*
     User model. Further implementation pending.
     *//*

    var User = rave.Model.extend({

    });

    */
/*
     Collection of users. Currently used for the share page users search.
     *//*

    var Users = rave.Collection.extend({
        model: User,
        pageSize: 10,

        //empty pagination data, is filled in on requests
        paginationData: {
            start:0,
            finish: 0,
            total: 0,
            prevLink: {},
            nextLink: {},
            pages: []
        },

        initialize: function(){
            //ensure that parse is always invoked in the context of this object
            _.bindAll(this, 'parse');
        },

        //filter collection with a search term
        filter: function (term) {
            this.searchTerm = term;

            if (this.searchTerm) {
                rave.api.rpc.searchUsers({searchTerm: this.searchTerm, offset: 0, successCallback: this.parse });
            }
            else {
                rave.api.rpc.getUsers({offset: 0, successCallback: this.parse });
            }
        },

        //used for pagination
        fetchPage: function (page) {
            var self = this;

            var offset = page?(page-1):0;
            offset *= this.pageSize;

            if (this.searchTerm) {
                rave.api.rpc.searchUsers({searchTerm: this.searchTerm, offset: offset, successCallback: this.parse });
            }
            else {
                rave.api.rpc.getUsers({offset: offset, successCallback: this.parse });
            }
        },

        //parse return data from the rpc call into a usable data model
        parse: function (data) {
            var result = data.result;
            this.pageSize = result.pageSize || 10;

            this.paginationData = {
                start: result.offset + 1,
                finish: result.resultSet.length + result.offset,
                total: result.totalResults,
                pageSize: result.pageSize,
                prevLink: {
                    show: result.currentPage > 1 ? true : false,
                    pageNumber: result.currentPage - 1
                },
                nextLink: {
                    show: result.currentPage < result.numberOfPages ? true : false,
                    pageNumber: result.currentPage + 1
                },
                //pages will be an array of objects from 1 to number of pages
                pages: _.map(_.range(1, result.numberOfPages + 1), function (pageNumber) {
                    return {
                        pageNumber: pageNumber,
                        current: pageNumber == result.currentPage
                    }
                })
            }

            this.reset(result.resultSet);
        },

        //When toViewModel is invoked, also provide pagination and filter data
        toViewModel: function () {
            return {
                searchTerm: this.searchTerm,
                pagination: this.paginationData,
                users: this.constructor.__super__.toViewModel.apply(this)
            }
        }
    });

    */
/*
     Page model. Used for managing most of the sharing functionality.
     *//*

    var Page = rave.Model.extend({

        defaults: {
            members: {}
        },

        */
/*
         TODO: currently this is used to silently bootstrap the page model from the page view. Once
         the jsp views are lightened up we should be able to provide a full representation of the page
         model to pass to .set() and this should not be needed.
         *//*

        addInitData: function (userId, isEditor) {
            var members = this.get('members');

            members[userId] = {
                userId: userId,
                editor: isEditor
            }

            this.set('members', members, {silent:true});
        },

        isUserOwner: function (userId) {
            return userId == this.get('ownerId');
        },

        isUserView: function(userId) {
            return userId == this.get('viewerId');
        },

        isUserMember: function (userId) {
            return this.get('members')[userId] ? true : false;
        },

        isUserEditor: function (userId) {
            var member = this.get('members')[userId];
            return member && member.editor;
        },

        addMember: function (userId) {
            var self = this;

            rave.api.rpc.addMemberToPage({pageId: self.get('id'), userId: userId,
                successCallback: function (result) {
                    var members = self.get('members');

                    members[userId] = {
                        userId: userId,
                        editor: false
                    }

                    self.set('members', members);
                    */
/*
                     The model does not manage or care about views. Instead it fires events
                     that views can subscribe to for ui representation.
                     TODO: solidify and document eventing model
                     *//*

                    self.trigger('share', 'member:add', userId);
                }
            });

        },

        removeMember: function (userId) {
            var self = this;

            rave.api.rpc.removeMemberFromPage({pageId: self.get('id'), userId: userId,
                successCallback: function (result) {
                    var members = self.get('members');

                    delete members[userId];

                    self.set('members', members);
                    self.trigger('share', 'member:remove', userId);
                }
            });
        },

        removeForSelf: function(){
            var self = this;

            rave.api.rpc.removeMemberFromPage({pageId: self.get('id'), userId: self.get('viewerId'),
                successCallback: function () {
                    self.trigger('declineShare', self.get('id'));
                }
            });
        },

        addEditor: function (userId) {
            var self = this;
            //updatePageEditingStatus
            rave.api.rpc.updatePageEditingStatus({pageId: self.get('id'), userId: userId, isEditor: true,
                successCallback: function () {
                    var members = self.get('members');

                    members[userId] = {
                        userId: userId,
                        editor: true
                    }

                    self.set('members', members);
                    self.trigger('share', 'editor:add', userId);
                }
            });
        },

        removeEditor: function (userId) {
            var self = this;
            rave.api.rpc.updatePageEditingStatus({pageId: self.get('id'), userId: userId, isEditor: false,
                successCallback: function () {

                    var members = self.get('members');

                    members[userId] = {
                        userId: userId,
                        editor: false
                    }

                    self.set('members', members);
                    self.trigger('share', 'editor:remove', userId);
                }
            });
        },

        cloneForUser: function (userId, pageName) {
            pageName = pageName || null;
            var self =this;
            rave.api.rpc.clonePageForUser({pageId: this.get('id'), userId: userId, pageName: pageName,
                successCallback: function(result){
                    if(result.error) {
                        */
/*
                         TODO: this is a weird error handling condition used by clone to catch duplicate
                         named pages. Firing an event and letting the view handle for now, but the api
                         should be managing errors better.
                         *//*

                        return self.trigger('error', result.errorCode, userId);
                    }
                    self.trigger('share', 'clone', userId);
                }
            });
        },

        acceptShare: function(){
            var self = this;
            rave.api.rpc.updateSharedPageStatus({pageId: this.get('id'), shareStatus: 'accepted',
                successCallback: function (result) {
                    self.trigger('acceptShare', self.get('id'));
                }
            });
        },

        declineShare: function(){
            var self = this;

            rave.api.rpc.updateSharedPageStatus({pageId: this.get('id'), shareStatus: 'refused',
                successCallback: function (result) {
                    rave.api.rpc.removeMemberFromPage({pageId: self.get('id'), userId: self.get('viewerId'),
                        successCallback: function (result) {
                            self.trigger('declineShare', self.get('id'));
                        }
                    });
                }
            })
        }
    });

    return {
        currentPage: new Page(),
        users: new Users()
    }

})();

*/

