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

<c:if test="${not empty errorMessage}">
	<div class="uportal-channel-error">
		<ul>
			<c:forEach var="error" items="${errorMessage}">
				<li><spring:message code="${error}" text="${error}" /></li>
			</c:forEach>
		</ul>
	</div>
	<br />
</c:if>
<div class="uportal-channel-subtitle">1. Session information</div>
<hr>

<%-- Define the main action and render URLs --%>
<%--<portlet:actionURL portletMode="EDIT" var="formActionUrl" />--%>

<portlet:actionURL portletMode="EDIT" var="formActionUrl">
	<portlet:param name="action" value="saveSession" />
</portlet:actionURL>

<portlet:actionURL portletMode="EDIT" var="addIntParticipantUrl">
	<portlet:param name="action" value="addInternalParticipant" />
</portlet:actionURL>

<portlet:renderURL portletMode="EDIT" var="addExtParticipantUrl">
	<portlet:param name="action" value="addExternalParticipant" />
</portlet:renderURL>

<form action="${formActionUrl}" method="post">
    <input type="hidden" name="newSession" value="${session.newSession}" />
	<c:if test="${!session.newSession}">
		<input type="hidden" name="sessionId" value="${session.sessionId}" />
	</c:if>
	<table>
		<tbody>
			<tr>
				<td><span class="uportal-channel-strong">Session Name: </span></td>
				<c:choose>
					<c:when test="${session.newSession}">
						<td><input name="sessionName" style="width: 50%;" class="uportal-input-text" value="${session.sessionName}" /></td>
					</c:when>
					<c:otherwise>
						<td><input type="hidden" name="sessionName" value="${session.sessionName}" />${session.sessionName}</td>
					</c:otherwise>
				</c:choose>

			</tr>
			<tr>
				<td>&nbsp;</td>
				<td class="uportal-channel-table-caption">The session name cannot be changed once created.</td>
			</tr>
			<tr>
				<td><span class="uportal-channel-strong">Start Date and Time: </span></td>
				<joda:format var="startDate" value="${session.startTime}" pattern="MM/dd/yyyy" />
				<joda:format var="startHour" value="${session.startTime}" pattern="H" />
				<joda:format var="startMinute" value="${session.startTime}" pattern="m" />
				
				<td><input style="width: 82px;" value="${startDate}" name="startDate" id="${n}startdatepicker" style="width: 70px;" type="text">&nbsp;
					<select name="startHour">
						<c:forEach var="i" begin="0" end="23" step="1">
							<option value="${i}" ${startHour == i ? 'selected' : ''}>${i}</option>
						</c:forEach>
					</select>
					: 
					<select name="startMinute">
						<c:forEach var="i" begin="0" end="45" step="15">
							<option value="${i}" ${startMinute == i ? 'selected' : ''}>${i}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><span class="uportal-channel-strong">End Date and Time: </span></td>
                <joda:format var="endDate" value="${session.endTime}" pattern="MM/dd/yyyy" />
                <joda:format var="endHour" value="${session.endTime}" pattern="H" />
                <joda:format var="endMinute" value="${session.endTime}" pattern="m" />
                
				<td><input style="width: 82px;" value="${endDate}" name="endDate" id="${n}enddatepicker" style="width: 70px;" type="text">&nbsp; 
					<select name="endHour">
						<c:forEach var="i" begin="0" end="23" step="1">
							<option value="${i}" ${endHour == i ? 'selected' : ''}>${i}</option>
						</c:forEach>
					</select>
					:  
					<select name="endMinute">
						<c:forEach var="i" begin="0" end="45" step="15">
							<option value="${i}" ${endMinute == i ? 'selected' : ''}>${i}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td class="uportal-channel-table-caption">Enter dates as in
					dd-mm-yyyy format. Time must be entered in 15 minute increments.
					Start time must be in the future.</td>
			</tr>
			<tr>
				<td><span class="uportal-channel-strong">Early Session Entry: </span></td>
				<td><select name="boundaryTime">
						<option value="15"
							${session.boundaryTime == "15" ? 'selected' : ''}>15 minutes</option>
						<option value="30"
							${session.boundaryTime == "30" ? 'selected' : ''}>30 minutes</option>
						<option value="45"
							${session.boundaryTime == "45" ? 'selected' : ''}>45 minutes</option>
						<option value="60"
							${session.boundaryTime == "60" ? 'selected' : ''}>1 hour</option>
						<option value="120"
							${session.boundaryTime == "120" ? 'selected' : ''}>2 hours</option>
						<option value="180"
							${session.boundaryTime == "180" ? 'selected' : ''}>3 hours</option>
				</select></td>
			</tr>
			<tr>
				<td></td>
				<td class="uportal-channel-table-caption">The period before the start of the session during which users can enter the session.</td>
			</tr>
            <sec:authorize var="fullAccess" access="hasRole('ROLE_FULL_ACCESS')" />
			<c:choose>
				<c:when test="${fullAccess}">
					<tr>
						<td><span class="uportal-channel-strong">Max
								Simultaneous Talkers: </span></td>
						<td>
							<select name="maxTalkers">
								<c:forEach var="i" begin="1" end="${serverConfiguration.maxAvailableTalkers}" step="1">
									<option value="${i}"
										${session.maxTalkers == i ? 'selected' : ''}>${i}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td></td>
						<td class="uportal-channel-table-caption">Maximum number of simultaneous talkers allowed at the start of a session.</td>
					</tr>
					<tr>
						<td><span class="uportal-channel-strong">Max Cameras:
						</span></td>
						<td>
							<select name="maxCameras">
								<c:forEach var="i" begin="1" end="${serverConfiguration.maxAvailableCameras}" step="1">
									<option value="${i}"
										${session.maxCameras == i ? 'selected' : ''}>${i}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td class="uportal-channel-table-caption">Maximum number of simultaneous web cameras allowed at the start of a session.</td>
					</tr>
					<tr>
						<td><span class="uportal-channel-strong">Supervised: </span></td>
						<td><input name="mustBeSupervised" type="checkbox" value="Y"
							${session.mustBeSupervised ? 'checked' : ''} /></td>
					</tr>
					<tr>
						<td></td>
						<td class="uportal-channel-table-caption">Moderators may view all private chat messages in the session.</td>
					</tr>
					<tr>
						<td><span class="uportal-channel-strong">All Permissions: </span></td>
						<td><input name="permissionsOn" type="checkbox" value="Y"
							${session.permissionsOn ? 'checked' : ''} /></td>
					</tr>
					<tr>
						<td></td>
						<td class="uportal-channel-table-caption">All participants have full permissions access to session resources such as audio, whiteboard, etc.</td>
					</tr>
					<tr>
						<td><span class="uportal-channel-strong">Raise Hand on Entry: </span></td>
						<td><input name="raiseHandOnEnter" type="checkbox" value="Y" ${session.raiseHandOnEnter ? 'checked' : ''} /></td>
					</tr>
					<tr>
						<td></td>
						<td class="uportal-channel-table-caption">Users automatically raise their hand when they join the session.</td>
					</tr>
					<tr>
						<td><span class="uportal-channel-strong">Recording Mode: </span></td>
						<td><select name="recordingMode" disabled>
								<option value="MANUAL"
									${session.recordingMode.name == "MANUAL" ? 'selected' : ''}>Manual</option>
								<option value="AUTOMATIC"
									${session.recordingMode.name == "AUTOMATIC" ? 'selected' : ''}>Automatic</option>
								<option value="DISABLED"
									${session.recordingMode.name == "DISABLED" ? 'selected' : ''}>Disabled</option>
						</select> <input type="hidden" name="recordingMode" value="${session.recordingMode.name}" /></td>
					</tr>
					<tr>
						<td></td>
						<td class="uportal-channel-table-caption">The mode of recording the session.</td>
					</tr>
					<tr>
						<td><span class="uportal-channel-strong">Hide Names in Recordings: </span></td>
						<td><input name="hideParticipantNames" type="checkbox" value="Y" ${session.hideParticipantNames  ? 'checked' : ''} /></td>
					</tr>
					<tr>
						<td></td>
						<td class="uportal-channel-table-caption">Names of session participants are hidden from viewers of recordings.</td>
					</tr>
					<tr>
						<td><span class="uportal-channel-strong">Allow In-Session Invitations: </span></td>
						<td><input name="allowInSessionInvites" type="checkbox" value="Y" ${session.allowInSessionInvites ? 'checked' : ''} /></td>
					</tr>
					<tr>
						<td></td>
						<td class="uportal-channel-table-caption">Moderators may send invitations to join the session from within the session.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<input type="hidden" name="maxTalkers"
						value="${session.maxTalkers}" />
					<input type="hidden" name="maxCameras"
						value="${session.maxCameras}" />
					<input type="hidden" name="mustBeSupervised"
						value="${session.mustBeSupervised}" />
					<input type="hidden" name="permissionsOn"
						value="${session.permissionsOn}" />
					<input type="hidden" name="raiseHandOnEnter"
						value="${session.raiseHandOnEnter}" />
					<input type="hidden" name="recordingMode"
						value="${session.recordingMode}" />
					<input type="hidden" name="hideParticipantNames"
						value="${session.hideParticipantNames}" />
					<input type="hidden" name="allowInSessionInvites"
						value="${session.allowInSessionInvites}" />
				</c:otherwise>
			</c:choose>

		</tbody>
	</table>

	<div class="uportal-channel-subtitle">2. Moderators</div>
	<hr>
	<table>
		<c:if test="${fn:length(moderators) gt 0}">
			<thead>
				<tr class="uportal-channel-table-header">
					<th>Username</th>
					<th>Name</th>
					<th>Email address</th>
					<th></th>
				</tr>
			</thead>
		</c:if>
		<tbody>
			<c:forEach var="moderator" items="${moderators}"
				varStatus="loopStatus">
				<input type="hidden" name="moderatorUids" value="${moderator.uid}" />
				<input type="hidden" name="moderatorDisplayNames"
					value="${moderator.displayName}" />
				<input type="hidden" name="moderatorEmails"
					value="${moderator.email}" />
				<tr
					class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
					<td>${moderator.uid}</td>
					<td>${moderator.displayName}</td>
					<td>${moderator.email}</td>
					<td><input value="${loopStatus.index}" name="deleteModerator"
						type="checkbox" /></td>
				</tr>
			</c:forEach>

			<c:if test="${fn:length(moderators) gt 0}">
				<tr>
					<td colspan="3"><input value="Delete Moderator(s)" name="action" style="text-transform: none;" class="uportal-button" type="submit" /></td>
				</tr>
			</c:if>
			<tr>
				<td colspan="3"><input id="${n}moderatiorUidInput" name="moderatorUid" type="text">&nbsp;<input id="${n}addModeratorSubmit" name="action"
					value="Add Moderator(s)" style="text-transform: none;"
					class="uportal-button" type="submit"></td>
			</tr>
			<tr>
				<td colspan="3" class="uportal-channel-table-caption">You can
					search for moderators using uun or display name. To search for
					multiple moderators, separate each with a comma.</td>
			</tr>
		</tbody>

	</table>


	<div class="uportal-channel-subtitle">3. Participants</div>
	<hr>

<%--
	<div class="uportal-channel-subtitle">Internal participants</div>
	<table>
		<c:if test="${fn:length(intParticipants) gt 0}">
			<thead>
				<tr class="uportal-channel-table-header">
					<th>Username</th>
					<th>Name</th>
					<th>Email address</th>
					<th></th>
				</tr>
			</thead>
		</c:if>
		<tbody>
			<c:forEach var="intParticipant" items="${intParticipants}" varStatus="loopStatus">
				<input type="hidden" name="intParticipantUids"
					value="${intParticipant.uid}" />
				<input type="hidden" name="intParticipantDisplayNames"
					value="${intParticipant.displayName}" />
				<input type="hidden" name="intParticipantEmails"
					value="${intParticipant.email}" />
				<tr
					class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
					<td>${intParticipant.uid}</td>
					<td>${intParticipant.displayName}</td>
					<td>${intParticipant.email}</td>
					<td><input value="${loopStatus.index}" name="deleteIntParticipant" type="checkbox" /></td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(intParticipants) gt 0}">
				<tr>
					<td colspan="3"><input value="Delete Internal Participant(s)"
						name="action" style="text-transform: none;" class="uportal-button"
						type="submit"></td>
				</tr>
			</c:if>
			<tr>
				<td colspan="3"><input id="${n}intParticipantInput"
					name="intParticipants" type="text">&nbsp;<input
					id="${n}addIntParticipantSubmit" name="action"
					value="Add Participant(s)" style="text-transform: none;"
					class="uportal-button" type="submit"></td>
			</tr>
			<tr>
				<td colspan="3" class="uportal-channel-table-caption">You can
					search for participants using uun or display name. To search for
					multiple participants, separate each with a comma.</td>
			</tr>
		</tbody>
	</table>
--%>

	<div class="uportal-channel-subtitle">External participants</div>
	<table>
		<c:if test="${fn:length(extParticipants) gt 0}">
			<thead>
				<tr class="uportal-channel-table-header">
					<th>Display Name</th>
					<th>Email address</th>
					<th></th>
				</tr>
			</thead>
		</c:if>
		<tbody>
			<c:forEach var="extParticipant" items="${extParticipants}"
				varStatus="loopStatus">

				<input type="hidden" name="extParticipantDisplayNames"
					value="${extParticipant.displayName}" />
				<input type="hidden" name="extParticipantEmails"
					value="${extParticipant.email}" />
				<tr
					class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
					<td><c:out value="${extParticipant.displayName}" /></td>
					<td><c:out value="${extParticipant.email}" /></td>
					<td><input value="${loopStatus.index}"
						name="deleteExtParticipant" type="checkbox" /></td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(extParticipants) gt 0}">
				<tr>
					<td colspan="3"><input value="Delete External Participant(s)"
						name="action" style="text-transform: none;" class="uportal-button"
						type="submit"></td>
				</tr>
			</c:if>
		</tbody>
	</table>
	<table>
		<tbody>
			<tr>
				<td>Display name:</td>
				<td><input id="${n}extParticipantDisplayNameInput"
					name="extParticipantDisplayName" type="text"></td>
			</tr>
			<tr>
				<td>Email:</td>
				<td><input id="${n}extParticipantEmailInput"
					name="extParticipantEmail" type="text"></td>
			</tr>
			<tr>
				<td><input id="${n}addExtParticipantSubmit"
					name="action" value="Add External Participant"
					class="uportal-button" type="submit"></td>
			</tr>
			<tr>
				<td colspan="3" class="uportal-channel-table-caption">Enter an
					external participant.</td>
			</tr>
		</tbody>
	</table>
	<c:choose>
		<c:when test="${!empty fullAccess}">
			<c:if test="${session.sessionId ne 0}">
				<div class="uportal-channel-subtitle">4. File upload</div>
				<hr />
				<div class="uportal-channel-subtitle">
					<spring:message code="editscreen.presentationuploadsubtitle"
						text="Presentation upload" />
				</div>
				<table
					summary="<spring:message code="editscreen.presentationuploadsubtitle" text="Presentation upload"/>">
					<thead>
						<tr class="uportal-channel-table-header">
							<th>Filename</th>
							<th style="width: 70px;"></th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${!empty presentation}">
							<tr class="uportal-channel-table-row-odd">
								<input type="hidden" name="presentationId"
									value="${presentation.presentationId}" />
								<td>${presentation.fileName}</td>
								<td><input value="Delete Presentation" name="action"
									class="uportal-button" type="submit"></td>
							</tr>
						</c:if>
						<tr>
							<td colspan="2"><input name="presentationUpload" size="40"
								type="file">&nbsp;<input value="Upload Presentation"
								name="action" class="uportal-button" type="submit"></td>
						</tr>
						<tr>
							<td colspan="2" class="uportal-channel-table-caption"><spring:message
									code="editscreen.presentationuploadcaption"
									text="Select a presentation/plan file to upload. You can only attach one file at a time." /></td>
						</tr>

					</tbody>
				</table>

				<div class="uportal-channel-subtitle">
					<spring:message code="editscreen.multimediauploadsubtitle"
						text="Multimedia upload" />
				</div>



				<table summary="Multimedia upload">
					<thead>
						<tr class="uportal-channel-table-header">
							<th>Filename</th>
							<th style="width: 70px;"></th>
						</tr>
					</thead>
					<tbody>

						<c:if test="${!empty multimedia}">
							<c:forEach items="${multimedia}" var="multimediaItem"
								varStatus="loopStatus">
								<tr
									class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
									<input type="hidden" name="multimediaId"
										value="${multimediaItem.multimediaId}" />
									<td>${multimediaItem.fileName}</td>
									<td><input type="checkbox" name="deleteMultimediaFiles"
										value="${multimediaItem.multimediaId}" /></td>
								</tr>
							</c:forEach>

							<tr>
								<td colspan="3"><input value="Delete Multimedia Item(s)"
									name="action" class="uportal-button" type="submit"></td>
							</tr>

						</c:if>
						<tr>
							<td colspan="2"><input name="multimediaUpload" size="40"
								type="file">&nbsp;<input value="Upload Multimedia"
								name="action" class="uportal-button" type="submit"></td>
						</tr>
						<tr>
							<td colspan="2" class="uportal-channel-table-caption">Select
								other multimedia files to upload. Any files will be scanned for
								viruses upon upload.</td>
						</tr>
					</tbody>
				</table>
			</c:if>
		</c:when>
	</c:choose>
	<table>
		<tbody>
			<tr>
				<td><input class="uportal-button" name="action"
					value="Save Session" type="submit"></td>
				<portlet:renderURL var="cancelAction" portletMode="VIEW"
					windowState="NORMAL" />
				<td><input class="uportal-button" name="cancel" value="Cancel"
					onclick="window.location='${cancelAction}'" type="button"></td>
			</tr>
		</tbody>

	</table>
</form>
<script type="text/javascript">
    <rs:compressJs>
	up.jQuery(function() {
		var $ = up.jQuery;
		$(document).ready(
				function() {
					$("#${n}startdatepicker").datepicker({
						showButtonPanel : true,
						dateFormat : 'mm/dd/yyyy'
					});
					$("#${n}enddatepicker").datepicker({
						showButtonPanel : true,
						dateFormat : 'mm/dd/yyyy'
					});
					$('#${n}moderatiorUidInput').keypress(
							function(e) {
								if (e.which == 13) {
									$('#${n}addModeratorSubmit')
											.focus().click();
									return false;
								}
							});
<%--
					$('#${n}intParticipantInput').keypress(
							function(e) {
								if (e.which == 13) {
									$('#${n}addIntParticipantSubmit')
											.focus().click();
									return false;
								}
							});
--%>
					$('#${n}extParticipantDisplayNameInput').keypress(
							function(e) {
								if (e.which == 13) {
									$('#${n}addExtParticipantSubmit')
											.focus().click();
									return false;
								}
							});
					$('#${n}extParticipantEmailInput').keypress(
							function(e) {
								if (e.which == 13) {
									$('#${n}addExtParticipantSubmit')
											.focus().click();
									return false;
								}
							});
				});
	});
    </rs:compressJs>
</script>
