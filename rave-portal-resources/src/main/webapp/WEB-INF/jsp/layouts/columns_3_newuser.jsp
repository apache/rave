<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<fmt:setBundle basename="messages"/>
<%--@elvariable id="page" type="org.apache.rave.portal.model.Page"--%>
<div class="columns_3_newuser_static">
    <fmt:message key="page.layout.newuser.introtext"/>
</div>

<div class="columns_3_newuser_widgets">
    <div class="columns_3_newuser_subtitle"><fmt:message key="page.layout.newuser.subtitle"/></div>
    <rave:region region="${page.regions[0]}" regionIdx="1" />
    <rave:region region="${page.regions[1]}" regionIdx="2" />
    <rave:region region="${page.regions[2]}" regionIdx="3" />
</div>