import React from "react";
import "./VideoPlayer.css";

function VideoPlayer({ videoUrl, thumbnail }) {
  return (
    <div className="video-wrapper">
      <video poster={thumbnail} controls className="video-player">
        <source src={videoUrl} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
    </div>
  );
}

export default VideoPlayer;
