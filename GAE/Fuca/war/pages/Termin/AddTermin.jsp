<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h1>Dodaj termin</h1>
<form:form commandName="termin">
<%-- 	<form:errors path="*" cssClass="errorblock" element="div" /> --%>
	<table>
		<tr>
			<td>Name :</td>
			<td><form:input path="name" /></td>
			<td><form:errors path="name" /></td>
		</tr>
		<tr>
			<td>Team1 :</td>
			<td><form:input path="team1" /></td>
			<td><form:errors path="team1" /></td>
		</tr>
		<tr>
			<td>Team2 :</td>
			<td><form:input path="team2" /></td>
			<td><form:errors path="team2" /></td>
		</tr>
		<tr>
			<td>Result :</td>
			<td><form:input path="result" /></td>
			<td><form:errors path="result" /></td>
		</tr>
		<tr>
			<td>Datum :</td>
			<td><form:input path="date" data-u-datepicker="true" /></td>
			<td><form:errors path="date" /></td>
		</tr>
	</table>
	<input type="submit" value="Spremi" />
</form:form>

