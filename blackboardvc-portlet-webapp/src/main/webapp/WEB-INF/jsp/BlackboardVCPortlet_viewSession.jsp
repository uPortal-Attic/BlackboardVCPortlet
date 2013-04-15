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

<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${!empty errorMessage}">
        <div class="uportal-channel-error">${errorMessage}</div>
    </c:when>
    <c:otherwise>
        <table>
            <tbody>
               
                <tr><td align="left">Session name: </td><td>${session.sessionName}</td></tr>
                <tr><td align="left">Start time: </td><td><joda:format value="${session.startTime}" pattern="MM/dd/yyyy HH:mm" /></td></tr>
                <tr><td align="left">End time: </td><td><joda:format value="${session.endTime}" pattern="MM/dd/yyyy HH:mm" /></td></tr>
                <c:if test="${! empty guestUrl}">
                    <tr><td>Guest link: </td><td><a href="${guestUrl}" target="_blank">${guestUrl}</a></td></tr>  
                </c:if>

            </tbody>
        </table>
        <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED" >
                  <portlet:param name="sessionId" value="${session.sessionId}" />
                  <portlet:param name="action" value="editSession" />
        </portlet:renderURL>        
        <sec:authorize access="hasPermission(#session, 'edit')">
            <div>&nbsp;<input value="Edit Session" name="${session.sessionId}" class="uportal-button" onclick="window.location='${editSessionUrl}'" type="button"></div><br/>
        </sec:authorize>
            
        <c:if test="${! empty showCSVDownload}">
        <div>
        	<portlet:resourceURL var="csvDownloadURL" id="csvDownload" />
            <form target="_blank" action="${csvDownloadURL}" method="post">
                <input type="hidden" name="sessionId" value="${session.sessionId}"/>
                <input type="hidden" name="uid" value="${uid}"/>
                <input type="submit" name="downloadSubmit" value="Download participant list (CSV file)"/>
            </form>
        </div>
            <br/>
        </c:if>    
       
        <c:choose>
            <c:when test="${showLaunchSession eq 'false'}">
                <div><b>Session is now closed</b></div><br/>
            </c:when>
            <c:otherwise>
                <div><a href="${launchSessionUrl}" target="_blank">Launch session (Will open a new window)</a></div><br/>
            </c:otherwise>
        </c:choose>
       
    </c:otherwise>
</c:choose>
<portlet:renderURL var="backUrl" portletMode="VIEW" />
<input name="Back" value="Back" type="button" class="uportal-button" onclick="window.location='${backUrl}'"/>
