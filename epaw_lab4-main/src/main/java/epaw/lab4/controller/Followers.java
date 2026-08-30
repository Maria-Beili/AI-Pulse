package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import epaw.lab4.model.User;
import epaw.lab4.service.UserService;

@WebServlet("/Followers")
public class Followers extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        UserService userService = UserService.getInstance();
        List<User> followers = userService.getFollowersUsers(user.getId(), 0, 50);
        request.setAttribute("followers", followers);
        request.getRequestDispatcher("Followers.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
