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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

/**
 */
@Entity
@Table(name = "account")
public class Account implements org.apache.shindig.social.opensocial.model.Account, BasicEntity {
  /**
   * The internal object ID used for references to this object. Should be generated
   * by the underlying storage mechanism
   */
  @Id
  @Column(name = "entity_id")
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "accountIdGenerator")
  @TableGenerator(name = "accountIdGenerator", table = "RAVE_SHINDIG_SEQUENCES", pkColumnName = "SEQ_NAME",
          valueColumnName = "SEQ_COUNT", pkColumnValue = "account", allocationSize = 1, initialValue = 1)
  private Long entityId;

  /**
   * An optimistic locking field.
   */
  @Version
  @Column(name = "version")
  private long version;

  /**
   * model field.
   * @see org.apache.shindig.social.opensocial.model.Account
   */
  @Basic
  @Column(name = "domain", length = 255)
  private String domain;

  /**
   * model field.
   * @see org.apache.shindig.social.opensocial.model.Account
   */
  @Basic
  @Column(name = "user_id", length = 255)
  private String userId;

  /**
   * model field.
   * @see org.apache.shindig.social.opensocial.model.Account
   */
  @Basic
  @Column(name = "username", length = 255)
  private String username;

  /**
   * create an empty account object.
   */
  public Account() {
  }

  /**
   * Create an account object based on domain, userId and username
   * @param domain the domain of the account
   * @param userId the user entityId of the account
   * @param username the username of the account
   */
  public Account(String domain, String userId, String username) {
    this.domain = domain;
    this.userId = userId;
    this.username = username;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Account#getDomain()
   */
  public String getDomain() {
    return domain;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Account#setDomain(String)
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Account#getUserId()
   */
  public String getUserId() {
    return userId;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Account#setUserId(String)
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Account#getUsername()
   */
  public String getUsername() {
    return username;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.model.Account#setUsername(String)
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * {@inheritDoc}
   * @see org.apache.shindig.social.opensocial.jpa.api.DbObject#getObjectId()
   */
  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
      this.entityId = entityId;
  }

}
