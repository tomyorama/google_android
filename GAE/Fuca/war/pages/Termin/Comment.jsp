<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="fuca.model.Comment"%>
<%@ page import="fuca.model.Termin"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
%>
<%
	//Comment p = ((Termin) pageContext.getAttribute("termin"))
	//		.getComments().get(index);
	Comment p = (Comment) pageContext.getAttribute("item");
	if (p == null) {
		p = (Comment) request.getAttribute("item");
	}
	String blobStringPicture = null;
	String blobStringVideo = null;
	if (p.getPicture() != null) {
		blobStringPicture = "/Users/serve/"
				+ p.getPicture().getKeyString() + "/100/80";
	}
	if (p.getVideo() != null) {
		blobStringVideo = "/Users/serveVideo/"
				+ p.getVideo().getKeyString();
	}
	Termin ter = (Termin) request.getAttribute("termin");
	boolean readonly = request.getAttribute("readonly") != null ? (Boolean) request
			.getAttribute("readonly") : false;
	Boolean isYouTube = false;
	String videoId = null;
	Long commentId = p.getLongId();
	String removeCommentUrl = "/Termin/RemoveComment/" + ter.getId()
			+ "/" + commentId;
	isYouTube = p
			.getText()
			.matches(
					"^https?://www.youtube(?:-nocookie)?.com/?(?:v|embed)?/([a-zA-Z0-9-]+).*");

	if (isYouTube) {
		videoId = p.getText().split("v=")[1].split("&")[0];
	}
%>
<c:choose>
	<c:when test="<%=!readonly && isYouTube%>">
		<tr>
			<td><a class="removeComment" title="Izbrisi"
				href="<%=removeCommentUrl%>">(Brisi)</a>${item.user}:</td>
			<td style="text-align: left;"><iframe width="300" height="200"
					src="http://www.youtube.com/embed/<%=videoId%>"> </iframe></td>
			<td><c:if test="<%=blobStringPicture != null%>">
					<img alt="UserImg" src="<%=blobStringPicture%>">
				</c:if> <c:if test="<%=blobStringVideo != null%>">
					<video controls width="320" height="240"
						class="video-js vjs-default-skin">
						<source src="<%=blobStringVideo%>" type="video/mp4">
  						<source src="<%=blobStringVideo%>" type="video/ogg"> 
  						<source src="<%=blobStringVideo%>" type="video/webm"> 

						<object type="application/x-shockwave-flash" data="player.swf"
										width="320" height="240">
    						<param name="allowfullscreen" value="true">
    						<param name="allowscriptaccess" value="always">
   							<param name="flashvars" value="file=<%=blobStringVideo%>">
    						<!--[if IE]><param name="movie" value="player.swf"><![endif]-->
    							<img src="video.jpg" width="854" height="480" alt="Video">
    							<p>Your browser can’t play HTML5 video. <a
												href="<%=blobStringVideo%>"> Download it</a> instead.</p>
 						</object>
					
					</video>
				</c:if></td>
		</tr>
	</c:when>
	<c:when test="<%=!readonly && !isYouTube%>">
		<tr>
			<td><a class="removeComment" title="Izbrisi"
				href="<%=removeCommentUrl%>">(Brisi)</a>${item.user}:</td>
			<td class="pre" style="text-align: left;">${item.text}</td>
			<td><c:if test="<%=blobStringPicture != null%>">
					<img alt="UserImg" src="<%=blobStringPicture%>">
				</c:if> <c:if test="<%=blobStringVideo != null%>">
					<video controls width="320" height="240"
						class="video-js vjs-default-skin">
						<source src="<%=blobStringVideo%>" type="video/mp4">
  						<source src="<%=blobStringVideo%>" type="video/ogg"> 
  						<source src="<%=blobStringVideo%>" type="video/webm"> 

						<object type="application/x-shockwave-flash" data="player.swf"
										width="320" height="240">
    						<param name="allowfullscreen" value="true">
    						<param name="allowscriptaccess" value="always">
   							<param name="flashvars" value="file=<%=blobStringVideo%>">
    						<!--[if IE]><param name="movie" value="player.swf"><![endif]-->
    							<img src="video.jpg" width="854" height="480" alt="Video">
    							<p>Your browser can’t play HTML5 video. <a
												href="<%=blobStringVideo%>"> Download it</a> instead.</p>
 						</object>
					
					
					</video>
				</c:if></td>
		</tr>
	</c:when>
	<c:when test="<%=readonly && isYouTube%>">
		<tr>
			<td>${item.user}:</td>
			<td style="text-align: left;"><iframe width="300" height="200"
					src="http://www.youtube.com/embed/<%=videoId%>"> </iframe></td>
			<td><c:if test="<%=blobStringPicture != null%>">
					<img alt="UserImg" src="<%=blobStringPicture%>">
				</c:if> <c:if test="<%=blobStringVideo != null%>">
					<video controls width="320" height="240"
						class="video-js vjs-default-skin">
						<source src="<%=blobStringVideo%>" type="video/mp4">
  						<source src="<%=blobStringVideo%>" type="video/ogg"> 
  						<source src="<%=blobStringVideo%>" type="video/webm"> 

						<object type="application/x-shockwave-flash" data="player.swf"
										width="320" height="240">
    						<param name="allowfullscreen" value="true">
    						<param name="allowscriptaccess" value="always">
   							<param name="flashvars" value="file=<%=blobStringVideo%>">
    						<!--[if IE]><param name="movie" value="player.swf"><![endif]-->
    							<img src="video.jpg" width="854" height="480" alt="Video">
    							<p>Your browser can’t play HTML5 video. <a
												href="<%=blobStringVideo%>"> Download it</a> instead.</p>
 						</object>
					
					
					
					</video>
				</c:if></td>
		</tr>
	</c:when>
	<c:when test="<%=readonly && !isYouTube%>">
		<tr>
			<td>${item.user}:</td>
			<td class="pre" style="text-align: left;">${item.text}</td>
			<td><c:if test="<%=blobStringPicture != null%>">
					<img alt="UserImg" src="<%=blobStringPicture%>">
				</c:if> <c:if test="<%=blobStringVideo != null%>">
					<video controls width="320" height="240"
						class="video-js vjs-default-skin">
						<source src="<%=blobStringVideo%>" type="video/mp4">
  						<source src="<%=blobStringVideo%>" type="video/ogg"> 
  						<source src="<%=blobStringVideo%>" type="video/webm"> 

						<object type="application/x-shockwave-flash" data="player.swf"
										width="320" height="240">
    						<param name="allowfullscreen" value="true">
    						<param name="allowscriptaccess" value="always">
   							<param name="flashvars" value="file=<%=blobStringVideo%>">
    						<!--[if IE]><param name="movie" value="player.swf"><![endif]-->
    							<img src="video.jpg" width="854" height="480" alt="Video">
    							<p>Your browser can’t play HTML5 video. <a
												href="<%=blobStringVideo%>"> Download it</a> instead.</p>
 						</object>
					
					
					
					</video>
				</c:if></td>
		</tr>
	</c:when>
	<c:otherwise>
		<tr>
			<td>${comment.user}:</td>
			<td><form:input path="text" /> <form:errors path="text" /></td>
			<td><input type="submit" value="Spremi" /></td>
		</tr>
	</c:otherwise>
</c:choose>

