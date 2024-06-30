import React from 'react';
import Video from './Video';
import './VideoList.css';

function VideoList({ videos, searchQuery }) {
  const filteredVideos = videos.filter(video =>
    video.title.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="row g-2 mt-2">
      {filteredVideos.map(video => (
        <Video {...video} key={video.id} />
      ))}
    </div>
  );
}

export default VideoList;
