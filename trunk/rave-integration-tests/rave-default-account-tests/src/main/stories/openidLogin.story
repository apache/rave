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
As the user of an OpenID account 
I want to login to my account using OpenID
So that I can view my pages

Scenario: User with OpenID logs into the portal
When I go to "http://localhost:8080/portal"
Then I see the login page
When I provide my OpenID identity "http://rave2011.myopenid.com/"
Then I see the OpenID authentication page
When I provide my OpenID password "rave2011"
Then I see the message "Hello rave2011.myopenid.com, welcome to Rave!" for the user "rave2011.myopenid.com"
When I log out
Then I see the Rave login page
