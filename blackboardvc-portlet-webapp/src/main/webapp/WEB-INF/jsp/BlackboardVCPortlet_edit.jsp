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
<div class="uportal-channel-subtitle">1. <spring:message code="sessionInformation" text="sessionInformation"/></div>
<hr/>
<portlet:actionURL portletMode="EDIT" var="saveSessionActionUrl">
  <portlet:param name="action" value="saveSession" />
</portlet:actionURL>
<form action="${saveSessionActionUrl}" method="post">
  <%-- Using nestedPath as form:form does not work for portlets see: https://jira.springsource.org/browse/SPR-10382 --%>
  <spring:nestedPath path="sessionForm">
    <form:hidden path="newSession"/>
  <c:if test="${!sessionForm.newSession}">
    <form:hidden path="sessionId"/>
  </c:if>
  <table>
    <tbody>
      <tr>
        <td>
            <span class="uportal-channel-strong"><spring:message code="sessionName" text="sessionName"/>: </span>
        </td>
        <c:choose>
          <c:when test="${sessionForm.newSession}">
            <td><form:input path="sessionName" style="width: 50%;" class="uportal-input-text" />&nbsp;&nbsp;<form:errors path="sessionName" cssClass="error"/></td>
          </c:when>
          <c:otherwise>
            <td><form:input path="sessionName" readonly="true"/></td>
          </c:otherwise>
        </c:choose>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="uportal-channel-table-caption"><spring:message code="sessionNameCanNotBeChanged" text="sessionNameCanNotBeChanged"/></td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong"><spring:message code="startDateAndTime" text="startDateAndTime"/>: </span></td>
        <td>
          <form:input id="${n}startdatepicker" path="startDate" style="width: 82px;"/>&nbsp;
          <form:errors path="startDate" cssClass="error"/>&nbsp;
          <form:select path="startHour">
              <c:forEach var="i" begin="0" end="23" step="1">
                  <form:option value="${i}">${i}</form:option>
              </c:forEach>
          </form:select>
          :
          <form:select path="startMinute">
              <c:forEach var="i" begin="0" end="45" step="15">
                  <form:option value="${i}">${i}</form:option>
              </c:forEach>
          </form:select>
            <form:errors path="startHour" cssClass="error"/>&nbsp;
            <form:errors path="startMinute" cssClass="error"/>&nbsp;
            <form:errors path="startTime" cssClass="error"/>
        </td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong"><spring:message code="endDateAndTime" text="endDateAndTime"/>: </span></td>

        <td>
          <form:input id="${n}enddatepicker" path="endDate" style="width: 82px;"/>&nbsp;
            <form:errors path="endDate" cssClass="error"/>&nbsp;
            <form:select path="endHour">
            <c:forEach var="i" begin="0" end="23" step="1">
              <form:option value="${i}">${i}</form:option>
            </c:forEach>
          </form:select>
          :
          <form:select path="endMinute">
            <c:forEach var="i" begin="0" end="45" step="15">
              <form:option value="${i}">${i}</form:option>
            </c:forEach>
          </form:select>
            <form:errors path="endHour" cssClass="error"/>&nbsp;
            <form:errors path="endMinute" cssClass="error"/>&nbsp;
            <form:errors path="endTime" cssClass="error"/>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="uportal-channel-table-caption"><spring:message code="enterDatesInFormat" text="enterDatesInFormat"/></td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong"><spring:message code="earlySessionEntry" text="earlySessionEntry"/>: </span></td>
        <td>
          <form:select path="boundaryTime">
            <form:option value="15"><spring:message code="boundryTime15Minutes" text="boundryTime15Minutes"/></form:option>
            <form:option value="30"><spring:message code="boundryTime30Minutes" text="boundryTime30Minutes"/></form:option>
            <form:option value="45"><spring:message code="boundryTime45Minutes" text="boundryTime45Minutes"/></form:option>
            <form:option value="60"><spring:message code="boundryTime1Hour" text="boundryTime1Hour"/></form:option>
            <form:option value="120"><spring:message code="boundryTime2Hours" text="boundryTime2Hours"/></form:option>
            <form:option value="180"><spring:message code="boundryTime3Hours" text="boundryTime3Hours"/></form:option>
          </form:select>
        </td>
      </tr>
      <tr>
        <td></td>
        <td class="uportal-channel-table-caption"><spring:message code="periodBeforeStartOfSessionUsersCanEnter" text="periodBeforeStartOfSessionUsersCanEnter"/></td>
      </tr>
      <sec:authorize var="fullAccess" access="hasRole('ROLE_FULL_ACCESS')" />
      <c:choose>
        <c:when test="${fullAccess}">
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="maxSimultaneousTalkers" text="maxSimultaneousTalkers"/>: </span></td>
            <td>
              <form:select path="maxTalkers">
                <c:forEach var="i" begin="1" end="${serverConfiguration.maxAvailableTalkers}" step="1">
                  <form:option value="${i}">${i}</form:option>
                </c:forEach>
              </form:select>
            </td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption"><spring:message code="maxSimultaneousTalkersToStartSession" text="maxSimultaneousTalkersToStartSession"/></td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="maxCameras" text="maxCameras"/>: </span></td>
            <td>
              <form:select path="maxCameras">
                <c:forEach var="i" begin="1" end="${serverConfiguration.maxAvailableCameras}" step="1">
                  <form:option value="${i}">${i}</form:option>
                </c:forEach>
              </form:select>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="uportal-channel-table-caption"><spring:message code="maxCamerasToStartSession" text="maxCamerasToStartSession"/></td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="supervised" text="supervised"/>: </span></td>
            <td><form:checkbox path="mustBeSupervised"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption"><spring:message code="moderatorsMayViewAllPrivateMessagesInSession" text="moderatorsMayViewAllPrivateMessagesInSession"/></td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="allPermissions" text="allPermissions"/>: </span></td>
            <td><form:checkbox path="permissionsOn"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption"><spring:message code="allParticipantsHaveFullPermissionsToAccessSessionResources" text="allParticipantsHaveFullPermissionsToAccessSessionResources"/></td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="raiseHandOnEntry" text="raiseHandOnEntry"/>: </span></td>
            <td><form:checkbox path="raiseHandOnEnter"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption"><spring:message code="usersAutomaticallyRaiseHandsOnEntry" text="usersAutomaticallyRaiseHandsOnEntry"/></td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="recordingMode" text="recordingMode"/>: </span></td>
            <td>
              <form:select path="recordingMode" items="${recordingModes}" />
            </td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption"><spring:message code="modeOfRecordingForSession" text="modeOfRecordingForSession"/></td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="hideNamesInRecordings" text="hideNamesInRecordings"/>: </span></td>
            <td><form:checkbox path="hideParticipantNames"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption"><spring:message code="namesOfSessionParticipantsAreHiddenInRecordings" text="namesOfSessionParticipantsAreHiddenInRecordings"/></td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong"><spring:message code="allowInSessionInvitations" text="allowInSessionInvitations"/>: </span></td>
            <td><form:checkbox path="allowInSessionInvites"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption"><spring:message code="moderatorsMaySendInvitationsWhileInSession" text="moderatorsMaySendInvitationsWhileInSession"/></td>
          </tr>
        </c:when>
        <c:otherwise>
          <form:hidden path="maxTalkers"/>
          <form:hidden path="maxCameras"/>
          <form:hidden path="mustBeSupervised"/>
          <form:hidden path="permissionsOn"/>
          <form:hidden path="raiseHandOnEnter"/>
          <form:hidden path="recordingMode"/>
          <form:hidden path="hideParticipantNames"/>
          <form:hidden path="allowInSessionInvites"/>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>
    <table>
        <tbody>
            <tr>
                <td>
                    <spring:message code="saveSession" var="saveSession" text="saveSession"/>
                  <input class="uportal-button" value="${saveSession}" type="submit">
                </td>
                <td>
                  <portlet:renderURL var="cancelAction" portletMode="VIEW" windowState="NORMAL" />
                  <a href="${cancelAction}" class="uportal-button"><spring:message code="cancel" text="cancel"/></a>
                </td>
            </tr>
        </tbody>
    </table>
  </spring:nestedPath>
</form>
<c:if test="${!sessionForm.newSession}">
  <br/>
  <div class="uportal-channel-subtitle">2. <spring:message code="moderators" text="moderators"/></div>
  <hr>
  <portlet:actionURL portletMode="EDIT" var="manageModeratorActionUrl" />
  <form action="${manageModeratorActionUrl}" method="post">
    <table>
      <thead>
          <spring:nestedPath path="deleteModeratorsForm">
        <tr class="uportal-channel-table-header">
          <th><spring:message code="name" text="name"/></th>
          <th><spring:message code="emailAddress" text="emailAddress"/></th>
          <th>
              <spring:message code="deleteModerators" var="deleteModerators" text="deleteModerators"/>
              <input type="hidden" name="deleteModeratorSessionId" value="${sessionForm.sessionId}" />
              <input value="${deleteModerators}" name="action" class="uportal-button" type="submit" />&nbsp;
              <form:errors path="chairId" cssClass="error"/>
          </th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="user" items="${sessionChairs}" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
          <td>${user.displayName}</td>
          <td>${user.email}</td>
          <td><input value="${user.userId}" name="chairId" type="checkbox" /></td>
          </tr>
        </c:forEach>
        </spring:nestedPath>
        <spring:nestedPath path="addModeratorForm">
        <tr>
          <td>
              <input type="hidden" name="sessionId" value="${sessionForm.sessionId}" />
            <input name="moderatorName" type="text" />&nbsp;<form:errors path="moderatorName" cssClass="error"/>
          </td>
          <td>
            <input name="emailAddress" type="text" />&nbsp;<form:errors path="emailAddress" cssClass="error"/>
          </td>
          <td>
              <spring:message code="addModerator" var="addModerator" text="addModerator"/>
            <input id="${n}addModeratorSubmit" name="action" value="${addModerator}" class="uportal-button" type="submit"/>
          </td>
        </tr>
        </spring:nestedPath>
      </tbody>
    </table>
  </form>

  <br/>
  <div class="uportal-channel-subtitle">3. <spring:message code="participants" text="participants"/></div>
  <hr>
  <portlet:actionURL portletMode="EDIT" var="manageParticipantActionUrl" />
  <form action="${manageParticipantActionUrl}" method="post">
    <table>
      <thead>
      <spring:nestedPath path="deleteParticipantsForm">
      <tr class="uportal-channel-table-header">
          <th><spring:message code="name" text="name"/></th>
          <th><spring:message code="emailAddress" text="emailAddress"/></th>
          <th>
              <input type="hidden" name="deleteParticipantsSessionId" value="${sessionForm.sessionId}" />
              <spring:message code="deleteParticipants" var="deleteParticipants" text="deleteParticipants"/>
              <input value="${deleteParticipants}" name="action" class="uportal-button" type="submit" />&nbsp;
              <form:errors path="nonChairId" cssClass="error"/>
          </th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="user" items="${sessionNonChairs}" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
          <td>${user.displayName}</td>
          <td>${user.email}</td>
          <td><input value="${user.userId}" name="nonChairId" type="checkbox" /></td>
          </tr>
        </c:forEach>
        </spring:nestedPath>
        <spring:nestedPath path="addParticipantForm">
        <tr>
          <td>
              <input type="hidden" name="sessionId" value="${sessionForm.sessionId}" />
              <input name="participantName" type="text" />&nbsp;<form:errors path="participantName" cssClass="error"/>
          </td>
          <td>
            <input name="emailAddress" type="text" />&nbsp;<form:errors path="emailAddress" cssClass="error"/>
          </td>
          <td>
            <spring:message code="addParticipant" var="addParticipant" text="addParticipant"/>
            <input id="${n}addParticipantSubmit" name="action" value="${addParticipant}" class="uportal-button" type="submit"/>
          </td>
        </tr>
        </spring:nestedPath>
      </tbody>
    </table>
  </form>

  <sec:authorize var="fullAccess" access="hasRole('ROLE_FULL_ACCESS')">
    <br/>
    <div class="uportal-channel-subtitle">4. <spring:message code="fileUpload" text="fileUpload"/></div>
    <hr>
    <div class="uportal-channel-subtitle">
      <spring:message code="editscreen.presentationuploadsubtitle" text="Presentation upload" />
    </div>
    <portlet:actionURL portletMode="EDIT" var="managePresentationActionUrl" />
    <form action="${managePresentationActionUrl}" method="post" enctype="multipart/form-data">
      <input type="hidden" name="sessionId" value="${sessionForm.sessionId}" />
      <spring:message var="presentationUploadSubtitle" code="editscreen.presentationuploadsubtitle" text="Presentation upload"/>
      <table summary="${presentationUploadSubtitle}">
        <thead>
          <tr class="uportal-channel-table-header">
            <th><spring:message code="fileName" text="fileName"/></th>
            <th style="width: 70px;"></th>
          </tr>
        </thead>
        <tbody>
          <c:if test="${!empty presentation}">
            <tr class="uportal-channel-table-row-odd">
              <td>${presentation.filename}</td>
              <td>
                  <spring:message code="deletePresentation" var="deletePresentation" text="deletePresentation"/>
                  <input value="${deletePresentation}" name="action" class="uportal-button" type="submit">
              </td>
            </tr>
          </c:if>
          <tr>
            <td><input name="presentationUpload" size="40" type="file" accept=".wbd,.wbp,.elp,.elpx"></td>
            <td>
                <spring:message code="uploadPresentation" var="uploadPresentation" text="uploadPresentation"/>
                <input value="${uploadPresentation}" name="action" class="uportal-button" type="submit">
            </td>
          </tr>
          <tr>
            <td colspan="2" class="uportal-channel-table-caption">
              <spring:message code="editscreen.presentationuploadcaption" text="Select a presentation/plan file to upload. You can only attach one file at a time." />
            </td>
          </tr>
        </tbody>
      </table>
    </form>

    <div class="uportal-channel-subtitle">
      <spring:message code="editscreen.multimediauploadsubtitle" text="Multimedia upload" />
    </div>
    <portlet:actionURL portletMode="EDIT" var="manageMultimediaActionUrl" />
    <form action="${manageMultimediaActionUrl}" method="post" enctype="multipart/form-data">
      <input type="hidden" name="sessionId" value="${sessionForm.sessionId}" />
      <table summary="Multimedia upload">
        <thead>
          <tr class="uportal-channel-table-header">
            <th><spring:message code="fileName" text="fileName"/></th>
            <th><input value="Delete Multimedia Item(s)" name="action" class="uportal-button" type="submit" /></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${sessionMultimedia}" var="multimediaItem" varStatus="loopStatus">
            <tr class="${loopStatus.index % 2 == 0 ? 'uportal-channel-table-row-odd' : 'uportal-channel-table-row-even'}">
              <td>${multimediaItem.filename}</td>
              <td><input type="checkbox" name="deleteMultimedia" value="${multimediaItem.multimediaId}" /></td>
            </tr>
          </c:forEach>
          <tr>
            <td><input name="multimediaUpload" size="40" type="file" accept=".mpeg,.mpg,.mpe,.mov,.qt,.swf,.m4v,.mp3,.mp4,.mpeg,.wmv"></td>
            <td>
                <spring:message code="uploadMultimedia" var="uploadMultimedia" text="uploadMultimedia"/>
                <input value="${uploadMultimedia}" name="action" class="uportal-button" type="submit"></td>
          </tr>
          <tr>
            <td colspan="2" class="uportal-channel-table-caption"><spring:message code="selectOtherMultimediaFilesToUpload" text="selectOtherMultimediaFilesToUpload"/></td>
          </tr>
        </tbody>
      </table>
    </form>
  </sec:authorize>
</c:if>

<script type="text/javascript">
    <rs:compressJs>
    (function($) {
    	$(document).ready(function() {
    		$("#${n}startdatepicker").datepicker();
    		$("#${n}enddatepicker").datepicker();
    	});
    })(blackboardPortlet.jQuery);
    </rs:compressJs>
</script>
</div>
