import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import React, { useState, useEffect } from 'react';
import { useLocation, Routes, Route, useNavigate } from 'react-router-dom';
import VideoList from './VideoList';
import VideoDetail from './VideoDetail';
import LeftMenu from './LeftMenu';
import MidMenu from './MidMenu';
import Search from './Search';
import YourVideos from './YourVideos';
import Videos from './Videos';
import UploadModal from './UploadModal';
import ShareModal from './ShareModal';
import useWindowWidth from './WindoWidth';
import './App.css';

function App() {
  const [videos, setVideos] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [bgColor, setBgColor] = useState('white');
  const [uploadedVideos, setUploadedVideos] = useState([]);
  const windowWidth = useWindowWidth();
  const location = useLocation();
  const navigate = useNavigate();
  const isVideoDetail = location.pathname.startsWith('/video/');
  const isLargeScreen = windowWidth >= 992;
  const [isLeftMenuOpen, setIsLeftMenuOpen] = useState(isLargeScreen);

  const toggleLeftMenu = () => {
    setIsLeftMenuOpen(!isLeftMenuOpen);
  };

  useEffect(() => {
    if (!isLargeScreen) {
      setIsLeftMenuOpen(false);
    } else {
      setIsLeftMenuOpen(true);
    }
  }, [isLargeScreen]);

  useEffect(() => {
    fetch(`${process.env.PUBLIC_URL}/videos.json`)
      .then(response => response.json())
      .then(data => {
        console.log("Fetched videos:", data);
        setVideos(data);
      })
      .catch(error => console.error('Error fetching videos:', error));
  }, []);

  const navigateToHome = () => {
    navigate('/');
  };

  return (
    <div className="container-fluid App container_full">
      <button className='sandwich-button' onClick={toggleLeftMenu}>
        <i className="bi bi-list"></i>
      </button>
      <div className={`left_section ${isLeftMenuOpen ? 'open' : 'closed'}`}>
        <div className="left-menu-top">
          <button className='youtube-button' onClick={navigateToHome}>
            <i className="bi bi-youtube youtube-icon"></i>
            <span className='youtube-text'>youtube</span>
          </button>
        </div>
        <div className="left-menu-content">
          {isLargeScreen && (
            <LeftMenu />
          )}
        </div>
      </div>
      <div className={`right_section ${isLeftMenuOpen ? 'with-menu' : 'full-width'}`}>
        <div className={`col ${isLargeScreen && isVideoDetail ? 'main-content' : 'main-content'}`}>
          <div style={{ backgroundColor: bgColor, minHeight: '100vh' }}>
            <Search onSearch={setSearchQuery} setBgColor={setBgColor} />
            <div className="row bg-white">
              {!isVideoDetail && <MidMenu />}
            </div>
            <Routes>
              <Route path="/" element={<VideoList videos={videos} searchQuery={searchQuery} />} />
              <Route path="/video/:id" element={<VideoDetail videos={videos} />} />
              <Route path="/your-videos" element={<YourVideos setUploadedVideos={setUploadedVideos} />} />
              <Route path="/videos" element={<Videos uploadedVideos={uploadedVideos} setUploadedVideos={setUploadedVideos} />} />
            </Routes>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;