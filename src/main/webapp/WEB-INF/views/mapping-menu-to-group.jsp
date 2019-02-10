<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h4>Mapping Menu To Group</h4>
<br />

<div id="div-menu">
    <h5>Menu</h5>
    <div class="row">
        <div class="col-sm-3">
            <select id="input-select-menu" class="form-control ">
                <!--refreshMenuList()-->
            </select>
        </div>
    </div>

    <br />
    <h5>Pilih group untuk menu ini</h5>
    <div id="menus-tree">
        <ul class="group-list-view">
            <!--refreshCheckboxListView()-->
        </ul>
    </div>
</div>

<div id="save-mapping-button" style="margin-left:18px;">
    <button type="button" class="btn btn-primary btn-sm save-mapping-btn">Save Mapping</button>
</div>

<script>
    $(document).ready(function(){
        document.title = "Mapping Menu to Group";

        refreshMenuList();
        setEventDropdown();
    });

    function refreshMenuList(){
        var ajax = $.ajax({
                        url: "<c:url value='/menu/get-all-children' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $("#input-select-menu").html("");   //clear

                response.data.sort(SortMenu);   //Sort by title

                $.each(response.data, function(i,item){
                    var htmlOption;
                    if(i==0)
                        htmlOption = "<option class='opt-menu' value='" + item.menuId + "' selected>" + item.title + "</option>"
                    else
                        htmlOption = "<option class='opt-menu' value='" + item.menuId + "'>" + item.title + "</option>"

                    $('#input-select-menu').append(htmlOption);
                });

                refreshCheckboxListView(parseInt($("option.opt-menu:selected").val()));
            }
            else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function refreshCheckboxListView(menuId){
        var ajax = $.ajax({
                        url: "<c:url value='/group/get-with-checked?menuId="+menuId+"' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $('.group-list-view').children().remove();  //Clear

                response.data.sort(SortGroup);  //sort by group name

                $.each(response.data, function(i,item){
                    var html;

                    if(item.checkedStatus==1){
                    		if(item.groupName=='${securityGroup}'){
                    				html = "<li><input type='checkbox' name='chosenGroup' value='"+ item.groupId +"' checked disabled/> &nbsp;"+item.groupName+"</li>";
                    		}
                    		else {
                    				html = "<li><input type='checkbox' name='chosenGroup' value='"+ item.groupId +"' checked /> &nbsp;"+item.groupName+"</li>";
                    		}        
                    }
                    else {
                    		if(item.groupName=='${securityGroup}'){
                    				html = "<li><input type='checkbox' name='chosenGroup' value='"+ item.groupId +"' disabled/> &nbsp;"+item.groupName+"</li>";
                    		}
                    		else {
                    				html = "<li><input type='checkbox' name='chosenGroup' value='"+ item.groupId +"' /> &nbsp;"+item.groupName+"</li>";
                    		}
                    }

                    $('.group-list-view').append(html);
                });

                saveMappingButton();
                
                if(getSelectedOptMenu()=='${manageGroup}' || getSelectedOptMenu()=='${manageFGroup}' 
                		|| getSelectedOptMenu()=='${addGroup}' || getSelectedOptMenu()=='${editGroup}'){
                		disableAll();
                } else {
                		enableAll();
                }
            }
            else {
                console.log(response.message);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function SortGroup(a, b){
          var aName = a.groupName.toLowerCase();
          var bName = b.groupName.toLowerCase();
          return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
    }

    function SortMenu(a, b){
          var aName = a.title.toLowerCase();
          var bName = b.title.toLowerCase();
          return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
    }

    function saveMappingButton(){
        $(".save-mapping-btn").off().on("click", function(){
            var menuId = parseInt($("option.opt-menu:selected").val());
            var groups = [];

            $.each($("input[name='chosenGroup']"),function(i,item){
                if($(this).is(":checked")){
                    groups.push(parseInt($(this).val()));
                }
            });

            var status = confirm("Apakah anda yakin dengan mapping ini??");

            if(status==1){
                var ajax = $.ajax({
                    url: "<c:url value='/group/map-menu?menuId="+menuId+"'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(groups)
                });
                ajax.done(function (response) {
                    alert(response.message);
                    refreshCheckboxListView(menuId);
                });
                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
        })
    }

    function setEventDropdown(){
        $("#input-select-menu").on("change", function(){
            var menuId = parseInt($("option.opt-menu:selected").val());
            refreshCheckboxListView(menuId);
        })
    }
    
    function getSelectedOptMenu(){
    		var selectedOptMenu = $("option.opt-menu:selected").text();
    		return selectedOptMenu;
    }
    
    function disableAll(){
    		$("input[name='chosenGroup']").prop("disabled", true);
    		$("button.save-mapping-btn").prop("disabled", true);
    }
    
    function enableAll(){
    		//$("input[name='chosenGroup']").prop("disabled", false);
    		$("button.save-mapping-btn").prop("disabled", false);
    }
</script>
