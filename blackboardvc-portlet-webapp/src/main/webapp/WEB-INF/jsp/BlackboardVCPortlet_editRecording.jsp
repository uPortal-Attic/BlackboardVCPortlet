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

<div class="uportal-channel-subtitle">1. <spring:message code="recordingInformation" text="recordingInformation"/></div>
<hr>

<%-- Define the main action and render URLs --%>
<portlet:actionURL portletMode="EDIT" var="saveRecordingActionUrl">
  <portlet:param name="action" value="saveRecording" />
</portlet:actionURL>
<form action="${saveRecordingActionUrl}" method="post">
  <%-- Using nestedPath as form:form does not work for portlets see: https://jira.springsource.org/browse/SPR-10382 --%>
  <spring:nestedPath path="recording">
    <form:hidden path="recordingId"/>
    
    <table width="100%">
      <tbody>
        <tr>           
          <td>
            <span class="uportal-channel-strong"><spring:message code="recordingName" text="recordingName"/>: </span>
          </td>
          <td>
            <form:input path="roomName" style="width: 50%;" class="uportal-input-text"/>
          </td>            
        </tr>
        <tr>
          <td>
            <span class="uportal-channel-strong"><spring:message code="startTime" text="startTime"/>: </span>
          </td>
          <td>
            <joda:format value="${recording.creationDate}" pattern="MM/dd/yyyy HH:mm z" />
          </td>
        </tr>
        <tr>
          <td>
            <span class="uportal-channel-strong"><spring:message code="size" text="size"/>:</span>
          </td>
          <td>${recording.displayRecordingSize}</td>
        </tr>
      </tbody>
    </table>
    
    <table>
      <tbody>
        <tr>
          <td>
            <spring:message code="saveRecording" var="saveRecording" text="saveRecording"/>
            <input class="uportal-button" value="${saveRecording}" type="submit">
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

