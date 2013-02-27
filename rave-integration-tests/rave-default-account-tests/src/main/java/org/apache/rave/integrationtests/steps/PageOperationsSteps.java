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
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Steps for managing page operations (adding pages, changing layout etc)
 */
@Step
public class PageOperationsSteps {

    @Autowired
    Portal portal;


    @When("I click the add page button")
    public void clickAddPage() {
        final WebElement addPageLink = portal.findElement(By.xpath("//li[@id='addPageButton']/a"));
        addPageLink.click();
        final WebElement pageForm = portal.findElement(By.id("pageForm"));
        assertThat(pageForm.isDisplayed(), equalTo(Boolean.TRUE));
    }

    @When("I enter the title \"$value\"")
    public void enterTitle(String value) {
        changeFieldValue("tab_title", value);
    }

    @When("I choose the two column layout")
    public void selectTwoColumnLayout() {
        final Select layouts = new Select(portal.findElement(By.id("pageLayout")));
        layouts.selectByValue("columns_2");
    }

    @When("I add the page")
    public void submitAddPage() {
        final WebElement updateButton = portal.findElement(By.id("pageMenuUpdateButton"));
        updateButton.click();
        sleep(2000L);
    }

    @Then("the new page with title \"$pageTitle\" is selected")
    public void checkNewPage(String pageTitle) {
        final WebElement activePage =
                portal.findElement(By.xpath("//div[@id='pageContent']/nav//li[contains(@class, 'active')]/a"));
        assertThat(activePage, notNullValue());
        assertThat(activePage.getText().trim(), equalTo(pageTitle));
        final WebElement emptyPageBox = portal.getEmptyPageBox();
        assertThat(emptyPageBox.isDisplayed(), equalTo(Boolean.TRUE));
    }

    @When("I delete the current page")
    public void deleteCurrentPage() {
        final WebElement activePage =
                portal.findElement(By.xpath("//div[@id='pageContent']/nav//li[contains(@class, 'active')]/a"));
        activePage.click();
        sleep(2000L);
        final WebElement deletePageLink = portal.findElement(By.xpath("//li[@id='pageMenuDelete']/a"));
        deletePageLink.click();
        sleep(2000L);
    }


    private void changeFieldValue(String fieldId, String value) {
        final WebElement field = portal.findElement(By.id(fieldId));
        field.clear();
        field.sendKeys(value);
    }

    private void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not sleep thread", e);
        }
    }

}
