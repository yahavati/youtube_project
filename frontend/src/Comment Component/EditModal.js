import React, { useState, useEffect } from "react";
import "./EditModal.css";

const EditModal = ({ show, handleClose, video, handleSave }) => {
  const [title, setTitle] = useState(video ? video.title : "");
  const [description, setDescription] = useState(
    video ? video.description : ""
  );
  const [thumbnail, setThumbnail] = useState(video ? video.thumbnail : null);
  const [thumbnailPreview, setThumbnailPreview] = useState(
    video ? video.thumbnail : null
  );

  useEffect(() => {
    if (video) {
      setTitle(video.title);
      setDescription(video.description);
      setThumbnail(video.thumbnail);
      setThumbnailPreview(video.thumbnail);
    }
  }, [video]);

  const handleThumbnailChange = (e) => {
    const file = e.target.files[0];
    setThumbnail(file);

    const reader = new FileReader();
    reader.onloadend = () => {
      setThumbnailPreview(reader.result);
    };
    reader.readAsDataURL(file);
  };

  const handleSubmit = () => {
    handleSave(video._id, title, description, thumbnail);
    handleClose();
  };

  if (!show) return null;

  return (
    <div className="edit-modal">
      <div className="edit-modal-content">
        <h2>Edit Video Details</h2>
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        <div className="form-group">
          <label htmlFor="thumbnail">Thumbnail</label>
          <input
            type="file"
            id="thumbnail"
            accept="image/*"
            onChange={handleThumbnailChange}
          />
          {thumbnailPreview && (
            <img
              src={thumbnailPreview}
              alt="Thumbnail Preview"
              className="thumbnail-preview"
            />
          )}
        </div>
        <button className="save-button" onClick={handleSubmit}>
          Save
        </button>
        <button className="cancel-button" onClick={handleClose}>
          Cancel
        </button>
      </div>
    </div>
  );
};

export default EditModal;
