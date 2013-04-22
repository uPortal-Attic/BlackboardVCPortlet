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
<div class="uportal-channel-subtitle">1. Session information</div>
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
        <td><span class="uportal-channel-strong">Session Name: </span></td>
        <c:choose>
          <c:when test="${sessionForm.newSession}">
            <td><form:input path="sessionName" style="width: 50%;" class="uportal-input-text" />&nbsp;&nbsp;<form:errors path="sessionName" cssClass="error"/></td>
          </c:when>
          <c:otherwise>
            <td><form:hidden path="sessionName" />${session.sessionName}</td>
          </c:otherwise>
        </c:choose>

      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="uportal-channel-table-caption">The session name cannot be changed once created.</td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong">Start Date and Time: </span></td>
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
            <form:errors path="startMinute" cssClass="error"/>
        </td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong">End Date and Time: </span></td>

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
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="uportal-channel-table-caption">Enter dates as in
          mm/dd/yyyy format. Time must be entered in 15 minute increments.
          Start time must be in the future.</td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong">Early Session Entry: </span></td>
        <td>
          <form:select path="boundaryTime">
            <form:option value="15">15 minutes</form:option>
            <form:option value="30">30 minutes</form:option>
            <form:option value="45">45 minutes</form:option>
            <form:option value="60">1 hour</form:option>
            <form:option value="120">2 hours</form:option>
            <form:option value="180">3 hours</form:option>
          </form:select>
        </td>
      </tr>
      <tr>
        <td></td>
        <td class="uportal-channel-table-caption">The period before the start of the session during which users can enter the session.</td>
      </tr>
      <sec:authorize var="fullAccess" access="hasRole('ROLE_FULL_ACCESS')" />
      <c:choose>
        <c:when test="${fullAccess}">
          <tr>
            <td><span class="uportal-channel-strong">Max Simultaneous Talkers: </span></td>
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
            <td class="uportal-channel-table-caption">Maximum number of simultaneous talkers allowed at the start of a session.</td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong">Max Cameras: </span></td>
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
            <td class="uportal-channel-table-caption">Maximum number of simultaneous web cameras allowed at the start of a session.</td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong">Supervised: </span></td>
            <td><form:checkbox path="mustBeSupervised"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption">Moderators may view all private chat messages in the session.</td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong">All Permissions: </span></td>
            <td><form:checkbox path="permissionsOn"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption">All participants have full permissions access to session resources such as audio, whiteboard, etc.</td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong">Raise Hand on Entry: </span></td>
            <td><form:checkbox path="raiseHandOnEnter"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption">Users automatically raise their hand when they join the session.</td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong">Recording Mode: </span></td>
            <td>
              <form:select path="recordingMode" items="${recordingModes}" />
            </td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption">The mode of recording the session.</td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong">Hide Names in Recordings: </span></td>
            <td><form:checkbox path="hideParticipantNames"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption">Names of session participants are hidden from viewers of recordings.</td>
          </tr>
          <tr>
            <td><span class="uportal-channel-strong">Allow In-Session Invitations: </span></td>
            <td><form:checkbox path="allowInSessionInvites"/></td>
          </tr>
          <tr>
            <td></td>
            <td class="uportal-channel-table-caption">Moderators may send invitations to join the session from within the session.</td>
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
                  <input class="uportal-button" value="Save Session" type="submit">
                </td>
                <td>
                  <portlet:renderURL var="cancelAction" portletMode="VIEW" windowState="NORMAL" />
                  <a href="${cancelAction}" class="uportal-button">Cancel</a>
                </td>
            </tr>
        </tbody>
    </table>
  </spring:nestedPath>
</form>
<c:if test="${!sessionForm.newSession}">
  <br/>
  <div class="uportal-channel-subtitle">2. Moderators</div>
  <hr>
  <portlet:actionURL portletMode="EDIT" var="manageModeratorActionUrl" />
  <form action="${manageModeratorActionUrl}" method="post">
    <input type="hidden" name="sessionId" value="${sessionForm.sessionId}" />
    <table>
      <thead>
        <tr class="uportal-channel-table-header">
          <th>Name</th>
          <th>Email address</th>
          <th><input value="Delete Moderator(s)" name="action" class="uportal-button" type="submit" /></th>
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
        <tr>
          <td>
            <input name="displayName" type="text" />
          </td>
          <td>
            <input name="email" type="text" />
          </td>
          <td>
            <input id="${n}addModeratorSubmit" name="action" value="Add Moderator" class="uportal-button" type="submit"/>
          </td>
        </tr>
      </tbody>
    </table>
  </form>


  <br/>
  <div class="uportal-channel-subtitle">3. Participants</div>
  <hr>
  <portlet:actionURL portletMode="EDIT" var="manageParticipantActionUrl" />
  <form action="${manageParticipantActionUrl}" method="post">
    <input type="hidden" name="sessionId" value="${sessionForm.sessionId}" />
    <table>
      <thead>
        <tr class="uportal-channel-table-header">
          <th>Name</th>
          <th>Email address</th>
          <th><input value="Delete Participant(s)" name="action" class="uportal-button" type="submit" /></th>
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
        <tr>
          <td>
            <input name="displayName" type="text" />
          </td>
          <td>
            <input name="email" type="text" />
          </td>
          <td>
            <input id="${n}addParticipantSubmit" name="action" value="Add Participant" class="uportal-button" type="submit"/>
          </td>
        </tr>
      </tbody>
    </table>
  </form>

  <sec:authorize var="fullAccess" access="hasRole('ROLE_FULL_ACCESS')">
    <br/>
    <div class="uportal-channel-subtitle">4. File upload</div>
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
            <th>Filename</th>
            <th style="width: 70px;"></th>
          </tr>
        </thead>
        <tbody>
          <c:if test="${!empty presentation}">
            <tr class="uportal-channel-table-row-odd">
              <td>${presentation.filename}</td>
              <td>
                <input value="Delete Presentation" name="action" class="uportal-button" type="submit">
              </td>
            </tr>
          </c:if>
          <tr>
            <td><input name="presentationUpload" size="40" type="file" accept=".wbd,.wbp,.elp,.elpx"></td>
            <td><input value="Upload Presentation" name="action" class="uportal-button" type="submit"></td>
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
            <th>Filename</th>
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
            <td><input value="Upload Multimedia" name="action" class="uportal-button" type="submit"></td>
          </tr>
          <tr>
            <td colspan="2" class="uportal-channel-table-caption">Select other multimedia files to upload. Any files will be scanned for viruses upon upload.</td>
          </tr>
        </tbody>
      </table>
    </form>
  </sec:authorize>
</c:if>

<script type="text/javascript">
    <rs:compressJs>
  up.jQuery(function() {
    var $ = up.jQuery;
    $(document).ready(
        function() {
          $("#${n}startdatepicker").datepicker({
            showButtonPanel : true
          });
          $("#${n}enddatepicker").datepicker({
            showButtonPanel : true
          });
        });
  });
    </rs:compressJs>
</script>
</div>
