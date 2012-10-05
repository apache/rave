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

<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>

<tiles:putAttribute name="pageTitleKey" value="page.newpassword.title"/>
<tiles:importAttribute name="pageTitleKey" scope="request"/>

    <rave:login_navbar hideButton="requestNewPasswordButton" />

<div class="container-fluid">
    <div class="row-fluid">
        <h1><fmt:message key="page.newpassword.title"/></h1>
        <c:choose>
            <c:when test="${success}">
                <div class="alert-message success">
                    <fmt:message key="page.newpassword.email.sent">
                        <fmt:param>${email}</fmt:param>
                    </fmt:message>
                </div>
                <a href="<c:url value="/"/>"><fmt:message key="page.newpassword.email.sent.login"/></a>
            </c:when>
    
            <c:otherwise>
    
                <form:form cssClass="form-horizontal well" commandName="user" action="newpassword" method="post">
                    <fieldset>
                        <p><fmt:message key="form.all.fields.required"/></p>
    
                        <p><form:errors cssClass="error"/></p>
    
                        <div class="control-group">
                            <label class="control-label" for="emailField"><fmt:message key="page.general.email"/></label>
                            <div class="controls">
                                <spring:bind path="email">
                                    <input type="email" name="email" id="emailField" required="required" autofocus="autofocus"
                                           value="<c:out value="${status.value}"/>"/>
                                </spring:bind>
                                <form:errors path="email" cssClass="error"/>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <div class="controls">${captchaHtml}</div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <fmt:message key="page.login.forgot.password.button" var="submitButtonText"/>
                        <button type="submit" class="btn btn-primary" value="${submitButtonText}">${submitButtonText}</button>
                    </fieldset>
                </form:form>
            </c:otherwise>
        </c:choose>
    </div>
</div>