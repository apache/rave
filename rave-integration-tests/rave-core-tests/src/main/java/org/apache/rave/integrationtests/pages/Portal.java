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

package org.apache.rave.integrationtests.pages;

import org.jbehave.web.selenium.WebDriverPage;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Web interface to the portal
 */
@Component
public class Portal extends WebDriverPage {
    private final static int SLEEP_TIME = 3333; //ms
    private final static int MAX_ATTEMPTS = 3;

    @Autowired
    public Portal(WebDriverProvider driverProvider) {
        super(driverProvider);
    }

    public void go(String url) {
        get(url);
    }

    public void pressNewAccountButton() {
        final WebElement newAccountButton = findElement(By.id("createNewAccountButton"));
        newAccountButton.click();
    }

    public WebElement getNewAccountForm() {
        return findElement(By.id("newAccountForm"));
    }

    public WebElement getLoginForm() {
        return findElement(By.id("loginForm"));
    }

    public WebElement getOpenIdLoginForm() {
        return findElement(By.id("openIdForm"));
    }

    public WebElement getEmptyPageBox() {
        return findElement(By.id("emptyPageMessageWrapper"));
    }

    public void login(String username, String password) {
        final WebElement loginForm = getLoginForm();
        loginForm.findElement(By.id("usernameField")).sendKeys(username);
        loginForm.findElement(By.id("passwordField")).sendKeys(password);
        loginForm.submit();
    }

    public void openIdLogin(String openIdUrl) {
        final WebElement openIdLogin = getOpenIdLoginForm();
        openIdLogin.findElement(By.id("openid_identifier")).sendKeys(openIdUrl);
        openIdLogin.submit();
    }

    public void logout() {
        final WebElement logoutLink = findElement(By.linkText("Logout"));
        logoutLink.click();
    }

    public void clickLink(WebElement linkToClick) {
        //TODO Should make sure this is clickable.
        linkToClick.click();
    }

    public WebElement findElement(By by) {
        return this.delayedFindElement(by, 1);
    }

    // Do 3 attempts with increasing interval to find a DOM element.
    // The DOM may not have finished when the check is fired.
    private WebElement delayedFindElement(By by, int attempt) {
        try {
            return super.findElement(by);
        } catch (NoSuchElementException e) {
            if (attempt >= MAX_ATTEMPTS) {
                throw e;
            }
            try {
                Thread.sleep(attempt * SLEEP_TIME);
            } catch (InterruptedException ie) {
                throw new RuntimeException("Could not sleep thread", ie);
            }
            return this.delayedFindElement(by, attempt + 1);
        }
    }
}
