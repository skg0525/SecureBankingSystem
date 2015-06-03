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
							<h1 class="page-header">Transactions Review</h1>
						</div>
					</div>
					<!-- /.row -->

					<div class="row">
						<div class="col-lg-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title"><i class="fa fa-bar-chart-o fa-fw"></i> Processed Transactions </h3>
								</div>
								<br/>
								<c:if test="${not empty errorMsg }">
												<h4><b>${errorMsg }</b></h4><br/>
												<a href = "transactionReview"> <b> Click here to go back! </b></a>
											</c:if>
								<div class="panel-body">
									<div id="morris-area-chart">
										<div class="panel-body">

											<!-- Bill pay information -->
											<c:if test="${not empty userTransactions}">
											
											<form:form method="POST" action="deleteTransaction" autocomplete="off" >
												Transaction ID to delete:<br/>
												<input type="text" name="deleteTransactionID" /><br/>
												<input type="submit" value="Delete Transaction" /><br/><br/><br/>
											</form:form>
											
											<br/>
											<c:if test="${not empty emptyBox }">
												${emptyBox }
											</c:if>
											<c:if test="${not empty deleteInformation }">
												${deleteInformation }
											</c:if>
												<table
													class="table table-bordered table-hover table-striped">
													<thead>
														<tr>
															<th>Transaction ID</th>
															<th>By User</th>
															<th>For User</th>
															<th>From Account Number</th>
															<th>By Account Number</th>
															<th>Transaction Type</th>
															<th>Timestamp</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach var="o" items="${userTransactions}">
															<tr>
																<td>${o.transactionID}</td>
																<td>${o.usernameFrom}</td>
																<td>${o.usernameTo}</td>
																<td>${o.usernameFromAccountNumber }</td>
																<td>${o.usernameToAccountNumber }</td>
																<td>${o.transactionType }</td>
																<td>${o.currentTimeStamp }</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</c:if>
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
