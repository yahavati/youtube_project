import React, { useState } from 'react';
import './YourVideos.css';
import UploadModal from '../Upload and Share Components/UploadModal';
import { useNavigate } from 'react-router-dom';

const YourVideos = ({ setUploadedVideos }) => {
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const handleClose = () => setShowModal(false);
  const handleShow = () => setShowModal(true);

  const handleFileUpload = (file) => {
    const newVideo = {
      id: Date.now(),
      name: file.name,
      url: URL.createObjectURL(file),
      file: file,
      description: ''
    };
    setUploadedVideos(prevVideos => [...prevVideos, newVideo]);
    handleClose();
  };

  const navigateToVideos = () => {
    navigate('/videos');
  };

  return (
    <div className="channel-content">
      <div className="content-header">
        <h2>Your Videos</h2>
        <button className="view-videos-button" onClick={navigateToVideos}>View All Videos</button>
      </div>
      <div className="no-content">
        <img src="/myvideoesicon.png" alt="No content available" className="no-content-image" />
        <p>No content available</p>
        <button className="upload-button" onClick={handleShow}>Upload Videos</button>
      </div>
      <UploadModal show={showModal} handleClose={handleClose} handleFileUpload={handleFileUpload} />
    </div>
  );
};

export default YourVideos;
