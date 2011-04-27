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
<h1>Hello ${pages[0].owner.username}, welcome to Rave!</h1>

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