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
      <a href="${prefs['helpUrl'][0]}" target="_blank" class="uportal-button"><spring:message code="help" text="help"/></a>
    </div>
</c:if>

<table class="sessionName">
  <thead>
    <tr>
        <th style="text-align: left;"><spring:message code="sessionName"/></th>
        <th style="text-align: right;">${session.sessionName}</th>
    </tr>
  </thead>
</table>

<table class="participantList">
  <thead>
    <tr>
      <th colspan="4" style="text-align: left;">
        <h2>Invite / Edit Participants</h2><br/>
        <small>Note: Participants listed here will receive an email invitation. Alternatively, you could manually share the Guest Link with participants.</small>
      </th>
    </tr>
    <tr>
        <th style="text-align: left;">Select</th>
        <th style="text-align: left;">Name</th>
        <th style="text-align: left;">Email Address</th>
        <th style="text-align: left;">Moderator ?</th>
    </tr>
  </thead>
  <tbody>
  </tbody>
  <tfoot>
    <tr>
      <td></td>
      <td>
        <div class="ajaxerror name"></div>
        <input type="text" name="newName"/>
      </td>
      <td>
        <div class="ajaxerror email"></div>
        <input type="text" name="newEmail"/>
      </td>
      <td>
        <select name="newModerator" class="moderator">
          <option value="true">Yes</option>
          <option selected="selected"  value="false">No</option>
        </select>
      </td>
    </tr>
    <tr>
        <td colspan="2" align="left"><button name="deleteSelected">Delete Selected</button></td>
        <td colspan="2" align="right"><button name="addParticipant">Add Another Participant</button></td>
    </tr>
  </tfoot>
</table>

<portlet:resourceURL var="getParticipantsUrl" id="getParticipants" escapeXml="false" />
<portlet:resourceURL var="addParticipantUrl" id="addParticipant" escapeXml="false" />
<portlet:resourceURL var="updateParticipantUrl" id="updateParticipant" escapeXml="false" />
<portlet:resourceURL var="deleteParticipantUrl" id="deleteParticipant" escapeXml="false" />

<script id="${n}participantTemplate" type="text/template">
  <td><input name="participant_select" type="checkbox" value="{{= cid }}"/></td>
  <td>{{=name}}</td>
  <td>{{=email}}</td>
  <td>
    <select name="moderator" class="moderator">
      <option {{= moderator == true ? 'selected="selected"' : '' }} value="true">Yes</option>
      <option {{= moderator == false ? 'selected="selected"' : '' }}  value="false">No</option>
    </select>
  </td>
</script>

<script type="text/javascript">
(function($, Backbone, _) {
   'use strict';
   
   blackboardPortlet.init({
      getParticipantsUrl: "${getParticipantsUrl}",
      addParticipantUrl: "${addParticipantUrl}",
      updateParticipantUrl: "${updateParticipantUrl}",
      deleteParticipantUrl: "${deleteParticipantUrl}",
      sessionId : '${session.sessionId}',
      participantTemplateSelector: '#${n}participantTemplate',
      participantsViewSelector: "div#${n}blackboardCollaboratePortlet table.participantList"
   });

})(blackboardPortlet.jQuery, blackboardPortlet.Backbone, blackboardPortlet._);
</script>
</div>
