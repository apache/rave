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

<tiles:insertDefinition name="templates.base">
    <tiles:putAttribute name="pageTitleKey" value="page.retrieveusername.title"/>
    <tiles:importAttribute name="pageTitleKey" scope="request"/>

    <tiles:putAttribute name="body">
        <div id="content">
            <h1><fmt:message key="page.retrieveusername.title"/></h1>
            <c:choose>
                <c:when test="${success}">

                    <div class="alert-message success">
                        <fmt:message key="page.newpassword.email.sent">
                            <fmt:param>${email}</fmt:param>
                        </fmt:message>
                    </div>
                    <a href="/"><fmt:message key="page.newpassword.email.sent.login"/></a>
                </c:when>

                <c:otherwise>

                    <h2><fmt:message key="page.newpassword.username.title"/></h2>
                    <form:form commandName="newUser" action="retrieveusername" method="post">
                        <fieldset>
                            <p><fmt:message key="form.all.fields.required"/></p>

                            <p><form:errors cssClass="error"/></p>

                            <p>
                                <label for="emailField"><fmt:message key="page.general.email"/></label>
                                <form:input id="emailField" path="email" required="required" autofocus="autofocus"/>
                                <form:errors path="email" cssClass="error"/>
                            </p>
                        </fieldset>
                        <fieldset>${captchaHtml}</fieldset>
                        <fieldset>
                            <fmt:message key="page.login.forgot.username.button" var="submitButtonText"/>
                            <input type="submit" value="${submitButtonText}"/>
                        </fieldset>
                    </form:form>
                </c:otherwise>
            </c:choose>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
