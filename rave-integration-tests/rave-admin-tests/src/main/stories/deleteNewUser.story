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
As an admin
I want to delete an account
So that I can clean up test accounts.

Scenario: Admin logs into the portal and deletes an account
When I go to "http://localhost:8080/portal"
Then I see the login page
When I log in as an administrator with username "canonical" and password "canonical"
Then I see the admin interface link
When I click the admin interface link
Then I see the admin interface
When I click the "Users" link
Then I get the user search form
When I search for username "newuser"
Then I see the user matches for "newuser"
When I click the link for information on "newuser"
Then I see the information for user "newuser"
When I delete the the user "newuser"
Then I see "The user profile has been removed"
When I log out
Then I see the Rave login page
