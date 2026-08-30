<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<section class="admin-panel w3-container w3-card w3-round w3-white w3-section">
    <h2>Admin Panel</h2>
    <p class="admin-panel-subtitle">Manage user accounts and posts with full moderation controls.</p>

    <h3>Users</h3>
    <c:choose>
        <c:when test="${empty users}">
            <p>No users available.</p>
        </c:when>
        <c:otherwise>
            <div class="w3-responsive">
                <table class="w3-table-all admin-table w3-margin-bottom">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>${u.id}</td>
                                <td><a href="Profile?id=${u.id}" class="profile-link">${u.name}</a></td>
                                <td>${u.email}</td>
                                <td>${u.role}</td>
                                <td>
                                    <a class="menu w3-button w3-blue w3-small" href="AdminEditUser?id=${u.id}">Edit</a>
                                    <button type="button" class="deleteUser w3-button w3-red w3-small" data-id="${u.id}">Delete</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>

    <h3>Posts</h3>
    <c:choose>
        <c:when test="${empty tweets}">
            <p>No posts available.</p>
        </c:when>
        <c:otherwise>
            <div class="w3-responsive">
                <table class="w3-table-all admin-table w3-margin-bottom">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Author</th>
                            <th>Content</th>
                            <th>Posted</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="t" items="${tweets}">
                            <tr>
                                <td>${t.id}</td>
                                <td><a href="Profile?id=${t.uid}" class="profile-link">${t.uname}</a></td>
                                <td>${t.content}</td>
                                <td>${t.postDateTime}</td>
                                <td>
                                    <button type="button" class="deleteTweetAdmin w3-button w3-red w3-small" data-id="${t.id}">Delete</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    $(document).on("click", ".deleteUser", function (event) {
        event.preventDefault();
        var id = $(this).data("id");
        $.post("DeleteUser", { id: id }, function () {
            $("#content").load("AdminPanel");
        });
    });

    $(document).on("click", ".deleteTweetAdmin", function (event) {
        event.preventDefault();
        var id = $(this).data("id");
        $.post("AdminDeleteTweet", { id: id }, function () {
            $("#content").load("AdminPanel");
        });
    });
</script>
