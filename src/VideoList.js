import React from 'react';
import Videos from './Videos'; // Ensure this path is correct
import Video from './Video'; // Ensure this path is correct

function VideoList({ searchQuery }) {
  const filteredVideos = Videos.filter((video) =>
    video.title.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="row g-2 mt-2">
      {filteredVideos.map((video) => (
        <Video {...video} key={video.id} />
      ))}
    </div>
  );
}

export default VideoList;
