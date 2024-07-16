import React, { useState, useContext } from "react";
import "./YourVideos.css";
import UploadModal from "../Upload and Share Components/UploadModal";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../UserContext";
import { VideosContext } from "../VideosContext";
import { createUserVideo } from "../api/user"; // Import the API function

const YourVideos = () => {
  const [showUploadModal, setShowUploadModal] = useState(false);
  const { user } = useContext(UserContext);
  const { addVideo } = useContext(VideosContext);
  const navigate = useNavigate();

  const handleUploadClose = () => setShowUploadModal(false);
  const handleUploadShow = () => setShowUploadModal(true);

  const handleFileUpload = async (file, resetUpload) => {
    const formData = new FormData();
    formData.append("title", file.name);
    formData.append("description", "");
    formData.append("url", file); // Assuming the file itself is uploaded as the video URL
    try {
      const newVideo = await createUserVideo(user._id, formData); // Use the API function to create a video
      addVideo(newVideo); // Update the context with the new video
      resetUpload();
    } catch (error) {
      console.error("Error uploading video:", error);
    }
  };

  const navigateToVideos = () => {
    navigate("/videos");
  };

  return (
    <div className="channel-content">
      <div className="content-header">
        <h2>Your Videos</h2>
        <button className="view-videos-button" onClick={navigateToVideos}>
          View All Videos
        </button>
      </div>
      <div className="no-content">
        <img
          src="/myvideoesicon.png"
          alt="No content available"
          className="no-content-image"
        />
        <p>No content available</p>
      </div>
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
