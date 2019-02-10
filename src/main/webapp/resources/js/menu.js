$(document).ready(function(){
    refreshMenu();
    cek();
});

function refreshMenu(){
    var ajax = $.ajax({
    	url: "/nisp/response-menu?group_id=1",            
       timeout : 30000,
       type : 'get'
     });
    ajax.always(function(){

    });
    ajax.done(function (response) {
        if (response.resultCode == 1000) {
            $.each(response.data.menus, function(i,item){
                var htmlMenu = getFullMenu(item);
                
                $('.accordion').append(htmlMenu);
                
                cek();
                clickAccordion();
            });
        } else {
            console.log(response);
        }
    });
    ajax.fail(function(xhr,status){
        console.log('Failed: '+status);
    });
}

function getLink(menu){
    if(menu.flag==1)
        return "javascript:void(0);";
    else
        return menu.link;
}

//function cek(){
//    $('.toggle').off().on('click', function (e) {
//        e.preventDefault();
//
//        var $this = $(this);
//
//        if ($this.next().hasClass('show')) {
//            $this.next().removeClass('show');
//            $this.next().slideUp(350);
//        } else {
//            $this.parent().parent().find('li .inner-menu').removeClass('show');
//            $this.parent().parent().find('li .inner-menu').slideUp(350);
//            $this.next().toggleClass('show');
//            $this.next().slideToggle(350);
//        }
//    });
//}

function cek(){
    $('.toggle').off().on('click', function (e) {
        //e.preventDefault();

        var $this = $(this);
        if($this.next().length>0){
            console.log("length : " + $this.next().length)
            console.log($this.next().is(":visible"));
            if($this.next().is(":visible")){
                $this.next(".inner-menu").slideUp(300);
                console.log("up");
            }
            else {
                $this.next(".inner-menu").slideDown(300);
                console.log("down");
            }
            //$this.next().toggle(500);
        }
    });
}

//function clickAccordion(){
//    $('.open-body').off().on('click', function() {
//        var link = $(this).attr("link");
//        $("#page-content").load("../" + link);
//    });
//}

function clickAccordion(){
    $('.open-body').click(function() {
        var link = $(this).attr("link");
        $("#page-content").load("../" + link);
    });
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