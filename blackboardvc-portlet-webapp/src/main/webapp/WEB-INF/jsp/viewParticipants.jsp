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

<portlet:renderURL var="viewSessionUrl" windowState="MAXIMIZED" portletMode="VIEW">
   <portlet:param name="sessionId" value="${session.sessionId}" />
   <portlet:param name="action" value="viewSession" />
</portlet:renderURL>
<table class="sessionName">
  <thead>
    <tr>
        <th style="text-align: left;"><spring:message code="sessionName"/></th>
        <th style="text-align: right;"><a href="${viewSessionUrl}">${session.sessionName}</a></th>
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
        <th style="text-align: left;">
        	Moderator
        	<spring:message code="tooltip.whatIsModerator" text="tooltip.whatIsModerator" var="tooltipWhatIsModerator" htmlEscape="false" />
			&nbsp;<a href="#" title="${ tooltipWhatIsModerator}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
        </th>
    </tr>
  </thead>
  <tbody>
  </tbody>
  <tfoot>
    <tr>
      <td><input type="hidden" name="newUniqueId"/></td>
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
      <td colspan="2" align="right"><button name="addParticipant">Add Participant</button></td>
    </tr>
    <tr>
      <td colspan="4" align="left"><a href="${viewSessionUrl}" class="uportal-button">Complete</a></td>
    </tr>
  </tfoot>
</table>

<portlet:resourceURL var="getParticipantsUrl" id="getParticipants" escapeXml="false" />
<portlet:resourceURL var="addParticipantUrl" id="addParticipant" escapeXml="false" />
<portlet:resourceURL var="updateParticipantUrl" id="updateParticipant" escapeXml="false" />
<portlet:resourceURL var="deleteParticipantUrl" id="deleteParticipant" escapeXml="false" />
<portlet:resourceURL var="searchForParticipantsUrl" id="searchForParticipants" escapeXml="false" />

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

   var autoCompleteOpts = {
      searchForParticipantsUrl: "${searchForParticipantsUrl}",
      uniqueIdSelector: "div#${n}blackboardCollaboratePortlet table.participantList input[name='newUniqueId']",
      nameSelector: "div#${n}blackboardCollaboratePortlet table.participantList input[name='newName']",
      emailSelector: "div#${n}blackboardCollaboratePortlet table.participantList input[name='newEmail']",
      currentSearchRequest: null
   };
   
   blackboardPortlet.initParticipantAutoComplete(autoCompleteOpts);
   
   blackboardPortlet.initParticipantBackbone({
      getParticipantsUrl: "${getParticipantsUrl}",
      addParticipantUrl: "${addParticipantUrl}",
      updateParticipantUrl: "${updateParticipantUrl}",
      deleteParticipantUrl: "${deleteParticipantUrl}",
      sessionId : '${session.sessionId}',
      participantTemplateSelector: '#${n}participantTemplate',
      participantsViewSelector: "div#${n}blackboardCollaboratePortlet table.participantList",
      onSyncAjax: function() {
         if (autoCompleteOpts.currentSearchRequest != null) {
            autoCompleteOpts.currentSearchRequest.abort();
            autoCompleteOpts.currentSearchRequest = null;
         }
      }
   });
   
   $(document).ready(function() {
      blackboardPortlet.showTooltip('.${n}toolTip');
   });

})(blackboardPortlet.jQuery, blackboardPortlet.Backbone, blackboardPortlet._);
</script>
</div>
