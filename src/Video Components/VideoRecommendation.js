import React from 'react';
import { Link } from 'react-router-dom';
import './VideoRecommendation.css';

function VideoRecommendation({ id, title, author, views, when, videoUrl }) {
    return (
        <Link to={`/video/${id}`} className="video-recommendation-item">
            <div className="video-thumbnail">
                <video
                    src={videoUrl}
                    preload="metadata"
                    muted
                    onMouseOver={event => event.target.play()}
                    onMouseOut={event => event.target.pause()}
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
