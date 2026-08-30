package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;

@WebServlet("/AdminDeleteTweet")
public class AdminDeleteTweet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
        if (idParam != null) {
            try {
                Integer id = Integer.parseInt(idParam);
                TweetService tweetService = TweetService.getInstance();
                tweetService.deleteById(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("AdminPanel");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
