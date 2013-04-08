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

<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:set var="namespace">
	<portlet:namespace />
</c:set>
<c:if test="${!empty feedbackMessage}">
	<div class="uportal-channel-success">
		<spring:message code="${feedbackMessage}" />
	</div>
</c:if>
<c:if test="${!empty warningMessage}">
	<div class="uportal-channel-warning">
		<spring:message code="${warningMessage}" />
	</div>
</c:if>
<table width="100%">
	<tbody>
		<tr>
			<td align="left">
			  <portlet:renderURL var="editUrl" portletMode="EDIT" windowState="MAXIMIZED" />
			  <a href="${editUrl}" class="uportal-button">Schedule Session</a>
			</td>
			<td align="right">
			 <portlet:actionURL portletMode="EDIT" var="deleteSessionActionUrl">
			     <portlet:param name="action" value="deleteSessions" />
		     </portlet:actionURL>
		     <form name="deleteSessions" action="${deleteSessionActionUrl}" method="post">
					<input id="dialog-confirm" value="Delete Session(s)" name="Delete"
						style="text-transform: none;" class="uportal-button"
						onclick="javascript:return confirm('Are you sure you wish to delete the session(s)?');"
						type="submit" />
		      </form>
	      </td>
		</tr>
	</tbody>
</table>

<c:choose>
	<c:when test="${fn:length(sessions) gt 0}">
		<table width="100%">
			<thead>
				<tr class="uportal-channel-table-header">
					<th width="15"><input id="${namespace}selectAllSessions"
						value="selectAllSessions" name="selectAllSessions" type="checkbox" /></th>
					<th>Session Name</th>
					<th>Start Date and Time</th>
					<th>End Date and Time</th>
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
					<tr align="center"
						class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
						<td><%--c:if test="${session.currUserCanEdit}">
								<input value="${session.sessionId}"
									class="${namespace}deleteSession" name="deleteSession"
									type="checkbox" />
							</c:if --%></td>
						<td><a href="${launchSessionUrl}">${session.sessionName}</a></td>
						<td><joda:format value="${session.startTime}"
								pattern="MM/dd/yyyy HH:mm" /></td>
						<td><joda:format value="${session.endTime}"
								pattern="MM/dd/yyyy HH:mm" /></td>
						<td><%--c:if test="${session.currUserCanEdit}">
								<input value="Edit" name="${session.sessionId}"
									class="uportal-button"
									onclick="window.location='${editSessionUrl}'" type="button">
							</c:if--%></td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
		<hr />
		</form>
	</c:when>
	<c:otherwise>
		</form>
		<b>No sessions available</b>
		<hr />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${fn:length(recordings) gt 0}">

		<table width="100%">
			<tbody>
				<tr>

					<portlet:actionURL portletMode="EDIT"
						var="deleteRecordingActionUrl">
						<portlet:param name="action" value="deleteRecordings" />
					</portlet:actionURL>

					<td align="left">&nbsp;</td>
					<form name="deleteRecordings" action="${deleteRecordingActionUrl}"
						method="POST">
						<td align="right"><input id="dialog-confirm"
							value="Delete Recording(s)" name="Delete"
							style="text-transform: none;" class="uportal-button"
							onclick="javascript:return confirm('Are you sure you wish to delete the recordings(s)?');"
							type="submit"></td>
				</tr>
			</tbody>
		</table>

		<table width="100%">
			<thead>
				<tr class="uportal-channel-table-header">
					<th width="15"><input id="${namespace}selectAllRecordings"
						value="selectAllRecordings" name="Recordings" type="checkbox" /></th>
					<th>Previously recorded</th>
					<th>Start Time</th>
					<th>Size</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="recording" items="${recordings}"
					varStatus="loopStatus">
					<portlet:renderURL var="editRecordingUrl" portletMode="EDIT"
						windowState="MAXIMIZED">
						<portlet:param name="recordingId" value="${recording.recordingId}" />
						<portlet:param name="action" value="editRecording" />
					</portlet:renderURL>
					<tr align="center"
						class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
						<td><c:if test="${recording.currUserCanDelete}">
								<input value="${recording.recordingId}"
									class="${namespace}deleteRecording" name="deleteRecording"
									type="checkbox" />
							</c:if></td>
						<td><a target="_blank" href="${recording.recordingUrl}"><c:out
									value="${recording.roomName}" /></a></td>
						<td><joda:format value="${recording.createdDate}"
								pattern="MM/dd/yyyy HH:mm" /></td>
						<td><c:out value="${recording.readableFileSize}" /></td>
						<td><c:if test="${recording.currUserCanDelete}">
								<input value="Edit" name="${recording.recordingId}"
									class="uportal-button"
									onclick="window.location='${editRecordingUrl}'" type="button">
							</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</form>
		<hr />

	</c:when>
	<c:otherwise>
		<br />
		<b>No recordings available</b>
		<hr />
	</c:otherwise>
</c:choose>
<script type="text/javascript">
    <rs:compressJs>
	up.jQuery(function() {
		var $ = up.jQuery;
		$(document).ready(
				function() {
					$('#${namespace}selectAllSessions').click(
							function() {
								$('.${namespace}deleteSession').attr('checked', this.checked);
							});
					$('#${namespace}selectAllRecordings').click(
							function() {
								$('.${namespace}deleteRecording').attr('checked', this.checked);
							});
				});
	});
    </rs:compressJs>
</script>

