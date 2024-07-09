import React, { useState, useEffect, useContext } from "react";
import "./CommentsSection.css";
import { UserContext } from "../UserContext";
import { VideosContext } from "../VideosContext";
import { useNavigate } from "react-router-dom";

function CommentsSection({
  videoId,
  comments = [],
  onAddComment,
  onUpdateComments,
}) {
  const [commentText, setCommentText] = useState("");
  const [commentList, setCommentList] = useState(comments);
  const { authenticatedUser } = useContext(UserContext);
  const { updateVideo } = useContext(VideosContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (Array.isArray(comments)) {
      setCommentList(comments);
    }
  }, [comments]);

  const handleCommentChange = (e) => {
    setCommentText(e.target.value);
  };

  const handleCommentSubmit = () => {
    if (!authenticatedUser) {
      navigate("/login");
      return;
    }

    if (commentText.trim()) {
      const newComment = {
        username: authenticatedUser.username,
        avatar: URL.createObjectURL(authenticatedUser.picture),
        text: commentText.trim(),
        likes: 0,
        dislikes: 0,
        liked: false,
        disliked: false,
        timestamp: new Date().toISOString(),
      };
      const updatedComments = [...commentList, newComment];
      setCommentList(updatedComments);
      onAddComment(newComment);
      updateVideo(videoId, { comments: updatedComments });
      setCommentText("");
    }
  };

  const handleLike = (index) => {
    if (!authenticatedUser) {
      navigate("/login");
      return;
    }

    const updatedComments = [...commentList];
    if (updatedComments[index].liked) {
      updatedComments[index].likes -= 1;
      updatedComments[index].liked = false;
    } else {
      updatedComments[index].likes += 1;
      updatedComments[index].liked = true;
      if (updatedComments[index].disliked) {
        updatedComments[index].dislikes -= 1;
        updatedComments[index].disliked = false;
      }
    }
    setCommentList(updatedComments);
    onUpdateComments(videoId, updatedComments);
    updateVideo(videoId, { comments: updatedComments });
  };

  const handleDislike = (index) => {
    if (!authenticatedUser) {
      navigate("/login");
      return;
    }

    const updatedComments = [...commentList];
    if (updatedComments[index].disliked) {
      updatedComments[index].dislikes -= 1;
      updatedComments[index].disliked = false;
    } else {
      updatedComments[index].dislikes += 1;
      updatedComments[index].disliked = true;
      if (updatedComments[index].liked) {
        updatedComments[index].likes -= 1;
        updatedComments[index].liked = false;
      }
    }
    setCommentList(updatedComments);
    onUpdateComments(videoId, updatedComments);
    updateVideo(videoId, { comments: updatedComments });
  };

  const handleEdit = (index) => {
    const newText = prompt("Edit comment:", commentList[index].text);
    if (newText !== null) {
      const updatedComments = [...commentList];
      updatedComments[index].text = newText;
      setCommentList(updatedComments);
      onUpdateComments(videoId, updatedComments);
      updateVideo(videoId, { comments: updatedComments });
    }
  };

  const handleDelete = (index) => {
    const updatedComments = commentList.filter((_, i) => i !== index);
    setCommentList(updatedComments);
    onUpdateComments(videoId, updatedComments);
    updateVideo(videoId, { comments: updatedComments });
  };

  const formatTimestamp = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleDateString() + " " + date.toLocaleTimeString();
  };

  return (
    <div className="comments-section">
      <h3>Comments</h3>
      <div className="add-comment">
        <input
          type="text"
          value={commentText}
          onChange={handleCommentChange}
          placeholder="Add a comment"
        />
        <button onClick={handleCommentSubmit}>Comment</button>
      </div>
      <div className="comments-list">
        {commentList.length > 0 &&
          commentList.map((comment, index) => (
            <div key={index} className="comment-item">
              <div className="comment-header">
                <img
                  src={comment.avatar}
                  alt={comment.username}
                  className="comment-avatar"
                />
                <div>
                  <span className="comment-username">@{comment.username}</span>
                  <span className="comment-timestamp">
                    {formatTimestamp(comment.timestamp)}
                  </span>
                </div>
              </div>
              <p>{comment.text}</p>
              <div className="comment-actions">
                <button
                  className={`my-button ${comment.liked ? "active" : ""}`}
                  onClick={() => handleLike(index)}
                >
                  <i className="bi bi-hand-thumbs-up"></i> {comment.likes}
                </button>
                <button
                  className={`my-button ${comment.disliked ? "active" : ""}`}
                  onClick={() => handleDislike(index)}
                >
                  <i className="bi bi-hand-thumbs-down"></i> {comment.dislikes}
                </button>
                {authenticatedUser?.username === comment.username && (
                  <>
                    <button onClick={() => handleEdit(index)}>Edit</button>
                    <button onClick={() => handleDelete(index)}>Delete</button>
                  </>
                )}
              </div>
            </div>
          ))}
      </div>
    </div>
  );
}

export default CommentsSection;
