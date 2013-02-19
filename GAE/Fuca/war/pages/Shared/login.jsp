<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%
	UserService userService = UserServiceFactory.getUserService();
	String thisURL = request.getRequestURL().toString();
	String logouth;
	String userName;
	if (request.getUserPrincipal() != null) {
		 userName = request.getUserPrincipal().getName();
		logouth = userService.createLogoutURL(thisURL);
	} else {
		String loginUrl = userService.createLoginURL(thisURL);
	}
%>

<span><%=request.getUserPrincipal().getName()%></span>
<a href="<%=userService.createLogoutURL("/")%>">Odjava</a>
