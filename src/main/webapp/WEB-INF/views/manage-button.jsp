<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    table.table-detail-button tbody tr td {
        vertical-align: middle;
    }

    table.table-detail-button thead th {
        vertical-align: middle;
        text-align: center;
        width: auto;
        font-weight: bold;
    }

    table.table-input tr td {
        padding: 3px;
    }

    table.table-input tr td .form-control {
        width: 300px;
    }

    input.in-table.the-title.the-url {
        width: 250px;
    }
</style>

<h4>Manage Button</h4>
<br/>

<button type="button" class="btn btn-default add-button-btn">Add Button</button>
<div class="add-button-area" style="display:none;">
    <br />
    <table class="table-input">
        <tr>
            <td>Button Title</td>
            <td style="width:10px;"> : </td>
            <td>
                <input type="text" class="form-control input-button-title" maxlength="50"/>
            </td>
        </tr>
        <tr>
            <td>Button Action URL</td>
            <td style="width:10px;"> : </td>
            <td><input type="text" class="form-control input-button-url" maxlength="50"/></td>
        </tr>
        <tr>
            <td>Button Activity Description</td>
            <td style="width:10px;"> : </td>
            <td><input type="text" class="form-control input-button-action" maxlength="50"/></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>
                <button type="button" class="btn btn-primary btn-sm save-button-btn">Save</button>
            </td>
        </tr>
    </table>
    <br />
</div>

<div class="table-wrapper" style="width:100%;">
    <div class="table-responsive">
        <table class="table table-striped table-hover table-bordered table-detail-button">
            <thead>
                <th style="width:25px;">No.</th>
                <th>Button Title</th>
                <th>Button Action URL</th>
                <th>Button Activity Description</th>
                <th style="width:150px;">Action</th>
            </thead>
            <tbody>
                <!--Content Menu-->
                <tr id="tr-no-row" style="display:none;">
                    <td colspan="5" style="text-align: center;">- No Record -</td>
                </tr>
            </tbody>
        </table>
    </div>
    <div style="text-align:right; margin-top:10px;">
        <button type="button" class="btn btn-primary btn-sm save-edited-btn">Save All</button>
        <button type="button" class="btn btn-primary btn-sm cancel-edited-btn">Cancel All</button>
    </div>
</div>

<script>
    $(document).ready(function(){
        document.title = "Manage Button";

        setAddButtonArea();
        saveBtnClicked();
        refreshTable();
    });

    function setAddButtonArea(){
        $(".add-button-btn").click(function (){
            $(".add-button-area").toggle(300);
        });
    }
    
    function showNoRecord(){
        $("tr#tr-no-row").show();
    }
    
    function hideNoRecord(){
    	$("tr#tr-no-row").hide();
    }

    function saveBtnClicked(){
        $(".save-button-btn").off().on("click", function(){
            var buttonTitle = $(".input-button-title").val();
            var buttonUrl = $(".input-button-url").val();
            var buttonAction = $(".input-button-action").val();

            var data = {
                "buttonTitle" : buttonTitle,
                "buttonUrl" : buttonUrl,
                "buttonAction" : buttonAction
            }

            var ajax = $.ajax({
                url: "<c:url value='/button/add'/>",
                timeout: 30000,
                type : "POST",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(data)
            });
            ajax.done(function (response) {
                alert(response.message);
                refreshTable();
                clearForm();
            })

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            });
        })
    }

    function clearForm(){
        $(".input-button-title").val("");
        $(".input-button-url").val("");
        $(".input-button-action").val("");
    }

    function clearTBody(){
        $('.table-detail-button tbody tr:not(#tr-no-row)').remove();
    }

    function refreshTable(){
        var ajax = $.ajax({
                        url: "<c:url value='/button/get-all' />",
                        timeout : 30000,
                        dataType : "JSON",
                        type : "GET"
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

                    var row = "<tr><td style='text-align:center;'>" + (i+1) + ".</td>"
                            + "<td><input type='hidden' class='old-the-title' value='"+(item.buttonTitle!=null ? item.buttonTitle : "")+"' /><input type='text' class='form-control in-table the-title' value='" + (item.buttonTitle!=null ? item.buttonTitle : "") + "' disabled/></td>"
                            + "<td><input type='hidden' class='old-the-url' value='"+(item.buttonUrl!=null ? item.buttonUrl : "")+"' /><input type='text' class='form-control in-table the-url' value='" + (item.buttonUrl!=null ? item.buttonUrl : "") + "' disabled/></td>"
                            + "<td><input type='hidden' class='old-the-action' value='"+(item.buttonAction!=null ? item.buttonAction : "")+"' /><input type='text' class='form-control in-table the-action' value='" + (item.buttonAction!=null ? item.buttonAction : "") + "' disabled/></td>"
                            + "<td style='text-align:center;'><input type='hidden' name='input-button-id' value='"+ item.buttonId +"'/>"
                            + "<button type='button' class='btn btn-default btn-sm edit-btn'>Edit</button>&nbsp;"
                            + "<button type='button' class='btn btn-default btn-sm delete-btn'>Delete</button></td></tr>";


                    $('.table-detail-button tbody').append(row);
                });

                deleteButton();
                editButton();
                saveAllEdited();
                cancelAllEdited();
            } else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

     /*DeleteButton*/
    function deleteButton(){
        $(".delete-btn").off().on('click', function(){
            var status = confirm("Apakah anda yakin akan menghapus data ini?");
            
            if(status==true){
                var $this = $(this);
            
                var buttonId = $this.prev().prev().val();

                var ajax = $.ajax({
                    url: "<c:url value='/button/delete?buttonId=" + buttonId + "'/>",
                    timeout: 30000,
                    type : "DELETE",
                    contentType: "application/json",
                    dataType: "json"
                })

                ajax.always(function(){

                })

                ajax.done(function (response) {
                    if(response.resultCode==1000){
                        refreshTable();  
                    }
                    else {
                        alert(response.message);
                    }
                                  
                })

                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
        })        
    }

    function editButton(){
        $(".edit-btn").off().on('click', function(){
            var $this = $(this);
            var tr = $this.parent().parent();

            if($this.text()=="Edit"){
                tr.find("input.in-table").prop("disabled", false);
                $this.text("Cancel");
            } else {
                tr.find("input.in-table").prop("disabled", true);
                $this.text("Edit");
                var allInput = tr.find("input.in-table");
                $.each(allInput, function(){
                    $(this).val($(this).prev().val());
                })
            }
        })
    }

    function cancelAllEdited(){
        $(".cancel-edited-btn").off().on("click", function(){
            $.each($(".table-detail-button").find("input.in-table"), function(){
                $(this).prop("disabled", true).val($(this).prev().val());
            })
            $(".edit-btn").text("Edit");
        })
    }

    function saveAllEdited(){
        $(".save-edited-btn").off().on("click", function(){
            var buttons = [];

            var trs = $(".table-detail-button tbody tr");
            $.each(trs, function(){
                var inputInTable = $(this).find("input.in-table:enabled");
                if(inputInTable.length<=0)
                    return true;    //continue;
                else {
                    var button = new Object();
                    button.buttonId = parseInt($(this).find("input[name='input-button-id']").first().val());
                    button.buttonTitle = inputInTable.eq(0).val();
                    button.buttonUrl = inputInTable.eq(1).val();
                    button.buttonAction = inputInTable.eq(2).val();
                    buttons.push(button);
                }
            })

            if(buttons.length==0){
                alert("Tidak ada data yang di-update");
                return;
            }

            var status = confirm("Apakah anda yakin akan mengupdate semua data ini?");

            if(status==true){
                var ajax = $.ajax({
                    url: "<c:url value='/button/bulk-update'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(buttons)
                });

                ajax.done(function (response) {
                    alert(response.message);
                    refreshTable();
                    clearForm();
                })

                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
        })
    }
</script>