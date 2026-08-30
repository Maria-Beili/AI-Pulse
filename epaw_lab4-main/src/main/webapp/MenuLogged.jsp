<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="w3-bar" role="navigation" aria-label="Main menu">
    <a class="menu w3-bar-item w3-button" href="Timeline" aria-label="Home"> <i class="fa fa-home" aria-hidden="true"></i> </a>
    <a class="menu w3-bar-item w3-button w3-hide-small" href="Timeline"> MyPosts </a>
    <a class="menu w3-bar-item w3-button w3-hide-small" href="Profile?id=${sessionScope.user.id}"> Profile </a>
    <a class="menu w3-bar-item w3-button w3-hide-small" href="Followed"> Buddies </a>
    <a class="menu w3-bar-item w3-button w3-hide-small" href="Followers"> Followers </a>
    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
        <a class="menu w3-bar-item w3-button w3-hide-small" href="AdminPanel"> Admin </a>
    </c:if>
    <a class="menu w3-bar-item w3-button w3-hide-small w3-right" href="Logout"> <i class="fa fa-sign-out"></i> </a>
    <a href="javascript:void(0)" class="w3-bar-item w3-button w3-right w3-hide-large w3-hide-medium" onclick="App.stack()" aria-controls="stack">&#9776;</a>
</div>

<div id="stack" class="w3-bar-block w3-hide w3-hide-large w3-hide-medium" role="menu">
    <a class="menu w3-bar-item w3-button" href="Timeline"> Home </a>
    <a class="menu w3-bar-item w3-button" href="Timeline"> MyPosts </a>
    <a class="menu w3-bar-item w3-button" href="Profile?id=${sessionScope.user.id}"> Profile </a>
    <a class="menu w3-bar-item w3-button" href="Followed"> Buddies </a>
    <a class="menu w3-bar-item w3-button" href="Followers"> Followers </a>
    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
        <a class="menu w3-bar-item w3-button" href="AdminPanel"> Admin </a>
    </c:if>
    <a class="menu w3-bar-item w3-button" href="Logout"> Logout </a>
</div>
