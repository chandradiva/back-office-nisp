<style type="text/css">
    .table-detail-contentparam tbody tr td{
        vertical-align: middle;
        text-align: center;
    }
    
    input {
        width:150px;
    }

    #div-info {
        clear: both;
        text-align: left;
        padding: 10px;
    }
</style>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h4>Content Parameter</h4>
<br />

<c:if test="${showCancel==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Saat ini anda sedang memiliki request perubahan data yang masih belum diproses. Anda tidak dapat melakukan request perubahan data kecuali melakukan cancel request.
</div>
</c:if>

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

<c:if test="${showInfoRequest==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Saat ini sedang ada request.. Silahkan cek halaman review.
</div>
</c:if>

<div class="table-responsive">
    <table class="table table-striped table-bordered table-detail-contentparam">
        <thead>
            <th style="width:25px;">No.</th>
            <th style="text-align:center; width:100px;">Key</th>
            <th style="text-align:center; width:300px;">Key Description</th>
            <th colspan="2" style="text-align:center;">Value</th>
        </thead>
        <tbody>
            <!--Content ContentParam-->
        </tbody>
    </table>
    
    <div style="float:right">
        <c:if test="${showSave==true}">
            <button type="button" class="btn btn-primary btn-sm save-contentparams-btn">Save All</button>&nbsp;
            <button style="display:inline;" type="button" class="btn btn-primary btn-sm cancel-contentparams-btn">Cancel All</button>
        </c:if>
        <c:if test="${showCancel==true}">
            <button type="button" class="btn btn-primary btn-sm cancel-request-btn">Cancel Request</button>
        </c:if>
    </div>
</div>

<div id="divLoadingBo">
</div>
    
<script>
    $(document).ready(function(){
        document.title = "Manage Content Parameters";
        
        refreshContentParamsTable();
    })
    
    function refreshContentParamsTable(){
        var ajax = $.ajax({
                        url: "<c:url value='/content-param/get' />",            
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.done(function (response) {            
            if (response.resultCode == 1000) {
                clearTBody();
                
                $.each(response.data, function(i,item){
                    var row = "<tr><td>" + (i+1) + "</td><td style='text-align:left;'>"
                            + item.key + "</td><td style='text-align:left;'>"
                            + item.keyDesc + "</td><td>"
                            + "<input type='hidden' name='input-key' value='"+ item.key +"' disabled/>" 
                            + "<input type='hidden' name='input-id' value='"+ item.id +"' disabled/>" 
                            + "<input type='hidden' name='input-old-value' class='' value='"+ item.value +"' disabled/>"
                            /* + "<input type='text' class='form-control input-new-value' size='20' value='"+ item.value +"' disabled/> </td> <td style='width:80px'>" */
                            + "<textarea class='form-control input-new-value' rows='4' maxlength='500' disabled>" + item.value + "</textarea> </td> <td style='width:80px'>"
                            + "<button type='button' style='width:80px;' class='btn btn-default btn-sm edit-cancel-btn'>Edit</button>"
                            + "</td></tr>"; 

                    $('.table-detail-contentparam tbody').append(row);
                });
                
                saveButtonEvent();
                cancelRequestButtonEvent();
                cancelAllButtonEvent();
                editCancelButtonEvent();
                
                <c:if test="${showInfoRequest==true}">
                	$("button.edit-cancel-btn").prop("disabled",true);
                </c:if>
            } 
            else {
                alert(response.message);
            }
        });
        ajax.fail(function(xhr,status){
            alert('Failed: '+status);
        });
    }
    
    function editCancelButtonEvent(){
        $(".edit-cancel-btn").off().on("click", function(){
            var $this = $(this);
            
            if($this.text()=="Edit"){
                $this.parent().prev().children("textarea.input-new-value").prop("disabled", false);
                $this.text("Cancel");
            } else {
                var oldValue = $this.parent().prev().children("textarea.input-new-value").prev().val();
                $this.parent().prev().children("textarea.input-new-value").prop("disabled", true);
                $this.text("Edit");
                $this.parent().prev().children("textarea.input-new-value").val(oldValue);
            }
        })
    }
    
    function cancelAllButtonEvent(){
        $(".cancel-contentparams-btn").off().on("click", function(){
            $.each($("textarea.input-new-value"), function(i,item){
                if($(this).prop("disabled") == false){
                    var oldValue = $(this).prev().val();
                    $(this).prop("disabled", true);
                    $(this).val(oldValue);
                    $(this).parent().next().children("button.edit-cancel-btn").text("Edit");
                }                
            })
        })        
    }
    
    function saveButtonEvent(){
        $(".save-contentparams-btn").off().on("click", function(){
            var contentParameters = [];
		
            $.each($("textarea.input-new-value"), function(i,item){
                if($(this).prop("disabled") == false){
                    var contentParameter = new Object();
                    var id = $(this).prev().prev().val();
                    var key = $(this).prev().prev().prev().val();
                    var oldValue = $(this).prev().val();
                    var newValue = $(this).val();
                    
                    if (newValue.length > 500) {
                    	alert(key + ' : Content Parameter Melebihi Batas Panjang 500 Karakter');
                    	contentParameters = [];
                    	return false;
                    }
                    
                    if(oldValue!=newValue){
                        contentParameter.newValue = newValue;
                        contentParameter.oldValue = oldValue;
                        contentParameter.id = id;
                        contentParameter.key = key;
                        contentParameters.push(contentParameter);
                    }
                }
            })
            
            if(contentParameters.length>0){      
                var status = confirm("Apakah anda yakin dengan perubahan data ini??");

                if(status){
                    var ajax = $.ajax({
                        url: "<c:url value='/content-param/update'/>",
                        timeout: 30000,
                        type : "PUT",
                        contentType: "application/json",
                        dataType: "json",
                        data: JSON.stringify(contentParameters)
                    });

                    ajax.done(function (response) {
                        //Reload page content --> current-page-link ada di dashboard.jsp
                        $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
                        
                        alert(response.message);
                    })

                    ajax.fail(function(xhr, status){
                        console.log('Failed: '+status);
                    })
                }           
            }
            else {
                refreshContentParamsTable();
            }
        })
    }
    
    function cancelRequestButtonEvent(){
        $(".cancel-request-btn").off().on("click", function(){
            var status = confirm("Apakah anda yakin akan membatalkan request perubahan data sebelumnya??");

            if(status){
                var ajax = $.ajax({
                    url: "<c:url value='/content-param/cancel'/>",
                    timeout: 30000,
                    type : "PUT"
                });
                ajax.done(function (response) {
                    //Reload page content --> current-page-link ada di dashboard.jsp
                    $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />");
                    
                    alert(response.message);
                });
                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                }) 
            }                    
        })
    }

    function clearTBody(){
        $('.table-detail-contentparam tbody tr').remove();
    }
    
</script>