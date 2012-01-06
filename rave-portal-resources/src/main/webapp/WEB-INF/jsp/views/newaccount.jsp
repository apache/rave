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
<%-- Note: This page has the body definition embedded so we can reference it directly from the security config file. --%>
<tiles:insertDefinition name="templates.base">
    <%-- Override the default pageTitleKey and then export it to the request scope for use later on this page --%>
    <tiles:putAttribute name="pageTitleKey" value="page.newaccount.title"/>
    <tiles:importAttribute name="pageTitleKey" scope="request"/>

    <tiles:putAttribute name="body">
        <div id="content">
            <h1>${pagetitle}</h1>
            <form:form id="newAccountForm" commandName="newUser" action="newaccount" method="POST">
                <fieldset>
                    <p><fmt:message key="form.all.fields.required"/></p>

                    <p><form:errors cssClass="error"/></p>

                    <p>
                        <label for="userNameField"><fmt:message key="page.general.username"/></label>
                        <form:input id="userNameField" path="username" required="required" autofocus="autofocus"/>
                        <form:errors path="username" cssClass="error"/>
                    </p>

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

                    <p>
                        <label for="emailField"><fmt:message key="page.general.email"/></label>
                        <form:input id="emailField" path="email" required="required" />
                        <form:errors path="email" cssClass="error"/>
                    </p>

                    <p>
                        <label for="pageLayoutField"><fmt:message key="page.general.addpage.selectlayout"/></label>
                        <form:select path="pageLayout" id="pageLayoutField">
                            <form:option value="columns_1" id="columns_1_id"><fmt:message
                                    key="page.general.addpage.layout.columns_1"/></form:option>
                            <form:option value="columns_2" id="columns_2_id"><fmt:message
                                    key="page.general.addpage.layout.columns_2"/></form:option>
                            <form:option value="columns_2wn" id="columns_2wn_id"><fmt:message
                                    key="page.general.addpage.layout.columns_2wn"/></form:option>
                            <form:option value="columns_3" id="columns_3_id"><fmt:message
                                    key="page.general.addpage.layout.columns_3"/></form:option>
                            <form:option value="columns_3nwn" id="columns_3nwn_id"><fmt:message
                                    key="page.general.addpage.layout.columns_3nwn"/></form:option>
                            <form:option value="columns_4" id="columns_4_id"><fmt:message
                                    key="page.general.addpage.layout.columns_4"/></form:option>
                            <form:option value="columns_3nwn_1_bottom" id="columns_3nwn_1_bottom"><fmt:message
                                    key="page.general.addpage.layout.columns_3nwn_1_bottom"/></form:option>
                        </form:select>
                    </p>
                </fieldset>
                <fieldset>${captchaHtml}</fieldset>
                <fieldset>
                    <fmt:message key="page.newaccount.button" var="submitButtonText"/>
                    <input type="submit" value="${submitButtonText}"/>
                </fieldset>
            </form:form>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.4.min.js"></script>
<script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.8.1/jquery.validate.min.js"></script>
<script src="<spring:url value="/script/rave.js"/>"></script>
<script src="<spring:url value="/script/rave_forms.js"/>"></script>

<script>$(document).ready(rave.forms.validateNewAccountForm());</script>