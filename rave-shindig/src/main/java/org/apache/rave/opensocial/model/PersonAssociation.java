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

package org.apache.rave.opensocial.model;

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;

/**
 * Represents an association between people
 *
 */
@Entity
@Table(name = "person_association")
@SequenceGenerator(name="personAssocIdSeq", sequenceName = "person_association_id_seq")
public class PersonAssociation implements BasicEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personAssocIdSeq")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name="follower_id", referencedColumnName = "id")
    Person follower;

    @OneToOne
    @JoinColumn(name="followed_id", referencedColumnName = "id")
    Person followed;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
}
