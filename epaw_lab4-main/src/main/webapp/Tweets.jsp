<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:forEach var="t" items="${tweets}">
 <div id="${t.id}" class="tweet-card w3-container w3-card w3-section w3-white w3-round w3-animate-opacity" data-tweet-url="Tweet?id=${t.id}"><br>
   <a href="Profile?id=${t.uid}" class="menu profile-link" aria-label="Open ${t.uname}'s profile">
     <img src="${empty t.upicture ? 'assets/default_user.png' : t.upicture}" alt="Avatar" class="w3-left w3-circle w3-margin-right tweet-avatar" style="width:60px">
   </a>
   <span class="w3-right w3-opacity"> ${t.postDateTime} </span>
  <h4>
    <a href="Profile?id=${t.uid}" class="menu profile-link profile-name"> ${t.uname} </a>
  </h4><br>
  <hr class="w3-clear">
  <p class="tweet-content"> ${t.content} </p>
  <c:if test="${not empty t.image}">
    <img src="${t.image}" alt="Tweet image" class="tweet-post-image w3-margin-top">
  </c:if>
   <div class="tweet-actions">
   <c:choose>
       <c:when test="${not empty user}">
           <c:choose>
               <c:when test="${t.likedByMe}">
                   <button type="button" class="unlikeTweet w3-button w3-theme-d4 w3-margin-bottom"><i class="fa fa-thumbs-down"></i> &nbsp;Unlike (${t.likesCount})</button>
               </c:when>
               <c:otherwise>
                   <button type="button" class="likeTweet w3-button w3-theme w3-margin-bottom"><i class="fa fa-thumbs-up"></i> &nbsp;Like (${t.likesCount})</button>
               </c:otherwise>
           </c:choose>
       </c:when>
       <c:otherwise>
           <button type="button" class="w3-button w3-theme w3-margin-bottom w3-disabled"><i class="fa fa-thumbs-up"></i> &nbsp;Like (${t.likesCount})</button>
       </c:otherwise>
   </c:choose>
   <a href="Tweet?id=${t.id}" class="menu w3-button w3-theme-l1 w3-margin-bottom"><i class="fa fa-comment"></i> &nbsp;Comment</a>
   <c:choose>
     <c:when test="${not empty user and user.id == t.uid}">
       <button type="button" class="editTweet w3-button w3-green w3-margin-bottom"><i class="fa fa-pencil"></i> &nbsp;Edit</button>
       <button type="button" class="delTweet w3-button w3-red w3-margin-bottom"><i class="fa fa-trash"></i> &nbsp;Delete</button>
     </c:when>
   </c:choose>
   </div>
 </div>
</c:forEach>