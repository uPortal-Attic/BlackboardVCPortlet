<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>

<div id="${n}blackboardCollaboratePortlet" class="blackboardVCRoot config-telephony">
<c:if test="${!empty prefs['helpUrl'][0]}">
    <div class="help-link">
      <a href="${prefs['helpUrl'][0]}" target="_blank" class="uportal-button"><spring:message code="help" text="help"/></a>
    </div>
</c:if>

<portlet:renderURL var="viewSessionUrl" windowState="MAXIMIZED" portletMode="VIEW">
   <portlet:param name="sessionId" value="${session.sessionId}" />
   <portlet:param name="action" value="viewSession" />
</portlet:renderURL>
<table class="sessionName">
  <thead>
    <tr>
        <th style="text-align: left;"><spring:message code="sessionName"/></th>
        <th style="text-align: right;"><a href="${viewSessionUrl}">${session.sessionName}</a></th>
    </tr>
  </thead>
</table>
<br/>
<c:if test="${!empty errorCode }">
	<div class="error"><spring:message code="${errorCode }" /></div>
</c:if>
<c:if test="${!empty error }">
    <div class="error">${error }</div>
</c:if>
<portlet:actionURL portletMode="EDIT" var="saveTelephonyActionUrl">
  <portlet:param name="action" value="saveTelephony" />
</portlet:actionURL>
<form action="${saveTelephonyActionUrl}" method="post">
	<%-- Using nestedPath as form:form does not work for portlets see: https://jira.springsource.org/browse/SPR-10382 --%>
	<spring:nestedPath path="telephonyForm">
		<form:hidden path="sessionId"/>
		<table class="telephony">
		  <thead>
		    <tr>
		      <th colspan="2" style="text-align: left;">
		        <h3><spring:message code="label.manageTelephony" text="Manage Telephony" /></h3>
		      </th>
		    </tr>
		  </thead>
		  <tbody>
		  	<tr>
		  		<td>
                <span class="uportal-channel-strong">
                    <spring:message code="label.moderatorphone" text="Moderator Phone" />
                </span>
                <span>
                    <spring:message code="tooltip.phoneformat" text="tooltip.phoneformat" var="tooltipPhoneformat" htmlEscape="false" />
                    <a href="#" title="${tooltipPhoneformat}" class="${n}toolTip">
                        <img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/>
                    </a>
                </span>
                </td>
                <td>
                    <form:input path="chairPhone" style="width: 12em;" class="uportal-input-text" required="required"/>
                        <span class="required"><spring:message code="requiredCharacter" text="*" /></span>
                        <span class="uportal-channel-strong ">
                            <spring:message code="label.pin" text="Moderator PIN" />
                        </span>
                    <form:input path="chairPIN" style="width: 7em;" class="uportal-input-text" />
                    &nbsp;<form:errors path="chairPhone" cssClass="error"/>
                    &nbsp;<form:errors path="chairPIN" cssClass="error"/>
                </td>
		  	</tr>
		  	<tr>
		  		<td>
                    <span class="uportal-channel-strong">
                        <spring:message code="label.participantphone" text="Participant Phone" />
                    </span>
                    <span>
                        <a href="#" title="${tooltipPhoneformat}" class="${n}toolTip">
                            <img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/>
                        </a>
                    </span>
                </td>
                <td>
                    <form:input path="nonChairPhone" style="width: 12em;" class="uportal-input-text" required="required"/>
                        <span class="required"><spring:message code="requiredCharacter" text="*" /></span>
                        <span class="uportal-channel-strong">
                            <spring:message code="label.pin" text="Participant PIN" />
                        </span>
                    <form:input path="nonChairPIN" style="width: 7em;" class="uportal-input-text" />
                    &nbsp;<form:errors path="nonChairPhone" cssClass="error"/>
                    &nbsp;<form:errors path="nonChairPIN" cssClass="error"/>
                </td>
		  	</tr>
		  	<tr>
		  		<td>
                    <span class="uportal-channel-strong">
                        <spring:message code="label.sipphone" text="SIP Phone" />
                    </span>
                    <spring:message code="tooltip.sipphone" text="tooltip.sipphone" var="tooltipSIPPhone" htmlEscape="false" />
                    <a href="#" title="${tooltipSIPPhone}" class="${n}toolTip">
                        <img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/>
                    </a>
                </td>
                <td>
                    <form:input path="sessionSIPPhone" style="width: 12em;" class="uportal-input-text" required="required"/>
                        <span class="required"><spring:message code="requiredCharacter" text="*" /></span>
                        <span class="uportal-channel-strong">
                            <spring:message code="label.pin" text="SIP PIN" />
                        </span>
                    <form:input path="sessionPIN" style="width: 7em;" class="uportal-input-text" />
                    &nbsp;<form:errors path="sessionSIPPhone" cssClass="error"/>
                    &nbsp;<form:errors path="sessionPIN" cssClass="error"/>
                </td>
		  	</tr>
		  </tbody>
	</table>
    <span class="required">
        <spring:message code="requiredCharacter" text="*" /><spring:message code="required" text="required" />
    </span>
	</spring:nestedPath>
	<table>
	  <tfoot>
	  <tr>
		<td>
			<spring:message code="saveTelephony" var="saveTelephony" text="Save Telephony"/>
			<input class="uportal-button" value="${saveTelephony}" type="submit">
		</td>
		<td>
			<a href="${viewSessionUrl}" class="uportal-button"><spring:message code="cancel" text="cancel"/></a>
		</td>
		<td>
			<portlet:actionURL var="deleteTelephonyURL" portletMode="EDIT">
				<portlet:param name="sessionId" value="${session.sessionId}" />
				<portlet:param name="action" value="deleteTelephony" />
			</portlet:actionURL>
			<a href="${deleteTelephonyURL}" class="uportal-button"><spring:message code="deleteTelephony" text="Use Default Telephony"/></a>
		</td>
           </tr>
	  </tfoot>
	</table>
	
</form>
</div>

<script type="text/javascript">
(function($) {
   $(document).ready(function() {
      blackboardPortlet.showTooltip('.${n}toolTip');
   });          
})(blackboardPortlet.jQuery);
</script>
