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
 * Time: 2:02 PM
 */
public class MongoDbWidgetTagTest {
    private UserRepository userRepository;
    private MongoDbWidgetTag widgetTag;

    @Before
    public void setup(){
        userRepository = createMock(UserRepository.class);
        widgetTag = new MongoDbWidgetTag();
        widgetTag.setUserRepository(userRepository);
    }

    @Test
    public void getUser_Null_User(){
        User user = new UserImpl();
        widgetTag.setUserId((long)123);
         expect(userRepository.get((long)123)).andReturn(user);
        replay(userRepository);

        assertThat(widgetTag.getUser(), is(sameInstance(user)));
    }

    @Test
    public void getUser_Set_User(){
        User user = new UserImpl();
        widgetTag.setUser(user);
        assertThat(user, is(sameInstance(widgetTag.getUser())));
    }
}
