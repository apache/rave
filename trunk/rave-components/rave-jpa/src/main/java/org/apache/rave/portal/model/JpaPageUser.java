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
package org.apache.rave.portal.model;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.rave.model.Page;
import org.apache.rave.model.PageInvitationStatus;
import org.apache.rave.model.PageUser;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.conversion.JpaConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
@Access(AccessType.FIELD)
@Table(name = "page_user", uniqueConstraints={@UniqueConstraint(columnNames={"page_id","user_id"})})
@NamedQueries({
        @NamedQuery(name = JpaPageUser.GET_BY_USER_ID_AND_PAGE_TYPE, query="SELECT p.page FROM JpaPageUser p, JpaPage q WHERE p.page.entityId = q.entityId and p.userId = :userId and q.pageType = :pageType ORDER BY p.renderSequence"),
        @NamedQuery(name = JpaPageUser.GET_PAGES_FOR_USER, query="SELECT p FROM JpaPageUser p, JpaPage q WHERE p.page.entityId = q.entityId and p.userId = :userId and q.pageType = :pageType ORDER BY p.renderSequence"),
        @NamedQuery(name = JpaPageUser.GET_SINGLE_RECORD, query="SELECT p FROM JpaPageUser p WHERE p.userId = :userId and p.page.entityId = :pageId")
})
public class JpaPageUser implements BasicEntity, Serializable, PageUser {
    private static final long serialVersionUID = 1L;

    public static final String GET_BY_USER_ID_AND_PAGE_TYPE ="JpaPageUser.getByUserIdAndPageType";
    public static final String GET_PAGES_FOR_USER = "JpaPageUser.getPagesForUser";
    public static final String GET_SINGLE_RECORD = "JpaPageUser.getSingleRecord";

    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageUserIdGenerator")
    @TableGenerator(name = "pageUserIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page_user", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "page_id")
    private JpaPage page;

    @Basic(optional=false) @Column(name="editor")
    private boolean editor;

    @Basic(optional=false) @Column(name="render_sequence")
    private Long renderSequence;

    @Basic
    @Column(name = "page_status")
    @Enumerated(EnumType.STRING)
    private PageInvitationStatus pageStatus;

    public JpaPageUser(){}

    public JpaPageUser(User user, Page page){
        this.setUserId(user.getId());
        setPage(page);
    }

    public JpaPageUser(User user, Page page, long sequence){
        this.userId = user.getId();
        setPage(page);
        this.renderSequence = sequence;
    }


    public JpaPageUser(String userId, Page page, long sequence){
        this.userId = userId;
        setPage(page);
        this.renderSequence = sequence;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }

    /**
	 * @return the editor
	 */
	@Override
    public boolean isEditor() {
        return editor;
	}

    /**
    * @param editor the editor to set
    */
    @Override
    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    /**
    * @return the userId
    */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
    * @return the page
    */
    @Override
    @JsonBackReference
    public Page getPage() {
        return page;
    }

    /**
    * @param page the page to set
    */
    @Override
    public void setPage(Page page) {
        this.page = JpaConverter.getInstance().convert(page, Page.class);
    }

    /**
     * Gets the order of the page instance relative to all pages for the owner (useful when presenting pages in an
     * ordered layout like tabs or an accordion container)
     *
     * @return Valid, unique render sequence
     */
    @Override
    public Long getRenderSequence() {
        return renderSequence;
    }

    @Override
    public void setRenderSequence(Long renderSequence) {
        this.renderSequence = renderSequence;
    }

    /**
     * Get the page status - used in shared states where a page can be pending, refused or accepted
     * Only applies to shared pages
     * @return an enum type
     */
    @Override
    public PageInvitationStatus getPageStatus() {
        return pageStatus;
    }

    @Override
    public void setPageStatus(PageInvitationStatus pageStatus) {
        this.pageStatus = pageStatus;
    }
}
