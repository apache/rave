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
As the user johnldap
I want to login to my account
So that I can view my pages

Scenario: John Doe logs into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I log in with username "johnldap" and password "johnldap"
Then I see the message "Hello John Ldap, welcome to Rave!" for the user "johnldap"
When I log out
Then I see the Rave login page
