import React, { useEffect, useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import "./UploadModal.css";

const UploadModal = ({ show, handleClose, handleFileUpload }) => {
  const [file, setFile] = useState(null);
  const fileInputRef = React.createRef();

  const resetUpload = () => {
    setFile(null);
  };

  const onFileChange = () => {
    const selectedFile = fileInputRef.current.files[0];
    setFile(selectedFile);
    handleFileUpload(selectedFile, resetUpload);
    handleClose(); // Close modal after upload
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Upload videos</Modal.Title>
      </Modal.Header>
      <Modal.Body className="text-center">
        <div className="upload-icon">
          <i
            className="bi bi-cloud-arrow-up-fill"
            style={{ fontSize: "4rem", color: "#aaa" }}
          ></i>
        </div>
        <p>Drag and drop video files to upload</p>
        <p>Your videos will be private until you publish them.</p>
        <input
          type="file"
          ref={fileInputRef}
          onChange={onFileChange}
          style={{ display: "none" }}
        />
        <Button variant="primary" onClick={() => fileInputRef.current.click()}>
          Select files
        </Button>
      </Modal.Body>
      <Modal.Footer>
        <small>
          By submitting your videos to YouTube, you acknowledge that you agree
          to YouTube's
          <a href="https://www.youtube.com/static?template=terms">
            {" "}
            Terms of Service
          </a>{" "}
          and
          <a href="https://www.youtube.com/about/policies/#community-guidelines">
            {" "}
            Community Guidelines
          </a>
          . Please be sure not to violate others' copyright or privacy rights.
          <a href="https://support.google.com/youtube/answer/2797370">
            Learn more
          </a>
        </small>
      </Modal.Footer>
    </Modal>
  );
};

export default UploadModal;
