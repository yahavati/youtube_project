import React, { useState, useEffect, useContext } from 'react'; // Add useState and useEffect imports
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
import { VideosContext } from '../VideosContext';

function HomeScreen() {
    const { videos, updateVideo } = useContext(VideosContext);
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

    const navigateToHome = () => {
        navigate('/home');
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
                            <Route path="/video/:id" element={<VideoDetail />} />
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
