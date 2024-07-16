import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";
import "./VideoRecommendation.css";
import { timeAgo } from "../utils/timeUtils";
import { getMediaSource } from "../utils/mediaUtils";

function VideoRecommendation({
  _id,
  title,
  user,
  views,
  createdAt,
  url,
  thumbnail,
}) {
  const [isPlaying, setIsPlaying] = useState(false);
  const videoRef = useRef(null);

  const handleMouseOver = () => {
    const video = videoRef.current;
    if (video.readyState >= 2) {
      setIsPlaying(true);
      video.play().catch((error) => {
        if (error.name !== "AbortError") {
          console.error("Playback failed:", error);
        }
      });
    }
  };

  const handleMouseOut = () => {
    setIsPlaying(false);
    if (videoRef.current && !videoRef.current.paused) {
      videoRef.current.pause();
    }
  };

  return (
    <Link
      to={`/video/${_id}`}
      className="video-recommendation-item"
      style={{ textDecoration: "none", color: "black" }}
    >
      <div className="video-thumbnail">
        <video
          ref={videoRef}
          poster={thumbnail}
          src={getMediaSource(url)}
          preload="metadata"
          muted
          onMouseOver={handleMouseOver}
          onMouseOut={handleMouseOut}
        >
          Your browser does not support the video tag.
        </video>
      </div>
      <div className="video-info">
        <h4>{title}</h4>
        <p>{user.displayName}</p>
        <p>
          {views} - {timeAgo(createdAt)}
        </p>
      </div>
    </Link>
  );
}

export default VideoRecommendation;
