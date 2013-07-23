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
<%@ page isErrorPage="true" language="java" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<c:set var="showStackTrace"><portal:render-show-stack-trace /></c:set>
<%-- Note: This page has the body definition embedded so we can reference it from the web.xml file
and use it as the default error page for the entire application. --%>
<tiles:insertDefinition name="templates.base">
    <tiles:putAttribute name="pageTitleKey" value="page.error.title"/>
    <tiles:putAttribute name="body">
        <c:if test="${showStackTrace == 1}">
        <rave:navbar/>
            <header>
                <h1>
                    <fmt:message key="page.error.title" />
                </h1>
            </header>
        <div id="content" class="container-fluid navbar-spacer">
            <div id="errorMessage" class="errorMessage">
                <h2><fmt:message key="page.error.message" />
                    &nbsp;<a href="javascript: window.location.reload();"><fmt:message key="page.error.reload" /></a>.</h2><br/>
                <a id="showErrorStack"><fmt:message key="page.error.details" /></a>
            </div>
            <div id="errorStack" title="Error Details" >
                <pre class="errorTrace">
                    <c:out value="${requestScope['javax.servlet.error.message']}"/>

                    <c:forEach var="i" items="${requestScope['javax.servlet.error.exception'].stackTrace}">
                        <c:out value="${i}" /><br />
                    </c:forEach>
                </pre>
                <button type="button" class="toggleErrorStack" id="hideErrorStack">
                    Close
                </button>
            </div>
        </div>
        </c:if>
        <c:if test="${showStackTrace == 0}">
            <rave:navbar/>
            <header>
                <h1>
                    <fmt:message key="page.error.title" />
                </h1>
            </header>
            <div id="content" class="container-fluid navbar-spacer">
                <div id="errorMessage" class="errorMessage">
                    <h2><fmt:message key="page.error.message" />
                        &nbsp;<a href="javascript: window.location.reload();"><fmt:message key="page.error.reload" /></a>.</h2><br/>
                </div>
            </div>
        </c:if>


        <script>
            require(["rave", "jquery"], function(rave, $){
                rave.registerOnInitHandler(function(){
                    $("#showErrorStack").click(function(){
                        document.getElementById('errorStack').style.display = 'block';
                    })

                    $("#hideErrorStack").click(function(){
                        document.getElementById('errorStack').style.display = 'none';
                    })
                })
            })
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>
