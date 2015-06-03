<%@taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Activate Account!</title>

<!-- Bootstrap Core CSS -->
<link
    href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"
    rel="stylesheet">

<!-- Custom CSS -->
<link
    href="${pageContext.request.contextPath}/resources/css/sb-admin.css"
    rel="stylesheet">

<!-- Custom Fonts -->
<link
    href="${pageContext.request.contextPath}/resources/font-awesome-4.1.0/css/font-awesome.min.css"
    rel="stylesheet" type="text/css">

</head>

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

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span> <span
                        class="icon-bar"></span> <span class="icon-bar"></span> <span
                        class="icon-bar"></span>
                </button>
            </div>




        </nav>

        <div id="page-wrapper">

            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">User Account Activation -</h1>
                        <h3> Hello! ${account}</h3> <br/>  
                    </div>
                </div>
                <c:if test="${not empty status}">
                    <b>"${status}"</b>
                </c:if>

                <form:form method="post" action="activateAccount" modelAttribute="usernameSearch" autocomplete="off">                  
                    <b>User Name :</b> <br/> <input type="text" name="username" id="accountNumber_RemoveUser" value ="${account}" style="color:#999;" /><br/> <br/>
                    <a> <input type="submit" style="margin-right: 5%" name="SearchUser" id="searchUserButton" value="Activate Account" /></a> <br/> <br/>
                    <c:if test="${not empty errorMsg}">
                        <h4> ${errorMsg} </h4>
                    </c:if> <br/> <br/>
                </form:form>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery Version 1.11.0 -->
    <script
        src="${pageContext.request.contextPath}/resources/js/jquery-1.11.0.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script
        src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>



</body>

</html>
