import React, { useContext, useState } from "react";
import { Link } from "react-router-dom";
import "./VideoItem.css";
import { VideosContext } from "../VideosContext";
import { timeAgo } from "../utils/timeUtils";

const VideoItem = ({ video, myVideo }) => {
  const [playing, setPlaying] = useState(false);
  const { deleteVideo } = useContext(VideosContext);

  const handleMouseOver = (event) => {
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

  return (
    <div className="video-item">
      <div className="video-thumbnail">
        <Link
          to={`/video/${video._id}`}
          style={{ textDecoration: "none", color: "inherit" }}
        >
          <video
            src={getVideoSource(video.url)}
            poster={video.thumbnail}
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
        {!myVideo ? (
          <>
            <Link to={`/videos/${video.user._id}`}>
              {video.user.displayName}
            </Link>
            <p>
              {video.views || 0} - {timeAgo(video.createdAt) || "now"}
            </p>
          </>
        ) : (
          <img
            src="/delete.png"
            alt="delete button"
            className="delete-video-button"
            onClick={() => deleteVideo(video.id)}
          />
        )}
      </div>
    </div>
  );
};

export default VideoItem;
