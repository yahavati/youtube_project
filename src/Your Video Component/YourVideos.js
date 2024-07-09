import React, { useState, useContext } from "react";
import "./YourVideos.css";
import UploadModal from "../Upload and Share Components/UploadModal";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../UserContext";
import { VideosContext } from "../VideosContext";
import VideoItem from "../Video Components/VideoItem";

const YourVideos = () => {
  const [showUploadModal, setShowUploadModal] = useState(false);
  const { addUploadedVideo } = useContext(UserContext);
  const { videos, addVideo } = useContext(VideosContext);
  const { authenticatedUser } = useContext(UserContext);
  const navigate = useNavigate();

  const userVideos = videos.filter(
    (video) => video.author === authenticatedUser.username
  );

  const handleUploadClose = () => setShowUploadModal(false);
  const handleUploadShow = () => setShowUploadModal(true);

  const handleFileUpload = (file, title, description, resetUpload) => {
    const newVideo = {
      id: Date.now(),
      author: authenticatedUser.username,
      videoUrl: URL.createObjectURL(file),
      file: file,
      title: title,
      description: description,
      likes: 0,
      dislikes: 0,
      views: 0,
      comments: [],
    };
    addUploadedVideo(newVideo);
    addVideo(newVideo);
    resetUpload();
  };

  const navigateToVideos = () => {
    navigate("/");
  };

  return (
    <div className="channel-content">
      <div className="content-header">
        <h2>Your Videos</h2>
        <button className="view-videos-button" onClick={navigateToVideos}>
          View All Videos
        </button>
      </div>
      {userVideos.length === 0 ? (
        <div className="no-content">
          <img
            src="/myvideoesicon.png"
            alt="No content available"
            className="no-content-image"
          />
          <p>No content available</p>
        </div>
      ) : (
        <div className="videos-list-your">
          {userVideos.map((video) => (
            <VideoItem key={video.id} video={video} myVideo={true} />
          ))}
        </div>
      )}
      <UploadModal
        show={showUploadModal}
        handleClose={handleUploadClose}
        handleFileUpload={handleFileUpload}
      />
      <button className="upload-button" onClick={handleUploadShow}>
        Upload Videos
      </button>
    </div>
  );
};

export default YourVideos;
