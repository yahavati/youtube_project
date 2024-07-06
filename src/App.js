import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login';
import SignUp from './Sign_up/SignUp';
import { UserProvider } from './UserContext';
import { VideosProvider } from './VideosContext'; // Import VideosProvider
import HomeScreen from './App Component/HomeScreen';

function App() {
    return (
        <UserProvider>
            <VideosProvider> 
                <Router>
                    <Routes>
                        <Route path="/" element={<Login />} />
                        <Route path="/signup" element={<SignUp />} />
                        <Route path="/home/*" element={<HomeScreen />} />
                    </Routes>
                </Router>
            </VideosProvider>
        </UserProvider>
    );
}

export default App;
