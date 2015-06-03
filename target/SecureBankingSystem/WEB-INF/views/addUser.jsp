<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>
<%@ taglib
    prefix="c"
    uri="http://java.sun.com/jsp/jstl/core" 
%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Add User</title>

    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/sb-admin.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${pageContext.request.contextPath}/resources/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    
    <!-- Logout Script -->
    <script type="text/javascript">
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

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
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index"><font COLOR=RED> <b><sec:authentication property="principal.username" /></b> </font></a>
            </div>
            <!-- Top Menu Items -->
            <ul class="nav navbar-right top-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> <sec:authentication property="principal.username" /> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="javascript:formSubmit()"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                        </li>
                    </ul>
                </li>
            </ul>
            
            <!-- Logout feature implementation -->
            <c:url value="/j_spring_security_logout" var="logoutUrl" />
			<form action="${logoutUrl}" method="post" id="logoutForm">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</form>
            <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav side-nav">
                    <li class="active">
                        <a href="addUser"><i class="fa fa-fw fa-dashboard"></i> Add User</a>
                    </li>
                    <li>
                        <a href="viewQueue"><i class="fa fa-fw fa-dashboard"></i> View Queue</a>
                    </li>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <li>
                        <a href="getList"><i class="fa fa-fw fa-dashboard"></i>View All</a>
                    </li>
                    </sec:authorize>
                    <li>
                        <a href="changePassword"><i class="fa fa-fw fa-dashboard"></i>Change Password (SELF)</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <div id="page-wrapper">

            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">
                          Add User
                        </h1>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-bar-chart-o fa-fw"></i> Enter the user information</h3>
                            </div>
            
                            <div class="panel-body">
                                <div id="morris-area-chart">
                                <h3><b> Requirements for adding new user - </b></h3>
                                <h4> 1. User name should not be empty and it should be less then 10 characters.</h4>
                                <h4> 2. Password must contain at least one capital character, one small character, one digit and one special character.</h4>
                                <h4> 3. Allowed special characters for password are ! @ # $ { } , % ^ & * + _ . - </h4>
                                <h4> 4. Password must be between 6 to 15 characters. Password and confirm password should be same.</h4>
                                <h4> 5. First and last name should not exceed 10 characters and they should not be empty.</h4>
                                <h4> 6. Phone number should not be empty. It should be exactly 10 digits, no characters allowed. Example: 1234567890 </h4>
                                <h4> 7. Social Security Number should not be empty. It should be exactly 10 digits, no characters allowed. Example: 123456789</h4>
                                <h4> 8. Address should not be empty. It should not exceed 50 characters.</h4>
                                <h4> 9. No special characters are allowed in: First name, last name, user name, address</h4>
                                <br/> <br/>
                                <form:form method="POST" action="addUser" modelAttribute="addUserForm" autocomplete="off">					
									<c:if test="${not empty errorMsg}">
										<h3> ${errorMsg} </h3>
                                    </c:if>
									<b>First Name:</b> <FONT color="red"> <form:errors path="firstName" /> </FONT> <br/> <input type="text" name="firstName" id="f_name_addUser" style="color:#999;" /><br/> <br/>
									<b>Last Name:</b> <FONT color="red"> <form:errors path="lastName" /> </FONT> <br/> <input type="text" name="lastName" id="l_name_addUser" style="color:#999;" /><br/> <br/>
									<b>Sex: </b><br/> <input type="radio" name="sex" value="Male" id="male" checked/> Male <br/>
									<input type="radio" name="sex" value="Female" id="male"/> Female <br/> <br/>
									<b>Contact:</b> <FONT color="red"> <form:errors path="contactNumber" /> </FONT> <br/> <input type="text" name="contactNumber" id="contact_addUser" style="color:#999;" /><br/> <br/>
									<b>Address:</b> <FONT color="red"> <form:errors path="address" /> </FONT> <br/> <input type="text" name="address" id="add_addUser" style="color:#999;" /><br/> <br/>
									<b>Email Address:</b> <FONT color="red"> <form:errors path="emailAddress_addUser" /> </FONT> <br/> <input type="email" name="emailAddress_addUser" id="email" style="color:#999;" /><br/> <br/>
									<b>User Name:</b> <FONT color="red"> <form:errors path="userName" /> </FONT> <br/> <input type="text" name="userName" id="u_name_addUser" style="color:#999;" /><br/> <br/>
									<b>Password:</b> <FONT color="red"> <form:errors path="password" /> </FONT> <br/> <input type="password" name="password" id="temp_pwd_addUser" style="color:#999;" /><br/> <br/>
									<b>Confirm Password:</b> <FONT color="red"> <form:errors path="confirmPassword" /> </FONT> <br/> <input type="password" name="confirmPassword" id="confirm_pwd_addUser" style="color:#999;" /><br/> <br/>
									<b>SSN:</b> <FONT color="red"> <form:errors path="socialSecurityNumber" /> </FONT> <br/> <input type="number" name="socialSecurityNumber" id="ssn_addUser" style="color:#999;" /><br/> <br/>
									<b>User Type:</b> 
									<select name="selection">
										<option value="internalUser">Internal User</option>
										<option value="externalUser">External User</option>
									</select>
									<br/> <br/>					
									<h4><input type="submit" style="margin-right: 5%" name="addUser" id="addUserButton" value="Add User" /></h4> <br/>
									<c:if test="${not empty successMsg}">
										<h4> ${successMsg} </h4>
                                    </c:if>
								</form:form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.row -->
                
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery Version 1.11.0 -->
    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.0.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>

</body>

</html>