<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h4>SPT Statement Supporting Data</h4>
<!-- Main Content -->
<div class="col-md-9 alert alert-danger" role="alert" style="display:none" id="div-error">
		<button type="button" class="close" data-hide="alert" aria-label="Close">
				<span aria-hidden="true">&times;</span>
		</button>
		<div id="div-error-message"></div>
	</div>

<div class="col-md-9 main-content">
	
	<!-- account balance snapshot -->
	<div class="well-container well-special">
		<div class="well widget">
			<div class="widget-header">
				<h4>
					Input CIF and Select Period
				</h4>
			</div>
			<div id="select_acc" class="widget-body collapse in">
				<!-- insert dynamic content here -->
				<div class="row">
					<div class="form-group">
						<div class="col-md-12">
							<div class="col-md-4">
								<input type="text" id="temp_cif" placeholder="CIF Key">										
							</div>
						</div>
						<div class="col-md-12">
							<br />							
							<div class="col-md-3">
								<select class="form-control" id="tr_year">
									<option value="-1">-- Year --</option>
									<c:forEach items="${years }" var="yearItem">
										<option value="${yearItem }">${yearItem }</option>
									</c:forEach>
								</select>
							</div>
																	
						</div>									
						<div class="col-md-12">
							<br />
							<div class="col-md-3">
								<select class="form-control" id="tr_from_month" disabled>
									<option value="-1">-- From Month --</option>																					
								</select>										
							</div>
							<div class="col-md-3">
								<select class="form-control" id="tr_to_month" disabled>
									<option value="-1">-- To Month --</option>								
								</select>										
							</div>
						</div>												
					</div>
				</div>
				<!-- end of dynamic content -->
				<br/>
					<div class="widget-footer text-right">
						<img class="loading-image" id="print-loading" style="display:none" src="<c:url value='/resources/img/ajax-loader-small.gif'/>" alt="processing.."/>
						<a href="#" class="btn btn-custom-rock" id="btn-export" disabled>Download&nbsp;<i class="fa fa-chevron-circle-right"></i></a>
						<!-- <button type="button" id="bt-generate" class="btn btn-custom-rock">Print &nbsp;<i class="fa fa-print"></i></button> -->						
					</div>
			</div>				
		</div>
	</div>
					
</div>
<!--end of main content-->

<script type="text/javascript">
	var monthsName = ['January', 'February', 'Maret', 'April', 'May', 
	                  'June', 'July', 'August', 'September', 'October', 
	                  'November', 'December'];
	
	function populateFromMonth(year){
		var thisMonth = ${month_now};
		var thisYear = ${year_now};
		$('#tr_from_month').html('');
		$('#tr_from_month').append('<option value="-1">-- From Month --</option>');
		if( thisYear == year ){
			$('#tr_from_month').prop('disabled', false);
			for(var i=0; i<=thisMonth; i++ ){
				$('#tr_from_month').append('<option value="'+(i+1)+'">'+monthsName[i]+'</option>');
			}
		}else if( thisYear-1 == year ){
			$('#tr_from_month').prop('disabled', false);
			for(var i=0; i<=11; i++ ){
				$('#tr_from_month').append('<option value="'+(i+1)+'">'+monthsName[i]+'</option>');
			}
		}else{
			noOptMonth($('#tr_from_month'), 1, monthsName[0]);
			noOptMonth($('#tr_to_month'), 12, monthsName[11]);
		}
	}
	
	function populateToMonth(fromMonth){
		var thisMonth = ${month_now};
		var thisYear = ${year_now};
		var selYear = parseInt($('#tr_year').val());
		$('#tr_to_month').html('');
		$('#tr_to_month').append('<option value="-1">-- To Month --</option>');
		if( thisYear == selYear ){
			$('#tr_to_month').prop('disabled', false);
			for(var i=fromMonth; i<=thisMonth; i++ ){
				$('#tr_to_month').append('<option value="'+(i+1)+'">'+monthsName[i]+'</option>');
			}
		}else if( thisYear-1 == selYear ){
			$('#tr_to_month').prop('disabled', false);
			for(var i=fromMonth; i<=11; i++ ){
				$('#tr_to_month').append('<option value="'+(i+1)+'">'+monthsName[i]+'</option>');
			}
		}
		
		$('#tr_to_month').prop('disabled', false);
	}
	
	function noOptMonth(monthComp, val, valStr){
		$(monthComp).prop('disabled', true);
		$(monthComp).html('');
		$(monthComp).append('<option value="'+val+'" selected>'+valStr+'</option>');
	}
	
	$(document).ready(function() {
		$('#tr_year').off('change').on('change',function(){
			$('#div-error').hide();
			var selVal = $(this).val();
			noOptMonth($('#tr_to_month'), -1, '-- To Month --');
			$('#btn-print').attr('disabled', 'disabled');
			$('#btn-export').attr('disabled', 'disabled');
			if( selVal == -1 ){
				noOptMonth($('#tr_from_month'), -1, '-- From Month --');										
			}else{
				populateFromMonth(selVal);					
			}
		});	
  
		$('#tr_from_month').off('change').on('change', function(){
			$('#div-error').hide();
			$('#btn-export').attr('disabled', 'disabled');
			var selVal = $(this).val();
			if( selVal === -1 ){
				noOptMonth($('#tr_to_month'), -1, '-- To Month --');
			}else{
				populateToMonth(parseInt(selVal)-1);
			}
		});
		
		$('#tr_to_month').off('change').on('change', function(){
			$('#div-error').hide();
			var selVal = $(this).val();
			if( selVal != -1 ){
				$('#btn-export').removeAttr('disabled');
			}
		});
	});

	$('#btn-export').off('click').on('click',function(e){
		$('#print-loading').show();
		$('#btn-export').attr('disabled', 'disabled');
		var url = "<c:url value='/tax/print' />";
		var cifKey = $('#temp_cif').val();
		console.log('cifKey: '+cifKey);
		var year = $('#tr_year').val();
		var fromMonth = $('#tr_from_month').val();
		var toMonth = $('#tr_to_month').val();
		var data = 'cifKey='+cifKey+'&year='+year+'&fromMonth='+fromMonth+'&toMonth='+toMonth;
		
		var filename = 'OCBCNISP_SPT_'+cifKey+'_'+year+'_'+fromMonth+'_'+toMonth+'.pdf';
		var data = 'cifKey='+cifKey+'&year='+year+'&fromMonth='+fromMonth+'&toMonth='+toMonth 
					+ '&isPrint=0';
		var url = '<c:url value="/tax/print" />';
		var contentType = 'application/pdf';
		
		var dAjax = $.ajax({
			url: url,
			data: data,
			type: 'POST'
		});
		
		dAjax.success(function(response){
			if( response.resultCode == 1000 ){
				var resp = JSON.parse(response.data);
				if( resp.resultCode == 1000 ){
					if( resp.data.length == 0 ){
						$('#div-error-message').html('No Data Found');
						$('#div-error').show();
					}else{
						var dlUrl = '<c:url value="/api/downloadfile/'+resp.data+'" />?dlName='+filename+'&contentType='+contentType;						
						var link = document.createElement('a');
						link.download = filename;
						link.href = dlUrl;
						document.body.appendChild(link);
						link.click();
						document.body.removeChild(link);
						return false;
					}					
				}
			}
		});
		
		dAjax.always(function(){
			$('#print-loading').hide();
			$('#btn-export').removeAttr('disabled');
		});
		return false;
	});
	
	$(function() {
		$('a.tooltips,button.tooltips').tooltip();
	});
</script>