
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h2>${termin.date}</h2>
<div>
	<span>${termin.name}</span>
</div>
<table style="width: 100%;">
	<thead>
		<tr>
			<th>Od</th>
			<th>Poruka</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${termin.comments}">
			<tr>
				<td>${item.user}</td>
				<td>${item.text}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
