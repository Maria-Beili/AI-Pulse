package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import epaw.lab4.service.TweetService;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.util.TweetImageUtil;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Servlet implementation class AddTweet
 */
@MultipartConfig
@WebServlet("/AddTweet")
public class AddTweet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddTweet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);

		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (user != null) {
				try {
					Tweet tweet = new Tweet();
					String content = request.getParameter("content");
					content = content == null ? "" : content.trim();
					String imageName = null;
					String contentType = request.getContentType();
					if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
						Part imagePart = request.getPart("image");
						if (imagePart != null && imagePart.getSize() > 0) {
							imageName = TweetImageUtil.save(imagePart, user.getName());
						}
					}
					if (content.isEmpty() && imageName == null) {
						return;
					}
					String pidStr = request.getParameter("pid");
					if (pidStr != null && !pidStr.trim().isEmpty()) {
						tweet.setPid(Integer.parseInt(pidStr.trim()));
					}
					tweet.setContent(content);
					tweet.setImage(imageName);
					tweet.setUid(user.getId());
					tweet.setUname(user.getName());
					tweet.setPostDateTime(new Timestamp(System.currentTimeMillis()));
					TweetService tweetService = TweetService.getInstance();
					tweetService.add(tweet);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
