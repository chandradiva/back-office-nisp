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

	#div-filter {
        width: 55%;
    }

    #div-filter > button {
        float: right;
        clear: both;
        margin-bottom: 10px;
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

<h4>Audit Trail Back Office</h4>
<br />

<button type="button" class="btn btn-default filter-btn">Filter</button>
<div id="div-filter">
    <table class="div-filter-table">
        <tr>
            <td style="padding-top:10px;">User ID </td>
            <td style="padding-top:10px;"><input class="form-control username" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Group </td>
            <td style="padding-top:10px;"><input class="form-control group" value="" type="text" /></td>
        </tr>
        <tr>
            <td>Date </td>
            <td>From : <input type="text" class="form-control at-date at-date-from" value="" placeholder="dd/MM/yyyy" readonly/></td>
            <td>To : <input type="text" class="form-control at-date at-date-to" value="" placeholder="dd/MM/yyyy" readonly/></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Activity</td>
            <td style="padding-top:10px;"><input class="form-control action" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Information</td>
            <td style="padding-top:10px;"><input class="form-control information" value="" type="text" /></td>
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
	            <th style="text-align:left;">Group</th>
	            <th style="text-align:center; width:140px;">DateTime</th>
	            <th style="text-align:left; width:200px;">Activity</th>
	            <th style="text-align:left;">Information</th>
	        </tr>
	    </thead>
	</table>
</div>

<script>
	var requestTable;


	$(document).ready(function() {
		document.title = "Audit Trail Back Office";

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
	});
	
	var entityMap = {
		'&': '&amp;',
		'<': '&lt;',
		'>': '&gt;',
		'"': '&quot;',
		"'": '&#39;',
		'/': '&#x2F;',
		'`': '&#x60;',
		'=': '&#x3D;',
	};
	
	function escapeHtml(str) {
		return String(str).replace(/[&<>"'`=\/]/g, function (s) {
			return entityMap[s];
		});
	}

	function setDataTable(){
		requestTable = $('#audit-trail-table').DataTable( {
	        "processing": true,
	        "serverSide": true,
	        "searching" : false,
	        "lengthChange" : true,
	        //"pageLength" : 15,
	        "ajax" : {
	        	"url" : "<c:url value='/audit-trail/get-all' />",
	        	"data" : function(d){
	        		d.username = $("input.username").val();
	        		d.groups = $("input.group").val();
	        		d.atDateFrom = $("input.at-date-from").val();
	        		d.atDateTo = $("input.at-date-to").val();
	        		d.action = $("input.action").val();
	        		d.information = $("input.information").val();
	        	},
	        	"dataSrc" : function (json) {
			      	for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
				        json.data[i].number = json.data[i].number+'.';
				        json.data[i].info = json.data[i].info!=null ? json.data[i].info.replace(/\n/g,"<br />") : "-";
				    }
				    return json.data;
				}
	        },
	        "columns": [
	        	{ "data": "number" },
	            { "data": "username" },
	            { "data": "groups" },
	            { "data": "time" },
	            { "data": "action" },
	            { "data": "info"}
	        ],
	        "columnDefs": [
			    { className: "td-number", "targets": [ 0 ] },
			    { className: "td-time", "targets": [ 3 ] },
			    { orderable: false, "targets": [0, 1, 2, 3, 4] }
			],
			"lengthMenu": [${strDefRowCount}],
			"dom": '<"entries"l>rt<"bottom"ifp><"clear">'
	    });
	}

	function clearFilter(){
		$("input.username").val("");
		$("input.at-date-from").val("");
		$("input.at-date-to").val("");
		$("input.action").val("");
		$("input.information").val("");
		$("input.group").val("");				
		reloadTable();
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