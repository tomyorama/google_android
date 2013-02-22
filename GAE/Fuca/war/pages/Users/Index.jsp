<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="search_users" data-url="/Users/userdata"></div>
<table id="user_search_grid" class="scroll">
</table>
<div id="user_nav" class="scroll" style="text-align: center;">
</div>
<a style="margin-top: 10px;" href="<c:url value="/Admin/AddUser"/>" data-u-button="true">Dodaj
	novog</a>

