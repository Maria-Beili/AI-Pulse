package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;
import epaw.lab4.service.UserService;

@WebServlet("/AdminPanel")
public class AdminPanel extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        UserService userService = UserService.getInstance();
        TweetService tweetService = TweetService.getInstance();

        List<User> users = userService.getAllUsers();
        List<Tweet> tweets = tweetService.getAllTweets(0, 100);

        request.setAttribute("users", users);
        request.setAttribute("tweets", tweets);
        request.getRequestDispatcher("AdminPanel.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
