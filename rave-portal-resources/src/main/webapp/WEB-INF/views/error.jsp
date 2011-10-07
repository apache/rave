<%@ page isErrorPage="true" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
--%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%--
--%><%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %><%--
--%><%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%><%--
--%><rave:rave_generic_page pageTitle="Error: ${exception.message}">
    <header>
      <h1>
          <fmt:message key="page.error.title" />
      </h1>
    </header>
    <div id="content" >
        <div id="errorMessage">
            <div class="errorLogo"><img src="<spring:url value="images/error_generic.png" />" alt="Error has occurred"
                                        title="Error has occurred"/></div>
            <div class="errorMessage">
                <fmt:message key="page.error.message" />
                <a href="javascript: window.location.reload();"><fmt:message key="page.error.reload" /></a>.<br/>
                <a onclick="document.getElementById('errorStack').style.display = 'block';"><fmt:message key="page.error.details" /></a>
            </div>
        </div>
        <div id="errorStack" title="Error Details" >
            <button type="button"
                    style="margin-bottom: 10px;"
                    onClick="document.getElementById('errorStack').style.display = 'none';">Close
            </button>
            <pre class="errorTrace">
                ${exception}

                Stack Trace:
                <c:forEach var="i" items="${exception.stackTrace}">
                    ${i}
                </c:forEach>
            </pre>
        </div>
    </div>
</rave:rave_generic_page>