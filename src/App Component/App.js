import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomeScreen from './HomeScreen';
import YourVideos from './YourVideos';
import Videos from './Videos';

function App() {
    const [uploadedVideos, setUploadedVideos] = useState([]);

    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomeScreen />} />
                <Route path="/your-videos" element={<YourVideos uploadedVideos={uploadedVideos} setUploadedVideos={setUploadedVideos} />} />
                <Route path="/videos" element={<Videos uploadedVideos={uploadedVideos} setUploadedVideos={setUploadedVideos} />} />
            </Routes>
        </Router>
    );
}

export default App;
