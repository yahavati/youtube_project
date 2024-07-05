import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './VideoList.css';

function VideoList({ videos, searchQuery }) {
    const filteredVideos = videos.filter(video =>
        video.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        video.author.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const [playingStates, setPlayingStates] = useState({});

    const handleMouseOver = (videoId, event) => {
        const video = event.target;
        if (video.readyState >= 2) {
            setPlayingStates(prev => ({ ...prev, [videoId]: true }));
            video.play().catch(error => {
                if (error.name !== "AbortError") {
                    console.error("Playback failed:", error);
                }
            });
        }
    };

    const handleMouseOut = (videoId, event) => {
        const video = event.target;
        setPlayingStates(prev => ({ ...prev, [videoId]: false }));
        if (!video.paused) {
            video.pause();
        }
    };

    return (
        <div className="video-list">
            {filteredVideos.map(video => (
                <Link to={`/video/${video.id}`} key={video.id} className="video-item">
                    <div className="video-thumbnail">
                        <video
                            src={video.videoUrl}
                            poster={video.img}
                            preload="metadata"
                            muted
                            onMouseOver={(e) => handleMouseOver(video.id, e)}
                            onMouseOut={(e) => handleMouseOut(video.id, e)}
                        >
                            Your browser does not support the video tag.
                        </video>
                    </div>
                    <div className="video-info">
                        <h4>{video.title}</h4>
                        <p>{video.author}</p>
                        <p>{video.views} - {video.when}</p>
                    </div>
                </Link>
            ))}
        </div>
    );
}

export default VideoList;