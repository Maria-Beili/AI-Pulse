<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty users}">
<p> </p>
</c:when>
<c:otherwise>
    <div class="w3-container w3-card w3-round w3-white w3-center w3-section">
        <p><strong>Popular users to follow</strong></p>
    </div>
<c:forEach var="u" items="${users}">       
<div id="${u.id}" class="w3-container w3-card w3-round w3-white w3-center w3-section">
	<p>Friend Suggestion</p>
    <a href="Profile?id=${u.id}" class="menu profile-link" aria-label="Open ${u.name}'s profile">
      <img src="${empty u.picture ? 'assets/default_user.png' : u.picture}" alt="Avatar" style="width:50%"><br>
    </a>
    <div><a href="Profile?id=${u.id}" class="menu profile-link profile-name">${u.name}</a></div>
   	<button type="button" class="followUser w3-row w3-button w3-green w3-section"><i class="fa fa-user-plus"></i> &nbsp;Follow</button> 
</div>
</c:forEach>
</c:otherwise>
</c:choose>