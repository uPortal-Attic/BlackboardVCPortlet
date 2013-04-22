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

<div id="${n}" class="blackboardVCRoot">
<c:if test="${!empty prefs['helpUrl'][0]}">
	<div class="help-link">
	  <a href="${prefs['helpUrl'][0]}" target="_blank">Help</a>
	</div>
</c:if>

<table>
  <tbody>
    <tr><td align="left">Session name: </td><td>${session.sessionName}</td></tr>
    <tr><td align="left">Start time: </td><td><joda:format value="${session.startTime}" pattern="MM/dd/yyyy HH:mm" /></td></tr>
    <tr><td align="left">End time: </td><td><joda:format value="${session.endTime}" pattern="MM/dd/yyyy HH:mm" /></td></tr>
    <sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
      <tr><td>Guest link: </td><td><a href="${session.guestUrl}" target="_blank">${session.guestUrl}</a></td></tr>  
    </sec:authorize>
  </tbody>
</table>   
<sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
  <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED" >
    <portlet:param name="sessionId" value="${session.sessionId}" />
    <portlet:param name="action" value="editSession" />
  </portlet:renderURL>
  <div>&nbsp; <a href="${editSessionUrl}" class="uportal-button">Edit Session</a></div><br/>
</sec:authorize>
    
<sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
  <div>
    <h4>Moderators</h4>
    <ul>
      <c:forEach var="user" items="${sessionChairs}">
        <li>${user.displayName}</li>
      </c:forEach>
    </ul>
    <h4>Participants</h4>
    <ul>
      <c:forEach var="user" items="${sessionNonChairs}">
        <li>${user.displayName}</li>
      </c:forEach>
    </ul>
  </div>
  <br/>
</sec:authorize>    

<c:choose>
    <c:when test="${empty launchUrl}">
        <div><b>Session is now closed</b></div><br/>
    </c:when>
    <c:otherwise>
        <div><a href="${launchUrl}" target="_blank">Launch session (Will open a new window)</a></div><br/>
    </c:otherwise>
</c:choose>
       
<portlet:renderURL var="backUrl" portletMode="VIEW" />
<a href="${backUrl}" class="uportal-button">Back</a>

</div>