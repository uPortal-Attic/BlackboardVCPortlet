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
	  <a href="${prefs['helpUrl'][0]}" target="_blank"><spring:message code="help" text="help"/></a>
	</div>
</c:if>
<portlet:renderURL var="backUrl" portletMode="VIEW" />
<a href="${backUrl}" class="uportal-button">&lt; Back to Session List</a>
<br/>
<table class="viewSession">
  <tbody>
  	<tr>
  		<th style="text-align: left;"><spring:message code="sessionSummary"/></th>
  		<th style="text-align: right;">
  		<sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
		  <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED" >
		    <portlet:param name="sessionId" value="${session.sessionId}" />
		    <portlet:param name="action" value="editSession" />
		  </portlet:renderURL>
  		  <a href="${editSessionUrl}" class="uportal-button"><spring:message code="editSession" text="Edit Session"/></a>
		  </sec:authorize>
		</th>
		</tr>
    <tr><td  class="label"><span class="uportal-channel-strong"><spring:message code="sessionName" text="sessionName"/></span></td><td>${session.sessionName}</td></tr>
    <tr class="even"><td  class="label" ><span class="uportal-channel-strong"><spring:message code="startTime" text="startTime"/></span></td><td><joda:format value="${session.startTime}" pattern="MM/dd/yyyy HH:mm z" /></td></tr>
    <tr><td  class="label"><span class="uportal-channel-strong"><spring:message code="endTime" text="endTime"/></span></td><td><joda:format value="${session.endTime}" pattern="MM/dd/yyyy HH:mm z" /></td></tr>
    <tr class="even">
    	<td class="label"><span class="uportal-channel-strong"><spring:message code="status" text="Status"/></span></td>
    	<td>
    		<c:choose>
		       <c:when test="${session.endTime.beforeNow}">
		         <spring:message code="sessionIsClosed" text="sessionIsClosed"/>
		       </c:when>
		       <c:otherwise>
		     	<c:choose>
		  	   <c:when test="${session.startTimeWithBoundaryTime.beforeNow}">
		  	      	<a href='${session.launchUrl}' target="_blank"><spring:message code="joinNow" text="joinNow"/></a>
		  	   </c:when>
		  	   <c:otherwise>
		  	    	${session.timeUntilJoin}
		  	   </c:otherwise>
		         </c:choose>
		       </c:otherwise>
	       </c:choose>
    	</td>
    </tr>
    <sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
      <tr>
      	<td class="label">
      		<span class="uportal-channel-strong">
      			<spring:message code="moderatorLink" text="moderatorLink"/>
      		</span>
      		<br/>
      		<span class="uportal-channel-table-caption"><spring:message code="moderatorLinkDesc" text="moderatorLinkDesc"/></span>
     	</td>
     	<td><a href="${session.guestUrl}" target="_blank">${session.guestUrl}</a></td></tr>
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
      <tr class="even">
      	<td class="label">
      		<span class="uportal-channel-strong">
      			<spring:message code="guestLink" text="guestLink"/>
      		</span>
      		<br/>
      		<span class="uportal-channel-table-caption"><spring:message code="guestLinkDesc" text="guestLinkDesc"/></span>
      	</td>
      	<td><a href="${session.guestUrl}" target="_blank">${session.guestUrl}</a></td></tr>
    </sec:authorize>
  </tbody>
</table>
<br/>
<table class="viewSession">
	<tr>
		<th colspan="2" style="text-align :left;"><spring:message code="additionalInfo" text="Additional Information"/></th>
	</tr>
	<tr>
		<td class="label">
			<span class="uportal-channel-strong">
				<spring:message code="participants" text="participants"/>
			</span>
		</td>
		<td>
			<ul>
		      <c:forEach var="user" items="${sessionChairs}">
		        <li>${user.displayName} ()<spring:message code="moderator" text="moderator"/>)</li>
		      </c:forEach>
		      <c:forEach var="user" items="${sessionNonChairs}">
		        <li>${user.displayName}</li>
		      </c:forEach>
		    </ul>
		    <br/>
		    <sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
		    	<portlet:renderURL var="addParticipantsUrl" portletMode="EDIT" windowState="MAXIMIZED" >
				    <portlet:param name="sessionId" value="${session.sessionId}" />
				    <portlet:param name="action" value="addParticipants" />
				</portlet:renderURL>
		    	<a href="${addParticipantsUrl}" class="uportal-button">Invite / Edit Participant(s)</a>
			</sec:authorize>		
		</td>
	</tr>
	<tr class="even">
		<td class="label">
			<span class="uportal-channel-strong">
				<spring:message code="presentationFiles" text="Presentation Files" />
			</span>
			<br/>
			<span class="uportal-channel-table-caption"><spring:message code="presentationFilesDesc" text="" /></span>
		</td>
		<td>
				<portlet:renderURL var="addPresentationFileUrl" portletMode="EDIT" windowState="MAXIMIZED" >
				    <portlet:param name="sessionId" value="${session.sessionId}" />
				    <portlet:param name="action" value="addPresentationFile" />
				</portlet:renderURL>
		    	<a href="${addPresentationFileUrl}" class="uportal-button">Upload Presentation File(s)</a>
		</td>
	</tr>
	<tr>
		<td class="label">
			<span class="uportal-channel-strong">
				<spring:message code="mediaFiles" text="Media Files" />
			</span>
			<br/>
			<span class="uportal-channel-table-caption"><spring:message code="mediaFilesDesc" text="" /></span>
		</td>
		<td>
			<portlet:renderURL var="addMediaFileUrl" portletMode="EDIT" windowState="MAXIMIZED" >
				    <portlet:param name="sessionId" value="${session.sessionId}" />
				    <portlet:param name="action" value="addMediaFile" />
				</portlet:renderURL>
		    	<a href="${addMediaFileUrl}" class="uportal-button">Upload Media File(s)</a>
		</td>
	</tr>
	<tr class="even">
		<td class="label">
			<span class="uportal-channel-strong">
				<spring:message code="recordings" text="Recordings" />
			</span>
		</td>
		<td>
			<%@ include file="/WEB-INF/jsp/recordingsList.jsp"%>
		</td>
	</tr>
</table>   
</div>
