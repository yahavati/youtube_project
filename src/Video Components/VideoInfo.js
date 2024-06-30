// VideoInfo.js
import React, {useState} from 'react';
import ShareModal from '../Upload and Share Components/ShareModal';
import './VideoInfo.css'

function VideoInfo({video}) {
    const [liked, setLiked] = useState(false);
    const [disliked, setDisliked] = useState(false);
    const [likes, setLikes] = useState(0);
    const [dislikes, setDislikes] = useState(0);
    const [isFireActive, setIsFireActive] = useState(false);
    const [isCryActive, setIsCryActive] = useState(false);
    const [subscribed, setSubscribed] = useState(false);
    const [isShareModalOpen, setIsShareModalOpen] = useState(false);
    const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);
    video.description = " Description Description Description Description Description Description Description Description Description Description Description Description" +
        " Description Description Description Description Description Description Description Description Description Description Description Description";

    const toggleDescription = () => {
        setIsDescriptionExpanded(!isDescriptionExpanded);
    };

    const truncateDescription = (text, maxLength) => {
        if (text.length <= maxLength) return text;
        return text.slice(0, maxLength) + '...';
    };

    const handleLikeClick = () => {
        setLiked(!liked);
        setDisliked(false);
        setLikes(liked ? likes - 1 : likes + 1);
        if (disliked) setDislikes(dislikes - 1);
        setIsFireActive(true);
        setTimeout(() => setIsFireActive(false), 2000);
    };

    const handleDislikeClick = () => {
        setDisliked(!disliked);
        setLiked(false);
        setDislikes(disliked ? dislikes - 1 : dislikes + 1);
        if (liked) setLikes(likes - 1);
        setIsCryActive(true);
        setTimeout(() => setIsCryActive(false), 2000);
    };

    const handleSubscribeClick = () => {
        setSubscribed(!subscribed);
    };

    const handleShareClick = () => {
        setIsShareModalOpen(true);
    };

    return (
        <div className="video-info">
            {isFireActive && <div className="fire-overlay active">🔥🔥🔥🔥🔥🔥🔥</div>}
            {isCryActive && <div className="cry-overlay active">😭😭😭😭😭😭😭</div>}
            <h2 className="video-title">{video.title}</h2>
            <div className="video-details">
                <div className="video-metadata">
                    <div><strong>{video.author.toUpperCase()}</strong></div>
                </div>
                <div className="video-actions">
                    <div className={`my-button btn-container ${disliked ? 'active' : ''}`} onClick={handleDislikeClick}>
                        <button className="btn btn-dislike">
                            <i className="bi bi-hand-thumbs-down"></i>
                        </button>
                        <span>{dislikes}</span>
                    </div>
                    <div className={`my-button btn-container ${liked ? 'active' : ''}`} onClick={handleLikeClick}>
                        <button className="btn btn-like">
                            <i className="bi bi-hand-thumbs-up"></i>
                        </button>
                        <span>{likes}</span>
                    </div>
                    <div className="my-button btn-container" onClick={handleShareClick}>
                        <button className="btn btn-share">
                            <i className="bi bi-share"></i>
                        </button>
                    </div>
                    <div className={`my-button btn-container ${subscribed ? 'active' : ''}`}
                         onClick={handleSubscribeClick}>
                        <button className="btn btn-subscribe">
                            {subscribed ? <i className="bi bi-check"></i> : <i className="bi bi-bell"></i>}
                        </button>
                    </div>
                </div>
            </div>
            <div className="description-container">
                <div className="upper-container">
                    <div><strong>Views:</strong> {video.views} - {video.when}</div>
                    <div className="creation-date"></div>
                </div>
                <div className="description">
                    {isDescriptionExpanded
                        ? video.description
                        : truncateDescription(video.description, 200)}
                    {video.description.length > 200 && (
                        <button onClick={toggleDescription} className="expand-button">
                            {isDescriptionExpanded ? 'Show less' : 'Click for more'}
                        </button>
                    )}
                </div>
            </div>
            <ShareModal
                isOpen={isShareModalOpen}
                onClose={() => setIsShareModalOpen(false)}
                videoUrl={video.videoUrl}
            />
        </div>
    );
}

export default VideoInfo;
