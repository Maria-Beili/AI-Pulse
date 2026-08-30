<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="main-container">
    <div class="brand-header">
        <h1><span>AI</span>-Pulse</h1>
        <p>Create your account and start sharing with the community.</p>
    </div>

    <div class="w3-card-4 auth-card">
        <div class="w3-container brand-title-bg">
            <h2>Register</h2>
            <p class="auth-subtitle">Fill in your profile details to create the account.</p>
        </div>

        <form id="registerForm" action="Register" method="POST" enctype="multipart/form-data" class="w3-container w3-padding-24 auth-form">
            <div class="auth-field">
                <label for="name" class="w3-text-theme">Username</label>
                <input class="w3-input w3-border" type="text" id="name" name="name" required minlength="5" maxlength="20"
                    value="${user.name}" placeholder="Choose a username"
                    title="Username must be between 5 and 20 characters." />
            </div>

            <div class="auth-field">
                <label for="email" class="w3-text-theme">Email</label>
                <input class="w3-input w3-border" type="email" id="email" name="email" required maxlength="120"
                    value="${user.email}" placeholder="name@example.com"
                    title="Enter a valid email address." />
            </div>

            <div class="auth-field">
                <label for="phone" class="w3-text-theme">Phone (optional)</label>
                <input class="w3-input w3-border" type="tel" id="phone" name="phone" pattern="[0-9]{9}" maxlength="9"
                    value="${user.phone}" placeholder="Optional: 9 digits"
                    title="Phone must contain 9 digits." />
            </div>

            <div class="auth-field">
                <label for="birthDate" class="w3-text-theme">Birth date</label>
                <input class="w3-input w3-border" type="date" id="birthDate" name="birthDate" required
                    value="${user.birthDate}" title="Birth date is required." />
            </div>

            <div class="auth-field">
                <label for="password" class="w3-text-theme">Password</label>
                <input class="w3-input w3-border" type="password" id="password" name="password" required
                    pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$" value="${user.password}"
                    placeholder="Create a secure password"
                    title="Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*)." />
            </div>

            <div class="auth-field">
                <label for="confirmPassword" class="w3-text-theme">Repeat password</label>
                <input class="w3-input w3-border" type="password" id="confirmPassword" name="confirmPassword" required
                    value="" placeholder="Repeat your password" title="Passwords must match" />
            </div>

            <div class="auth-field">
                <label class="w3-small">
                    <input type="checkbox" id="showRegisterPassword" /> Show passwords
                </label>
            </div>

            <div class="auth-field">
                <label for="picture" class="w3-text-theme">Profile Picture</label>
                <input class="w3-input w3-border" type="file" id="picture" name="picture" accept="image/*" />
            </div>

            <button type="submit" class="w3-button w3-block w3-theme w3-section auth-action">Submit Registration</button>
        </form>
    </div>
</div>

<script>
    App.Errors = {};
    <c:forEach var="error" items="${errors}">
    App.Errors["${error.key}"] = "${error.value}";
    </c:forEach>
    App.initRegisterValidation(App.Errors);

    // UX: Show/Hide Password functionality
    (function () { // IIFE (Immediately Invoked Function Expression) for scope isolation
        var toggle = document.getElementById("showRegisterPassword");
        var password = document.getElementById("password");
        var confirmPassword = document.getElementById("confirmPassword");

        if (toggle && password && confirmPassword) {
            // DOM Manipulation: React to checkbox change to toggle the HTML input type
            toggle.addEventListener("change", function () {
                var inputType = this.checked ? "text" : "password";
                password.type = inputType;
                confirmPassword.type = inputType;
            });
        }
    })();
</script>
