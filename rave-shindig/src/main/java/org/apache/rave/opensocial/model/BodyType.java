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
 * The body type entity, stored in "body_type"
 */
@Embeddable
public class BodyType implements org.apache.shindig.social.opensocial.model.BodyType {

    /**
     * model field.
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType
     */
    @Basic
    @Column(name = "build", length = 255)
    private String build;

    /**
     * model field.
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType
     */
    @Basic
    @Column(name = "eye_color", length = 255)
    private String eyeColor;

    /**
     * model field.
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType
     */
    @Basic
    @Column(name = "hair_color", length = 255)
    private String hairColor;

    /**
     * model field.
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType
     */
    @Basic
    @Column(name = "height")
    private Float height;

    /**
     * model field.
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType
     */
    @Basic
    @Column(name = "weight")
    private Float weight;

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#getBuild()
     */
    public String getBuild() {
        return build;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#setBuild(String)
     */
    public void setBuild(String build) {
        this.build = build;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#getEyeColor()
     */
    public String getEyeColor() {
        return eyeColor;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#setEyeColor(String)
     */
    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#getHairColor()
     */
    public String getHairColor() {
        return hairColor;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#setHairColor(String)
     */
    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#getHeight()
     */
    public Float getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    public void setHeight(Float height) {
        this.height = height;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shindig.social.opensocial.model.BodyType#getWeight()
     */
    public Float getWeight() {
        return weight;
    }

    /**
     * {@inheritDoc}
     */
    public void setWeight(Float weight) {
        this.weight = weight;
    }

}
