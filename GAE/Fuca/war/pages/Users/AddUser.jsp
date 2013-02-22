<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h1>Dodaj igrača</h1>
<form:form commandName="user">
<%-- 	<form:errors path="*" cssClass="errorblock" element="div" /> --%>
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
	</table>
	<input type="submit" value="Spremi" />
</form:form>



