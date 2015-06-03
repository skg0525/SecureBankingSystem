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

    <title>View User</title>

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
                    <li>
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
                          View User
                        </h1>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-bar-chart-o fa-fw"></i> View User Details</h3>
                            </div>
            
                            <div class="panel-body">
                                <div id="morris-area-chart">
	                               <c:if test="${not empty status}">
											<h4> ${status} </h4>
											<h4> <a href="getList">Get View All Customer List click here</a></h4>
	                                    </c:if> <br/> <br/>
                                <c:if test="${ticketDetailDTO.requesttype == 'Modify'}">
                                 <b> <u> <h4> LIST OF ALL USERS - </h4> </u> </b>
									  <table border="1">  
									   <tr>  
									    <td class="heading">User Name</td>  
									    <td class="heading">First Name</td>  
									    <td class="heading">Last Name</td>  
									    <td class="heading">Sex</td> 
									    <td class="heading">Contact Number</td>  
									    <td class="heading">Email</td>
									    <td class="heading">Address</td> 
									    <td class="heading">Modify</td> 
									  
									   </tr>  
									    <tr>  
									     <td>${ticketDetailDTO.username}</td>  
									     <td>${ticketDetailDTO.firstname}</td>  
									     <td>${ticketDetailDTO.lastname}</td>   
									     <td>${ticketDetailDTO.sex}</td> 
									     <td>${ticketDetailDTO.phonenumber}</td>  
									     <td>${ticketDetailDTO.email}</td> 
									     <td>${ticketDetailDTO.address}</td>  
									     <td><form:form method="post" action="modifyExternalUserByInternal" modelAttribute="modifyExternalUserByAdmin" autocomplete="off">                    
					                     <input type="hidden" name="username" id="accountNumber_RemoveUser" value="${ticketDetailDTO.username}" style="color:#999;" />
					                     <input type="hidden" name="firstname" id="accountNumber_RemoveUser" value="${ticketDetailDTO.firstname}" style="color:#999;" />
					               		 <input type="hidden" name="lastname" id="accountNumber_RemoveUser" value="${ticketDetailDTO.lastname}" style="color:#999;" />
					                     <input type="hidden" name="sex" id="accountNumber_RemoveUser" value="${ticketDetailDTO.sex}" style="color:#999;" /><br/> <br/>
					                     <input type="hidden" name="phonenumber" id="accountNumber_RemoveUser" value="${ticketDetailDTO.phonenumber}" style="color:#999;" />
					               		 <input type="hidden" name="email" id="accountNumber_RemoveUser" value="${ticketDetailDTO.email}" style="color:#999;" />
					                     <input type="hidden" name="address" id="accountNumber_RemoveUser" value="${ticketDetailDTO.address}" style="color:#999;" />
					                     <input type="hidden" name="requesttype" id="accountNumber_RemoveUser" value="${ticketDetailDTO.requesttype}" style="color:#999;" />
					                     <input type="hidden" name="id" id="accountNumber_RemoveUser" value="${ticketDetailDTO.id}" style="color:#999;" />
					                    
					                    <a> <input type="submit" style="margin-right: 5%" name="SearchUser" id="modifyUserButton" value="Update User" /></a>
					                        </form:form></td>  
									     
									    </tr>
									  </table>  
									  </c:if>
									  
									  
									  <c:if test = "${ticketDetailDTO.requesttype == 'Delete'}">     
       									<b> <u> <h4> LIST OF ALL USERS - </h4> </u> </b>
									  <table border="1">  
									   <tr>  
									    <td class="heading">User Name</td>  
									    <td class="heading">First Name</td>  
									    <td class="heading">Last Name</td>  
									    <td class="heading">Delete Account</td>  
									    <td class="heading">Account Numbere</td>  
									    <td class="heading">Account Balance</td>    
									    <td class="heading">Delete</td>  
									  
									   </tr>  
									    <tr>  
									     <td>${ticketDetailDTO.username}</td>  
									     <td>${ticketDetailDTO.firstname}</td>  
									     <td>${ticketDetailDTO.lastname}</td>
									     <td>${ticketDetailDTO.deleteaccount}</td>  
									     <td>${ticketDetailDTO.accountNumber}</td>   
									     <td>${ticketDetailDTO.accountBalance}</td>  
									     <td> <form:form method="post" action="removeUserExternal" modelAttribute="usernameSearch">					
										 <input type="hidden" name="account" id="accountNumber_RemoveUser" value="${ticketDetailDTO.username}" style="color:#999;" />
										 <a> <input type="submit" style="margin-right: 5%" name="SearchUser" id="removeUserButton" value="Remove User" /></a>
										 </form:form></td> 
									    </tr>
									  </table> 
									  </c:if> 
									  
									  
									  <c:if test="${ticketDetailDTO.requesttype == 'Authorize'}">
                                 <b> <u> <h4> LIST OF ALL USERS - </h4> </u> </b>
									  <table border="1">  
									   <tr>  
									    <td class="heading">User Name</td>  
									    <td class="heading">First Name</td>  
									    <td class="heading">Last Name</td>  
									    <td class="heading">Account Number</td>  
									    <td class="heading">Account Balance</td>
									    <td class="heading">Transaction Amt</td>  
									    <td class="heading">Reciepeint Acc number</td>
									    <td class="heading">bill pay</td> 
									    
									    <td class="heading">Approve</td>  
									    <td class="heading">Reject</td>  
									  
									   </tr>  
									    <tr>  
									     <td>${ticketDetailDTO.username}</td>  
									     <td>${ticketDetailDTO.firstname}</td>  
									     <td>${ticketDetailDTO.lastname}</td> 
									     <td>${ticketDetailDTO.accountNumber}</td>   
									     <td>${ticketDetailDTO.accountBalance}</td> 
									     <td>${ticketDetailDTO.transactionAmount}</td> 
									     <td>${ticketDetailDTO.toAccountNumber}</td> 
									     <td>${ticketDetailDTO.billpay}</td> 
									     <td><form:form method="post" action="authorizeTransactionsApprove" modelAttribute="authorizeTransactions" autocomplete="off">                    
					                     <input type="hidden" name="accountNumber" id="accountNumber_RemoveUser" value="${ticketDetailDTO.accountNumber}" style="color:#999;" />
					                     <input type="hidden" name="username" id="accountNumber_RemoveUser" value="${ticketDetailDTO.username}" style="color:#999;" />
					                     <input type="hidden" name="firstname" id="accountNumber_RemoveUser" value="${ticketDetailDTO.firstname}" style="color:#999;" />
					               		 <input type="hidden" name="lastname" id="accountNumber_RemoveUser" value="${ticketDetailDTO.lastname}" style="color:#999;" />
					                     <input type="hidden" name="accountBalance" id="accountNumber_RemoveUser" value="${ticketDetailDTO.accountBalance}" style="color:#999;" />
					                     <input type="hidden" name="transactionAmount" id="accountNumber_RemoveUser" value="${ticketDetailDTO.transactionAmount}" style="color:#999;" />
					               		 <input type="hidden" name="toAccountNumber" id="accountNumber_RemoveUser" value="${ticketDetailDTO.toAccountNumber}" style="color:#999;" />
					                     <input type="hidden" name="billpay" id="accountNumber_RemoveUser" value="${ticketDetailDTO.billpay}" style="color:#999;" />
					                     <input type="hidden" name="requesttype" id="accountNumber_RemoveUser" value="${ticketDetailDTO.requesttype}" style="color:#999;" />
					                     <input type="hidden" name="pendingid" id="accountNumber_RemoveUser" value="${ticketDetailDTO.pendingid}" style="color:#999;" />
					                     <input type="hidden" name="transactionamountInfloat" id="accountNumber_RemoveUser" value="${ticketDetailDTO.transactionamountInfloat}" style="color:#999;" />
					                     <input type="hidden" name="id" id="accountNumber_RemoveUser" value="${ticketDetailDTO.id}" style="color:#999;" />
					                    
					                    
					                    <a> <input type="submit" style="margin-right: 5%" name="SearchUser" id="modifyUserButton" value="Approve" /></a>
					                        </form:form></td>  
									     <td> <form:form method="post" action="authorizeTransactionsReject" modelAttribute="authorizeTransactions" autocomplete="off">					
										 <input type="hidden" name="accountNumber" id="accountNumber_RemoveUser" value="${ticketDetailDTO.accountNumber}" style="color:#999;" />
					                     <input type="hidden" name="username" id="accountNumber_RemoveUser" value="${ticketDetailDTO.username}" style="color:#999;" />
					                     <input type="hidden" name="firstname" id="accountNumber_RemoveUser" value="${ticketDetailDTO.firstname}" style="color:#999;" />
					               		 <input type="hidden" name="lastname" id="accountNumber_RemoveUser" value="${ticketDetailDTO.lastname}" style="color:#999;" />
					                     <input type="hidden" name="accountBalance" id="accountNumber_RemoveUser" value="${ticketDetailDTO.accountBalance}" style="color:#999;" />
					                     <input type="hidden" name="transactionAmount" id="accountNumber_RemoveUser" value="${ticketDetailDTO.transactionAmount}" style="color:#999;" />
					               		 <input type="hidden" name="toAccountNumber" id="accountNumber_RemoveUser" value="${ticketDetailDTO.toAccountNumber}" style="color:#999;" />
					                     <input type="hidden" name="billpay" id="accountNumber_RemoveUser" value="${ticketDetailDTO.billpay}" style="color:#999;" />
					                     <input type="hidden" name="requesttype" id="accountNumber_RemoveUser" value="${ticketDetailDTO.requesttype}" style="color:#999;" />
					                     <input type="hidden" name="pendingid" id="accountNumber_RemoveUser" value="${ticketDetailDTO.pendingid}" style="color:#999;" />
					                     <input type="hidden" name="transactionamountInfloat" id="accountNumber_RemoveUser" value="${ticketDetailDTO.transactionamountInfloat}" style="color:#999;" />
					                     <input type="hidden" name="id" id="accountNumber_RemoveUser" value="${ticketDetailDTO.id}" style="color:#999;" />
					                     <a> <input type="submit" style="margin-right: 5%" name="SearchUser" id="removeUserButton" value="Reject" /></a>
										 </form:form></td> 
									    </tr>
									  </table>  
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
    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.0.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>

</body>

</html>