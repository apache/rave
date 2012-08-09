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
package org.apache.rave.portal.model.impl;

import org.apache.rave.portal.model.*;

public class PageUserImpl implements PageUser {
    private String id;
    private User user;
    private Page page;
    private boolean editor;
    private Long renderSequence;
    private PageInvitationStatus pageStatus;

    public PageUserImpl(){}

    public PageUserImpl(String id){
        this.id = id;
    }

    public PageUserImpl(User user, Page page){
        this.user = user;
        this.page = page;
    }

    public PageUserImpl(User user, Page page, long sequence){
        this.user = user;
        this.page = page;
        this.renderSequence = sequence;
    }


    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public boolean isEditor() {
        return editor;
    }

    @Override
    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    @Override
    public Long getRenderSequence() {
        return renderSequence;
    }

    @Override
    public void setRenderSequence(Long renderSequence) {
        this.renderSequence = renderSequence;
    }

    @Override
    public PageInvitationStatus getPageStatus() {
        return pageStatus;
    }

    @Override
    public void setPageStatus(PageInvitationStatus pageStatus) {
        this.pageStatus = pageStatus;
    }
}
