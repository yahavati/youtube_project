import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import "./UploadModal.css";

const UploadModal = ({ show, handleClose, handleFileUpload }) => {
  const [file, setFile] = useState(null);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const fileInputRef = React.createRef();

  const onFileChange = () => {
    const selectedFile = fileInputRef.current.files[0];
    setFile(selectedFile);
  };

  const handleTitleChange = (e) => setTitle(e.target.value);
  const handleDescriptionChange = (e) => setDescription(e.target.value);

  const resetUpload = () => {
    setFile(null);
    setTitle("");
    setDescription("");
  };

  const handleUpload = () => {
    handleFileUpload(file, title, description, resetUpload);
    handleClose(); // Close modal after upload
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Upload videos</Modal.Title>
      </Modal.Header>
      <Modal.Body className="text-center">
        {!file && (
          <>
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
            <Button
              variant="primary"
              onClick={() => fileInputRef.current.click()}
            >
              Select files
            </Button>
          </>
        )}
        {file && (
          <>
            <Form.Group controlId="videoTitle">
              <Form.Label>Title</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter video title"
                value={title}
                onChange={handleTitleChange}
              />
            </Form.Group>
            <Form.Group controlId="videoDescription" className="mt-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="Enter video description"
                value={description}
                onChange={handleDescriptionChange}
              />
            </Form.Group>
          </>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleUpload}>
          Upload
        </Button>
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
