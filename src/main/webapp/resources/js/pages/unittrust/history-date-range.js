var totalAllData = 10;
var currentPage = 1;
var sortBy = 'reference number';
var sortType = 'ASC';
var countPerPage = 10;
var countPages = 1;
var countPerPageAllowed = [10, 25, 50];

$(document).ready(function () {
	$('.footable').footable();
	$('.footable').bind({
		'footable_sorting' : function(e) {
			sortData(e.column.name, e.direction);
			return true;
		},
		'footable_filtering' : function(e) {
			if (e.clear) {
				return confirm('Do you want to clear the filter?');
			} else {
				return confirm('Do you want to filter by ' + e.filter);
			}
		}
	});
		
	refreshData();
	
	function refreshData(){
		requestData(accountNo, dateStart, dateEnd, currentPage, countPerPage, sortBy, sortType);	
	}
	
	function refreshPagingRule(){
		$('.pagesize').on('change', function(){
			countPerPage = $(this).val();
			refreshData();
		});
		$('.gotoPage').on('change', function(){
			currentPage = $(this).val();
			refreshData();
		});
		$('.page-navigation').on('click', function(){
			var func = $(this).data('page');
			if (func == 'first'){
				currentPage = 1;
			} else if (func == 'prev'){
				currentPage--;
			} else if (func == 'next'){
				currentPage++;
			} else if (func == 'last'){
				currentPage = countPages;
			}
			refreshData();
		});
	}
	
	function setupPage(totalData){
		totalAllData = totalData;
		countPages = Math.ceil(totalAllData/countPerPage);
		
		$('.pagination').empty();
		if (currentPage != 1){
			$('.pagination').append('<li class="first" title="First page"><a href="#first" class="page-navigation" data-page="first">&laquo; First</a></li>');
			$('.pagination').append('<li class="prev" title="Previous page"><a href="#prev" class="page-navigation" data-page="prev">&lsaquo; Prev</a></li>');
		}
		$('.pagination').append('<li class="pagedisplay">-</li>');
		if (currentPage != countPages && countPages > 1){
			$('.pagination').append('<li class="next" title="Next page"><a href="#next" class="page-navigation" data-page="next">Next &rsaquo;</a></li>');
			$('.pagination').append('<li class="last" title="Last page"><a href="#last" class="page-navigation" data-page="last">Last &raquo;</a></li>');
		}
		
		$('.pagedisplay').html(currentPage+'/'+countPages);
		$('.total_transaction').html(totalAllData);
		$('.gotoPage').empty();
		for(var i=1;i<=countPages;i++){
			$('.gotoPage').append('<option value="'+i+'" '+(i==currentPage?'selected="selected"':'')+'>'+i+'</option>');
		}
		$('.pagesize').empty();
		for(var i=0;i<countPerPageAllowed.length;i++){
			$('.pagesize').append('<option value="'+countPerPageAllowed[i]+'" '+(countPerPageAllowed[i]==countPerPage?'selected="selected"':'')+'>'+countPerPageAllowed[i]+'</option>');
		}
	}
	
	function requestData(accountNumber, start, end, page, countPerPage, sortBy, sortType){
		
		var data = {
			accountNumber: accountNumber,
			currency : currency,
			start: start,
			end: end,
			page: page, 
			countPerPage: countPerPage,
			sortBy: sortBy,
			sortType: sortType
		};
		
		$('#loading').fadeIn();
		var ajax = $.ajax({
                       url : url,
                       data : data,
                       timeout : 120000,
                       type : 'POST',
                       dataType : 'json'
                   });
     	ajax.always(function(){
			$('#loading').hide();
			$('#table-current-account tbody').empty();
		});
		ajax.done(function (response) {
			if (response.errCode == 1000) {
				var resp = JSON.parse(response.data);
				if( resp.resultCode == 1000 ){
					$.each(resp.data, function(i,item){
			            addRow(item.referenceNo, item.transactionDate, item.effectiveDate, item.transactionType, item.currencyCode, item.amount, 
			            		item.nav, item.feeAmount, item.unit, item.status, item.transactionSource)
			        });
			        
			        setupPage(resp.totalAllData);
			        refreshPagingRule();
				}else{
					console.log(resp);
			        $('#textError').html(resp.message);
			        $('#mini-statement-error').fadeIn();
				}
		        
		    } else {
		        console.log(response);
		        $('#textError').html(response.message);
		        $('#mini-statement-error').fadeIn();
		    }
		});
		ajax.fail(function(xhr,status){
		    console.log('Failed: '+status);
		});
	}
	
	function sortData(columnName, direction){
		columnName = columnName.toLowerCase();
		direction = direction.toUpperCase();
		
		if (columnName.startsWith('no. reference'))
			sortBy = 'reference number';
		else if (columnName.startsWith('eff. date'))
			sortBy = 'effective date';
		else if (columnName.startsWith('transaction type'))
			sortBy = 'transaction amount';
		else if (columnName.startsWith('nab'))
			sortBy = 'nav';
		else if (columnName.startsWith('status'))
			sortBy = 'status';
		else
			sortBy = columnName;
				
		sortType = direction;
		refreshData();
	}
	
	function addRow(referenceNo, transactionDate, effectiveDate, transactionType, currencyCode, amount, nav, feeAmount, unit, status, transactionSource){
		amount = amount.formatMoney(2, '.', ',');
		unit = unit.formatMoney(2, '.', ',');
		nav = nav.formatMoney(2, '.', ',');
		feeAmount = feeAmount.formatMoney(2, '.', ',');
		
		var transactionTypeString = getTransactionType(transactionType);
		var transactionSourceString = getTransactionSource(transactionSource);
		var transactionStatusString = getTransactionStatus(status);
		
		var stringRow = '<tr>'+
							'<td><span style="font-weight:bold; color:#00a2cb; vertical-align:middle">'+referenceNo+'</span></td>'+
							'<td>'+transactionDate+'</td>'+
							'<td><span style="font-weight:bold; vertical-align:middle">'+effectiveDate+'</span></td>'+
							'<td class="text-right">'+
								'<a href="transaction_receipt.html" class="fancybox fancybox.ajax">'+
									'<span class="text-format-green" style="color:green">'+transactionTypeString+'</span>'+
									'<span style="font-weight:bold; color:green">'+amount+'</span>'+
								'</a>'+
							'</td>'+
							'<td class="text-right" style="color:red;"><span style="color:blue">'+nav+'</span>/ '+feeAmount+'</td>'+
							'<td class="text-right">'+unit+'</td>'+
							'<td class="text-right">'+
								'<span class="label label-success">'+transactionStatusString+'</span><br/>'+
								transactionSourceString+
							'</td>'+
						'</tr>';
						
		$('#table-current-account tbody').append(stringRow);
	}
});