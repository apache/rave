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
public class DefaultAccountSteps {

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

    @When("I log in with username \"$username\" and password \"$password\"")
    public void loginAsUser(String username, String password) {
        portal.login(username, password);
    }

    @Then("I see the message \"$welcomeMessage\" for the user \"$username\"")
    public void getLoggedInPage(String welcomeMessage, String username) {
        final WebElement displayedWelcome = portal.findElement(By.className("brand"));
        assertThat(displayedWelcome.getText().trim(), equalTo(welcomeMessage));
    }

    @When("I provide my OpenID identity \"$openIdUrl\"")
    public void openIdLogin(String openIdUrl) {
        portal.openIdLogin(openIdUrl);
    }

    @Then("I see the OpenID authentication page")
    public void getOpenIdPage() {
        //Note this is specific to MyOpenID (and presumably their English page)
        final WebElement openIdPage = portal.findElement(By.linkText("myOpenID - The free, secure OpenID server"));
    }

    @When("I provide my OpenID password \"$openIdPassword\"")
    public void loginToOpenIdProvider(String openIdPassword) {
        final WebElement openIdLoginForm = portal.findElement(By.id("password-signin-form"));
        openIdLoginForm.findElement(By.id("password")).sendKeys(openIdPassword);
        openIdLoginForm.submit();
    }


    @When("I log out")
    public void iLogOut() {
        portal.logout();
    }

    @Then("I see the Rave login page")
    public void backToLoginPage() {
        final String title = portal.getTitle();
        assertThat(title.trim(), equalTo("Login - Rave"));
    }

}
