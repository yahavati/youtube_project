import React from "react";
import "./VideoList.css";
import VideoItem from "./VideoItem";

function VideoList({ videos, searchQuery }) {
  const filteredVideos = searchQuery
    ? videos.filter(
        (video) =>
          video.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
          video.author.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : videos;

  return (
    <div className="video-list">
      {filteredVideos.map((video) => (
        <VideoItem key={video.id} video={video} />
      ))}
    </div>
  );
}

export default VideoList;
