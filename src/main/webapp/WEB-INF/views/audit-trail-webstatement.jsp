<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="<c:url value="/resources/plugin/datatable/jquery.dataTables.min.css" />" rel="stylesheet"/>
<script src=<c:url value="/resources/plugin/datatable/jquery.dataTables.min.js"/> ></script>
<script src="<c:url value='/resources/js/bootstrap-datepicker.js'/>" ></script>
<link href='<c:url value="/resources/css/datepicker.css" />' rel="stylesheet" />

<style>
	table.div-filter-table {
        width: 100%;
    }

    table.div-filter-table tr td{
        padding: 5px;
        vertical-align: middle;
    }
	#audit-trail-table tbody tr td.td-number {
		text-align: center;
		width: 15px;
	}

	#audit-trail-table tbody tr td.td-time {
		text-align: center;
		width: auto;
	}
	
	#audit-trail-table tbody tr td.td-req-param {
		text-align: left;
		width: 200px !important;
	}

	#div-filter {
        width: 55%;
    }

    #div-filter > #button-filter {
    	float: right;
    }

    table#button-filter tr td {
    	padding:3px;
    }

	.entries {
        float: right;
    }
    
    input.at-date {
    		cursor: pointer !important;
    }
    
    
</style>

<h4>Audit Trail Web Statement</h4>
<br />

<button type="button" class="btn btn-default filter-btn">Filter</button>
<div id="div-filter">
    <table class="div-filter-table">
        <tr>
            <td style="padding-top:10px;">User ID</td>
            <td style="padding-top:10px;"><input class="form-control username" value="" type="text" /></td>
        </tr>
        <tr>
            <td>Date </td>
            <td>From : <input type="text" class="form-control at-date at-date-from" value="" placeholder="dd/MM/yyyy" readonly/></td>
            <td>To : <input type="text" class="form-control at-date at-date-to" value="" placeholder="dd/MM/yyyy" readonly /></td>
        </tr>
        <tr style="display:none;">
            <td style="padding-top:10px;">URL Path</td>
            <td style="padding-top:10px;"><input class="form-control url-path" value="" type="text" /></td>
        </tr>
        <tr style="display:none;">
            <td style="padding-top:10px;">Request Parameter</td>
            <td style="padding-top:10px;"><input class="form-control reqParam" value="" type="text" /></td>
        </tr>
        <tr style="display:none;">
            <td style="padding-top:10px;">Information</td>
            <td style="padding-top:10px;"><input class="form-control information" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">CIF Key</td>
            <td style="padding-top:10px;"><input class="form-control cifKey" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Activity</td>
            <td style="padding-top:10px;"><input class="form-control activity" value="" type="text" /></td>
        </tr>
    </table>

    <table id="button-filter">
    	<tr>
    		<td><button type="button" class="btn btn-primary btn-sm" onclick="reloadTable()">Search</button></td>
    		<td><button type="button" class="btn btn-primary btn-sm" onclick="clearFilter()">Clear Filter</button></td>
    	</tr>
    </table>
</div>

<div class="table-responsive" style="width:100%;">
	<table id="audit-trail-table" class="display" width="100%" cellspacing="0">
	    <thead>
	        <tr>
	            <th style="width:10px;">No.</th>
	            <th style="text-align:left; width:50px;">User ID</th>
	            <th style="text-align:left; width:150px;">CIF Key</th>
	            <th style="text-align:center; width:140px;">Time</th>
	            <th style="text-align:left; width:200px;">Activity</th>
	            <th style="text-align:left;">URL Path</th>
	            <th style="text-align:left; width:200px;">Request Parameter</th>
	            <!--  <th style="text-align:left;">Information</th> -->
	            <th style="text-align:left;">IP Address</th>
	            <th style="text-align:left;">Browser</th>
	        </tr>
	    </thead>
	</table>
</div>

<script>
	var requestTable;

	$(document).ready(function() {
		document.title = "Audit Trail Web Statement";

		setDataTable();
		setDivFilter();
		setDatePicker();
		$('.form-control').keypress(function(e){
			var key = e.which;
			if( key == 13 ){
				reloadTable();
				return false;
			}
		});
	} );

	function setDataTable(){
		requestTable = $('#audit-trail-table').DataTable( {
	        "processing": true,
	        "serverSide": true,
	        "searching" : false,
	        "lengthChange" : true,
	        //"pageLength" : 15,
	        "ajax" : {
	        	"url" : "<c:url value='/audit-trail/get-all-webstatement' />",
	        	"data" : function(d){
	        		d.username = $("input.username").val();
	        		d.atDateFrom = $("input.at-date-from").val();
	        		d.atDateTo = $("input.at-date-to").val();
	        		d.reqParam = $("input.reqParam").val();
	        		d.information = $("input.information").val();
	        		d.urlPath = $("input.url-path").val();
	        		d.cifKey = $("input.cifKey").val();
	        		d.activity = $("input.activity").val();
	        	},
	        	"dataSrc" : function (json) {
			      	for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
				        json.data[i].number = json.data[i].number+'.';
				        json.data[i].reqParam = json.data[i].reqParam!=null ? json.data[i].reqParam.replace(/\n/g,"<br />") : "-";
				        json.data[i].reqParam = json.data[i].reqParam!=null ? json.data[i].reqParam.replace("|","") : "-";
				         json.data[i].reqParam = json.data[i].reqParam!=null ? json.data[i].reqParam.replace(",|","<br />") : "-";
				    }
				    return json.data;
				}
	        },
	        "columns": [
	        	{ "data": "number" },
	            { "data": "username" },
	            { "data": "cifKey"},
	            { "data": "time" },
	            { "data": "activity"},
	            { "data": "urlPath" },
	            { "data": "reqParam"},
	            //{ "data": "information"},
	            { "data": "ipAddress"},
	            { "data": "browser"},
	        ],
	        "columnDefs": [
			    { className: "td-number", "targets": [ 0 ] },
			    { className: "td-time", "targets": [ 3 ] },
			    { className: "td-req-param", "targets": [ 6 ] },
			    { orderable: false, "targets": [0, 1, 2, 3, 4, 5, 6, 7, 8] }
			],
			"lengthMenu": [${strDefRowCount}],
			"dom": '<"entries"l>rt<"bottom"ifp><"clear">'
	    });
	}

	function reloadTable(){
		var atDateFromStr = $("input.at-date-from").val();
    	var atDateToStr = $("input.at-date-to").val();
    	var atDateFrom = $("input.at-date-from").datepicker("getDate");
    	var atDateTo = $("input.at-date-to").datepicker("getDate");
    	if(atDateFromStr!="" && atDateToStr!=""){
    		if(atDateTo<atDateFrom){
        		alert("Tanggal mulai harus lebih lama dari tanggal selesai!")
        		return;
        	}
    	}
    	requestTable.ajax.reload();
    }

    function clearFilter(){
		$("input.username").val("");
		$("input.at-date-from").val("");
		$("input.at-date-to").val("");
		$("input.url-path").val("");
		$("input.reqParam").val("");
		$("input.information").val("");
		$("input.cifKey").val("");
		$("input.activity").val("");
		reloadTable();
	}

    function setDivFilter(){
        $(".filter-btn").click(function (){
            $("#div-filter").slideToggle(200);
        });
    }

    function setDatePicker(){
    	var reqDate=$('input.at-date');

        reqDate.datepicker({
            format: 'dd/mm/yyyy',
            startView: "days",
            minViewMode: "days",
            autoclose: true,
            locale: "id"
        });
    }
</script>