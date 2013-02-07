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

<form:form cssClass="form-horizontal" id="newAccountForm" commandName="newUser" action="newaccount" method="POST">
    <fieldset>
        <p><fmt:message key="form.all.fields.required"/></p>

        <p><form:errors cssClass="error"/></p>


        <div class="control-group">
            <label class="control-label" for="userNameField"><fmt:message key="page.general.username"/></label>
            <div class="controls">
                <form:input id="userNameField" path="username" required="required" autofocus="autofocus" autocapitalize="off" autocorrect="off"/>
                <form:errors path="username" cssClass="error"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="passwordField"><fmt:message key="page.general.password"/></label>
            <div class="controls">
                <form:password id="passwordField" path="password" required="required"/>
                <form:errors path="password" cssClass="error"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="passwordConfirmField"><fmt:message key="page.general.confirmpassword"/></label>
            <div class="controls">
                <form:password id="passwordConfirmField" path="confirmPassword" required="required"/>
                <form:errors path="confirmPassword" cssClass="error"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="emailField"><fmt:message key="page.general.email"/></label>
            <div class="controls">
                <spring:bind path="email">
                    <input type="email" id="emailField" name="email" required="required" value="<c:out value="${status.value}"/>"/>
                </spring:bind>
                <form:errors path="email" cssClass="error"/>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label" for="openIdField"><fmt:message key="page.general.openid"/></label>
            <div class="controls">
                <form:input id="openIdField" path="openId"/>
                <form:errors path="openId" cssClass="error"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="pageLayoutField"><fmt:message key="page.general.addpage.selectlayout"/></label>
            <div class="controls">
                <form:select path="defaultPageLayoutCode" id="pageLayoutField">
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
            </div>
        </div>
    </fieldset>

    <fieldset>
        <div class="control-group">
            <div class="controls">${captchaHtml}</div>
        </div>
    </fieldset>

    <!-- Personal information optional -->
    <h2><fmt:message key="page.general.personal.information"/></h2>

    <fieldset>
        <div class="control-group">
            <label class="control-label" for="firstNameField"><fmt:message key="page.general.first.name"/></label>
            <div class="controls"><form:input id="firstNameField" path="givenName"/></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="lastNameField"><fmt:message key="page.general.last.name"/></label>
            <div class="controls"><form:input id="lastNameField" path="familyName"/></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="displayNameField"><fmt:message key="page.general.display.name"/></label>
            <div class="controls"><form:input id="displayNameField" path="displayName"/></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="statusField"><fmt:message key="page.general.relation.status"/></label>
            <div class="controls">
                <form:select path="status" id="statusField">
                	<fmt:message key="page.general.relation.single" var="singleText"/>
                    <form:option value="${singleText}" id="single_id">${singleText}</form:option>
                    <fmt:message key="page.general.relation.committed" var="committedText"/>
                    <form:option value="${committedText}" id="committed_id">${committedText}</form:option>
                    <fmt:message key="page.general.relation.married" var="marriedText"/>
                    <form:option value="${marriedText}" id="married_id">${marriedText}</form:option>
                    <fmt:message key="page.general.relation.other" var="otherText"/>
                    <form:option value="${otherText}" id="other_id">${otherText}</form:option>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="aboutMeField"><fmt:message key="page.general.about.me"/></label>
            <div class="controls"><form:textarea id="aboutMeField" path="aboutMe"/></div>
        </div>
    </fieldset>

    <fieldset>
        <input type="hidden" name="referringPageId" value="<c:out value="${referringPageId}"/>"/>
        <fmt:message key="page.newaccount.button" var="submitButtonText"/>
        <button class="btn btn-primary" type="submit" value="${submitButtonText}">${submitButtonText}</button>
    </fieldset>
</form:form>