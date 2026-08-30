package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Profile")
public class Profile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // If an id parameter is provided, load that user's public profile
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Integer id = Integer.parseInt(idParam);
                epaw.lab4.service.UserService userService = epaw.lab4.service.UserService.getInstance();
                epaw.lab4.model.User profileUser = userService.getUserById(id);
                if (profileUser != null) {
                    request.setAttribute("profileUser", profileUser);
                }
            } catch (NumberFormatException e) {
                // ignore invalid id
            }
        }
        request.getRequestDispatcher("Profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}