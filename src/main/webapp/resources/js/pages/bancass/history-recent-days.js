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
		setupData(currentPage, countPerPage, sortBy, sortType)
	}
	function setupData(page, countPerPage, sortBy, sortType){
		requestData($('#combo_account').val(), days, page, countPerPage, sortBy, sortType);
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
	
	function requestData(accountNumber, countLastDays, page, countPerPage, sortBy, sortType){
		
		var data = {
			accountNumber: accountNumber,
			countLastDays: countLastDays,
			page: page, 
			countPerPage: countPerPage,
			sortBy: sortBy,
			sortType: sortType
		};
		
		$('#loading').fadeIn();
		var ajax = $.ajax({
                       url : url,
                       /* url : "<c:url value='/resources/sample/error.json'/>", */
                       data : data,
                       timeout : 30000,
                       type : 'get',
                       dataType : 'json'
                   });
     	ajax.always(function(){
			$('#loading').hide();
			$('#table-current-account tbody').empty();
		});
		ajax.done(function (response) {
			if (response.resultCode == 1000) {
		        $.each(response.data, function(i,item){
		            addRow(item.transactionDate, item.description, item.amount, item.currency, item.status)
		        });
		        
		        setupPage(response.totalAllData);
		        refreshPagingRule();
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
		else
			sortBy = columnName;
				
		sortType = direction;
		refreshData();
	}
	
	function addRow(transactionDate, description, amount, currency, status){
		amount = amount.formatMoney(2, '.', ',');
		/* balance = balance.formatMoney(2, '.', ','); */
		
		var stringRow = '<tr>'+
							'<td><span style="font-weight:bold; color:#00a2cb; vertical-align:middle">'+transactionDate+'</span></td>'+
							'<td><span style="font-weight:bold; vertical-align:middle">'+transactionDate+'</span></td>'+
							'<td class="text-right"><span class="text-format-green" style="color:green"><a href="transaction_receipt.html" class="fancybox fancybox.ajax">'+description+'</a></span></td>'+
							'<td class="text-right">'+amount+'</td>'+
							(status == 'G'?'<td class="text-right"><span class="label label-warning">Failed</span></td>':'<td class="text-right"><span class="label label-success">Successed</span></td>')+
						'</tr>';
						
		$('#table-current-account tbody').append(stringRow);
	}
});