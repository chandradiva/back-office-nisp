<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
	.table-detail-contentparam tbody tr td{
        vertical-align: middle;
        text-align: center;
    }

    .approve-btn, .reject-btn {
    	display: none;
    }

    input.highlight {
    	background-color: rgb(255,255,0) !important;
    	font-weight: bold;
    }
    
    textarea.highlight {
    	background-color: rgb(255,255,0) !important;
    	font-weight: bold;
    }

    #div-info {
        clear: both;
        text-align: left;
        padding: 10px;
    }
</style>

<h4>Review Content Parameter</h4>
<br />

<c:if test="${showNotif==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Permintaan perubahan data yang anda lakukan telah <strong>${requestStatus}</strong> oleh <strong>${reviewer}</strong>
    
    <script>
    		var ajax = $.ajax({
                    url: "<c:url value='/content-param/set-notif-off'/>",
                    timeout: 30000,
                    type : "PUT"
                });
            ajax.done(function (response) {
                if(response.resultCode!=1000){
                		console.log("Error notif");
                }
            });
            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            })
    </script>
</div>
</c:if>

<c:if test="${showInfo==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Saat ini sedang tidak ada request perubahan data.
</div>
</c:if>

<c:if test="${showRequestInfo==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Perubahan data ini di-request oleh <strong>${requestBy}</strong>.
</div>
</c:if>

<div class="table-responsive">
    <table class="table table-striped table-bordered table-detail-contentparam">
        <thead>
            <th style="width:25px;">No.</th>
            <th style="text-align:center; width:80px;">Key</th>
            <th style="text-align:center; width:250px;">Key Description</th>
            <th style="text-align:center;">Old Value</th>
            <th style="text-align:center;">New Value</th>
        </thead>
        <tbody>
            <!--Content ContentParam-->
        </tbody>
    </table>
    
    <c:if test="${showButtons==true}">
    <div style="float:right">
        <button type="button" class="btn btn-primary btn-sm approve-btn" onclick="approveParams()">Approve</button>&nbsp;
        <button type="button" class="btn btn-primary btn-sm reject-btn" onclick="rejectParams()">Reject</button>
    </div>
    </c:if>
    
</div>

<div id="divLoadingBo">
</div>

<script>
	$(document).ready(function(){
        document.title = "Review Content Parameter";
        
        refreshTable();
    });

	function refreshTable(){
		setReviewBtn(false);
		
        var ajax = $.ajax({
                        url: "<c:url value='/content-param/get-unapproved' />",            
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {            
            if (response.resultCode == 1000) {
                clearTBody();
                
                $.each(response.data, function(i,item){
                    
                    if(item.oldValue==item.newValue){
                    	var row = "<tr><td>" + (i+1) + "</td><td style='text-align:left;'>"
                            + item.key + "</td><td style='text-align:left;'>"
                            + item.keyDesc + "</td><td>"
                            /* + "<input type='text' class='form-control old-value' value='"+ item.oldValue +"' disabled/>" */
                            + "<textarea class='form-control old-value' rows='4' disabled>" + item.oldValue + "</textarea>"
                            + "</td><td>"
                            /* + "<input type='text' class='form-control new-value' value='"+ item.newValue +"' disabled/></td></tr>"; */
                    		+ "<textarea class='form-control new-value' rows='4' disabled>" + item.newValue + "</textarea> </td> </tr>";
                    }
                    else {
                    	var row = "<tr><td>" + (i+1) + "</td><td style='text-align:left;'>"
                            + item.key + "</td><td style='text-align:left;'>"
                            + item.keyDesc + "</td><td>"
                            /* + "<input type='text' class='form-control old-value' value='"+ item.oldValue +"' disabled/>" */
                            + "<textarea class='form-control old-value' rows='4' disabled>" + item.oldValue + "</textarea>"
                            + "</td><td>"
                            /* + "<input type='text' class='form-control new-value highlight' value='"+ item.newValue +"' disabled/></td></tr>"; */
                    		+ "<textarea class='form-control new-value highlight' rows='4' disabled>" + item.newValue + "</textarea> </td> </tr>";
                    }
                    

                    $('.table-detail-contentparam tbody').append(row);
                });

                if($("textarea.highlight").length>0)
                	setReviewBtn(true);
            } 
            else {
                alert(response.message);
            }
        });
        ajax.fail(function(xhr,status){
            alert('Failed: '+status);
        });
    }

    function clearTBody(){
        $('.table-detail-contentparam tbody tr').remove();
    }

    function setReviewBtn(isShow){
    	if(isShow){
    		$(".approve-btn").show();
    		$(".reject-btn").show();
    	} else {
    		$(".approve-btn").hide();
    		$(".reject-btn").hide();
    	}
    }

    <c:if test="${showButtons==true}">
    function approveParams(){
    	var status = confirm("Apakah anda yakin akan menyetujui perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/content-param/approve'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
            	//Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />");
            	
                alert(response.message);                
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}    	
    }

    function rejectParams(){
    	var status = confirm("Apakah anda yakin akan menolak perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/content-param/reject'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
            	//Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />");
            	
                alert(response.message);
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}
    }
    </c:if>
</script>