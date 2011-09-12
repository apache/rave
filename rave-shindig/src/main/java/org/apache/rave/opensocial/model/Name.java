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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The name object, stored in the name table.
 */
@Embeddable
public class Name implements org.apache.shindig.social.opensocial.model.Name {

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "additional_name", length = 255)
  private String additionalName;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "family_name", length = 255)
  private String familyName;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "given_name", length = 255)
  private String givenName;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "honorific_prefix", length = 255)
  private String honorificPrefix;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "honorific_suffix", length = 255)
  private String honorificSuffix;

  /**
   * model field.
   *
   * @see org.apache.shindig.social.opensocial.model.Name
   */
  @Basic
  @Column(name = "formatted", length = 255)
  private String formatted;

  /**
   *
   */
  public Name() {
  }

  /**
   * @param formatted
   */
  public Name(String formatted) {
    this.formatted = formatted;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getAdditionalName()
   */
  public String getAdditionalName() {
    return additionalName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setAdditionalName(String)
   */
  public void setAdditionalName(String additionalName) {
    this.additionalName = additionalName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getFamilyName()
   */
  public String getFamilyName() {
    return familyName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setFamilyName(String)
   */
  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getGivenName()
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setGivenName(String)
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getHonorificPrefix()
   */
  public String getHonorificPrefix() {
    return honorificPrefix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setHonorificPrefix(String)
   */
  public void setHonorificPrefix(String honorificPrefix) {
    this.honorificPrefix = honorificPrefix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getHonorificSuffix()
   */
  public String getHonorificSuffix() {
    return honorificSuffix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setHonorificSuffix(String)
   */
  public void setHonorificSuffix(String honorificSuffix) {
    this.honorificSuffix = honorificSuffix;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#getFormatted()
   */
  public String getFormatted() {
    return formatted;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.shindig.social.opensocial.model.Name#setFormatted(String)
   */
  public void setFormatted(String formatted) {
    this.formatted = formatted;
  }
}
