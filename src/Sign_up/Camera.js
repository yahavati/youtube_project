// src/Sign_up/Camera.js
import React, { useRef } from 'react';

const Camera = ({ onCapture, onClose }) => {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);

  const startCamera = async () => {
    const stream = await navigator.mediaDevices.getUserMedia({ video: true });
    videoRef.current.srcObject = stream;
  };

  const capturePhoto = () => {
    const context = canvasRef.current.getContext('2d');
    context.drawImage(videoRef.current, 0, 0, canvasRef.current.width, canvasRef.current.height);
    const dataUrl = canvasRef.current.toDataURL('image/png');
    onCapture(dataUrl);
  };

  React.useEffect(() => {
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
      <button onClick={capturePhoto}>Capture Photo</button>
      <button onClick={onClose}>Close</button>
    </div>
  );
};

export default Camera;
