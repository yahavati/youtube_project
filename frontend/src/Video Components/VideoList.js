import React from "react";
import "./VideoList.css";
import VideoItem from "./VideoItem";

function VideoList({ videos, searchQuery }) {
  console.log(videos);
  const filteredVideos = searchQuery
    ? videos.filter(
        (video) =>
          video.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
          video.user.displayName
            .toLowerCase()
            .includes(searchQuery.toLowerCase())
      )
    : videos;

  return (
    <div className="video-list">
      {filteredVideos.map((video) => (
        <VideoItem key={video._id} video={video} />
      ))}
    </div>
  );
}

export default VideoList;
