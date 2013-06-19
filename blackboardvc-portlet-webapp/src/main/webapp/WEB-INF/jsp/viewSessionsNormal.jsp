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

<portlet:renderURL var="createAndEditSessionUrl" portletMode="EDIT" windowState="MAXIMIZED">
  <portlet:param name="action" value="createAndEditSession" />
</portlet:renderURL>
<table width="100%">
    <tbody>
      <tr>
        <td align="right">
	    	<a href="${createAndEditSessionUrl}" id="create-user" class="uportal-button"><spring:message code="scheduleSession" text="scheduleSession"/></a>
        </td>
      </tr>
    </tbody>
</table>
<div id="${n}tabs">
  <ul>
    <li><a href="#${n}tabs-1">Upcoming Sessions</a></li>
    <li><a href="#${n}tabs-2">Completed Sessions</a></li>
  </ul>
<div id="${n}tabs-1">
	<c:choose>
    <c:when test="${fn:length(upcomingSessions) gt 0}">
      <table width="100%" id="sessionList">
        <thead>
          <tr class="uportal-channel-table-header">
            <th><spring:message code="sessionName" text="sessionName"/></th>
            <th><spring:message code="startDateAndTime" text="startDateAndTime"/></th>
            <th><spring:message code="endDateAndTime" text="endDateAndTime"/></th>
            <spring:message code="statusText" text="statusText" var="statusText" htmlEscape="true" />
            <th id="${n}statusToolTip"><spring:message code="status" text="status"/>&nbsp;<a href="#" title="${ statusText}"><img alt="?" src="/images/questionmark.jpg"></a></th>

          </tr>
         </thead>
          <tbody>
          <c:forEach var="session" items="${upcomingSessions}"  begin="0" end="4" varStatus="loopStatus">
	          <portlet:renderURL var="viewSessionUrl">
			     <portlet:param name="sessionId" value="${session.sessionId}" />
			     <portlet:param name="action" value="viewSession" />
		      </portlet:renderURL>
		      <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED">
		     	<portlet:param name="sessionId" value="${session.sessionId}" />
		     	<portlet:param name="action" value="editSession" />
		      </portlet:renderURL>
          	  <tr class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
				<td><a href='${viewSessionUrl}'>${session.sessionName}</a></td>
				<td>
	      			<joda:format value="${session.startTime}" pattern="MM/dd/yyyy HH:mm z" />
			    </td>
			    <td>
			      <joda:format value="${session.endTime}" pattern="MM/dd/yyyy HH:mm z" />
			    </td>
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
          </c:forEach>
        </tbody>
      </table>
      <div style="float: right;">
      <br/>
      	Showing 
      	<c:choose>
	       <c:when test="${fn:length(upcomingSessions) gt 4}">
	       5
	       </c:when>
	       <c:otherwise>${fn:length(upcomingSessions)}</c:otherwise>
		</c:choose>
		of ${fn:length(upcomingSessions)}&nbsp;<a href="#">Show All Sessions</a>
      </div>
    </c:when>
    <c:otherwise>
      <b>No sessions available</b>
    </c:otherwise>
  </c:choose>
</div>
<div id="${n}tabs-2">
<!-- completedSessionList -->
	<c:choose>
    <c:when test="${fn:length(completedSessions) gt 0}">
      <table width="100%" id="completedSessionList">
        <thead>
          <tr class="uportal-channel-table-header">
            <th><spring:message code="sessionName" text="sessionName"/></th>
            <th><spring:message code="startDateAndTime" text="startDateAndTime"/></th>
            <th><spring:message code="endDateAndTime" text="endDateAndTime"/></th>
            <spring:message code="statusText" text="statusText" var="statusText" htmlEscape="true" />
            <th id="${n}statusToolTip"><spring:message code="status" text="status"/>&nbsp;<a href="#" title="${ statusText}"><img alt="?" src="/images/questionmark.jpg"></a></th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="completedSessions" items="${completedSessions}" begin="0" end="4" varStatus="loopStatus2">
	          <portlet:renderURL var="viewSessionUrl">
			     <portlet:param name="sessionId" value="${completedSessions.sessionId}" />
			     <portlet:param name="action" value="viewSession" />
		      </portlet:renderURL>
		      <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED">
		     	<portlet:param name="sessionId" value="${completedSessions.sessionId}" />
		     	<portlet:param name="action" value="editSession" />
		      </portlet:renderURL>
          	  <tr class="${loopStatus2.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
				<td><a href='${viewSessionUrl}'>${completedSessions.sessionName}</a></td>
				<td>
	      			<joda:format value="${completedSessions.startTime}" pattern="MM/dd/yyyy HH:mm z" />
			    </td>
			    <td>
			      <joda:format value="${completedSessions.endTime}" pattern="MM/dd/yyyy HH:mm z" />
			    </td>
			    <td>
			     <c:choose>
			       <c:when test="${completedSessions.endTime.beforeNow}">
			         <spring:message code="sessionIsClosed" text="sessionIsClosed"/>
			       </c:when>
			       <c:otherwise>
			     	<c:choose>
			  	   <c:when test="${completedSessions.startTimeWithBoundaryTime.beforeNow}">
			  	      	<a href='${completedSessions.launchUrl}' target="_blank"><spring:message code="joinNow" text="joinNow"/></a>
			  	   </c:when>
			  	   <c:otherwise>
			  	    	${completedSessions.timeUntilJoin}
			  	   </c:otherwise>
			         </c:choose>
			       </c:otherwise>
			     </c:choose>
			    </td>
          	  </tr>
          </c:forEach>
        </tbody>
      </table>
      <div style="float: right;">
      <br/>
      	Showing 
      	<c:choose>
	       <c:when test="${fn:length(completedSessions) gt 4}">
	       5
	       </c:when>
	       <c:otherwise>${fn:length(completedSessions)}</c:otherwise>
		</c:choose>
		of ${fn:length(completedSessions)}&nbsp;<a href="#">Show All Sessions</a>
      </div>
    </c:when>
    <c:otherwise>
      <b>No sessions available</b>
    </c:otherwise>
  </c:choose>
</div>
</div>
<script type="text/javascript">

(function($) 
		{
			blackboardPortlet.jQuery(function($) 
					{
  					$(document).ready(function() 
  							{
  								var tabs = $("#${n}tabs");
  								tabs.tabs();
  							});
  					
					});
			
		})(blackboardPortlet.jQuery);

</script>
</div>