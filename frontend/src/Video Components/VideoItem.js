import React, { useContext, useState } from "react";
import { Link } from "react-router-dom";
import "./VideoItem.css";
import { VideosContext } from "../VideosContext";
import { timeAgo } from "../utils/timeUtils";
import { getMediaSource } from "../utils/mediaUtils";

const VideoItem = ({ video, myVideo }) => {
  const [playing, setPlaying] = useState(false);
  const { deleteVideo } = useContext(VideosContext);

  const handleMouseOver = (event) => {
    if (video.thumbnail) {
      return;
    }
    const videoElement = event.target;
    if (videoElement.readyState >= 2) {
      setPlaying(true);
      videoElement.play().catch((error) => {
        if (error.name !== "AbortError") {
          console.error("Playback failed:", error);
        }
      });
    }
  };

  const handleMouseOut = (event) => {
    if (video.thumbnail) {
      return;
    }
    const videoElement = event.target;
    setPlaying(false);
    if (!videoElement.paused) {
      videoElement.pause();
    }
  };

  const getVideoSource = () => {
    if (video.url.startsWith("http")) {
      return video.url;
    }
    return `data:video/mp4;base64,${video.url}`;
  };

  const getImageSource = () => {
    if (
      video.thumbnail.startsWith("http") ||
      video.thumbnail.startsWith("data")
    ) {
      return video.thumbnail;
    }
    return `data:image;base64,${video.thumbnail}`;
  };

  return (
    <div className="video-item">
      <div className="video-thumbnail">
        <Link
          to={`/video/${video._id}`}
          style={{ textDecoration: "none", color: "inherit" }}
        >
          <video
            src={getVideoSource(video.url)}
            poster={video.thumbnail && getImageSource(video.thumbnail)}
            preload="metadata"
            muted
            onMouseOver={handleMouseOver}
            onMouseOut={handleMouseOut}
          >
            Your browser does not support the video tag.
          </video>
        </Link>
      </div>
      <div className="video-info">
        <h4>{video.title}</h4>
        <div>
          <img
            src={getMediaSource(video.user.photo)}
            alt="user"
            style={{
              width: "40px",
              height: "40px",
              objectFit: "cover",
              borderRadius: "50%",
              marginRight: "10px",
            }}
          />
          <Link style={{ color: "black" }} to={`/videos/${video.user._id}`}>
            {video.user.displayName}
          </Link>
        </div>
        <p>
          {video.views || 0} - {timeAgo(video.createdAt) || "now"}
        </p>
      </div>
    </div>
  );
};

export default VideoItem;
