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

<portlet:actionURL portletMode="EDIT" var="deleteRecordingActionUrl">
  <portlet:param name="action" value="deleteRecordings" />
</portlet:actionURL>
  <c:choose>
    <c:when test="${fn:length(recordings) gt 0}">
        <form name="deleteRecordings" action="${deleteRecordingActionUrl}" method="post">
        <table width="100%">
            <tbody>
            <tr>
                <td align="left">
                </td>
                <td align="right">
                    <spring:message code="deleteRecording" var="deleteRecording" text="deleteRecording"/>
                    <spring:message code="areYouSureYouWantToDeleteRecording" var="areYouSureYouWantToDeleteRecording" text="areYouSureYouWantToDeleteRecording"/>
                    <input id="dialog-confirm" value="${deleteRecording}" name="Delete"
                           style="text-transform: none;" class="uportal-button"
                           onclick="javascript:return confirm('${areYouSureYouWantToDeleteRecording}');"
                           type="submit" />
                </td>
            </tr>
            </tbody>
        </table>
        <table width="100%" id="recordingList">
        <thead>
          <tr class="uportal-channel-table-header">
            <th width="15"><input id="${n}selectAllRecordings" value="selectAllRecordings" name="Recordings" type="checkbox" /></th>
            <th><spring:message code="previouslyRecorded" text="previouslyRecorded"/></th>
            <th><spring:message code="startTime" text="startTime"/></th>
            <th><spring:message code="size" text="size"/></th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
     </form>
    </c:when>
    <c:otherwise>
      <b>No recordings available</b>
    </c:otherwise>
  </c:choose>

<script type="text/javascript">
<rs:compressJs>
(function($) {
blackboardPortlet.jQuery(function() {
  var $ = blackboardPortlet.jQuery;
  
  var recordings = 
	  <json:array var="recording" items="${recordings}" prettyPrint="true" escapeXml="false">
	    <json:array>
	    <portlet:renderURL var="editRecordingUrl" portletMode="EDIT" windowState="MAXIMIZED">
	      <portlet:param name="recordingId" value="${recording.recordingId}" />
	      <portlet:param name="action" value="editRecording" />
        </portlet:renderURL>
        <json:property name="deleteCheckbox">
          <sec:authorize access="hasPermission(#recording, 'delete')">
            <input value="${recording.recordingId}" class="${n}deleteRecording" name="deleteRecording" type="checkbox" />
          </sec:authorize>
        </json:property>
        <json:property name="recordingName">
          <a href="${recording.recordingUrl}" target="_blank">${recording.roomName}</a>
          </json:property>
        <json:property name="creationDate">
          <joda:format value="${recording.creationDate}" pattern="MM/dd/yyyy HH:mm z" />
        </json:property>
        <json:property name="recordingSize">
          ${recording.displayRecordingSize}
        </json:property>
        <json:property name="editLink">
          <sec:authorize access="hasPermission(#recording, 'edit')">
            <spring:message code="edit" var="edit" text="edit"/>
            <a href="${editRecordingUrl}" class="uportal-button">${edit}</a>
          </sec:authorize>
        </json:property>
	    </json:array>
	  </json:array>

  $(document).ready(function() {
    $('.${n}deleteRecording').click(function() {
      if (!$(this).is(':checked')) {
        $('#${n}selectAllRecordings').attr('checked', false);
      }
      else if ($('.${n}deleteRecording').not(':checked').length == 0) {
        $('#${n}selectAllRecordings').attr('checked', true);
      }
    });
       
    $('#${n}selectAllRecordings').click(function() {
      $('.${n}deleteRecording').attr('checked', $(this).is(':checked'));
    });
    
    $('#recordingList').dataTable( {
		"aaData": recordings,
		"aaSorting": [[2, "desc"]],
		"bAutoWidth" : false,
		"aoColumns": [{ "bSortable": false },
		              null,
		              null,
		              null,
		              { "bSortable": false }
		              ]
		} );
    
  });
});
})(blackboardPortlet.jQuery);
</rs:compressJs>
</script>
