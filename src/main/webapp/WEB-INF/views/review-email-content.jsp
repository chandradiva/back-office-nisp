<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
	#div-info {
        clear: both;
        text-align: left;
        padding: 10px;
    }

    table.table-detail-email-content th {
    	font-weight: bold;
    }

    table.table-detail-email-content tbody tr td {
    	vertical-align: middle;
    }

    textarea.highlight, input.highlight {
    	background-color: rgb(255,255,0) !important;
    }

    textarea.form-control {
        resize: vertical;
    }

    textarea.old-header, textarea.new-header {
        height: 40px;
        min-height: 40px;
        max-height: 75px;
    }

    textarea.old-pembuka1, textarea.new-pembuka1, textarea.new-pembuka2, textarea.old-pembuka2 {
        height: 75px;
        min-height: 75px;
        max-height: 100px;
    }

    textarea.old-body, textarea.new-body {
        height: 250px;
        min-height: 250px;
        max-height: 350px;
    }

    textarea.old-penutup, textarea.new-penutup {
        height: 75px;
        min-height: 75px;
        max-height: 100px;
    }

    textarea.old-footer, textarea.new-footer {
        height: 75px;
        min-height: 75px;
        max-height: 125px;
    }

    textarea.old-attachment, textarea.new-attachment {
        height: 35px;
        min-height: 35px;
        resize: none;
    }

    table.table-detail-email-content tbody tr td.open-button {
        width: 30px;
        display: none;
    }
    
    li.ec-tabmenu:active {
        margin:0px !important;
    }

	ul.nav-tabmenu {
		margin-top:10px;
	}

    ul.nav-tabmenu li {
        margin:0px;
    }

    div.tab-email-content {
        border-top-left-radius: 0px;
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
        border-bottom-left-radius: 4px;
    }
</style>

<ul class="nav nav-tabs nav-tabmenu">
    <li class="ec-tabmenu active"><a data-toggle="tab" href="#div-casa">CASA</a></li>
    <li class="ec-tabmenu"><a id="cc-tabmenu" data-toggle="tab" href="#div-cc">CC</a></li>
    <li class="ec-tabmenu"><a id="cc-tabmenu" data-toggle="tab" href="#div-casa-company">CASA Company</a></li>
</ul>

<div class="tab-content tab-email-content">
	<div id="div-casa" class="tab-pane fade in active">
		<h4>Review Email Content CASA</h4>
		<br />
		
        <c:if test="${showNotif==true}">
        <div id="div-info" class="alert alert-info">
            <strong>Info!</strong><br />
            Permintaan perubahan data yang anda lakukan telah <strong>${requestStatus}</strong> oleh <strong>${reviewer}</strong>
            
            <script>
                    var ajax = $.ajax({
                            url: "<c:url value='/email-content/set-notif-off'/>",
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
		    <table class="table table-striped table-bordered table-detail-email-content">
		        <thead>
		            <th style="text-align:center; width:150px;">Field</th>
		            <th style="text-align:center;">Old Value</th>
		            <th style="text-align:center;" colspan="2">New Value</th>
		        </thead>
		        <tbody>
			        <tr>
			        	<td>Header</td>
			        	<td><textarea class="form-control old-header" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-header" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Pembuka dengan Password</td>
			        	<td><textarea class="form-control old-pembuka1" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-pembuka1" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Pembuka tanpa Password</td>
			        	<td><textarea class="form-control old-pembuka2" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-pembuka2" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Body</td>
			        	<td><textarea class="form-control old-body" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-body" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Penutup</td>
			        	<td><textarea class="form-control old-penutup" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-penutup" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Footer</td>
			        	<td><textarea class="form-control old-footer" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-footer" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Attachment Multi Currency</td>
		                <td><input type="text" class="form-control old-attachment1" disabled /></td>
			        	<td><input type="text" class="form-control new-attachment1" disabled /></td>
		                <td class="open-button attachment1-open-btn">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${mcyId})">Open</button>
		                </td>
			        </tr>
		            <tr>
		                <td>Attachment Giro Non KRK</td>
		                <td><input type="text" class="form-control old-attachment2" disabled /></td>
		                <td><input type="text" class="form-control new-attachment2" disabled /></td>
		                <td class="open-button attachment2-open-btn">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro1Id})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Giro KRK Tanpa Tunggakan</td>
		                <td><input type="text" class="form-control old-attachment3" disabled /></td>
		                <td><input type="text" class="form-control new-attachment3" disabled /></td>
		                <td class="open-button attachment3-open-btn">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro2Id})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Giro KRK dengan Tunggakan</td>
		                <td><input type="text" class="form-control old-attachment4" disabled /></td>
		                <td><input type="text" class="form-control new-attachment4" disabled /></td>
		                <td class="open-button attachment4-open-btn">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro3Id})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Tabungan Harian</td>
		                <td><input type="text" class="form-control old-attachment5" disabled /></td>
		                <td><input type="text" class="form-control new-attachment5" disabled /></td>
		                <td class="open-button attachment5-open-btn">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro4Id})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Multi Currency Billingual</td>
		                <td><input type="text" class="form-control old-attachment6" disabled /></td>
		                <td><input type="text" class="form-control new-attachment6" disabled /></td>
		                <td class="open-button attachment6-open-btn">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${mcbId})">Open</button>
		                </td>
		            </tr>
		        </tbody>
		    </table>
		    
		    <c:if test="${showButtons==true}">
		    <div style="float:right">
		        	<button type="button" class="btn btn-primary btn-sm approve-btn" onclick="approve()">Approve</button>&nbsp;
		        	<button type="button" class="btn btn-primary btn-sm reject-btn" onclick="reject()">Reject</button>
		    </div>
		    </c:if>
		</div>
	</div>
	
	<div id="div-cc" class="tab-pane fade">
		<h4>Review Email Content CC</h4>
		<br />
		
        <c:if test="${showNotifCC==true}">
        <div id="div-info" class="alert alert-info">
            <strong>Info!</strong><br />
            Permintaan perubahan data yang anda lakukan telah <strong>${requestStatusCC}</strong> oleh <strong>${reviewerCC}</strong>
            
            <script>
                function setNotifCCOff(){
                    $("a#cc-tabmenu").on("click", function(){
                        var ajax = $.ajax({
                            url: "<c:url value='/email-content/set-notif-off-cc'/>",
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
                    })
                }
            </script>
        </div>
        </c:if>

		<c:if test="${showInfoCC==true}">
		<div id="div-info" class="alert alert-info">
		    <strong>Info!</strong><br />
		    Saat ini sedang tidak ada request perubahan data.
		</div>
		</c:if>
		
		<c:if test="${showRequestInfoCC==true}">
		<div id="div-info" class="alert alert-info">
		    <strong>Info!</strong><br />
		    Perubahan data ini di-request oleh <strong>${requestByCC}</strong>.
		</div>
		</c:if>
		
		<div class="table-responsive">
		    <table class="table table-striped table-bordered table-detail-email-content">
		        <thead>
		            <th style="text-align:center; width:150px;">Field</th>
		            <th style="text-align:center;">Old Value</th>
		            <th style="text-align:center;" colspan="2">New Value</th>
		        </thead>
		        <tbody>
			        <tr>
			        	<td>Header</td>
			        	<td><textarea class="form-control old-header-cc" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-header-cc" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Pembuka dengan Password</td>
			        	<td><textarea class="form-control old-pembuka1-cc" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-pembuka1-cc" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Pembuka tanpa Password</td>
			        	<td><textarea class="form-control old-pembuka2-cc" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-pembuka2-cc" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Body</td>
			        	<td><textarea class="form-control old-body-cc" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-body-cc" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Penutup</td>
			        	<td><textarea class="form-control old-penutup-cc" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-penutup-cc" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Footer</td>
			        	<td><textarea class="form-control old-footer-cc" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control new-footer-cc" disabled></textarea></td>
			        </tr>
                    <c:forEach items="${ccAttchs}" var="attch">
                        <tr>
                            <td>Attachment ${attch.productName}</td>
                            <td><input type="text" class="form-control old-attachment-${attch.attchCode}" disabled /></td>
                            <td><input type="text" class="form-control new-attachment-${attch.attchCode}" disabled /></td>
                            <td class="open-button attachment-${attch.attchCode}-open-btn">
                                <button type="button" class="btn btn-primary btn-sm" onclick="openFileCC('${attch.attchCode}')">Open</button>
                            </td>
                        </tr>
                    </c:forEach>
		        </tbody>
		    </table>
		    
		    <c:if test="${showButtonsCC==true}">
		    <div style="float:right">
		        	<button type="button" class="btn btn-primary btn-sm" onclick="approveCC()">Approve</button>&nbsp;
		        	<button type="button" class="btn btn-primary btn-sm" onclick="rejectCC()">Reject</button>
		    </div>
		    </c:if>
	</div>
</div>

<div id="div-casa-company" class="tab-pane fade">
		<h4>Review Email Content CASA Company</h4>
		<br />
		
        <c:if test="${showNotifCasaCompany==true}">
        <div id="div-info" class="alert alert-info">
            <strong>Info!</strong><br />
            Permintaan perubahan data yang anda lakukan telah <strong>${requestStatusCasaCompany}</strong> oleh <strong>${reviewerCasaCompany}</strong>
            
            <script>
                    var ajax = $.ajax({
                            url: "<c:url value='/email-content/set-notif-off-casa-company'/>",
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

		<c:if test="${showInfoCasaCompany==true}">
		<div id="div-info" class="alert alert-info">
		    <strong>Info!</strong><br />
		    Saat ini sedang tidak ada request perubahan data.
		</div>
		</c:if>
		
		<c:if test="${showRequestInfoCasaCompany==true}">
		<div id="div-info" class="alert alert-info">
		    <strong>Info!</strong><br />
		    Perubahan data ini di-request oleh <strong>${requestByCasaCompany}</strong>.
		</div>
		</c:if>
		
		<div class="table-responsive">
		    <table class="table table-striped table-bordered table-detail-email-content">
		        <thead>
		            <th style="text-align:center; width:150px;">Field</th>
		            <th style="text-align:center;">Old Value</th>
		            <th style="text-align:center;" colspan="2">New Value</th>
		        </thead>
		        <tbody>
			        <tr>
			        	<td>Header</td>
			        	<td><textarea class="form-control" id="old-header-casa-company" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control" id="new-header-casa-company" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Pembuka dengan Password</td>
			        	<td><textarea class="form-control" id="old-pembuka1-casa-company" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control" id="new-pembuka1-casa-company" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Pembuka tanpa Password</td>
			        	<td><textarea class="form-control" id="old-pembuka2-casa-company" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control" id="new-pembuka2-casa-company" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Body</td>
			        	<td><textarea class="form-control" id="old-body-casa-company" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control" id="new-body-casa-company" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Penutup</td>
			        	<td><textarea class="form-control" id="old-penutup-casa-company" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control" id="new-penutup-casa-company" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Footer</td>
			        	<td><textarea class="form-control" id="old-footer-casa-company" disabled></textarea></td>
			        	<td colspan="2"><textarea class="form-control" id="new-footer-casa-company" disabled></textarea></td>
			        </tr>
			        <tr>
			        	<td>Attachment Multi Currency</td>
		                <td><input type="text" class="form-control" id="old-attachment1-casa-company" disabled /></td>
			        	<td><input type="text" class="form-control" id="new-attachment1-casa-company" disabled /></td>
		                <td class="open-button attachment1-open-btn-casa-company">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${mcyIdCasaCompany})">Open</button>
		                </td>
			        </tr>
		            <tr>
		                <td>Attachment Giro Non KRK</td>
		                <td><input type="text" class="form-control" id="old-attachment2-casa-company" disabled /></td>
		                <td><input type="text" class="form-control" id="new-attachment2-casa-company" disabled /></td>
		                <td class="open-button attachment2-open-btn-casa-company">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro1IdCasaCompany})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Giro KRK Tanpa Tunggakan</td>
		                <td><input type="text" class="form-control" id="old-attachment3-casa-company" disabled /></td>
		                <td><input type="text" class="form-control" id="new-attachment3-casa-company" disabled /></td>
		                <td class="open-button attachment3-open-btn-casa-company">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro2IdCasaCompany})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Giro KRK dengan Tunggakan</td>
		                <td><input type="text" class="form-control" id="old-attachment4-casa-company" disabled /></td>
		                <td><input type="text" class="form-control" id="new-attachment4-casa-company" disabled /></td>
		                <td class="open-button attachment4-open-btn-casa-company">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro3IdCasaCompany})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Tabungan Harian</td>
		                <td><input type="text" class="form-control" id="old-attachment5-casa-company" disabled /></td>
		                <td><input type="text" class="form-control" id="new-attachment5-casa-company" disabled /></td>
		                <td class="open-button attachment5-open-btn-casa-company">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${giro4IdCasaCompany})">Open</button>
		                </td>
		            </tr>
		            <tr>
		                <td>Attachment Multi Currency Billingual</td>
		                <td><input type="text" class="form-control" id="old-attachment6-casa-company" disabled /></td>
		                <td><input type="text" class="form-control" id="new-attachment6-casa-company" disabled /></td>
		                <td class="open-button attachment6-open-btn-casa-company">
		                    <button type="button" class="btn btn-primary btn-sm" onclick="openFile(${mcbIdCasaCompany})">Open</button>
		                </td>
		            </tr>
		        </tbody>
		    </table>
		    
		    <c:if test="${showButtonsCasaCompany==true}">
		    <div style="float:right">
		        	<button type="button" class="btn btn-primary btn-sm approve-btn" onclick="approveCasaCompany()">Approve</button>&nbsp;
		        	<button type="button" class="btn btn-primary btn-sm reject-btn" onclick="rejectCasaCompany()">Reject</button>
		    </div>
		    </c:if>
		</div>
	</div>
	
<script>
	$(document).ready(function(){
        document.title = "Review Email Content";
        
        refreshTable();
        refreshTableCC();
        refreshTableCasaCompany();
        
        <c:if test="${showNotifCC==true}">
            setNotifCCOff();
        </c:if>
    });

    function refreshTable(){		
        var ajax = $.ajax({
                        url: "<c:url value='/email-content/get-unapproved' />",            
                        timeout : 30000,
                        type : 'get'
                    });

        ajax.done(function (response) {            
            if (response.resultCode == 1000) {
                clearTBody();
                
                var header = response.data.header!=null ? response.data.header.replace(/\r/g,"") : "";
                var oldHeader = response.data.oldHeader!=null ? response.data.oldHeader.replace(/\r/g,"") : "";
                var pembuka2 = response.data.pembuka2!=null ? response.data.pembuka2.replace(/\r/g,"") : "";
                var oldPembuka2 = response.data.oldPembuka2!=null ? response.data.oldPembuka2.replace(/\r/g,"") : "";
                var oldPembuka1 = response.data.oldPembuka1!=null ? response.data.oldPembuka1.replace(/\r/g,"") : "";
                var pembuka1 = response.data.pembuka1!=null ? response.data.pembuka1.replace(/\r/g,"") : "";
                var oldBody = response.data.oldBody!=null ? response.data.oldBody.replace(/\r/g,"") : "";
                var body = response.data.body!=null ? response.data.body.replace(/\r/g,"") : "";
                var penutup = response.data.penutup!=null ? response.data.penutup.replace(/\r/g,"") : "";
                var oldPenutup = response.data.oldPenutup!=null ? response.data.oldPenutup.replace(/\r/g,"") : "";
                var oldFooter = response.data.oldFooter!=null ? response.data.oldFooter.replace(/\r/g,"") : "";
                var footer = response.data.footer!=null ? response.data.footer.replace(/\r/g,"") : "";
                var attachment1 = response.data.attachment1.newAttachment!=null ? response.data.attachment1.newAttachment : "";
                var oldAttachment1 = response.data.attachment1.oldAttachment!=null ? response.data.attachment1.oldAttachment : "";
                var attachment2 = response.data.attachment2.newAttachment!=null ? response.data.attachment2.newAttachment : "";
                var oldAttachment2 = response.data.attachment2.oldAttachment!=null ? response.data.attachment2.oldAttachment : "";
                var attachment3 = response.data.attachment3.newAttachment!=null ? response.data.attachment3.newAttachment : "";
                var oldAttachment3 = response.data.attachment3.oldAttachment!=null ? response.data.attachment3.oldAttachment : "";
                var attachment4 = response.data.attachment4.newAttachment!=null ? response.data.attachment4.newAttachment : "";
                var oldAttachment4 = response.data.attachment4.oldAttachment!=null ? response.data.attachment4.oldAttachment : "";
                var attachment5 = response.data.attachment5.newAttachment!=null ? response.data.attachment5.newAttachment : "";
                var oldAttachment5 = response.data.attachment5.oldAttachment!=null ? response.data.attachment5.oldAttachment : "";
                var attachment6 = response.data.attachment6.newAttachment!=null ? response.data.attachment6.newAttachment : "";
                var oldAttachment6 = response.data.attachment6.oldAttachment!=null ? response.data.attachment6.oldAttachment : "";

                $(".old-header").html(oldHeader);
                $(".new-header").html(header);
                $(".old-pembuka1").html(oldPembuka1);
                $(".new-pembuka1").html(pembuka1);
                $(".old-pembuka2").html(oldPembuka2);
                $(".new-pembuka2").html(pembuka2);
                $(".old-body").html(oldBody);
                $(".new-body").html(body);
                $(".old-penutup").html(oldPenutup);
                $(".new-penutup").html(penutup);
                $(".old-footer").html(oldFooter);
                $(".new-footer").html(footer);
                $(".old-attachment1").val(getFileName(oldAttachment1));
                $(".new-attachment1").val(getFileName(attachment1));
                $(".old-attachment2").val(getFileName(oldAttachment2));
                $(".new-attachment2").val(getFileName(attachment2));
                $(".old-attachment3").val(getFileName(oldAttachment3));
                $(".new-attachment3").val(getFileName(attachment3));
                $(".old-attachment4").val(getFileName(oldAttachment4));
                $(".new-attachment4").val(getFileName(attachment4));
                $(".old-attachment5").val(getFileName(oldAttachment5));
                $(".new-attachment5").val(getFileName(attachment5));
                $(".old-attachment6").val(getFileName(oldAttachment6));
                $(".new-attachment6").val(getFileName(attachment6));

                if(oldHeader!=header)
                	$(".new-header").addClass("highlight");
                if(oldPembuka1!=pembuka1)
                	$(".new-pembuka1").addClass("highlight");
                if(oldPembuka2!=pembuka2)
                	$(".new-pembuka2").addClass("highlight");
                if(oldBody!=body)
                	$(".new-body").addClass("highlight");
                if(oldFooter!=footer)
                	$(".new-footer").addClass("highlight");
                if(oldPenutup!=penutup)
                	$(".new-penutup").addClass("highlight");

                if(response.data.attachment1.highlight==true){
                	$(".new-attachment1").addClass("highlight");

                    if(attachment1!="")
                        $("td.attachment1-open-btn").show();
                    else
                        $(".new-attachment1").parent().prop("colspan", 2);
                } else {
                    $(".new-attachment1").parent().prop("colspan", 2);
                }

                if(response.data.attachment2.highlight==true){
                    $(".new-attachment2").addClass("highlight");

                    if(attachment2.length!="")
                        $("td.attachment2-open-btn").show();
                    else
                        $(".new-attachment2").parent().prop("colspan", 2);
                } else {
                    $(".new-attachment2").parent().prop("colspan", 2);
                }

                if(response.data.attachment3.highlight==true){
                    $(".new-attachment3").addClass("highlight");

                    if(attachment3!="")
                        $("td.attachment3-open-btn").show();
                    else
                        $(".new-attachment3").parent().prop("colspan", 2);
                } else {
                    $(".new-attachment3").parent().prop("colspan", 2);
                }

                if(response.data.attachment4.highlight==true){
                    $(".new-attachment4").addClass("highlight");

                    if(attachment4!="")
                        $("td.attachment4-open-btn").show();
                    else
                        $(".new-attachment4").parent().prop("colspan", 2);
                } else {
                    $(".new-attachment4").parent().prop("colspan", 2);
                }

                if(response.data.attachment5.highlight==true){
                    $(".new-attachment5").addClass("highlight");

                    if(attachment5!="")
                        $("td.attachment5-open-btn").show();
                    else
                        $(".new-attachment5").parent().prop("colspan", 2);
                } else {
                    $(".new-attachment5").parent().prop("colspan", 2);
                }

                if(response.data.attachment6.highlight==true){
                    $(".new-attachment6").addClass("highlight");

                    if(attachment6!="")
                        $("td.attachment6-open-btn").show();
                    else
                        $(".new-attachment6").parent().prop("colspan", 2);
                } else {
                    $(".new-attachment6").parent().prop("colspan", 2);
                }
            } 
            else {
                console.log(response.message);
            }
        });
        ajax.fail(function(xhr,status){
            alert('Failed: '+status);
        });
    }
    
    function refreshTableCasaCompany(){		
        var ajax = $.ajax({
                        url: "<c:url value='/email-content/get-unapproved-casa-company' />",            
                        timeout : 30000,
                        type : 'get'
                    });

        ajax.done(function (response) {            
            if (response.resultCode == 1000) {
                clearTBody();
                
                var header = response.data.header!=null ? response.data.header.replace(/\r/g,"") : "";
                var oldHeader = response.data.oldHeader!=null ? response.data.oldHeader.replace(/\r/g,"") : "";
                var pembuka2 = response.data.pembuka2!=null ? response.data.pembuka2.replace(/\r/g,"") : "";
                var oldPembuka2 = response.data.oldPembuka2!=null ? response.data.oldPembuka2.replace(/\r/g,"") : "";
                var oldPembuka1 = response.data.oldPembuka1!=null ? response.data.oldPembuka1.replace(/\r/g,"") : "";
                var pembuka1 = response.data.pembuka1!=null ? response.data.pembuka1.replace(/\r/g,"") : "";
                var oldBody = response.data.oldBody!=null ? response.data.oldBody.replace(/\r/g,"") : "";
                var body = response.data.body!=null ? response.data.body.replace(/\r/g,"") : "";
                var penutup = response.data.penutup!=null ? response.data.penutup.replace(/\r/g,"") : "";
                var oldPenutup = response.data.oldPenutup!=null ? response.data.oldPenutup.replace(/\r/g,"") : "";
                var oldFooter = response.data.oldFooter!=null ? response.data.oldFooter.replace(/\r/g,"") : "";
                var footer = response.data.footer!=null ? response.data.footer.replace(/\r/g,"") : "";
                var attachment1 = response.data.attachment1.newAttachment!=null ? response.data.attachment1.newAttachment : "";
                var oldAttachment1 = response.data.attachment1.oldAttachment!=null ? response.data.attachment1.oldAttachment : "";
                var attachment2 = response.data.attachment2.newAttachment!=null ? response.data.attachment2.newAttachment : "";
                var oldAttachment2 = response.data.attachment2.oldAttachment!=null ? response.data.attachment2.oldAttachment : "";
                var attachment3 = response.data.attachment3.newAttachment!=null ? response.data.attachment3.newAttachment : "";
                var oldAttachment3 = response.data.attachment3.oldAttachment!=null ? response.data.attachment3.oldAttachment : "";
                var attachment4 = response.data.attachment4.newAttachment!=null ? response.data.attachment4.newAttachment : "";
                var oldAttachment4 = response.data.attachment4.oldAttachment!=null ? response.data.attachment4.oldAttachment : "";
                var attachment5 = response.data.attachment5.newAttachment!=null ? response.data.attachment5.newAttachment : "";
                var oldAttachment5 = response.data.attachment5.oldAttachment!=null ? response.data.attachment5.oldAttachment : "";
                var attachment6 = response.data.attachment6.newAttachment!=null ? response.data.attachment6.newAttachment : "";
                var oldAttachment6 = response.data.attachment6.oldAttachment!=null ? response.data.attachment6.oldAttachment : "";

                $("#old-header-casa-company").html(oldHeader);
                $("#new-header-casa-company").html(header);
                $("#old-pembuka1-casa-company").html(oldPembuka1);
                $("#new-pembuka1-casa-company").html(pembuka1);
                $("#old-pembuka2-casa-company").html(oldPembuka2);
                $("#new-pembuka2-casa-company").html(pembuka2);
                $("#old-body-casa-company").html(oldBody);
                $("#new-body-casa-company").html(body);
                $("#old-penutup-casa-company").html(oldPenutup);
                $("#new-penutup-casa-company").html(penutup);
                $("#old-footer-casa-company").html(oldFooter);
                $("#new-footer-casa-company").html(footer);
                $("#old-attachment1-casa-company").val(getFileName(oldAttachment1));
                $("#new-attachment1-casa-company").val(getFileName(attachment1));
                $("#old-attachment2-casa-company").val(getFileName(oldAttachment2));
                $("#new-attachment2-casa-company").val(getFileName(attachment2));
                $("#old-attachment3-casa-company").val(getFileName(oldAttachment3));
                $("#new-attachment3-casa-company").val(getFileName(attachment3));
                $("#old-attachment4-casa-company").val(getFileName(oldAttachment4));
                $("#new-attachment4-casa-company").val(getFileName(attachment4));
                $("#old-attachment5-casa-company").val(getFileName(oldAttachment5));
                $("#new-attachment5-casa-company").val(getFileName(attachment5));
                $("#old-attachment6-casa-company").val(getFileName(oldAttachment6));
                $("#new-attachment6-casa-company").val(getFileName(attachment6));

                if(oldHeader!=header)
                	$("#new-header-casa-company").addClass("highlight");
                if(oldPembuka1!=pembuka1)
                	$("#new-pembuka1-casa-company").addClass("highlight");
                if(oldPembuka2!=pembuka2)
                	$("#new-pembuka2-casa-company").addClass("highlight");
                if(oldBody!=body)
                	$("#new-body-casa-company").addClass("highlight");
                if(oldFooter!=footer)
                	$("#new-footer-casa-company").addClass("highlight");
                if(oldPenutup!=penutup)
                	$("#new-penutup-casa-company").addClass("highlight");

                if(response.data.attachment1.highlight==true){
                	$("#new-attachment1-casa-company").addClass("highlight");

                    if(attachment1!="")
                        $("td.attachment1-open-btn-casa-company").show();
                    else
                        $("#new-attachment1-casa-company").parent().prop("colspan", 2);
                } else {
                    $("#new-attachment1-casa-company").parent().prop("colspan", 2);
                }

                if(response.data.attachment2.highlight==true){
                    $("#new-attachment2-casa-company").addClass("highlight");

                    if(attachment2.length!="")
                        $("td.attachment2-open-btn-casa-company").show();
                    else
                        $("#new-attachment2-casa-company").parent().prop("colspan", 2);
                } else {
                    $("#new-attachment2-casa-company").parent().prop("colspan", 2);
                }

                if(response.data.attachment3.highlight==true){
                    $("#new-attachment3-casa-company").addClass("highlight");

                    if(attachment3!="")
                        $("td.attachment3-open-btn-casa-company").show();
                    else
                        $("#new-attachment3-casa-company").parent().prop("colspan", 2);
                } else {
                    $("#new-attachment3-casa-company").parent().prop("colspan", 2);
                }

                if(response.data.attachment4.highlight==true){
                    $("#new-attachment4-casa-company").addClass("highlight");

                    if(attachment4!="")
                        $("td.attachment4-open-btn-casa-company").show();
                    else
                        $("#new-attachment4-casa-company").parent().prop("colspan", 2);
                } else {
                    $("#new-attachment4-casa-company").parent().prop("colspan", 2);
                }

                if(response.data.attachment5.highlight==true){
                    $("#new-attachment5-casa-company").addClass("highlight");

                    if(attachment5!="")
                        $("td.attachment5-open-btn-casa-company").show();
                    else
                        $("#new-attachment5-casa-company").parent().prop("colspan", 2);
                } else {
                    $("#new-attachment5-casa-company").parent().prop("colspan", 2);
                }

                if(response.data.attachment6.highlight==true){
                    $("#new-attachment6-casa-company").addClass("highlight");

                    if(attachment6!="")
                        $("td.attachment6-open-btn-casa-company").show();
                    else
                        $("#new-attachment6-casa-company").parent().prop("colspan", 2);
                } else {
                    $("#new-attachment6-casa-company").parent().prop("colspan", 2);
                }
            } 
            else {
                console.log(response.message);
            }
        });
        ajax.fail(function(xhr,status){
            alert('Failed: '+status);
        });
    }
    
    function refreshTableCC(){		
        var ajax = $.ajax({
                        url: "<c:url value='/email-content/get-unapproved-cc' />",            
                        timeout : 30000,
                        type : 'get'
                    });

        ajax.done(function (response) {            
            if (response.resultCode == 1000) {
                clearTBody();
                
                var header = response.data.header!=null ? response.data.header.replace(/\r/g,"") : "";
                var oldHeader = response.data.oldHeader!=null ? response.data.oldHeader.replace(/\r/g,"") : "";
                var pembuka2 = response.data.pembuka2!=null ? response.data.pembuka2.replace(/\r/g,"") : "";
                var oldPembuka2 = response.data.oldPembuka2!=null ? response.data.oldPembuka2.replace(/\r/g,"") : "";
                var oldPembuka1 = response.data.oldPembuka1!=null ? response.data.oldPembuka1.replace(/\r/g,"") : "";
                var pembuka1 = response.data.pembuka1!=null ? response.data.pembuka1.replace(/\r/g,"") : "";
                var oldBody = response.data.oldBody!=null ? response.data.oldBody.replace(/\r/g,"") : "";
                var body = response.data.body!=null ? response.data.body.replace(/\r/g,"") : "";
                var penutup = response.data.penutup!=null ? response.data.penutup.replace(/\r/g,"") : "";
                var oldPenutup = response.data.oldPenutup!=null ? response.data.oldPenutup.replace(/\r/g,"") : "";
                var oldFooter = response.data.oldFooter!=null ? response.data.oldFooter.replace(/\r/g,"") : "";
                var footer = response.data.footer!=null ? response.data.footer.replace(/\r/g,"") : "";
                var attachments = response.data.attachments;
                
                $(".old-header-cc").html(oldHeader);
                $(".new-header-cc").html(header);
                $(".old-pembuka1-cc").html(oldPembuka1);
                $(".new-pembuka1-cc").html(pembuka1);
                $(".old-pembuka2-cc").html(oldPembuka2);
                $(".new-pembuka2-cc").html(pembuka2);
                $(".old-body-cc").html(oldBody);
                $(".new-body-cc").html(body);
                $(".old-penutup-cc").html(oldPenutup);
                $(".new-penutup-cc").html(penutup);
                $(".old-footer-cc").html(oldFooter);
                $(".new-footer-cc").html(footer);

                if(oldHeader!=header)
                	$(".new-header-cc").addClass("highlight");
                if(oldPembuka1!=pembuka1)
                	$(".new-pembuka1-cc").addClass("highlight");
                if(oldPembuka2!=pembuka2)
                	$(".new-pembuka2-cc").addClass("highlight");
                if(oldBody!=body)
                	$(".new-body-cc").addClass("highlight");
                if(oldFooter!=footer)
                	$(".new-footer-cc").addClass("highlight");
                if(oldPenutup!=penutup)
                	$(".new-penutup-cc").addClass("highlight");
                
                for(var attch of attachments){
                    $(".old-attachment-"+attch.attchCode).val(attch.oldAttachment!=null ? attch.oldAttachment : "");
                    $(".new-attachment-"+attch.attchCode).val(attch.newAttachment!=null ? attch.newAttachment : "");

                    if(attch.highlight==true){
                        $(".new-attachment-"+attch.attchCode).addClass("highlight");

                        if(attch.newAttachment!=null && attch.newAttachment!="")
                            $("td.attachment-"+attch.attchCode+"-open-btn").show();
                        else
                            $(".new-attachment-"+attch.attchCode).parent().prop("colspan", 2);
                    }
                }
            } 
            else {
                console.log(response.message);
            }
        });
        ajax.fail(function(xhr,status){
            alert('Failed: '+status);
        });
    }

    function clearTBody(){
        $('.table-detail-appparam tbody tr').remove();
    }

    function getFileName(fullpath){
        if(fullpath==null)
            return "-";

        return fullpath.replace(/^.*[\\\/]/, '');
    }

    function openFile(catId){
        window.open('<c:url value="/email-content/open-file?catId='+catId+'"/>','_blank');
    }

    function openFileCC(attchCode){
        window.open('<c:url value="/email-content/open-file-cc?attchCode='+attchCode+'"/>','_blank');
    }

    <c:if test="${showButtons==true}">
    function approve(){
    	var status = confirm("Apakah anda yakin akan menyetujui perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/email-content/approve'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
                alert(response.message);
                
                //Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}    	
    }
    

    function reject(){
    	var status = confirm("Apakah anda yakin akan menolak perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/email-content/reject'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
                alert(response.message);
                
                //Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}
    }
    </c:if>
    
    <c:if test="${showButtonsCasaCompany==true}">
    function approveCasaCompany(){
    	var status = confirm("Apakah anda yakin akan menyetujui perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/email-content/approve-casa-company'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
                alert(response.message);
                
                //Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}    	
    }
    

    function rejectCasaCompany(){
    	var status = confirm("Apakah anda yakin akan menolak perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/email-content/reject-casa-company'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
                alert(response.message);
                
                //Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}
    }
    </c:if>
    
    <c:if test="${showButtonsCC==true}">
    function approveCC(){
    	var status = confirm("Apakah anda yakin akan menyetujui perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/email-content/approve-cc'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
                alert(response.message);
                
                //Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />", function(){
                    $("#cc-tabmenu").trigger("click");
                }); 
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}    	
    }
    

    function rejectCC(){
    	var status = confirm("Apakah anda yakin akan menolak perubahan data ini??");

    	if(status){
    		var ajax = $.ajax({
                    url: "<c:url value='/email-content/reject-cc'/>",
                    timeout: 30000,
                    type : "PUT",
                    dataType: "json",
                });

            ajax.done(function (response) {
                alert(response.message);
                
                //Reload page content --> current-page-link ada di dashboard.jsp
                $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />", function(){
                    $("#cc-tabmenu").trigger("click");
                }); 
            });

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            }); 
    	}
    }
    </c:if>
</script>