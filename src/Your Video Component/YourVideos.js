import React, { useState, useContext } from 'react';
import './YourVideos.css';
import UploadModal from '../Upload and Share Components/UploadModal';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../UserContext';

const YourVideos = () => {
    const [showUploadModal, setShowUploadModal] = useState(false);
    const navigate = useNavigate();
    const { addUploadedVideo } = useContext(UserContext);

    const handleUploadClose = () => setShowUploadModal(false);
    const handleUploadShow = () => setShowUploadModal(true);

    const handleFileUpload = (file) => {
        const newVideo = {
            id: Date.now(),
            name: file.name,
            url: URL.createObjectURL(file),
            file: file,
            description: ''
        };
        addUploadedVideo(newVideo);
        handleUploadClose();
    };

    const navigateToVideos = () => {
        navigate('/home/videos');
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
                <button className="upload-button" onClick={handleUploadShow}>Upload Videos</button>
            </div>
            <UploadModal show={showUploadModal} handleClose={handleUploadClose} handleFileUpload={handleFileUpload} />
        </div>
    );
};

export default YourVideos;
