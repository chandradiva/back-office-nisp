<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="<c:url value='/resources/js/bootstrap-datepicker.js'/>" ></script>
<link href='<c:url value="/resources/css/datepicker.css" />' rel="stylesheet" />
<!--<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css' />">-->

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
        display: none;
    }

    #div-filter > button {
        float: right;
        clear: both;
        margin-bottom: 10px;
    }

    #div-filter > #button-filter {
        float: right;
        margin-bottom: 10px;
    }

    table#button-filter tr td {
        padding:3px;
    }

    table.table-detail-send-email thead th {
        text-align: left;
        vertical-align: middle;
        border-left: none;
        border-right: none;
        font-weight: bold;
    }

    table.table-detail-send-email tbody tr td {
        vertical-align: middle;
        border-left: none;
        border-right: none;
        text-align: center;
    }

    #list-email-pagination {
        text-align: right;
    }

    ul.pagination {
        margin:0px 0px 20px 0px;
    }

    #div-button-action {
        text-align: right;
    }

    .paginations {
      display: inline-block;
      padding-left: 0;
      margin: 0px 0px 20px 0px;
      border-radius: 4px;
    }
    .paginations > li {
      display: inline;
    }
    .paginations > li > a,
    .paginations > li > span {
      position: relative;
      float: left;
      padding: 6px 12px;
      margin-left: -1px;
      line-height: 1.42857143;
      color: #337ab7;
      text-decoration: none;
      background-color: #fff;
      border: 1px solid #ddd;
    }
    .paginations > li:first-child > a,
    .paginations > li:first-child > span {
      margin-left: 0;
      border-top-left-radius: 4px;
      border-bottom-left-radius: 4px;
    }
    .paginations > li:last-child > a,
    .paginations > li:last-child > span {
      border-top-right-radius: 4px;
      border-bottom-right-radius: 4px;
    }
    .paginations > li > a:hover,
    .paginations > li > span:hover,
    .paginations > li > a:focus,
    .paginations > li > span:focus {
      z-index: 2;
      color: #23527c;
      background-color: #eee;
      border-color: #ddd;
    }

    .paginations > li > a:hover {
        cursor: pointer;
    }

    .paginations > .active > a,
    .paginations > .active > span,
    .paginations > .active > a:hover,
    .paginations > .active > span:hover,
    .paginations > .active > a:focus,
    .paginations > .active > span:focus {
      z-index: 3;
      color: #fff;
      cursor: default;
      background-color: #337ab7;
      border-color: #337ab7;
    }
    .paginations > .disabled > span,
    .paginations > .disabled > span:hover,
    .paginations > .disabled > span:focus,
    .paginations > .disabled > a,
    .paginations > .disabled > a:hover,
    .paginations > .disabled > a:focus {
      color: #777;
      cursor: not-allowed;
      background-color: #fff;
      border-color: #ddd;
    }

    #div-info {
        clear: both;
        display: none;
        text-align: center;
        padding: 5px;
    }
</style>

<h4>Monitoring Laporan Konsolidasi</h4>

<br />

<button type="button" class="btn btn-default filter-btn">Filter</button>
<div id="div-filter">
    <table class="div-filter-table">
        <tr>
            <td>CIF Key</td>
            <td><input type="text" class="form-control input-cif-key" maxlength="50" /></td>
        </tr>
        <!-- <tr>
            <td>Account Number</td>
            <td><input type="text" class="form-control input-account-number" maxlength="50" /></td>
        </tr> -->
        <tr>
            <td>Periode</td>
            <td><input class="form-control input-periode" style="cursor:pointer;" placeholder="Month YYYY" type="text" readonly /></td>
        </tr>
        <tr>
            <td>Status</td>
            <td>
                <select class="form-control input-status">
                    <!--Dropdown Status-->
                    <option value="${defaultStatus}" selected>All Status</option>
                    <option value="${sentStatus}">Terkirim</option>
                    <option value="${failedStatus}">Gagal Kirim</option>
                    <option value="${expiredStatus}">Expired</option>
                    <option value="${queueStatus}">Queue</option>
                    <option value="${processingStatus}">Processing</option>
                    <option value="${readStatus}">Dibaca</option>
                </select>
            </td>
        </tr>
    </table>

    <table id="button-filter">
        <tr>
            <td><button type="button" class="btn btn-primary btn-sm" id="search-btn">Search</button></td>
            <td><button type="button" class="btn btn-primary btn-sm" onclick="clearFilter()">Clear Filter</button></td>
        </tr>
    </table>
    
</div>

<div id="div-info" class="alert alert-info">
    <!--Text-->
</div>

<div id="div-sendmail-error" class="alert alert-danger" style="display:none">
	<strong>Failed</strong>
	<div id="div-sendmail-error-message"></div>
</div>

<div id="divLoadingBo"> 
</div>

<div class="table-responsive">
	<div align="right">
	<label>Show 
		<select aria-controls="log-table" class="" id="table_length">
			${strDefRowCountOptions }
		</select> entries
	</label>
    <table class="table table-striped table-bordered table-detail-send-email">
        <thead>
            <th style="width:40px; text-align:center;" class="check-column" >
                <input type="checkbox" id="parent-check" />
            </th>
            <th style="width:100px;">CIF Key</th>
            <!-- <th style="width:130px;">Account Number</th> -->
            <th style="width:130px;">Periode</th>
            <th style="width:120px; text-align:center;">PDF Generated Date</th>
            <th style="width:110px; text-align:center;">Tanggal Kirim</th>
            <th>Filename</th>
            <th style="width:100px;">Status</th>
        </thead>
        <tbody>
            <!--Content List Email-->
            <tr id="tr-no-row" style="display: none;">
                <td colspan="9" style="text-align: center;">- No Record -</td>
            </tr>
        </tbody>
    </table>

    <div id="list-email-pagination">
        <ul class="paginations">
            <li class='first-btn-pagination disabled'><a>&lsaquo;&lsaquo;</a></li>
            <li class='prev-btn-pagination disabled'><a>&lsaquo;</a></li>
            <li class='left-dot-btn-pagination disabled'><a>...</a></li>
            <!--page number-->
            <li class='right-dot-btn-pagination'><a>...</a></li>
            <li class='next-btn-pagination'><a>&rsaquo;</a></li>
            <li class='last-btn-pagination'><a>&rsaquo;&rsaquo;</a></li>
        </ul>
    </div>

    <div id="div-button-action">
        <c:if test="${showResendButton==true}">
            <button type="button" class="btn btn-primary btn-sm resend-all-btn">Resend Email</button>
        </c:if>
    </div>
</div>

<div id="parameter-awal" style="display:none;">
    <input class="old-cif-key" value="" />
    <input class="old-account-number" value="" />
    <input class="old-periode" value="" />
    <input class="old-status" value="${defaultStatus}" />
    <input class="old-total-records" value="0" />
    <input class="old-total-records-wl" value="0" />
    <input class="old-total-per-page" value="10" />
    <input class="all-checked-status" value="0" />
    <input class="number-of-nav" value="5" />
</div>

<script>
	var totalRow;
	var gActivePage=1;
	var gLastPage;
    $(document).ready(function(){    	
        document.title = "Monitoring Laporan Konsolidasi";

        $('#table_length').on('change', function(e){
    		var val = $(this).val();
    		$('.old-total-per-page').val(val);
    		$('#search-btn').click();
    	});
        
        /*Request awal Table & Pagination*/
        var cifKey = $(".old-cif-key").val();
        var accountNumber = $(".old-account-number").val();
        var periode = $(".old-periode").val();
        var status = $(".old-status").val();
        var pageNumber = 1;
        var totalPerPage = $(".old-total-per-page").val();

        refreshTable(cifKey, periode, status, pageNumber, totalPerPage, getAllCheckedStatus());
        loadPagination(cifKey, periode, status);

        setDivFilter();
        setDatePicker();
        searchButtonEvent();
        $('.form-control').keypress(function(e){
			var key = e.which;
			if( key == 13 ){
				$('#search-btn').click();
				return false;
			}
		});
    });
	
    function openFile(sendEmailId){
    	$('#div-sendmail-error').hide();
    	$("#divLoadingBo").addClass("show");
    	var ajax = $.ajax({
    		url: "<c:url value='konsolidasi-email/open-file?id="+sendEmailId+"&isReprint=1' />",
    		type: 'GET'
    	});
    	
    	ajax.success(function(response){
    		$("#divLoadingBo").removeClass("show");
    		if( response.resultCode == 1000 ){
    			var resp = JSON.parse(response.data.res);
    			if( resp.resultCode == 1000 ){
    				window.open('<c:url value="/api/downloadfile/'+resp.data+'" />?filename='+response.data.filename+"&contentType=application/pdf");
    			}else{
    				$('#div-sendmail-error').show();
        			$('#div-sendmail-error-message').html('Open file is not available right now, please try again later.');
    			}
    		}else if( response.resultCode == 4000 ){
    			window.location.href = '<c:url value="/login" />';
    		}else{
    			$('#div-sendmail-error').show();
    			$('#div-sendmail-error-message').html('Open file is not available right now, please try again later.');
    		}
    	});
    	return false;
    }
    
    function setDivFilter(){
        $(".filter-btn").click(function (){
            $("#div-filter").slideToggle(300);
        });
    }

    function setDatePicker(){
        var date_input=$('.input-periode');

        date_input.datepicker({
            format: 'MM yyyy',
            startView: "months",
            minViewMode: "months",
            autoclose : true,
            locale: "id"
        });
    }

    function checkAllEvent(){
        $("#parent-check").on("change", function(){
            if($(this).is(":checked")){
            		/* $.each($("input.email-checkbox"), function(){
            				if($(this).prop("disabled") == false){
            						$(this).prop("checked", true);
            				}
            		}) */
            		$("input.email-checkbox:enabled").prop("checked", true);

                //var totalRecord = parseInt($(".old-total-records").val());
                var totalRecord = getTotalRecordsWL();
                //var totalPerPage = parseInt($(".old-total-per-page").val());
                var totalPerPage = $("input.email-checkbox:enabled").length;

                //if(totalRecord > totalPerPage)
                    showDivInfo();
            }
            else {
                $(".email-checkbox").prop("checked", false);
                hideDivInfo();
            }
        })
    }

    function checkUncheckEvent(){
        $(".email-checkbox").on("change", function(){
            if($(this).is(":checked")){
                if ($('.email-checkbox:checked').length == $('.email-checkbox:enabled').length) {
                    $("#parent-check").prop("checked", true);

                    //var totalRecord = parseInt($(".old-total-records").val());
                    var totalRecord = getTotalRecordsWL();
                    //var totalPerPage = parseInt($(".old-total-per-page").val());
                    var totalPerPage = $("input.email-checkbox:enabled").length;

                    //if(totalRecord > totalPerPage)
                        showDivInfo();
                }
            } else {
                if ($('.email-checkbox:checked').length != $('.email-checkbox:enabled').length) {
                   $("#parent-check").prop("checked", false);
                    hideDivInfo();
                }
            }
        })
    }

    function getStatusInfo(statusCode){
        switch(statusCode){
            case 0:
                return "Belum Dikirim";

            case ${sentStatus}:
                return "Terkirim";

            case ${failedStatus}:
                return "Gagal Kirim";

            case ${expiredStatus}:
                return "Expired";

			case ${queueStatus}:
                return "Queue";
						
			case ${processingStatus}:
                return "Processing";

            case ${readStatus}:
                return "Dibaca";
						
            default:
                break;
        }
    }

    function clearTBody(){
        $('.table-detail-send-email tbody tr:not(#tr-no-row)').remove();
    }

    function showNoRecord(){
        $("tr#tr-no-row").show();
    }
    
    function hideNoRecord(){
    	$("tr#tr-no-row").hide();
    }

    function refreshTable(cifKey, periode, status, pageNumber, totalPerPage, checkedStatus){
        var ajax = $.ajax({
                        url: "<c:url value='/konsolidasi-email/list?cifKey="+cifKey+ "&periode="+periode+"&status="+status+"&current="+pageNumber+"&size="+totalPerPage+"' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                clearTBody();
                hideNoRecord();

                if(response.data.length==0){
                    showNoRecord();
                }

                $.each(response.data, function(i,item){
                	if(checkedStatus==1){                		
	                    var row = "<tr><td class='check-column'>"
	                            + "<input type='hidden' value='" + item.id + "' />";
	                            
	                    if(item.enabledStatus==false){
	                    		row += "<input type='checkbox' class='email-checkbox' disabled/></td><td style='text-align:left;'>"; 
	                    } else {
	                    		row += "<input type='checkbox' class='email-checkbox' checked/></td><td style='text-align:left;'>";
	                    }        
	                            
	                    row +=  (item.cifKey!=null ? item.cifKey : "N/A") + "</td>"
	                            + "<td style='text-align:left;'>"
	                            + getPeriode(item.periode) + "</td><td>"
	                            + (item.tglCetak!=null ? item.tglCetak : "") + "</td><td>"
	                            + (item.tglKirim!=null ? item.tglKirim : "") + "</td><td style='text-align:left;'>"
	                            + "<a href='#' onclick='return openFile("+item.id+")' >"+getFileName(item.filename)+"</a></td><td style='text-align:left;'>"
	                            + getStatusInfo(item.status)
	                            + "</td></tr>";
                	} else {
                		var row = "<tr><td class='check-column'>"
                            + "<input type='hidden' value='" + item.id + "' />"
                            
	                    if(item.enabledStatus==false){
	                    		row += "<input type='checkbox' class='email-checkbox' disabled/></td><td style='text-align:left;'>";
	                    }
	                    else {
	                    		row += "<input type='checkbox' class='email-checkbox'/></td><td style='text-align:left;'>";
	                    }        
	                            
	                    row +=  (item.cifKey!=null ? item.cifKey : "N/A") + "</td>"
	                            + "<td style='text-align:left;'>"
	                            + getPeriode(item.periode) + "</td><td>"
	                            + (item.tglCetak!=null ? item.tglCetak : "") + "</td><td>"
	                            + (item.tglKirim!=null ? item.tglKirim : "") + "</td><td style='text-align:left;'>"
	                            + "<a href='#' onclick='return openFile("+item.id+")'>"+getFileName(item.filename)+"</a></td><td style='text-align:left;'>"
	                            + getStatusInfo(item.status)
	                            + "</td></tr>";
                	}

                    $('.table-detail-send-email tbody').append(row);
                });

                checkAllEvent();
                
                if(getAllCheckedStatus()==1){
                	showDivInfo();
                	//selectAllData($(".old-total-records").val());
                	selectAllData(getTotalRecordsWL());
                } else {
                	hideDivInfo();
                }
                
                checkUncheckEvent();
                resendAllButton();
                //setCheckBoxColumn();
            }
            else {
                alert(response.message);
            }
        });
        ajax.fail(function(xhr,status){
            alert('Failed: '+status);
        });
    }

    function getFileName(fullpath){
        if(fullpath==null)
            return "-";

        return fullpath.replace(/^.*[\\\/]/, '');
    }

    function getPeriode(yyyyMM){
        var bulan;
        var tahun = yyyyMM.substr(0,4);

        if(yyyyMM.length!=6)
            return "-";

        switch(yyyyMM.substr(4,2)){
            case "01":
                bulan = "Januari";
                break;

            case "02":
                bulan = "Februari";
                break;

            case "03":
                bulan = "Maret";
                break;

            case "04":
                bulan = "April";
                break;

            case "05":
                bulan = "Mei";
                break;

            case "06":
                bulan = "Juni";
                break;

            case "07":
                bulan = "Juli";
                break;

            case "08":
                bulan = "Agustus";
                break;

            case "09":
                bulan = "September";
                break;

            case "10":
                bulan = "Oktober";
                break;

            case "11":
                bulan = "November";
                break;

            case "12":
                bulan = "Desember";
                break;

            default:
                bulan = "";
                break;
        }

        return bulan + " " + tahun;
    }

    function loadPagination(cifKey, periode, status){        
        var ajax2 = $.ajax({
                        url: "<c:url value='/konsolidasi-email/get-row-whitelist?cifKey="+cifKey+"&periode="+periode+"&status="+status+"' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax2.done(function (response) {
            if (response.resultCode == 1000) {
                $("input.old-total-records-wl").val(response.data);
                
                var ajax = $.ajax({
                    url: "<c:url value='/konsolidasi-email/get-row?cifKey="+cifKey+"&periode="+periode+"&status="+status+"' />",
                    timeout : 30000,
                    type : 'get'
                });
			    ajax.done(function (response) {
			        if (response.resultCode == 1000) {
			        	totalRow = response.data;
			            refreshPagination(totalRow);
			            setActivePage(1);
			        }
			        else {
			            alert(response.message);
			        }
			    });
			    ajax.fail(function(xhr,status){
			        alert('Failed: '+status);
			    });
            }
            else {
                alert(response.message);
            }
        });
        ajax2.fail(function(xhr,status){
            alert('Failed: '+status);
        });
    }
    
    function getTotalRecordsWL(){
    		return $("input.old-total-records-wl").val();
    }

    function refreshPagination(totalRow){
        var totalPerPage = $(".old-total-per-page").val();
        var jmlPage = Math.ceil(totalRow/totalPerPage);
		gLastPage = jmlPage;
        /*Remove Previous Pagination*/
        $("li.page-number-btn-pagination").remove();
        // $.each($("li"),function() {
        //     if($(this).hasClass("page-number-btn-pagination"))
        //         $(this).remove();
        // })

        /*Append new pagination*/
        var start=gActivePage - 4;
        if( start < 1 )
        	start = 1;
        var end = start+5;
        if( end > jmlPage )
        	end = jmlPage;
        
        $(".right-dot-btn-pagination").html('');
        for(var i=start; i<=end;i++){
            var htmlPage = "<li class='page-number-btn-pagination' style='display:none'><input class='page-number-button' type='hidden' value='"+i+"' /><a>" + i + "</a></li>";
            $(".right-dot-btn-pagination").before(htmlPage);
        }

        $(".old-total-records").val(totalRow);
        pageNumberPaginationEvent();
        //setActivePage(1);
        setPrevNextBtn();
        setPrevNextBtnEvent();
        setPrevNextNavEvent();
        hidePagination();
    }

    function pageNumberPaginationEvent(){
        var cifKey = $(".old-cif-key").val();
        var accountNumber = $(".old-account-number").val();
        var periode = $(".old-periode").val();
        var status = parseInt($(".old-status").val());
        var totalPerPage = $(".old-total-per-page").val();

        $(".page-number-btn-pagination").off().on("click", function(){
            var pageNumber = parseInt($(this).children().first().val());

            refreshTable(cifKey, periode, status, pageNumber, totalPerPage, getAllCheckedStatus());
            setActivePage(pageNumber);
            setPrevNextBtn();
            setPrevNextNavView();
        })
    }

    function setActivePage(pageNumber){
        var allList = $(".page-number-btn-pagination");

        $.each($(".page-number-btn-pagination"), function(){
            if($(this).hasClass("active"))
                $(this).removeClass("active");

            var currentPage = gActivePage;
            if(currentPage == pageNumber)
                $(this).addClass("active");
        });
        gActivePage = pageNumber;
        refreshPagination(totalRow);
    }

    function setOldValues(cifKey, periode, status){
        $(".old-cif-key").val(cifKey);
        $(".old-account-number").val('');
        $(".old-periode").val(periode);
        $(".old-status").val(status);
    }

    function searchButtonEvent(){
        $("#search-btn").off().on("click", function(){
            var cifKey = $(".input-cif-key").val();
            var accountNumber = $(".input-account-number").val();
            var periode = getPeriodeValue();
            var status = $(".input-status").val();
            var totalPerPage = $(".old-total-per-page").val();
            resetAllChecked();

            refreshTable(cifKey, periode, status, 1, totalPerPage, getAllCheckedStatus());
            loadPagination(cifKey, periode, status);

            setOldValues(cifKey, periode, status);
            hideDivInfo();
            showPagination();
        })
    }

    function clearFilter(){
        $(".input-cif-key").val("");
        $(".input-account-number").val("");
        $(".input-periode").val("");
        resetAllChecked();
        
        var $selectStatus = $("select.input-status");

        $.each($selectStatus.children(), function(){
            if($(this).val()==${defaultStatus}){   //All Status
                $(this).prop("selected", true);
            }
        })

        var cifKey = $(".input-cif-key").val();
        var accountNumber = $(".input-account-number").val();
        var periode = $(".input-periode").val();
        var status = $(".input-status").val();
        var totalPerPage = $(".old-total-per-page").val();

        refreshTable(cifKey, periode, status, 1, totalPerPage, getAllCheckedStatus());
        loadPagination(cifKey, periode, status);

        setOldValues(cifKey, periode, status);
        hideDivInfo();
        showPagination();
    }

    function getPeriodeValue(){
        var currentVal = $('.input-periode').val();

        if(currentVal.length==0)
            return "";

        return $('.input-periode').data("datepicker").getFormattedDate("yyyymm");
    }

    function showPagination(){
        $(".paginations").show();
    }

    function hidePagination(){
        var totalRecords = parseInt($(".old-total-records").val());
        var totalPerPage = parseInt($(".old-total-per-page").val());

        if(totalRecords <= totalPerPage){
            $(".paginations").hide();
        }
        else {
            setNavPagination(1, 0);
        }
    }

    function setNavPagination(activePage, status){
        var numberOfNav = parseInt($(".number-of-nav").val());
        var totalRow = parseInt($(".old-total-records").val());
        var totalPerPage = parseInt($(".old-total-per-page").val());
        var totalPage = Math.ceil(totalRow/totalPerPage);

        if(totalPage <= numberOfNav){
            $(".page-number-btn-pagination").show();
        } else {
            if(activePage == 1){
                var first = $(".page-number-btn-pagination").first();
                hideAllPage();
                for(var i=1;i<= numberOfNav;i++){
                    first.show();
                    first = first.next();
                }
            }
            else if(activePage == totalPage){
                var last = $(".page-number-btn-pagination").last();
                hideAllPage();
                for(var i=1;i<=numberOfNav;i++){
                    last.show();
                    last = last.prev();
                }
            }
            else {
                var first = $("ul.paginations").find("li.page-number-btn-pagination:visible:first");
                var last = $("ul.paginations").find("li.page-number-btn-pagination:visible:last");
                var firstValue = parseInt($("ul.paginations").find("li.page-number-btn-pagination:visible:first").children().first().val());
                var lastValue = parseInt($("ul.paginations").find("li.page-number-btn-pagination:visible:last").children().first().val());
                var lastPage = $(".page-number-btn-pagination").last();
                var firstPage = $(".page-number-btn-pagination").first();

                if(activePage > lastValue){
                    first.hide();
                    last.next().show();
                }
                if(activePage < firstValue){
                    last.hide();
                    first.prev().show();
                }

                var currentPage = $(".page-number-button[value='"+activePage+"']");
                var button = currentPage.parent();

                if(button.is(":visible")==false){
                    hideAllPage();
                    if(status==1){
                        if(totalPage - activePage >= numberOfNav){
                            for(var i=1;i<=numberOfNav;i++){
                                button.show();
                                button = button.next();
                            }
                        }
                        else {
                            button = lastPage;
                            for(var i=1;i<=numberOfNav;i++){
                                button.show();
                                button = button.prev();
                            }
                        }
                    }
                    if(status==2){
                        if(activePage - numberOfNav >= 1){
                            for(var i=1;i<=numberOfNav;i++){
                                button.show();
                                button = button.prev();
                            }
                        }
                        else {
                            button = firstPage;
                            for(var i=1;i<=numberOfNav;i++){
                                button.show();
                                button = button.next();
                            }
                        }
                    }
                }
            }
        }

        setPrevNextNavView();
    }

    function hideAllPage(){
        $(".page-number-btn-pagination").hide();
    }

    function setPrevNextBtn(){
        //var lastPage = $(".page-number-btn-pagination").length;
        //var activePage = $(".page-number-btn-pagination.active").children().first().val();

        /*Set Enabled Disabled Button*/
        if(gActivePage==1){
            $(".prev-btn-pagination").addClass("disabled");
            $(".first-btn-pagination").addClass("disabled");
        } else {
            $(".prev-btn-pagination").removeClass("disabled");
            $(".first-btn-pagination").removeClass("disabled");
        }

        if(gActivePage==gLastPage){
            $(".next-btn-pagination").addClass("disabled");
            $(".last-btn-pagination").addClass("disabled");
            $(".right-dot-btn-pagination").addClass("disabled");
        } else {
            $(".next-btn-pagination").removeClass("disabled");
            $(".last-btn-pagination").removeClass("disabled");
            $(".right-dot-btn-pagination").removeClass("disabled");
        }
    }

    function setPrevNextNavView(){
        var first = $(".page-number-btn-pagination").first();
        var last = $(".page-number-btn-pagination").last();

        if(first.is(":visible")){
            $(".left-dot-btn-pagination").addClass("disabled");
        } else {
            $(".left-dot-btn-pagination").removeClass("disabled");
        }

        if(last.is(":visible")){
            $(".right-dot-btn-pagination").addClass("disabled");
        } else {
            $(".right-dot-btn-pagination").removeClass("disabled");
        }
    }

    function setPrevNextNavEvent(){
        $(".left-dot-btn-pagination").off().on("click", function(){
            var firstVisible = $("ul.paginations").find("li.page-number-btn-pagination:visible:first");
            var firstValue = parseInt($("ul.paginations").find("li.page-number-btn-pagination:visible:first").children().first().val());
            var firstPage = $(".page-number-btn-pagination").first();
            var numberOfNav = parseInt($(".number-of-nav").val());

            if(firstValue - numberOfNav>=1){
                hideAllPage();
                var button = firstVisible.prev();
                for(var i=1; i<=numberOfNav;i++){
                    button.show();
                    button = button.prev();
                }
            } else {
                hideAllPage();
                var button = firstPage;
                for(var i=1; i<=numberOfNav;i++){
                    button.show();
                    button = button.next();
                }
            }

            setPrevNextNavView();
        });

        $(".right-dot-btn-pagination").off().on("click", function(){
            var lastVisible = $("ul.paginations").find("li.page-number-btn-pagination:visible:last");
            var lastValue = parseInt($("ul.paginations").find("li.page-number-btn-pagination:visible:last").children().first().val());
            var lastPageValue = $(".page-number-btn-pagination").length;
            var lastPage = $(".page-number-btn-pagination").last();
            var numberOfNav = parseInt($(".number-of-nav").val());

            if(lastValue + numberOfNav <= lastPageValue){
                hideAllPage();
                var button = lastVisible.next();
                for(var i=1; i<=numberOfNav;i++){
                    button.show();
                    button = button.next();
                }
            } else {
                hideAllPage();
                var button = lastPage;
                for(var i=1; i<=numberOfNav;i++){
                    button.show();
                    button = button.prev();
                }
            }

            setPrevNextNavView();
        });
    }

    function setPrevNextBtnEvent(){
        $(".first-btn-pagination").off().on("click", function(){
            if($(this).hasClass("disabled") == false){
                var periode = getPeriodeValue();
                var cifKey = $(".old-cif-key").val();
                var accountNumber = $(".old-account-number").val();
                var status = parseInt($(".old-status").val());
                var totalPerPage = parseInt($(".old-total-per-page").val());
                var pageNumber = 1;

                refreshTable(cifKey, periode, status, pageNumber, totalPerPage, getAllCheckedStatus());
                setActivePage(pageNumber);
                setNavPagination(pageNumber, 0);
                setPrevNextBtn();
            }
        });

        $(".prev-btn-pagination").off().on("click", function(){
            if($(this).hasClass("disabled") == false){
                var periode = getPeriodeValue();
                var cifKey = $(".old-cif-key").val();
                var accountNumber = $(".old-account-number").val();
                var status = parseInt($(".old-status").val());
                var totalPerPage = parseInt($(".old-total-per-page").val());
                //var pageNumber = parseInt($(".page-number-btn-pagination.active").children().first().val()) - 1;
				var pageNumber = gActivePage-1;
                refreshTable(cifKey, periode, status, pageNumber, totalPerPage, getAllCheckedStatus());
                setActivePage(pageNumber);
                setNavPagination(pageNumber, 1);
                setPrevNextBtn();
            }

        });

        $(".next-btn-pagination").off().on("click", function(){
            if($(this).hasClass("disabled") == false){
                var periode = getPeriodeValue();
                var cifKey = $(".old-cif-key").val();
                var accountNumber = $(".old-account-number").val();
                var status = parseInt($(".old-status").val());
                var totalPerPage = parseInt($(".old-total-per-page").val());
                //var pageNumber = parseInt($(".page-number-btn-pagination.active").children().first().val()) + 1;
				var pageNumber = gActivePage + 1;
                refreshTable(cifKey, periode, status, pageNumber, totalPerPage, getAllCheckedStatus());
                setActivePage(pageNumber);
                setNavPagination(pageNumber, 2);
                setPrevNextBtn();
            }
        });

        $(".last-btn-pagination").off().on("click", function(){
            if($(this).hasClass("disabled") == false){
                var periode = getPeriodeValue();
                var cifKey = $(".old-cif-key").val();
                var accountNumber = $(".old-account-number").val();
                var status = parseInt($(".old-status").val());
                var totalPerPage = parseInt($(".old-total-per-page").val());
                //var pageNumber = $(".page-number-btn-pagination").length;

                console.log('last page: '+gLastPage);
                refreshTable(cifKey, periode, status, gLastPage, totalPerPage, getAllCheckedStatus());
                setActivePage(gLastPage);
                setNavPagination(gLastPage, 0);
                setPrevNextBtn();
            }
        });
    }

    function getNumberOfChecked(){
        var n = 0;

        $.each($(".email-checkbox"), function(){
            if($(this).is(":checked"))
                n++;
        })

        return n;
    }

    function showDivInfo(){
        var n = getNumberOfChecked();
        //var total = $(".old-total-records").val();
        var total = getTotalRecordsWL();

				if(n>=0){
						if(n>0){
								var text = "All <strong>"+ n +"</strong> data on this page are selected. ";
								
								if(total>n){
				        		text += "<a onclick='selectAllData("+total+")' style='text-decoration:underline; cursor:pointer;'>Select all <strong>"+total+"</strong> data</a>";
				        }
				        
				        $("#div-info").html(text);
				        $("#div-info").slideDown(100);
						} else {	//n = 0
								if(total>n){
										var text = "All <strong>"+ n +"</strong> data on this page are selected. <a onclick='selectAllData("+total+")' style='text-decoration:underline; cursor:pointer;'>Select all <strong>"+total+"</strong> data</a>";
										
										$("#div-info").html(text);
				        		$("#div-info").slideDown(100);
								}
						}
				}
    }

    function hideDivInfo(){
        $("#div-info").slideUp(100);
        $("#parent-check").prop("checked", false);
        resetAllChecked();
    }

    function resetAllChecked(){
        $(".all-checked-status").val("0");
    }

    function setCheckBoxColumn(){
        var status = parseInt($(".old-status").val());

        <c:choose>
            <c:when test="${showResendButton==true}">
                $(".check-column").show();
            </c:when>
            <c:otherwise>
                $(".check-column").hide();
            </c:otherwise>
        </c:choose>         
    }

    function clearSelection(){
        hideDivInfo();
        $(".email-checkbox").prop("checked", false);
    }

    function selectAllData(total){
        $(".all-checked-status").val("1");

        var text = "All <strong>"+ total +"</strong> data are selected. <a onclick='clearSelection()' style='text-decoration:underline; cursor:pointer;'>Clear Selection</a>";
        $("#div-info").html(text);
    }

    function getAllCheckedStatus(){
        return parseInt($(".all-checked-status").val());
    }

    function resendAllButton(){
        <c:if test="${showResendButton==true}">
        $(".resend-all-btn").off().on("click", function(){
            ajaxRequestSendEmail("resend");
        })
        </c:if>
    }

    <c:if test="${showResendButton==true}">
    function ajaxRequestSendEmail(actionString){
        if(getAllCheckedStatus()==1){
            var cifKey = $(".old-cif-key").val();
            var accountNumber = $(".old-account-number").val();
            var periode = $(".old-periode").val();
            var status = parseInt($(".old-status").val());

            var actionUrl;
            if(actionString=="send"){
                actionUrl = "<c:url value='/konsolidasi-email/send-all' />";
            } else {
                actionUrl = "<c:url value='/konsolidasi-email/resend-all' />";
            }

            var data = {
                "cifKey" : cifKey,
                "periode" : periode,
                "status" : status
            }

            var commit = confirm("Apakah anda yakin akan mengirim semua halaman email ini?");

            if(commit){
                var ajax = $.ajax({
                            url: actionUrl,
                            timeout : 30000,
                            type : 'post',
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",
                            data : JSON.stringify(data)
                        });
                ajax.always(function(){

                });
                ajax.done(function (response) {
                    if (response.resultCode == 1000) {
                        alert(response.message);
                        var periode = getPeriodeValue();
                        var cifKey = $(".old-cif-key").val();
                        var accountNumber = $(".old-account-number").val();
                        var status = parseInt($(".old-status").val());
                        var totalPerPage = parseInt($(".old-total-per-page").val());
                        var pageNumber = $(".page-number-btn-pagination.active").children().first().val();

                        refreshTable(cifKey, periode, status, pageNumber, totalPerPage, getAllCheckedStatus());
                    }
                    else {
                        alert(response.message);
                    }
                });
                ajax.fail(function(xhr,status){
                    alert('Failed: '+status);
                });
            }
        } else {
            var listId = [];

            var actionUrl;
            if(actionString=="send"){
                actionUrl = "<c:url value='/konsolidasi-email/send' />";
            } else {
                actionUrl = "<c:url value='/konsolidasi-email/resend' />";
            }

            $.each($(".email-checkbox"), function(){
               if($(this).is(":checked")){
                   var val = parseInt($(this).prev().val());
                   listId.push(val);
               }
            });

            if(listId.length>0){
                var commit = confirm("Apakah anda yakin akan mengirim " + listId.length + " email ini?");

                if(commit){
                    var ajax = $.ajax({
                        url: actionUrl,
                        timeout : 30000,
                        type : 'post',
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        data : JSON.stringify(listId)
                    });
                    ajax.done(function (response) {
                        if (response.resultCode == 1000) {
                            alert(response.message);
                            var periode = getPeriodeValue();
                            var cifKey = $(".old-cif-key").val();
                            var accountNumber = $(".old-account-number").val();
                            var status = parseInt($(".old-status").val());
                            var totalPerPage = parseInt($(".old-total-per-page").val());
                            var pageNumber = $(".page-number-btn-pagination.active").children().first().val();

                            refreshTable(cifKey, accountNumber, periode, status, pageNumber, totalPerPage, getAllCheckedStatus());
                        }
                        else {
                            alert(response.message);
                        }
                    });
                    ajax.fail(function(xhr,status){
                        alert('Failed: '+status);
                    });
                }
            } else {
                //alert("Tidak ada email yang dipilih");
            }
        }
    }
    </c:if>
</script>
