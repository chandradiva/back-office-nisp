<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Placed at the end of the document so the pages load faster -->
<script src=<c:url value="/resources/js/jquery-1.11.0.min.js"/> ></script>
<script src=<c:url value="/resources/js/bootstrap.min.js"/> ></script>

<!-- Placeholder Fix -->
<script src=<c:url value="/resources/js/jquery.placeholder.js"/> ></script>

<!-- === Addons === -->
<!-- Table -->
<script src=<c:url value="/resources/js/jquery.tablesorter.min.js"/> ></script>
<script src=<c:url value="/resources/js/jquery.tablesorter.widgets.min.js"/> ></script>
<script src=<c:url value="/resources/js/jquery.tablesorter.widgets-filter-formatter.min.js"/> ></script>
<script src=<c:url value="/resources/js/addons/pager/jquery.tablesorter.pager.js"/> ></script>
<script src=<c:url value="/resources/js/custom_table.js"/> ></script>

<!-- Date Picker -->
<script src=<c:url value="/resources/js/moment-with-locales.min.js"/> ></script>
<script src=<c:url value="/resources/js/bootstrap-datetimepicker.min.js"/> ></script>

<!-- Morris Chart -->
<script src=<c:url value="/resources/js/plugins/morris/morris.min.js"/> type="text/javascript"></script>
<script src=<c:url value="/resources/js/plugins/morris/raphael-min.js"/> type="text/javascript"></script>

<script src=<c:url value="/resources/js/jquery.circliful.min.js"/> ></script>
<script src=<c:url value="/resources/js/bootstrap-multiselect.js"/> ></script>

<!-- Fancybox -->
<script src=<c:url value="/resources/js/jquery.fancybox.pack.js"/> ></script>

<!-- FlexSlider @ home -->
<script defer src=<c:url value="/resources/js/jquery.flexslider-min.js"/> ></script>

<!-- Global JS -->
<script src=<c:url value="/resources/js/tools.js"/> ></script>

<!-- Validation-->
<script src=<c:url value="/resources/js/jquery.validate.min.js"/> ></script>

<!-- Footable -->	
<script src=<c:url value="/resources/js/plugins/footable/footable.js"/> type="text/javascript"></script>
<script src=<c:url value="/resources/js/plugins/footable/footable.sortable.js"/> type="text/javascript"></script>
<script src=<c:url value="/resources/js/jquery.validate.min.js"/> ></script>
<script src=<c:url value="/resources/js/jquery.validate.min.js"/> ></script>

<script src=<c:url value="/resources/js/custom.js"/> ></script>

<c:choose>
	<c:when test="${userSession == null}">
		<script type="text/javascript">
			var limit = 0;			
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			var limit = ${userSession.idleTime};
		</script>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
$.ajaxSetup({ cache: false });
var isSessionTimeout = false;
var currentSecond = 0;
var counterBoAjaxReq = 0;
$(function(){
	
	
	$( document ).ajaxSend(function(event, xhr, settings) {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		xhr.setRequestHeader(header, token);
		counterBoAjaxReq++;
		if(counterBoAjaxReq==1){
			$( "#divLoadingBo" ).addClass("show");	
		}
	});
	
	$( document ).ajaxComplete(function( event, xhr, settings ) {
		if (xhr.responseText !== undefined && xhr.responseText.indexOf("<title>E-Statement - Login</title>") !== -1){
			//login page is returned, so redirect all page to login
			
			window.location.href = "<c:url value='/session'/>";
		}
		
		counterBoAjaxReq--;
		if(counterBoAjaxReq==0){
			$( "#divLoadingBo" ).removeClass("show");
		}
	}); 
	
	
	
	//Zero the idle timer on mouse movement.
    $(this).mousemove(function (e) {
        currentSecond = 0;
    });
    $(this).keypress(function (e) {
        currentSecond = 0;
    });
    
		incSessionTime()
});

function incSessionTime(){
	/* console.log('test '+window.location.href+" "+window.location.href.indexOf("session")); */ 
	if (window.location.href.indexOf("login") === -1
		&& window.location.href.indexOf("help") === -1 ){
		setTimeout(function(){
			currentSecond++;
			if (currentSecond > limit){
				window.location.href= "<c:url value='/session/logout'/>";
			} else {
				incSessionTime();
			}
		}, 1000);
	}
}

</script>