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
import static org.hamcrest.Matchers.startsWith;

@Step
public class CommonSteps {
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

    @When("I log in as an administrator with username \"$username\" and password \"$password\"")
    public void loginAsAdmin(String username, String password) {
        portal.login(username, password);
    }

    @Then("I see the admin interface link")
    public void getAdminInterfaceLink() {
        //TODO: make sure the admin interface actually is loaded
        final WebElement adminInterfaceLink = portal.findElement(By.linkText("Admin interface"));
    }

    @When("I click the admin interface link")
    public void clickAdminInterfaceLink() {
        //TODO: there should be consequences if this fails.
        final WebElement adminInterfaceLink = portal.findElement(By.linkText("Admin interface"));
        portal.clickLink(adminInterfaceLink);
    }

    @Then("I see the admin interface")
    public void getAdminInterface() {
        final String adminInterface = portal.getTitle();
        assertThat(adminInterface.trim(), startsWith("Rave admin interface"));
    }

    @When("I click the \"$linkName\" link")
    public void clickLink(String linkName) {
        final WebElement usersLink = portal.findElement(By.linkText(linkName));
        usersLink.click();
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
