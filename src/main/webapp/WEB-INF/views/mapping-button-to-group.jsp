<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h4>Mapping Button To Group</h4>
<br />

<div id="div-button">
    <h5>Button</h5>
    <div class="row">
        <div class="col-sm-3">
            <select id="input-select-button" class="form-control ">
                <!--refreshButtonList()-->
            </select>
        </div>
    </div>

    <br />
    <h5>Pilih group untuk button ini</h5>
    <div id="buttons-tree">
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
        document.title = "Mapping Button to Group";

        refreshButtonList();
        setEventDropdown();
    });

    function refreshButtonList(){
        var ajax = $.ajax({
                        url: "<c:url value='/button/get-all' />",
                        timeout : 30000,
                        type : 'get'
                    });
        ajax.always(function(){

        });
        ajax.done(function (response) {
            if (response.resultCode == 1000) {
                $("#input-select-button").html("");   //clear

                response.data.sort(SortButton);   //Sort by title

                $.each(response.data, function(i,item){
                    var htmlOption;
                    if(i==0)
                        htmlOption = "<option class='opt-button' value='" + item.buttonId + "' selected>" + item.buttonTitle + "</option>"
                    else
                        htmlOption = "<option class='opt-button' value='" + item.buttonId + "'>" + item.buttonTitle + "</option>"

                    $('#input-select-button').append(htmlOption);
                });

                refreshCheckboxListView(parseInt($("option.opt-button:selected").val()));
            }
            else {
                console.log(response);
            }
        });
        ajax.fail(function(xhr,status){
            console.log('Failed: '+status);
        });
    }

    function refreshCheckboxListView(buttonId){
        var ajax = $.ajax({
                        url: "<c:url value='/group/get-group-button?buttonId="+buttonId+"' />",
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
                    				html = "<li><input type='checkbox' name='chosenButton' value='"+ item.groupId +"' checked disabled/> &nbsp;"+item.groupName+"</li>";
                    		}
                    		else {
                    				html = "<li><input type='checkbox' name='chosenButton' value='"+ item.groupId +"' checked /> &nbsp;"+item.groupName+"</li>";
                    		}
                    }
                    else {
                    		if(item.groupName=='${securityGroup}'){
                    				html = "<li><input type='checkbox' name='chosenButton' value='"+ item.groupId +"' disabled/> &nbsp;"+item.groupName+"</li>";
                    		}
                    		else {
                    				html = "<li><input type='checkbox' name='chosenButton' value='"+ item.groupId +"' /> &nbsp;"+item.groupName+"</li>";
                    		}
                    }

                    $('.group-list-view').append(html);
                });

                saveMappingButton();
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

    function SortButton(a, b){
          var aName = a.buttonTitle.toLowerCase();
          var bName = b.buttonTitle.toLowerCase();
          return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
    }

    function saveMappingButton(){
        $(".save-mapping-btn").off().on("click", function(){
            var buttonId = parseInt($("option.opt-button:selected").val());
            var groups = [];

            $.each($("input[name='chosenButton']"),function(i,item){
                if($(this).is(":checked")){
                    groups.push(parseInt($(this).val()));
                }
            });

            var status = confirm("Apakah anda yakin dengan mapping ini??");

            if(status==1){
                var ajax = $.ajax({
                    url: "<c:url value='/group/map-button?buttonId="+buttonId+"'/>",
                    timeout: 30000,
                    type : "PUT",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(groups)
                });
                ajax.done(function (response) {
                    alert(response.message);
                    refreshCheckboxListView(buttonId);
                });
                ajax.fail(function(xhr, status){
                    console.log('Failed: '+status);
                })
            }
        })
    }

    function setEventDropdown(){
        $("#input-select-button").on("change", function(){
            var buttonId = parseInt($("option.opt-button:selected").val());
            refreshCheckboxListView(buttonId);
        })
    }
</script>
