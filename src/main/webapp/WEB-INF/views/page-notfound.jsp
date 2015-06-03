<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<script>
document.onmousedown=disableclick;
status="Right Click Disabled";
function disableclick(event)
{
  if(event.button==2)
   {
     alert(status);
     return false;    
   }
}
</script>
<body oncontextmenu="return false">
	<h1>HTTP Status 404 - Page Not Found</h1>

	<c:choose>
		<c:when test="${empty username}">
			<h2>This page doesn't exist please go back to application.!</h2>
		</c:when>
		<c:otherwise>
			<h2>Username : ${username} <br/>This page doesn't exist please go back to application.!</h2>
		</c:otherwise>
	</c:choose>

</body>
</html>