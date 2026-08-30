package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import epaw.lab4.model.User;
import epaw.lab4.service.UserService;

@MultipartConfig
@WebServlet("/EditProfile")
public class EditProfile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        User currentUser = userService.getUserById(sessionUser.getId());
        if (currentUser == null) {
            currentUser = sessionUser;
        }

        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        User existingUser = userService.getUserById(sessionUser.getId());
        if (existingUser == null) {
            existingUser = sessionUser;
        }

        User user = new User();
        try {
            // BeanUtils.populate: Automagically maps all HTML form fields to the User object properties via Reflection
            BeanUtils.populate(user, request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Security: Override Role and ID from the database to prevent Privilege Escalation via form tampering
        user.setRole(existingUser.getRole());
        user.setId(existingUser.getId());

        String newPassword = user.getPassword();
        boolean passwordChanged = newPassword != null && !newPassword.trim().isEmpty();
        if (!passwordChanged) {
            user.setPassword(existingUser.getPassword());
            user.setConfirmPassword(existingUser.getPassword());
        }

        try {
            String picturePath = userService.saveProfilePicture(request.getPart("picture"), user.getName());
            if (picturePath != null) {
                user.setPicture(picturePath);
            } else {
                user.setPicture(existingUser.getPicture());
            }
        } catch (Exception e) {
            e.printStackTrace();
            user.setPicture(existingUser.getPicture());
        }

        Map<String, String> errors = userService.updateProfile(user, passwordChanged);
        if (errors.isEmpty()) {
            // UX / State Consistency: Update the HTTP Session immediately so the UI reflects the new name/picture instantly
            session.setAttribute("user", userService.getUserById(user.getId()));
            request.setAttribute("user", userService.getUserById(user.getId()));
            request.getRequestDispatcher("Profile.jsp").forward(request, response);
        } else {
            request.setAttribute("user", user);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
        }
    }
}
