<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Welcome, ${username }</title>

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
	<sec:authorize access="hasRole('ROLE_USER')">
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
					<a class="navbar-brand" href="index">Customer</a>
				</div>
				<!-- Top Menu Items -->
				<ul class="nav navbar-right top-nav">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><i class="fa fa-user"></i> <!-- Accessing the session object -->
							<c:if test="${pageContext.request.userPrincipal.name != null }">
                    	${pageContext.request.userPrincipal.name}
                    </c:if> <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="accountSummary"><i class="fa fa-fw fa-user"></i> Profile</a>
							</li>
							<li class="divider"></li>
							<li><a href="javascript:formSubmit()"><i
									class="fa fa-fw fa-power-off"></i> Log Out</a></li>
						</ul></li>
				</ul>

				<!-- Logout feature implementation -->
				<c:url value="/j_spring_security_logout" var="logoutUrl" />
				<form action="${logoutUrl}" method="post" id="logoutForm">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</form>

				<!-- Logout Script -->
				<script type="text/javascript">
					function formSubmit() {
						document.getElementById("logoutForm").submit();
					}
				</script>

				<!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
				<div class="collapse navbar-collapse navbar-ex1-collapse">
					<ul class="nav navbar-nav side-nav">
						<li class="active"><a href="index.html"><i
								class="fa fa-fw fa-dashboard"></i> Account</a></li>
						<li><a href="processBillPayment"><i
								class="fa fa-fw fa-bar-chart-o"></i> Bill Pay</a></li>
						<li><a href="javascript:;" data-toggle="collapse"
							data-target="#demo1"><i class="fa fa-fw fa-arrows-v"></i>
								Transfer Money <i class="fa fa-fw fa-caret-down"></i></a>
							<ul id="demo1" class="collapse">
								<li><a href="transferMoney">Make a Transfer</a></li>
								<li><a href="transactionReview">Review Transactions</a></li>
							</ul></li>
						<li><a href="javascript:;" data-toggle="collapse"
							data-target="#demo2"><i class="fa fa-fw fa-arrows-v"></i>
								Debit or Credit Funds <i class="fa fa-fw fa-caret-down"></i></a>
							<ul id="demo2" class="collapse">
								<li><a href="debitFunds">Debit</a></li>
								<li><a href="creditFunds">Credit</a></li>
							</ul></li>
						<li><a href="deleteAccount"><i
								class="fa fa-fw fa-bar-chart-o"></i> Delete Account</a></li>
					</ul>
				</div>
				<!-- /.navbar-collapse -->
			</nav>

			<div id="page-wrapper">

				<div class="container-fluid">

					<!-- Page Heading -->
					<div class="row">
						<div class="col-lg-12">
							<h1 class="page-header">Modify Information</h1>
						</div>
					</div>
					<!-- /.row -->

					<div class="row">
						<div class="col-lg-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">
										<i class="fa fa-bar-chart-o fa-fw"></i> Your details 
									</h3>
								</div>
								<div class="panel-body">
									<div id="morris-area-chart">
										<div class="panel-body">
										<b> Requirements - </b> <br/>
										1. First name and last name should not have more than 10 characters. No special characters and numbers are allowed. <br/>
										2. Address should not have more than 50 characters and no special characters are allowed. <br/>
										3. Phone number should not exceed 10 digits. No characters or special characters are allowed. <br/>
										
										<br/> <br/>
										<c:if test="${not empty userInformation}">
										<c:forEach var="o" items="${userInformation}">
											<b> Username: </b> ${o.username} <br/> <br/>
											<b> Firstname: </b> ${o.firstname} <br/> <br/>
											<b> Lastname: </b> ${o.lastname} <br/> <br/>
											<b> Sex: </b> ${o.sex} <br/> <br/>
											<b> Selection: </b> ${o.selection} <br/> <br/>
											<b> Phonenumber: </b> ${o.phonenumber} <br/> <br/>
											<b> Email: </b> ${o.email} <br/> <br/>
											<b> Address: </b> ${o.address} <br/> <br/>
 											<b> Accountnumber: </b> ${o.accountNumber} <br/> <br/>
											<b> Accountbalance: </b> ${o.accountBalance}<br/> <br/>
										</c:forEach>
										</c:if>
										
										<br/>
										
										<c:if test="${not empty requestSubmitMessage}">
											${requestSubmitMessage }
										</c:if>
										<!-- New form to update only Lastname,
										Sex, Selection, Phonenumber, Email and Address -->
										<form:form method="POST" action="modifyUserExternal" modelAttribute="modifyExternalUserAttributes" autocomplete="off">
											<c:if test="${not empty errorMsg1}">
												<h4> ${errorMsg1} </h4>
                                    		</c:if>
											<b>First name: </b> <br/><input type="text" name="firstname" /><br/><br/>
											<c:if test="${not empty errorMsg2}">
												<h4> ${errorMsg2} </h4>
                                    		</c:if>
											<b> Last name: </b> <br/><input type="text" name="lastname" /><br/><br/>
											<b> Sex: </b> <br/> <input type="radio" name="sex" value="Male" id="male" checked/> Male <br/>
											<input type="radio" name="sex" value="Female" id="male"/> Female <br/><br/>
											<b>Individual or Merchant:</b><br/>
											<select name="selection">
												<option value="Individual">Individual</option>
												<option value="Merchant">Merchant</option>
											</select><br/><br/>
											<c:if test="${not empty errorMsg3}">
												<h4> ${errorMsg3} </h4>
                                    		</c:if>
											<b> Phone Number: </b> <FONT color="red"> <form:errors path="phonenumber" /> </FONT><br/><input type="text" name="phonenumber" id="contact" style="color:#999;" /><br/><br/>
											<b> Email Address: </b> <br/><input type="email" name="email" id="email" style="color:#999;" /><br/><br/>
											<c:if test="${not empty errorMsg4}">
												<h4> ${errorMsg4} </h4>
                                    		</c:if>
											<b> Address: </b> <FONT color="red"> <form:errors path="address" /></FONT><br/><input type="text" name="address" id="add" style="color:#999;" /><br/><br/>
											<input type="submit" value="Request Modification" name="requestModificationButton"/>
										</form:form>
									</div>
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
		<script
			src="${pageContext.request.contextPath}/resources/js/jquery-1.11.0.js"></script>

		<!-- Bootstrap Core JavaScript -->
		<script
			src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
	</sec:authorize>
</body>

</html>
