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
<c:choose>
    <c:when test="${not empty errorMessage}">
        <rave:navbar/>
        <div id="na_content" class="container">
            <div class="row detail-widget storeItem">
                <div class="span3">
                    <div class="detail-widget-preview">
                        <div class="span8 detail-widget-main">
                            <div>
                                <h2>
                                    <fmt:message key="page.widget.title"/>
                                </h2>
                                <br/>
                                <span id="widget-0-title"><fmt:message
                                        key="page.widget.marketplace.showdetail.notavailable"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <rave:navbar pageTitle="${widget.title}"/>
        <div id="na_content" class="container">
            <div class="row detail-widget storeItem">
                <div class="span3">
                    <div class="detail-widget-preview">
                        <c:if test="${not empty widget.screenshotUrl}">
                            <div class="detailWidgetScreenshot">
                                <img src="${widget.screenshotUrl}"
                                     alt="<fmt:message key="page.general.screenshot"/>"
                                     title="<c:out value="${widget.title}"/> <fmt:message key="page.general.screenshot"/>"/>
                            </div>
                        </c:if>
                        <c:if test="${not empty widget.thumbnailUrl}">
                            <div>
                                <img src="<c:out value="${widget.thumbnailUrl}"/>"
                                     title="<c:out value="${widget.title}"/>"
                                     alt="<fmt:message key="page.general.thumbnail"/>"/>
                            </div>
                        </c:if>
                        <div id="widgetAdded_${widget.id}" class="detailWidgetAdd">
                            <button class="btn btn-primary btn-large storeItemButton"
                                    id="widgetMarketplaceConfirmAddButton"
                                    data-widget-url="<c:out value='${widget.url}'/>"
                                    data-widget-type="<c:out value='${widget.type}'/>">
                                <fmt:message key="page.widget.marketplace.addToStore"/>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="span8 detail-widget-main">
                    <div>
                        <h2>
                            <c:set var="widgetHasTitleUrl" value="${not empty widget.titleUrl}"/>
                            <c:if test="${widgetHasTitleUrl}"><a href="<c:out value="${widget.titleUrl}"/>"
                                                                 rel="external">
                            </c:if>
                            <span id="widget-${widget.id}-title"><c:out value="${widget.title}"/></span>
                            <c:if test="${widgetHasTitleUrl}"></a></c:if>
                        </h2>
                        <c:if test="${widget.disableRendering}">
                            <div class="storeWidgetDisabled">
                        <span class="widget-disabled-icon-store ui-icon ui-icon-alert"
                              title="<fmt:message key="widget.chrome.disabled"/>"></span>
                                <c:out value="${widget.disableRenderingMessage}" escapeXml="true"/>
                            </div>
                        </c:if>
                        <c:if test="${not empty widget.author}">
                            <p class="storeWidgetAuthor">
                                <fmt:message key="widget.author"/>
                                <c:out value=" "/><%-- intentional empty String in the c:out --%>
                                <c:choose>
                                    <c:when test="${not empty widget.authorEmail}">
                                        <a href="mailto:<c:out value="${widget.authorEmail}"/>"><c:out
                                                value="${widget.author}"/></a>
                                    </c:when>
                                    <c:otherwise><c:out value="${widget.author}"/></c:otherwise>
                                </c:choose>
                            </p>
                        </c:if>

                        <c:if test="${not empty widget.description}">
                            <p class="storeWidgetDesc"><c:out value="${widget.description}"/></p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>


        <portal:register-init-script location="${'AFTER_RAVE'}">
            <script>
                require(["rave", "portal/rave_store", "jquery"],
                        function (rave, raveStore, raveEventBindings, $) {
                            rave.registerOnInitHandler(function () {
                                $('#widgetMarketplaceConfirmAddButton').click(function () {
                                    var element = $(this);
                                    raveStore.confirmAddFromMarketplace(element.data('widget-url'), element.data('widget-type'));
                                })

                                raveStore.init('<c:out value="${referringPageId}"/>');
                                raveStore.initTags("<c:out value="${widget.id}"/>");
                            })
                        })
            </script>
        </portal:register-init-script>
    </c:otherwise>
</c:choose>
