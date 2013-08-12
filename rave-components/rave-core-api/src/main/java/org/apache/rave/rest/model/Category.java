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

package org.apache.rave.rest.model;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Category", propOrder = {
        "id", "text"
})
@XmlRootElement(name = "Category")
public class Category implements RestEntity{
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "Text")
    private String text;

    public Category() {}

    public Category(org.apache.rave.model.Category category) {
        this.id = category.getId();
        this.text = category.getText();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void saveToCategory(org.apache.rave.model.Category category) {
        if (category.getId() != null && !category.getId().equals(id)) {
            throw new RuntimeException("You cannot change the ID of a Category object");
        }

        if (text != null)  { category.setText(text); }
    }
}
