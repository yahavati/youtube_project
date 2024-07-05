import React, { useState, useEffect } from 'react';
import './CommentsSection.css';

function CommentsSection({ comments, onAddComment, onUpdateComments }) {
    const [commentText, setCommentText] = useState('');
    const [commentList, setCommentList] = useState(comments);

    useEffect(() => {
        setCommentList(comments);
    }, [comments]);

    const handleCommentChange = (e) => {
        setCommentText(e.target.value);
    };

    const handleCommentSubmit = () => {
        if (commentText.trim()) {
            const newComment = {
                text: commentText.trim(),
                likes: 0,
                dislikes: 0,
                liked: false,
                disliked: false
            };
            const updatedComments = [...commentList, newComment];
            onAddComment(newComment);
            setCommentList(updatedComments);
            onUpdateComments(updatedComments);
            setCommentText('');
        }
    };

    const handleLike = (index) => {
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
        onUpdateComments(updatedComments);
    };

    const handleDislike = (index) => {
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
        onUpdateComments(updatedComments);
    };

    const handleEdit = (index) => {
        const newText = prompt("Edit comment:", commentList[index].text);
        if (newText !== null) {
            const updatedComments = [...commentList];
            updatedComments[index].text = newText;
            setCommentList(updatedComments);
            onUpdateComments(updatedComments);
        }
    };

    const handleDelete = (index) => {
        const updatedComments = commentList.filter((_, i) => i !== index);
        setCommentList(updatedComments);
        onUpdateComments(updatedComments);
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
                {commentList.map((comment, index) => (
                    <div key={index} className="comment-item">
                        <p>{comment.text}</p>
                        <div className="comment-actions">
                            <button className={`my-button ${comment.liked ? 'active' : ''}`} onClick={() => handleLike(index)}>
                                <i className="bi bi-hand-thumbs-up"></i> {comment.likes}
                            </button>
                            <button className={`my-button ${comment.disliked ? 'active' : ''}`} onClick={() => handleDislike(index)}>
                                <i className="bi bi-hand-thumbs-down"></i> {comment.dislikes}
                            </button>
                            <button onClick={() => handleEdit(index)}>Edit</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default CommentsSection;