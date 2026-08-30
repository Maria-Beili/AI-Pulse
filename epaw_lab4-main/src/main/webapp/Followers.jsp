<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="w3-container w3-card w3-round w3-white w3-section">
    <h2>Followers</h2>
    <p>People who follow you.</p>

    <c:choose>
        <c:when test="${empty followers}">
            <p>You do not have any followers yet.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="f" items="${followers}">
                <div class="w3-container w3-card w3-round w3-white w3-section w3-row-padding">
                    <div class="w3-col m2">
                        <img src="${empty f.picture ? 'assets/default_user.png' : f.picture}" alt="Avatar" class="w3-circle" style="width:72px;height:72px;object-fit:cover;" />
                    </div>
                    <div class="w3-col m8">
                        <h4>${f.name}</h4>
                        <p><a href="Profile?id=${f.id}" class="menu profile-link">View profile</a></p>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
