<%@page import="sun.misc.Request"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@ page import="fuca.model.User"%>
<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
%>
<a href="<c:url value="/Users/EditUser/${user.id}"/>"
	data-u-button="true">Uredi</a>
<h2>${user.nickname}</h2>
<div style="float: left; width: 50%;">
	<c:if test="${not empty blobString}">
		<img alt="UserImg" src="/Users/serve/${blobString}/400/300">
	</c:if>
</div>
<div style="float: left; width: 50%;">
	<table>
		<tr>
			<td>Ime :</td>
			<td>${user.name}</td>
		</tr>
		<tr>
			<td>Prezime :</td>
			<td>${user.lastname}</td>
		</tr>
		<tr>
			<td>Nadimak :</td>
			<td>${user.nickname}</td>
		</tr>
		<tr>
			<td>Email :</td>
			<td>${user.email}</td>
		</tr>
		<tr>
			<td>Opis :</td>
			<td class="pre">${user.description}</td>
		</tr>

	</table>

</div>

<script type="text/javascript">
	$(function() {

		$('#check_file_upload').on('change', function() {
			var value = $(this).val().toUpperCase();
			if (!value.match(/.JPG$|.PNG$|.JPEG$/i)) {
				alert('Dozvoljeni tipovi slike: *.jpg,*.jpeg,*.png');
				$(this).val('');
			}
		});
	});
</script>