<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<%--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  --%>

<header>

    <nav>
        <div class="navbar navbar-fixed-top">
            <div class="container">
                <a href="/" class="brand">RAVE</a>

                <ul class="nav pull-right">
                    <li>
                        <form class="form-inline" action="<c:url value="/app/newpassword"/>" method="get">
                            <fmt:message key="page.login.forgot.password.button" var="requestNewPasswordButtonText"/>
                            <button class="btn btn-link" id="requestNewPasswordButton" type="submit" value="${requestNewPasswordButtonText}">${requestNewPasswordButtonText}</button>
                        </form>
                    </li>
                    <li class="divider-vertical"></li>
                    <li>
                        <form class="form-inline " action="<c:url value="/app/retrieveusername"/>" method="get">
                            <fmt:message key="page.login.forgot.username.button" var="requestNewUsernameButton"/>
                            <button class="btn btn-link" id="requestNewUsernameButton" type="submit" value="${requestNewUsernameButton}">${requestNewUsernameButton}</button>
                        </form>
                    </li>
                    <li class="divider-vertical"></li>

                    <li>
                        <form class="form-inline" action="<c:url value="/app/newaccount.jsp"/>" method="get">
                            <fieldset>
                                <fmt:message key="page.login.createaccount.button" var="createAccountButtonText"/>
                                <button class="btn btn-link" id="createNewAccountButton" type="submit" value="${createAccountButtonText}">${createAccountButtonText}</button>
                            </fieldset>
                        </form>
                    </li>
                    <li class="divider-vertical"></li>
                </ul>
            </div>
        </div>
    </nav>
</header>