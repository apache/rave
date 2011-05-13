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
package org.apache.rave.portal.model;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests the User class.
 */
public class UserTest {
	private User user1;
	private User user2;
	private User user3;
	private long id;
	private String userName;
	private String userPassword;
	private boolean user1Expired, user2Expired, user3Expired;
	private boolean user1Enabled, user2Enabled, user3Enabled;
	private boolean user1CredExpired, user2CredExpired, user3CredExpired;
	
	@Before
	public void setup(){
		id=123L;
		userName="testUser";
		userPassword="qwerty";
		user1Enabled=true;
		user2Enabled=false;
		user3Enabled=true;
		user1CredExpired=false;
		user2CredExpired=true;
		user3CredExpired=false;
		user1Expired=false;
		user2Expired=true;
		user3Expired=false;

		
		user1=new User();
		user1.setUsername(userName);
		user1.setUserId(id);
		user1.setPassword(userPassword);
		user1.setEnabled(user1Enabled);
		user1.setExpired(user1Expired);
		
		user2=new User(id);
		user2.setExpired(user2Expired);
		user2.setEnabled(user2Enabled);
		
		user3=new User(id,userName);
		user3.setExpired(user3Expired);
		user3.setEnabled(user3Enabled);
	}
	
	@Test
	public void testAccessorMethods(){
		assertTrue(user1.getUsername().equals(userName));
		assertTrue(user1.getPassword().equals(userPassword));
		assertTrue(user1.getUserId().equals(id));
		assertTrue(user1.isEnabled());
		assertFalse(user2.isEnabled());
		assertTrue(user3.isEnabled());
	}
	public void testAltConstructors(){
		assertTrue(user1.getUserId()==user2.getUserId());
		assertTrue(user1.getUserId()==user3.getUserId());
		assertTrue(user1.getUsername().equals(user3.getUsername()));
	}
	
	@Test
	public void testExpirations() {
		//Credentials and accounts seem entangled, so need tests
		//to make sure implementation doesn't chage.
		assertTrue(user1.isAccountNonExpired());
		assertFalse(user2.isAccountNonExpired());
		assertTrue(user3.isAccountNonExpired());
		assertTrue("Account and credential nonexpirations handled correctly",user1.isAccountNonExpired() && user1.isCredentialsNonExpired());
		assertTrue("Account and credential expirations handled correctly",!user2.isAccountNonExpired() && !user2.isCredentialsNonExpired());
	}
}
