import React, { useState, useEffect } from 'react';
import { useLocation, Routes, Route, useNavigate } from 'react-router-dom';
import VideoList from './VideoList';
import VideoDetail from './VideoDetail';
import LeftMenu from './LeftMenu';
import MidMenu from './MidMenu';
import Search from './Search';
import WindowWidth from './WindoWidth';
import './App.css';

function App() {
  const [videos, setVideos] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [bgColor, setBgColor] = useState('white');
  const windowWidth = WindowWidth();
  const location = useLocation();
  const navigate = useNavigate();
  const isVideoDetail = location.pathname.startsWith('/video/');
  const isLargeScreen = windowWidth >= 992;

  useEffect(() => {
    fetch('/videos.json')
      .then(response => response.json())
      .then(data => setVideos(data))
      .catch(error => console.error('Error fetching videos:', error));
  }, []);

  const navigateToHome = () => {
    navigate('/');
  };

  return (
    <div className="container-fluid App">
      <div className="row">
        <div className="col-12">
          <button className='youtube-button' onClick={navigateToHome}>
            <i className="bi bi-youtube youtube-icon"></i>
            <span className='youtube-text'>youtube</span>
          </button>
        </div>
      </div>
      <div className="row">
        {isLargeScreen && (
          <div className="col-xl-2 col-lg-3">
            <LeftMenu />
          </div>
        )}
        <div className={`col ${isLargeScreen && isVideoDetail ? 'col-lg-9' : 'main-content'}`}>
          <div style={{ backgroundColor: bgColor, minHeight: '100vh' }}>
            <Search onSearch={setSearchQuery} setBgColor={setBgColor} />
            <div className="row bg-white">
              {!isVideoDetail && <MidMenu />}
            </div>
            <Routes>
              <Route path="/" element={<VideoList videos={videos} searchQuery={searchQuery} />} />
              <Route path="/video/:id" element={<VideoDetail videos={videos} />} />
            </Routes>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
