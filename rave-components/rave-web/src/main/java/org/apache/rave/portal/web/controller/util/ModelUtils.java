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
package org.apache.rave.portal.web.controller.util;

import org.apache.rave.model.Authority;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.util.CollectionUtils;

/**
 */
public class ModelUtils {

    private ModelUtils() {}


    public static User convert(UserForm form) {
        User newUser = new UserImpl(form.getId(),  form.getUsername());
        newUser.setAuthorities(CollectionUtils.<Authority>toBaseTypedCollection(form.getAuthorities()));
        newUser.setPassword(form.getPassword());
        newUser.setConfirmPassword(form.getConfirmPassword());
        newUser.setForgotPasswordHash(form.getForgotPasswordHash());
        newUser.setDefaultPageLayoutCode(form.getDefaultPageLayoutCode());
        newUser.setStatus(form.getStatus());
        newUser.setAboutMe(form.getAboutMe());
        newUser.setGivenName(form.getGivenName());
        newUser.setFamilyName(form.getFamilyName());
        newUser.setDisplayName(form.getDisplayName());
        newUser.setEmail(form.getEmail());
        newUser.setOpenId(form.getOpenId());
        newUser.setEnabled(form.isEnabled());
        newUser.setExpired(form.isExpired());
        newUser.setLocked(form.isLocked());
        return newUser;
    }
}
