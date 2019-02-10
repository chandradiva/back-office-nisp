<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html >
    <head>
        <meta charset="UTF-8">
        <sec:csrfMetaTags />
        <title>NISP</title>
        <jsp:include page="../includes/css.jsp"></jsp:include>
        <link href="<c:url value="/resources/css/menu.css" />" rel="stylesheet"/>
    </head>

    <body>
    
    <div id="divLoadingBo">
    </div>
    
    <div id="wrap">
    <jsp:include page="../includes/header.jsp"></jsp:include>

        <div class="page-sidebar">
            <ul class="accordion">
                <!--sidebar menu-->
            </ul>
        </div>

        <input type="hidden" id="current-page-link" value="" />
        <div id="page-content" class="page-content">
            <h4>Dashboard</h4>
        </div>

    </div>

    <jsp:include page="../includes/footer.jsp"></jsp:include>
	<jsp:include page="../includes/js.jsp"></jsp:include>
	<script>
        $(document).ready(function(){
            refreshSidebarMenu();
            setToggle();
        });

        function refreshSidebarMenu(){
            var ajax = $.ajax({
                            url: "<c:url value='/menu/get-sidebar' />",
                            timeout : 30000,
                            type : 'get'
                        });
            ajax.always(function(){

            });
            ajax.done(function (response) {
                if (response.resultCode == 1000) {
                    response.data.sort(SortMenu);
                    var initLink = "";

                    $.each(response.data, function(i,item){
                        var htmlMenu = getFullMenu(item);

                        $('.accordion').append(htmlMenu);

                        setToggle();
                        clickAccordion();
                    });

                    $.each(response.data, function(i,item){
                        initLink = getInitLink(item);

                        if(initLink!=""){
                            initPageContent(initLink);
                            return false;   //break;
                        }
                    });
                } else {
                    if(response.resultCode == 5001)
                        //alert(response.message);
                    window.location.href = '<c:url value="/login" />';
                }
            });
            ajax.fail(function(xhr,status){
                console.log('Failed: '+status);
            });
        }

        function getInitLink(menu){
            var link = "";

            if(menu.flag==0){
                link = getLink(menu);
                return link;   //break;
            } else {
                if(hasSubMenu(menu)){
                    var i;
                    var length = menu.subMenus.length;

                    for(i=0;i<length;i++){
                        link = getInitLink(menu.subMenus[i]);

                        if(link!=""){
                            break;
                        }
                    }
                }

                return link;
            }            
        }

        function hasSubMenu(menu){
            return menu.subMenus.length>0;
        }

        function SortMenu(a, b){
          var aName = a.title.toLowerCase();
          var bName = b.title.toLowerCase();
          return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
        }

        function getLink(menu){
            if(menu.flag==1)
                return "javascript:void(0);";
            else
                return menu.link;
        }

        function setToggle(){
            $('.toggle').off().on('click', function (e) {
                //e.preventDefault();

                var $this = $(this);
                if($this.next().length>0){
                    if($this.next().is(":visible")){
                        $this.next(".inner-menu").slideUp(300);
                    }
                    else {
                        $this.next(".inner-menu").slideDown(300);
                    }
                    //$this.next().toggle(500);
                }
            });
        }

        function clickAccordion(){
            $('.open-body').click(function() {
                var link = $(this).attr("link");
                
                $("#current-page-link").val(link);
                
                //$("#divLoadingBo").addClass("show");
                $("#page-content").load(link, function(){
                	//$("#divLoadingBo").removeClass("show");	
                });                
            });
        }

        function initPageContent(link){
            $("#page-content").load("<c:url value='"+link+"' />");
            $("#current-page-link").val(link);
            openSidebarTree(link);
        }

        function openSidebarTree(link){
            var $target = null;

            $.each($("ul.accordion").find("a.open-body"), function(){
                if($(this).attr("link") == link){
                    $target = $(this);
                    return false;   //break;
                }
            });

            if($target!=null){
                while($target.hasClass("accordion")==false){
                    $target.show();
                    $target = $target.parent();
                }
            }
        }

        function getFullMenu(menu){
            var htmlMenu;
            var link = getLink(menu);
            var name = menu.title;

            if(menu.flag==0){
                htmlMenu = "<li><a class='toggle open-body' link='"+ link +"'>"+ name + "</a></li>";
            }
            else {
                htmlMenu = "<li><a class='toggle' href='"+link+"'>"+ name + "</a>" + "<ul class='inner-menu'>";

                $.each(menu.subMenus, function(i, subMenu){
                    htmlMenu += getFullMenu(subMenu);
                })

                htmlMenu += "</ul></li>";
            }
            return htmlMenu;
        }
    </script>
    
    </body>
</html>
