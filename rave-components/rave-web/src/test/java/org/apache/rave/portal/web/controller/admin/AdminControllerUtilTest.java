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

package org.apache.rave.portal.web.controller.admin;

import org.junit.Test;
import org.springframework.web.bind.support.SessionStatus;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link AdminControllerUtil}
 */
public class AdminControllerUtilTest {

    @Test
    public void checkTokens_valid() throws Exception {
        String token = AdminControllerUtil.generateSessionToken();
        SessionStatus status = createMock(SessionStatus.class);
        AdminControllerUtil.checkTokens(token, token, status);
        assertTrue("No errors", true);
    }

    @Test(expected = SecurityException.class)
    public void checkTokens_invalidLength() throws Exception {
        String token = "token";
        SessionStatus status = createMock(SessionStatus.class);
        AdminControllerUtil.checkTokens(token, token, status);
        assertTrue("Exception occurred", false);
    }
    
    @Test(expected = SecurityException.class)
    public void checkTokens_invalidNoMatch() throws Exception {
        String token1 = AdminControllerUtil.generateSessionToken();
        String token2 = AdminControllerUtil.generateSessionToken();
        SessionStatus status = createMock(SessionStatus.class);
        AdminControllerUtil.checkTokens(token1, token2, status);
        assertTrue("Exception occurred", false);
    }
}
