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

<div id="${n}blackboardCollaboratePortlet" class="blackboardVCRoot">
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
<c:if test="${!empty error }">
	<div class="error"><spring:message code="${error }" /></div>
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
		  		<td><span class="uportal-channel-strong"><spring:message code="label.moderatorphone" text="Moderator Phone & PIN" /></span></td><td><form:input path="chairPhone" style="width: 12em;" class="uportal-input-text" /> <form:input path="chairPIN" style="width: 7em;" class="uportal-input-text" />&nbsp;<form:errors path="chairPhone" cssClass="error"/></td>
		  	</tr>
		  	<tr>
		  		<td><span class="uportal-channel-strong"><spring:message code="label.participantphone" text="Participant Phone & PIN" /></span></td><td><form:input path="nonChairPhone" style="width: 12em;" class="uportal-input-text" /> <form:input path="nonChairPIN" style="width: 7em;" class="uportal-input-text" /></td>
		  	</tr>
		  	<tr>
		  		<td><span class="uportal-channel-strong"><spring:message code="label.sipphone" text="SIP Phone & PIN" /></span></td><td><form:input path="sessionSIPPhone" style="width: 12em;" class="uportal-input-text" /> <form:input path="sessionPIN" style="width: 7em;" class="uportal-input-text" /></td>
		  	</tr>
		  </tbody>
	</table>
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
