import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./VideoItem.css";

const VideoItem = ({ video }) => {
  const [playing, setPlaying] = useState(false);

  const handleClick = (event) => {
    const videoElement = event.target;
    if (!playing) {
      videoElement.play();
      setPlaying(true);
    } else {
      videoElement.pause();
      setPlaying(false);
    }
  };

  return (
    <div className="video-item">
      <div className="video-thumbnail">
        <Link to={`/video/${video.id}`}>
          <video
            src={video.videoUrl}
            poster={video.img}
            preload="metadata"
            muted
            controls={false}
            onClick={handleClick}
          >
            Your browser does not support the video tag.
          </video>
        </Link>
      </div>
      <div className="video-info">
        <h4>{video.title}</h4>
        <p>{video.author}</p>
        <p>{video.views} - {video.when}</p>
      </div>
    </div>
  );
};

export default VideoItem;
