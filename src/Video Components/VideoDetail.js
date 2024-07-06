// VideoDetail.js
import React, { useEffect, useState, useContext } from 'react';
import { useParams } from 'react-router-dom';
import VideoPlayer from '../Video Components/VideoPlayer';
import VideoInfo from './VideoInfo';
import CommentsSection from '../Comment Component/CommentsSection';
import VideoRecommendation from '../Video Components/VideoRecommendation';
import './VideoDetail.css';
import { VideosContext } from '../VideosContext';

function VideoDetail() {
    const { id } = useParams();
    const { videos, updateVideo } = useContext(VideosContext);
    const [video, setVideo] = useState(null);
    const [likes, setLikes] = useState(0);
    const [dislikes, setDislikes] = useState(0);
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const selectedVideo = videos.find(video => video.id.toString() === id);
        if (selectedVideo) {
            setVideo(selectedVideo);
            setLikes(selectedVideo.likes || 0);
            setDislikes(selectedVideo.dislikes || 0);
            setComments(selectedVideo.comments || []);
        }
    }, [id, videos]);

    if (!video) {
        return <div>Video not found</div>;
    }

    const handleLikeClick = (change) => {
        const newLikes = likes + change;
        setLikes(newLikes);
        updateVideo(video.id, { likes: newLikes });
    };

    const handleDislikeClick = (change) => {
        const newDislikes = dislikes + change;
        setDislikes(newDislikes);
        updateVideo(video.id, { dislikes: newDislikes });
    };

    const handleAddComment = (comment) => {
        const newComments = [...comments, comment];
        setComments(newComments);
        updateVideo(video.id, { comments: newComments });
    };

    const handleUpdateComments = (updatedComments) => {
        setComments(updatedComments);
        updateVideo(video.id, { comments: updatedComments });
    };

    return (
        <div className="video-detail container mt-4">
            <div className="video-content">
                <VideoPlayer key={video.id} videoUrl={video.videoUrl} />
                <VideoInfo
                    video={video}
                    likes={likes}
                    dislikes={dislikes}
                    onLike={handleLikeClick}
                    onDislike={handleDislikeClick}
                />
                <CommentsSection
                    videoId={video.id}
                    comments={comments}
                    onAddComment={handleAddComment}
                    onUpdateComments={handleUpdateComments}
                />
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
