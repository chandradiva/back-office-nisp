$(function(){

	// **********************************
	//  Description of ALL pager options
	// **********************************
	var pagerOptions = {

		// target the pager markup - see the HTML block below
		container: $(".modal-body .pager"),

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
		storageKey:'modal_tablesorter-pager',

		// if true, the table will remain the same height no matter how many records are displayed. The space is made up by an empty
		// table row set to a height to compensate; default is false
		fixedHeight: false,

		// remove rows from the table to speed up the sort of large tables.
		// setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
		removeRows: false,

		// css class names of pager arrows
		cssNext: '.modal_next', // next page arrow
		cssPrev: '.modal_prev', // previous page arrow
		cssFirst: '.modal_first', // go to first page arrow
		cssLast: '.modal_last', // go to last page arrow
		cssGoto: '.modal_gotoPage', // select dropdown to allow choosing a page

		cssPageDisplay: '.modal_pagedisplay', // location of where the "output" is displayed
		cssPageSize: '.modal_pagesize', // page size selector - select dropdown that sets the "size" option

		// class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
		cssDisabled: 'disabled', // Note there is no period "." in front of this class name
		cssErrorRow: 'modal_tablesorter-errorRow' // ajax error information row

	};

	$(".tablesorter").tablesorter()
	// bind to pager events
	.bind('pagerChange pagerComplete pagerInitialized pageMoved', function(e, c){
		var msg = '"</span> event triggered, ' + (e.type === 'pagerChange' ? 'going to' : 'now on') +
			' page <span class="typ">' + (c.page + 1) + '/' + c.totalPages + '</span>';
		$('.modal-body #display')
			.append('<li><span class="str">"' + e.type + msg + '</li>')
			.find('li:first').remove();
	})

	// initialize the pager plugin
	.tablesorterPager(pagerOptions);
});

// Custom Features
$(function(){
	// reset to page one on pager when user change pagesize 
	$('.modal_pagesize').on('change',function(e){
		$(this)
			.closest('div[class*="col-"]')
			.siblings()
			.find('.modal_first').trigger('click');
	});

	// ajax loader
	$('.modal_tablesorter-pager .pagination li:not(disabled)').on('click', function () {
		if( $('.modal_tablesorter-pager .pagination li.disabled').length ){
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