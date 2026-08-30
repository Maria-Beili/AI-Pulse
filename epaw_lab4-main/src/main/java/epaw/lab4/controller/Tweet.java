package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import epaw.lab4.model.User;
import java.io.IOException;

@WebServlet("/Tweet")
public class Tweet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        request.setAttribute("user", user);

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Integer id = Integer.parseInt(idParam);
                epaw.lab4.service.TweetService tweetService = epaw.lab4.service.TweetService.getInstance();
                epaw.lab4.model.Tweet tweet = tweetService.getTweetById(id);
                if (tweet != null) {
                    if (user != null) {
                        tweet.setLikedByMe(tweetService.isLikedBy(tweet.getId(), user.getId()));
                    }
                    request.setAttribute("tweet", tweet);
                    // Hierarchical Data Model: Fetch the original root tweet AND its replies (where pid = tweet.id)
                    List<epaw.lab4.model.Tweet> comments = tweetService.getCommentsByParent(id, 0, 100);
                    if (comments != null && user != null) {
                        for (epaw.lab4.model.Tweet c : comments) {
                            c.setLikedByMe(tweetService.isLikedBy(c.getId(), user.getId()));
                        }
                    }
                    request.setAttribute("comments", comments);
                    boolean canEdit = user != null && user.getId().equals(tweet.getUid());
                    request.setAttribute("canEdit", canEdit);

                    String editCommentParam = request.getParameter("editComment");
                    if (editCommentParam != null) {
                        try {
                            Integer editCommentId = Integer.parseInt(editCommentParam);
                            epaw.lab4.model.Tweet editingComment = tweetService.getTweetById(editCommentId);
                            if (editingComment != null && user != null && user.getId().equals(editingComment.getUid())
                                    && editingComment.getPid() != null && editingComment.getPid().equals(tweet.getId())) {
                                request.setAttribute("editingComment", editingComment);
                            }
                        } catch (NumberFormatException e) {
                            // ignore invalid comment id
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // ignore invalid id
            }
        }
        request.getRequestDispatcher("Tweet.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
