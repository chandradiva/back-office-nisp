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
     
    #riwayat-transaksi-table tbody tr {
    	cursor: pointer;
    }
       
</style>

<h4>Riwayat Transaksi</h4>
<br />

<div id="div-filter">
    <table class="div-filter-table">
        <tr>
            <td style="padding-top:10px;">CIF Number </td>
            <td style="padding-top:10px;"><input class="form-control" id="cif_number" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Nama Nasabah </td>
            <td style="padding-top:10px;"><input class="form-control" id="nama_nasabah" value="" type="text" /></td>
        </tr>        
        <tr>
            <td style="padding-top:10px;">Nomor Rekening</td>
            <td style="padding-top:10px;"><input class="form-control" id="nomor_rekening" value="" type="text" /></td>
        </tr>
        <tr>
            <td style="padding-top:10px;">Currency</td>
            <td style="padding-top:10px;"><select class="form-control"  id="currency" >
            	<option value="-1" selected>All Currency</option>
            	<option value="IDR">IDR</option>
            	<option value="USD">USD</option>
            	<option value="SGD">SGD</option>
            	<option value="EUR">EUR</option>
            	<option value="AUD">AUD</option>
            	<option value="HKD">HKD</option>
            	<option value="JPY">JPY</option>
            	<option value="GBP">GBP</option>
            	<option value="NZD">NZD</option>
            	<option value="CNH">CNH</option>
            	<option value="CHF">CHF</option>
            	<option value="CAD">CAD</option>            	
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

<div id="data-table" class="table-responsive" style="width:100%;">
	<table id="riwayat-transaksi-table" class="display" width="100%" cellspacing="0">
	    <thead>
	        <tr>
	            <th style="width:10px;">No.</th>
	            <th style="text-align:left;">CIF Number</th>
	            <th style="text-align:left;">Nama Nasabah</th>	            
	            <th style="text-align:left;">Nomor Rekening</th>
	            <th style="text-align:left;">Currency</th>
	        </tr>
	    </thead>
	</table>
</div>

<div id="pick-time" class="table-responsive col-lg-6" style="display:none">
	<h4>Pilih Rentang Waktu Transaksi</h4>
	<table class="table">	
		<tr>
			<td>
				CIF Number
			</td>
			<td colspan="2">
				<label id="lCifNumber"></label>
			</td>
		<tr>
			<td>
				Nama Nasabah
			</td>
			<td colspan="2">
				<label id="lNamaNasabah"></label>
			</td>
		</tr>
		<tr>
			<td>
				Nomor Rekening
			</td>
			<td colspan="2">
				<label id="lNomorRekening"></label>
			</td>
		</tr>
		<tr>
			<td>
				Currency
			</td>
			<td colspan="2">
				<label id="lCurrency"></label>
			</td>
		</tr>
		<tr>
			<td width="10%" style="background-color:transparent; border:none">
				<div class="form-inline form-group">
				  <div class="col-md-12 input-group date" >
						<input id="startDate" class="form-control" type="text" placeholder="e.g. ${strAMonthAgo}" readonly>
						<span class="input-group-addon"><span class="glyphicon-calendar glyphicon"></span></span>
				  </div>
				</div>
			</td>
			<td width="10%" style="background-color:transparent; border:none; text-align:center">
				sampai
			</td>
			<td width="10%" style="background-color:transparent; border:none">
				<div class="form-inline form-group">
				  <div class="col-md-12 input-group date">
						<input id="endDate" class="form-control" type="text" placeholder="e.g. ${strToday}" readonly>
						<span class="input-group-addon"><span class="glyphicon-calendar glyphicon"></span></span>
				  </div>
				</div>
			</td>
		</tr>
		<tr>    		
    		<td colspan="3" style="background-color:transparent; border:none">
    		<button type="button" class="btn btn-primary btn-sm" onclick="kembali()">Kembali</button>
    		<button type="button" class="btn btn-primary btn-sm" onclick="tampil()" id="btn_tampil" disabled>Tampilkan</button>
    		<button type="button" class="btn btn-primary btn-sm" onclick="download()" id="btn_download" disabled>Download</button></td>
    	</tr>
	</table>
	<div id="div-info" class="alert alert-info">
	    <strong>Info!</strong><br>
	    Disarankan untuk memilih rentang tanggal hanya per 1 bulan saja.<br>
	    Karena apabila memilih rentang tanggal langsung beberapa bulan, akan membuat lama penarikan Data Statement.
	</div>
	<div id="div-error" class="alert alert-danger" style="display:none">
		<strong>Failed</strong>
		<div id="div-error-message"></div>
	</div>
</div>
<div id="divLoadingBo"> 
</div>

<script>
	var requestTable;
	var selectedData;
	
	$(document).ready(function() {
		document.title = "Riwayat Transaksi";

		setDataTable();		
		$('#riwayat-transaksi-table tbody').on('click', 'tr',function(){
			$('#div-error').hide();
			 if ( $(this).hasClass('selected') ) {
		            $(this).removeClass('selected');
		        }
		        else {
		        	requestTable.$('tr.selected').removeClass('selected');
		            $(this).addClass('selected');
		        }
			 selectedData = requestTable.row( this ).data();	
			 	$('#lCifNumber').text(selectedData.cifKey);
				$('#lNamaNasabah').text(selectedData.namaNasabah);
				$('#lNomorRekening').text(selectedData.accountNo);
				$('#lCurrency').text(selectedData.currency);
			$('#startDate').val('');
			$('#endDate').val('');
			$('#btn_tampil').prop('disabled', true);
			$('#btn_download').prop('disabled', true);
			 $('#div-filter').hide();
			 $('#data-table').hide();
			 $('#pick-time').show();
								
		});
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
		$("#cif_number").val("-1");
		requestTable = $('#riwayat-transaksi-table').DataTable( {
	        "processing": true,
	        "serverSide": true,
	        "searching" : false,
	        "lengthChange" : true,
	        //"pageLength" : 15,
	        "ajax" : {
	        	"url" : "<c:url value='/riwayat-transaksi/get-all' />",
	        	"data" : function(d){
	        		d.cifKey = $("#cif_number").val();
	        		d.nomorRekening = $("#nomor_rekening").val();
	        		d.namaNasabah = $("#nama_nasabah").val();
	        		d.currency = $("#currency").val();	        		
	        	},
	        	"dataSrc" : function (json) {
			      	for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
			      		json.data[i].cifKey = json.data[i].cifKey.substring(1);
			      		json.data[i].accountNo = json.data[i].accountNo.substring(7);
				        json.data[i].number = json.data[i].number+'.';
				        json.data[i].namaNasabah = json.data[i].namaNasabah1+json.data[i].namaNasabah2+json.data[i].namaNasabah3;
				    }
				    return json.data;
				}
	        },
	        "columns": [
	        	{ "data": "number" },
	            { "data": "cifKey" },
	            { "data": "namaNasabah" },
	            { "data": "accountNo" },
	            { "data": "currency" },
	            { "data": "productName" },
	            { "data": "namaNasabah1" },
	            { "data": "namaNasabah2" },
	            { "data": "namaNasabah3" },
	            { "data": "branchCode" },
	            { "data": "branchName" },
	            { "data": "accountType" },
	            { "data": "flag" }
	        ],
	        "columnDefs": [
			    { className: "td-number", "targets": [ 0 ] },
			    { orderable: false, "targets": [0, 1, 2, 3, 4] },
			    { visible: false, searchable: false, "targets": [5, 6, 7, 8, 9, 10, 11, 12] }
			],
			"lengthMenu": [${strDefRowCount}],
			"dom": '<"entries"l>rt<"bottom"ifp><"clear">'
	    });
		$("#cif_number").val("");
	}

	function clearFilter(){
		$("#cif_number").val("-1");
		$("#nama_nasabah").val("");
		$("#nomor_rekening").val("");
		$("#currency").val("-1");
		reloadTable();
		$("#cif_number").val("");
	}

	function reloadTable(){
		var cifKey = $("#cif_number").val();
		var nomorRekening = $("#nomor_rekening").val();
		var namaNasabah = $("#nama_nasabah").val();
		var currency = $("#currency").val();		
		if( cifKey == '' && nomorRekening == '' && namaNasabah == '' && currency == '-1')
			alert('Silakan isi salah satu field pencarian');
		else
    		requestTable.ajax.reload();
    }
    
	$(function () {
		$('.date').datetimepicker({
			pickTime: false,
			format: 'DD/MM/YYYY'		
			
		}).on('dp.change', function(e){			
			dateOnChange();
		});
	});
	
	function dateOnChange(){
		var startDate = $('#startDate').val().trim();
		var endDate = $('#endDate').val().trim();
		var good = false;
		if( compareDate(startDate, endDate) > 0 ){
			alert('Tanggal awal tidak boleh lebih besar dari tanggal akhir');						
		}else{
			good = startDate.length > 0 && endDate.length > 0 ;			
		}
		if( good ){
			$('#btn_tampil').prop('disabled', false);
			$('#btn_download').prop('disabled', false);
		}else{
			$('#btn_tampil').prop('disabled', true);
			$('#btn_download').prop('disabled', true);
		}
	}
	
	function kembali(){
		$('#div-filter').show();
		$('#data-table').show();
		$('#pick-time').hide();
		$('#div-error').hide();
	}
	
	function tampil(){
		$('#div-error').hide();
		$("#divLoadingBo").addClass("show");
		var ajax = $.ajax({
    		url: "<c:url value='riwayat-transaksi/riwayat_transaksi_tampil' />",
    		type: 'POST',
    		data: {
    			'cif': selectedData.cifKey,
				'nomorRekening': selectedData.accountNo,
				'currency': selectedData.currency,
				'startDate': $('#startDate').val(),
				'endDate': $('#endDate').val(),
				'productName': selectedData.productName,
				'namaNasabah1': selectedData.namaNasabah1,
				'namaNasabah2': selectedData.namaNasabah2,
				'namaNasabah3': selectedData.namaNasabah3,
				'branchCode': selectedData.branchCode,
				'branchName': selectedData.branchName,
				'accountType': selectedData.accountType,
				'flag': selectedData.flag
			}
    	});
    	
    	ajax.success(function(response){
    		$("#divLoadingBo").removeClass("show");
    		if( response.resultCode == 1000 ){
    			var resp = JSON.parse(response.data.res);
    			if( resp.resultCode == 1000 ){
    				window.open('<c:url value="/api/downloadfile/'+resp.data+'" />?filename='+resp.data+"&contentType=application/pdf&isPrint=1");
    			} else if (resp.resultCode == 1001) {
    				$('#div-error').show();
        			$('#div-error-message').html('No data found.');
    			}else{
    				$('#div-error').show();
        			$('#div-error-message').html('Open file is not available right now, please try again later.');
    			}
    		}else if( response.resultCode == 4000 ){
    			window.location.href = '<c:url value="/login" />';
    		}else{
    			$('#div-error').show();
    			$('#div-error-message').html('Open file is not available right now, please try again later.');
    		}	
    	});
    	return false;
	}
    
	function download(){
		$('#div-error').hide();
		$("#divLoadingBo").addClass("show");
		var ajax = $.ajax({
    		url: "<c:url value='riwayat-transaksi/riwayat_transaksi_download' />",
    		type: 'POST',
    		data: {    			
				'cif': selectedData.cifKey,
				'nomorRekening': selectedData.accountNo,
				'currency': selectedData.currency,
				'startDate': $('#startDate').val(),
				'endDate': $('#endDate').val(),
				'productName': selectedData.productName,
				'namaNasabah1': selectedData.namaNasabah1,
				'namaNasabah2': selectedData.namaNasabah2,
				'namaNasabah3': selectedData.namaNasabah3,
				'branchCode': selectedData.branchCode,
				'branchName': selectedData.branchName,
				'accountType': selectedData.accountType,
				'flag': selectedData.flag
			}
    	});
    	
    	ajax.success(function(response){
    		$("#divLoadingBo").removeClass("show");
    		if( response.resultCode == 1000 ){
    			var resp = JSON.parse(response.data.res);
    			if( resp.resultCode == 1000 ){
    				var dlUrl = '<c:url value="/api/downloadfile/'+resp.data+'" />?filename='+resp.data+"&contentType=application/pdf";
					var link = document.createElement('a');
					link.download = resp.data;
					link.href = dlUrl;
					document.body.appendChild(link);
					link.click();
					document.body.removeChild(link);
    			}else if (resp.resultCode == 1001) {
    				$('#div-error').show();
        			$('#div-error-message').html('No data found.');
    			}else{
    				$('#div-error').show();
        			$('#div-error-message').html('Open file is not available right now, please try again later.');
    			}
    		}else if( response.resultCode == 4000 ){
    			window.location.href = '<c:url value="/login" />';
    		}else{
    			$('#div-error').show();
    			$('#div-error-message').html('Open file is not available right now, please try again later.');
    		}
    	});
    	return false;
	}
</script>