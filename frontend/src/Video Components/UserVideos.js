import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getUserVideos } from "../api/user";
import { getMediaSource } from "../utils/mediaUtils";
import "./UserVideos.css";

const UserVideos = () => {
  const [playing, setPlaying] = useState(false);
  const { id } = useParams();
  const navigate = useNavigate();
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [user, setUser] = useState({});

  const handleMouseOver = (event) => {
    const videoElement = event.target;
    if (videoElement.readyState >= 2) {
      setPlaying(true);
      videoElement.play().catch((error) => {
        if (error.name !== "AbortError") {
          console.error("Playback failed:", error);
        }
      });
    }
  };

  const handleMouseOut = (event) => {
    const videoElement = event.target;
    setPlaying(false);
    if (!videoElement.paused) {
      videoElement.pause();
    }
  };

  useEffect(() => {
    const fetchUserVideos = async () => {
      try {
        const response = await getUserVideos(id);
        console.log(response);
        setVideos(response.videos);
        setUser(response.user);
      } catch (err) {
        setError(err.message || "Error fetching user videos");
      } finally {
        setLoading(false);
      }
    };

    fetchUserVideos();
  }, [id]);

  const handleVideoClick = (videoId) => {
    navigate(`/video/${videoId}`);
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="user-videos-container">
      <h2>{user.displayName} Videos</h2>
      <div className="user-videos-grid">
        {videos.map((video) => (
          <div
            key={video._id}
            className="user-video-card"
            onClick={() => handleVideoClick(video._id)}
          >
            <div className="user-video-thumbnail">
              <video
                src={getMediaSource(video.url)}
                poster={video.thumbnail}
                preload="metadata"
                muted
                onMouseOver={handleMouseOver}
                onMouseOut={handleMouseOut}
              >
                Your browser does not support the video tag.
              </video>
            </div>
            <div className="user-video-info">
              <h5>{video.title}</h5>
              <p>{video.description}</p>
              <p>Views: {video.views}</p>
              <p>Likes: {video.likes}</p>
              <p>Dislikes: {video.dislikes}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default UserVideos;
