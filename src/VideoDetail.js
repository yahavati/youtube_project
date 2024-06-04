import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import Videos from './Videos'; // Corrected import statement

function VideoDetail() {
  const { id } = useParams();
  const video = Videos.find((video) => video.id.toString() === id);

  const [liked, setLiked] = useState(false);
  const [disliked, setDisliked] = useState(false);

  if (!video) {
    return <div>Video not found</div>;
  }

  const handleLikeClick = () => {
    setLiked(!liked);
    if (disliked) setDisliked(false);
  };

  const handleDislikeClick = () => {
    setDisliked(!disliked);
    if (liked) setLiked(false);
  };

  return (
    <div className="video-detail container mt-4">
      <video controls className="w-100 mb-4">
        <source src={video.videoUrl} type="video/mp4" />
      </video>
      <div className="video-info">
        <h2 className="video-title">{video.title}</h2>
        <div className="video-details">
          <div className="video-metadata">
            <p><strong>Author:</strong> {video.author}</p>
            <p><strong>Views:</strong> {video.views} - {video.when}</p>
          </div>
          <div className="video-actions">
            <div className={`btn-container ${liked ? 'active' : ''}`} onClick={handleLikeClick}>
              <button className="btn btn-like">
                <i class="bi bi-hand-thumbs-up-fill"></i>
              </button>
            </div>
            <div className={`btn-container ${disliked ? 'active' : ''}`} onClick={handleDislikeClick}>
              <button className="btn btn-dislike">
                <i class="bi bi-hand-thumbs-down-fill"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default VideoDetail;
