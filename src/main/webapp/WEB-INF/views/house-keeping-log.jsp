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
    
   input.input-tanggal {
   		cursor: pointer !important;
   }
</style>

<h4>House Keeping Log</h4>
<br />

<button type="button" class="btn btn-default filter-btn">Filter</button>
<div id="div-filter">
    <table class="div-filter-table">
    	<tr>
            <td>Proses</td>
            <td colspan="2"><input id="input-proses" type="text" class="form-control" value="" maxlength="200" /></td>
        </tr>
        <tr>
            <td style="width:100px; valign:middle;">Jangka Waktu</td>
            <td>Tanggal Mulai : <input id="tanggal-from" class="form-control input-tanggal" value="" placeholder="dd/mm/yyyy" type="text" readonly/></td>
            <td>Tanggal Selesai : <input id="tanggal-to" class="form-control input-tanggal" value="" placeholder="dd/mm/yyyy" type="text" readonly/></td>
        </tr>
        <tr>
            <td>Status</td>
            <td colspan="1">
                <select id="input-status" class="form-control">
                    <!--Dropdown Status-->
                    <option value="-1" selected>Semua Status</option>
                    <option value="1">Sukses</option>
                    <option value="0">Gagal</option>                    
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
	            <th>Proses</th>
	            <th style="text-align:center; width:140px;">Waktu Mulai</th>
	            <th style="text-align:center; width:140px;">Waktu Selesai</th>
	            <th>Status</th>
	        </tr>
	    </thead>
	</table>
</div>

<script type="text/javascript">
    var logTable;

	$(document).ready(function(){
        document.title = "House Keeping Log";

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
                "url" : "<c:url value='/house-keeping/get-log-list' />",
                "data" : function(d){
                    d.proses = $("#input-proses").val();
                    d.strWaktuMulai = $("#tanggal-from").val();
                    d.strWaktuSelesai = $("#tanggal-to").val();
                    d.status = $("#input-status").val();
                },
                "dataSrc" : function (json) {
                    for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
                        json.data[i].number = json.data[i].number+'.';                                               
                    }
                    return json.data;
                }
            },
            "columns": [
                { "data": "number"},
                { "data": "proses" },
                { "data": "waktuMulai" },
                { "data": "waktuSelesai" },
                { "data": "status" }
            ],
            "columnDefs": [
                { className: "td-number", "targets": [ 0 ] },
                { orderable: false, "targets": [0, 1, 2, 3, 4] }
            ],
            "lengthMenu": [${strDefRowCount}],
            "dom": '<"entries"l>rt<"bottom"ifp><"clear">'
        });
    }

    function reloadTable(){
    		var atDateFromStr = $("#tanggal-from").val();
	    	var atDateToStr = $("#tanggal-to").val();
	    	var atDateFrom = $("#tanggal-from").datepicker("getDate");
	    	var atDateTo = $("#tanggal-to").datepicker("getDate");
	    	if(atDateFromStr!="" && atDateToStr!=""){
	    		if(atDateTo<atDateFrom){
	        		alert("Tanggal mulai harus sebelum atau sama dengan tanggal selesai!")
	        		return;
	        	}
	    	}
        logTable.ajax.reload();
    }

    function clearFilter(){
        $("#input-proses").val("");
        $(".input-tanggal").val("");
        $.each($("#input-status").children(), function(){
        	console.log($(this).val());
            if($(this).val()==-1){            	
                $(this).prop("selected", true);
            }else{
            	$(this).prop("selected", false);
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
        var date_input=$('.input-tanggal');

        date_input.datepicker({
            format: 'dd/mm/yyyy',
            startView: "days",
            minViewMode: "days",
            autoclose: true,
            locale: "id"
        });
    }
</script>