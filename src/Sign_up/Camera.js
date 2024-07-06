// src/Sign_up/Camera.js
import React, { useRef, useEffect } from 'react';
import './Camera.css';

const Camera = ({ onCapture, onClose }) => {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);

  const startCamera = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true });
      videoRef.current.srcObject = stream;
    } catch (error) {
      console.error("Error accessing the camera: ", error);
      alert("Error accessing the camera. Please ensure the camera is not being used by another application and try again.");
    }
  };

  const capturePhoto = () => {
    if (!videoRef.current || !canvasRef.current) return;
    const context = canvasRef.current.getContext('2d');
    context.drawImage(videoRef.current, 0, 0, canvasRef.current.width, canvasRef.current.height);
    const dataUrl = canvasRef.current.toDataURL('image/png');
    onCapture(dataUrl);
  };

  useEffect(() => {
    startCamera();
    return () => {
      if (videoRef.current && videoRef.current.srcObject) {
        videoRef.current.srcObject.getTracks().forEach(track => track.stop());
      }
    };
  }, []);

  return (
    <div className="camera">
      <video ref={videoRef} autoPlay playsInline width="100%" />
      <canvas ref={canvasRef} width="640" height="480" style={{ display: 'none' }}></canvas>
      <button className="camera-button" onClick={capturePhoto}>Capture Photo</button>
      <button className="camera-button" onClick={onClose}>Close</button>
    </div>
  );
};

export default Camera;
