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
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Steps for managing a user profile
 */
@Step
public class ProfileSteps {

    @Autowired
    private Portal portal;

    @Then("I see the email address \"$email\" on the profile page")
    public void getEmailOnProfilePage(String email) {
        final WebElement emailField = portal.findElement(By.id("email"));
        assertThat(emailField.getText().trim(), equalTo(email));
    }

    @Then("I see the about me \"$aboutMe\" on the profile page")
    public void getAboutMeOnProfilePage(String aboutMe) {
        final WebElement aboutMeField = portal.findElement(By.id("aboutMe"));
        assertThat(aboutMeField.getText().trim(), equalTo(aboutMe));
    }

    @Then("I see the status \"$status\" on the profile page")
    public void getStatusOnProfilePage(String status) {
        final WebElement statusField = portal.findElement(By.id("status"));
        assertThat(statusField.getText().trim(), equalTo(status));
    }

    @When("I click on the \"$buttonId\" button")
    public void clickButton(String buttonId) {
        final WebElement button = portal.findElement(By.id(buttonId));
        button.click();
    }

    @Then("I can edit the email address")
    public void editEmailFieldIsVisible() {
        final WebElement editEmailField = portal.findElement(By.id("emailField"));
        editEmailField.isDisplayed();
    }

    @When("I change the email address to \"$email\"")
    public void changeEmailAddress(String email) {
        changeFieldValue("emailField", email);
    }

    @When("I change the about me to \"$aboutMe\"")
    public void changeAboutMe(String aboutMe) {
        changeFieldValue("aboutMeField", aboutMe);
    }

    @When("I choose the status as \"$status\"")
    public void changeStatus(String status) {
        final Select relationshipStatus = new Select(portal.findElement(By.id("statusField")));
        relationshipStatus.selectByValue(status);
    }

    private void changeFieldValue(String fieldId, String value) {
        final WebElement field = portal.findElement(By.id(fieldId));
        field.clear();
        field.sendKeys(value);
    }

    @When("I submit the edit profile form")
    public void submitProfileForm() {
        final WebElement editAccountForm = portal.findElement(By.id("editAccountForm"));
        editAccountForm.submit();
    }
}
