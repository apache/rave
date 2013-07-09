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

import org.apache.rave.model.FriendRequestStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:37 PM
 */
public class MongoDbPersonAssociationTest {

    @Test
    public void testPersonAssociation(){

        MongoDbPersonAssociation personAssociation = new MongoDbPersonAssociation();
        personAssociation.setRequestStatus(FriendRequestStatus.ACCEPTED);
        personAssociation.setPersonId("46765");

        assertThat(personAssociation.getRequestStatus(), is(equalTo(FriendRequestStatus.ACCEPTED)));
        assertThat(personAssociation.getPersonId(), is(equalTo("46765")));

    }
}
