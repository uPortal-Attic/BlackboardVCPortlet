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
<div class="fl-widget portlet error view-detailed" role="section">

<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>

<div id="${n}blackboardCollaboratePortlet" class="blackboardVCRoot">
<c:if test="${!empty prefs['helpUrl'][0]}">
	<div class="help-link">
	  <a href="${prefs['helpUrl'][0]}" target="_blank" class="uportal-button"><spring:message code="help" text="help"/></a>
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
    	<spring:message code="statusText" text="statusText" var="statusText" htmlEscape="false" />
    	<td class="label"><span class="uportal-channel-strong"><spring:message code="status" text="Status"/></span>&nbsp;<a href="#" title="${ statusText}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a></td>
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
     	<td><a href="${session.launchUrl}" target="_blank">${session.launchUrl}</a></td></tr>
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
<c:if test="${telephonyEnabled eq 'true' }" >
	<br/>
	<table class="viewSession">
		<tr>
			<th style="text-align: left;">Telephony Information</th>
			<th style="text-align: right">
				<sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
					<portlet:renderURL var="configTelephonyURL" portletMode="EDIT" windowState="MAXIMIZED">
						<portlet:param name="sessionId" value="${session.sessionId}" />
					    <portlet:param name="action" value="configureTelephony" />
					</portlet:renderURL>
					<a href="${configTelephonyURL }" class="uportal-button">Configure Telephony</a>
				</sec:authorize>
			</th>
		</tr>
		<c:choose>
			<c:when test="${!empty sessionTelephony }">
				<sec:authorize access="hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')">
					<!-- Only want to see chair number/pin if chair -->
					<tr class="even">
						<td class="label">
							<span class="uportal-channel-strong">
								<spring:message code="moderatorPhone" text="Moderator Phone" />
							</span>
						</td>
						<td>
							${sessionTelephony.chairPhone }<c:if test="${!empty sessionTelephony.chairPIN }">&nbsp;PIN: ${sessionTelephony.chairPIN }</c:if>
						</td>
					</tr>
				</sec:authorize>
				<tr class="odd">
					<td class="label">
						<span class="uportal-channel-strong">
							<spring:message code="participantPhone" text="Participant Phone" />
						</span>
					</td>
					<td>
						${sessionTelephony.nonChairPhone } <c:if test="${!empty sessionTelephony.nonChairPIN }">&nbsp;PIN:&nbsp;${sessionTelephony.nonChairPIN }</c:if>
					</td>
				</tr>
				<tr class="even">
					<td class="label">
						<span class="uportal-channel-strong">
							<spring:message code="SIPPhone" text="SIP Phone" />
						</span>
					</td>
					<td>
						${sessionTelephony.sessionSIPPhone } <c:if test="${!empty sessionTelephony.sessionPIN }">&nbsp;PIN:&nbsp;${sessionTelephony.sessionPIN }</c:if>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan='2'><spring:message code="notelephony" text="No Telephony set. This session will utilize the default integrated telephony."/></td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
</c:if>
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
			<spring:message code="tooltip.participants" text="tooltip.participants" var="tooltipParticipants" htmlEscape="false" />
			&nbsp;<a href="#" title="${ tooltipParticipants}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		</td>
		<td>
			<ul>
		      <c:forEach var="user" items="${sessionChairs}">
		        <li>${user.displayName} (<spring:message code="moderator" text="moderator"/>)</li>
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
	<tr class="odd">
		<td class="label">
			<span class="uportal-channel-strong">
				<spring:message code="presentationFile" text="Presentation File" />
			</span>
			<spring:message code="tooltip.presentationFile" text="tooltip.presentationFile" var="tooltipPresentationFile" htmlEscape="false" />
			&nbsp;<a href="#" title="${ tooltipPresentationFile}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
			<br/>
			<span class="uportal-channel-table-caption"><spring:message code="presentationFileDesc" text="" /></span>
		</td>
		<td>
			<portlet:actionURL portletMode="EDIT" var="managePresentationActionUrl" />
		    <form action="${managePresentationActionUrl}" method="post" enctype="multipart/form-data">
		      <input type="hidden" name="sessionId" value="${session.sessionId}" />
		      <input type="hidden" name="needToSendInitialEmail" value="false" />
			
			  <c:choose>
			  <c:when test="${!empty session.presentation }">
				  ${session.presentation.filename } 
				  &nbsp;
				  <portlet:actionURL var="deletePresentationURL" portletMode="EDIT">
				    <portlet:param name="sessionId" value="${session.sessionId}" />
				    <portlet:param name="action" value="deletePresentation" />
				</portlet:actionURL>
		    	<a href="${deletePresentationURL}" class="destroy" title="Delete">&nbsp;</a>
			  </c:when>
			  <c:otherwise>
			  	No Presentation Uploaded
			  </c:otherwise>
			  </c:choose>
			  <br/>
			  <input name="presentationUpload" size="40" type="file" accept="${presentationFileTypes}">
              <c:if test="${!empty presentationUploadError}">
                  <span class="error">${presentationUploadError}</span>
              </c:if>
              <spring:message code="uploadPresentation" var="uploadPresentation" text="uploadPresentation"/>
           	  <input value="${uploadPresentation}" name="action" class="uportal-button" type="submit">
           	</form>
		</td>
	</tr>
	<tr class="even">
		<td class="label">
			<span class="uportal-channel-strong">
				<spring:message code="mediaFiles" text="Media Files" />
			</span>
			<spring:message code="tooltip.mediaFiles" text="tooltip.mediaFiles" var="tooltipMediaFiles" htmlEscape="false" />
			&nbsp;<a href="#" title="${ tooltipMediaFiles}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
			<br/>
			<span class="uportal-channel-table-caption"><spring:message code="mediaFilesDesc" text="" /></span>
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty multimedias }">
					<ul>
						<c:forEach items="${multimedias}" var="multimediaItem" varStatus="loopStatus">
				            <li>${multimediaItem.filename}</li>
				        </c:forEach>
			        </ul>
	        	</c:when>
	        	<c:otherwise>
	        		No Media files Uploaded
	        		<br/>
	        	</c:otherwise>
        	</c:choose>
			<portlet:renderURL var="addMediaFileUrl" portletMode="EDIT" windowState="MAXIMIZED" >
			    <portlet:param name="sessionId" value="${session.sessionId}" />
			    <portlet:param name="action" value="manageMediaFiles" />
			</portlet:renderURL>
	    	<a href="${addMediaFileUrl}" class="uportal-button">Upload Media File(s)</a>
		</td>
	</tr>
	<tr class="odd">
		<td class="label">
			<span class="uportal-channel-strong">
				<spring:message code="recordings" text="Recordings" />
			</span>
			<spring:message code="tooltip.recordings" text="tooltip.recordings" var="tooltipRecordings" htmlEscape="false" />
			&nbsp;<a href="#" title="${ tooltipRecordings}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty recordings }">
			<ul>
				<c:forEach items="${recordings }" var="recording">
					<li><a href="${recording.recordingUrl}" target="_blank">${recording.roomName}</a></li>
				</c:forEach>
			</ul>
			</c:when>
			<c:otherwise>
				No Recordings available
			</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>   

<script type="text/javascript">
(function($) {
   $(document).ready(function() {
      blackboardPortlet.showTooltip('.${n}toolTip');
   });			
})(blackboardPortlet.jQuery);
</script>

</div>
</div>