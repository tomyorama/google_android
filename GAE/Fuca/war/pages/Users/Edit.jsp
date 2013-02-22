<%@page import="sun.misc.Request"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	String urlToSave = blobstoreService
			.createUploadUrl("/Users/EditUser");
%>
<form:form commandName="userToSave" action="<%=urlToSave%>"
	method="post" enctype="multipart/form-data">
	<button type="submit" value="save">Spremi</button>
	<a href="/Users/Details/${userToSave.id}" data-u-button="true">Odustani</a>
	<h2>${user.nickname}</h2>
	<div style="float: left; width: 50%;">
		<c:if test="${not empty blobString}">
			<img alt="UserImg" src="/Users/serve/${blobString}/400/300">
		</c:if>
		<div>
			<form:label for="picture" path="picture">Promijeni sliku</form:label>
			<br />
			<form:input id="check_file_upload" path="picture" type="file" />
		</div>
	</div>
	<div style="float: left; width: 50%;">
		<table>
			<tr>
				<td>Ime :</td>
				<td><form:input path="name" /></td>
				<td><form:errors path="name" /></td>
			</tr>
			<tr>
				<td>Prezime :</td>
				<td><form:input path="lastname" /></td>
				<td><form:errors path="lastname" /></td>
			</tr>
			<tr>
				<td>Nadimak :</td>
				<td><form:input path="nickname" /></td>
				<td><form:errors path="nickname" /></td>
			</tr>
			<tr>
				<td>Email :</td>
				<td><form:input path="email" /></td>
				<td><form:errors path="email" /></td>
			</tr>
			<tr>
				<td>Opis :</td>
				<td><form:textarea path="description" style="width:98%;"
						rows="5" /></td>
				<td><form:errors path="description" /></td>
			</tr>
		</table>
	</div>
	<form:hidden path="id" />

</form:form>
<script type="text/javascript">
	$(function() {

		$('#check_file_upload').on('change', function() {
			var value = $(this).val().toUpperCase();
			if (!value.match(/.JPG$|.PNG$|.GIF$|.JPEG$/i)) {
				alert('Dozvoljeni tipovi slike: *.jpg,*.jpeg,*.png,.gif');
				$(this).val('');
			}
		});
	});
</script>