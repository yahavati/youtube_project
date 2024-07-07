import React, { useState, useContext } from 'react';
import './Videos.css';
import EditModal from '../Comment Component/EditModal';
import { UserContext } from '../UserContext';

const Videos = () => {
    const { authenticatedUser, updateUploadedVideo, deleteUploadedVideo } = useContext(UserContext);
    const [showEditModal, setShowEditModal] = useState(false);
    const [currentVideo, setCurrentVideo] = useState(null);

    const handleEditClick = (video) => {
        setCurrentVideo(video);
        setShowEditModal(true);
    };

    const handleCloseEditModal = () => {
        setShowEditModal(false);
        setCurrentVideo(null);
    };

    const handleSaveEdit = (id, newName, newDescription) => {
        updateUploadedVideo(id, { ...currentVideo, name: newName, description: newDescription });
        handleCloseEditModal();
    };

    return (
        <div className="videos-list">
            {authenticatedUser?.videos.map(video => (
                <div className="video-item" key={video.id}>
                    <video width="200" controls>
                        <source src={video.url} type="video/mp4" />
                        Your browser does not support the video tag.
                    </video>
                    <p>{video.name}</p>
                    <p>{video.description}</p>
                    <button className="edit-button" onClick={() => handleEditClick(video)}>Edit</button>
                    <button className="delete-button" onClick={() => deleteUploadedVideo(video.id)}>Delete</button>
                </div>
            ))}
            {currentVideo && (
                <EditModal
                    show={showEditModal}
                    handleClose={handleCloseEditModal}
                    video={currentVideo}
                    handleSave={handleSaveEdit}
                />
            )}
        </div>
    );
};

export default Videos;
