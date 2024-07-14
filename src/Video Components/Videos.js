import React, { useState, useContext } from "react";
import "./Videos.css";
import EditModal from "../Comment Component/EditModal";
import { UserContext } from "../UserContext";
import { VideosContext } from "../VideosContext";

const Videos = () => {
  const { authenticatedUser, updateUploadedVideo, deleteUploadedVideo } =
    useContext(UserContext);
  const { updateVideo, deleteVideo } = useContext(VideosContext);
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

  const handleSaveEdit = (id, newTitle, newDescription, newThumbnail, thumbnailFile) => {
    const updatedVideo = {
      ...currentVideo,
      title: newTitle,
      description: newDescription,
      img: newThumbnail,
      thumbnailFile: thumbnailFile,
    };
    updateUploadedVideo(id, updatedVideo);
    updateVideo(id, updatedVideo);
    handleCloseEditModal();
  };

  const handleDelete = (id) => {
    deleteUploadedVideo(id);
    deleteVideo(id);
  };

  return (
    <div className="videos-list">
      {authenticatedUser?.videos.map((video) => (
        <div className="videos-item" key={video.id}>
          <video width="200" controls>
            <source src={video.videoUrl} type="video/mp4" />
            Your browser does not support the video tag.
          </video>
          <div style={{ width: "100%" }}>
            <h5>{video.title}</h5>
            <p>{video.description}</p>
          </div>
          <button
            className="edit-button"
            onClick={() => handleEditClick(video)}
          >
            Edit
          </button>
          <button
            className="delete-button"
            onClick={() => handleDelete(video.id)}
          >
            Delete
          </button>
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
