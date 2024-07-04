import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login';
import SignUp from './Sign_up/SignUp';
import { UserProvider } from './UserContext';

const appStyle = {
    textAlign: 'center',
};

function App() {
    return (
        <UserProvider>
            <div style={appStyle}>
                <Router>
                    <Routes>
                        <Route path="/" element={<Login />} />
                        <Route path="/signup" element={<SignUp />} />
                    </Routes>
                </Router>
            </div>
        </UserProvider>
    );
}

export default App;
