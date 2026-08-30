<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
// Execute loads immediately when this fragment is inserted into the DOM.
// Avoid using document.ready here because it doesn't reliably fire when
// the fragment is loaded via jQuery.load().
$('#rcolumn').load('NotFollowed', function(response,status,xhr){ console.log('NotFollowed load',status,xhr && xhr.status); if(status!='success') console.error(xhr && xhr.responseText); });
$('#lcolumn').load('Profile', function(response,status,xhr){ console.log('Profile load',status,xhr && xhr.status); if(status!='success') console.error(xhr && xhr.responseText); });
$('#iterator').load('Tweets', function(response,status,xhr){ console.log('Tweets load',status,xhr && xhr.status); if(status!='success') console.error(xhr && xhr.responseText); });
</script>

<c:choose>
<!-- JSTL Check: Only render Tweet composer if user is logged in -->
<c:when test="${not empty user}">
<div class="w3-container w3-card w3-round w3-white w3-section">
	<h6 class="w3-opacity"> ${user.name}, what are you thinking? </h6>
	<p id="tweetContent" contenteditable="true" class="w3-border w3-padding" style="min-height:80px;"></p>
	<label for="tweetImage" class="w3-small w3-opacity">Attach an image</label>
	<input id="tweetImage" type="file" accept="image/*" class="w3-input w3-border w3-margin-bottom" />
	<button id="addTweet" type="button" class="w3-button w3-theme w3-section"><i class="fa fa-pencil"></i> &nbsp;Post</button> 
</div>
</c:when>
<c:otherwise>
<!-- Fallback for Anonymous Users: Display public banner instead of composer -->
<div class="w3-container w3-card w3-round w3-white w3-section public-timeline-banner">
	<div class="public-timeline-kicker">Discover</div>
	<h3 class="public-timeline-title">Public timeline</h3>
	<p class="public-timeline-copy">Browse the latest posts from the community. Log in to share and interact.</p>
 </div>
</c:otherwise>
</c:choose>
 
<div id="iterator">
<!-- Tweets will be loaded here -->
</div>




