import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import './UploadModal.css';

const UploadModal = ({ show, handleClose, handleFileUpload }) => {
  const fileInputRef = React.createRef();

  const onFileChange = () => {
    const file = fileInputRef.current.files[0];
    handleFileUpload(file);
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Upload videos</Modal.Title>
      </Modal.Header>
      <Modal.Body className="text-center">
        <div className="upload-icon">
          <i className="bi bi-cloud-arrow-up-fill" style={{ fontSize: '4rem', color: '#aaa' }}></i>
        </div>
        <p>Drag and drop video files to upload</p>
        <p>Your videos will be private until you publish them.</p>
        <input type="file" ref={fileInputRef} onChange={onFileChange} />
        <Button variant="primary" onClick={() => fileInputRef.current.click()}>SELECT FILES</Button>
      </Modal.Body>
      <Modal.Footer>
        <small>
          By submitting your videos to YouTube, you acknowledge that you agree to YouTube's 
          <a href="https://www.youtube.com/static?template=terms"> Terms of Service</a> and 
          <a href="https://www.youtube.com/about/policies/#community-guidelines"> Community Guidelines</a>.
          Please be sure not to violate others' copyright or privacy rights. 
          <a href="https://support.google.com/youtube/answer/2797370">Learn more</a>
        </small>
      </Modal.Footer>
    </Modal>
  );
};

export default UploadModal;
