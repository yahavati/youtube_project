import React from 'react';
import { useParams } from 'react-router-dom';
import VideoPlayer from './VideoPlayer';
import VideoInfo from './VIdeoInfo'; // Corrected capitalization
import CommentsSection from './CommentsSection';
import './App.css';

function VideoDetail({ videos }) {
  const { id } = useParams();
  const video = videos.find(video => video.id.toString() === id);

  if (!video) {
    return <div>Video not found</div>;
  }

  return (
    <div className="video-detail container mt-4">
      <VideoPlayer videoUrl={video.videoUrl} />
      <VideoInfo video={video} /> {/* Corrected capitalization */}
      <CommentsSection />
    </div>
  );
}

export default VideoDetail;
