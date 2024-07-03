import React, { useState, useRef } from 'react';
import { Link } from 'react-router-dom';
import './VideoRecommendation.css';

function VideoRecommendation({ id, title, author, views, when, videoUrl }) {
    const [isPlaying, setIsPlaying] = useState(false);
    const videoRef = useRef(null);

    const handleMouseOver = () => {
        const video = videoRef.current;
        if (video.readyState >= 2) {
            setIsPlaying(true);
            video.play().catch(error => {
                if (error.name !== "AbortError") {
                    console.error("Playback failed:", error);
                }
            });
        }
    };

    const handleMouseOut = () => {
        setIsPlaying(false);
        if (videoRef.current && !videoRef.current.paused) {
            videoRef.current.pause();
        }
    };

    return (
        <Link to={`/video/${id}`} className="video-recommendation-item">
            <div className="video-thumbnail">
                <video
                    ref={videoRef}
                    src={videoUrl}
                    preload="metadata"
                    muted
                    onMouseOver={handleMouseOver}
                    onMouseOut={handleMouseOut}
                >
                    Your browser does not support the video tag.
                </video>
            </div>
            <div className="video-info">
                <h4>{title}</h4>
                <p>{author}</p>
                <p>{views} - {when}</p>
            </div>
        </Link>
    );
}

export default VideoRecommendation;