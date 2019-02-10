<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="navbar navbar-default yamm" role="navigation" style="background:none">
	<div class="container">
		<div class="top-header row" style="margin-top: -15px">
			<!-- Top Menu Links -->
			<ul class="top-menu nav nav-pills" style="margin-top: 25px">
				<li><a data-toggle="dropdown" class="dropdown-toggle" href="#"><span
						class="glyphicon glyphicon-user"></span>Account</a>
					<ul
						class="pull-right dropdown-menu dropdown-caret dropdown-navbar account-navbar">
						<li><a href="<c:url value='/session/logout' />">Logout</a></li>
					</ul></li>
			</ul>

			<!-- Original OCBC NISP Logo -->
			<div id="ocbcnisp-logo" class="col-xs-12 col-sm-5 col-md-5" style="margin-top: 52px">
				<a class="navbar-brand" href="#"><img src=<c:url value="/resources/img/main_logo.png" />
					alt="OCBC Bank"></a>
			</div>

			<div class="welcome-block col-xs-12 col-sm-4 col-md-6" style="margin-top: 32px">
				<span>Welcome, <span class="user-name"
						contenteditable="false">${userSession.username}</span></span>
				<span>Group: <span class="user-name" contenteditable="false">${userSession.groupName}</span></span>
				<span>FGroup: <span class="user-name" contenteditable="false">${userSession.fGroupName}</span></span>
						PT BANK OCBC NISP TBK -
				Jakarta <span class="last-login">Last login: ${userSession.lastLogin }</span>				
			</div>


		</div>
		<div class="navbar-header">
			<span class="menu_text">MENU</span>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="collapse navbar-collapse"></div>
	</div>
</div>
<!-- end of header -->