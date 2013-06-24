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
<%@ page errorPage="/WEB-INF/jsp/views/error.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<%-- Expose any attributes defined in the tiles-defs.xml to the request scope for use in other tiles --%>
<tiles:importAttribute scope="request"/>
<!DOCTYPE html>
<html class="no-js">
<head>
    <rave:rave_meta_data />
    <title><rave:title /></title>
    <rave:client_side_templates/>
    <rave:rave_css/>
    <rave:custom_css/>
    <rave:third_party_head_js/>
    <portal:render-script location="${'HEAD'}" />
 </head>
<body>
<div class="wrapper">
<%-- Header Content --%>
	<tiles:insertAttribute name="header"/>
	<%-- Main Body Content --%>
	<tiles:insertAttribute name="body"/>
	<div class="push"></div>
</div>
<%-- Footer Content --%>
<tiles:insertAttribute name="footer"/>
<%-- render any script that needs to execute pre-src includes --%>
<portal:render-init-script location="${'BEFORE_RAVE'}" />
<%-- render rave_js tag --%>
<rave:rave_js/>
<%-- render any script that needs to execute post-src includes --%>
<portal:render-init-script location="${'AFTER_RAVE'}" />
<%-- render custom javascript from extension projects if the tag is overlayed --%>
<rave:custom_js/>
</body>
</html>
