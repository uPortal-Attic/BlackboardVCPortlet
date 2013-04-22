<%@ include file="/WEB-INF/jsp/include.jsp"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<rs:aggregatedResources path="/resources.xml"/>

<portlet:defineObjects/>

<c:set var="n">
    <portlet:namespace />
</c:set>
<c:set var="prefs" scope="request" value="${renderRequest.preferences.map}" />
