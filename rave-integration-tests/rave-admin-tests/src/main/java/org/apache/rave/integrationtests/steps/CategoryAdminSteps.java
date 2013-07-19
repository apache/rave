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
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Step
public class CategoryAdminSteps {

    @Autowired
    private Portal portal;

    @Given("I see the categories list table")
    @Then("I see the categories list table")
    public void isCategoryTableVisible(){
        //Just need to make sure that the table is there
        WebElement table = portal.findElement(By.tagName("H2"));
        assertThat(table.getText().trim(), equalTo("Categories"));
    }

    @Given("I see the create category form")
    public void isCreateCategoryVisible() {
        WebElement btn = portal.findElement(By.tagName("button"));
        assertThat(btn.getText().trim(), equalTo("Create Category"));
    }

    @When("I fill in a new category named \"$categoryName\"")
    public void fillInNewCategory(String categoryName){
        WebElement form = portal.findElement(By.id("createCategory"));
        WebElement field = form.findElement(By.id("text"));
        field.sendKeys(categoryName);
    }

    @When("I click the \"Create Category\" button")
    public void clickCreateCategoryButton() {
        WebElement form = portal.findElement(By.id("createCategory"));
        WebElement btn = form.findElement(By.tagName("button"));
        btn.click();
    }

    @Given("I see a category called \"$categoryName\" in the categories list table")
    @Then("I see a category called \"$categoryName\" in the categories list table")
    public void isCategoryInTable(String categoryName) {
        WebElement categoryLink = getCategoryLinkFromListTable(categoryName);
        assertThat(categoryLink, notNullValue());
        assertThat(categoryLink.getText(), equalTo(categoryName));
    }

    @When("I click on the category named \"$categoryName\"")
    public void clickOnCategoryInListTable(String categoryName) {
        WebElement link = getCategoryLinkFromListTable(categoryName);
        assertThat(link, notNullValue());
        link.click();
    }

    @Then("I see the category details page")
    public void isCategoryDetailsPageVisible() {
        WebElement element = portal.findElement(By.id("deleteCategory"));
        assertThat(element, notNullValue());
    }

    @When("I change the name of the category to \"$categoryName\"")
    public void fillInUpdatedCategory(String categoryName) {
        WebElement form = portal.findElement(By.id("updateCategory"));
        WebElement field = form.findElement(By.id("text"));
        field.clear();
        field.sendKeys(categoryName);
    }

    @When("I click the \"Update Category\" button")
    public void clickUpdateCategoryButton() {
        WebElement form = portal.findElement(By.id("updateCategory"));
        WebElement btn = form.findElement(By.tagName("button"));
        btn.click();
    }

    @When("I click the \"Delete Category\" button")
    public void clickDeleteCategoryButton() {
        WebElement form = portal.findElement(By.id("deleteCategory"));
        WebElement btn = form.findElement(By.tagName("button"));
        btn.click();
    }

    @Given("I do not see a category called \"$categoryName\" in the categories list table")
    @Then("I do not see a category called \"$categoryName\" in the categories list table")
    public void isCategoryNotInTable(String categoryName) {
        try {
            WebElement categoryLink = getCategoryLinkFromListTable(categoryName);
            Assert.fail();
        } catch(NoSuchElementException ex) {
            // Success
        }
    }

    @When("I select the delete checkbox")
    public void selectDeleteCategoryCheckbox() {
        WebElement form = portal.findElement(By.id("deleteCategory"));
        WebElement chk = form.findElement(By.xpath("fieldset/div/label/input"));
        chk.click();
    }

    private WebElement getCategoryLinkFromListTable(String categoryName) {
        WebElement table = portal.findElement(By.id("categoryList"));
        return table.findElement(By.linkText(categoryName));
    }
}
