<%@ page isErrorPage="true" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<rave:rave_generic_page pageTitle="Error ${requestScope['javax.servlet.error.status_code']}: ${requestScope['javax.servlet.error.exception_type'].simpleName}">
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
                &nbsp;<a href="javascript: window.location.reload();"><fmt:message key="page.error.reload" /></a>.<br/>
                <a onclick="document.getElementById('errorStack').style.display = 'block';"><fmt:message key="page.error.details" /></a>
            </div>
        </div>
        <div id="errorStack" title="Error Details" >
            <pre class="errorTrace">
                <c:out value="${requestScope['javax.servlet.error.message']}"/>

                <c:forEach var="i" items="${requestScope['javax.servlet.error.exception'].stackTrace}">
                    <c:out value="${i}" /><br />
                </c:forEach>
            </pre>
            <button type="button"
                    onClick="document.getElementById('errorStack').style.display = 'none';">Close
            </button>
        </div>
    </div>
</rave:rave_generic_page>