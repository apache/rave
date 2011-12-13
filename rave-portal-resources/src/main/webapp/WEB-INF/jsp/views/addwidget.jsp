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
<header>
    <nav class="topnav">
        <ul class="horizontal-list">
          <c:if test="${not empty referringPageId}">
                <li>
                    <a href="<spring:url value="/app/store?referringPageId=${referringPageId}" />">
                        <fmt:message key="page.widget.backToStore"/>
                    </a>
                </li>
            </c:if>
            <li>
               <c:choose>
                <c:when test="${empty referringPageId}">
                    <spring:url value="/index.html" var="gobackurl" />
                </c:when>
                <c:otherwise>
                    <spring:url value="/app/page/view/${referringPageId}" var="gobackurl"/>
                </c:otherwise>
            </c:choose>
            <a href="<c:out value="${gobackurl}"/>"><fmt:message key="page.general.back"/></a>
            </li>
            <sec:authorize url="/app/admin/">
                <li>
                    <a href="<spring:url value="/app/admin/"/>">
                        <fmt:message key="page.general.toadmininterface"/>
                    </a>
                </li>
            </sec:authorize>
            <li>
                <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">
                  <fmt:message key="page.general.logout"/></a>
            </li>
        </ul>
    </nav>
    <h1><fmt:message key="page.addwidget.title"/></h1>
</header>

<div id="content">
    <h2><fmt:message key="page.addwidget.form.header"/></h2>
    <form:errors path="widget" cssClass="error" element="p"/>
    <form:form id="newWidgetForm" action="add?referringPageId=${referringPageId}" commandName="widget" method="POST">
        <fieldset>
            <p><fmt:message key="form.some.fields.required"/></p>

            <p>
                <form:label path="title"><fmt:message key="widget.title"/> *</form:label>
                <form:input path="title" cssClass="long" required="required" autofocus="autofocus"/>
                <form:errors path="title" cssClass="error"/>
            </p>

            <p>
                <spring:bind path="url">
                    <label for="url"><fmt:message key="widget.url"/> *</label>
                    <input type="url" name="url" id="url"
                           placeholder="http://example.com/widget.xml" required="required"
                           class="long" value="<c:out value="${widget.url}"/>"/>
                </spring:bind>
                <form:errors path="url" cssClass="error"/>
            </p>

            <p>
                <label for="type1"><fmt:message key="widget.type"/> *</label>
                <label for="type1" class="formradio"><form:radiobutton path="type" value="OpenSocial"/>
                    <fmt:message key="widget.type.OpenSocial"/></label>
                <label for="type2" class="formradio"><form:radiobutton path="type" value="W3C"/>
                    <fmt:message key="widget.type.W3C"/></label>
                <form:errors path="type" cssClass="error"/>
            </p>

            <p>
                <form:label path="description"><fmt:message key="widget.description"/> *</form:label>
                <form:textarea path="description" required="required" cssClass="long"/>
                <form:errors path="description" cssClass="error"/>
            </p>

            <p>
                <spring:bind path="thumbnailUrl">
                    <label for="thumbnailUrl"><fmt:message key="widget.thumbnailUrl"/></label>
                    <input type="url" name="thumbnailUrl" id="thumbnailUrl"
                           placeholder="http://example.com/thumbnail.png" class="long"
                           value="<c:out value="${widget.thumbnailUrl}"/>"/>
                </spring:bind>
                <form:errors path="thumbnailUrl" cssClass="error"/>
            </p>

            <p>
                <spring:bind path="screenshotUrl">
                    <label for="screenshotUrl"><fmt:message key="widget.screenshotUrl"/></label>
                    <input type="url" name="screenshotUrl" id="screenshotUrl"
                           placeholder="http://example.com/screenshot.png" class="long"
                           value="<c:out value="${widget.screenshotUrl}"/>"/>
                </spring:bind>
                <form:errors path="screenshotUrl" cssClass="error"/>
            </p>

            <p>
                <spring:bind path="titleUrl">
                    <label for="titleUrl"><fmt:message key="widget.titleUrl"/></label>
                    <input type="url" name="titleUrl" id="titleUrl"
                           class="long" value="<c:out value="${widget.titleUrl}"/>"/>
                </spring:bind>
                <form:errors path="titleUrl" cssClass="error"/>
            </p>

            <p>
                <form:label path="author"><fmt:message key="widget.author"/></form:label>
                <form:input path="author" cssClass="long"/>
                <form:errors path="author" cssClass="error"/>
            </p>
            <p>
                <spring:bind path="authorEmail">
                    <label for="authorEmail"><fmt:message key="widget.authorEmail"/></label>
                    <input type="email" name="authorEmail" id="authorEmail" class="long"
                            value="<c:out value="${widget.authorEmail}"/>"/>
                </spring:bind>
                <form:errors path="titleUrl" cssClass="error"/>
            </p>

        </fieldset>
        <fieldset>
            <fmt:message key="page.addwidget.form.submit" var="submit"/>
            <input type="submit" value="${submit}"/>
        </fieldset>
    </form:form>
</div>