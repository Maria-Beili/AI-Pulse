package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;
import epaw.lab4.util.TweetImageUtil;

import java.io.IOException;

@MultipartConfig
@WebServlet("/UpdateTweet")
public class UpdateTweet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		User user = (User) session.getAttribute("user");
		if (user == null) {
			return;
		}

		String idParam = request.getParameter("id");
		if (idParam == null) {
			return;
		}

		try {
			Integer id = Integer.parseInt(idParam);
			TweetService tweetService = TweetService.getInstance();
			Tweet existing = tweetService.getTweetById(id);
			if (existing == null || !user.getId().equals(existing.getUid())) {
				return;
			}

			String content = request.getParameter("content");
			content = content == null ? "" : content.trim();
			String imageName = existing.getImage();

			String removeImage = request.getParameter("removeImage");
			if ("1".equals(removeImage) || "true".equalsIgnoreCase(removeImage)) {
				imageName = null;
			}

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

			tweetService.update(id, user.getId(), content, imageName);
		} catch (NumberFormatException e) {
			// Ignore invalid payload
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
