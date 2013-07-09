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
As the user john.doe
I want to login to my account
So that I can view my pages

Scenario: John Doe logs into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I log in with username "john.doe" and password "john.doe"
Then I see the message "Hello John Doe, welcome to Rave!" for the user "john.doe"
When I log out
Then I see the Rave login page

Scenario: John Doe updates his profile
When I log in with username "john.doe" and password "john.doe"
And I go to "http://localhost:8080/portal/app/person/john.doe"
Then I see the email address "john.doe@example.com" on the profile page
And I see the about me "" on the profile page
And I see the status "Single" on the profile page
When I click on the "profileEdit" button
Then I can edit the email address
When I change the email address to "john.doe@example.net"
And I change the about me to "I'm a test user"
And I choose the status as "Committed"
And I submit the edit profile form
Then I see the email address "john.doe@example.net" on the profile page
And I see the about me "I'm a test user" on the profile page
And I see the status "Committed" on the profile page
When I log out

Scenario: John Doe reverts his profile after logging out and in
When I log in with username "john.doe" and password "john.doe"
And I go to "http://localhost:8080/portal/app/person/john.doe"
Then I see the email address "john.doe@example.net" on the profile page
And I see the about me "I'm a test user" on the profile page
And I see the status "Committed" on the profile page
When I click on the "profileEdit" button
Then I can edit the email address
When I change the email address to "john.doe@example.com"
And I change the about me to ""
And I choose the status as "Single"
And I submit the edit profile form
Then I see the email address "john.doe@example.com" on the profile page
And I see the about me "" on the profile page
And I see the status "Single" on the profile page
When I log out

Scenario: John Doe adds a new page
When I log in with username "john.doe" and password "john.doe"
And I click the add page button
And I enter the title "Auto test"
And I choose the two column layout
And I add the page
Then the new page with title "Auto test" is selected
When I delete the current page
When I log out
