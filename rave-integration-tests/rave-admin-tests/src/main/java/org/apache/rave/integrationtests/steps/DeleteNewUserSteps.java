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

package org.apache.rave.integrationtests.steps;

import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.rave.integrationtests.pages.Portal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

/**
 * Steps for the new user stories
 */
@Step
public class DeleteNewUserSteps {

    @Autowired
    private Portal portal;

    @When("I go to \"$url\"")
    public void goTo(String url) {
        portal.go(url);
    }

    @Then("I see the login page")
    public void isLoginPage() {
        final WebElement title = portal.findElement(By.tagName("title"));
        assertThat(title.getText().trim(), equalTo("Login - Rave"));
    }

    @When("I log in as an administrator with username \"$username\" and password \"$password\"")
    public void loginAsAdmin(String username, String password) {
        portal.login(username,password);
    }

    @Then("I see the admin interface link")
    public void getAdminInterfaceLink() {
		  //TODO: make sure the admin interface actually is loaded
		  final WebElement adminInterfaceLink=portal.findElement(By.linkText("Admin interface"));
    }
	 
	 @When("I click the admin interface link")
	 public void clickAdminInterfaceLink() {
		  //TODO: there should be consequences if this fails.
		  final WebElement adminInterfaceLink=portal.findElement(By.linkText("Admin interface"));
		  portal.clickLink(adminInterfaceLink);
	 }

	 @Then("I see the admin interface")
	 public void getAdminInterface() {
		  final WebElement adminInterface=portal.findElement(By.tagName("title"));
		  assertThat(adminInterface.getText().trim(),startsWith("Rave admin interface"));
	 }
	 
	 @When("I click the \"$usersLinkName\" link")
	 public void clickUsersLink(String usersLinkName) {
		  final WebElement usersLink=portal.findElement(By.linkText(usersLinkName));
		  usersLink.click();
	 }

	 @Then("I get the user search form")
	 public void getUserList(){
		  //Just need to make sure that the search button is there
		  WebElement searchForm=portal.findElement(By.id("userSearchForm"));
	 }

	 @When("I search for username \"$newuser\"")
	 public void searchForNewUser(String newuser) {
		  WebElement searchForm=portal.findElement(By.id("userSearchForm"));
		  searchForm.findElement(By.id("searchTerm")).sendKeys(newuser);
		  searchForm.submit();
	 }

	 @Then("I see the matches for \"$newuser\"")
	 public void getUserSearchMatches() {
		  WebElement searchResultsHeading=portal.findElement(By.tagName("h2"));
		  assertThat(searchResultsHeading.getText().trim(),equalTo("Showing 1 - 1 of 1 results that match 'newuser'"));
	 }
	 
	 @When("I click the link for information on \"$newuser\"")
	 public void clickLinkForUser(String newuser){
		  WebElement userLink=portal.findElement(By.linkText(newuser));
		  userLink.click();
	 }

	 @Then("I see the information for user \"$newuser\"")
	 public void getUserInformation(String newuser) {
		  WebElement userProfileHeader=portal.findElement(By.tagName("h2"));
		  assertThat(userProfileHeader.getText().trim(),equalTo(newuser));
	 }

	 @When("I delete the the user \"$newuser\"")
	 public void deleteNewUser() {
		  WebElement deleteUserProfileForm=portal.findElement(By.id("deleteUserProfile"));
		  //Click the checkbox to confirm the deletion.
		  deleteUserProfileForm.findElement(By.id("confirmdelete")).click();
		  deleteUserProfileForm.submit();
	 }

	 //See the list of users again, search, and not find "newuser" this time.

	 @Then("I see \"$deletionConfirmationStatement\"") 
	 public void noResultsFound(String deletionConfirmationStatement) {
		  WebElement confirmationText=portal.findElement(By.className("alert"));
		  assertThat(confirmationText.getText().trim(),startsWith(deletionConfirmationStatement));
	 }

	 @When("I log out")
	 public void iLogOut() {
		  portal.logout();
	 }

	 @Then("I see the Rave login page")
	 public void backToLoginPage() {
        final WebElement title = portal.findElement(By.tagName("title"));
        assertThat(title.getText().trim(), equalTo("Login - Rave"));
	 }
}
