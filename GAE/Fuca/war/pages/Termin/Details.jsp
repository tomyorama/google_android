<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="fuca.model.Comment"%>
<h2>${termin.name}</h2>
<div>
	<span>Da:${tmpdate}</span>
</div>
<div style="width: 100%">
	<div class="players team1">
		<div class="teamheader ui-widget-header">
			<span>${termin.team1}</span>
		</div>
		<div class="teamground">
			<table>
				<colgroup>
					<col style="width: 30%;">
					<col style="width: 30%;">
					<col style="width: 20%;">
					<col style="width: 20%;">
				</colgroup>
				<thead>
					<tr>
						<th>Nadimak</th>
						<th>Ime</th>
						<th>Potvrda</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${termin.team1Players}">
						<tr>
							<td>${item.nickname}</td>
							<td>${item.name}</td>
							<c:if test="${(empty item.confirmed)||!item.confirmed}">
								<td><img
										alt="Nedolazim" title="Nedolazim"
										src="../../Content/images/1354390210_button_cancel.png" /></td>
							</c:if>
							<c:if test="${(not empty item.confirmed)&&(item.confirmed)}">
								<td><img
										alt="Dolazim" title="Dolazim"
										src="../../Content/images/1354390215_Check.png" /></td>
							</c:if>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>
	<div class="players team2">
		<div class="teamheader ui-widget-header">
			<span>${termin.team2}</span>
		</div>
		<div class="teamground">
			<table>
				<colgroup>
					<col style="width: 30%;">
					<col style="width: 30%;">
					<col style="width: 20%;">
					<col style="width: 20%;">
				</colgroup>
				<thead>
					<tr>
						<th>Nadimak</th>
						<th>Ime</th>
						<th>Potvrda</th>
						<th></th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="item" items="${termin.team2Players}">
						<tr>
							<td>${item.nickname}</td>
							<td>${item.name}</td>
							<c:if test="${(empty item.confirmed)||!item.confirmed}">
								<td>
										<img alt="Nedolazim" title="Nedolazim"
										src="../../Content/images/1354390210_button_cancel.png" />
								</td>
							</c:if>
							<c:if test="${(not empty item.confirmed)&&(item.confirmed)}">
								<td><img
										alt="Dolazim" title="Dolazim"
										src="../../Content/images/1354390215_Check.png" /></td>
							</c:if>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<div style="clear: both;"></div>
<div class="comments">
	<div class="teamheader ui-widget-header">
		<span>Komentari</span>
	</div>
	<div class="teamground">
			<table>
				<colgroup>
					<col style="width: 20%;">
					<col style="width: 50%; text-align: left;">
					<col style="width: 30%; text-align: left;" class="pre">
				</colgroup>
				<thead>
					<tr>
						<th>Od</th>
						<th>Poruka</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${termin.comments}">
						<%@ include file="Comment.jsp"%>
					</c:forEach>
				</tbody>
			</table>
	</div>
</div>
