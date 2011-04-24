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
  
  $Id$
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/>
<html>
<head>
  <title>Rave Home</title>
</head>
<body>
<h1>Hello ${pages[0].owner.userId}, welcome to Rave!</h1>

<c:if test="${errorMessage != null}">
   <p class="error">ERROR: ${errorMessage}</p>
</c:if>

<p>This is very early, alpha quality code. It doesn't do much yet. Note that we don't have authentication right now so
we are simply selecting a random user each time this page is loaded. Alternatively you can provide a userId request 
parameter to display a specific user (valid values are "canonical", "jane.doe" and "john.doe").</p>

<form action="login">
  <label for+"userId">User ID</label><input id="userId" name="userId"/>
  <input type="submit"/>
</form>

<table>
  <tr>
    <c:forEach var="region" items="${pages[0].regions}">
      <td>
        <c:forEach var="regionWidget" items="${region.regionWidgets}">
          <c:set var="widget" value="${regionWidget.widget}"/>
          ${widget.title}
          <br>
          <iframe src="/gadgets/ifr?url=${widget.url}&view=home" width="250" height="250" frameborder="0"></iframe>
          <br><br>
        </c:forEach>
      </td>
    </c:forEach>
  </tr>
</table>
</body>
</html>