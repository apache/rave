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

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;

/**
 * Represents an association between people
 */
@Entity
@Table(name = "person_association",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followed_id"})
)
public class PersonAssociation implements BasicEntity {

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "personAssociationIdGenerator")
    @TableGenerator(name = "personAssociationIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "person_association", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @OneToOne
    @JoinColumn(name="follower_id", referencedColumnName = "entity_id")
    Person follower;

    @OneToOne
    @JoinColumn(name="followed_id", referencedColumnName = "entity_id")
    Person followed;

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Person getFollower() {
        return follower;
    }

    public void setFollower(Person follower) {
        this.follower = follower;
    }

    public Person getFollowed() {
        return followed;
    }

    public void setFollowed(Person followed) {
        this.followed = followed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonAssociation that = (PersonAssociation) o;

        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entityId != null ? entityId.hashCode() : 0;
    }
}
