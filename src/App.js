import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomeScreen from "./App Component/HomeScreen";
import Login from "./Log_in/Login";
import SignUp from "./Sign_up/SignUp";
import YourVideos from "./Your Video Component/YourVideos";
import { UserProvider } from "./UserContext";
import VideoDetail from "./Video Components/VideoDetail";
import { VideosProvider } from "./VideosContext";
import ProtectedRoute from "./ProtectedRoute";
import AppLayout from "./App Component/AppLayout";
import { SearchQueryProvider } from "./SearchQueryContext";
import Videos from "./Video Components/Videos";

function App() {
  return (
    <VideosProvider>
      <SearchQueryProvider>
        <UserProvider>
          <Router>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<SignUp />} />
              <Route element={<AppLayout />}>
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
              </Route>
            </Routes>
          </Router>
        </UserProvider>
      </SearchQueryProvider>
    </VideosProvider>
  );
}

export default App;
