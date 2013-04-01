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

<c:set var="namespace"><portlet:namespace/></c:set>

<c:if test="${!empty errorMessage}">
    <div class="uportal-channel-error"><spring:message code="${errorMessage}" text="${errorMessage}"/></div><br/>
</c:if>
<div class="uportal-channel-subtitle">1. Recording information</div>
<hr>

<%-- Define the main action and render URLs --%>
<portlet:actionURL portletMode="EDIT" var="formActionUrl">
    <portlet:param name="action" value="saveRecording"/>
</portlet:actionURL>

<form:form name="editRecording" action="${formActionUrl}" method="post">
     <c:if test="${!empty recording.recordingId}">
        <input type="hidden" name="recordingId" value="${recording.recordingId}"/>
    </c:if>
  
<table width="100%">
    <tbody>
        <tr>           
            <td><span class="uportal-channel-strong">Recording name*: </span></td>
            <td><input name="roomName" style="width: 50%;" class="uportal-input-text" value="${recording.roomName}"/></td>            
        </tr>
        <tr>
        	<td><span class="uportal-channel-strong">Start Time: </span></td>
        	<td><fmt:formatDate value="${recording.createdDate}" pattern="dd-MM-yyyy HH:mm" /></td>
        </tr>
        <tr>
            <td><span class="uportal-channel-strong">Size:</span></td><td>${recording.readableFileSize}</td>
        </tr>
    </tbody>
</table>
<table width="100%">
    <tbody>
        <tr>
            <td align="left"><input class="uportal-button" name="action" value="Save Recording" type="submit"></td>
                <portlet:renderURL var="cancelAction" portletMode="VIEW" windowState="NORMAL"/>
            <td align="right"><input class="uportal-button" name="cancel" value="Cancel" onclick="window.location='${cancelAction}'" type="button"></td>
        </tr>
    </tbody>

</table>
</form:form>

