<style type="text/css">
    .table-detail-sysparam tbody tr td{
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
<h4>System Parameter</h4>
<br />

<c:if test="${showNotif==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Permintaan perubahan data yang anda lakukan telah <strong>${requestStatus}</strong> oleh <strong>${reviewer}</strong>
    
    <script>
    		var ajax = $.ajax({
                    url: "<c:url value='/sys-param/set-notif-off'/>",
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

<c:if test="${showCancel==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Saat ini anda sedang memiliki request perubahan data yang masih belum diproses. Anda tidak dapat melakukan request perubahan data kecuali melakukan cancel request.
</div>
</c:if>

<c:if test="${showInfoRequest==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Saat ini sedang ada request.. Silahkan cek halaman review.
</div>
</c:if>

<div class="table-responsive">
    <table class="table table-striped table-bordered table-detail-sysparam">
        <thead>
            <th style="width:25px;">No.</th>
            <th style="text-align:center; width:100px;">Key</th>
            <th style="text-align:center; width:300px;">Key Description</th>
            <th colspan="2" style="text-align:center;">Value</th>
        </thead>
        <tbody>
            <!--Content SysParam-->
        </tbody>
    </table>
    
    <div style="float:right">
        <c:if test="${showSave==true}">
            <button type="button" class="btn btn-primary btn-sm save-sysparams-btn">Save All</button>&nbsp;
            <button style="display:inline;" type="button" class="btn btn-primary btn-sm cancel-sysparams-btn">Cancel All</button>
        </c:if>
        <c:if test="${showCancel==true}">
            <button type="button" class="btn btn-primary btn-sm cancel-request-btn">Cancel Request</button>
        </c:if>
    </div>
</div>
    
<script>
    $(document).ready(function(){
        document.title = "Manage System Parameters";
        
        refreshSysParamsTable();
    })
    
    function validateNumber() {
    	$(".validate-number").keydown(function (e) {
            // Allow: backspace, delete, tab, escape, enter, dot and comma
            if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190, 188]) !== -1 ||
                 // Allow: Ctrl+A
                (e.keyCode == 65 && e.ctrlKey === true) ||
                 // Allow: Ctrl+C
                (e.keyCode == 67 && e.ctrlKey === true) ||
                 // Allow: Ctrl+X
                (e.keyCode == 88 && e.ctrlKey === true) ||
                 // Allow: home, end, left, right
                (e.keyCode >= 35 && e.keyCode <= 39)) {
                     // let it happen, don't do anything
                     return;
            }
            // Ensure that it is a number and stop the keypress
            if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                e.preventDefault();
            }
        });	
    }
    
    function validateNumberOnly() {
    	$(".validate-number-only").keydown(function (e) {
            // Allow: backspace, delete, tab, escape, enter, dot, and comma
            if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
                 // Allow: Ctrl+A
                (e.keyCode == 65 && e.ctrlKey === true) ||
                 // Allow: Ctrl+C
                (e.keyCode == 67 && e.ctrlKey === true) ||
                 // Allow: Ctrl+X
                (e.keyCode == 88 && e.ctrlKey === true) ||
                 // Allow: home, end, left, right
                (e.keyCode >= 35 && e.keyCode <= 39)) {
                     // let it happen, don't do anything
                     return;
            }
            // Ensure that it is a number and stop the keypress
            if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                e.preventDefault();
            }
        });	
    }
    
    function validateEmail(email) {
    	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    	return re.test(email);
    }
    
    function checkingEmail(oldemail) {
    	$(".validate-email").blur(function (e) {
    		var email = $(".validate-email").val();
    		if (!validateEmail(email)) {
    			alert("Format Email Tidak Valid !");
    			$(".validate-email").val(oldemail);
    		}
    	});
    }
    
    function refreshSysParamsTable(){
        var ajax = $.ajax({
                        url: "<c:url value='/sys-param/get' />",            
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {            
            if (response.resultCode == 1000) {
                clearTBody();
                
                $.each(response.data, function(i,item){
                    
                    // var row = "<tr><td>" + (i+1) + "</td><td style='text-align:left;'>"
                    //         + item.key + "</td><td style='text-align:left;'>"
                    //         + item.keyDesc + "</td><td>"
                    //         + "<input type='hidden' name='input-key' value='"+ item.key +"' disabled/>" 
                    //         + "<input type='hidden' name='input-id' value='"+ item.id +"' disabled/>" 
                    //         + "<input type='hidden' name='input-old-value' class='' value='"+ item.value +"' disabled/>" 
                    //         + "<input type='text' style='width:85%; float:left;' class='form-control input-new-value' size='20' value='"+ item.value +"' disabled/>&nbsp;"
                    //         + "<button type='button' style='width:75px;' class='btn btn-default btn-sm edit-cancel-btn'>Edit</button>"
                    //         + "</td></tr>"; 

                    var classInput = '';
                    if (item.valueType == 1) {
                    	classInput = 'validate-number';
                    } else if (item.valueType == 2) {
                    	classInput = 'validate-email';
                    } else if (item.valueType == 3) {
                    	classInput = 'validate-number-only';
                    }
                    
                    var row = "<tr><td>" + (i+1) + "</td><td style='text-align:left;'>"
                            + (item.key!=null ? item.key : "") + "</td><td style='text-align:left;'>"
                            + (item.keyDesc!=null ? item.keyDesc : "") + "</td><td>"
                            + "<input type='hidden' name='input-type' value='"+ item.valueType +"' disabled/>"
                            + "<input type='hidden' name='input-key' value='"+ (item.key!=null ? item.key : "") +"' disabled/>" 
                            + "<input type='hidden' name='input-id' value='"+ item.id +"' disabled/>" 
                            + "<input type='hidden' name='input-old-value' class='' value='"+ (item.value!=null ? item.value : "") +"' disabled/>" 
                            + "<input type='text' class='form-control input-new-value " + classInput + "' size='20' maxlength='200' value='"+ (item.value!=null ? item.value : "") +"' id='"+item.key+"' disabled/></td><td style='width:80px'>"
                            + "<button type='button' style='width:80px;' class='btn btn-default btn-sm edit-cancel-btn'>Edit</button>"
                            + "</td></tr>"; 

                    $('.table-detail-sysparam tbody').append(row);
                    
                    if (item.valueType == 1) {
                    	validateNumber();
                    } else if (item.valueType == 2) {
                    	checkingEmail(item.value);
                    } else if (item.valueType == 3) {
                    	validateNumberOnly();
                    }
                });
                
                saveButtonEvent();
                cancelRequestButtonEvent();
                cancelAllButtonEvent()
                editCancelButtonEvent();
                
                <c:if test="${showInfoRequest==true || showCancel==true}">
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
                $this.parent().prev().children("input.input-new-value").prop("disabled", false);
                $this.text("Cancel");
            } else {
                var oldValue = $this.parent().prev().children("input.input-new-value").prev().val();
                $this.parent().prev().children("input.input-new-value").prop("disabled", true);
                $this.text("Edit");
                $this.parent().prev().children("input.input-new-value").val(oldValue);
            }
        })
    }
    
    function cancelAllButtonEvent(){
        $(".cancel-sysparams-btn").off().on("click", function(){
            $.each($("input.input-new-value"), function(i,item){
                if($(this).prop("disabled") == false){
                    var oldValue = $(this).prev().val();
                    $(this).prop("disabled", true);
                    $(this).val(oldValue);
                    $(this).parent().next().children("button.edit-cancel-btn").text("Edit");
                }                
            })
        })        
    }
    
    function validateNumberOnlyAll(){
    	var ret = true;
    	$.each($('.validate-number-only'), function(i, item){
    		var regex = /^[0-9]*$/;
    		var val = $(this).val();
    		if( !regex.test(val) ){    			
    			alert('Format inputan salah!!!');
    			var id = $(this).attr('id');
    			setTimeout(function() { $('#'+id).focus(); }, 100);
    		    ret = false;
    		    return ret;
    		}
    	});
    	return ret;
    }
    
    function validateNumberAll(){
    	var ret = true;
    	$.each($('.validate-number'), function(i, item){
    		var regex = /[,]{2,}|,$/;
    		var regex1 = /^[0-9,]*$/;
    		var val = $(this).val();
    		if( regex.test(val) || !regex1.test(val)){    			
    			alert('Format inputan salah!!!');
    			var id = $(this).attr('id');
    			setTimeout(function() { $('#'+id).focus(); }, 100);
    		    ret = false;
    		    return ret;
    		}
    	});
    	return ret;
    }
    
    function saveButtonEvent(){
        $(".save-sysparams-btn").off().on("click", function(){
        	if( !validateNumberOnlyAll() || !validateNumberAll() )
        		return;
            var systemParameters = [];
            
            $.each($("input.input-new-value"), function(i,item){
                if($(this).prop("disabled") == false){
                    var systemParameter = new Object();
                    var valType = $(this).prev().prev().prev().prev().val();
                    var key = $(this).prev().prev().prev().val();
                    var id = $(this).prev().prev().val();
                    var oldValue = $(this).prev().val();
                    var newValue = $(this).val();
                    
                    if (valType == 2) {
                    	//validate email
                    	if (!validateEmail(newValue)) {
                    		alert(key + ' : Email Parameter Tidak Valid');
                    		systemParameters = [];
                    		return false;
                    	}
                    }
                    
                    if (newValue.length > 200) {
                    	alert(key + ' : System Parameter Melebihi Batas Panjang 200 Karakter');
                    	systemParameters = [];
                    	return false;
                    }
                    
                    if(oldValue!=newValue){
                        systemParameter.newValue = newValue;
                        systemParameter.oldValue = oldValue;
                        systemParameter.id = id;
                        systemParameter.key = key;
                        systemParameters.push(systemParameter);
                    }                    
                }
            })

            if(systemParameters.length>0){                
                var status = confirm("Apakah anda yakin dengan perubahan data ini??");

                if(status){
                    var ajax = $.ajax({
                        url: "<c:url value='/sys-param/update'/>",
                        timeout: 30000,
                        type : "PUT",
                        contentType: "application/json",
                        dataType: "json",
                        data: JSON.stringify(systemParameters)
                    })

                    ajax.always(function(){

                    })

                    ajax.done(function (response) {
                        alert(response.message);
                        
                        //Reload page content --> current-page-link ada di dashboard.jsp
                        $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
                    })

                    ajax.fail(function(xhr, status){
                        console.log('Failed: '+status);
                    })
                }                 
            }
            else {
                refreshSysParamsTable();
            }
        })
    }

    function cancelRequestButtonEvent(){
        $(".cancel-request-btn").off().on("click", function(){
            var status = confirm("Apakah anda yakin akan membatalkan request perubahan data sebelumnya??");

            if(status){
                var ajax = $.ajax({
                    url: "<c:url value='/sys-param/cancel'/>",
                    timeout: 30000,
                    type : "PUT"
                });
                ajax.done(function (response) {
                    alert(response.message);

                    //Reload page content --> current-page-link ada di dashboard.jsp
                    $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
                });
                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                }) 
            }                    
        })
    }
    
    function clearTBody(){
        $('.table-detail-sysparam tbody tr').remove();
    }
    
</script>