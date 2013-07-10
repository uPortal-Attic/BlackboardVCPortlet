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
<portlet:renderURL var="createSessionUrl" portletMode="EDIT" windowState="MAXIMIZED" />
<table width="100%">
    <tbody>
      <tr>
        <td align="right" colspan="3">
        	<sec:authorize var="adminAccess" access="hasRole('ROLE_ADMIN')" />
		      <c:choose>
		        <c:when test="${adminAccess}">
		        	<portlet:renderURL var="homeURL" portletMode="VIEW" windowState="MAXIMIZED" />
	          		<a href="${homeURL}" class="uportal-button"><spring:message code="adminHome" text="adminHome"/></a>
		        </c:when>
        		<c:otherwise>
	        		<a href="${createSessionUrl }" id="create-user" class="uportal-button"><spring:message code="scheduleWebConferencingSession" text="scheduleWebConferencingSession"/></a>
        		</c:otherwise>
	        </c:choose>
        </td>
        <td align="right" width="2em" colspan="1">
        <c:if test="${!empty prefs['helpUrl'][0]}">
        	<a href="${prefs['helpUrl'][0]}" target="_blank" class="uportal-button"><spring:message code="help" text="help"/></a>
        </c:if>
        </td>
      </tr>
      <tr style="display: none;">
      	<portlet:actionURL portletMode="VIEW" var="filterSessionsUrl">
		  <portlet:param name="action" value="filterSessions" />
		</portlet:actionURL>
		<form name="filterSessions" action="${filterSessionsUrl}" method="post">
	      	<td style="font-weight:bold; text-align: right; width:20em;">
	      		<spring:message code="find" text="find" htmlEscape="false"/>
	      	</td>
	      	<td style="width:10em;">
	      		<select>
	      			<option value="ALL">All Sessions</option>
	      			<option value="30">All Sessions Next 30 Days</option>
	      			<option value="60">All Sessions Next 60 Days</option>
	      			<option value="90">All Sessions Next 90 Days</option>
	      			<option value="CUR">All Sessions in Current Year</option>
	      			<option value="LAST">All Sessions Last Year</option>
	      		</select>
	      		<input value="Find Sessions" style="text-transform: none;" class="uportal-button" type="submit" />
	      	</td>
	      	<td style="font-weight:bold; text-align: right; width:20em;">
	      		Search by <br/>Keyword
	      	</td>
	      	<td>
	      		<input type="text" name="keyword" /><input value="Search Sessions" style="text-transform: none;" class="uportal-button" type="submit" />
	      	</td>
      	</form>
      </tr>
    </tbody>
  </table>
<div id="${n}tabs">
  <ul>
    <li><a href="#${n}tabs-1">Upcoming Sessions</a></li>
    <li><a href="#${n}tabs-2">Completed Sessions</a></li>
  </ul>
<div id="${n}tabs-1">
<portlet:actionURL portletMode="EDIT" var="deleteSessionActionUrl">
  <portlet:param name="action" value="deleteSessions" />
</portlet:actionURL>
<form name="deleteSessions" action="${deleteSessionActionUrl}" method="post">
	<c:choose>
    <c:when test="${fn:length(upcomingSessions) gt 0}">
      <table width="100%" id="sessionList">
        <thead>
          <tr class="uportal-channel-table-header">
            <th class="dt" style="width: 1em;"><input id="${n}selectAllSessions" value="selectAllSessions" name="selectAllSessions" type="checkbox" /></th>
            <th class="dt" ><spring:message code="sessionName" text="sessionName"/></th>
            <th class="dt" ><spring:message code="startDateAndTime" text="startDateAndTime"/></th>
            <th class="dt" ><spring:message code="endDateAndTime" text="endDateAndTime"/></th>
                        <spring:message code="statusText" text="statusText" var="statusText" htmlEscape="false" />
            <th class="dt" style="width: 20em;" id="${n}statusToolTip"><spring:message code="status" text="status"/>&nbsp;<a href="#" title="${ statusText}" class="${n}statusLink"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a></th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
      <table>
	      <tr  width="100%" >
		      <td align="left">
		            <spring:message code="deleteSession" var="deleteSession" text="deleteSession"/>
		            <spring:message code="areYouSureYouWantToDeleteSession" var="areYouSureYouWantToDeleteSession" text="areYouSureYouWantToDeleteSession"/>
		          <input id="dialog-confirm" value="${deleteSession}" name="Delete"
		            style="text-transform: none;" class="uportal-button"
		            onclick="javascript:return confirm('${areYouSureYouWantToDeleteSession}');"
		            type="submit" />
		            <c:if test="${!empty deleteSessionError}">
		                <span class="error">${deleteSessionError}</span>
		            </c:if>
		        </td>
	      </tr>
      </table>
    </c:when>
    <c:otherwise>
      <b>No sessions available</b>
    </c:otherwise>
  </c:choose>
</form>
</div>
<div id="${n}tabs-2">
<!-- completedSessionList -->
<form name="deleteSessions" action="${deleteSessionActionUrl}" method="post">
	<c:choose>
    <c:when test="${fn:length(completedSessions) gt 0}">
      <table width="100%" id="completedSessionList">
        <thead>
          <tr class="uportal-channel-table-header">
            <th style="width: 1em;"><input id="${n}selectAllSessions" value="selectAllSessions" name="selectAllSessions" type="checkbox" /></th>
            <th><spring:message code="sessionName" text="sessionName"/></th>
            <th><spring:message code="startDateAndTime" text="startDateAndTime"/></th>
            <th><spring:message code="endDateAndTime" text="endDateAndTime"/></th>
            <spring:message code="statusText" text="statusText" var="statusText" htmlEscape="false" />
            <th style="width: 20em;" id="${n}statusToolTip"><spring:message code="status" text="status"/>&nbsp;<a href="#" title="${ statusText}" class="${n}statusLink"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a></th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
      <table>
	      <tr  width="100%" >
		      <td align="left">
		            <spring:message code="deleteSession" var="deleteSession" text="deleteSession"/>
		            <spring:message code="areYouSureYouWantToDeleteSession" var="areYouSureYouWantToDeleteSession" text="areYouSureYouWantToDeleteSession"/>
		          <input id="dialog-confirm" value="${deleteSession}" name="Delete"
		            style="text-transform: none;" class="uportal-button"
		            onclick="javascript:return confirm('${areYouSureYouWantToDeleteSession}');"
		            type="submit" />
		            <c:if test="${!empty deleteSessionError}">
		                <span class="error">${deleteSessionError}</span>
		            </c:if>
		        </td>
	      </tr>
      </table>
    </c:when>
    <c:otherwise>
      <b>No sessions available</b>
    </c:otherwise>
  </c:choose>
</form>
</div>
</div>
<script type="text/javascript">
<rs:compressJs>
//begin javascript
(function($) {
blackboardPortlet.jQuery(function() {
  var $ = blackboardPortlet.jQuery;
  
  $("#${n}tabs").tabs();
  
  var upcomingSessions = 
	  <json:array var="session" items="${upcomingSessions}" prettyPrint="true" escapeXml="false">
	    <json:array>
	    <portlet:renderURL var="viewSessionUrl" windowState="MAXIMIZED">
	     <portlet:param name="sessionId" value="${session.sessionId}" />
	     <portlet:param name="action" value="viewSession" />
	    </portlet:renderURL>
	    
	    <json:property name="deleteCheckbox">
	    <sec:authorize access="hasPermission(#session, 'delete')">
	        <input value='${session.sessionId}' class='${n}deleteSession' name='deleteSession' type='checkbox' />
	    </sec:authorize>
	    </json:property>
	    <json:property name="sessionName">
	  	  <a href='${viewSessionUrl}'>${session.sessionName}</a>
	    </json:property>
	    <json:property name="startTime">
	      <joda:format value="${session.startTime}" pattern="MM/dd/yyyy HH:mm z" />
	    </json:property>
	    <json:property name="endTime">
	      <joda:format value="${session.endTime}" pattern="MM/dd/yyyy HH:mm z" />
	    </json:property>
	    <json:property name="join">
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
	    </json:property>
	    </json:array>
	  </json:array>
	  
	  var completedSessions = 
		  <json:array var="completedSessions" items="${completedSessions}" prettyPrint="true" escapeXml="false">
		    <json:array>
		    <portlet:renderURL var="viewSessionUrl" windowState="MAXIMIZED">
		     <portlet:param name="sessionId" value="${completedSessions.sessionId}" />
		     <portlet:param name="action" value="viewSession" />
		    </portlet:renderURL>
		    
		    <json:property name="deleteCheckbox">
		    <sec:authorize access="hasPermission(#completedSessions, 'delete')">
		        <input value='${completedSessions.sessionId}' class='${n}deleteSession' name='deleteSession' type='checkbox' />
		    </sec:authorize>
		    </json:property>
		    <json:property name="sessionName">
		  	  <a href='${viewSessionUrl}'>${completedSessions.sessionName}</a>
		    </json:property>
		    <json:property name="startTime">
		      <joda:format value="${completedSessions.startTime}" pattern="MM/dd/yyyy HH:mm z" />
		    </json:property>
		    <json:property name="endTime">
		      <joda:format value="${completedSessions.endTime}" pattern="MM/dd/yyyy HH:mm z" />
		    </json:property>
		    <json:property name="join">
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
		  	    	${session.timeUntilJoin}
		  	   </c:otherwise>
		         </c:choose>
		       </c:otherwise>
		     </c:choose>
		    </json:property>
		    </json:array>
		  </json:array>

  $(document).ready(function() {
	  
	$('#${n}blackboardCollaboratePortlet .${n}deleteSession').click(function() {
	  if (!$(this).is(':checked')) {
		$('#${n}blackboardCollaboratePortlet #${n}selectAllSessions').attr('checked', false);
	  }
	  else if ($('#${n}blackboardCollaboratePortlet .${n}deleteSession').not(':checked').length == 0) {
		$('#${n}blackboardCollaboratePortlet #${n}selectAllSessions').attr('checked', true);
	  }
	});
       
    $('#${n}blackboardCollaboratePortlet #${n}selectAllSessions').click(function() {
      $('#${n}blackboardCollaboratePortlet .${n}deleteSession').attr('checked', $(this).is(':checked'));
    });
    
    var futureTable = $('#sessionList').dataTable( {
    		"aaData": upcomingSessions,
    		"aaSorting": [[3, "desc"]],
    		"bAutoWidth" : false,
    		"bDeferRender": true,
    		"sPaginationType": "full_numbers",
    		"aoColumns": [{ "bSortable": false },
    		              null,
    		              null,
    		              null,
    		              null
    		              ]
    		} );
    
  });
  
  var pastTable = $('#completedSessionList').dataTable( {
		"aaData": completedSessions,
		"aaSorting": [[3, "desc"]],
		"bAutoWidth" : false,
		"bDeferRender": true,
		"sPaginationType": "full_numbers",
		"aoColumns": [{ "bSortable": false },
		              null,
		              null,
		              null,
		              null
		              ]
	});
  
  blackboardPortlet.showTooltip('.${n}statusLink');
  
  
  
});
})(blackboardPortlet.jQuery);
</rs:compressJs>
</script>
</div>