import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomeScreen from "./App Component/HomeScreen";
import Login from "./Login";
import SignUp from "./Sign_up/SignUp";
import YourVideos from "./Your Video Component/YourVideos";
import Videos from "./Video Components/Videos";
import { UserProvider } from "./UserContext";
import VideoDetail from "./Video Components/VideoDetail";
import { VideosProvider } from "./VideosContext";
import ProtectedRoute from "./ProtectedRoute";

function App() {
  return (
    <VideosProvider>
      <UserProvider>
        <Router>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/" element={<HomeScreen />} />
            <Route path="/video/:id" element={<VideoDetail />} />
            <Route
              path="/your-videos"
              element={
                <ProtectedRoute>
                  <YourVideos />
                </ProtectedRoute>
              }
            />
            <Route path="/videos" element={<Videos />} />
          </Routes>
        </Router>
      </UserProvider>
    </VideosProvider>
  );
}

export default App;
