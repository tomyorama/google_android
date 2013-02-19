<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="search_patients" data-url="/Termin/termindata"></div>
<table id="patient_search_grid" class="scroll">
</table>
<div id="patient_nav" class="scroll" style="text-align: center;">
</div>
<a href="<c:url value="/Admin/AddTermin"/>" data-u-button="true">Dodaj
	novi</a>
<%-- <a id="test" href="<c:url value="#"/>">Test</a> --%>
<script type="text/javascript">
	$(function() {

		$('#test').on('click', function() {
			$.ajax({
				url : "/Termin/termindata?page=21",
				datatype : 'json'
			}).done(function(data) {
				alert(data);
			});
			return false;
		});
	});
</script>
