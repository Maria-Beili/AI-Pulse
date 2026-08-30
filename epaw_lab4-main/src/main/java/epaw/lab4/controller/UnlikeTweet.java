package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;

import java.io.IOException;

@WebServlet("/UnlikeTweet")
public class UnlikeTweet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UnlikeTweet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (user != null) {
				try {
					TweetService tweetService = TweetService.getInstance();
					tweetService.removeLike(user.getId(), Integer.parseInt(request.getParameter("id")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
