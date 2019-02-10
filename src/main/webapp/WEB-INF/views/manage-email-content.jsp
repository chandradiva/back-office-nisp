<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    #div-body{
        width:80%;
        float:left;
        border-right: 1px solid rgb(224,224,224);
        padding-right: 20px;
        padding-top:10px;
    }

    #div-tag{
        width: 20%;
        float: right;
        padding-left: 20px;
    }

    table tr td textarea.form-control {
        width: 100%;
        resize: vertical;
    }

    table.page-email-content, table.page-email-content-cc {
        width: 100%;
    }

    textarea.email-header, textarea.email-header-cc {
        height: 40px;
        min-height: 40px;
        max-height: 75px;
    }

    textarea.email-pembuka1, textarea.email-pembuka2, textarea.email-pembuka1-cc, textarea.email-pembuka2-cc {
        height: 75px;
        min-height: 75px;
        max-height: 100px;
    }

    textarea.email-body, textarea.email-body-cc {
        height: 250px;
        min-height: 250px;
        max-height: 350px;
    }

    textarea.email-penutup, textarea.email-penutup-cc {
        height: 75px;
        min-height: 75px;
        max-height: 100px;
    }

    textarea.email-footer, textarea.email-footer-cc {
        height: 75px;
        min-height: 75px;
        max-height: 125px;
    }

    table.page-email-content tr td, table.page-email-content-cc tr td {
        padding: 5px 0px 5px 0px;
    }

    #div-tag p{
        font-family: "Courier New";
        font-size: 12px;
        color: rgb(99,101,97);
        margin-bottom: 0px;
    }

    .form-control,.input-group-addon {
        display: inline;
    }

    #action-button {
        margin-top:10px;
    }

    td.delete-button {
        display: none;
    }

    li.ec-tabmenu:active {
        margin:0px !important;
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

<div id="div-body">
    <input type="hidden" class="max-attachment-size" value="${maxSize}" />

    <ul class="nav nav-tabs nav-tabmenu">
        <li class="ec-tabmenu active"><a data-toggle="tab" href="#div-casa">CASA</a></li>
        <li class="ec-tabmenu"><a id="cc-tabmenu" data-toggle="tab" href="#div-cc">CC</a></li>
        <li class="ec-tabmenu"><a id="casa-company-tabmenu" data-toggle="tab" href="#div-casa-company">CASA Company</a></li>
    </ul>

    <div class="tab-content tab-email-content">
        <div id="div-casa" class="tab-pane fade in active">
            <h4>Email Content CASA</h4>
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
                Saat ini anda sedang memiliki request perubahan data yang masih belum diproses. Anda tidak dapat melakukan request perubahan data kecuali melakukan <a href="#cancel-button" style="font-weight:bold; text-decoration:underline;">cancel request</a> terlebih dahulu.
            </div>
            </c:if>
            
            <c:if test="${showInfoReq==true}">
            <div id="div-info" class="alert alert-info">
                <strong>Info!</strong><br />
                Saat ini sedang ada request perubahan data.. Silahkan cek halaman Review.
            </div>
            </c:if>

            <form id="form2" method="post" action="<c:url value='/email-content/save' />" enctype="multipart/form-data">
                <table class="page-email-content">
                    <tr style="margin-bottom:10px;">
                        <td colspan="2">
                            <h5>Header</h5>
                            <textarea class="form-control email-header-old" style="display:none;"></textarea>
                            <textarea placeholder="Header" class="form-control email-header"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Pembuka dengan Password</h5>
                            <textarea class="form-control email-pembuka1-old" style="display:none;"></textarea>
                            <textarea placeholder="Pembuka" class="form-control email-pembuka1"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Pembuka tanpa Password</h5>
                            <textarea class="form-control email-pembuka2-old" style="display:none;"></textarea>
                            <textarea placeholder="Pembuka" class="form-control email-pembuka2"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Body</h5>
                            <textarea class="form-control email-body-old" style="display:none;"></textarea>
                            <textarea placeholder="Body" class="form-control email-body"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Penutup</h5>
                            <textarea class="form-control email-penutup-old" style="display:none;"></textarea>
                            <textarea placeholder="Penutup" class="form-control email-penutup"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Footer</h5>
                            <textarea class="form-control email-footer-old" style="display:none;"></textarea>
                            <textarea placeholder="Footer" class="form-control email-footer"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Multi Currency</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control attachment-mcy-old" disabled />
                                    <input type="text" class="form-control attachment-mcy" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_mcy" name="file" class="input-browse" style="display: none;" accept=".pdf">
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment1-delete-btn" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachment(${mcyId})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro non KRK</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control attachment-giro1-old" disabled />
                                    <input type="text" class="form-control attachment-giro1" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro1" name="file" class="input-browse" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment2-delete-btn" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachment(${giro1Id})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro KRK tanpa Tunggakan</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control attachment-giro2-old" disabled />
                                    <input type="text" class="form-control attachment-giro2" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro2" name="file" class="input-browse" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment3-delete-btn" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachment(${giro2Id})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro KRK dengan Tunggakan</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control attachment-giro3-old" disabled />
                                    <input type="text" class="form-control attachment-giro3" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro3" name="file" class="input-browse" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment4-delete-btn" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachment(${giro3Id})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro Tabungan Harian</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control attachment-giro4-old" disabled />
                                    <input type="text" class="form-control attachment-giro4" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro4" name="file" class="input-browse" style="display: none; accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment5-delete-btn" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachment(${giro4Id})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Multi Currency Billingual</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control attachment-mcb-old" disabled />
                                    <input type="text" class="form-control attachment-mcb" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_mcb" name="file" class="input-browse" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment6-delete-btn" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachment(${mcbId})">Delete</button>
                        </td>
                    </tr>
                </table>
            </form>

            <div id="action-button">
                <c:if test="${showSave==true}">
                    <button type="button" class="btn btn-primary btn-sm save-email-btn">Save</button>&nbsp;
                    <button type="button" class="btn btn-primary btn-sm cancel-email-btn">Cancel</button>
                </c:if>

                <c:if test="${showCancel==true}">
                    <button type="button" id="cancel-button" class="btn btn-primary btn-sm cancel-request-btn">Cancel Request</button>
                </c:if>
            </div>
        </div>

        <div id="div-cc" class="tab-pane fade">
            <h4>Email Content Credit Card</h4>
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
                Saat ini anda sedang memiliki request perubahan data yang masih belum diproses. Anda tidak dapat melakukan request perubahan data kecuali melakukan <a href="#cancel-button-cc" style="font-weight:bold; text-decoration:underline;">cancel request</a> terlebih dahulu.
            </div>
            </c:if>
            
            <c:if test="${showInfoReqCC==true}">
            <div id="div-info" class="alert alert-info">
                <strong>Info!</strong><br />
                Saat ini sedang ada request perubahan data.. Silahkan cek halaman Review.
            </div>
            </c:if>
            
            <table class="page-email-content-cc">
                <tr style="margin-bottom:10px;">
                    <td colspan="2">
                        <h5>Header</h5>
                        <textarea class="form-control email-header-old-cc" style="display:none;"></textarea>
                        <textarea placeholder="Header" class="form-control email-header-cc"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <h5>Pembuka dengan Password</h5>
                        <textarea class="form-control email-pembuka1-old-cc" style="display:none;"></textarea>
                        <textarea placeholder="Pembuka" class="form-control email-pembuka1-cc"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <h5>Pembuka tanpa Password</h5>
                        <textarea class="form-control email-pembuka2-old-cc" style="display:none;"></textarea>
                        <textarea placeholder="Pembuka" class="form-control email-pembuka2-cc"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <h5>Body</h5>
                        <textarea class="form-control email-body-old-cc" style="display:none;"></textarea>
                        <textarea placeholder="Body" class="form-control email-body-cc"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <h5>Penutup</h5>
                        <textarea class="form-control email-penutup-old-cc" style="display:none;"></textarea>
                        <textarea placeholder="Penutup" class="form-control email-penutup-cc"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <h5>Footer</h5>
                        <textarea class="form-control email-footer-old-cc" style="display:none;"></textarea>
                        <textarea placeholder="Footer" class="form-control email-footer-cc"></textarea>
                    </td>
                </tr>
                <c:forEach items="${ccAttchs}" var="attch">
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment ${attch.productName} (BIN ${attch.bin}${binMap[attch.bin]})</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control attachment-${attch.attchCode}-old" disabled />
                                    <input type="text" class="form-control attachment-${attch.attchCode}" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_${attch.attchCode}" name="file" class="input-browse-cc" style="display: none;" accept=".pdf">
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment-delete-btn-${attch.attchCode}" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachment('${attch.attchCode}')">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <div id="action-button">
                <c:if test="${showSaveCC==true}">
                    <button type="button" class="btn btn-primary btn-sm" onclick="saveEmailCC()">Save</button>&nbsp;
                    <button type="button" class="btn btn-primary btn-sm" onclick="cancelEmailCC()">Cancel</button>
                </c:if>

                <c:if test="${showCancelCC==true}">
                    <button type="button" id="cancel-button-cc" class="btn btn-primary btn-sm" onclick="cancelRequestCC()">Cancel Request</button>
                </c:if>
            </div>
        </div>
        
        <div id="div-casa-company" class="tab-pane fade">
            <h4>Email Content CASA Company</h4>
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
                Saat ini anda sedang memiliki request perubahan data yang masih belum diproses. Anda tidak dapat melakukan request perubahan data kecuali melakukan <a href="#cancel-button-casa-company" style="font-weight:bold; text-decoration:underline;">cancel request</a> terlebih dahulu.
            </div>
            </c:if>
            
            <c:if test="${showInfoReqCasaCompany==true}">
            <div id="div-info" class="alert alert-info">
                <strong>Info!</strong><br />
                Saat ini sedang ada request perubahan data.. Silahkan cek halaman Review.
            </div>
            </c:if>

            <table class="page-email-content">
                    <tr style="margin-bottom:10px;">
                        <td colspan="2">
                            <h5>Header</h5>
                            <textarea class="form-control" id="email-header-old-casa-company" style="display:none;"></textarea>
                            <textarea placeholder="Header" class="form-control" id="email-header-casa-company"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Pembuka dengan Password</h5>
                            <textarea class="form-control" id="email-pembuka1-old-casa-company" style="display:none;"></textarea>
                            <textarea placeholder="Pembuka" class="form-control" id="email-pembuka1-casa-company"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Pembuka tanpa Password</h5>
                            <textarea class="form-control" id="email-pembuka2-old-casa-company" style="display:none;"></textarea>
                            <textarea placeholder="Pembuka" class="form-control" id="email-pembuka2-casa-company"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Body</h5>
                            <textarea class="form-control" id="email-body-old-casa-company" style="display:none;"></textarea>
                            <textarea placeholder="Body" class="form-control" id="email-body-casa-company"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Penutup</h5>
                            <textarea class="form-control" id="email-penutup-old-casa-company" style="display:none;"></textarea>
                            <textarea placeholder="Penutup" class="form-control" id="email-penutup-casa-company"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <h5>Footer</h5>
                            <textarea class="form-control" id="email-footer-old-casa-company" style="display:none;"></textarea>
                            <textarea placeholder="Footer" class="form-control" id="email-footer-casa-company"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Multi Currency</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control" id="attachment-mcy-old-casa-company" disabled />
                                    <input type="text" class="form-control" id="attachment-mcy-casa-company" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_mcy_casa_company" name="file" class="input-browse-casa-company" style="display: none;" accept=".pdf">
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment1-delete-btn-casa-company" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachmentCasaCompany(${mcyIdCasaCompany})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro non KRK</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control" id="attachment-giro1-old-casa-company" disabled />
                                    <input type="text" class="form-control" id="attachment-giro1-casa-company" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro1_casa_company" name="file" class="input-browse-casa-company" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment2-delete-btn-casa-company" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachmentCasaCompany(${giro1IdCasaCompany})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro KRK tanpa Tunggakan</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control" id="attachment-giro2-old-casa-company" disabled />
                                    <input type="text" class="form-control" id="attachment-giro2-casa-company" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro2_casa_company" name="file" class="input-browse-casa-company" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment3-delete-btn-casa-company" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachmentCasaCompany(${giro2IdCasaCompany})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro KRK dengan Tunggakan</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control" id="attachment-giro3-old-casa-company" disabled />
                                    <input type="text" class="form-control" id="attachment-giro3-casa-company" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro3_casa_company" name="file" class="input-browse-casa-company" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment4-delete-btn-casa-company" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachmentCasaCompany(${giro3IdCasaCompany})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Giro Tabungan Harian</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control" id="attachment-giro4-old-casa-company" disabled />
                                    <input type="text" class="form-control" id="attachment-giro4-casa-company" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_giro4_casa_company" name="file" class="input-browse-casa-company" style="display: none; accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment5-delete-btn-casa-company" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachmentCasaCompany(${giro4IdCasaCompany})">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 325px;">
                            <h5>Attachment Multi Currency Billingual</h5>
                            <div class="input-group">
                                <div class="input-group-addon" style="padding:0; border:none;">
                                    <input type="hidden" class="form-control" id="attachment-mcb-old-casa-company" disabled />
                                    <input type="text" class="form-control" id="attachment-mcb-casa-company" disabled />
                                    <label class="btn btn-default btn-file">
                                        Browse <input type="file" id="file_mcb_casa_company" name="file" class="input-browse-casa-company" style="display: none;" accept=".pdf" multiple>
                                    </label>
                                </div>
                            </div>
                        </td>
                        <td class="delete-button attachment6-delete-btn-casa-company" style="padding-top: 3em;">
                            <button type="button" class="btn btn-primary btn-sm" onclick="deleteAttachmentCasaCompany(${mcbIdCasaCompany})">Delete</button>
                        </td>
                    </tr>
                </table>

            <div id="action-button">
                <c:if test="${showSave==true}">
                    <button type="button" class="btn btn-primary btn-sm" onclick="saveEmailCasaCompany()">Save</button>&nbsp;
                    <button type="button" class="btn btn-primary btn-sm" onclick="cancelEmailCasaCompany()">Cancel</button>
                </c:if>

                <c:if test="${showCancelCasaCompany==true}">
                    <button type="button" id="cancel-button-casa-company" class="btn btn-primary btn-sm" onclick="cancelRequestCasaCompany()" >Cancel Request</button>
                </c:if>
            </div>
        </div>
    </div>

</div>

<div id="div-tag">
    <h4 style="margin-bottom:30px;">Tag List</h4>

	<p>&lt;account-number&gt;</p>
    <p>&lt;customer-name&gt;</p>
    <p>&lt;password&gt;</p>
    <p>&lt;period&gt;</p>    
    <p>&lt;product-name&gt;</p>
    <p>&lt;today&gt;</p>
</div>

<script>
    $(document).ready(function(){

        document.title = "Email Content";

        cancelEmailButton();
        refreshEmailContentTextArea();
        refreshEmailContentTextAreaCC();
        refreshEmailContentTextAreaCasaCompany();
        validateFile();

        <c:if test="${showNotifCC==true}">
            setNotifCCOff();
        </c:if>
    })

    function refreshEmailContentTextArea(){
        var ajax = $.ajax({
                        url: "<c:url value='/email-content/get' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                setTextArea(response.data);
                saveEmailButton();
                cancelRequestButtonEvent();
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function refreshEmailContentTextAreaCasaCompany(){
        var ajax = $.ajax({
                        url: "<c:url value='/email-content/get-casa-company' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                setTextAreaCasaCompany(response.data);
                //saveEmailButton();
                //cancelRequestButtonEvent();
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }
    
    function refreshEmailContentTextAreaCC(){
        var ajax = $.ajax({
                        url: "<c:url value='/email-content/get-cc' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                setTextAreaCC(response.data);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function setTextArea(emailContent){
        var inputId = "<input type='hidden' class='email-id' value='"+ (emailContent!=null ? (emailContent.id !=null ? emailContent.id : -1) : -1) +"' />";
        $("#div-body").append(inputId);

        if(emailContent!=null){
            $(".email-header-old").val(emailContent.header);
            $(".email-pembuka1-old").val(emailContent.pembuka1);
            $(".email-pembuka2-old").val(emailContent.pembuka2);
            $(".email-body-old").val(emailContent.body);
            $(".email-penutup-old").val(emailContent.penutup);
            $(".email-footer-old").val(emailContent.footer);
            $(".attachment-mcy-old").val(getFileName(emailContent.attachment1.oldAttachment));
            $(".attachment-giro1-old").val(getFileName(emailContent.attachment2.oldAttachment));
            $(".attachment-giro2-old").val(getFileName(emailContent.attachment3.oldAttachment));
            $(".attachment-giro3-old").val(getFileName(emailContent.attachment4.oldAttachment));
            $(".attachment-giro4-old").val(getFileName(emailContent.attachment5.oldAttachment));
            $(".attachment-mcb-old").val(getFileName(emailContent.attachment6.oldAttachment));

            $(".email-header").val(emailContent.header);
            $(".email-pembuka1").val(emailContent.pembuka1);
            $(".email-pembuka2").val(emailContent.pembuka2);
            $(".email-body").val(emailContent.body);
            $(".email-penutup").val(emailContent.penutup);
            $(".email-footer").val(emailContent.footer);
            $(".attachment-mcy").val(getFileName(emailContent.attachment1.oldAttachment));
            $(".attachment-giro1").val(getFileName(emailContent.attachment2.oldAttachment));
            $(".attachment-giro2").val(getFileName(emailContent.attachment3.oldAttachment));
            $(".attachment-giro3").val(getFileName(emailContent.attachment4.oldAttachment));
            $(".attachment-giro4").val(getFileName(emailContent.attachment5.oldAttachment));
            $(".attachment-mcb").val(getFileName(emailContent.attachment6.oldAttachment));

            if($("input.attachment-mcy").val()!="")
                $("td.attachment1-delete-btn").show();
            if($("input.attachment-giro1").val()!="")
                $("td.attachment2-delete-btn").show();
            if($("input.attachment-giro2").val()!="")
                $("td.attachment3-delete-btn").show();
            if($("input.attachment-giro3").val()!="")
                $("td.attachment4-delete-btn").show();
            if($("input.attachment-giro4").val()!="")
                $("td.attachment5-delete-btn").show();
            if($("input.attachment-mcb").val()!="")
                $("td.attachment6-delete-btn").show();

            <c:if test="${showInfo==true || showInfoReq==true}">
                $(".email-header").prop("disabled", true);
                $(".email-pembuka1").prop("disabled", true);
                $(".email-pembuka2").prop("disabled", true);
                $(".email-body").prop("disabled", true);
                $(".email-penutup").prop("disabled", true);
                $(".email-footer").prop("disabled", true);
                $(".input-browse").prop("disabled", true);
            </c:if>
        }       
    }

    function setTextAreaCasaCompany(emailContent){
        var inputId = "<input type='hidden' id='email-id-casa-company' value='"+ (emailContent!=null ? (emailContent.id !=null ? emailContent.id : -1) : -1) +"' />";
        $("#div-body").append(inputId);

        if(emailContent!=null){
            $("#email-header-old-casa-company").val(emailContent.header);
            $("#email-pembuka1-old-casa-company").val(emailContent.pembuka1);
            $("#email-pembuka2-old-casa-company").val(emailContent.pembuka2);
            $("#email-body-old-casa-company").val(emailContent.body);
            $("#email-penutup-old-casa-company").val(emailContent.penutup);
            $("#email-footer-old-casa-company").val(emailContent.footer);
            $("#attachment-mcy-old-casa-company").val(getFileName(emailContent.attachment1.oldAttachment));
            $("#attachment-giro1-old-casa-company").val(getFileName(emailContent.attachment2.oldAttachment));
            $("#attachment-giro2-old-casa-company").val(getFileName(emailContent.attachment3.oldAttachment));
            $("#attachment-giro3-old-casa-company").val(getFileName(emailContent.attachment4.oldAttachment));
            $("#attachment-giro4-old-casa-company").val(getFileName(emailContent.attachment5.oldAttachment));
            $("#attachment-mcb-old-casa-company").val(getFileName(emailContent.attachment6.oldAttachment));

            $("#email-header-casa-company").val(emailContent.header);
            $("#email-pembuka1-casa-company").val(emailContent.pembuka1);
            $("#email-pembuka2-casa-company").val(emailContent.pembuka2);
            $("#email-body-casa-company").val(emailContent.body);
            $("#email-penutup-casa-company").val(emailContent.penutup);
            $("#email-footer-casa-company").val(emailContent.footer);
            $("#attachment-mcy-casa-company").val(getFileName(emailContent.attachment1.oldAttachment));
            $("#attachment-giro1-casa-company").val(getFileName(emailContent.attachment2.oldAttachment));
            $("#attachment-giro2-casa-company").val(getFileName(emailContent.attachment3.oldAttachment));
            $("#attachment-giro3-casa-company").val(getFileName(emailContent.attachment4.oldAttachment));
            $("#attachment-giro4-casa-company").val(getFileName(emailContent.attachment5.oldAttachment));
            $("#attachment-mcb-casa-company").val(getFileName(emailContent.attachment6.oldAttachment));

            if($("input#attachment-mcy-casa-company").val()!="")
                $("td.attachment1-delete-btn-casa-company").show();
            if($("input#attachment-giro1-casa-company").val()!="")
                $("td.attachment2-delete-btn-casa-company").show();
            if($("input#attachment-giro2-casa-company").val()!="")
                $("td.attachment3-delete-btn-casa-company").show();
            if($("input#attachment-giro3-casa-company").val()!="")
                $("td.attachment4-delete-btn-casa-company").show();
            if($("input#attachment-giro4-casa-company").val()!="")
                $("td.attachment5-delete-btn-casa-company").show();
            if($("input#attachment-mcb-casa-company").val()!="")
                $("td.attachment6-delete-btn-casa-company").show();

            <c:if test="${showInfoCasaCompany==true || showInfoReqCasaCompany==true}">
                $("#email-header-casa-company").prop("disabled", true);
                $("#email-pembuka1-casa-company").prop("disabled", true);
                $("#email-pembuka2-casa-company").prop("disabled", true);
                $("#email-body-casa-company").prop("disabled", true);
                $("#email-penutup-casa-company").prop("disabled", true);
                $("#email-footer-casa-company").prop("disabled", true);
                $(".input-browse-casa-company").prop("disabled", true);
            </c:if>
        }       
    }
    
    function setTextAreaCC(emailContent){
        var inputId ="<input type='hidden' class='email-id-cc' value='"+ (emailContent!=null ? emailContent.id !=null ? emailContent.id : -1 : -1) +"' />";
        $("#div-body").append(inputId);
                
        if(emailContent!=null){
            $(".email-header-old-cc").val(emailContent.header);
            $(".email-pembuka1-old-cc").val(emailContent.pembuka1);
            $(".email-pembuka2-old-cc").val(emailContent.pembuka2);
            $(".email-body-old-cc").val(emailContent.body);
            $(".email-penutup-old-cc").val(emailContent.penutup);
            $(".email-footer-old-cc").val(emailContent.footer);
            
            $.each(emailContent.attachments, function(i, ec){
                $(".attachment-"+ec.attchCode+"-old").val(getFileName(ec.oldAttachment));
                $(".attachment-"+ec.attchCode).val(getFileName(ec.oldAttachment));

                if($("input.attachment-"+ec.attchCode).val()!="")
                    $("td.attachment-delete-btn-"+ec.attchCode).show();
            })

            $(".email-header-cc").val(emailContent.header);
            $(".email-pembuka1-cc").val(emailContent.pembuka1);
            $(".email-pembuka2-cc").val(emailContent.pembuka2);
            $(".email-body-cc").val(emailContent.body);
            $(".email-penutup-cc").val(emailContent.penutup);
            $(".email-footer-cc").val(emailContent.footer);

            <c:if test="${showInfoCC==true || showInfoReqCC==true}">
                $(".email-header-cc").prop("disabled", true);
                $(".email-pembuka1-cc").prop("disabled", true);
                $(".email-pembuka2-cc").prop("disabled", true);
                $(".email-body-cc").prop("disabled", true);
                $(".email-penutup-cc").prop("disabled", true);
                $(".email-footer-cc").prop("disabled", true);
                $(".input-browse-cc").prop("disabled", true);
            </c:if>
        }       
    }

    function getFileName(fullpath){
        if(fullpath==null)
            return "";

        return fullpath.replace(/^.*[\\\/]/, '');
    }

    function deleteAttachment($catId){
        if($catId == ${mcyId}){
            $("input.attachment-mcy").val("");
            resetFileField($("input#file_mcy"));
        }
        else if($catId == ${giro1Id}){
            $("input.attachment-giro1").val("");
            resetFileField($("input#file_giro1"));
        }
        else if($catId == ${giro2Id}){
            $("input.attachment-giro2").val("");
            resetFileField($("input#file_giro2"));
        }
        else if($catId == ${giro3Id}){
            $("input.attachment-giro3").val("");
            resetFileField($("input#file_giro3"));
        }
        else if($catId == ${giro4Id}){
            $("input.attachment-giro4").val("");
            resetFileField($("input#file_giro4"));
        }
        else if($catId == ${mcbId}){
            $("input.attachment-mcb").val("");
            resetFileField($("input#file_mcb"));
        }
        else {
            $("input.attachment-"+$catId).val("");
            resetFileField($("input#file_"+$catId));
        }
    }
    
    function deleteAttachmentCasaCompany($catId){
        if($catId == ${mcyIdCasaCompany}){
            $("input#attachment-mcy-casa-company").val("");
            resetFileField($("input#file_mcy_casa_company"));
        }
        else if($catId == ${giro1IdCasaCompany}){
            $("input#attachment-giro1-casa-company").val("");
            resetFileField($("input#file_giro1_casa_company"));
        }
        else if($catId == ${giro2IdCasaCompany}){
            $("input#attachment-giro2-casa-company").val("");
            resetFileField($("input#file_giro2_casa_company"));
        }
        else if($catId == ${giro3IdCasaCompany}){
            $("input#attachment-giro3-casa-company").val("");
            resetFileField($("input#file_giro3_casa_company"));
        }
        else if($catId == ${giro4IdCasaCompany}){
            $("input#attachment-giro4-casa-company").val("");
            resetFileField($("input#file_giro4_casa_company"));
        }
        else if($catId == ${mcbIdCasaCompany}){
            $("input#attachment-mcb-casa-company").val("");
            resetFileField($("input#file_mcb_casa_company"));
        }
        else {
            $("input#attachment-"+$catId).val("");
            resetFileField($("input#file_"+$catId));
        }
    }

    function cancelEmailButton(){
        <c:if test="${showSave==true}">
        $(".cancel-email-btn").off().on("click", function(){
            $(".email-header").val($(".email-header-old").val());
            $(".email-pembuka1").val($(".email-pembuka1-old").val());
            $(".email-pembuka2").val($(".email-pembuka2-old").val());
            $(".email-body").val($(".email-body-old").val());
            $(".email-penutup").val($(".email-penutup-old").val());
            $(".email-footer").val($(".email-footer-old").val());
            $(".attachment-mcy").val($(".attachment-mcy-old").val());
            $(".attachment-giro1").val($(".attachment-giro1-old").val());
            $(".attachment-giro2").val($(".attachment-giro2-old").val());
            $(".attachment-giro3").val($(".attachment-giro3-old").val());
            $(".attachment-giro4").val($(".attachment-giro4-old").val());
            $(".attachment-mcb").val($(".attachment-mcb-old").val());
            clearFile($("input.input-browse"));
        })
        </c:if>
    }

    function cancelEmailButton(){
        <c:if test="${showSaveCasaCompany==true}">
        $("#cancel-email-btn-casa-company").off().on("click", function(){
            $("#email-header-casa-company").val($("#email-header-old-casa-company").val());
            $("#email-pembuka1-casa-company").val($("#email-pembuka1-old-casa-company").val());
            $("#email-pembuka2-casa-company").val($("#email-pembuka2-old-casa-company").val());
            $("#email-body-casa-company").val($("#email-body-old-casa-company").val());
            $("#email-penutup-casa-company").val($("#email-penutup-old-casa-company").val());
            $("#email-footer-casa-company").val($("#email-footer-old-casa-company").val());
            $("#attachment-mcy-casa-company").val($("#attachment-mcy-old-casa-company").val());
            $("#attachment-giro1-casa-company").val($("#attachment-giro1-old-casa-company").val());
            $("#attachment-giro2-casa-company").val($("#attachment-giro2-old-casa-company").val());
            $("#attachment-giro3-casa-company").val($("#attachment-giro3-old-casa-company").val());
            $("#attachment-giro4-casa-company").val($("#attachment-giro4-old-casa-company").val());
            $("#attachment-mcb-casa-company").val($("#attachment-mcb-old-casa-company").val());
            clearFile($("input.input-browse-casa-company"));
        })
        </c:if>
    }
    
    function cancelEmailCC(){
        $(".email-header-cc").val($(".email-header-old-cc").val());
        $(".email-pembuka1-cc").val($(".email-pembuka1-old-cc").val());
        $(".email-pembuka2-cc").val($(".email-pembuka2-old-cc").val());
        $(".email-body-cc").val($(".email-body-old-cc").val());
        $(".email-penutup-cc").val($(".email-penutup-old-cc").val());
        $(".email-footer-cc").val($(".email-footer-old-cc").val());

        <c:forEach items="${ccAttchs}" var="attch">
            $(".attachment-${attch.attchCode}").val($(".attachment-${attch.attchCode}-old").val());
        </c:forEach>
        clearFile($("input.input-browse-cc"));
    }

    function saveEmailButton(){
        <c:if test="${showSave==true}">
        $(".save-email-btn").off().on("click", function(){
            var header = $(".email-header").val();
            var pembuka1 = $(".email-pembuka1").val();
            var pembuka2 = $(".email-pembuka2").val();
            var body = $(".email-body").val();
            var penutup = $(".email-penutup").val();
            var footer = $(".email-footer").val();
            var oldHeader = $(".email-header-old").val();
            var oldPembuka1 = $(".email-pembuka1-old").val();
            var oldPembuka2 = $(".email-pembuka2-old").val();
            var oldBody = $(".email-body-old").val();
            var oldPenutup = $(".email-penutup-old").val();
            var oldFooter = $(".email-footer-old").val();
            var id = parseInt($(".email-id").val());
						
            /*Old Attachment*/
            var oldAttachment1 = $(".attachment-mcy-old").val();
            var oldAttachment2 = $(".attachment-giro1-old").val();
            var oldAttachment3 = $(".attachment-giro2-old").val();
            var oldAttachment4 = $(".attachment-giro3-old").val();
            var oldAttachment5 = $(".attachment-giro4-old").val();
            var oldAttachment6 = $(".attachment-mcb-old").val();

            /*New Attachment*/
            var attachment1 = $("input.attachment-mcy").val();
            var attachment2 = $("input.attachment-giro1").val();
            var attachment3 = $("input.attachment-giro2").val();
            var attachment4 = $("input.attachment-giro3").val();
            var attachment5 = $("input.attachment-giro4").val();
            var attachment6 = $("input.attachment-mcb").val();

            /*Baru*/
            var counter = 0;
            $.each($("input.input-browse"), function(){
                if($(this).prop("files").length>0)
                    counter++;
            });

            if(counter==0)
                ajaxReq(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6);
            else
                ajaxReqWithAttachment(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6);
        })
        </c:if>
    }

    <c:if test="${showSaveCC==true}">
    function saveEmailCC(){
        var header = $(".email-header-cc").val();
        var pembuka1 = $(".email-pembuka1-cc").val();
        var pembuka2 = $(".email-pembuka2-cc").val();
        var body = $(".email-body-cc").val();
        var penutup = $(".email-penutup-cc").val();
        var footer = $(".email-footer-cc").val();
        var oldHeader = $(".email-header-old-cc").val();
        var oldPembuka1 = $(".email-pembuka1-old-cc").val();
        var oldPembuka2 = $(".email-pembuka2-old-cc").val();
        var oldBody = $(".email-body-old-cc").val();
        var oldPenutup = $(".email-penutup-old-cc").val();
        var oldFooter = $(".email-footer-old-cc").val();
        var id = parseInt($(".email-id-cc").val());

        var attachments = [];

        <c:forEach items="${ccAttchs}" var="attch">
            var attch = new Object();
            attch.oldAttachment = $(".attachment-${attch.attchCode}-old").val();
            attch.newAttachment = $("input.attachment-${attch.attchCode}").val();
            attch.attchCode = "${attch.attchCode}";
            attachments.push(attch);
        </c:forEach>    
        
        /*Baru*/
        var counter = 0;
        $.each($("input.input-browse-cc"), function(){
            if($(this).prop("files").length>0)
                counter++;
        });

        if(counter==0)
            ajaxReqCC(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, 
                    oldPenutup, oldFooter, attachments);
        else
            ajaxReqWithAttachmentCC(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, 
                    oldBody, oldPenutup, oldFooter, attachments);
    }
    </c:if>

    <c:if test="${showSaveCasaCompany==true}">
    function saveEmailCasaCompany(){
    	var header = $("#email-header-casa-company").val();
        var pembuka1 = $("#email-pembuka1-casa-company").val();
        var pembuka2 = $("#email-pembuka2-casa-company").val();
        var body = $("#email-body-casa-company").val();
        var penutup = $("#email-penutup-casa-company").val();
        var footer = $("#email-footer-casa-company").val();
        var oldHeader = $("#email-header-old-casa-company").val();
        var oldPembuka1 = $("#email-pembuka1-old-casa-company").val();
        var oldPembuka2 = $("#email-pembuka2-old-casa-company").val();
        var oldBody = $("#email-body-old-casa-company").val();
        var oldPenutup = $("#email-penutup-old-casa-company").val();
        var oldFooter = $("#email-footer-old-casa-company").val();
        var id = parseInt($("#email-id-casa-company").val());
					
        /*Old Attachment*/
        var oldAttachment1 = $("#attachment-mcy-old-casa-company").val();
        var oldAttachment2 = $("#attachment-giro1-old-casa-company").val();
        var oldAttachment3 = $("#attachment-giro2-old-casa-company").val();
        var oldAttachment4 = $("#attachment-giro3-old-casa-company").val();
        var oldAttachment5 = $("#attachment-giro4-old-casa-company").val();
        var oldAttachment6 = $("#attachment-mcb-old-casa-company").val();

        /*New Attachment*/
        var attachment1 = $("input#attachment-mcy-casa-company").val();
        var attachment2 = $("input#attachment-giro1-casa-company").val();
        var attachment3 = $("input#attachment-giro2-casa-company").val();
        var attachment4 = $("input#attachment-giro3-casa-company").val();
        var attachment5 = $("input#attachment-giro4-casa-company").val();
        var attachment6 = $("input#attachment-mcb-casa-company").val();

        /*Baru*/
        var counter = 0;
        $.each($("input.input-browse-casa-company"), function(){
            if($(this).prop("files").length>0)
                counter++;
        });

        if(counter==0)
            ajaxReqCasaCompany(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6);
        else
            ajaxReqWithAttachmentCasaCompany(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6);
    }
    </c:if>
    <c:if test="${showSave==true}">
    function ajaxReq(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6){
        var data = {
            "id" : id,
            "header" : header,
            "pembuka1" : pembuka1,
            "pembuka2" : pembuka2,
            "body" : body,
            "penutup" : penutup,
            "footer" : footer,
            "oldHeader" : oldHeader,
            "oldPembuka1" : oldPembuka1,
            "oldPembuka2" : oldPembuka2,
            "oldBody" : oldBody,
            "oldPenutup" : oldPenutup,
            "oldFooter" : oldFooter,
            "attachment1" : {
            	"oldAttachment" : oldAttachment1,
            	"newAttachment" : attachment1
            },
            "attachment2" : {
            	"oldAttachment" : oldAttachment2,
            	"newAttachment" : attachment2
            },
            "attachment3" : {
            	"oldAttachment" : oldAttachment3,
            	"newAttachment" : attachment3
            },
            "attachment4" : {
            	"oldAttachment" : oldAttachment4,
            	"newAttachment" : attachment4
            },
            "attachment5" : {
            	"oldAttachment" : oldAttachment5,
            	"newAttachment" : attachment5
            },
            "attachment6" : {
            	"oldAttachment" : oldAttachment6,
            	"newAttachment" : attachment6
            }
        }
        
        if(header!=oldHeader || pembuka1!=oldPembuka1 || pembuka2!=oldPembuka2 || footer!=oldFooter || penutup!=oldPenutup || body!=oldBody || attachment1!=oldAttachment1 || attachment2!=oldAttachment2 || attachment3!=oldAttachment3 || attachment4!=oldAttachment4 || attachment5!=oldAttachment5 || attachment6!=oldAttachment6){
            var status = confirm("Apakah anda yakin dengan data ini?");

            if(status==true){
                var ajax = $.ajax({
                    url: "<c:url value='/email-content/save1'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(data)
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
    }

    function ajaxReqWithAttachment(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6){

        var formData = new FormData();
        formData.append("header", header);
        formData.append("pembuka1", pembuka1);
        formData.append("pembuka2", pembuka2);
        formData.append("body", body);
        formData.append("penutup", penutup);
        formData.append("footer", footer);
        formData.append("oldHeader", oldHeader);
        formData.append("oldPembuka1", oldPembuka1);
        formData.append("oldPembuka2", oldPembuka2);
        formData.append("oldBody", oldBody);
        formData.append("oldPenutup", oldPenutup);
        formData.append("oldFooter", oldFooter);
        formData.append("id", id);
        formData.append("oldAttachment1", oldAttachment1);
        formData.append("oldAttachment2", oldAttachment2);
        formData.append("oldAttachment3", oldAttachment3);
        formData.append("oldAttachment4", oldAttachment4);
        formData.append("oldAttachment5", oldAttachment5);
        formData.append("oldAttachment6", oldAttachment6);
        formData.append("newAttachment1", attachment1);
        formData.append("newAttachment2", attachment2);
        formData.append("newAttachment3", attachment3);
        formData.append("newAttachment4", attachment4);
        formData.append("newAttachment5", attachment5);
        formData.append("newAttachment6", attachment6);

        if($("input#file_mcy").prop("files").length>0){
            formData.append("attachment1", file_mcy.files[0]);
            formData.append("attachment1Name", file_mcy.files[0].name);
        }
        if($("input#file_giro1").prop("files").length>0){
            formData.append("attachment2", file_giro1.files[0]);
            formData.append("attachment2Name", file_giro1.files[0].name);
        }
        if($("input#file_giro2").prop("files").length>0){
            formData.append("attachment3", file_giro2.files[0]);
            formData.append("attachment3Name", file_giro2.files[0].name);
        }   
        if($("input#file_giro3").prop("files").length>0){
            formData.append("attachment4", file_giro3.files[0]);
            formData.append("attachment4Name", file_giro3.files[0].name);
        }
        if($("input#file_giro4").prop("files").length>0){
            formData.append("attachment5", file_giro4.files[0]);
            formData.append("attachment5Name", file_giro4.files[0].name);
        }
        if($("input#file_mcb").prop("files").length>0){
            formData.append("attachment6", file_mcb.files[0]);
            formData.append("attachment6Name", file_mcb.files[0].name);
        }

        var status = confirm("Apakah anda yakin dengan data ini?");

        if(status==true){
            var ajax = $.ajax({
                url: "<c:url value='/email-content/save2'/>",
                timeout: 3600000,
                data: formData,
                dataType: 'json',
                processData: false,
                contentType: false,
                type: 'POST'
            })
            ajax.done(function (response) {
                if(response.resultCode==1000){
                    alert("Sukses Update");
                    //Reload page content --> current-page-link ada di dashboard.jsp
                    $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
                }
                else if(response.resultCode==1001){
                    alert(response.message);
                }
                else if(response.resultCode==1002){
                    alert(response.message);
                }
                else {
                    alert("Gagal update");
                }

            })
            ajax.fail(function(){
                console.log('Failed: '+status);
            })
        }
    }
    </c:if>

    <c:if test="${showSaveCasaCompany==true}">
    function ajaxReqCasaCompany(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6){
        var data = {
            "id" : id,
            "header" : header,
            "pembuka1" : pembuka1,
            "pembuka2" : pembuka2,
            "body" : body,
            "penutup" : penutup,
            "footer" : footer,
            "oldHeader" : oldHeader,
            "oldPembuka1" : oldPembuka1,
            "oldPembuka2" : oldPembuka2,
            "oldBody" : oldBody,
            "oldPenutup" : oldPenutup,
            "oldFooter" : oldFooter,
            "attachment1" : {
            	"oldAttachment" : oldAttachment1,
            	"newAttachment" : attachment1
            },
            "attachment2" : {
            	"oldAttachment" : oldAttachment2,
            	"newAttachment" : attachment2
            },
            "attachment3" : {
            	"oldAttachment" : oldAttachment3,
            	"newAttachment" : attachment3
            },
            "attachment4" : {
            	"oldAttachment" : oldAttachment4,
            	"newAttachment" : attachment4
            },
            "attachment5" : {
            	"oldAttachment" : oldAttachment5,
            	"newAttachment" : attachment5
            },
            "attachment6" : {
            	"oldAttachment" : oldAttachment6,
            	"newAttachment" : attachment6
            }
        }
        
        if(header!=oldHeader || pembuka1!=oldPembuka1 || pembuka2!=oldPembuka2 || footer!=oldFooter || penutup!=oldPenutup || body!=oldBody || attachment1!=oldAttachment1 || attachment2!=oldAttachment2 || attachment3!=oldAttachment3 || attachment4!=oldAttachment4 || attachment5!=oldAttachment5 || attachment6!=oldAttachment6){
            var status = confirm("Apakah anda yakin dengan data ini?");

            if(status==true){
                var ajax = $.ajax({
                    url: "<c:url value='/email-content/save1-casa-company'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(data)
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
    }

    function ajaxReqWithAttachmentCasaCompany(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, oldPenutup, oldFooter, oldAttachment1, oldAttachment2, oldAttachment3, oldAttachment4, oldAttachment5, oldAttachment6, attachment1, attachment2, attachment3, attachment4, attachment5, attachment6){

        var formData = new FormData();
        formData.append("header", header);
        formData.append("pembuka1", pembuka1);
        formData.append("pembuka2", pembuka2);
        formData.append("body", body);
        formData.append("penutup", penutup);
        formData.append("footer", footer);
        formData.append("oldHeader", oldHeader);
        formData.append("oldPembuka1", oldPembuka1);
        formData.append("oldPembuka2", oldPembuka2);
        formData.append("oldBody", oldBody);
        formData.append("oldPenutup", oldPenutup);
        formData.append("oldFooter", oldFooter);
        formData.append("id", id);
        formData.append("oldAttachment1", oldAttachment1);
        formData.append("oldAttachment2", oldAttachment2);
        formData.append("oldAttachment3", oldAttachment3);
        formData.append("oldAttachment4", oldAttachment4);
        formData.append("oldAttachment5", oldAttachment5);
        formData.append("oldAttachment6", oldAttachment6);
        formData.append("newAttachment1", attachment1);
        formData.append("newAttachment2", attachment2);
        formData.append("newAttachment3", attachment3);
        formData.append("newAttachment4", attachment4);
        formData.append("newAttachment5", attachment5);
        formData.append("newAttachment6", attachment6);

        if($("input#file_mcy_casa_company").prop("files").length>0){
            formData.append("attachment1", file_mcy_casa_company.files[0]);
            formData.append("attachment1Name", file_mcy_casa_company.files[0].name);
        }
        if($("input#file_giro1_casa_company").prop("files").length>0){
            formData.append("attachment2", file_giro1_casa_company.files[0]);
            formData.append("attachment2Name", file_giro1_casa_company.files[0].name);
        }
        if($("input#file_giro2_casa_company").prop("files").length>0){
            formData.append("attachment3", file_giro2_casa_company.files[0]);
            formData.append("attachment3Name", file_giro2_casa_company.files[0].name);
        }   
        if($("input#file_giro3_casa_company").prop("files").length>0){
            formData.append("attachment4", file_giro3_casa_company.files[0]);
            formData.append("attachment4Name", file_giro3_casa_company.files[0].name);
        }
        if($("input#file_giro4_casa_company").prop("files").length>0){
            formData.append("attachment5", file_giro4_casa_company.files[0]);
            formData.append("attachment5Name", file_giro4_casa_company.files[0].name);
        }
        if($("input#file_mcb_casa_company").prop("files").length>0){
            formData.append("attachment6", file_mcb_casa_company.files[0]);
            formData.append("attachment6Name", file_mcb_casa_company.files[0].name);
        }

        var status = confirm("Apakah anda yakin dengan data ini?");

        if(status==true){
            var ajax = $.ajax({
                url: "<c:url value='/email-content/save2-casa-company'/>",
                timeout: 3600000,
                data: formData,
                dataType: 'json',
                processData: false,
                contentType: false,
                type: 'POST'
            })
            ajax.done(function (response) {
                if(response.resultCode==1000){
                    alert("Sukses Update");
                    //Reload page content --> current-page-link ada di dashboard.jsp
                    $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />"); 
                }
                else if(response.resultCode==1001){
                    alert(response.message);
                }
                else if(response.resultCode==1002){
                    alert(response.message);
                }
                else {
                    alert("Gagal update");
                }

            })
            ajax.fail(function(){
                console.log('Failed: '+status);
            })
        }
    }
    </c:if>
    
    <c:if test="${showSaveCC==true}">
    function ajaxReqCC(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, oldBody, 
                    oldPenutup, oldFooter, attachments){
        var data = {
            "id" : id,
            "header" : header,
            "pembuka1" : pembuka1,
            "pembuka2" : pembuka2,
            "body" : body,
            "penutup" : penutup,
            "footer" : footer,
            "oldHeader" : oldHeader,
            "oldPembuka1" : oldPembuka1,
            "oldPembuka2" : oldPembuka2,
            "oldBody" : oldBody,
            "oldPenutup" : oldPenutup,
            "oldFooter" : oldFooter,
            "attachments" : attachments
        }
        
        var isUpdated = false;
        if(header!=oldHeader || pembuka1!=oldPembuka1 || pembuka2!=oldPembuka2 || footer!=oldFooter || penutup!=oldPenutup || body!=oldBody){
            isUpdated = true;
        }
        if(isUpdated==false){
           $.each(attachments, function(i, attch){
               if(attch.oldAttachment!=attch.newAttachment){
                   isUpdated = true;
                   return false;    //break;
               }
           })
        }

        if(isUpdated){
            var status = confirm("Apakah anda yakin dengan data ini?");

            if(status==true){
                var ajax = $.ajax({
                    url: "<c:url value='/email-content/save1-cc'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(data)
                })

                ajax.done(function (response) {
                    alert(response.message);

                    //Reload page content --> current-page-link ada di dashboard.jsp
                    $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />", function(){
                        $("#cc-tabmenu").trigger("click");
                    }); 
                })

                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
        }        
    }

    function ajaxReqWithAttachmentCC(id, header, pembuka1, pembuka2, body, penutup, footer, oldHeader, oldPembuka1, oldPembuka2, 
        oldBody, oldPenutup, oldFooter, attachments){

        var formData = new FormData();
        formData.append("header", header);
        formData.append("pembuka1", pembuka1);
        formData.append("pembuka2", pembuka2);
        formData.append("body", body);
        formData.append("penutup", penutup);
        formData.append("footer", footer);
        formData.append("oldHeader", oldHeader);
        formData.append("oldPembuka1", oldPembuka1);
        formData.append("oldPembuka2", oldPembuka2);
        formData.append("oldBody", oldBody);
        formData.append("oldPenutup", oldPenutup);
        formData.append("oldFooter", oldFooter);
        formData.append("id", id);

        var attchCodes = "";
        for(var attch of attachments){
            formData.append("oldAttachment"+attch.attchCode, attch.oldAttachment);
            formData.append("newAttachment"+attch.attchCode, attch.newAttachment);
            if($("input#file_"+attch.attchCode).prop("files").length>0){
                formData.append("attachment"+attch.attchCode, $("#file_"+attch.attchCode)[0].files[0]);
            }
            
            if(attchCodes==""){
                attchCodes = attch.attchCode;
            } else {
                attchCodes += "---" + attch.attchCode;
            }
        }
        formData.append("attchCodes", attchCodes);

        // for (var pair of formData.entries()) {
        //     console.log(pair[0]+ ', ' + pair[1]); 
        // }
        var status = confirm("Apakah anda yakin dengan data ini?");

        if(status==true){
            var ajax = $.ajax({
                url: "<c:url value='/email-content/save2-cc'/>",
                timeout: 3600000,
                data: formData,
                dataType: 'json',
                processData: false,
                contentType: false,
                type: 'POST'
            })
            ajax.done(function (response) {
                if(response.resultCode==1000){
                    alert("Sukses Update");
                    //Reload page content --> current-page-link ada di dashboard.jsp
                    $("#page-content").load("<c:url value='"+$("#current-page-link").val()+"' />", function(){
                        $("#cc-tabmenu").trigger("click");
                    }); 
                }
                else if(response.resultCode==1001){
                    alert(response.message);
                }
                else if(response.resultCode==1002){
                    alert(response.message);
                }
                else {
                    alert("Gagal update");
                }

            })
            ajax.fail(function(){
                console.log('Failed: '+status);
            })
        }
    }
    </c:if>

    function validateFile() {
        $('.input-browse').change(function() {
            var $changed_input = $(this),
            file_extension = $changed_input.val().match(/\.([^\.]+)$/)[1].toLowerCase(),
            //allowed_extensions = new RegExp($changed_input.attr('accept').replace(/,/g, "$|^").replace(/\./g, "").toLowerCase());
            allowed_extensions = new RegExp(".pdf".replace(/,/g, "$|^").replace(/\./g, "").toLowerCase());

            var checkExtension = true;

            var fileSize = $changed_input.prop("files")[0].size;
            if(fileSize > getMaxAttachmentSize()){
                alert("Ukuran file melebihi batas maksimum [${maxSize} KB]");
                $(this).parent().prev().val($(this).parent().prev().prev().val());  //Tampilkan nama old file di field
                $(this).replaceWith($(this).val("").clone(true));   //reset field
                checkExtension = false;
            }

            if(checkExtension){
                if (file_extension.match(allowed_extensions)) {
                    $(this).parent().prev().val($(this).val()); //Tampilkan nama file di field
                }
                else {
                    alert('Format file yang diperbolehkan hanya .PDF');
                    $(this).parent().prev().val($(this).parent().prev().prev().val());  //Tampilkan nama old file di field
                    $(this).replaceWith($(this).val("").clone(true));   //reset field
                }; 
            }        
        });
        
        $('.input-browse-casa-company').change(function() {
            var $changed_input = $(this),
            file_extension = $changed_input.val().match(/\.([^\.]+)$/)[1].toLowerCase(),
            //allowed_extensions = new RegExp($changed_input.attr('accept').replace(/,/g, "$|^").replace(/\./g, "").toLowerCase());
            allowed_extensions = new RegExp(".pdf".replace(/,/g, "$|^").replace(/\./g, "").toLowerCase());

            var checkExtension = true;

            var fileSize = $changed_input.prop("files")[0].size;
            if(fileSize > getMaxAttachmentSize()){
                alert("Ukuran file melebihi batas maksimum [${maxSize} KB]");
                $(this).parent().prev().val($(this).parent().prev().prev().val());  //Tampilkan nama old file di field
                $(this).replaceWith($(this).val("").clone(true));   //reset field
                checkExtension = false;
            }

            if(checkExtension){
                if (file_extension.match(allowed_extensions)) {
                    $(this).parent().prev().val($(this).val()); //Tampilkan nama file di field
                }
                else {
                    alert('Format file yang diperbolehkan hanya .PDF');
                    $(this).parent().prev().val($(this).parent().prev().prev().val());  //Tampilkan nama old file di field
                    $(this).replaceWith($(this).val("").clone(true));   //reset field
                }; 
            }        
        });

        $('.input-browse-cc').change(function() {
            var $changed_input = $(this),
            file_extension = $changed_input.val().match(/\.([^\.]+)$/)[1].toLowerCase(),
            //allowed_extensions = new RegExp($changed_input.attr('accept').replace(/,/g, "$|^").replace(/\./g, "").toLowerCase());
            allowed_extensions = new RegExp(".pdf".replace(/,/g, "$|^").replace(/\./g, "").toLowerCase());

            var checkExtension = true;

            var fileSize = $changed_input.prop("files")[0].size;
            if(fileSize > getMaxAttachmentSize()){
                alert("Ukuran file melebihi batas maksimum [${maxSize} KB]");
                $(this).parent().prev().val($(this).parent().prev().prev().val());  //Tampilkan nama old file di field
                $(this).replaceWith($(this).val("").clone(true));   //reset field
                checkExtension = false;
            }

            if(checkExtension){
                if (file_extension.match(allowed_extensions)) {
                    $(this).parent().prev().val($(this).val()); //Tampilkan nama file di field
                }
                else {
                    alert('Format file yang diperbolehkan hanya .PDF');
                    $(this).parent().prev().val($(this).parent().prev().prev().val());  //Tampilkan nama old file di field
                    $(this).replaceWith($(this).val("").clone(true));   //reset field
                }; 
            }        
        });
    }

    function clearFile(theFiles){
        $.each(theFiles, function(){
            $(this).replaceWith($(this).val("").clone(true));   //reset field
        });
    }

    function resetFileField($theFile){
        $theFile.replaceWith($theFile.val("").clone(true));   //reset field
    }

    function getMaxAttachmentSize(){
        var maxInByte = ${maxSize} * 1024;
        return maxInByte;
    }

    function cancelRequestButtonEvent(){
        <c:if test="${showCancel==true}">
        $(".cancel-request-btn").off().on("click", function(){
            var status = confirm("Apakah anda yakin akan membatalkan request sebelumnya??");

            if(status){
                var ajax = $.ajax({
                    url: "<c:url value='/email-content/cancel'/>",
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
        </c:if>
    }

    function cancelRequestCasaCompany(){
        <c:if test="${showCancelCasaCompany==true}">
        var status = confirm("Apakah anda yakin akan membatalkan request sebelumnya??");

        if(status){
            var ajax = $.ajax({
                url: "<c:url value='/email-content/cancel-casa-company'/>",
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
        </c:if>
    }
    
    <c:if test="${showCancelCC==true}">
    function cancelRequestCC(){
        var status = confirm("Apakah anda yakin akan membatalkan request sebelumnya??");

        if(status){
            var ajax = $.ajax({
                url: "<c:url value='/email-content/cancel-cc'/>",
                timeout: 30000,
                type : "PUT"
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
            }) 
        } 
    }
    </c:if>
</script>
