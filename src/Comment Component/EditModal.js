import React, { useState } from 'react';
import './EditModal.css';

const EditModal = ({ show, handleClose, video, handleSave }) => {
    const [title, setTitle] = useState(video ? video.title : '');
    const [description, setDescription] = useState(video ? video.description : '');
    const [thumbnail, setThumbnail] = useState(video ? video.img : '');

    const handleThumbnailChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setThumbnail(URL.createObjectURL(file));
            video.thumbnailFile = file; // Save the file for later use
        }
    };

    const handleSubmit = () => {
        handleSave(video.id, title, description, thumbnail, video.thumbnailFile);
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
                    {thumbnail && <img src={thumbnail} alt="Thumbnail" className="thumbnail-preview" />}
                </div>
                <button className="save-button" onClick={handleSubmit}>Save</button>
                <button className="cancel-button" onClick={handleClose}>Cancel</button>
            </div>
        </div>
    );
};

export default EditModal;
