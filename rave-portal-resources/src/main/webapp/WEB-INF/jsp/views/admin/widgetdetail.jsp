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
<div class="container-fluid">
<div class="span3">
    <rave:admin_tabsheader/>
</div>
<article class="span12">

<ul class="pager">
    <li class="previous">
        <a href="<spring:url value="/app/admin/widgets"/>"><fmt:message key="admin.widgetdetail.goback"/> </a></li>
</ul>
    <h2>
        <c:out value="${widget.title}"/>
    </h2>

    <div class="rightcolumn">
        <c:if
                test="${not empty widget.screenshotUrl or not empty widget.thumbnailUrl}">
            <section class="formbox"><c:if
                    test="${not empty widget.screenshotUrl}">
                <figure class="screenshot"><img
                        src="<c:out value="${widget.screenshotUrl}"/>" alt="">
                    <figcaption>
                        <fmt:message key="widget.screenshotUrl"/></figcaption>
                </figure>
            </c:if> <c:if test="${not empty widget.thumbnailUrl}">
                <figure><img src="<c:out value="${widget.thumbnailUrl}"/>"
                             alt="">
                    <figcaption><fmt:message
                            key="widget.thumbnailUrl"/></figcaption>
                </figure>
            </c:if></section>
        </c:if>
    </div>

    <div class="leftcolumn">
        <section class="formbox">
            <h3>
                <fmt:message key="admin.widgetdetail.editdata"/>
            </h3>
            <form:form id="updateWidget" action="update" commandName="widget"
                       method="POST">
                <form:errors cssClass="error" element="p"/>
                <fieldset>
                    <input type="hidden" name="token"
                           value="<c:out value="${tokencheck}"/>"/>
                    <p>
                        <fmt:message key="form.some.fields.required"/>
                    </p>

                    <p>
                        <form:label path="title">
                            <fmt:message key="widget.title"/> *</form:label>
                        <form:input path="title" cssClass="long" required="required"
                                    autofocus="autofocus"/>
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
                        <label for="type1"><fmt:message key="widget.type"/> *</label> <label
                            for="type1" class="formradio"><form:radiobutton
                            path="type" value="OpenSocial"/> <fmt:message
                            key="widget.type.OpenSocial"/> </label> <label for="type2"
                                                                           class="formradio"><form:radiobutton path="type"
                                                                                                               value="W3C"/>
                        <fmt:message key="widget.type.W3C"/> </label>
                        <form:errors path="type" cssClass="error"/>
                    </p>
                    <p>
                        <a href="#" class="storeItemButton" id="fetchMetadataButton"
                           onclick="rave.api.rpc.getWidgetMetadata({
                                url: $('#url').get(0).value,
                                providerType: $('input:radio[name=type]:checked').val(),
                                successCallback: function(result) {
                                    var widget = result.result;
                                    $('#title').val(widget.title);
                                    $('#description').val(widget.description);
                                    $('#thumbnailUrl').val(widget.thumbnailUrl);
                                    $('#screenshotUrl').val(widget.screenshotUrl);
                                    $('#titleUrl').val(widget.titleUrl);
                                    $('#author').val(widget.author);
                                    $('#authorEmail').val(widget.authorEmail);
                                }
                            });">
                            <fmt:message key="page.updateWidgetMetadata.button"/> </a>

                    </p>
                    <p>
                        <form:label path="description">
                            <fmt:message key="widget.description"/> *</form:label>
                        <form:textarea path="description" required="required"
                                       cssClass="long"/>
                        <form:errors path="description" cssClass="error"/>
                    </p>

                    <p>
                        <form:label path="featured">
                            <fmt:message key="page.general.checkBox.featured"/>
                        </form:label>
                        <form:checkbox path="featured" id="featured"/>
                        <form:errors path="featured" cssClass="error"/>
                    </p>

                    <p>
                        <form:label path="disableRendering">
                            <fmt:message key="widget.disableRendering"/>
                        </form:label>
                        <form:checkbox path="disableRendering" id="disableRendering"/>
                        <form:errors path="disableRendering" cssClass="error"/>
                    </p>

                    <p>
                        <form:label path="disableRenderingMessage">
                            <fmt:message key="widget.disableRenderingMessage"/>
                        </form:label>
                        <form:input path="disableRenderingMessage" cssClass="long"
                                    autofocus="autofocus"/>
                        <form:errors path="disableRenderingMessage" cssClass="error"/>
                    </p>

                    <p>
                        <form:label path="widgetStatus">
                            <fmt:message key="widget.widgetStatus"/>
                        </form:label>
                        <form:select path="widgetStatus" items="${widgetStatus}"/>
                    </p>

                    <p>
                        <form:label path="categories">
                            <fmt:message key="widget.categories"/>
                        </form:label>
                        <form:select path="categories" items="${categories}" multiple="true" itemLabel="text" size="10"/>
                    </p>

                    <p>
                        <spring:bind path="thumbnailUrl">
                            <label for="thumbnailUrl"><fmt:message
                                    key="widget.thumbnailUrl"/> </label>
                            <input type="url" name="thumbnailUrl" id="thumbnailUrl"
                                   placeholder="http://example.com/thumbnail.png" class="long"
                                   value="<c:out value="${widget.thumbnailUrl}"/>"/>
                        </spring:bind>
                        <form:errors path="thumbnailUrl" cssClass="error"/>
                    </p>

                    <p>
                        <spring:bind path="screenshotUrl">
                            <label for="screenshotUrl"><fmt:message
                                    key="widget.screenshotUrl"/> </label>
                            <input type="url" name="screenshotUrl" id="screenshotUrl"
                                   placeholder="http://example.com/screenshot.png" class="long"
                                   value="<c:out value="${widget.screenshotUrl}"/>"/>
                        </spring:bind>
                        <form:errors path="screenshotUrl" cssClass="error"/>
                    </p>

                    <p>
                        <spring:bind path="titleUrl">
                            <label for="titleUrl"><fmt:message key="widget.titleUrl"/>
                            </label>
                            <input type="url" name="titleUrl" id="titleUrl" class="long"
                                   value="<c:out value="${widget.titleUrl}"/>"/>
                        </spring:bind>
                        <form:errors path="titleUrl" cssClass="error"/>
                    </p>

                    <p>
                        <form:label path="author">
                            <fmt:message key="widget.author"/>
                        </form:label>
                        <form:input path="author" cssClass="long"/>
                        <form:errors path="author" cssClass="error"/>
                    </p>
                    <p>
                        <spring:bind path="authorEmail">
                            <label for="authorEmail"><fmt:message
                                    key="widget.authorEmail"/> </label>
                            <input type="email" name="authorEmail" id="authorEmail"
                                   class="long" value="<c:out value="${widget.authorEmail}"/>"/>
                        </spring:bind>
                        <form:errors path="titleUrl" cssClass="error"/>
                    </p>

                </fieldset>
                <fieldset>
                    <fmt:message key="admin.widgetdetail.updatebutton"
                                 var="updateButtonText"/>
                    <button class="btn btn-primary" type="submit" value="${updateButtonText}">${updateButtonText}</button>
                </fieldset>
            </form:form></section>

    </div>

    <div class="clear-float"></div>


</article>
</div>
