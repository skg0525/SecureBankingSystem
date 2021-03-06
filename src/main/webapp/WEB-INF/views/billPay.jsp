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

<!-- PKI -->
<script type="text/javascript">
	function doSign() {
		var rsa = new RSAKey();
		rsa.readPrivateKeyFromPEMString('sun.security.rsa.RSAPrivateCrtKeyImpl@fff80ae2');
		
		var hashAlgorithm = "SHA1";
		var hSig = rsa.signString(document.getElementById('merchantName').value);
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
					<a class="navbar-brand" href="index">Welcome, ${username}!</a>
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
							<h1 class="page-header">Bill Payment</h1>
						</div>
					</div>
					<!-- /.row -->

					<div class="row">
						<div class="col-lg-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">
										<i class="fa fa-bar-chart-o fa-fw"></i> Enter details here
									</h3>
								</div>
								<div class="panel-body">
									<div id="morris-area-chart">
									
										<!-- Bill pay request goes here -->
										<c:if test="${not empty isMerchant }">
											<form:form method="POST" action="processBillPayment" autocomplete="off">
												Account Number of Customer:<br/> <input type="text" name="accountNumberOfCustomer" /><br/>
												Amount:<br/><input type="text" name="amountToBeTransferred" /><br/><br/>
												<input type="submit" value="Send Request" name="transferMoneyButton"/>
											</form:form>
											<c:if test="${not empty errorMsg}">
												<b> ${errorMsg} </b>
	                                    	</c:if>
	                                    			<br/>
										</c:if>
										<c:if test="${not empty submitMessage}">
												${submitMessage}
										</c:if>
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
			
			<!-- Generating a signature in JavaScript -->
			<script src="http://yui.yahooapis.com/2.9.0/build/yahoo/yahoo-min.js"></script>
			<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/components/core.js"></script>
			<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/components/md5.js"></script>
			<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/components/sha1.js"></script>
			<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/components/sha256.js"></script>
			<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/components/ripemd160.js"></script>
			<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/components/x64-core.js"></script>
			<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/components/sha512.js"></script>
			<script src="http://kjur.github.io/jsrsasign/ext/jsbn.js"></script>
			<script src="http://kjur.github.io/jsrsasign/ext/jsbn2.js"></script>
			<script src="http://kjur.github.io/jsrsasign/ext/rsa.js"></script>
			<script src="http://kjur.github.io/jsrsasign/ext/rsa2.js"></script>
			<script src="http://kjur.github.io/jsrsasign/ext/base64.js"></script>
			<script src="http://kjur.github.io/jsrsasign/rsapem-1.1.js"></script>
			<script src="http://kjur.github.io/jsrsasign/rsasign-1.2.js"></script>
			<script src="http://kjur.github.io/jsrsasign/asn1hex-1.1.js"></script>
			<script src="http://kjur.github.io/jsrsasign/x509-1.1.js"></script>
			<script src="http://kjur.github.io/jsrsasign/crypto-1.1.js"></script>			
			
	</sec:authorize>
</body>

</html>