<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<link href="/Content/Site.css" rel="stylesheet" type="text/css" />
<link href="/Content/jquery/blue/jquery-ui-1.8.21.custom.css"
	rel="stylesheet" type="text/css" />
<link href="/Content/custom.css" rel="stylesheet" type="text/css" />

<script src="/Scripts/lib/jquery.min.js" type="text/javascript"></script>
<script src="/Scripts/jquery-ui-1.8.21.custom.min.js"
	type="text/javascript"></script>
<script src="/Scripts/jqgrid/grid.locale-hr.js" type="text/javascript"></script>
<script src="/Scripts/jqgrid/jquery.jqGrid.min.js"
	type="text/javascript"></script>
<link rel="stylesheet" href="/Scripts/jqgrid/ui.jqgrid.css"
	type="text/css" media="all" />
<script src="/Scripts/custom/customgrid.js" type="text/javascript"></script>
<script src="/Scripts/custom/usersGrid.js" type="text/javascript"></script>

<script src="/Scripts/init.js" type="text/javascript"></script>


<link href="http://vjs.zencdn.net/c/video-js.css" rel="stylesheet">
<script src="http://vjs.zencdn.net/c/video.js"></script>


<tiles:insertAttribute name="header" />

</head>
<body>
	<header>
		<div class="content-wrapper">
			<div class="float-left">
				<img alt="XX" src="../../Content/images/football-icon.png" />
			</div>
			<div class="float-right">
				<section id="login">
					<tiles:insertAttribute name="login" />
				</section>
				<nav>
					<ul id="menu">
						<li><a href="<c:url value="/"/>">Početna</a></li>
						<li><a href="<c:url value="/Termin"/>">Termin</a></li>
						<li><a href="<c:url value="/Users"/>">Igrači</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</header>
	<div id="body">
		<section class="content-wrapper main-content clear-fix">
			<tiles:insertAttribute name="body" />
		</section>
	</div>
	<footer>
		<div class="content-wrapper">
			<div class="float-left">
				<p>&copy;Fuca-termin</p>
			</div>
		</div>
	</footer>
</body>
</html>
