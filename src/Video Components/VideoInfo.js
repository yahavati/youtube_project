import React, { useContext, useState } from "react";
import ShareModal from "../Upload and Share Components/ShareModal";
import "./VideoInfo.css";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../UserContext";

function VideoInfo({ video, likes, dislikes, onLike, onDislike }) {
  const [liked, setLiked] = useState(false);
  const [disliked, setDisliked] = useState(false);
  const [isShareModalOpen, setIsShareModalOpen] = useState(false);
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);
  const { authenticatedUser } = useContext(UserContext);
  const navigate = useNavigate();

  const toggleDescription = () => {
    setIsDescriptionExpanded(!isDescriptionExpanded);
  };

  const truncateDescription = (text, maxLength) => {
    if (!text || text.length <= maxLength) return text;
    return text.slice(0, maxLength) + "...";
  };

  const handleLikeClick = () => {
    if (!authenticatedUser) {
      navigate("/login");
      return;
    }
    if (liked) {
      setLiked(false);
      onLike(-1);
    } else {
      setLiked(true);
      if (disliked) {
        setDisliked(false);
        onDislike(-1);
      }
      onLike(1);
    }
  };

  const handleDislikeClick = () => {
    if (!authenticatedUser) {
      navigate("/login");
      return;
    }
    if (disliked) {
      setDisliked(false);
      onDislike(-1);
    } else {
      setDisliked(true);
      if (liked) {
        setLiked(false);
        onLike(-1);
      }
      onDislike(1);
    }
  };

  const handleShareClick = () => {
    setIsShareModalOpen(true);
  };

  return (
    <div className="video-info">
      <h2 className="video-title">{video.title}</h2>
      <div className="video-details">
        <div className="video-metadata">
          <div>
            <strong>{video.author.toUpperCase()}</strong>
          </div>
        </div>
        <div className="video-actions">
          <div
            className={`my-button btn-container ${disliked ? "active" : ""}`}
            onClick={handleDislikeClick}
          >
            <button className="btn btn-dislike">
              <i className="bi bi-hand-thumbs-down"></i>
            </button>
            <span>{dislikes}</span>
          </div>
          <div
            className={`my-button btn-container ${liked ? "active" : ""}`}
            onClick={handleLikeClick}
          >
            <button className="btn btn-like">
              <i className="bi bi-hand-thumbs-up"></i>
            </button>
            <span>{likes}</span>
          </div>
          <div className="my-button btn-container" onClick={handleShareClick}>
            <button className="btn btn-share">
              <i className="bi bi-share"></i>
            </button>
          </div>
        </div>
      </div>
      <div className="description-container">
        <div className="upper-container">
          <div>
            <strong>Views:</strong> {video.views} - {video.when}
          </div>
        </div>
        <div className="description">
          {isDescriptionExpanded
            ? video.description
            : truncateDescription(video.description, 200)}
          {video.description && video.description.length > 200 && (
            <button onClick={toggleDescription} className="expand-button">
              {isDescriptionExpanded ? "Show less" : "Click for more"}
            </button>
          )}
        </div>
      </div>
      <ShareModal
        isOpen={isShareModalOpen}
        onClose={() => setIsShareModalOpen(false)}
        videoUrl={video.videoUrl}
      />
    </div>
  );
}

export default VideoInfo;
