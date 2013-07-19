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

import org.apache.rave.integrationtests.pages.Portal;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Steps for the new user stories
 */
@Step
public class NewUserSteps {

    @Autowired
    private Portal portal;

    @When("I go to \"$url\"")
    public void goTo(String url) {
        portal.go(url);
    }

    @Then("I see the login page")
    public void isLoginPage() {
        final String title = portal.getTitle();
        assertThat(title.trim(), equalTo("Login - Rave"));
    }

    @When("I follow the new account link")
    public void followNewAccountLink() {
        portal.pressNewAccountButton();
    }

    @Then("I get the new account form")
    public void isNewAccountForm() {
        // throws exception if id is not present
        portal.getNewAccountForm();
    }

    @When("I fill in the form with username \"$username\" password \"$password\" confirmpassword \"$confirmPassword\" email \"$email\"")
    public void populateNewAccountForm(String username, String password, String confirmpassword, String email) {
        final WebElement newAccountForm = portal.getNewAccountForm();
        newAccountForm.findElement(By.id("userNameField")).sendKeys(username);
        newAccountForm.findElement(By.id("passwordField")).sendKeys(password);
        newAccountForm.findElement(By.id("passwordConfirmField")).sendKeys(confirmpassword);
        newAccountForm.findElement(By.id("emailField")).sendKeys(email);
    }

    @When("I submit the new account form")
    public void submitNewAccountForm() {
        portal.getNewAccountForm().submit();
    }

    @Then("A message appears \"$message\"")
    public void messagePresent(String message) {
        final WebElement messageBox = portal.findElement(By.className("alert"));
        assertThat(message, equalTo(messageBox.getText().trim()));
    }

    @When("I fill in the login form with username \"$username\" password \"$password\"")
    public void login(String username, String password) {
        final WebElement loginForm = portal.getLoginForm();
        loginForm.findElement(By.id("usernameField")).sendKeys(username);
        loginForm.findElement(By.id("passwordField")).sendKeys(password);
        loginForm.submit();
    }

    @Then("I see my portal page with the new user widget page")
    public void iSeeMyNewUserPortalPage() {
        final WebElement newUserDiv = portal.findElement(By.className("columns_3_newuser_static"));
        assertThat(newUserDiv.isDisplayed(), equalTo(Boolean.TRUE));
    }


}
