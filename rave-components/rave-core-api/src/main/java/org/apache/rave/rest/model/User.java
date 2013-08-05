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
@XmlType(name = "User", propOrder = {
        "id", "username", "locked", "enabled"
})
@XmlRootElement(name = "User")
public class User  implements RestEntity{
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "Username")
    private String username;
    @XmlElement(name = "Locked")
    private Boolean locked;
    @XmlElement(name = "Enabled")
    private Boolean enabled;

    public User(){}

    public User(org.apache.rave.model.User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.locked = user.isLocked();
        this.enabled = user.isEnabled();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void saveToUser(org.apache.rave.model.User user) {
        if (user.getId() != null && !user.getId().equals(id)) {
            throw new RuntimeException("You cannot change the ID of a User object");
        }

        if (username != null)  { user.setUsername(username); }
        if (locked != null)  { user.setLocked(locked); }
        if (enabled != null)  { user.setEnabled(enabled); }
    }

}
