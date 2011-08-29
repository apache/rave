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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave" %>
<fmt:setBundle basename="messages"/>

<fmt:message key="page.userprofile.title" var="pagetitle"/>

<jsp:useBean id="userProfile" type="org.apache.rave.portal.model.User" scope="request"/>
<rave:rave_generic_page pageTitle="${pagetitle}">
    <div id="content">
        <h1>${pagetitle}</h1>
        <form:form id="userProfileForm" commandName="userProfile" action="updateUserProfile" method="POST">
            <fieldset>
                <form:errors cssClass="error" element="p"/>
                <p>
                    <fmt:message key="page.general.username"/> <c:out value=" ${userProfile.username}"/>
                </p>
                <p>
                    <label for="passwordField"><fmt:message key="page.general.password"/></label>
                    <form:password path="password" id="passwordField" required="required"
                                   showPassword="true"/>
                    <form:errors path="password" cssClass="error"/>
                </p>

                <p>
                    <label for="passwordConfirmField"><fmt:message key="page.general.confirmpassword"/></label>
                    <form:password path="confirmPassword" id="passwordConfirmField" required="required"
                                   showPassword="true"/>
                    <form:errors path="confirmPassword" cssClass="error"/>
                </p>

                <p>
                    <label for="emailField"><fmt:message key="page.general.email"/></label>
                    <spring:bind path="email">
                        <input type="email" name="email" id="emailField" required="required" value="<c:out value="${status.value}"/>"/>
                    </spring:bind>
                    <form:errors path="email" cssClass="error"/>
                </p>

                <p>
                    <label for="openIdField"><fmt:message key="page.userprofile.openid.url"/></label>
                    <spring:bind path="openId">
                        <input type="url" id="openIdField" name="openId" value="<c:out value="${status.value}"/>"/>
                    </spring:bind>
                    <form:errors path="openId" cssClass="error"/>
                </p>
            </fieldset>
            <fieldset>
                <fmt:message key="page.userprofile.button" var="updateButtonText"/>
                <input type="submit" value="${updateButtonText}"/>
            </fieldset>
        </form:form>
    </div>
    <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min.js"></script>
    <script src="<spring:url value="/script/rave.js"/>"></script>
    <script src="<spring:url value="/script/rave_forms.js"/>"></script>

    <script>$(document).ready(rave.forms.validateUserProfileForm());</script>
</rave:rave_generic_page>
