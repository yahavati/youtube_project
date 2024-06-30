import React, { useState } from 'react';
import './Videos.css';
import EditModal from '../Comment Component/EditModal'; // Correct import path based on the structure shown

const Videos = ({ uploadedVideos, setUploadedVideos }) => {
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
    const updatedVideos = uploadedVideos.map(video => {
      if (video.id === id) {
        return { ...video, name: newName, description: newDescription };
      }
      return video;
    });
    setUploadedVideos(updatedVideos);
    handleCloseEditModal();
  };

  const handleDelete = (id) => {
    const updatedVideos = uploadedVideos.filter(video => video.id !== id);
    setUploadedVideos(updatedVideos);
  };

  return (
    <div className="videos-page">
      <h2>Videos</h2>
      <div className="videos-list">
        {uploadedVideos.map(video => (
          <div className="video-item" key={video.id}>
            <video width="200" controls>
              <source src={video.url} type="video/mp4" />
              Your browser does not support the video tag.
            </video>
            <p>{video.name}</p>
            <p>{video.description}</p>
            <button className="edit-button" onClick={() => handleEditClick(video)}>Edit</button>
            <button className="delete-button" onClick={() => handleDelete(video.id)}>Delete</button>
          </div>
        ))}
      </div>
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
