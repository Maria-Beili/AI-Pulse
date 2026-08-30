package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;

import java.io.IOException;
import java.util.List;

@WebServlet("/Tweets")
public class Tweets extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Tweets() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Tweet> tweets = null;
		User user = null;
		// getSession(false): Do not create a new session if one doesn't exist (saves RAM for anonymous users)
		HttpSession session = request.getSession(false);
		// Check for optional uid parameter to show a specific user's tweets (public profile)
		String uidParam = request.getParameter("uid");
		if (uidParam != null) {
			try {
				int uid = Integer.parseInt(uidParam);
				TweetService tweetService = TweetService.getInstance();
				tweets = tweetService.getTweetsByUser(uid,0,10);
			} catch (NumberFormatException e) {
				// ignore invalid uid and fallthrough
			}
		} else {
			if (session != null) {
				user = (User) session.getAttribute("user");
				if (user != null) {
					TweetService tweetService = TweetService.getInstance();
					tweets = tweetService.getFeedTweets(user.getId(),0,10);
				}
			}
			// Requirement: Anonymous users see global timeline (user remains null)
			// If no logged user, show global latest tweets
			if (user == null) {
				TweetService tweetService = TweetService.getInstance();
				tweets = tweetService.getLatestTweets(0, 10);
			}
		}

		if (tweets != null && user != null) {
			TweetService tweetService = TweetService.getInstance();
			for (Tweet t : tweets) {
				t.setLikedByMe(tweetService.isLikedBy(t.getId(), user.getId()));
			}
		}

		request.setAttribute("tweets",tweets);
		request.setAttribute("user",user);
		request.getRequestDispatcher("Tweets.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
