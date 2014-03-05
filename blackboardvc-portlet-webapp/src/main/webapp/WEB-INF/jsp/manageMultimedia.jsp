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

<div class="error">
<span class="uportal-channel-strong">${multimediaUploadError}</span>
</div>
<table class="sessionName">
  <thead>
    <tr>
        <td style="text-align: left;"><span class="uportal-channel-strong"><spring:message code="sessionName"/></span></td>
        <td style="text-align: right;"><a href="${viewSessionUrl}">${session.sessionName}</a></td>
    </tr>
  </thead>
</table>

<table class="mediaFileList">
  <thead>
    <tr>
      <th colspan="4" style="text-align: left;">
        <h2>Upload / Edit Media Files</h2><br/>
        <small>You may upload most common video formats, including .mpeg, .mpg, .mpe, .mov, .qt, .swf, .m4v, .mp3, .mp4, .wmv.</small>
      </th>
    </tr>
    <tr>
      <td style="text-align: left;"><span class="uportal-channel-strong">Select</span></td><td style="text-align: left;"><span class="uportal-channel-strong">Media Files</span></td>
    </tr>
  </thead>
  <tbody>
  </tbody>
  <tfoot>
    <tr>
      <td></td>
      <td>
        <portlet:actionURL portletMode="EDIT" var="uploadMediaFileActionUrl" name="uploadMediaFile">
          <portlet:param name="sessionId" value="${session.sessionId}" />
        </portlet:actionURL>
        <form action="${uploadMediaFileActionUrl}" method="post" enctype="multipart/form-data">
          <div class="ajaxerror file"></div>
          <div>
            <input name="multimediaUpload" size="40" type="file" accept="${multimediaFileTypes}"> <button name="uploadFile" type="submit">Upload File</button>
          </div>
          <div>Accepted file formats: .mpeg, .mpg, .mpe, .mov, .qt, .swf, .m4v, .mp3, .mp4, mpeg, .wmv</div>
        </form>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="left"><button name="deleteSelected">Delete Selected</button></td>
    </tr>
    <tr>
      <td colspan="2" align="left"><a href="${viewSessionUrl}" class="uportal-button">&lt; Back</a></td>
    </tr>
  </tfoot>
</table>

<portlet:resourceURL var="getMediaFilesUrl" id="getMediaFiles" escapeXml="false" />
<portlet:resourceURL var="deleteMediaFileUrl" id="deleteMediaFile" escapeXml="false" />

<script id="${n}mediFileTemplate" type="text/template">
  <td><input name="file_select" type="checkbox" value="{{= cid }}"/></td>
  <td>{{=name}}</td>
</script>

<script type="text/javascript">
(function($, Backbone, _) {
   'use strict';

   blackboardPortlet.initMediaFileBackbone({
      getMediaFilesUrl: "${getMediaFilesUrl}",
      deleteMediaFileUrl: "${deleteMediaFileUrl}",
      sessionId : '${session.sessionId}',
      mediaFileTemplateSelector: '#${n}mediFileTemplate',
      mediaFilesViewSelector: "div#${n}blackboardCollaboratePortlet table.mediaFileList"
   });

	$(document).ready(function() {
		blackboardPortlet.showTooltip('.${n}toolTip');
	});
})(blackboardPortlet.jQuery);

</script>
</div>
