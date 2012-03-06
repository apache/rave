<%--
  ~ Licensed to the Apache Software Foundation (ASF) under one
  ~ or more contributor license agreements.  See the NOTICE file
  ~ distributed with this work for additional information
  ~ regarding copyright ownership.  The ASF licenses this file
  ~ to you under the Apache License, Version 2.0 (the
  ~ "License"); you may not use this file except in compliance
  ~ with the License.  You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>
<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<%-- Note: This page has the body definition embedded so we can reference it directly from the security config file. --%>
<tiles:insertDefinition name="templates.base">
    <%-- Override the default pageTitleKey and then export it to the request scope for use later on this page --%>
    <tiles:putAttribute name="pageTitleKey" value="page.login.title"/>
    <tiles:importAttribute name="pageTitleKey" scope="request"/>

    <tiles:putAttribute name="body">
        <div id="content">

            <c:if test="${not empty message}">
                <div class="topbar-message">${message}</div>
            </c:if>

            <h1><fmt:message key="${pageTitleKey}"/></h1>

            <h2><fmt:message key="page.login.usernamepassword"/></h2>

            <form id="loginForm" name="loginForm" action="j_spring_security_check" method="post">
                <c:if test="${param['authfail'] eq 'form'}">
                    <p class="error"><fmt:message key="page.login.usernamepassword.fail"/></p>
                </c:if>
                <fieldset>
                    <p>
                        <label for="usernameField"><fmt:message key="page.general.username"/></label>
                        <input id="usernameField" type="text" name="j_username" autofocus="autofocus"/>
                    </p>

                    <p>
                        <label for="passwordField"><fmt:message key="page.general.password"/></label>
                        <input id="passwordField" type="password" name="j_password"/>
                    </p>

                    <p>
                        <label for="remember_me" class="checkboxLabel">
                            <input type='checkbox' name='_spring_security_remember_me' id="remember_me"
                                   value="true"/>
                            <fmt:message key="page.login.rememberme"/>
                        </label>
                    </p>
                </fieldset>
                <fieldset>
                    <fmt:message key="page.login.usernamepassword.login" var="loginButtonText"/>
                    <input type="submit" value="${loginButtonText}"/>
                </fieldset>
            </form>

            <h2><fmt:message key="page.login.forgot.password"/></h2>

            <form id="newPassword" action="<c:url value="/app/newpassword"/>" method="get">
            <fieldset>
                <p>
                    <label for="requestNewPasswordButton"><fmt:message
                            key="page.login.forgot.password.label"/></label>
                    <fmt:message key="page.login.forgot.password.button" var="requestNewPasswordButtonText"/>
                    <input id="requestNewPasswordButton" type="submit" value="${requestNewPasswordButtonText}"/>
                </p>
            </fieldset>
            </form>
            <h2><fmt:message key="page.login.forgot.username"/></h2>

            <form id="newPassword" action="<c:url value="/app/retrieveusername"/>" method="get">
            <fieldset>
                <p>
                    <label for="requestNewUsernameButton"><fmt:message
                            key="page.login.forgot.username.label"/></label>
                    <fmt:message key="page.login.forgot.username.button" var="requestNewUsernameButton"/>
                    <input id="requestNewUsernameButton" type="submit" value="${requestNewUsernameButton}"/>
                </p>
            </fieldset>
            </form>
            <h2><fmt:message key="page.login.createaccount"/></h2>

            <form id="newAccount" action="<c:url value="/app/newaccount.jsp"/>" method="get">
            <fieldset>
                <p>
                    <label for="createNewAccountButton"><fmt:message key="page.login.createaccount.label"/></label>
                    <fmt:message key="page.login.createaccount.button" var="createAccountButtonText"/>
                    <input id="createNewAccountButton" type="submit" value="${createAccountButtonText}"/>
                </p>
            </fieldset>
            </form>

            <h2><fmt:message key="page.login.openid"/></h2>

            <form id="oidForm" name='oidf' action='j_spring_openid_security_check' method='POST'>
                <c:if test="${param['authfail'] eq 'openid'}">
                    <p class="error"><fmt:message key="page.login.openid.fail"/></p>
                </c:if>
                <fieldset>
                    <p>
                        <label for="openid_identifier"><fmt:message key="page.login.openid.identifier"/></label>
                        <input type='text' id="openid_identifier" name='openid_identifier' class="long"/>
                    </p>

                    <p>
                        <label for="remember_me_openid" class="checkboxLabel">
                            <input type='checkbox' name='_spring_security_remember_me' id="remember_me_openid"
                                   value="true"/>
                            <fmt:message key="page.login.rememberme"/>
                        </label>
                    </p>
                </fieldset>
                <fieldset>
                    <fmt:message key="page.login.openid.button" var="openidButtonText"/>
                    <input type="submit" value="${openidButtonText}"/>
                </fieldset>

            </form>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
