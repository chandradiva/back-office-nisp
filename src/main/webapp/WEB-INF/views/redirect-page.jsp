<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<p>No session.. You will be redirected to login page. Click <a href="<c:url value='login' />"><strong>Here</strong></a> if it doesn't redirect automatically.</p>

<script>
	var url = '<c:url value="${url}" />';
	if(url){
		window.location.replace(url);
	} else {
		window.location.replace('<c:url value="login" />');
	}
</script>