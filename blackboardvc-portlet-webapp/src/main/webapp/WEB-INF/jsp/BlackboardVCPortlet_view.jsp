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
  <a href="${prefs['helpUrl'][0]}" target="_blank">Help</a>
</div>
</c:if>

<portlet:actionURL portletMode="EDIT" var="deleteSessionActionUrl">
  <portlet:param name="action" value="deleteSessions" />
</portlet:actionURL>
<form name="deleteSessions" action="${deleteSessionActionUrl}" method="post">
  <table width="100%">
    <tbody>
      <tr>
        <td align="left">
        	<sec:authorize var="adminAccess" access="hasRole('ROLE_ADMIN')" />
		      <c:choose>
		        <c:when test="${adminAccess}">
		        	<portlet:renderURL var="homeURL" portletMode="VIEW" windowState="MAXIMIZED" />
	          		<a href="${homeURL}" class="uportal-button"><spring:message code="adminHome" text="adminHome"/></a>
		        </c:when>
        		<c:otherwise>
	        		<portlet:renderURL var="editUrl" portletMode="EDIT" windowState="MAXIMIZED" />
		          	<a href="${editUrl}" class="uportal-button"><spring:message code="scheduleSession" text="scheduleSession"/></a>
        		</c:otherwise>
	        </c:choose>
        </td>
        <td align="right">
            <spring:message code="deleteSession" var="deleteSession" text="deleteSession"/>
            <spring:message code="areYouSureYouWantToDeleteSession" var="areYouSureYouWantToDeleteSession" text="areYouSureYouWantToDeleteSession"/>
          <input id="dialog-confirm" value="${deleteSession}" name="Delete"
            style="text-transform: none;" class="uportal-button"
            onclick="javascript:return confirm('${areYouSureYouWantToDeleteSession}');"
            type="submit" />
        </td>
      </tr>
    </tbody>
  </table>
  <c:choose>
    <c:when test="${fn:length(sessions) gt 0}">
      <table width="100%">
        <thead>
          <tr class="uportal-channel-table-header">
            <th width="15"><input id="${n}selectAllSessions" value="selectAllSessions" name="selectAllSessions" type="checkbox" /></th>
            <th><spring:message code="sessionName" text="sessionName"/></th>
            <th><spring:message code="startDateAndTime" text="startDateAndTime"/></th>
            <th><spring:message code="endDateAndTime" text="endDateAndTime"/></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="session" items="${sessions}" varStatus="loopStatus">
            <portlet:renderURL var="launchSessionUrl">
              <portlet:param name="sessionId" value="${session.sessionId}" />
              <portlet:param name="action" value="viewSession" />
            </portlet:renderURL>
            <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED">
              <portlet:param name="sessionId" value="${session.sessionId}" />
              <portlet:param name="action" value="editSession" />
            </portlet:renderURL>
            <tr align="center" class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
              <td>
                <sec:authorize access="hasPermission(#session, 'delete')">
                  <input value="${session.sessionId}" class="${n}deleteSession" name="deleteSession" type="checkbox" />
                </sec:authorize>
              </td>
              <td><a href="${launchSessionUrl}">${session.sessionName}</a></td>
              <td><joda:format value="${session.startTime}" pattern="MM/dd/yyyy HH:mm" /></td>
              <td><joda:format value="${session.endTime}" pattern="MM/dd/yyyy HH:mm" /></td>
              <td>
                <sec:authorize access="hasPermission(#session, 'edit')">
                  <spring:message code="edit" var="edit" text="edit"/>
                  <a href="${editSessionUrl}" class="uportal-button">${edit}</a>
                </sec:authorize>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:when>
    <c:otherwise>
      <b>No sessions available</b>
    </c:otherwise>
  </c:choose>
</form>

<hr />


<form name="deleteRecordings" action="${deleteSessionActionUrl}" method="post">
  <table width="100%">
    <tbody>
      <tr>
        <td align="left">
        </td>
        <td align="right">
          <spring:message code="deleteRecording" var="deleteRecording" text="deleteRecording"/>
          <spring:message code="areYouSureYouWantToDeleteRecording" var="areYouSureYouWantToDeleteRecording" text="areYouSureYouWantToDeleteRecording"/>
          <input id="dialog-confirm" value="${deleteRecording}" name="Delete"
            style="text-transform: none;" class="uportal-button"
            onclick="javascript:return confirm('${areYouSureYouWantToDeleteRecording}');"
            type="submit" />
        </td>
      </tr>
    </tbody>
  </table>
  <c:choose>
    <c:when test="${fn:length(recordings) gt 0}">
      <table width="100%">
        <thead>
          <tr class="uportal-channel-table-header">
            <th width="15"><input id="${n}selectAllRecordings" value="selectAllRecordings" name="Recordings" type="checkbox" /></th>
            <th><spring:message code="previouslyRecorded" text="previouslyRecorded"/></th>
            <th><spring:message code="startTime" text="startTime"/></th>
            <th><spring:message code="size" text="size"/></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="recording" items="${recordings}" varStatus="loopStatus">
            <portlet:renderURL var="editRecordingUrl" portletMode="EDIT" windowState="MAXIMIZED">
              <portlet:param name="recordingId" value="${recording.recordingId}" />
              <portlet:param name="action" value="editRecording" />
            </portlet:renderURL>
            <tr align="center" class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
              <td>
                <sec:authorize access="hasPermission(#recording, 'delete')">
                  <input value="${recording.recordingId}" class="${n}deleteRecording" name="deleteRecording" type="checkbox" />
                </sec:authorize>
              </td>
              <td><a href="${recording.recordingUrl}">${recording.roomName}</a></td>
              <td><joda:format value="${recording.creationDate}" pattern="MM/dd/yyyy HH:mm" /></td>
              <td>${recording.displayRecordingSize}</td>
              <td>
                <sec:authorize access="hasPermission(#recording, 'edit')">
                  <spring:message code="edit" var="edit" text="edit"/>
                  <a href="${editRecordingUrl}" class="uportal-button">${edit}</a>
                </sec:authorize>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:when>
    <c:otherwise>
      <b>No recordings available</b>
    </c:otherwise>
  </c:choose>
</form>
<script type="text/javascript">
<rs:compressJs>
(function($) {
blackboardPortlet.jQuery(function() {
  var $ = blackboardPortlet.jQuery;

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
    
    

    $('#${n}blackboardCollaboratePortlet .${n}deleteRecording').click(function() {
      if (!$(this).is(':checked')) {
        $('#${n}blackboardCollaboratePortlet #${n}selectAllRecordings').attr('checked', false);
      }
      else if ($('#${n}blackboardCollaboratePortlet .${n}deleteRecording').not(':checked').length == 0) {
        $('#${n}blackboardCollaboratePortlet #${n}selectAllRecordings').attr('checked', true);
      }
    });
       
    $('#${n}blackboardCollaboratePortlet #${n}selectAllRecordings').click(function() {
      $('#${n}blackboardCollaboratePortlet .${n}deleteRecording').attr('checked', $(this).is(':checked'));
    });
  });
});
})(blackboardPortlet.jQuery);
</rs:compressJs>
</script>
</div>