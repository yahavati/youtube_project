import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomeScreen from "./App Component/HomeScreen";
import Login from "./Log_in/Login";
import SignUp from "./Sign_up/SignUp";
import YourVideos from "./Your Video Component/YourVideos";
import VideoDetail from "./Video Components/VideoDetail";
import { VideosProvider } from "./VideosContext";
import ProtectedRoute from "./ProtectedRoute";
import AppLayout from "./App Component/AppLayout";
import { SearchQueryProvider } from "./SearchQueryContext";
import Videos from "./Video Components/Videos";
import UserVideos from "./Video Components/UserVideos";
import { DarkModeProvider } from "./DarkModeContext";
import Settings from "./User Components/Settings";

function App() {
  return (
    <DarkModeProvider>
      <VideosProvider>
        <SearchQueryProvider>
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
                <Route path="/videos/:id" element={<UserVideos />} />
                <Route path="/settings" element={<Settings />} />
              </Route>
            </Routes>
          </Router>
        </SearchQueryProvider>
      </VideosProvider>
    </DarkModeProvider>
  );
}

export default App;
