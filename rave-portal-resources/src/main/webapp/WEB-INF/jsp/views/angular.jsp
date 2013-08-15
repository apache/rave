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
<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
<c:set var="jsDebugMode"><portal:render-js-debug-mode/></c:set>

<!DOCTYPE html>
<html>
<head>
    <base href="<spring:url value="/app/angular/${context}/"/>">
    <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet">
    <link href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" rel="stylesheet">
    <link rel="stylesheet" href="<spring:url value="/static/css/rave.css"/>"/>
    <script>
        require = {
            paths: {}
        };
    </script>>
<%--
    check to see if the javaScriptDebugMode is on, if so render the individual JS files,
    otherwise render the minified single file
    --%>
    <c:choose>
        <c:when test="${jsDebugMode == '1'}">
            <script>
                require.baseUrl = '<spring:url value="/static/script/"/>';
            </script>
        </c:when>
        <c:otherwise>
            <script>
                require.baseUrl = '<spring:url value="/static/script-built/"/>';
            </script>
        </c:otherwise>
    </c:choose>
    <script data-main="${context}/main.js"
            src="//cdnjs.cloudflare.com/ajax/libs/require.js/2.1.5/require.min.js"></script>

</head>
<body>
<div class="wrapper" ng-include="'/portal/static/html/${context}/index.html'" ng-cloak>

</div>

<%--<div ng-view></div>--%>

</body>
</html>