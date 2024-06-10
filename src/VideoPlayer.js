// VideoPlayer.js
import React from 'react';

function VideoPlayer({ videoUrl }) {
  return (
    <video controls className="w-100 mb-4">
      <source src={videoUrl} type="video/mp4" />
    </video>
  );
}

export default VideoPlayer;
