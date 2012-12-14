/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 3:40 PM
 */
public class MongoDbWidgetCommentTest {
    private MongoDbWidgetComment comment;
    private UserRepository userRepository;

    @Before
    public void setup(){
        comment = new MongoDbWidgetComment();
        userRepository = createMock(UserRepository.class);
        comment.setUserRepository(userRepository);
    }

    @Test
    public void getUser_Valid(){
        comment.setUserId((long)123);
        User user = new UserImpl();
        expect(userRepository.get((long)123)).andReturn(user);
        replay(userRepository);

        assertThat(comment.getUser(), is(sameInstance(user)));
    }

    @Test
    public void getUser_Not_Null(){
        User user = new UserImpl();
        comment.setUser(user);
        assertThat(comment.getUser(), is(sameInstance(user)));
    }
}
