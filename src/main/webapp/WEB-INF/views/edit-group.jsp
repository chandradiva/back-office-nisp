<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h4>Edit Group</h4>
<br />

<style>
    #div-group {
        width: 40%;
        clear: left;
        float: left;
    }

    #save-group-button {
        text-align: left;
        float: left;
        clear: both;
    }

    #div-fgroup {
        float: right;
        text-align: left;
        width: 60%;
        margin-top:35px;
    }

    select.input-fgroup {
        width:290px;
    }

    select#input-select-group {
        width: 200px;
    }

    table.table-detail-fgroup {
        width:350px;
    }

    table.table-detail-fgroup thead th {
        font-weight: bold;
    }

    table.fgroup-input-area {
        width: 350px;
    }

    table.list-group tr td {
        padding: 5px 5px 0px 0px;
        margin: 0px;
    }
    
    ul .inner-menu-eg {
	  padding-left: 18px;
	  overflow: hidden;
	  display: none;
	}
</style>

<c:if test="${showInfoNoGroup==true}">
<div id="div-info" class="alert alert-info">
    <strong>Info!</strong><br />
    Saat ini anda belum ada group yang tersedia..
</div>
</c:if>

<div id="div-group">
    <div style="width:20%;">
        <table class="list-group" border="0" cellspacing="0" cellpadding="0"> 
            <tr>
                <td>
                    <select id="input-select-group" class="form-control ">
                        <!--refreshGroupList()-->
                    </select>
                </td>
                <td>
                    <button type="button" class="btn btn-primary btn-md edit-name-btn">Edit Name</button>
                </td>
            </tr>
            <tr class="edit-name" style="display: none;">
                <td>
                    <div class="edit-name" style="display: none;">
                        <input type="text" class="form-control group-name" />
                    </div>
                </td>
                <td></td>
            </tr>
        </table>
    </div>

    <h5>Pilih Menu untuk grup ini</h5>
    <div id="menus-tree">
        <ul class="tree-view-checked">
            <!--refreshTreeViewChecked-->
        </ul>
    </div>
</div>

<div id="div-fgroup">
    <h5>Assign FGroup</h5>
    <table class="fgroup-input-area">
        <tr>
            <td>
                <select id="input-select-fgroup" class="form-control input-fgroup">
                    <option class="fgroup" value="-1" selected>--Pilih FGroup--</option>
                    <!-- List FGroup -->
                </select>
            </td>
            <td>&nbsp;
                <button type="button" class="btn btn-primary btn-md add-fgroup-btn">Add</button>
            </td>
        </tr>
    </table>

    <br /><br />
    <table class="table table-striped table-hover table-bordered table-detail-fgroup">
        <thead>
            <th>FGroup Name</th>
            <th style="text-align:center; width:70px;">Action</th>
        </thead>
        <tbody>
            <!-- Body Content -->
        </tbody>
    </table>
</div>

<c:if test="${showInfoNoGroup==false}">
<div id="save-group-button" style="margin-left:18px;">
    <button type="button" class="btn btn-primary btn-sm save-group-btn">Save Group</button>
    <button id="del-group-btn" type="button" onclick="deleteGroup()" class="btn btn-primary btn-sm">Hapus Group</button>
</div>
</c:if>

<script>
    $(document).ready(function(){
        document.title = "Edit Group";

        <c:choose>
            <c:when test="${showInfoNoGroup==false}">
                refreshGroupList(0);
                refreshFGroupList();
                setEventDropdown();
                editNameButton();
            </c:when>
            <c:otherwise>
                disableAllFunc();
            </c:otherwise>
        </c:choose>
    })

    function disableAllFunc(){
        $("select#input-select-group").prop("disabled", true);
        $("button.edit-name-btn").prop("disabled", true);
        $("button.add-fgroup-btn").prop("disabled", true);
    }

    function editNameButton(){
        $(".edit-name-btn").off().on("click", function(){
            if($("tr.edit-name").hasClass("show")){
                $("tr.edit-name").hide();
            } else
                $("tr.edit-name").show();

            $("div.edit-name").slideToggle(200);
        })
    }

    function refreshFGroupList(){
        var ajax = $.ajax({
                        url: "<c:url value='/fgroup/get-all-no-security' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $.each(response.data, function(i,item){
                    htmlOption = "<option class='fgroup' value='" + item.fgroupId + "'>" + item.fgroupName + "</option>";
                    $('#input-select-fgroup').append(htmlOption);
                });

                addFGroupBtn();
            }
            else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function refreshGroupList(selectedId){
        var ajax = $.ajax({
                        url: "<c:url value='/group/get' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $("#input-select-group").html("");

                $.each(response.data, function(i,item){
                    var htmlOption;
                    if(selectedId==0){
                        if(i==0) {
                            htmlOption = "<option class='group-list' value='" + item.groupId + "' selected>" + item.groupName + "</option>"

                            $("input.group-name").val(item.groupName);
                        }
                        else {
                            htmlOption = "<option class='group-list' value='" + item.groupId + "'>" + item.groupName + "</option>"
                        }
                    } else {
                        if(item.groupId==selectedId){
                            htmlOption = "<option class='group-list' value='" + item.groupId + "' selected>" + item.groupName + "</option>"

                            $("input.group-name").val(item.groupName);
                        } 
                        else {
                            htmlOption = "<option class='group-list' value='" + item.groupId + "'>" + item.groupName + "</option>"
                        }
                    }
                    

                    $('#input-select-group').append(htmlOption);
                });

                refreshTreeViewChecked(parseInt($("option.group-list:selected").val()));
                refreshFGroupTable(parseInt($("option.group-list:selected").val()));
            }
            else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function refreshTreeViewChecked(groupId){
        var ajax = $.ajax({
                        url: "<c:url value='/menu/get-full-checked?groupId="+groupId+"' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $('.tree-view-checked').children().remove();

                response.data.sort(SortMenu);

                $.each(response.data, function(i,item){
                    var htmlMenu = "";
                    htmlMenu += getFullMenu(item);

                    $('.tree-view-checked').append(htmlMenu);

                    setToggleButton();
                });								

                setOpenAllMenu();
                setEventCheckbox();
                saveGroupButton();
                
                if(getSelectedOptGroup()=='${securityGroup}'){
                		disableAllActions();
                } else {
                		enableAllActions();
                }
            }
            else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function SortMenu(a, b){
          var aName = a.title.toLowerCase();
          var bName = b.title.toLowerCase();
          return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
    }

 function getFullMenu(menu){
        var htmlMenu;
        var link = getLink(menu);
        var name = menu.title;
				
        if(hasSubMenu(menu)){
            if(menu.checkedStatus==1){
            		if(menu.title!='${manageGroup}' && menu.title!='${manageFGroup}' && menu.title!='${addGroup}' && menu.title != '${editGroup}'){
            				htmlMenu = "<li>"
		                    + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' checked />&nbsp;&nbsp;"
		                    + "<button style='width:20px;' class='toggleButton btn btn-default btn-xs'>-</button>&nbsp;&nbsp;"
		                    + menu.title + "<ul class='inner-menu-eg'>";
            		} else {
            				htmlMenu = "<li>"
		                    + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' checked disabled />&nbsp;&nbsp;"
		                    + "<button style='width:20px;' class='toggleButton btn btn-default btn-xs'>-</button>&nbsp;&nbsp;"
		                    + menu.title + "<ul class='inner-menu-eg'>";
            		}
                
            } else {
            		if(menu.title!='${manageGroup}' && menu.title!='${manageFGroup}' && menu.title!='${addGroup}' && menu.title != '${editGroup}'){
            				htmlMenu = "<li>"
		                    + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' />&nbsp;&nbsp;"
		                    + "<button style='width:20px;' class='toggleButton btn btn-default btn-xs'>-</button>&nbsp;&nbsp;"
		                    + menu.title + "<ul class='inner-menu-eg'>";
            		} else {
            				htmlMenu = "<li>"
		                    + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' disabled />&nbsp;&nbsp;"
		                    + "<button style='width:20px;' class='toggleButton btn btn-default btn-xs'>-</button>&nbsp;&nbsp;"
		                    + menu.title + "<ul class='inner-menu-eg'>";
            		}
            }

            $.each(menu.subMenus, function(i, subMenu){
                htmlMenu += getFullMenu(subMenu);
            })

            htmlMenu += "</ul></li>";
        } else {
            if(menu.checkedStatus==1){
            		if(menu.title!='${manageGroup}' && menu.title!='${manageFGroup}' && menu.title!='${addGroup}' && menu.title != '${editGroup}'){
            				htmlMenu = "<li>"
                        + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' checked/>&nbsp;&nbsp;"
                        + "<button style='width:20px;' class='btn btn-default btn-xs'>o</button>&nbsp;&nbsp;"
                        + menu.title + "</li>";
            		} else {
            				htmlMenu = "<li>"
                        + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' checked disabled/>&nbsp;&nbsp;"
                        + "<button style='width:20px;' class='btn btn-default btn-xs'>o</button>&nbsp;&nbsp;"
                        + menu.title + "</li>";
            		}
            } else {
            		if(menu.title!='${manageGroup}' && menu.title!='${manageFGroup}' && menu.title!='${addGroup}' && menu.title != '${editGroup}'){
            				htmlMenu = "<li>"
                        + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' />&nbsp;&nbsp;"
                        + "<button style='width:20px;' class='btn btn-default btn-xs'>o</button>&nbsp;&nbsp;"
                        + menu.title + "</li>";
            		} else {
            				htmlMenu = "<li>"
                        + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' disabled />&nbsp;&nbsp;"
                        + "<button style='width:20px;' class='btn btn-default btn-xs'>o</button>&nbsp;&nbsp;"
                        + menu.title + "</li>";
            		}
            }

        }

        return htmlMenu;
    }   

    function setToggleButton(){
        $('.toggleButton').off().on('click', function (e) {
            //e.preventDefault();

            var $this = $(this);

            if($this.text()=="+")
                $this.text("-");
            else
                $this.text("+");

            if($this.next().length>0){
                if($this.next().is(":visible")){
                    $this.next(".inner-menu-eg").slideUp(300);
                }
                else {
                    $this.next(".inner-menu-eg").slideDown(300);
                }
            }
        });
    }

    function setOpenAllMenu(){
        $(".inner-menu-eg").fadeIn(200);
    }

    function hasSubMenu(menu){
        return menu.subMenus.length>0;
    }

    function setEventCheckbox(){
        $(".input-menu").on("change", function(){
            var childrens = $(this).parent().find("input");

            if($(this).is(":checked")){ //Checked
                /*Check anak anaknya juga*/
                $.each(childrens,function(i,item){
                    $(this).prop("checked", true);
                })

                var $this = $(this);

                checkParent($this);
            }
            else {  //Unchecked
                /*Uncek anak-anaknya*/
                $.each(childrens,function(i,item){
                    $(this).prop("checked", false);
                })

                unCheckParent($(this));
            }
        })
    }

    function unCheckParent(checkbox){
        if(!hasSibling(checkbox)){
            var parent = checkbox.closest("ul.inner-menu-eg").prev().prev();
            parent.prop("checked",false);

            if(checkbox.parent().parent().hasClass("tree-view-checked") == false)
                unCheckParent(parent);
        }
    }

    function hasSibling(checkbox){
        var valThis = parseInt(checkbox.val());
        var parent = checkbox.parent().parent();
        var list = parent.children("li");
        var status = false;

        if(list.length<2)   //Tidak Punya Sibling
            return false;

        $.each(list, function(){
            var valSibling = parseInt($(this).children().first().val());
            if(valSibling != valThis && $(this).children().first().is(":checked")){
                status = true;
                return false;   //break loop
            }
        })

        return status;
    }

    function checkParent(objek){
        while(objek.hasClass("tree-view-checked")==false){
            if(objek.hasClass("inner-menu-eg")){
                objek.prev().prev().prop("checked", true);
            }

            objek = objek.parent();
        }
    }

    function setEventDropdown(){
        $("#input-select-group").on("change", function(){
            var groupId = parseInt($("option.group-list:selected").val());
            refreshTreeViewChecked(groupId);
            refreshFGroupTable(groupId);

            $("input.group-name").val($("option.group-list:selected").text());
            $(".edit-name").hide();
        })
    }

    function saveGroupButton(){
        $(".save-group-btn").off().on("click", function(){
            var groupId = parseInt($("option.group-list:selected").val());
            var groupName = $("input.group-name").val();
            var menus = [];
            var fgroups = [];

            $.each($(".input-menu"),function(i,item){
                if($(this).is(":checked")){
                    menus.push(parseInt($(this).val()));
                }
            })

            $.each($("input.fgroup-id"),function(i,item){
                fgroups.push(parseInt($(this).val()));
            })

            var data = {
                "group" : {
                    "groupId" : groupId,
                    "groupName" : groupName
                },
                "menuIds" : menus,
                "fgroupIds" : fgroups
            }

            var ajax = $.ajax({
                url: "<c:url value='/group/edit?groupId="+groupId+"'/>",
                timeout: 30000,
                type : "PUT",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(data)
            })

            ajax.done(function (response) {
                alert(response.message);
                //refreshTreeViewChecked(groupId);
                refreshGroupList(groupId);
            })

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            })
        })
    }

    function addFGroupBtn(){
        $(".add-fgroup-btn").off().on("click", function(){
            var id = parseInt($("option.fgroup:selected").val());
            var name = $("option.fgroup:selected").text();


            var html = "<tr><td>"
                        + "<input class='fgroup-id' type='hidden' value='"+id+"' />"
                        + name + "</td>"
                        + "<td><button type='button' class='btn btn-default btn-xs delete-btn'>Delete</button></td>"
                        + "</tr>";

            if(isAlreadyExist(id)==true){
                alert("Fgroup " + name + " sudah di-assign pada group ini!");
            }
            else {
                if(id!=-1){
                    $(".table-detail-fgroup").append(html);
                    deleteItem();
                }
            }
        });
    }

    function isAlreadyExist(fgroupId){

        var listInput = [];
        var status = false;
        $.each($("input.fgroup-id"), function(){
            var value = parseInt($(this).val());
            if(value==fgroupId){
                status=true;
                return false;   //break loop
            }
        });

        return status;
    }

    function deleteItem(){
        $(".delete-btn").off().on("click", function(){
            $(this).parent().parent().remove();
        })
    }

    function refreshFGroupTable(groupId){
        var ajax = $.ajax({
                        url: "<c:url value='/fgroup/get?groupId="+groupId+"' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $(".table-detail-fgroup tbody").children().remove();

                $.each(response.data, function(i,item){
                    var html = "<tr><td>"
                                + "<input class='fgroup-id' type='hidden' value='"+item.fgroupId+"' />"
                                + item.fgroupName + "</td>"
                                + "<td><button type='button' class='btn btn-default btn-xs delete-btn'>Delete</button></td>"
                                + "</tr>";

                    $('.table-detail-fgroup').append(html);
                });

                deleteItem();
                if(getSelectedOptGroup()=='${securityGroup}'){
                		disableDeleteFGroup();
                } else {
                		enableDeleteFGroup();
                }
            }
            else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function deleteGroup(){
        var groupId = parseInt($("option.group-list:selected").val());

        var status = confirm("Apakah anda yakin akan menghapus grup ini??");

        if(status){
            var ajax = $.ajax({
                url: "<c:url value='/group/delete?groupId="+groupId+"'/>",
                timeout: 30000,
                type : "DELETE"
            })

            ajax.done(function (response) {
                alert(response.message);
                refreshGroupList(0);
            })

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            })
        }
    }
    
    function getSelectedOptGroup(){
    		return $("option.group-list:selected").text();
    }
    
    function disableAllActions(){
    		$("button.edit-name-btn").prop("disabled", true);
    		$("input[name='chosenMenu']").prop("disabled", true);
    		$("button.save-group-btn").prop("disabled", true);
    		$("button#del-group-btn").prop("disabled", true);
    		$("select#input-select-fgroup").prop("disabled", true);
    		$("button.add-fgroup-btn").prop("disabled", true);
    }
    
    function enableAllActions(){
    		$("button.edit-name-btn").prop("disabled", false);
    		$("button.save-group-btn").prop("disabled", false);
    		$("button#del-group-btn").prop("disabled", false);
    		$("select#input-select-fgroup").prop("disabled", false);
    		$("button.add-fgroup-btn").prop("disabled", false);
    }
    
    function disableDeleteFGroup(){
    		$("button.delete-btn").prop("disabled", true);
    }
    
    function enableDeleteFGroup(){
    		$("button.delete-btn").prop("disabled", false);
    }
</script>
