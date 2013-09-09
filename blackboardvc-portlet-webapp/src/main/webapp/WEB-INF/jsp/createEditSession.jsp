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
<div class="fl-widget portlet error view-detailed" role="section">
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>

<div id="${n}" class="blackboardVCRoot">
<c:if test="${!empty prefs['helpUrl'][0]}">
	<div class="help-link">
	  <a href="${prefs['helpUrl'][0]}" target="_blank" class="uportal-button"><spring:message code="help" text="help"/></a>
	</div>
</c:if>
<div class="uportal-channel-subtitle"><spring:message code="scheduleSession" text="scheduleSession"/></div>
<portlet:actionURL portletMode="EDIT" var="saveSessionActionUrl">
  <portlet:param name="action" value="saveSession" />
</portlet:actionURL>
<form action="${saveSessionActionUrl}" method="post">
  <%-- Using nestedPath as form:form does not work for portlets see: https://jira.springsource.org/browse/SPR-10382 --%>
  <spring:nestedPath path="sessionForm">
  <form:hidden path="sessionId"/>
  <form:hidden path="newSession" />
  <form:hidden path="needToSendInitialEmail" />
  <form:errors path="" cssClass="error"/>
  <table width="100%">
    <tbody>
      <tr>
      	<th colspan="2" style="text-align: left;"><h3 class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all ui-accordion-icons ui-state-focus"><span class="uportal-channel-strong" style="padding-left:2.4em;"><spring:message code="enterInfo" text="enterInfo"/></span></h3></th>
      </tr>
      <tr>
        <td>
            <span class="uportal-channel-strong"><spring:message code="sessionName" text="sessionName"/></span>
        </td>
        <td>
	        <c:choose>
	          <c:when test="${sessionForm.newSession}">
	            <form:input path="sessionName" style="width: 50%;" class="uportal-input-text" />&nbsp;&nbsp;<form:errors path="sessionName" cssClass="error"/>
	          </c:when>
	          <c:otherwise>
	            <form:input path="sessionName" readonly="true"/>
	          </c:otherwise>
	        </c:choose>
        </td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong"><spring:message code="startDateAndTime" text="startDateAndTime"/></span></td>
        <td>
          <form:input id="${n}startdatepicker" path="startDate" style="width: 82px;"/>&nbsp;
          
          <form:input id="${n}startHourMinute" path="startHourMinute" style="width: 82px;"/>&nbsp;
          
          &nbsp;<spring:message code="centralTime" text="centralTime"/>
          &nbsp;<form:errors path="startDate" cssClass="error"/>
          &nbsp;<form:errors path="startHour" cssClass="error"/>
          &nbsp;<form:errors path="startMinute" cssClass="error"/>
          &nbsp;<form:errors path="startTime" cssClass="error"/>
          
          <br/>
          <span class="uportal-channel-table-caption">mm/dd/yyyy</span>
            
        </td>
      </tr>
      <tr>
        <td><span class="uportal-channel-strong"><spring:message code="endDateAndTime" text="endDateAndTime"/></span></td>

        <td>
          <form:input id="${n}enddatepicker" path="endDate" style="width: 82px;"/>&nbsp;
          
          <form:input id="${n}endHourMinute" path="endHourMinute" style="width: 82px;"/>&nbsp;
          &nbsp;<spring:message code="centralTime" text="centralTime"/>&nbsp;
          <form:errors path="endDate" cssClass="error"/>&nbsp;
            <form:errors path="endHour" cssClass="error"/>&nbsp;
            <form:errors path="endMinute" cssClass="error"/>&nbsp;
            <form:errors path="endTime" cssClass="error"/>
          <br/>
          <span class="uportal-channel-table-caption">mm/dd/yyyy</span>
        </td>
      </tr>
      <tr><td colspan="2">
      <div id="${n}accordion">
	      <h3 id="${n}accordion1"><span class="uportal-channel-strong" style="padding-left:2.4em;"><spring:message code="additionalSettings" text="additionalSettings" /></span>
	      <br/>
	      <span class="uportal-channel-table-caption" style="padding-left:3em;"><spring:message code="additionalSettingDesc" text="additionalSettingDesc"/></span>
	      </h3>
	      <div style="padding: 2em;">
	      <table>
		      <tr>
		        <td>
		        	<span class="uportal-channel-strong"><spring:message code="earlySessionEntry" text="earlySessionEntry"/></span>
		        	<spring:message code="tooltip.earlySessionEntry" text="tooltip.earlySessionEntry" var="tooltipEarlySessionEntry" htmlEscape="false" />
					&nbsp;<a href="#" title="${ tooltipEarlySessionEntry}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		        </td>
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
		      <%-- <sec:authorize var="fullAccess" access="hasRole('ROLE_FULL_ACCESS')" /> --%>
		      <c:set value="true" var="fullAccess"/> 
		      <c:choose>
		        <c:when test="${fullAccess}">
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="maxSimultaneousTalkers" text="maxSimultaneousTalkers"/></span>
		            	<spring:message code="tooltip.maxSimultaneousTalkers" text="tooltip.maxSimultaneousTalkers" var="tooltipMaxSimultaneousTalkers" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltipMaxSimultaneousTalkers}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td>
		              <form:select path="maxTalkers">
		                <c:forEach var="i" begin="1" end="${serverConfiguration.maxAvailableTalkers}" step="1">
		                  <form:option value="${i}">${i}</form:option>
		                </c:forEach>
		              </form:select>
		            </td>
		          </tr>
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="maxCameras" text="maxCameras"/></span>
		            	<spring:message code="tooltip.maxCamera" text="tooltip.maxCamera" var="tooltipMaxCamera" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltipMaxCamera}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td>
		              <form:select path="maxCameras">
		                <c:forEach var="i" begin="1" end="${serverConfiguration.maxAvailableCameras}" step="1">
		                  <form:option value="${i}">${i}</form:option>
		                </c:forEach>
		              </form:select>
		            </td>
		          </tr>
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="supervised" text="supervised"/></span>
		            	<spring:message code="tooltip.supervised" text="tooltip.supervised" var="tooltipSupervised" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltipSupervised}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td><form:checkbox path="mustBeSupervised"/></td>
		          </tr>
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="allPermissions" text="allPermissions"/></span>
		            	<spring:message code="tooltip.allPermissions" text="tooltip.allPermissions" var="tooltipAllPermissions" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltipAllPermissions}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td><form:checkbox path="permissionsOn"/></td>
		          </tr>
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="raiseHandOnEntry" text="raiseHandOnEntry"/></span>
		            	<spring:message code="tooltip.raiseHandOnEntry" text="tooltip.raiseHandOnEntry" var="tooltipRaiseHandOnEntry" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltipRaiseHandOnEntry}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td><form:checkbox path="raiseHandOnEnter"/></td>
		          </tr>
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="recordingMode" text="recordingMode"/></span>
		            	<spring:message code="tooltip.recordingMode" text="tooltip.recordingMode" var="tooltipRecordingMode" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltipRecordingMode}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td>
		              <form:select path="recordingMode" items="${recordingModes}" />
		            </td>
		          </tr>
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="hideNamesInRecordings" text="hideNamesInRecordings"/></span>
		            	<spring:message code="tooltip.hideNamesInRecordings" text="tooltip.hideNamesInRecordings" var="tooltiphideNamesInRecordings" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltiphideNamesInRecordings}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td><form:checkbox path="hideParticipantNames"/></td>
		          </tr>
		          <tr>
		            <td>
		            	<span class="uportal-channel-strong"><spring:message code="allowInSessionInvitations" text="allowInSessionInvitations"/></span>
		            	<spring:message code="tooltip.allowInSessionInvitations" text="tooltip.allowInSessionInvitations" var="tooltipallowInSessionInvitations" htmlEscape="false" />
						&nbsp;<a href="#" title="${ tooltipallowInSessionInvitations}" class="${n}toolTip"><img src='<c:url value="/images/questionmark.jpg"/>' alt="?"/></a>
		            </td>
		            <td><form:checkbox path="allowInSessionInvites"/></td>
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
	          </table>
	          </div>
          </div>
          </td></tr>
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

<script type="text/javascript">
    <rs:compressJs>
    (function($) {
    	$(document).ready(function() {
    		$("#${n}startdatepicker").datepicker();
    		$("#${n}enddatepicker").datepicker();
    		$( "#${n}accordion" ).accordion({
    		      collapsible: true,
    		      heightStyle: "content",
    		      autoHeight: false,
    		      active: false
    		});
    		blackboardPortlet.showTooltip('.${n}toolTip');
    		$("#${n}startHourMinute").timePicker({
    				endTimeSelector : "#${n}endHourMinute",
    				endDateSelector : "#${n}enddatepicker"
    		});
    		$("#${n}endHourMinute").timePicker();

    	});
    })(blackboardPortlet.jQuery);
    </rs:compressJs>
</script>
</div>
</div>
