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

    #div-filter {
        width: 45%;
        //display: none;
    }

    #div-filter > button {
        float: right;
        clear: both;
        margin-bottom: 10px;
    }

    #div-filter > #button-filter {
        float: right;
        clear: both;
        margin-bottom: 10px;
    }

    table#button-filter tr td {
        padding:3px;
    }

    #log-table tbody tr td.td-number {
		text-align: center;
		width: 15px;
	}

	#log-table tbody tr td.td-time {
		text-align: center;
	}

    .entries {
        float: right;
    }
    
   input.input-tanggal-kirim {
   		cursor: pointer !important;
   }
</style>

<h4>Send Email Log</h4>
<br />

<button type="button" class="btn btn-default filter-btn">Filter</button>
<div id="div-filter">
    <table class="div-filter-table">
    		<tr style="display:none;">
            <td>Subject</td>
            <td colspan="2"><input type="text" class="form-control input-subject" value="" maxlength="200" /></td>
        </tr>
        <tr>
            <td>Mail To</td>
            <td colspan="2"><input type="text" class="form-control input-mail-to" value="" maxlength="200" /></td>
        </tr>
        <tr>
            <td style="width:100px; valign:middle;">Tanggal Kirim</td>
            <td>From : <input class="form-control input-tanggal-kirim tanggal-from" value="" placeholder="dd/mm/yyyy" type="text" readonly/></td>
            <td>To : <input class="form-control input-tanggal-kirim tanggal-to" value="" placeholder="dd/mm/yyyy" type="text" readonly/></td>
        </tr>
        <tr>
            <td>Status</td>
            <td colspan="1">
                <select class="form-control input-status">
                    <!--Dropdown Status-->
                    <option value="${defaultStatus}" selected>Semua Status</option>
                    <option value="${sentStatus}">Terkirim</option>
                    <option value="${failedStatus}">Gagal Kirim</option>
                    <option value="${queueStatus}">Queue</option>
                    <option value="${processingStatus}">Processing</option>
                    <option value="${readStatus}">Dibaca</option>
                </select>
            </td>
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
	<table id="log-table" class="display" width="100%" cellspacing="0">
	    <thead>
	        <tr>
	            <th style="width:10px;">No.</th>
	            <th>CIF Key</th>
	            <th>Account Number/CC Number</th>
	            <th>Nama Nasabah</th>
	            <th>Email</th>
	            <th style="text-align:center; width:140px;">Datetime Perubahan Status</th>
	            <th>Status</th>
	        </tr>
	    </thead>
	</table>
</div>

<script type="text/javascript">
    var logTable;

	$(document).ready(function(){
        document.title = "Send Email Log";

        setDivFilter();
        setDatePicker();
        setDataTable();
        $('.form-control').keypress(function(e){
			var key = e.which;
			if( key == 13 ){
				reloadTable();
				return false;
			}
		});
    });
	
    function setDataTable(){
        logTable =  $('#log-table').DataTable( {
            "processing": true,
            "serverSide": true,
            "searching" : false,
            "lengthChange" : true,
            //"pageLength" : 5,
            "ajax" : {
                "url" : "<c:url value='/send-email/get-log-list' />",
                "data" : function(d){
                    d.subject = $(".input-subject").val();
                    d.mailTo = $(".input-mail-to").val();
                    d.tglKirimFrom = $(".tanggal-from").val();
                    d.tglKirimTo = $(".tanggal-to").val();
                    d.status = $(".input-status").val();
                },
                "dataSrc" : function (json) {
                    for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
                    	/*json.data[i].mailTo = json.data[i].mailTo.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;')*/
                    	json.data[i].mailTo = json.data[i].mailTo.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
                    	json.data[i].number = json.data[i].number+'.';
                        json.data[i].cifKey = json.data[i].cifKey!=null ? json.data[i].cifKey : "N/A";
                        
                        var accNo = json.data[i].accountNumber!=null ? json.data[i].accountNumber : "N/A";
                        if( accNo.length == 19 )
                        	accNo = accNo.substring(7);
                        
                        json.data[i].accountNumber = accNo;
                    }
                    return json.data;
                }
            },
            "columns": [
                { "data": "number"},
                { "data": "cifKey" },
                { "data": "accountNumber"},
                { "data": "namaRekening" },
                { "data": "mailTo" },
                { "data": "time" },
                { "data": "status" }
            ],
            "columnDefs": [
                { className: "td-number", "targets": [ 0 ] },
                { className: "td-time", "targets": [ 5 ] },
                { orderable: false, "targets": [0, 1, 2, 3, 4, 5, 6] }
            ],
            "lengthMenu": [${strDefRowCount}],
            "dom": '<"entries"l>rt<"bottom"ifp><"clear">'
        });
    }

    function reloadTable(){
    		var atDateFromStr = $("input.tanggal-from").val();
	    	var atDateToStr = $("input.tanggal-to").val();
	    	var atDateFrom = $("input.tanggal-from").datepicker("getDate");
	    	var atDateTo = $("input.tanggal-to").datepicker("getDate");
	    	if(atDateFromStr!="" && atDateToStr!=""){
	    		if(atDateTo<atDateFrom){
	        		alert("Tanggal mulai harus lebih lama dari tanggal selesai!")
	        		return;
	        	}
	    	}
        logTable.ajax.reload();
    }

    function clearFilter(){
        $(".input-subject").val("");
        $(".input-mail-to").val("");
        $(".input-tanggal-kirim").val("");
        $.each($(".input-status").children(), function(){
            if($(this).val()==${defaultStatus}){
                $(this).prop("selected", true);
            }
        });

        reloadTable();
    }

    function setDivFilter(){
        $(".filter-btn").click(function (){
            $("#div-filter").slideToggle(300);
        });
    }

    function setDatePicker(){
        var date_input=$('.input-tanggal-kirim');

        date_input.datepicker({
            format: 'dd/mm/yyyy',
            startView: "days",
            minViewMode: "days",
            autoclose: true,
            locale: "id"
        });
    }
</script>