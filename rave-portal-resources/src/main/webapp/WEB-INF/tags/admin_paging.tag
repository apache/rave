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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="searchResult" type="org.apache.rave.rest.model.SearchResult"--%>
<c:if test="${searchResult.pageSize lt searchResult.totalResults}">
    <%-- offset is 0 based, pages 1 based --%>
    <div class="pagination">
        <ul>
            <c:if test="${searchResult.currentPage gt 1}">
                <c:url var="pageUrl" value="">
                    <c:if test="${not empty searchTerm}"><c:param name="searchTerm" value="${searchTerm}"/></c:if>
                    <c:if test="${not empty selectedWidgetType}"><c:param name="widgettype" value="${selectedWidgetType}"/></c:if>
                    <c:if test="${not empty selectedWidgetStatus}"><c:param name="widgetstatus" value="${selectedWidgetStatus}"/></c:if>
                    <c:param name="offset" value="${(searchResult.currentPage - 2)  * searchResult.pageSize}"/>
                </c:url>
                <li><a href="<c:out value="${pageUrl}&referringPageId=${referringPageId}"/>">&lt;</a></li>
            </c:if>
            <c:forEach var="i" begin="1" end="${searchResult.numberOfPages}">
                <c:choose>
                    <c:when test="${i eq searchResult.currentPage}">
                        <li class="active"><a href="#">${i}</a></li>
                    </c:when>
                    <c:otherwise>
                        <c:url var="pageUrl" value="">
                            <c:if test="${not empty searchTerm}"><c:param name="searchTerm" value="${searchTerm}"/></c:if>
                            <c:if test="${not empty selectedWidgetType}"><c:param name="widgettype" value="${selectedWidgetType}"/></c:if>
                            <c:if test="${not empty selectedWidgetStatus}"><c:param name="widgetstatus" value="${selectedWidgetStatus}"/></c:if>
                            <c:param name="offset" value="${(i - 1) * searchResult.pageSize}"/>
                        </c:url>
                        <li><a href="<c:out value="${pageUrl}&referringPageId=${referringPageId}"/>">${i}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${searchResult.currentPage lt searchResult.numberOfPages}">
                <c:url var="pageUrl" value="">
                    <c:if test="${not empty searchTerm}"><c:param name="searchTerm" value="${searchTerm}"/></c:if>
                    <c:if test="${not empty selectedWidgetType}"><c:param name="widgettype" value="${selectedWidgetType}"/></c:if>
                    <c:if test="${not empty selectedWidgetStatus}"><c:param name="widgetstatus" value="${selectedWidgetStatus}"/></c:if>
                    <c:param name="offset" value="${(searchResult.currentPage)  * searchResult.pageSize}"/>
                </c:url>
                <li><a href="<c:out value="${pageUrl}&referringPageId=${referringPageId}"/>">&gt;</a></li>
            </c:if>
        </ul>
    </div>
</c:if>
