<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<div class="alert alert-info" style='margin: 1em;'>
    <p>
        <spring:message code="instruction.format" text="<b>Format:</b> mm-dd-yyyy. IE: 09-19-1983" htmlEscape='false' /><br/><br/>
        <spring:message code="instruction.recordingdatafix" text="This job collects all recordings from start date to end date, then goes through to see if they exist in the local cache database. If it doesn't exist it creates the record."/>
    </p>
</div>
<div>
    <span class="uportal-channel-strong"><spring:message code="startDate" text="Start Date"/></span>
    <span><input id="startDate"></input></span>
</div>
<div>
    <span class="uportal-channel-strong"><spring:message code="endDate" text="End Date"/></span>
    <span><input id="endDate"></input></span>
</div>

<div id="recordingGoButtonDiv">
    <span><button id="recordingGo" type="button" class="btn btn-default">Go</button></span>
</div>

<portlet:resourceURL var="recordingDatafixAction" id="datafixRecording" escapeXml="false">
  <portlet:param name="startDate" value="STARTDATE" />
  <portlet:param name="endDate" value="ENDDATE" />
</portlet:resourceURL>

<script type="text/javascript">
(function($) {
    $(function() {
        //datepicker stuff
        $('#startDate').datepicker({dateFormat : 'mm-dd-yy'});
        $('#endDate').datepicker({dateFormat : 'mm-dd-yy'});
        
        //recording call
        var loadingDiv = "<span class='ui-autocomplete-loading' id='loading' style='padding-right: 2em;'>Running</span>";
        
        $( "#recordingGo" ).click(function(){
            
            //enable loading gif
            $("#recordingGo").prop('disabled',true);
            $("#recordingGoButtonDiv").append(loadingDiv);

            //set start/end dates
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            var theURL = '${recordingDatafixAction}';
            theURL = theURL.replace("STARTDATE",startDate);
            theURL = theURL.replace("ENDDATE",endDate);
            
            //call le function
            $.ajax({
                url: theURL,
                type: "POST",
                data: null,
                dataType: "json",
                async: true,
                success: function (request, text){
                    $("#recordingGo").prop('disabled',false);
                    $('#loading').remove();
                    $("#recordingGoButtonDiv").append('<span><div class="alert alert-success alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>Datafix completed successfully.</div></span>');
                },
                error: function(request, text, error) {
                    //Leaving the Go button disabled on purpose.
                    //$("#recordingGo").prop('disabled',false);
                    $('#loading').remove();
                    $("#recordingGoButtonDiv").append('<span><div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>Datafix failed, see logs for more details. This may have happened due to a large dataset. Please review logs before running again.</div></span>');
                }
            });
        });
    });
})(blackboardPortlet.jQuery);
</script>
