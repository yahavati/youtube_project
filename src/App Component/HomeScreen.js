
import React, { useState, useEffect } from 'react';
import { useLocation, Routes, Route, useNavigate } from 'react-router-dom';
import VideoList from '../Video Components/VideoList';
import VideoDetail from '../Video Components/VideoDetail';
import LeftMenu from '../Navigation Components/LeftMenu';
import MidMenu from '../Navigation Components/MidMenu';
import Search from '../Search Component/Search';
import YourVideos from '../Your Video Component/YourVideos';
import Videos from '../Video Components/Videos';
import UploadModal from '../Upload and Share Components/UploadModal';
import ShareModal from '../Upload and Share Components/ShareModal';
import useWindowWidth from '../WindoWidth';
import './HomeScreen.css';
import VideoRecommendation from '../Video Components/VideoRecommendation';


function HomeScreen() {
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
                const initializedVideos = data.map(video => ({
                    ...video,
                    likes: video.likes || 0,
                    dislikes: video.dislikes || 0,
                    comments: video.comments || []
                }));
                setVideos(initializedVideos);
            })
            .catch(error => console.error('Error fetching videos:', error));
    }, []);

    const navigateToHome = () => {
        navigate('/');
    };

    const handleVideoUpdate = (id, updates) => {
        setVideos(prevVideos =>
            prevVideos.map(video =>
                video.id === id ? { ...video, ...updates } : video
            )
        );
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
                    {isLargeScreen && <LeftMenu />}
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
                            <Route path="/video/:id" element={<VideoDetail videos={videos} onVideoUpdate={handleVideoUpdate} />} />
                            <Route path="/your-videos" element={<YourVideos setUploadedVideos={setUploadedVideos} />} />
                            <Route path="/videos" element={<Videos uploadedVideos={uploadedVideos} setUploadedVideos={setUploadedVideos} />} />
                        </Routes>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomeScreen;
