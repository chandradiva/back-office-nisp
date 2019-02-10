// add parser through the tablesorter addParser method
$.tablesorter.addParser({
	id: 'checkbox',
	is: function(s) {
		return false;
	},
	format: function(s, table, cell, cellIndex) {
		var $t = $(table),
			$tb = $t.children('tbody'),
			$c = $(cell),
			c, check,

			// resort the table after the checkbox status has changed
			resort = false;

		if (!$t.hasClass('hasCheckbox')) {
			
			// update the select all visible checkbox in the header
			check = function($t) {
				var $v = $tb.children('tr:visible'),
					$c = $v.filter('.checked');
				$t.find('.selectVisible').prop('checked', $v.length === $c.length);
			};

			$t
			.addClass('hasCheckbox')
			// update select all checkbox in header
			.bind('pageMoved', function() {
				check($t);
			})
			// make checkbox in header set all others
			.find('thead th:eq(' + cellIndex + ') input[type=checkbox]')
			.addClass('selectVisible')
			.bind('change', function() {
				c = this.checked;
				$tb.find('> tr:visible td:nth-child(' + (cellIndex + 1) + ') input')
					.each(function() {
						this.checked = c;
						$(this).trigger('change');
					});
			}).bind('mouseup', function() {
				return false;
			});
			$tb.children('tr').each(function() {
				$(this).find('td:eq(' + cellIndex + ')').find('input[type=checkbox]')
					.bind('change', function() {
						$t.trigger('updateCell', [$(this).closest('td')[0], resort]);
						check($t);
					});
			});
		}
		// return 1 for true, 2 for false, so true sorts before false
		c = ($c.find('input[type=checkbox]')[0].checked) ? 1 : 2;
		$c.closest('tr')[c === 1 ? 'addClass' : 'removeClass']('checked');
		return c;
	},
	type: 'numeric'
});

// Tablesorter
$(function(){
	// **********************************
	//  Description of ALL pager options
	// **********************************
	var pagerOptions = {

		// target the pager markup - see the HTML block below
		container: $(".pager"),

		// use this url format "http:/mydatabase.com?page={page}&size={size}&{sortList:col}"
		ajaxUrl: null,

		// modify the url after all processing has been applied
		customAjaxUrl: function(table, url) { return url; },

		// process ajax so that the data object is returned along with the total number of rows
		// example: { "data" : [{ "ID": 1, "Name": "Foo", "Last": "Bar" }], "total_rows" : 100 }
		ajaxProcessing: function(data){
			 if (data && data.hasOwnProperty('rows')) {
			  var r, row, c, d = data.rows,
			  // total number of rows (required)
			  total = data.total_rows,
			  // array of header names (optional)
			  //headers = data.headers,
			  // all rows: array of arrays; each internal array has the table cell data for that row
			  rows = [],
			  // len should match pager set size (c.size)
			  len = d.length;
			  // this will depend on how the json is set up - see City0.json
			  // rows
			  for ( r=0; r < len; r++ ) {
				row = []; // new row array
				// cells
				for ( c in d[r] ) {
				  if (typeof(c) === "string") {
					row.push(d[r][c]); // add each table cell data to row array
				  }
				}
				rows.push(row); // add new row array to rows array
			  }
			  // in version 2.10, you can optionally return $(rows) a set of table rows within a jQuery object
			  return [ total, rows ];
			}
		},
		// output string - default is '{page}/{totalPages}'
		// possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
		output: '{page}/{totalPages}',

		// apply disabled classname to the pager arrows when the rows at either extreme is visible - default is true
		updateArrows: true,

		// starting page of the pager (zero based index)
		page: 0,

		// Number of visible rows - default is 10
		size: 10,

		// Save pager page & size if the storage script is loaded (requires $.tablesorter.storage in jquery.tablesorter.widgets.js)
		savePages : false,
		
		//defines custom storage key
		storageKey:'tablesorter-pager',

		// if true, the table will remain the same height no matter how many records are displayed. The space is made up by an empty
		// table row set to a height to compensate; default is false
		fixedHeight: false,

		// remove rows from the table to speed up the sort of large tables.
		// setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
		removeRows: false,

		// css class names of pager arrows
		cssNext: '.next', // next page arrow
		cssPrev: '.prev', // previous page arrow
		cssFirst: '.first', // go to first page arrow
		cssLast: '.last', // go to last page arrow
		cssGoto: '.gotoPage', // select dropdown to allow choosing a page

		cssPageDisplay: '.pagedisplay', // location of where the "output" is displayed
		cssPageSize: '.pagesize', // page size selector - select dropdown that sets the "size" option

		// class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
		cssDisabled: 'disabled', // Note there is no period "." in front of this class name
		cssErrorRow: 'tablesorter-errorRow' // ajax error information row

	};
	
	$(".tablesorter").tablesorter()
	// bind to pager events
	.bind('pagerChange pagerComplete pagerInitialized pageMoved', function(e, c){
		var msg = '"</span> event triggered, ' + (e.type === 'pagerChange' ? 'going to' : 'now on') +
			' page <span class="typ">' + (c.page + 1) + '/' + c.totalPages + '</span>';
		$('#display')
			.append('<li><span class="str">"' + e.type + msg + '</li>')
			.find('li:first').remove();
	})

	// initialize the pager plugin
	.tablesorterPager(pagerOptions);

});

// Custom Features
$(function(){
	// reset to page one on pager when user change pagesize 
	$('.pagesize').on('change',function(e){
		$(this)
			.closest('div[class*="col-"]')
			.siblings()
			.find('.first').trigger('click');
	});

	// all checkboxes unchecked when user clicks on next/previous page 
	$('.pagination li:not(disabled)').click(function () {
		$(this)
			.closest("div.row")
			.siblings(".table-responsive")
			.find("th input[type=checkbox], td input[type=checkbox]")
			.prop('checked', false);
		$(this)
			.closest("div.row")
			.siblings(".table-responsive")
			.find("tbody tr")
			.removeClass('checked');
	});
	
	// all checkboxes unchecked when user choose page 
	$('.gotoPage').change(function(){
		$(this)
			.closest("div.row")
			.siblings(".table-responsive")
			.find("th input[type=checkbox], td input[type=checkbox]")
			.prop('checked', false);
		$(this)
			.closest("div.row")
			.siblings(".table-responsive")
			.find("tbody tr")
			.removeClass('checked');
	});	
	
	// ajax loader
	$('.pagination li:not(disabled)').on('click', function () {
		if( $('.pagination li.disabled').length ){
		} else {
			$.ajax({
				// your ajax code
				beforeSend: function(){
					$(this)
						.closest("div.row")
						.siblings(".table-responsive")
						.find('.loader').show()
				},
				complete: function(){
					$(this)
						.closest("div.row")
						.siblings(".table-responsive")
						.find('.loader').hide();
				}
			});
		}
	});	
});