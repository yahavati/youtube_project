import React from 'react';
import { Link } from 'react-router-dom';
import './VideoList.css';

function VideoList({ videos, searchQuery }) {
    const filteredVideos = videos.filter(video =>
        video.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        video.author.toLowerCase().includes(searchQuery.toLowerCase())
    );

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
                            onMouseOver={event => event.target.play()}
                            onMouseOut={event => event.target.pause()}
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
