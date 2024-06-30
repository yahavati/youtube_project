import React from 'react';
import { useParams } from 'react-router-dom';
import VideoPlayer from './VideoPlayer';
import VideoInfo from './VIdeoInfo';
import CommentsSection from './CommentsSection';
import VideoRecommendation from './VideoRecommendation';
import './VideoDetail.css';

function VideoDetail({ videos }) {
  const { id } = useParams();
  const video = videos.find(video => video.id.toString() === id);

  if (!video) {
    return <div>Video not found</div>;
  }

  return (
    <div className="video-detail container mt-4">
      <div className="video-content">
        <VideoPlayer videoUrl={video.videoUrl} />
        <VideoInfo video={video} />
        <CommentsSection />
      </div>
      <div className="video-recommendations">
        {videos.filter(v => v.id.toString() !== id).map(video => (
          <VideoRecommendation key={video.id} {...video} />
        ))}
      </div>
    </div>
  );
}

export default VideoDetail;
