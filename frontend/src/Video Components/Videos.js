import React, { useState, useEffect, useContext } from "react";
import "./Videos.css";
import EditModal from "../Comment Component/EditModal";
import { UserContext } from "../UserContext";
import { getUserVideos, updateUserVideo, deleteUserVideo } from "../api/user"; // Import API functions
import { VideosContext } from "../VideosContext";
import { getMediaSource } from "../utils/mediaUtils";

const Videos = () => {
  const { user } = useContext(UserContext);
  const { updateVideo } = useContext(VideosContext);
  const [videos, setVideos] = useState([]);
  const [showEditModal, setShowEditModal] = useState(false);
  const [currentVideo, setCurrentVideo] = useState(null);

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const fetchedVideos = await getUserVideos(user._id);
        setVideos(fetchedVideos.videos);
      } catch (error) {
        console.error("Error fetching videos:", error);
      }
    };

    if (user) {
      fetchVideos();
    }
  }, [user]);

  const handleEditClick = (video) => {
    setCurrentVideo(video);
    setShowEditModal(true);
  };

  const handleCloseEditModal = () => {
    setShowEditModal(false);
    setCurrentVideo(null);
  };

  const handleSaveEdit = async (id, newTitle, newDescription, thumbnail) => {
    try {
      const updatedVideo = await updateUserVideo(user._id, id, {
        title: newTitle,
        description: newDescription,
        thumbnail,
      });

      let base64Thumbnail = "";

      if (thumbnail) {
        base64Thumbnail = await convertFileToBase64(thumbnail);
      }

      setVideos(
        videos.map((video) => (video._id === id ? updatedVideo : video))
      );
      updateVideo(id, {
        title: newTitle,
        description: newDescription,
        thumbnail: base64Thumbnail,
      });
      handleCloseEditModal();
    } catch (error) {
      console.error("Error updating video:", error);
    }
  };

  const convertFileToBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  };

  const handleDelete = async (id) => {
    try {
      await deleteUserVideo(user._id, id);
      setVideos(videos.filter((video) => video._id !== id));
    } catch (error) {
      console.error("Error deleting video:", error);
    }
  };

  return (
    <div className="videos-list">
      {videos.map((video) => (
        <div className="videos-item" key={video._id}>
          <video
            poster={video.thumbnail && getMediaSource(video.thumbnail)}
            width="200"
            controls
          >
            <source src={getMediaSource(video.url)} type="video/mp4" />
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
            onClick={() => handleDelete(video._id)}
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
