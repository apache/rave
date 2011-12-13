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

<fmt:message key="${pageTitleKey}" var="pagetitle"/>
<rave:header pageTitle="${pagetitle}"/>
<rave:admin_tabsheader/>
<div class="pageContent">
    <article class="admincontent">
        <ul class="horizontal-list goback">
            <li><a href="<spring:url value="/app/admin/preferences"/>">
                <fmt:message key="admin.preferencedetail.goback"/></a>
            </li>
        </ul>
        <h2><fmt:message key="admin.preferences.shorttitle"/></h2>

        <div class="leftcolumn">
            <section class="formbox">
                <spring:url value="/app/admin/preferencedetail/update" var="formAction"/>
                <form:form action="${formAction}" method="POST" modelAttribute="preferenceForm">
                    <form:errors cssClass="error" element="p"/>
                    <fieldset>
                        <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
                        <p><fmt:message key="form.some.fields.required"/></p>

                        <p>
                            <form:label path="titleSuffix.value"><fmt:message key="admin.preferencedetail.titleSuffix"/></form:label>
                            <form:input path="titleSuffix.value"/>
                            <form:errors path="titleSuffix.value" cssClass="error"/>
                        </p>
                    </fieldset>
                    <fieldset>
                        <p>
                            <spring:bind path="pageSize.value">
                                <label for="pageSize"><fmt:message key="admin.preferencedetail.pageSize"/> *</label>
                                <input id="pageSize" name="pageSize.value" type="number" step="1"
                                       value="<c:out value="${status.value}"/>"/>
                            </spring:bind>
                            <form:errors path="pageSize.value" cssClass="error"/>
                        </p>
                    </fieldset>
                    <fieldset>
                        <fmt:message key="admin.preferencedetail.updateButton" var="updateButtonText"/>
                        <input type="submit" value="${updateButtonText}"/>
                    </fieldset>
                </form:form>
            </section>
            <div class="clear-float">

            </div>

        </div>
    </article>
</div>