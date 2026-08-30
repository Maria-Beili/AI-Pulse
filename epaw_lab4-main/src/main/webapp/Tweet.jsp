<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
  <c:when test="${not empty tweet}">
    <div id="${tweet.id}" class="tweet-card w3-container w3-card w3-section w3-white w3-round w3-animate-opacity" data-tweet-url="Tweet?id=${tweet.id}">
      <a href="Profile?id=${tweet.uid}" class="menu profile-link" aria-label="Open ${tweet.uname}'s profile">
        <img src="${empty tweet.upicture ? 'assets/default_user.png' : tweet.upicture}" alt="Avatar" class="w3-left w3-circle w3-margin-right tweet-avatar" style="width:60px">
      </a>
      <span class="w3-right w3-opacity"> ${tweet.postDateTime} </span>
      <h4> <a href="Profile?id=${tweet.uid}" class="menu profile-link profile-name"> ${tweet.uname} </a> </h4>
      <hr class="w3-clear">

      <c:choose>
        <c:when test="${canEdit and param.edit eq '1'}">
          <div class="tweet-edit-box" data-tweet-id="${tweet.id}">
            <textarea class="w3-input w3-border tweet-edit-content" rows="4" maxlength="100">${tweet.content}</textarea>
            <input type="hidden" class="remove-image-flag" value="0">
            <c:if test="${not empty tweet.image}">
              <div class="edit-image-preview w3-margin-top">
                <img src="${tweet.image}" alt="Tweet image" class="tweet-post-image edit-current-image">
                <button type="button" class="remove-edit-image" aria-label="Remove image"><i class="fa fa-times"></i></button>
              </div>
            </c:if>
            <label class="w3-small w3-opacity w3-margin-top">Attach an image</label>
            <input type="file" accept="image/*" class="tweet-edit-image w3-input w3-border w3-margin-bottom" />
            <div class="tweet-actions w3-margin-top">
              <button type="button" class="saveTweetEdit w3-button w3-green"><i class="fa fa-save"></i> &nbsp;Save</button>
              <a href="Tweet?id=${tweet.id}" class="menu w3-button w3-light-grey">Cancel</a>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <c:if test="${not empty tweet.image}">
            <img src="${tweet.image}" alt="Tweet image" class="tweet-post-image w3-margin-top">
          </c:if>
          <p class="tweet-content"> ${tweet.content} </p>
          <div class="tweet-actions">
            <c:choose>
                <c:when test="${not empty user}">
                    <c:choose>
                        <c:when test="${tweet.likedByMe}">
                            <button type="button" class="unlikeTweet w3-button w3-theme-d4 w3-margin-bottom"><i class="fa fa-thumbs-down"></i> &nbsp;Unlike (${tweet.likesCount})</button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="likeTweet w3-button w3-theme w3-margin-bottom"><i class="fa fa-thumbs-up"></i> &nbsp;Like (${tweet.likesCount})</button>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <button type="button" class="w3-button w3-theme w3-margin-bottom w3-disabled"><i class="fa fa-thumbs-up"></i> &nbsp;Like (${tweet.likesCount})</button>
                </c:otherwise>
            </c:choose>
            <a href="Tweet?id=${tweet.id}" class="menu w3-button w3-theme-l1 w3-margin-bottom"><i class="fa fa-comment"></i> &nbsp;Comment</a>
            <c:if test="${canEdit}">
              <button type="button" class="editTweet w3-button w3-green w3-margin-bottom"><i class="fa fa-pencil"></i> &nbsp;Edit</button>
              <button type="button" class="delTweet w3-button w3-red w3-margin-bottom"><i class="fa fa-trash"></i> &nbsp;Delete</button>
            </c:if>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="w3-container w3-card w3-section w3-white w3-round">
      <h5>Comments</h5>
      <c:choose>
        <c:when test="${not empty user}">
          <div class="comment-form" data-parent-id="${tweet.id}">
            <textarea class="w3-input w3-border comment-content" rows="3" maxlength="100" placeholder="Write a comment..."></textarea>
            <label class="w3-small w3-opacity">Attach an image</label>
            <input type="file" accept="image/*" class="comment-image w3-input w3-border w3-margin-bottom" />
            <button type="button" class="addComment w3-button w3-theme w3-margin-top"><i class="fa fa-comment"></i> &nbsp;Comment</button>
          </div>
        </c:when>
        <c:otherwise>
          <p>Log in to comment.</p>
        </c:otherwise>
      </c:choose>
      <hr>

      <c:choose>
        <c:when test="${empty comments}">
          <p class="w3-text-grey">No comments yet.</p>
        </c:when>
        <c:otherwise>
          <c:forEach var="c" items="${comments}">
            <div id="${c.id}" class="comment-item w3-border-bottom w3-padding-small" data-comment-id="${c.id}">
              <a href="Profile?id=${c.uid}" class="menu profile-link"><strong>${c.uname}</strong></a>
              <span class="w3-right w3-opacity">${c.postDateTime}</span>
              <c:choose>
                <c:when test="${not empty editingComment and editingComment.id eq c.id}">
                  <div class="comment-edit-box" data-comment-id="${c.id}" data-parent-id="${tweet.id}">
                    <textarea class="w3-input w3-border comment-edit-content" rows="3" maxlength="100">${c.content}</textarea>
                    <input type="hidden" class="remove-image-flag" value="0">
                    <c:if test="${not empty c.image}">
                      <div class="edit-image-preview w3-margin-top">
                        <img src="${c.image}" alt="Comment image" class="tweet-post-image edit-current-image">
                        <button type="button" class="remove-edit-image" aria-label="Remove image"><i class="fa fa-times"></i></button>
                      </div>
                    </c:if>
                    <label class="w3-small w3-opacity w3-margin-top">Attach an image</label>
                    <input type="file" accept="image/*" class="comment-edit-image w3-input w3-border w3-margin-bottom" />
                    <div class="tweet-actions w3-margin-top">
                      <button type="button" class="saveCommentEdit w3-button w3-green"><i class="fa fa-save"></i> &nbsp;Save</button>
                      <button type="button" class="delCommentEdit w3-button w3-red"><i class="fa fa-trash"></i> &nbsp;Delete</button>
                      <button type="button" class="cancelCommentEdit w3-button w3-light-grey">Cancel</button>
                    </div>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="comment-body">
                    <p class="tweet-content comment-content-text">${c.content}</p>
                    <c:if test="${not empty c.image}">
                      <img src="${c.image}" alt="Comment image" class="tweet-post-image w3-margin-top">
                    </c:if>
                    <div class="tweet-actions">
                      <c:choose>
                        <c:when test="${not empty user}">
                          <c:choose>
                            <c:when test="${c.likedByMe}">
                              <button type="button" class="unlikeTweet w3-button w3-theme-d4 w3-margin-bottom"><i class="fa fa-thumbs-down"></i> &nbsp;Unlike (${c.likesCount})</button>
                            </c:when>
                            <c:otherwise>
                              <button type="button" class="likeTweet w3-button w3-theme w3-margin-bottom"><i class="fa fa-thumbs-up"></i> &nbsp;Like (${c.likesCount})</button>
                            </c:otherwise>
                          </c:choose>
                        </c:when>
                        <c:otherwise>
                          <button type="button" class="w3-button w3-theme w3-margin-bottom w3-disabled"><i class="fa fa-thumbs-up"></i> &nbsp;Like (${c.likesCount})</button>
                        </c:otherwise>
                      </c:choose>
                      <c:if test="${not empty user and user.id == c.uid}">
                        <button type="button" class="editComment w3-button w3-green w3-margin-bottom" data-comment-id="${c.id}" data-parent-id="${tweet.id}"><i class="fa fa-pencil"></i> &nbsp;Edit</button>
                      </c:if>
                    </div>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </c:when>
  <c:otherwise>
    <div class="w3-container w3-card w3-round w3-white w3-section">
      <p>Tweet not found.</p>
    </div>
  </c:otherwise>
</c:choose>
