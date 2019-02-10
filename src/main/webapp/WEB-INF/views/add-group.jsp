<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
ul .inner-menu-ag {
  padding-left: 18px;
  overflow: hidden;
  display: none;
}
</style>

<h4>Add Group</h4>
<br />

<div style="width:20%;">
    <input type="text" class="form-control input-group-name" maxlength="50" placeholder="Nama Grup"/>
</div>

<h5>Pilih Menu untuk grup ini</h5>
<div id="menus-tree">
    <ul class="tree-view-full-menus">
    </ul>
</div>

<div id="save-group-button" style="margin-left:16px;">
    <button type="button" class="btn btn-primary btn-sm save-group-btn">Save Group</button>
</div>


<script>
    $(document).ready(function(){
        document.title = "Add Group";

        refreshTreeViewFullMenus();
    })

    function refreshTreeViewFullMenus(){
        var ajax = $.ajax({
                        url: "<c:url value='/menu/get-full-menus' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $('.tree-view-full-menus').children().remove();

                response.data.sort(SortMenu);

                $.each(response.data, function(i,item){

                    var htmlMenu = "";
                    htmlMenu += getFullMenu2(item);

                    $('.tree-view-full-menus').append(htmlMenu);

                    setToggleButton();
                });

                setOpenAllMenu();
                setEventCheckbox();
                saveGroupButton();
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

    function getFullMenu2(menu){
        var htmlMenu;
        var link = getLink(menu);
        var name = menu.title;

        if(hasSubMenu(menu)){
        		if(menu.title!='${manageGroup}' && menu.title!='${manageFGroup}' && menu.title!='${addGroup}' && menu.title != '${editGroup}'){
        				htmlMenu = "<li>"
                    + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' />&nbsp;&nbsp;"
                    + "<button style='width:20px;' class='toggleButton btn btn-default btn-xs'>-</button>&nbsp;&nbsp;"
                    + menu.title + "<ul class='inner-menu-ag'>";
        		}
        		else {
        				htmlMenu = "<li>"
                    + "<input type='checkbox' class='input-menu' name='chosenMenu' value='"+ menu.menuId +"' disabled />&nbsp;&nbsp;"
                    + "<button style='width:20px;' class='toggleButton btn btn-default btn-xs'>-</button>&nbsp;&nbsp;"
                    + menu.title + "<ul class='inner-menu-ag'>";
        		}            

            $.each(menu.subMenus, function(i, subMenu){
                htmlMenu += getFullMenu2(subMenu);
            })

            htmlMenu += "</ul></li>";
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
                    $this.next(".inner-menu-ag").slideUp(300);
                }
                else {
                    $this.next(".inner-menu-ag").slideDown(300);
                }
            }
        });
    }

    function setOpenAllMenu(){
        $(".inner-menu-ag").slideDown();
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
            var parent = checkbox.closest("ul.inner-menu-ag").prev().prev();
            parent.prop("checked",false);

            if(checkbox.parent().parent().hasClass("tree-view-full-menus") == false)
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
        while(objek.hasClass("tree-view-full-menus")==false){
            if(objek.hasClass("inner-menu-ag")){
                objek.prev().prev().prop("checked", true);
            }

            objek = objek.parent();
        }
    }

    function saveGroupButton(){
        $(".save-group-btn").off().on("click", function(){
            var groupName = $(".input-group-name").val();
            var menus = [];

						if(groupName!=""){
								$.each($(".input-menu"),function(i,item){
		                if($(this).is(":checked")){
		                    menus.push(parseInt($(this).val()));
		                }
		            })
		
		            var data = {
		                "group" : {
		                    "groupName" : groupName
		                },
		                "menuIds" : menus
		            }
		
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
		
		                $(".input-group-name").val("");
		                refreshTreeViewFullMenus();
		            })
		
		            ajax.fail(function(xhr, status){
		                console.log('Failed: '+status);
		            })
						}
						else {
								alert("Nama Group tidak boleh kosong");
						}
        })
    }
</script>
