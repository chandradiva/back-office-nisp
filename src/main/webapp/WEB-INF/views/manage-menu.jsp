<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    table.table-detail-menu tbody tr td {
        vertical-align: middle;
    }

    table.table-detail-menu thead th {
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

    input.in-table.the-title.the-subtitle {
        width: 250px;
    }

    input[type='radio'] {
        vertical-align: top;
        cursor: pointer;
    }

    input[type='radio']:checked {
        background: rgb(230,33,41);
    }
</style>

<h4>Manage Menu</h4>
<br/>

<button type="button" class="btn btn-default add-menu-btn">Add Menu</button>
<div class="add-menu-area" style="display:none;">
    <h5>Pilih Parent Menu</h5>
    <div id="parents-tree">
        <ul class="tree-view">

        </ul>
    </div>

    <br />
    <table class="table-input">
        <tr>
            <td>Title</td>
            <td style="width:10px;"> : </td>
            <td>
                <input type="text" class="form-control input-title" maxlength="50"/>
            </td>
        </tr>
        <tr>
            <td>Subtitle</td>
            <td style="width:10px;"> : </td>
            <td><input type="text" class="form-control input-subtitle" maxlength="50"/></td>
        </tr>
        <tr>
            <td>Tipe</td>
            <td style="width:10px;"> : </td>
            <td>
                <select id="input-select-menu" class="form-control input-tipe">
                    <option value="1" selected>Parent</option>
                    <option value="0">Child</option>
                </select>
            </td>
        </tr>
        <tr class="row-link" style="display:none;">
            <td>Link</td>
            <td style="width:10px;"> : </td>
            <td><input type="text" class="form-control input-link" value="" maxlength="255" disabled/></td>
        </tr>
        <tr class="row-link" style="display:none;">
            <td>Activity</td>
            <td style="width:10px;"> : </td>
            <td><input type="text" class="form-control input-action" value="" maxlength="255" disabled/></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>
                <button type="button" class="btn btn-primary btn-sm save-menu-btn">Save</button>
            </td>
        </tr>
    </table>
    <br />
</div>

<div class="table-responsive">
    <table class="table table-striped table-hover table-bordered table-detail-menu">
        <thead>
            <th style="width:25px;">No.</th>
            <th style="width:250px;">Title</th>
            <th style="width:75px;">Subtitle</th>
            <th>Link</th>
            <th>Activity Description</th>
            <th style="width:75px;">Tipe</th>
            <th style="width:150px;">Action</th>
        </thead>
        <tbody>
            <!--Content Menu-->
            <tr id="tr-no-row" style="display:none;">
                <td colspan="7" style="text-align: center;">- No Record -</td>
            </tr>
        </tbody>
    </table>
</div>
<div style="text-align:right; margin-top:10px;">
    <button type="button" class="btn btn-primary btn-sm save-edited-btn">Save All</button>
    <button type="button" class="btn btn-primary btn-sm cancel-edited-btn">Cancel All</button>
</div>

<script>
    $(document).ready(function(){
        document.title = "Manage Menu";

        setAddMenuArea();
        enableDisableLink();
        saveBtnClicked();
        refreshTreeView();
        refreshTable();
    })

    function refreshTreeView(){
        var ajax = $.ajax({
                        url: "<c:url value='/menu/get-parents' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $('.tree-view').children().remove();

                // response.data.sort(function(a, b) {
                //     return parseInt(a.menuId) - parseInt(b.menuId);
                // })
                response.data.sort(SortMenu);

                $.each(response.data, function(i,item){

                    var htmlMenu = "";
                    htmlMenu += getFullMenu(item);

                    $('.tree-view').append(htmlMenu);

                    setToggle();
                });

                htmlMenu = "<li>"
                        + "<input type='radio' class='radioBtn' name='chosenParent' value='0' checked />&nbsp;&nbsp;"
                        + "<button style='width:20px;' class='btn btn-default btn-xs'>o</button>&nbsp;&nbsp;No Parent</li>";
                $('.tree-view').append(htmlMenu);
            }
            else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function setToggle(){
        $('.toggleBtn').off().on('click', function (e) {
            //e.preventDefault();

            var $this = $(this);

            if($this.text()=="+")
                $this.text("-");
            else
                $this.text("+");

            if($this.next().length>0){
                if($this.next().is(":visible")){
                    $this.next(".inner-menu").slideUp(300);
                }
                else {
                    $this.next(".inner-menu").slideDown(300);
                }
            }
        });
    }

    function getFullMenu(menu){
        var htmlMenu;
        var link = getLink(menu);
        var name = menu.title;

        if(hasSubMenu(menu)){
            htmlMenu = "<li>" +
                    "<input type='radio' class='radioBtn' name='chosenParent' value='"+ menu.menuId +"' />&nbsp;&nbsp;" +
                "<button style='width:20px;' class='toggleBtn btn btn-default btn-xs'>+</button>&nbsp;&nbsp;"+ menu.title + "<ul class='inner-menu'>";

            $.each(menu.subMenus, function(i, subMenu){
                htmlMenu += getFullMenu(subMenu);
            })

            htmlMenu += "</ul></li>";
        } else {
            htmlMenu = "<li>"+
                    "<input type='radio' class='radioBtn' name='chosenParent' value='"+ menu.menuId +"' />&nbsp;&nbsp;"
                + "<button style='width:20px;' class='btn btn-default btn-xs'>o</button>&nbsp;&nbsp;" + menu.title + "</li>";
        }

        return htmlMenu;
    }

    function hasSubMenu(menu){
        return menu.subMenus.length>0;
    }

    function setAddMenuArea(){
        $(".add-menu-btn").click(function (){
            $(".add-menu-area").toggle(300);
        });
    }

    function enableDisableLink(){
        $(".input-tipe").off().on('change', function(){
            var $this = $(this);
            var tipe = parseInt($this.val());

            if(tipe == 0){
                $(".input-link").prop("disabled", false);
                $(".input-action").prop("disabled", false);
                $(".row-link").show();
            }
            else {
                $(".input-link").prop("disabled", true);
                $(".input-action").prop("disabled", true);
                $(".input-link").val("");
                $(".input-action").val("");
                $(".row-link").hide();
            }
        })
    }

    function saveBtnClicked(){
        $(".save-menu-btn").off().on("click", function(){
            var title = $(".input-title").val();
            var subtitle = $(".input-subtitle").val();
            var link = $(".input-link").val();
            var action = $(".input-action").val();
            var flag = parseInt($(".input-tipe").val());
            var parentId = $('input[name=chosenParent]:checked').val();

            var data = {
                "title" : title,
                "subtitle" : subtitle,
                "link" : link,
                "flag" : flag,
                "action" : action
            }

            var ajax = $.ajax({
                url: "<c:url value='/menu/add?parentId=" + parentId + "'/>",
                timeout: 30000,
                type : "POST",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(data)
            })
            ajax.done(function (response) {
                alert(response.message);
                refreshTreeView();
                refreshTable();
                clearForm();
            })

            ajax.fail(function(xhr, status){
                console.log('Failed: '+status);
            })
        })
    }

    function clearForm(){
        $(".input-title").val("");
        $(".input-subtitle").val("");
        $(".input-tipe").val("1");
        $(".input-link").val("");
        $(".input-action").val("");
        $(".input-link").prop("disabled", true);
        $(".input-action").prop("disabled", true);
        $(".row-link").hide();
    }

    function showNoRecord(){
        $("tr#tr-no-row").show();
    }
    
    function hideNoRecord(){
    	$("tr#tr-no-row").hide();
    }
    
    function refreshTable(){
        var ajax = $.ajax({
                        url: "<c:url value='/menu/get' />",
                        timeout : 30000,
                        dataType : "JSON",
                        type : "GET"
                    });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                clearTBody();
                hideNoRecord();
                response.data.sort(SortMenu);

                if(response.data.length==0){
                    showNoRecord();
                }
                
                $.each(response.data, function(i,item){
                    var tipe = item.flag==1 ? "parent" : "child";
                    if(item.flag==1){
                        var row = "<tr><td style='text-align:center;'>" + (i+1) + ".</td>"
                                + "<td><input type='hidden' class='old-the-title' value='"+ (item.title!=null ? item.title : "") +"' /><input type='text' class='form-control in-table the-title' value='" + (item.title!=null ? item.title : "") + "' disabled/></td>"
                                + "<td><input type='hidden' class='old-the-subtitle' value='"+ (item.subtitle!=null ? item.subtitle : "") +"' /><input type='text' class='form-control in-table the-subtitle' value='" + (item.subtitle!=null ? item.subtitle : "") + "' disabled/></td>"
                                + "<td></td><td></td>"
                                + "<td>" + tipe + "</td><td style='text-align:center;'>"
                                + "<input type='hidden' name='input-menu-id' value='"+ item.menuId +"'/>"
                                + "<button type='button' class='btn btn-default btn-sm edit-btn'>Edit</button>&nbsp;"
                                + "<button type='button' class='btn btn-default btn-sm delete-btn'>Delete</button></td></tr>";
                    }
                    else {
                        var row = "<tr><td style='text-align:center;'>" + (i+1) + ".</td>"
                                + "<td><input type='hidden' class='old-the-title' value='"+ (item.title!=null ? item.title : "") +"' /><input type='text' class='form-control in-table the-title' value='" + (item.title!=null ? item.title : "") + "' disabled/></td>"
                                + "<td><input type='hidden' class='old-the-subtitle' value='"+(item.subtitle!=null ? item.subtitle : "")+"' /><input type='text' class='form-control in-table the-subtitle' value='" + (item.subtitle!=null ? item.subtitle : "") + "' disabled/></td>"
                                + "<td><input type='hidden' class='old-the-link' value='"+ (item.link!=null ? item.link : "") +"' /><input type='text' class='form-control in-table the-link' value='" + (item.link!=null ? item.link : "") + "' disabled/></td>"
                                + "<td><input type='hidden' class='old-the-action' value='" + (item.action!=null ? item.action : "") + "' /><input type='text' class='form-control in-table the-action' value='" + (item.action!=null ? item.action : "") + "' disabled/></td>"
                                + "<td>" + tipe + "</td><td style='text-align:center;'>"
                                + "<input type='hidden' name='input-menu-id' value='"+ item.menuId +"'/>"
                                + "<button type='button' class='btn btn-default btn-sm edit-btn'>Edit</button>&nbsp;"
                                + "<button type='button' class='btn btn-default btn-sm delete-btn'>Delete</button></td></tr>";
                    }


                    $('.table-detail-menu tbody').append(row);
                });

                deleteMenu();
                editMenu();
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
    function deleteMenu(){
        $(".delete-btn").off().on('click', function(){
            var status = confirm("Apakah anda yakin akan menghapus data ini?");

            if(status==true){
                var $this = $(this);
                var menuId = $this.prev().prev().val();
                var ajax = $.ajax({
                    url: "<c:url value='/menu/delete?menuId=" + menuId + "'/>",
                    timeout: 30000,
                    type : "DELETE",
                    dataType: "json"
                })

                ajax.always(function(){

                })

                ajax.done(function (response) {
                    if(response.resultCode == 1000){    //success
                        refreshTable();
                        refreshTreeView();
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

    function editMenu(){
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

    function SortMenu(a, b){
      var flag1 = a.title.toLowerCase();
      var flag2 = b.title.toLowerCase();
      return ((flag1 < flag2) ? -1 : ((flag1 > flag2) ? 1 : 0));
    }

    function saveAllEdited(){
        $(".save-edited-btn").off().on("click", function(){
            var menus = [];

            var trs = $(".table-detail-menu tbody tr");
            $.each(trs, function(){
                var inputInTable = $(this).find("input.in-table:enabled");
                if(inputInTable.length<=0)
                    return true;    //continue;
                else {
                    var menu = new Object();
                    menu.menuId = parseInt($(this).find("input[name='input-menu-id']").first().val());
                    menu.title = inputInTable.eq(0).val();
                    menu.subtitle = inputInTable.eq(1).val();
                    menu.link = inputInTable.eq(2).val();
                    menu.action = inputInTable.eq(3).val();
                    menus.push(menu);
                }
            })

            if(menus.length==0){
                alert("Tidak ada data yang di-update");
                return;
            }

            var status = confirm("Apakah anda yakin akan mengupdate semua data ini?");

            if(status==true){
                var ajax = $.ajax({
                    url: "<c:url value='/menu/bulk-update'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(menus)
                });

                ajax.done(function (response) {
                    alert(response.message);
                    refreshTreeView();
                    refreshTable();
                    clearForm();
                })

                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
        })
    }

    function cancelAllEdited(){
        $(".cancel-edited-btn").off().on("click", function(){
            $.each($(".table-detail-menu").find("input.in-table"), function(){
                $(this).prop("disabled", true).val($(this).prev().val());
            })
            $(".edit-btn").text("Edit");
        })
    }

    function clearTBody(){
        $('.table-detail-menu tbody tr:not(#tr-no-row)').remove();
    }

</script>
