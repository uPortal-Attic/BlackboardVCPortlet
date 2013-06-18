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

<portlet:renderURL var="createAndEditSessionUrl" portletMode="EDIT" windowState="MAXIMIZED">
  <portlet:param name="action" value="createAndEditSession" />
</portlet:renderURL>
<div id="dialog-form" title="Create New Session">
  <p class="validateTips">All fields are required.</p>
  
  <form name="createSession" id="createSession" action="${createAndEditSessionUrl}" method="post">
  <fieldset style="margin-top: 1em;">
    <label style="font-weight: bold;" for="name">Session Name</label>
    <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
  </fieldset>
  </form>
</div>
<table width="100%">
    <tbody>
      <tr>
        <td align="right">
        	<sec:authorize var="adminAccess" access="hasRole('ROLE_ADMIN')" />
		      <c:choose>
		        <c:when test="${adminAccess}">
		        	<portlet:renderURL var="homeURL" portletMode="VIEW" windowState="MAXIMIZED" />
	          		<a href="${homeURL}" class="uportal-button"><spring:message code="adminHome" text="adminHome"/></a>
		        </c:when>
        		<c:otherwise>
	        		<a href="#" id="create-user" class="uportal-button"><spring:message code="scheduleSession" text="scheduleSession"/></a>
        		</c:otherwise>
	        </c:choose>
        </td>
        <c:if test="${!empty prefs['helpUrl'][0]}">
	        <td align="right" width="2em">
	        	<a href="${prefs['helpUrl'][0]}" target="_blank" class="uportal-button"><spring:message code="help" text="help"/></a>
	        </td>
        </c:if>
      </tr>
    </tbody>
  </table>
<div id="${n}tabs">
  <ul>
    <li><a href="#${n}tabs-1">Upcoming Sessions</a></li>
    <li><a href="#${n}tabs-2">Completed Sessions</a></li>
  </ul>
<div id="${n}tabs-1">
<portlet:actionURL portletMode="EDIT" var="deleteSessionActionUrl">
  <portlet:param name="action" value="deleteSessions" />
</portlet:actionURL>
<form name="deleteSessions" action="${deleteSessionActionUrl}" method="post">
	<c:choose>
    <c:when test="${fn:length(upcomingSessions) gt 0}">
      <table width="100%" id="sessionList">
        <thead>
          <tr class="uportal-channel-table-header">
            <th style="width: 1em;"><input id="${n}selectAllSessions" value="selectAllSessions" name="selectAllSessions" type="checkbox" /></th>
            <th><spring:message code="sessionName" text="sessionName"/></th>
            <th><spring:message code="startDateAndTime" text="startDateAndTime"/></th>
            <th><spring:message code="endDateAndTime" text="endDateAndTime"/></th>
            <th style="width: 20em;"><spring:message code="join" text="join"/></th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
      <table>
	      <tr  width="100%" >
		      <td align="left">
		            <spring:message code="deleteSession" var="deleteSession" text="deleteSession"/>
		            <spring:message code="areYouSureYouWantToDeleteSession" var="areYouSureYouWantToDeleteSession" text="areYouSureYouWantToDeleteSession"/>
		          <input id="dialog-confirm" value="${deleteSession}" name="Delete"
		            style="text-transform: none;" class="uportal-button"
		            onclick="javascript:return confirm('${areYouSureYouWantToDeleteSession}');"
		            type="submit" />
		            <c:if test="${!empty deleteSessionError}">
		                <span class="error">${deleteSessionError}</span>
		            </c:if>
		        </td>
	      </tr>
      </table>
    </c:when>
    <c:otherwise>
      <b>No sessions available</b>
    </c:otherwise>
  </c:choose>
</form>
</div>
<div id="${n}tabs-2">
<!-- completedSessionList -->
<form name="deleteSessions" action="${deleteSessionActionUrl}" method="post">
	<c:choose>
    <c:when test="${fn:length(completedSessions) gt 0}">
      <table width="100%" id="completedSessionList">
        <thead>
          <tr class="uportal-channel-table-header">
            <th style="width: 1em;"><input id="${n}selectAllSessions" value="selectAllSessions" name="selectAllSessions" type="checkbox" /></th>
            <th><spring:message code="sessionName" text="sessionName"/></th>
            <th><spring:message code="startDateAndTime" text="startDateAndTime"/></th>
            <th><spring:message code="endDateAndTime" text="endDateAndTime"/></th>
            <th style="width: 20em;"><spring:message code="join" text="join"/></th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
      <table>
	      <tr  width="100%" >
		      <td align="left">
		            <spring:message code="deleteSession" var="deleteSession" text="deleteSession"/>
		            <spring:message code="areYouSureYouWantToDeleteSession" var="areYouSureYouWantToDeleteSession" text="areYouSureYouWantToDeleteSession"/>
		          <input id="dialog-confirm" value="${deleteSession}" name="Delete"
		            style="text-transform: none;" class="uportal-button"
		            onclick="javascript:return confirm('${areYouSureYouWantToDeleteSession}');"
		            type="submit" />
		            <c:if test="${!empty deleteSessionError}">
		                <span class="error">${deleteSessionError}</span>
		            </c:if>
		        </td>
	      </tr>
      </table>
    </c:when>
    <c:otherwise>
      <b>No sessions available</b>
    </c:otherwise>
  </c:choose>
</form>
</div>
</div>
<script type="text/javascript">
<rs:compressJs>
//begin javascript
(function($) {
blackboardPortlet.jQuery(function() {
  var $ = blackboardPortlet.jQuery;
  
  $("#${n}tabs").tabs();
  
  var upcomingSessions = 
	  <json:array var="session" items="${upcomingSessions}" prettyPrint="true" escapeXml="false">
	    <json:array>
	    <portlet:renderURL var="viewSessionUrl">
	     <portlet:param name="sessionId" value="${session.sessionId}" />
	     <portlet:param name="action" value="viewSession" />
	    </portlet:renderURL>
	    <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED">
	     <portlet:param name="sessionId" value="${session.sessionId}" />
	     <portlet:param name="action" value="editSession" />
	    </portlet:renderURL>
	    
	    <json:property name="deleteCheckbox">
	    <sec:authorize access="hasPermission(#session, 'delete')">
	        <input value='${session.sessionId}' class='${n}deleteSession' name='deleteSession' type='checkbox' />
	    </sec:authorize>
	    </json:property>
	    <json:property name="sessionName">
	  	  <a href='${viewSessionUrl}'>${session.sessionName}</a>
	    </json:property>
	    <json:property name="startTime">
	      <joda:format value="${session.startTime}" pattern="MM/dd/yyyy HH:mm z" />
	    </json:property>
	    <json:property name="endTime">
	      <joda:format value="${session.endTime}" pattern="MM/dd/yyyy HH:mm z" />
	    </json:property>
	    <json:property name="join">
	     <c:choose>
	       <c:when test="${session.endTime.beforeNow}">
	         <spring:message code="sessionIsClosed" text="sessionIsClosed"/>
	       </c:when>
	       <c:otherwise>
	     	<c:choose>
	  	   <c:when test="${session.startTimeWithBoundaryTime.beforeNow}">
	  	      	<a href='${session.launchUrl}' target="_blank"><spring:message code="joinNow" text="joinNow"/></a>
	  	   </c:when>
	  	   <c:otherwise>
	  	    	${session.timeUntilJoin}
	  	   </c:otherwise>
	         </c:choose>
	       </c:otherwise>
	     </c:choose>
	    </json:property>
	    <json:property name="edit">
	     <sec:authorize access="hasPermission(#session, 'edit')">
	       <a href='${editSessionUrl}' class='uportal-button'><spring:message code="edit" text="edit"/></a>
	     </sec:authorize>
	    </json:property>
	    </json:array>
	  </json:array>
	  
	  var completedSessions = 
		  <json:array var="completedSessions" items="${completedSessions}" prettyPrint="true" escapeXml="false">
		    <json:array>
		    <portlet:renderURL var="viewSessionUrl">
		     <portlet:param name="sessionId" value="${completedSessions.sessionId}" />
		     <portlet:param name="action" value="viewSession" />
		    </portlet:renderURL>
		    <portlet:renderURL var="editSessionUrl" portletMode="EDIT" windowState="MAXIMIZED">
		     <portlet:param name="sessionId" value="${completedSessions.sessionId}" />
		     <portlet:param name="action" value="editSession" />
		    </portlet:renderURL>
		    
		    <json:property name="deleteCheckbox">
		    <sec:authorize access="hasPermission(#completedSessions, 'delete')">
		        <input value='${completedSessions.sessionId}' class='${n}deleteSession' name='deleteSession' type='checkbox' />
		    </sec:authorize>
		    </json:property>
		    <json:property name="sessionName">
		  	  <a href='${viewSessionUrl}'>${session.sessionName}</a>
		    </json:property>
		    <json:property name="startTime">
		      <joda:format value="${completedSessions.startTime}" pattern="MM/dd/yyyy HH:mm z" />
		    </json:property>
		    <json:property name="endTime">
		      <joda:format value="${completedSessions.endTime}" pattern="MM/dd/yyyy HH:mm z" />
		    </json:property>
		    <json:property name="join">
		     <c:choose>
		       <c:when test="${completedSessions.endTime.beforeNow}">
		         <spring:message code="sessionIsClosed" text="sessionIsClosed"/>
		       </c:when>
		       <c:otherwise>
		     	<c:choose>
		  	   <c:when test="${completedSessions.startTimeWithBoundaryTime.beforeNow}">
		  	      	<a href='${completedSessions.launchUrl}' target="_blank"><spring:message code="joinNow" text="joinNow"/></a>
		  	   </c:when>
		  	   <c:otherwise>
		  	    	${session.timeUntilJoin}
		  	   </c:otherwise>
		         </c:choose>
		       </c:otherwise>
		     </c:choose>
		    </json:property>
		    <json:property name="edit">
		     <sec:authorize access="hasPermission(#completedSessions, 'edit')">
		       <a href='${editSessionUrl}' class='uportal-button'><spring:message code="edit" text="edit"/></a>
		     </sec:authorize>
		    </json:property>
		    </json:array>
		  </json:array>

  $(document).ready(function() {
	  
	$('#${n}blackboardCollaboratePortlet .${n}deleteSession').click(function() {
	  if (!$(this).is(':checked')) {
		$('#${n}blackboardCollaboratePortlet #${n}selectAllSessions').attr('checked', false);
	  }
	  else if ($('#${n}blackboardCollaboratePortlet .${n}deleteSession').not(':checked').length == 0) {
		$('#${n}blackboardCollaboratePortlet #${n}selectAllSessions').attr('checked', true);
	  }
	});
       
    $('#${n}blackboardCollaboratePortlet #${n}selectAllSessions').click(function() {
      $('#${n}blackboardCollaboratePortlet .${n}deleteSession').attr('checked', $(this).is(':checked'));
    });
    
    var name = $( "#name" ),
    allFields = $( [] ).add( name ),
    tips = $( ".validateTips" );
    $( 'span .ui-icon-closethick').css('margin','0');
    
    function updateTips( t ) {
      tips
        .text( t )
        .addClass( "ui-state-highlight" )
        .css('color','red');
      setTimeout(function() {
        tips.removeClass( "ui-state-highlight", 1500 );
      }, 500 );
    }
    
    function checkLength( o, n, min, max ) {
      if ( o.val().length > max || o.val().length < min ) {
        o.addClass( "ui-state-error" );
        updateTips( "Length of " + n + " must be between " +
          min + " and " + max + "." );
        return false;
      } else {
        return true;
      }
    }
    
    $( "#${n}blackboardCollaboratePortlet #dialog-form" ).dialog({
        autoOpen: false,
        height: 200,
        width: 220,
        modal: true,
        buttons: {
          "Setup Session": function() {
            var bValid = true;
            allFields.removeClass( "ui-state-error" );
            bValid = bValid && checkLength( name, "name", 1, 256 );
            if ( bValid ) {
            	//submit form
            	$( "#createSession" ).submit();
            	$( this ).dialog( "close" );
            }
       },
       Cancel: function() {
            $( this ).dialog( "close" );
          }
       },closeOnEscape: false,
       open: function(event, ui) 
       { 
    	  $(".ui-dialog-titlebar-close").hide(); 
       },
       close: function()
       {
    	   allFields.val( "" ).removeClass( "ui-state-error" );
       }
      });
    $( "#${n}blackboardCollaboratePortlet #create-user" )
    .button()
    .click(function() {
      $( "#dialog-form" ).dialog( "open" );
    });
    
    $('#sessionList').dataTable( {
    		"aaData": upcomingSessions,
    		"aaSorting": [[3, "desc"]],
    		"bAutoWidth" : false,
    		"bDeferRender": true,
    		"aoColumns": [{ "bSortable": false },
    		              null,
    		              null,
    		              null,
    		              null,
    		              { "bSortable": false }
    		              ]
    		} );
    
  });
  
  $('#completedSessionList').dataTable( {
		"aaData": completedSessions,
		"aaSorting": [[3, "desc"]],
		"bAutoWidth" : false,
		"bDeferRender": true,
		"aoColumns": [{ "bSortable": false },
		              null,
		              null,
		              null,
		              null,
		              { "bSortable": false }
		              ]
	});
});
})(blackboardPortlet.jQuery);
</rs:compressJs>
</script>
</div>