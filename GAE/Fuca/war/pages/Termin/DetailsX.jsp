<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="fuca.model.Comment"%>
<h2>${termin.name}</h2>
<div>
	<span>Datum: ${tmpdate}</span>
</div>
<div style="width: 100%;">
	<div style="width: 45%; float: left;">
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
									<td><a class="confirmlink"
										href="<c:url value="/Termin/Confirm1/${termin.id}/${item.userId}/true"/>"><img
											alt="Nedolazim" title="Nedolazim"
											src="../../Content/images/1354390210_button_cancel.png" /></a></td>
								</c:if>
								<c:if test="${(not empty item.confirmed)&&(item.confirmed)}">
									<td><a class="confirmlink"
										href="<c:url value="/Termin/Confirm1/${termin.id}/${item.userId}/false"/>"><img
											alt="Dolazim" title="Dolazim"
											src="../../Content/images/1354390215_Check.png" /></a></td>
								</c:if>
								<td><a
									href="<c:url value="/Termin/RemoveUsers1/${termin.id}/${item.userId}"/>">Izbaci</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="dialog-form1" title="Dodaj novog igrača">
				<div style="width: 500px;">
					<div id="search_users1" data-url="/Users/userdata/${termin.id}/1"></div>
					<table id="user_search_grid1" class="scroll">
					</table>
					<div id="user_nav1"></div>
				</div>
			</div>
			<button id="create-user1" data-u-button="true">Dodaj igrača</button>

		</div>
	</div>
	<div class="result" style="width: 10%; float: left;">
		<span>${termin.result}</span>
	</div>
	<div style="width: 45%; float: left;">
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
									<td><a class="confirmlink"
										href="<c:url value="/Termin/Confirm2/${termin.id}/${item.userId}/true"/>">
											<img alt="Nedolazim" title="Nedolazim"
											src="../../Content/images/1354390210_button_cancel.png" />
									</a></td>
								</c:if>
								<c:if test="${(not empty item.confirmed)&&(item.confirmed)}">
									<td><a class="confirmlink"
										href="<c:url value="/Termin/Confirm2/${termin.id}/${item.userId}/false"/>"><img
											alt="Dolazim" title="Dolazim"
											src="../../Content/images/1354390215_Check.png" /> </a></td>
								</c:if>
								<td><a
									href="<c:url value="/Termin/RemoveUsers2/${termin.id}/${item.userId}"/>">Izbaci</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="dialog-form2" title="Dodaj novog igrača">
				<div style="width: 500px;">
					<div id="search_users2" data-url="/Users/userdata/${termin.id}/2"></div>
					<table id="user_search_grid2" class="scroll">
					</table>
					<div id="user_nav2"></div>
				</div>
			</div>
			<button id="create-user2" data-u-button="true">Dodaj igrača</button>
		</div>
	</div>
</div>
<div style="clear: both;"></div>

<div class="comments">
	<div class="teamheader ui-widget-header">
		<span>Komentari</span>
	</div>
	<div class="teamground">
		<form:form commandName="comment" id="addCommentForm"
			action="/Termin/addComment/${termin.id}">
			<table>
				<colgroup>
					<col style="width: 30%;">
					<col style="width: 50%; text-align: left;">
					<col style="width: 20%; text-align: left;" class="pre">
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
					<tr id="last">
						<td>${comment.user}:</td>
						<td style="text-align: left;"><form:textarea path="text"
								id="textmessage" style="width:95%;" rows="5" /> <form:errors
								path="text" /></td>
						<td style="text-align: left;"><input type="submit"
							value="Spremi" /></td>
					</tr>
				</tbody>
			</table>
			<form:hidden path="user" />
			<form:errors path="*" cssClass="errorblock" element="div" />
		</form:form>
	</div>
</div>

<script>
	$(function() {
		function dialogXX(num, gridSelector) {
			$("#dialog-form" + num)
					.dialog(
							{
								autoOpen : false,
								height : 430,
								width : 530,
								modal : true,
								buttons : {
									"Dodaj" : function() {
										var s;
										s = jQuery(gridSelector).jqGrid(
												'getGridParam', 'selarrrow');
										$
												.ajax(
														{
															type : 'POST',
															url : '/Termin/AddUsers'
																	+ num
																	+ '/'
																	+ '${termin.id}',
															data : {
																usersIds : s
															}
														})
												.done(
														function() {
															window.location.href = window.location.href;
														});
										$(this).dialog("close");
									},
									Cancel : function() {
										$(this).dialog("close");
									}
								},
								close : function() {

								}
							});
		}
		dialogXX(1, "#user_search_grid1");
		dialogXX(2, "#user_search_grid2");
		$("#create-user1").button().click(function() {
			$("#dialog-form1").dialog("open");
		});
		$("#create-user2").button().click(function() {
			$("#dialog-form2").dialog("open");
		});

		$("#addCommentForm").submit(function() {

			$.ajax({
				type : "POST",
				url : $(this).attr('action'),
				data : $(this).serialize()
			}).done(function(response) {
				$("#last").before(response);
				$("#textmessage").val('');
			}).fail(function(response) {
				$("#last").before(response);
				$("#textmessage").val('');
			});
			return false;
		});

		$(".confirmlink").click(function(e) {
			e.preventDefault();
			var self = $(this);
			$.ajax({
				type : "GET",
				url : $(this).attr('href')
			}).done(function(response) {
				self.children().attr('src', response.src);
				self.children().attr('alt', response.alt);
				self.children().attr('title', response.title);
				self.attr('href', response.href);
			});
			return false;
		});

		$(document).on('click', ".removeComment", function(e) {
			e.preventDefault();
			var self = $(this);
			$.ajax({
				type : "GET",
				url : $(this).attr('href')
			}).done(function(response) {
				self.parent().parent().remove();
			});
			return false;
		});
	});
</script>

