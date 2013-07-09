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

import org.apache.rave.model.FriendRequestStatus;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * Represents an association between people
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "person_association",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followedby_id"}))
@NamedQueries(value = {
		@NamedQuery(name = JpaPersonAssociation.FIND_ASSOCIATION_ITEM_BY_USERNAMES, query = "select a from JpaPersonAssociation a where a.follower.username = :follower_username and a.followedby.username = :followedby_username"),
		@NamedQuery(name = JpaPersonAssociation.DELETE_ASSOCIATION_ITEMS_BY_USERID, query = "delete from JpaPersonAssociation a where a.follower.entityId = :userid or a.followedby.entityId = :userid")
})
public class JpaPersonAssociation implements BasicEntity {

    public static final String FOLLOWER_USERNAME = "follower_username";
    public static final String FOLLOWEDBY_USERNAME = "followedby_username";
    public static final String USERID = "userid";
    public static final String FIND_ASSOCIATION_ITEM_BY_USERNAMES = "PersonAssociation.findAssociationItemByUsernames";
    public static final String DELETE_ASSOCIATION_ITEMS_BY_USERID = "PersonAssociation.deleteAssociationItemsByUserid";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "personAssociationIdGenerator")
    @TableGenerator(name = "personAssociationIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "person_association", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @ManyToOne
    @JoinColumn(name="follower_id", referencedColumnName = "entity_id")
    JpaPerson follower;

    @ManyToOne
    @JoinColumn(name="followedby_id", referencedColumnName = "entity_id")
    JpaPerson followedby;

    @Basic
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public JpaPerson getFollower() {
        return follower;
    }

    public void setFollower(JpaPerson follower) {
        this.follower = follower;
    }

    public JpaPerson getFollowedby() {
        return followedby;
    }

    public void setFollowedby(JpaPerson followedby) {
        this.followedby = followedby;
    }

    public FriendRequestStatus getStatus() {
		return status;
	}

	public void setStatus(FriendRequestStatus status) {
		this.status = status;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaPersonAssociation that = (JpaPersonAssociation) o;

        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entityId != null ? entityId.hashCode() : 0;
    }
}
