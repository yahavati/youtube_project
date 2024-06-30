import React from 'react';

function VideoPlayer({ videoUrl }) {
  return (
    <video controls className="w-100">
      <source src={videoUrl} type="video/mp4" />
      Your browser does not support the video tag.
    </video>
  );
}

export default VideoPlayer;
