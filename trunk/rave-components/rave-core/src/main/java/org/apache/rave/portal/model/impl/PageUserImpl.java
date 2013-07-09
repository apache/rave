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

import org.apache.rave.model.Page;
import org.apache.rave.model.PageInvitationStatus;
import org.apache.rave.model.PageUser;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class PageUserImpl implements PageUser {
    private String id;
    private String userId;
    private boolean editor;
    private Long renderSequence;
    private PageInvitationStatus pageStatus;

    @JsonBackReference
    private Page page;

    public PageUserImpl(){}

    public PageUserImpl(String id){
        this.id = id;
    }

    public PageUserImpl(String userId, Page page){
        this.userId = userId;
        this.page = page;
    }

    public PageUserImpl(String userId, Page page, long sequence){
        this.userId = userId;
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
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String user) {
        this.userId = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageUserImpl)) return false;

        PageUserImpl pageUser = (PageUserImpl) o;

        if (editor != pageUser.editor) return false;
        if (id != null ? !id.equals(pageUser.id) : pageUser.id != null) return false;
        if (page != null ? !page.equals(pageUser.page) : pageUser.page != null) return false;
        if (pageStatus != pageUser.pageStatus) return false;
        if (renderSequence != null ? !renderSequence.equals(pageUser.renderSequence) : pageUser.renderSequence != null)
            return false;
        if (userId != null ? !userId.equals(pageUser.userId) : pageUser.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (editor ? 1 : 0);
        result = 31 * result + (renderSequence != null ? renderSequence.hashCode() : 0);
        result = 31 * result + (pageStatus != null ? pageStatus.hashCode() : 0);
        result = 31 * result + (page != null ? page.hashCode() : 0);
        return result;
    }
}
