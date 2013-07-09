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

    <tiles:putAttribute name="pageTitleKey" value="page.changepassword.title"/>
    <tiles:importAttribute name="pageTitleKey" scope="request"/>

    <tiles:putAttribute name="body">
        <div id="content">
            <h1><fmt:message key="page.changepassword.title"/></h1>
            <form:form commandName="newUser" action="changepassword" method="post">
                <fieldset>
                    <p><fmt:message key="form.all.fields.required"/></p>
                    <form:hidden path="forgotPasswordHash"/>
                    <p><form:errors cssClass="error"/></p>

                    <p>
                        <label for="passwordField"><fmt:message key="page.general.password"/></label>
                        <form:password id="passwordField" path="password" required="required"/>
                        <form:errors path="password" cssClass="error"/>
                    </p>

                    <p>
                        <label for="passwordConfirmField"><fmt:message key="page.general.confirmpassword"/></label>
                        <form:password id="passwordConfirmField" path="confirmPassword" required="required"/>
                        <form:errors path="confirmPassword" cssClass="error"/>
                    </p>
                </fieldset>
                <fieldset>
                    <fmt:message key="page.changepassword.button" var="submitButtonText"/>
                    <input type="submit" value="${submitButtonText}"/>
                </fieldset>
            </form:form>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
