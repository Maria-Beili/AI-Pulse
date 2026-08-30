<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
  <c:when test="${not empty profileUser}">
    <div id="${profileUser.id}" class="w3-container w3-card w3-round w3-white w3-section w3-center">
      <h4>Profile: ${profileUser.name}</h4>
      <p>
        <a href="Profile?id=${profileUser.id}" class="menu profile-link" aria-label="Open ${profileUser.name}'s profile">
          <img src="${empty profileUser.picture ? 'assets/default_user.png' : profileUser.picture}" class="w3-circle tweet-avatar" style="height:106px;width:106px" alt="Avatar">
        </a>
      </p>
      <hr>
      <p class="w3-left-align"> <a href="Profile?id=${profileUser.id}" class="menu profile-link profile-name"><i class="fa fa-id-card fa-fw w3-margin-right"></i> ${profileUser.name}</a> </p>
      <p class="w3-left-align"> <i class="fa fa-envelope fa-fw w3-margin-right"></i> ${profileUser.email} </p>
      <!-- ACL Check: Only show Edit button if logged-in user matches the profile owner -->
      <c:if test="${not empty user and user.id == profileUser.id}">
        <a href="EditProfile" class="menu editUser w3-row w3-button w3-green w3-section" style="text-decoration:none;display:inline-block;"><i class="fa fa-user-plus"></i> &nbsp;Edit</a>
        <a href="Followers" class="menu editUser w3-row w3-button w3-blue w3-section" style="text-decoration:none;display:inline-block;"><i class="fa fa-users"></i> &nbsp;View Followers</a>
      </c:if>
    </div>
    <br>
    <div id="iterator">
      <!-- Tweets for this user will be loaded into #iterator by Timeline/Profile loader -->
    </div>
    <script>
      // load this user's tweets into the iterator placeholder
      (function(){
        // UI trick: Hide sidebars and center content specifically for the Profile view
        if (typeof App !== 'undefined' && App.setSingleColumnLayout) {
          App.setSingleColumnLayout();
        }
        var uid = '${profileUser.id}';
        if (uid) {
          $('#iterator').load('Tweets?uid=' + uid, function(resp,status,xhr){
            console.log('Profile tweets load', status, xhr && xhr.status);
            if(status!='success') console.error(xhr && xhr.responseText);
          });
        }
      })();
    </script>
  </c:when>
  <c:when test="${not empty user}">
    <div id="${user.id}" class="w3-container w3-card w3-round w3-white w3-section w3-center">
      <h4><a href="Profile?id=${user.id}" class="menu" style="text-decoration:none;">My Profile</a></h4>
      <p>
        <a href="Profile?id=${user.id}" class="menu" aria-label="Open My Profile">
          <img src="${empty user.picture ? 'assets/default_user.png' : user.picture}" class="w3-circle w3-hover-opacity" style="height:106px;width:106px" alt="Avatar">
        </a>
      </p>
      <hr>
      <p class="w3-left-align"> <a href="Profile?id=${user.id}" class="menu profile-link profile-name" style="text-decoration:none;"><i class="fa fa-id-card fa-fw w3-margin-right"></i> ${user.name}</a> </p>
      <a href="EditProfile" class="menu editUser w3-row w3-button w3-green w3-section" style="text-decoration:none;display:inline-block;"><i class="fa fa-user-plus"></i> &nbsp;Edit</a>
      <a href="Followers" class="menu editUser w3-row w3-button w3-blue w3-section" style="text-decoration:none;display:inline-block;"><i class="fa fa-users"></i> &nbsp;View Followers</a>
    </div>
    <br>
  </c:when>
  <c:otherwise>
    <p></p>
  </c:otherwise>
</c:choose>