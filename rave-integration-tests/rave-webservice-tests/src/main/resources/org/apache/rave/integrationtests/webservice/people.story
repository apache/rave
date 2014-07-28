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

Scenario: The system is set to a known state

Given the user "canonical" with the password of "canonical" is logged into the system expecting "JSON"
Then there should be "13" people in the system


Scenario: The user finds their record in the list and retrieves their full record

Given the system contains "13" people
When the user retrieves the full person record for the display name of "Canonical User"
Then the "username" field on the active person object is "canonical"
And the "emailAddress" field on the active person object is "canonical@example.com"
And the "displayName" field on the active person object is "Canonical User"
And the "aboutMe" field on the active person object is null
And the "preferredName" field on the active person object is null
And the "status" field on the active person object is "Single"
And the "additionalName" field on the active person object is "canonical"
And the "familyName" field on the active person object is "User"
And the "givenName" field on the active person object is "Canonical"
And the "honorificPrefix" field on the active person object is null
And the "honorificSuffix" field on the active person object is null