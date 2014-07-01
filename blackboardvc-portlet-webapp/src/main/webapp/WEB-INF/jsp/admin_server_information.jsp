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
	<div id="tabs">
        <ul>
          <li><a href="#tabs-serverInformation"><spring:message code="serverInformation" text="serverInformation"/></a></li>
          <li><a href="#tabs-serverQuota"><spring:message code="serverQuota" text="serverQuota"/></a></li>
          <li><a href="#tabs-recordingsDatafix"><spring:message code="recordingsDatafix" text="Recordings Datafix" /></a></li>
          <li><a href="#tabs-otherActions"><spring:message code="otherActions" text="Other Actions" /></a></li>
        </ul>
        <div id="tabs-serverInformation">
        	<table width="100%">
        	 <thead>
        	 	<tr class="uportal-channel-table-header">
        		 	<th>
        		 		<spring:message code="lastUpdated" text="lastUpdated"/>
        		 	</th>
        		 	<th>
        		 		<spring:message code="entityVersion" text="entityVersion"/>
        		 	</th>
        		 	<th>
        		 		<spring:message code="timezone" text="timezone"/>
        		 	</th>
        		 	<th>
        		 		<spring:message code="maxAvaliableCameras" text="maxAvaliableCameras" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="maxAvaliableTalkers" text="maxAvaliableTalkers" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="defaultBoundryTime" text="defaultBoundryTime" />
        		 	</th>
        	 	</tr>
        	 </thead>
        	 <tbody>
        	 	<tr align="center" class="uportal-channel-table-row-even">
        	 		<td>
        	 			<joda:format value="${serverConfiguration.lastUpdated}" pattern="MM/dd/yyyy HH:mm z" />
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.entityVersion}
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.timezone}
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.maxAvailableCameras}
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.maxAvailableTalkers}
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.boundaryTime} &nbsp; <spring:message code="minutes" text="minutes" />
        	 		</td>
        	 	</tr>
        	 </tbody>
        	</table>
        	
        	<table width="100%">
        	 <thead>
        	 	<tr class="uportal-channel-table-header">
        		 	<th>
        		 		<spring:message code="mustReserveSeat" text="mustReserveSeat" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="mayUseSecureSignOn" text="mayUseSecureSignOn" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="mayUseTelephony" text="mayUseTelephony" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="raiseHandOnEnter" text="raiseHandOnEnter" />
        		 	</th>
        	 	</tr>
        	 </thead>
        	 <tbody>
        	 	<tr align="center" class="uportal-channel-table-row-even">
        	 		<td>
        	 			${serverConfiguration.mustReserveSeats}
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.mayUseSecureSignOn}
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.mayUseTelephony}
        	 		</td>
        	 		<td>
        	 			${serverConfiguration.raiseHandOnEnter}
        	 		</td>
        	 	</tr>
        	 </tbody>
        	</table>
            </div>
            <div id="tabs-serverQuota">
        	
        	<table width="100%">
        	 <thead>
        	 	<tr class="uportal-channel-table-header">
        	 		<th>
        		 		<spring:message code="lastUpdated" text="lastUpdated" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="diskQuota" text="diskQuota" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="diskQuotaAvailable" text="diskQuotaAvailable" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="sessionQuotaAvailable" text="sessionQuotaAvailable" />
        		 	</th>
        		 	<th>
        		 		<spring:message code="sessionQuota" text="sessionQuota" />
        		 	</th>
        	 	</tr>
        	 </thead>
        	 <tbody>
        	 	<tr align="center" class="uportal-channel-table-row-even">
        	 		<td>
        	 			<joda:format value="${serverQuota.lastUpdated}" pattern="MM/dd/yyyy HH:mm z" />
        	 		</td>
        	 		<td>
        	 			${serverQuota.diskQuota}
        	 		</td>
        	 		<td>
        	 			${serverQuota.diskQuotaAvailable}
        	 		</td>
        	 		<td>
        	 			${serverQuota.sessionQuotaAvailable} 
        	 		</td>
        	 		<td>
        	 		 	${serverQuota.sessionQuota}
        	 		</td>
        	 	</tr>
        	 </tbody>
        	</table>
        </div>
        <div id="tabs-recordingsDatafix">
            <jsp:include page="recordingDatafix.jsp"></jsp:include>
        </div>
        <div id="tabs-otherActions">
        	
        	<portlet:renderURL var="viewAllSessions" portletMode="VIEW" windowState="MAXIMIZED">
        		<portlet:param name="action" value="viewAllSessions"/>
            </portlet:renderURL>
        	<a href="${viewAllSessions}" class="uportal-button"><spring:message code="viewAllSessions" text="viewAllSessions" /></a>
        </div>
    </div>
</div>

<script type="text/javascript">
(function($) {
    $(function() {
        $( "#tabs" ).tabs();
    });
})(blackboardPortlet.jQuery);
</script>
