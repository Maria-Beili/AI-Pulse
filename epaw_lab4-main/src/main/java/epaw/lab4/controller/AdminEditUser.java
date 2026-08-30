package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import epaw.lab4.model.User;
import epaw.lab4.service.UserService;

@MultipartConfig
@WebServlet("/AdminEditUser")
public class AdminEditUser extends HttpServlet {
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
        if (!sessionUser.isAdmin()) {
            response.sendRedirect("Timeline");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect("AdminPanel");
            return;
        }

        try {
            Integer id = Integer.parseInt(idParam);
            User user = userService.getUserById(id);
            if (user == null) {
                response.sendRedirect("AdminPanel");
                return;
            }
            request.setAttribute("user", user);
            request.getRequestDispatcher("AdminEditUser.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("AdminPanel");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        if (!sessionUser.isAdmin()) {
            response.sendRedirect("Timeline");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect("AdminPanel");
            return;
        }

        try {
            Integer id = Integer.parseInt(idParam);
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                response.sendRedirect("AdminPanel");
                return;
            }

            User user = new User();
            try {
                BeanUtils.populate(user, request.getParameterMap());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (user.getRole() == null) {
                user.setRole(existingUser.getRole());
            }

            user.setId(existingUser.getId());
            String newPassword = user.getPassword();
            boolean passwordChanged = newPassword != null && !newPassword.trim().isEmpty();
            if (!passwordChanged) {
                user.setPassword(existingUser.getPassword());
                user.setConfirmPassword(existingUser.getPassword());
            }

            try {
                Part picturePart = request.getPart("picture");
                String picturePath = userService.saveProfilePicture(picturePart, user.getName());
                if (picturePath != null) {
                    user.setPicture(picturePath);
                } else {
                    user.setPicture(existingUser.getPicture());
                }
            } catch (Exception e) {
                e.printStackTrace();
                user.setPicture(existingUser.getPicture());
            }

            Map<String, String> errors = userService.updateUser(user, passwordChanged);
            if (errors.isEmpty()) {
                response.sendRedirect("AdminPanel");
            } else {
                request.setAttribute("user", user);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("AdminEditUser.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("AdminPanel");
        }
    }
}
