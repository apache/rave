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

Scenario: Admin logs into the portal and views all the existing categories
When I go to "http://localhost:8080/portal"
Then I see the login page
When I log in as an administrator with username "canonical" and password "canonical"
Then I see the admin interface link
When I click the admin interface link
Then I see the admin interface
When I click the "Categories" link
Then I see the categories list table

Scenario: Admin creates a new category
Given I see the create category form
When I fill in a new category named "TestCategory"
And I click the "Create Category" button
Then I see the categories list table
And I see a category called "TestCategory" in the categories list table

Scenario: Admin updates "TestCategory" and renames it to "My Category"
Given I see the categories list table
And I see a category called "TestCategory" in the categories list table
When I click on the category named "TestCategory"
Then I see the category details page
When I change the name of the category to "My Category"
And I click the "Update Category" button
Then I see the categories list table
And I see a category called "My Category" in the categories list table
And I do not see a category called "Test Category" in the categories list table

Scenario: Admin deletes "My Category"
Given I see the categories list table
And I see a category called "My Category" in the categories list table
When I click on the category named "My Category"
Then I see the category details page
When I select the delete checkbox
And I click the "Delete Category" button
Then I see the categories list table
And I do not see a category called "My Category" in the categories list table

Scenario: Admin logs out
Given I see the categories list table
When I log out
Then I see the Rave login page