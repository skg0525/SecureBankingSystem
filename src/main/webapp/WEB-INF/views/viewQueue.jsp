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

    <title>View Queue</title>

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
                    <li class="active">
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
                          Ticket Queues
                        </h1>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-bar-chart-o fa-fw"></i> View Queue Details</h3>
                            </div>
            
                            <div class="panel-body">
                                <div id="morris-area-chart">
	                                					
										<b> <u> <h4> LIST OF ALL OPEN Tickets- </h4> </u> </b>
									  <table border="1">  
									   <tr>  
									    <td class="heading">Ticket Number</td>  
									    <td class="heading">Invoked By</td>  
									    <td class="heading">Type</td>    
									    <td class="heading">Action</td> 
									   </tr>  
									   <c:forEach var="user" items="${userList}">  
									    <tr>  
									     <td>${user.id}</td>  
									     <td>${user.username}</td>  
									     <td>${user.requesttype}</td>   
									     <td><form:form method="post" action="viewTicket" modelAttribute="ticketDetail" autocomplete="off">					
										 <input type="hidden" name="id" id="ticketId" value="${user.id}" style="color:#999;" />
										<input type="hidden" name="username" id="ticketId" value="${user.username}" style="color:#999;" />
										 <input type="hidden" name="requesttype" id="ticketId" value="${user.requesttype}" style="color:#999;" />
										 <a> <input type="submit" style="margin-right: 5%" name="viewTicket" id="viewTicketButton" value="View" /></a> <br/> <br/>
										 </form:form></td> 
									    </tr>  
									   </c:forEach>
									  </table>
										<br/><br/> <br/>
									
                                </div>
                            </div>
                            
                            <c:if test="${not empty approvedList}">
                            <!-- this is for approved tickets -->
                            <div class="panel-body">
                                <div id="morris-area-chart">
	                                					
										<b> <u> <h4> LIST OF ALL Approved Tickets- </h4> </u> </b>
									  <table border="1">  
									   <tr>  
									    <td class="heading">Ticket Number</td>  
									    <td class="heading">Invoked By</td>  
									    <td class="heading">Type</td>
									   </tr>  
									   <c:forEach var="approvedList" items="${approvedList}">  
									    <tr>  
									     <td>${approvedList.id}</td>  
									     <td>${approvedList.username}</td>  
									     <td>${approvedList.requesttype}</td>   
									      
									    </tr>  
									   </c:forEach>  
									  </table>
										<br/><br/> <br/>
									
                                </div>
                            </div>
                            </c:if>
                            
                            <c:if test="${not empty rejectedList}">
                            <!-- this is for rejected tickets -->
                            <div class="panel-body">
                                <div id="morris-area-chart">
	                                					
										<b> <u> <h4> LIST OF ALL Rejected Tickets- </h4> </u> </b>
									  <table border="1">  
									   <tr>  
									    <td class="heading">Ticket Number</td>  
									    <td class="heading">Invoked By</td>  
									    <td class="heading">Type</td>
									   </tr>  
									   <c:forEach var="rejectedList" items="${rejectedList}">  
									    <tr>  
									     <td>${rejectedList.id}</td>  
									     <td>${rejectedList.username}</td>  
									     <td>${rejectedList.requesttype}</td>   
									      
									    </tr>  
									   </c:forEach>  
									  </table>
										<br/><br/> <br/>
									
                                </div>
                            </div>
                            </c:if>
                            
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
