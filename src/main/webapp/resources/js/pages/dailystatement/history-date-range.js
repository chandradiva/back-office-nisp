var totalAllData = 10;
var currentPage = 1;
var sortBy = 'transaction date';
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
		requestData(accNo, dateStart, dateEnd, currentPage, countPerPage, sortBy, sortType);
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
	
	function requestData(accountNumber, start, end, page, countPerPage, sortBy, sortType){
		
		var data = {
			accountNumber: accountNumber,
			start: start,
			end: end,
			page: page, 
			countPerPage: countPerPage,
			sortBy: sortBy,
			sortType: sortType,
			currency: currency
		};
		
		$('#loading').fadeIn();
		var ajax = $.ajax({
                       url : url,
                       /* url : "<c:url value='/resources/sample/error.json'/>", */
                       data : data,
                       timeout : 30000,
                       type : 'post',
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
			            addRow(item.transactionDate, item.valueDate, item.transDescription, item.trxDescription, item.sign, item.amount, item.balance);     
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
		/* - sortBy (optional, valid value "transaction date", "value date", "description", "debet", "credit", "balance") */
		columnName = columnName.toLowerCase();
		direction = direction.toUpperCase();
		
		if (columnName.startsWith('description'))
			sortBy = 'description';
		else if (columnName.startsWith('debet'))
			sortBy = 'debet';
		else if (columnName.startsWith('credit'))
			sortBy = 'credit';
				
		sortType = direction;
		refreshData();
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
	
	function addRow(transactionDate, valueDate, transDescription, trxDescription, sign, amount, balance){
		amount = amount.formatMoney(isPoint==1?0:2, '.', ',');
		balance = balance.formatMoney(isPoint==1?0:2, '.', ',');
		
		var stringRow = '<tr>'+
							'<td><span style="font-weight:bold; color:#00a2cb; vertical-align:middle">'+transactionDate+'</span></td>'+
							'<td><span style="font-weight:bold; vertical-align:middle">'+valueDate+'</span></td>'+
							'<td class="text-right">'+
								'<div class="text-format-green" style="color:green">'+transDescription+'</div>'+
								'<div style="font-weight:bold; color:green">'+trxDescription+'</div>'+
							'</td>'+
							'<td class="text-right" style="color:red;">'+(sign=='D'?amount:'')+'</td>'+
							'<td class="text-right" style="color:blue;">'+(sign=='C'?amount:'')+'</td>'+
							'<td class="text-right" style="color:grey;">'+balance+'</td>'+
						'</tr>';
						
		$('#table-current-account tbody').append(stringRow);
	}
});