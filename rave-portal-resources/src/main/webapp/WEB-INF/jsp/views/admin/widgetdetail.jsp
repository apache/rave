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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave" %>
<fmt:setBundle basename="messages"/>

<fmt:message key="admin.widgetdetail.title" var="pagetitle"/>

<rave:rave_generic_page pageTitle="${pagetitle}">
    <rave:header pageTitle="${pagetitle}"/>
    <rave:admin_tabsheader/>
    <div class="pageContent">
        <article class="admincontent">
            <ul class="horizontal-list goback">
                <li><a href="<spring:url value="/app/admin/widgets"/>"><fmt:message key="admin.widgetdetail.goback"/></a>
                </li>
            </ul>
            <h2><c:out value="${widget.title}"/></h2>

            <div class="rightcolumn">
                <c:if test="${not empty widget.screenshotUrl or not empty widget.thumbnailUrl}">
                    <section class="formbox">
                        <c:if test="${not empty widget.screenshotUrl}">
                            <figure class="screenshot">
                                <img src="<c:out value="${widget.screenshotUrl}"/>" alt="">
                                <figcaption><fmt:message key="widget.screenshotUrl"/></figcaption>
                            </figure>
                        </c:if>

                        <c:if test="${not empty widget.thumbnailUrl}">
                            <figure>
                                <img src="<c:out value="${widget.thumbnailUrl}"/>" alt="">
                                <figcaption><fmt:message key="widget.thumbnailUrl"/></figcaption>
                            </figure>
                        </c:if>
                    </section>
                </c:if>
            </div>

            <div class="leftcolumn">
                <section class="formbox">
                    <h3><fmt:message key="admin.widgetdetail.editdata"/></h3>
                    <form:form id="updateWidget" action="update" commandName="widget" method="POST">
                        <form:errors cssClass="error" element="p"/>
                        <fieldset>
                            <input type="hidden" name="token" value="<c:out value="${tokencheck}"/>"/>
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
                                <form:label path="disableRendering"><fmt:message key="widget.disableRendering"/></form:label>
                                <form:checkbox path="disableRendering" id="disableRendering"/>
                                <form:errors path="disableRendering" cssClass="error"/>
                            </p>

                            <p>
                                <form:label path="disableRenderingMessage"><fmt:message key="widget.disableRenderingMessage"/></form:label>
                                <form:input path="disableRenderingMessage" cssClass="long" autofocus="autofocus"/>
                                <form:errors path="disableRenderingMessage" cssClass="error"/>
                            </p>

                            <p>
                                <form:label path="widgetStatus"><fmt:message key="widget.widgetStatus"/></form:label>
                                <form:select path="widgetStatus" items="${widgetStatus}"/>
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
                            <fmt:message key="admin.widgetdetail.updatebutton" var="updateButtonText"/>
                            <input type="submit" value="${updateButtonText}"/>
                        </fieldset>
                    </form:form>
                </section>

            </div>

            <div class="clear-float">

            </div>


        </article>
    </div>
</rave:rave_generic_page>