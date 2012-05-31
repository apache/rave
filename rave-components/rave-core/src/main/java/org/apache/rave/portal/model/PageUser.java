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

import org.apache.rave.persistence.BasicEntity;
import org.codehaus.jackson.annotate.JsonBackReference;
@Entity
@Access(AccessType.FIELD)
@Table(name = "page_user", uniqueConstraints={@UniqueConstraint(columnNames={"page_id","user_id"})})

@NamedQueries({
        @NamedQuery(name = PageUser.GET_BY_USER_ID_AND_PAGE_TYPE, query="SELECT p.page FROM PageUser p, Page q WHERE p.page.entityId = q.entityId and p.user.entityId = :userId and q.pageType = :pageType ORDER BY p.renderSequence"),
        @NamedQuery(name = PageUser.GET_PAGES_FOR_USER, query="SELECT p FROM PageUser p, Page q WHERE p.page.entityId = q.entityId and p.user.entityId = :userId and q.pageType = :pageType ORDER BY p.renderSequence"),
        @NamedQuery(name = PageUser.GET_SINGLE_RECORD, query="SELECT p FROM PageUser p WHERE p.user.entityId = :userId and p.page.entityId = :pageId")
})
public class PageUser implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String GET_BY_USER_ID_AND_PAGE_TYPE ="PageUser.getByUserIdAndPageType";
    public static final String GET_PAGES_FOR_USER = "PageUser.getPagesForUser";
    public static final String GET_SINGLE_RECORD = "PageUser.getSingleRecord";
    
    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageUserIdGenerator")
    @TableGenerator(name = "pageUserIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page_user", allocationSize = 1, initialValue = 1)
            
    private Long entityId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "page_id", nullable=false)
    private Page page;
    
    private boolean editor;
    
    @Basic(optional=false) @Column(name="render_sequence")
    private Long renderSequence;
    
    @Basic
    @Column(name = "page_status")
    @Enumerated(EnumType.STRING)
    private PageInvitationStatus pageStatus;
    
    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
	
    /**
	 * @return the editor
	 */
	public boolean isEditor() {
        return editor;
	}

    /**
    * @param editor the editor to set
    */
    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    /**
    * @return the user
    */
    public User getUser() {
        return user;
    }

    /**
    * @param user the user to set
    */
    public void setUser(User user) {
        this.user = user;
    }

    /**
    * @return the page
    */
    @JsonBackReference
    public Page getPage() {
        return page;
    }

    /**
    * @param page the page to set
    */
    public void setPage(Page page) {
        this.page = page;
    }

    /**
     * Gets the order of the page instance relative to all pages for the owner (useful when presenting pages in an
     * ordered layout like tabs or an accordion container)
     *
     * @return Valid, unique render sequence
     */
    public Long getRenderSequence() {
        return renderSequence;
    }

    public void setRenderSequence(Long renderSequence) {
        this.renderSequence = renderSequence;
    }

    /**
     * Get the page status - used in shared states where a page can be pending, refused or accepted
     * Only applies to shared pages
     * @return an enum type
     */
    public PageInvitationStatus getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(PageInvitationStatus pageStatus) {
        this.pageStatus = pageStatus;
    }
    
    public PageUser(){}
    
    public PageUser(User user, Page page){
        this.user = user;
        this.page = page;
    }

    public PageUser(User user, Page page, long sequence){
        this.user = user;
        this.page = page;
        this.renderSequence = sequence;
    }
}
