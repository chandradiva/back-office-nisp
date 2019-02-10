<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="<c:url value="/resources/plugin/datatable/jquery.dataTables.min.css" />" rel="stylesheet"/>
<script src=<c:url value="/resources/plugin/datatable/jquery.dataTables.min.js"/> ></script>

<style>
	table.div-filter-table {
        width: 100%;
    }

    table.div-filter-table tr td{
        padding: 5px;
        vertical-align: middle;
    }

    #div-filter {
        width: 55%;
    }

    #div-filter > button {
        float: right;
        clear: both;
        margin-bottom: 10px;
    }

    table#block-hc-table thead th {
    	padding-left: 10px;
    	text-align: left;
    }

    table#block-hc-table tbody tr td {
    	text-align: left;
    }

    table#block-hc-table tbody tr td.td-center {
    	text-align: center;
    }

    .entries {
        float: right;
    }
    
    #div-input-passwd table tr td {
    	padding:5px 0px 5px 5px;
    }

    table#button-filter {
        float: right;
        clear: both;
        margin-bottom: 10px;
    }

    table#button-filter tr td {
        padding:3px;
    }
</style>

<h4>Block / Unblock Hardcopy</h4>
<br />

<button type="button" class="btn btn-default filter-btn">Filter</button>
<div id="div-filter">
    <table class="div-filter-table">
        <tr>
            <td style="padding-top:10px;">Name</td>
            <td style="padding-top:10px;"><input id="account-name" class="form-control" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Account Number </td>
            <td style="padding-top:10px;"><input id="account-number" class="form-control" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Status</td>
            <td style="padding-top:10px;">
                <select id="status" class="form-control">
                    <option value="${allStatus}" selected>All Status</option>
                    <option value="${blockedStatus}">Blocked</option>
                    <option value="${unblockedStatus}">Unblocked</option>
                </select>
            </td>
        </tr>
    </table>

    <table id="button-filter">
    <tr>
        <td>
            <button type="button" class="btn btn-primary btn-sm" onclick="reloadTable()">Search</button>
        </td>
        <td><button type="button" class="btn btn-primary btn-sm" onclick="clearFilter()">Clear Filter</button></td>
    </tr>
    </table>
</div>

<div class="table-responsive" style="width:100%;">
	<table id="block-hc-table" class="display" width="100%" cellspacing="0">
	    <thead>
	        <tr>
	            <th style="width:10px;"></th>
	            <th>Name</th>
	            <th>Account Number</th>
	            <th style="width:100px;">Status</th>
	        </tr>
	    </thead>
	</table>

    <div id="div-button-action" style="float: right; margin-top: 15px;">
        <button type="button" class="btn btn-primary btn-sm" onclick="changeStatus(${blockedStatus})">Block</button>
        <button type="button" class="btn btn-primary btn-sm" onclick="changeStatus(${unblockedStatus})">Unblock</button>
    </div>
</div>



<script>
	var blockHcTable;

	$(document).ready(function(){
		document.title = "Block / Unblock Hardcopy";

        setDataTable();
        setDivFilter();
        $('.form-control').keypress(function(e){
			var key = e.which;
			if( key == 13 ){
				reloadTable();
				return false;
			}
		});
	});

	function setDivFilter(){
        $(".filter-btn").click(function (){
            $("#div-filter").slideToggle(200);
        });
    }

    function setDataTable(){
    	blockHcTable =	$('#block-hc-table').DataTable( {
	        "processing": true,
	        "serverSide": true,
	        "searching" : false,
	        "lengthChange" : true,
	        "ajax" : {
	        	"url" : "<c:url value='/block-hardcopy/get-list' />",
	        	"data" : function(d){
	        		d.name = $("input#account-name").val();
	        		d.accountNumber = $("input#account-number").val();
                    d.status = $("select#status").val();
	        	},
	        	"dataSrc" : function (json) {
			      	for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
				        json.data[i].checkBox = '<input type="hidden" value="' + json.data[i].id + '" /><input type="checkbox" class="block-hc-checkbox" />';
				        var status = json.data[i].status;

				        if(status==${blockedStatus})
				        	json.data[i].status = "Blocked";
				        else if(status==${unblockedStatus})
				        	json.data[i].status = "Unblocked";
				    }
				    return json.data;
				}
	        },
	        "columns": [
	        	{ "data": "checkBox"},
	            { "data": "name" },
	            { "data": "accountNumber" },
	            { "data": "status" }
	        ],
	        "columnDefs": [
			    { className: "td-center", "targets": [ 0 ] },
                { orderable: false, "targets": [0, 1, 2, 3] }
			],
			"lengthMenu": [${strDefRowCount}],
            "dom": '<"entries"l>rt<"bottom"ifp><"clear">'
   		});
    }

    function reloadTable(){
    	blockHcTable.ajax.reload();
    }

    function clearFilter(){
        $("input#account-name").val("");
        $("input#account-number").val("");
        $.each($("select#status").children(), function(){
            if($(this).val()==${allStatus}){
                $(this).prop("selected", true);
                return false;   //break;
            }
        });
        reloadTable();
    }
    
    function changeStatus($status){
    	var blockHcs = [];

    	$.each($("input.block-hc-checkbox"), function(){
           if($(this).is(":checked")){
               var val = parseInt($(this).prev().val());
               var blockHc = new Object();
               blockHc.id = val;
               blockHc.status = $status;
               blockHcs.push(blockHc);
           }
        });

        if(blockHcs.length>0){
        	var commit;

            if($status=='${blockedStatus}'){
                commit = confirm("Apakah anda yakin akan memblokir ini??")
            } else {
                commit = confirm("Apakah anda yakin akan meng-unblock ini??")
            }

        	if(commit){
        		var ajax = $.ajax({
                        url: "<c:url value='/block-hardcopy/change-status' />",
                        timeout : 30000,
                        type : 'PUT',
                        dataType: "json",
                        contentType: "application/json",
                        data : JSON.stringify(blockHcs)
                    });
                    ajax.done(function (response) {
                        if (response.resultCode == 1000) {
                            reloadTable();
                            alert(response.message);
                        }
                        else if(response.resultCode == 4000){
                            alert(response.message);
                            window.location.replace('<c:url value="login" />');
                        }
                        else {
                        	console.log(response)
                        }
                    });
                    ajax.fail(function(xhr,status){
                        alert('Failed: '+status);
                    });
        	}
        }
    }
</script>