import React, { useState } from 'react';
import './EditModal.css'; // Ensure this file exists in the same directory

const EditModal = ({ show, handleClose, video, handleSave }) => {
  const [title, setTitle] = useState(video ? video.name : '');
  const [description, setDescription] = useState(video ? video.description : '');

  const handleTitleChange = (e) => setTitle(e.target.value);
  const handleDescriptionChange = (e) => setDescription(e.target.value);

  const handleSubmit = () => {
    handleSave(video.id, title, description);
  };

  if (!show) {
    return null;
  }

  return (
    <div className="modal">
      <div className="modal-content">
        <span className="close" onClick={handleClose}>&times;</span>
        <h2>Edit Video</h2>
        <form>
          <div className="form-group">
            <label>Title</label>
            <input type="text" value={title} onChange={handleTitleChange} />
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea value={description} onChange={handleDescriptionChange} />
          </div>
          <button type="button" onClick={handleSubmit}>Save</button>
        </form>
      </div>
    </div>
  );
};

export default EditModal;
