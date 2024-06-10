// CommentsSection.js
import React, { useState } from 'react';

function CommentsSection() {
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [editingCommentText, setEditingCommentText] = useState('');

  const handleAddComment = () => {
    if (newComment.trim()) {
      setComments([
        ...comments,
        { id: Date.now(), text: newComment, likes: 0, dislikes: 0, liked: false, disliked: false },
      ]);
      setNewComment('');
    }
  };

  const handleEditComment = (id) => {
    setEditingCommentId(id);
    const commentToEdit = comments.find((comment) => comment.id === id);
    setEditingCommentText(commentToEdit.text);
  };

  const handleUpdateComment = () => {
    setComments(
      comments.map((comment) =>
        comment.id === editingCommentId
          ? { ...comment, text: editingCommentText }
          : comment
      )
    );
    setEditingCommentId(null);
    setEditingCommentText('');
  };

  const handleDeleteComment = (id) => {
    setComments(comments.filter((comment) => comment.id !== id));
  };

  const handleLikeComment = (id) => {
    setComments(
      comments.map((comment) =>
        comment.id === id
          ? {
              ...comment,
              likes: comment.liked ? comment.likes - 1 : comment.likes + 1,
              dislikes: comment.disliked ? comment.dislikes - 1 : comment.dislikes,
              liked: !comment.liked,
              disliked: false,
            }
          : comment
      )
    );
  };

  const handleDislikeComment = (id) => {
    setComments(
      comments.map((comment) =>
        comment.id === id
          ? {
              ...comment,
              dislikes: comment.disliked ? comment.dislikes - 1 : comment.dislikes + 1,
              likes: comment.liked ? comment.likes - 1 : comment.likes,
              disliked: !comment.disliked,
              liked: false,
            }
          : comment
      )
    );
  };

  return (
    <div className="comments-section">
      <h3>Comments</h3>
      <div className="add-comment">
        <textarea
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          placeholder="Add a comment"
        />
        <button onClick={handleAddComment}>Comment</button>
      </div>
      <div className="comments-list">
        {comments.map((comment) => (
          <div key={comment.id} className="comment">
            {editingCommentId === comment.id ? (
              <div className="edit-comment">
                <textarea
                  value={editingCommentText}
                  onChange={(e) => setEditingCommentText(e.target.value)}
                />
                <button onClick={handleUpdateComment}>Update</button>
              </div>
            ) : (
              <>
                <p>{comment.text}</p>
                <div className="comment-actions">
                  <button onClick={() => handleLikeComment(comment.id)}>
                    <i className={`bi bi-hand-thumbs-up ${comment.liked ? 'liked' : ''}`}></i> {comment.likes}
                  </button>
                  <button onClick={() => handleDislikeComment(comment.id)}>
                    <i className={`bi bi-hand-thumbs-down ${comment.disliked ? 'disliked' : ''}`}></i> {comment.dislikes}
                  </button>
                  <button onClick={() => handleEditComment(comment.id)}>Edit</button>
                  <button onClick={() => handleDeleteComment(comment.id)}>Delete</button>
                </div>
              </>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default CommentsSection;
