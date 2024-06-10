// ShareModal.js
import React from 'react';
import './App.css'; // Updated to import the CSS directly from App.css

const ShareModal = ({ isOpen, onClose, videoUrl }) => {
  if (!isOpen) {
    return null;
  }

  const handleCopyClick = () => {
    navigator.clipboard.writeText(videoUrl);
    alert('Copied to clipboard');
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
      <button className="close-button" onClick={onClose}>×</button>
        <div className="share-options">
          <button className="share-option">
          <i class="bi bi-whatsapp"></i>
            WhatsApp</button>
          <button className="share-option">
          <i class="bi bi-facebook"></i>
            Facebook</button>
          <button className="share-option">
          <i class="bi bi-meta"></i>
            Meta</button>
          <button className="share-option">
          <i class="bi bi-envelope-at"></i>
            Email</button>
          <button className="share-option">
          <i class="bi bi-messenger"></i>
            Messenger</button>
        </div>
        <div className="share-url">
          <input type="text" value={videoUrl} readOnly />
          <button className="copy-button" onClick={handleCopyClick}>Copy</button>
        </div>
      </div>
    </div>
  );
};

export default ShareModal;
