!--
!-- Licensed to the Apache Software Foundation (ASF) under one
!-- or more contributor license agreements.  See the NOTICE file
!-- distributed with this work for additional information
!-- regarding copyright ownership.  The ASF licenses this file
!-- to you under the Apache License, Version 2.0 (the
!-- "License"); you may not use this file except in compliance
!-- with the License.  You may obtain a copy of the License at
!--
!--   http://www.apache.org/licenses/LICENSE-2.0
!--
!-- Unless required by applicable law or agreed to in writing,
!-- software distributed under the License is distributed on an
!-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
!-- KIND, either express or implied.  See the License for the
!-- specific language governing permissions and limitations
!-- under the License.
!--

Meta:

Narrative:
As a new user
I want to create an account
So that I can login into the portal

Scenario: User creates a new account and logs in into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I follow the new account link
Then I get the new account form
When I fill in the form with username "newuser" password "password" confirmpassword "password" email "newuser@example.com"
And I submit the new account form
Then I see the login page
And A message appears "Account successfully created"
When I fill in the login form with username "newuser" password "password"
Then I see my portal page with the new user widget page
