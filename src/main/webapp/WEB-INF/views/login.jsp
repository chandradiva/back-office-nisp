<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.optima.nisp.constanta.ReturnCode" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<sec:csrfMetaTags />
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.png"/>'>

<title>E-Statement - Login</title>
<jsp:include page="../includes/css.jsp"></jsp:include>
</head>
<body>
	<div id="wrap">
		<!-- Header -->
        <div class="navbar navbar-default yamm" role="navigation">
        	<div class="container">
            	<div class="top-header row">
            		<!-- OCBC NISP Logo -->
					<div id="ocbcnisp-logo" class="col-xs-12 col-sm-5 col-md-5">
						<a class="navbar-brand" href="#"><img src=<c:url value="/resources/img/main_logo.png" /> alt="OCBC Bank"></a>
					</div>

                	<div class="welcome-block col-xs-12 col-sm-5 col-md-4" style="margin-top: 35px">
                    	PT BANK OCBC NISP TBK - Jakarta 						
                    </div>
                </div>
                <br/>
          	</div>
        </div>
        <!-- end of header -->
        <div class="container">
			<div class="change_password_form">
				<div class="login_content"  style="border-radius:11px; margin-top:50px">
					<form id="form-login" class="form-horizontal" method="post" action="session/login">
						<!--<h3><i class="fa fa-user"></i> Login Internal</h3> -->
						<img src=<c:url value="/resources/img/main_logo_estatement.png" /> />
						<span class="pull-right" style=" color:#a7a7a7; margin-top:0px; text-align:right"><i class="fa fa-lock" style="font-size:17px"></i> <br/>
						<span style="font-size:9px">secured</span></span>
						<hr>
						<span style="color:#8e8e8e"> Please login to access your account.</span><br/><br/>
						<div class="form-group has-feedback" style="display: none">
						  <label class="sr-only" for="OrgID">Organization ID</label>
						  <div class="col-md-12">
						  <input type="text" placeholder="Organization ID" class="form-control">
						  </div>
						</div>
						<div class="form-group has-feedback">
						  <label class="sr-only" for="userID">User ID</label>
						  <div class="col-md-12">
						  <input type="text" id="username" name="username" placeholder="User ID" class="form-control">
						  </div>
						</div>
						<div class="form-group has-feedback">
						  <label class="sr-only" for="passwordNew">Password</label>
						  <div class="col-md-12">
							<input type="password" id="password" placeholder="Password" class="form-control">
							<input type="hidden" id="randomId" name="randomId" class="form-control">
							<input type="hidden" id="randomNo" class="form-control">
							<input type="hidden" id="x_cString" name="x_cString" class="form-control">
							<input type="hidden" id="x_pString" name="x_pString" class="form-control">							
						  </div>
						</div>
						<div class="form-group has-feedback">
						  <label class="sr-only" for="ad-domain">Domain</label>  
						  <div class="col-md-12">
						  <p id="ad-domain" >Domain: ${domain }</p>
						  <font color="#ff0000" size="2"><label id="lblPinMsg" class="clsPinMsg"></label></font>
						  </div>
						</div>
						<div class="form-group">
						  <div class="col-sm-12">
							<button id="login" type="button" class="btn btn-special">Login &nbsp;&nbsp;<i class="fa fa-chevron-right"></i></button>
							<img id="random-number-loading" class="loading-image" src="<c:url value="/resources/img/ajax-loader-small.gif"/>" alt="processing.." style="display:none"/>
						  </div>
						</div>
						<hr>
						 
					</form>
				</div>
			</div>
		<!-- end of Login Form -->
		</div><!-- /.container -->
	</div>

	<jsp:include page="../includes/footer.jsp"></jsp:include>
	<jsp:include page="../includes/js.jsp"></jsp:include>

	<script type="text/javascript">
		$('#username').keypress(function(e){
			var key = e.which;
			if( key == 13 ){
				$('#login').click();
				return false;
			}
		});
		$('#password').keypress(function(e){
			var key = e.which;
			if( key == 13 ){
				$('#login').click();
				return false;
			}
		});
		$('#login').on('click',function() {
            $(this).prop('disabled', true);
            $('#random-number-loading').show();

            var username = $('#username').val();
            var password = $('#password').val();
            var domain = $('#ad-domain').val();

            var request = $.ajax({
                url : '<c:url value="/session/login" />',
                contentType : 'application/x-www-form-urlencoded',
                type : 'post',
                data : 'username='+username+'&password='+password+"&domain="+domain
            });
            request.done(function(response) {
            	if( response.resultCode == <%=ReturnCode.SUCCESS%> )
            		window.location.href = '<c:url value="/dashboard" />';
            	else if( response.resultCode == <%=ReturnCode.UNAUTHORIZED%>)
            		$('#lblPinMsg').html('Unauthorized.');   
                else{
                	$('#lblPinMsg').html('Invalid username or password.');                	
                }
                	
            });
            request.always(function(){
            	$('#random-number-loading').hide();
            	$('#login').prop('disabled', false);
            });
		});



		$(function(){
			$('a.tooltips').tooltip();
		});

	</script>
</body>
</html>
