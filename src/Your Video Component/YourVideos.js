import React, { useState } from 'react';
import './YourVideos.css';
import UploadModal from '../Upload and Share Components/UploadModal';
import EditModal from '../Comment Component/EditModal';  // Adjust this if EditModal is in a different directory
import { useNavigate } from 'react-router-dom';

const YourVideos = ({ setUploadedVideos }) => {
    const [showUploadModal, setShowUploadModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [selectedVideo, setSelectedVideo] = useState(null);
    const navigate = useNavigate();

    const handleUploadClose = () => setShowUploadModal(false);
    const handleUploadShow = () => setShowUploadModal(true);

    const handleEditClose = () => setShowEditModal(false);
    const handleEditShow = (video) => {
        setSelectedVideo(video);
        setShowEditModal(true);
    };

    const handleFileUpload = (file) => {
        const newVideo = {
            id: Date.now(),
            name: file.name,
            url: URL.createObjectURL(file),
            file: file,
            description: ''
        };
        setUploadedVideos(prevVideos => [...prevVideos, newVideo]);
        handleUploadClose();
    };

    const handleSave = (id, title, description) => {
        setUploadedVideos(prevVideos => prevVideos.map(video =>
            video.id === id ? { ...video, name: title, description } : video
        ));
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
            <EditModal show={showEditModal} handleClose={handleEditClose} video={selectedVideo} handleSave={handleSave} />
        </div>
    );
};

export default YourVideos;
