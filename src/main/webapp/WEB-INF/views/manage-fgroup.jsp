<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    table.table-detail-fgroup tbody tr td {
        vertical-align: middle;
    }

    table.table-detail-fgroup thead th {
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

<h4>Manage Fgroup</h4>
<br/>

<button type="button" class="btn btn-default add-fgroup-btn">Add Fgroup</button>
<div class="add-fgroup-area" style="display:none;">
    <br />
    <table class="table-input">
        <tr>
            <td>Fgroup</td>
            <td style="width:10px;"> : </td>
            <td>
                <input type="text" class="form-control input-fgroup-title" maxlength="50"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>
                <button type="button" class="btn btn-primary btn-sm save-fgroup-btn">Save</button>
            </td>
        </tr>
    </table>
    <br />
</div>

<div class="table-wrapper" style="width:100%;">
    <div class="table-responsive">
        <table class="table table-striped table-hover table-bordered table-detail-fgroup">
            <thead>
                <th style="width:25px;">No.</th>
                <th>FGroup</th>
                <th style="width:150px;">Action</th>
            </thead>
            <tbody>
                <!--Content Menu-->
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
        document.title = "Manage Fgroup";

        setAddFgroupArea();
        saveBtnClicked();
        refreshTable();
    });

    function setAddFgroupArea(){
        $(".add-fgroup-btn").click(function (){
            $(".add-fgroup-area").toggle(300);
        });
    }

    function saveBtnClicked(){
        $(".save-fgroup-btn").off().on("click", function(){
            var fgroupName = $(".input-fgroup-title").val();

            var data = {
                "fgroupName" : fgroupName
            }

            var ajax = $.ajax({
                url: "<c:url value='/fgroup/add'/>",
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
        $(".input-fgroup-title").val("");
    }

    function clearTBody(){
        $('.table-detail-fgroup tbody tr').remove();
    }

    function refreshTable(){
        var ajax = $.ajax({
                        url: "<c:url value='/fgroup/get-all' />",
                        timeout : 30000,
                        dataType : "JSON",
                        type : "GET"
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                clearTBody();

                var arr = response.data;
                var indexSecAdm = -1;
                var secAdm;
                
                for(var i=0; i<arr.length; i++){
                	if(arr[i].fgroupName=='${secAdmin}'){
                		indexSecAdm = i;	
                		secAdm = arr[i];
                		break;
                	}
                }
                
                if(indexSecAdm!=-1){
                	arr.splice(indexSecAdm,1);
                	arr.splice(0, 0, secAdm);
                }
                
                $.each(arr, function(i,item){
                    var row = "<tr><td style='text-align:center;'>" + (i+1) + ".</td>"
                            + "<td><input type='hidden' class='old-the-title' value='"+item.fgroupName+"' /><input type='text' class='form-control in-table the-title' value='" + item.fgroupName + "' disabled/></td>"
                            + "<td style='text-align:center;'>";

					if(item.fgroupName!='${secAdmin}'){
						row += "<input type='hidden' name='input-fgroup-id' value='"+ item.fgroupId +"'/>"
                        		+ "<button type='button' class='btn btn-default btn-sm edit-btn'>Edit</button>&nbsp;"
                        		+ "<button type='button' class='btn btn-default btn-sm delete-btn'>Delete</button>"
					}
						
					row += "</td></tr>";
						
                    $('.table-detail-fgroup tbody').append(row);
                });

                deleteFgroup();
                editFgroup();
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

     /*DeleteFgroup*/
    function deleteFgroup(){
        $(".delete-btn").off().on('click', function(){
            var status = confirm("Apakah anda yakin akan menghapus data ini?");
            
            if(status==true){
                var $this = $(this);
            
                var fgroupId = $this.prev().prev().val();

                var ajax = $.ajax({
                    url: "<c:url value='/fgroup/delete?fgroupId=" + fgroupId + "'/>",
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

    function editFgroup(){
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
            $.each($(".table-detail-fgroup").find("input.in-table"), function(){
                $(this).prop("disabled", true).val($(this).prev().val());
            })
            $(".edit-btn").text("Edit");
        })
    }

    function saveAllEdited(){
        $(".save-edited-btn").off().on("click", function(){
            var fgroups = [];

            var trs = $(".table-detail-fgroup tbody tr");
            $.each(trs, function(){
                var inputInTable = $(this).find("input.in-table:enabled");
                if(inputInTable.length<=0)
                    return true;    //continue;
                else {
                    var fgroup = new Object();
                    fgroup.fgroupId = parseInt($(this).find("input[name='input-fgroup-id']").first().val());
                    fgroup.fgroupName = inputInTable.eq(0).val();
                    fgroups.push(fgroup);
                }
            })

            if(fgroups.length==0){
                alert("Tidak ada data yang di-update");
                return;
            }

            var status = confirm("Apakah anda yakin akan mengupdate semua data ini?");

            if(status==true){
                var ajax = $.ajax({
                    url: "<c:url value='/fgroup/bulk-update'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(fgroups)
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