import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login';
import SignUp from './Sign_up/SignUp';
import { UserProvider } from './UserContext';

import HomeScreen from './App Component/HomeScreen';
import YourVideos from './Your Video Component/YourVideos';
import Videos from './Video Components/Videos';

function App() {
    const [uploadedVideos, setUploadedVideos] = useState([]);

    return (
        <UserProvider>
          
        <Router>
                    <Routes>
                        <Route path="/" element={<Login />} />
                        <Route path="/signup" element={<SignUp />} />
                <Route path="/home" element={<HomeScreen />} />
                <Route path="/your-videos" element={<YourVideos uploadedVideos={uploadedVideos} setUploadedVideos={setUploadedVideos} />} />
                <Route path="/videos" element={<Videos uploadedVideos={uploadedVideos} setUploadedVideos={setUploadedVideos} />} />
                      
            </Routes>
        </Router>
          
        </UserProvider>
    );
}

export default App;
