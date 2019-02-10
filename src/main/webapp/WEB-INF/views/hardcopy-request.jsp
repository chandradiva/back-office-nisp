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
        width: 55%;
        //display: none;
    }

    #div-filter > button {
        float: right;
        clear: both;
        margin-bottom: 10px;
    }

    table#req-table thead th {
    	padding-left: 10px;
    	text-align: left;
    }

    table#req-table tbody tr td {
    	text-align: left;
    }

    table#req-table tbody tr td.td-center {
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
    
    input.req-date, input.periode-date {
    		cursor:pointer !important;
    }
</style>

<h4>Statement Hardcopy Request</h4>
<br />

<button type="button" class="btn btn-default filter-btn">Filter</button>
<div id="div-filter">
    <table class="div-filter-table">
        <tr>
            <td>Date Request </td>
            <td>From : <input type="text" class="form-control req-date req-from" value="${defaultReqFrom}" placeholder="dd/MM/yyyy" readonly /></td>
            <td>To : <input type="text" class="form-control req-date req-to" value="${defaultReqTo}" placeholder="dd/MM/yyyy" readonly /></td>
        </tr>
        <tr>
            <td>Period</td>
            <td>From : <input type="text" class="form-control periode-date periode-from" value="${defaultPeriodeFrom}" placeholder="MMM yyyy" readonly /></td>
            <td>To : <input type="text" class="form-control periode-date periode-to" value="${defaultPeriodeTo}" placeholder="MMM yyyy" readonly /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Account Number </td>
            <td style="padding-top:10px;"><input class="form-control account-number" value="${defaultAccountNumber}" type="text" /></td>
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
	<table id="req-table" class="display" width="100%" cellspacing="0">
	    <thead>
	        <tr>
	            <th style="width:20px; padding: 0px; text-align:center;" class="check-column">
	            	<input type="checkbox" id="all-check" />
	            </th>
	            <th style="width:130px;">Date/Time Requested</th>
	            <th style="width:85px;">Period</th>
	            <th style="width:130px;">Account Number</th>
	            <th style="width:100px; text-align:center;">Currency Code</th>
	            <th>Mailing Address</th>
	            <th style="width:50px;">Status</th>
	        </tr>
	    </thead>
	</table>
</div>

<div id="div-button-action" style="float: right; margin-top: 15px;">
    <button type="button" class="btn btn-primary btn-sm" onclick="openPassword()">Process Selected Request</button>
	<button type="button" class="btn btn-primary btn-sm" onclick="cancelRequest()">Cancel Selected Request</button>
</div>

<div id="div-input-passwd" style="display:none;">
	<div class="col-md-12">
		<div class="col-md-12 alert alert-info" role="alert"> 
			<strong>Petunjuk Pemilihan Kata Sandi</strong><br/>
				Panjang Kata Sandi tidak boleh kurang dari 8 atau lebih dari 15 karakter.<br/> 
				Kata Sandi yang kuat harus memenuhi 3 kriteria dari 4 kreteria berikut:<br/>
				1. Mengandung huruf besar<br/>
				2. Mengandung huruf kecil<br/>
				3. Mengandung angka<br/>
				4. Mengandung karakter khusus<br/>
		</div>
	</div>
	<div class="col-md-12" style="display:none" id="div-bad-password">
		<div class="col-md-12 alert alert-danger" role="alert">
			<button type="button" class="close" data-dismiss="alert" aria-label="Close">
					<span aria-hidden="true">&times;</span>
			</button>
			<div id="div-message"></div>
		</div>
	</div>
	<br />
	<table>
		<tr>
			<td>Enter Password</td>
			<td>:</td>
			<td><input type="password" class="form-control input-password" /></td>
		</tr>
		<tr>
			<td>Re-Enter Password</td>
			<td>:</td>
			<td><input type="password" class="form-control input-repassword" /></td>
		</tr>
	</table>
	
	<button type="button" style="float:right;" class="btn btn-primary btn-sm" onclick="processRequest()">Submit</button>
</div>

<script>
	var requestTable;

	$(document).ready(function(){
		document.title = "Statement Hardcopy Request";

        setDataTable();
        setDivFilter();
        setDatePicker();
        checkAllEvent();
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

    function setDatePicker(){
    	var reqDate=$('input.req-date');

        reqDate.datepicker({
            format: 'dd/mm/yyyy',
            startView: "days",
            minViewMode: "days",
            autoclose: true,
            locale: "id"
        });

        var periodeDate = $('input.periode-date');
        periodeDate.datepicker({
            format: 'MM yyyy',
            startView: "months",
            minViewMode: "months",
            autoclose: true,
            locale: "id"
        });
    }

    function setDataTable(){
    	requestTable =	$('#req-table').DataTable( {
	        "processing": true,
	        "serverSide": true,
	        "searching" : false,
	        "lengthChange" : true,
	        //"pageLength" : 5,
	        "ajax" : {
	        	"url" : "<c:url value='/hardcopy-request/get-list' />",
	        	"data" : function(d){
	        		d.reqFrom = $("input.req-from").val();
	        		d.reqTo = $("input.req-to").val();
	        		d.periodeFrom = getPeriodeValue($("input.periode-from"));
	        		d.periodeTo = getPeriodeValue($("input.periode-to"));
	        		d.accountNumber = $("input.account-number").val();
	        	},
	        	"dataSrc" : function (json) {
			      	for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
				        //json.data[i].number = json.data[i].number+'.';
				        json.data[i].checkBox = '<input type="hidden" value="' + json.data[i].id + '" /><input type="checkbox" class="request-checkbox td" />';
				        var status = json.data[i].status;

				        if(status==${pendingStatus})
				        	json.data[i].status = "Pending";
				        else if(status==${processedStatus})
				        	json.data[i].status = "Processed";
				        else
				        	json.data[i].status = "Canceled";
				    }
				    return json.data;
				}
	        },
	        "columns": [
	        	{ "data": "checkBox"},
	            { "data": "requestDate" },
	            { "data": "periode" },
	            { "data": "accountNumber" },
	            { "data": "currency" },
	            { "data": "mailingAddress" },
	            { "data": "status" }
	        ],
	        "columnDefs": [
			    { className: "td-center", "targets": [ 0, 4 ] },
                { orderable: false, "targets": [0, 1, 2, 3, 4, 5, 6] }
			],
            "dom": '<"entries"l>rt<"bottom"ifp><"clear">',
            "lengthMenu": [${strDefRowCount}],
            "drawCallback": function(settings) {
		        checkUncheckEvent();
		    }
   		});
    }

    function reloadTable(){
    	var reqFromStr = $("input.req-from").val();
    	var reqToStr = $("input.req-to").val();
    	var reqFrom = $("input.req-from").datepicker("getDate");
    	var reqTo = $("input.req-to").datepicker("getDate");
    	var periodeFromStr = $("input.periode-from").val();
    	var periodeToStr = $("input.periode-to").val();
    	var periodeFrom = $("input.periode-from").datepicker("getDate");
    	var periodeTo = $("input.periode-to").datepicker("getDate");
    	
    	if(reqFromStr!="" && reqToStr!=""){
    		if(reqTo<reqFrom){
        		alert("Tanggal request mulai harus lebih lama dari tanggal request selesai!")
        		return;
        	}
    	} 
    	if(periodeFromStr!="" && periodeToStr!=""){
    		if(periodeTo<periodeFrom){
    			alert("Bulan periode mulai harus lebih lama dari bulan periode selesai");
    			return;
    		}
		}
    	$("#all-check").prop("checked", false);
    	requestTable.ajax.reload();
    }

    function clearFilter(){
      $("input.req-from").val("");
      $("input.req-to").val("");
      $("input.periode-from").val("");
      $("input.periode-to").val("");
      $("input.account-number").val("");
      reloadTable();
    }

    function getPeriodeValue($input){
        var currentVal = $input.val();

        if(currentVal.length==0)
            return "";

        return $input.data("datepicker").getFormattedDate("yyyymm");
    }

    function checkAllEvent(){
        $("#all-check").on("change", function(){
            if($(this).is(":checked")){
                $(".request-checkbox").prop("checked", true);
            }
            else {
                $(".request-checkbox").prop("checked", false);
            }
        })
    }

    function checkUncheckEvent(){
        $(".request-checkbox").on("change", function(){
            if($(this).is(":checked")){
                if ($('.request-checkbox:checked').length == $('.request-checkbox').length) {
                    $("#all-check").prop("checked", true);
                }
            } else {
                if ($('.request-checkbox:checked').length != $('.request-checkbox').length) {
                   $("#all-check").prop("checked", false);
                }
            }
        })
    }

    function openPassword(){
    	var hcReqIds = [];

    	$.each($(".td.request-checkbox"), function(){
           if($(this).is(":checked")){
               var val = parseInt($(this).prev().val());
               hcReqIds.push(val);
           }
        });
    	
    	if(hcReqIds.length>0){
    		var commit = confirm("Apakah anda yakin akan memproses requests ini??");

        	if(commit){
        		processRequest();
        	}
    	}    	
    }
    
    function validateChangePassword(){
    	var password = $(".input-password").val().trim();
    	var cpassword = $(".input-repassword").val().trim();
    	var message = '';
    	
       	if (password != '' && cpassword != ''){
       		if (password == cpassword){
       			if( password.length < 8 ){
       				message = 'Kata sandi terlalu pendek.';       				
       			} else if( password.length > 15 ){
       				message = 'Kata sandi terlalu panjang.';       				
       			}else if( isStrongPassword(password) ){
       				return true;
       			}else{
       				message = 'Kata sandi kurang kuat.';
       			}       			
       		} else {
       			message = 'Kata sandi dan konfirmasi kata sandi harus sama.';
       		}
       	} else {
       		message = 'Kata sandi dan konfirmasi kata sandi tidak boleh kosong.';
       	}
       	$('#div-bad-password').show();
       	$('#div-message').html(message);
       	
       	return false;
    }
    
    function processRequest(){
    	var hcReqIds = [];
		var passwd = $(".input-password").val();
		
    	$.each($(".td.request-checkbox"), function(){
           if($(this).is(":checked")){
               var val = parseInt($(this).prev().val());
               hcReqIds.push(val);
           }
        });

        if(hcReqIds.length>0){
        	loading();
        	var ajax = $.ajax({
                       url: "<c:url value='/hardcopy-request/process?password=&hcReqIds="+hcReqIds+"' />",
                       timeout : 6000000,
                       type : 'PUT',
                       dataType: "json"
                   });
                ajax.done(function (response) {
             	   if (response.resultCode == 1000) {
                        	reloadTable();
                        
                        	/* var dlUrl = '<c:url value="/api/downloadfile/'+response.data+'" />?filename='+response.data+'&contentType=application/zip';
				var link = document.createElement('a');
				link.download = response.data;
				link.href = dlUrl;
				document.body.appendChild(link);
				link.click();
				 document.body.removeChild(link);*/
                    }
                    else if(response.resultCode == 5001){
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

    function loading(){
    	$('.fancybox-opened').hide();
		$('.fancybox-overlay').append('<div id="fancybox-loading"><div></div></div>');
    }
    
    function clearPassword(){
    	$(".input-password").val("");
    	$(".input-repassword").val("");
    }
    
    function isStrongPassword(password){
		var point = 0;
		if( /[0-9]/.test(password) )
			point++;
		
		if( /[a-z]/.test(password) )
			point++;
		
		if( /[A-Z]/.test(password) )
			point++;
		
		if( /[~`!#$%\^&*+=\-\[\]\\';,/{}|\\":<>\?@]/.test(password) )
			point++;
		return point >= 3;
	}
    
    function cancelRequest(){
    	var hcReqIds = [];

    	$.each($(".td.request-checkbox"), function(){
           if($(this).is(":checked")){
               var val = parseInt($(this).prev().val());
               hcReqIds.push(val);
           }
        });

        if(hcReqIds.length>0){
        	var commit = confirm("Apakah anda yakin akan membatalkan requests ini??");

        	if(commit){
        		var ajax = $.ajax({
                        url: "<c:url value='/hardcopy-request/cancel?hcReqIds="+hcReqIds+"' />",
                        timeout : 30000,
                        type : 'PUT',
                        dataType: "json"
                    });
                    ajax.done(function (response) {
                    	console.log(JSON.stringify(response));
                        if (response.resultCode == 1000) {
                            reloadTable();
                            alert(response.message);
                        }
                        else if(response.resultCode == 5001){
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