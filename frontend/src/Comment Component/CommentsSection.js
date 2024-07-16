import React, { useState, useEffect, useContext } from "react";
import "./CommentsSection.css";
import { UserContext } from "../UserContext";
import { VideosContext } from "../VideosContext";
import { useNavigate } from "react-router-dom";
import {
  addComment,
  updateComment,
  deleteComment,
  toggleLike,
  toggleDislike,
} from "../api/comment";
import { getMediaSource } from "../utils/mediaUtils";

function CommentsSection({
  videoId,
  comments = [],
  onAddComment,
  onUpdateComments,
}) {
  const [commentText, setCommentText] = useState("");
  const [commentList, setCommentList] = useState(comments);
  const { user } = useContext(UserContext);
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

  const handleCommentSubmit = async () => {
    if (!user) {
      navigate("/login");
      return;
    }

    if (commentText.trim()) {
      const newComment = {
        user: {
          _id: user._id,
          photo: user.photo,
          displayName: user.displayName,
        },
        video: videoId,
        text: commentText.trim(),
        createdAt: new Date().toISOString(),
        likes: 0,
        dislikes: 0,
        likedBy: [],
        dislikedBy: [],
      };

      // Optimistically update the UI
      const updatedComments = [...commentList, newComment];
      setCommentList(updatedComments);
      setCommentText("");

      try {
        const addedComment = await addComment({
          user: user._id,
          video: videoId,
          text: commentText.trim(),
        });

        // Update the newly added comment with the data from the server (if needed)
        const finalComments = updatedComments.map((comment) =>
          comment === newComment ? { ...comment, ...addedComment } : comment
        );

        setCommentList(finalComments);
        onAddComment(addedComment);
        updateVideo(videoId, { comments: finalComments });
      } catch (error) {
        console.error("Error adding comment:", error);
        // Revert the optimistic update if there was an error
        setCommentList(commentList);
      }
    }
  };

  const handleToggleLike = async (index) => {
    if (!user) {
      navigate("/login");
      return;
    }

    const updatedComments = [...commentList];
    const comment = updatedComments[index];

    try {
      const updatedComment = await toggleLike(comment._id);
      updatedComments[index] = { ...comment, ...updatedComment };

      setCommentList(updatedComments);
      onUpdateComments(videoId, updatedComments);
      updateVideo(videoId, { comments: updatedComments });
    } catch (error) {
      console.error("Error toggling like on comment:", error);
    }
  };

  const handleToggleDislike = async (index) => {
    if (!user) {
      navigate("/login");
      return;
    }

    const updatedComments = [...commentList];
    const comment = updatedComments[index];

    try {
      const updatedComment = await toggleDislike(comment._id);
      updatedComments[index] = { ...comment, ...updatedComment };

      setCommentList(updatedComments);
      onUpdateComments(videoId, updatedComments);
      updateVideo(videoId, { comments: updatedComments });
    } catch (error) {
      console.error("Error toggling dislike on comment:", error);
    }
  };

  const handleEdit = async (index) => {
    const newText = prompt("Edit comment:", commentList[index].text);
    if (newText !== null) {
      const updatedComments = [...commentList];
      updatedComments[index].text = newText;

      try {
        await updateComment(updatedComments[index]._id, { text: newText });
        setCommentList(updatedComments);
        onUpdateComments(videoId, updatedComments);
        updateVideo(videoId, { comments: updatedComments });
      } catch (error) {
        console.error("Error editing comment:", error);
      }
    }
  };

  const handleDelete = async (index) => {
    const commentId = commentList[index]._id;

    try {
      await deleteComment(commentId);
      const updatedComments = commentList.filter((_, i) => i !== index);
      setCommentList(updatedComments);
      onUpdateComments(videoId, updatedComments);
      updateVideo(videoId, { comments: updatedComments });
    } catch (error) {
      console.error("Error deleting comment:", error);
    }
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
                  src={comment.user.photo && getMediaSource(comment.user.photo)}
                  alt={comment.user.displayName}
                  className="comment-avatar"
                />
                <div>
                  <span className="comment-username">
                    @{comment.user.displayName}
                  </span>
                  <span className="comment-timestamp">
                    {formatTimestamp(comment.createdAt)}
                  </span>
                </div>
              </div>
              <p>{comment.text}</p>
              <div className="comment-actions">
                <button
                  className={`my-button ${
                    comment.likedBy?.includes(user?._id) ? "active" : ""
                  }`}
                  onClick={() => handleToggleLike(index)}
                >
                  <i className="bi bi-hand-thumbs-up"></i> {comment.likes}
                </button>
                <button
                  className={`my-button ${
                    comment.dislikedBy?.includes(user?._id) ? "active" : ""
                  }`}
                  onClick={() => handleToggleDislike(index)}
                >
                  <i className="bi bi-hand-thumbs-down"></i> {comment.dislikes}
                </button>
                {user?.displayName === comment.user.displayName && (
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
