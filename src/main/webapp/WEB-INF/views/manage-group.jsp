<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h4>Manage Group</h4>
    
<br />
<button type="button" class="btn btn-default add-group-btn">Add Group</button>
<div class="add-group-area" style="display:none;">
    <table>
        <tr>
            <td>Group Name</td>
            <td style="width:10px;"> : </td>
            <td>
                <input type="hidden" class="form-control input-group-id-add" value="0" maxlength="50"/>
                <input type="text" class="form-control input-group-name" maxlength="50"/>
            </td>
        </tr>
        <tr>
            <td>FGroup</td>
            <td style="width:10px;"> : </td>
            <td><input type="text" class="form-control input-fgroup" maxlength="50"/></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>
                <button type="button" class="btn btn-primary btn-sm save-group-btn">Save</button>&nbsp;
                <button type="button" class="btn btn-primary btn-sm cancel-group-btn">Cancel</button>
            </td>
        </tr>
    </table>
    <br />
</div>
    
<div class="table-responsive" style="width:50%;">
    <table class="table table-striped table-hover table-bordered table-detail-group">
        <thead>
            <th style="width:25px;">No.</th>
            <th>Group Name</th>
            <th>FGroup</th>
            <th style="width:110px;">Action</th>
        </thead>
        <tbody>
            <!--Content Group-->
        </tbody>
    </table>
</div>
    
<script>
    $(document).ready(function(){
        document.title = "Manage Group";
        
        $(".add-group-btn").click(function (){
            $(".add-group-area").toggle(300);
        });
        
        $(".culun").click(function(){
            clearTBody();
        })
        
        refreshTable();
        saveGroup();
        cancelGroup();
    })
    
    function refreshTable(){
        var ajax = $.ajax({
                        url: "<c:url value='/group/get' />",            
                        timeout : 30000,
                        dataType : "JSON",
                        type : "GET"
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $.each(response.data, function(i,item){
                    var row = "<tr><td>" + (i+1) + "</td><td>" + item.groupName + "</td><td>" + item.fgroup + "</td><td>" +
                                "<input type='hidden' name='input-group-id' value='"+ item.groupId +"'/>" +
                                "<button type='button' class='btn btn-default btn-xs edit-btn'>Edit</button>&nbsp;" +
                                "<button type='button' class='btn btn-default btn-xs delete-btn'>Delete</button></td></tr>";

                    $('.table-detail-group tbody').append(row);

                    editGroup();
                    deleteGroup();
                });
            } else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }
    
    /*Cancel Button*/
    function cancelGroup(){
        $(".cancel-group-btn").off().on('click', function(){
            clearForm();
        })
    }
    
    /*Edit Group*/
    function editGroup(){
        $(".edit-btn").off().on('click', function(){
            var $this = $(this);
            var groupId = $this.prev().val();
            var groupName = $this.parent().prev().prev().text();
            var fgroup = $this.parent().prev().text();
                        
            $(".add-group-btn").text("Edit Group");
            
            $(".input-group-id-add").val(groupId);
            $(".input-group-name").val(groupName);
            $(".input-fgroup").val(fgroup);
            
            $(".add-group-area").slideDown(300);
        }) 
    }
    
    /*DeleteButton*/
    function deleteGroup(){
        $(".delete-btn").off().on('click', function(){
            var status = confirm("Apakah anda yakin akan menghapus data ini?");

            if(status==true){
                var $this = $(this);
                var groupId = $this.prev().prev().val();
                var ajax = $.ajax({
                    url: "<c:url value='/group/delete?groupId=" + groupId + "'/>",
                    timeout: 30000,
                    type : "DELETE",
                    dataType: "json"
                })

                ajax.always(function(){

                })

                ajax.done(function (response) {
                    if(response.resultCode == 1000){    //success
                        clearTBody();
                        refreshTable();
                    }
                    else 
                        alert(response.message);
                })

                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }            
        })        
    }
    
    /*Remove Tbody*/
    function clearTBody(){
        $(".table-detail-group tbody tr").remove();
    }
    
    function clearForm(){
        $(".input-group-id-add").val(0);
        $(".input-group-name").val("");
        $(".input-fgroup").val("");
        $(".add-group-btn").text("Add Group");
    }
    
    /*Save Button*/
    function saveGroup(){
        $(".save-group-btn").off().on('click', function(){
            var groupId = parseInt($(".input-group-id-add").val());
            var groupName = $(".input-group-name").val();
            var fgroup = $(".input-fgroup").val();
            var ajax;
            
            if(groupId==0){ //Add New Group
                var data = {
					"groupName": groupName,
					"fgroup": fgroup
				};
            
                var ajax = $.ajax({
                    url: "<c:url value='/group/add'/>",
                    timeout: 30000,
                    type : "POST",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(data)
                })

                ajax.always(function(){

                })

                ajax.done(function (response) {
                    alert(response.message);
                    clearTBody();
                    clearForm();
                    refreshTable();
                })

                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
            else {  //Edit Group
                var data = {
                    "groupId": groupId,
					"groupName": groupName,
					"fgroup": fgroup
				};
            
                var ajax = $.ajax({
                    url: "<c:url value='/group/update'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(data)
                })

                ajax.always(function(){

                })

                ajax.done(function (response) {
                    alert(response.message);
                    clearTBody();
                    clearForm();
                    refreshTable();
                })

                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
        })
    }
</script>