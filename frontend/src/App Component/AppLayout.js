import React, { useState, useEffect, useContext } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import VideoList from "../Video Components/VideoList";
import LeftMenu from "../Navigation Components/LeftMenu";
import MidMenu from "../Navigation Components/MidMenu";
import Search from "../Search Component/Search";

import useWindowWidth from "../WindoWidth";
import "./AppLayout.css";
import { VideosContext } from "../VideosContext";
import { SearchQueryContext } from "../SearchQueryContext";

function AppLayout() {
  const { videos } = useContext(VideosContext);
  const { setSearchQuery } = useContext(SearchQueryContext);
  const [bgColor, setBgColor] = useState("white");
  const windowWidth = useWindowWidth();
  const location = useLocation();
  const navigate = useNavigate();
  const isVideoDetail = location.pathname.startsWith("/video/");
  const isLargeScreen = windowWidth >= 992;
  const [isLeftMenuOpen, setIsLeftMenuOpen] = useState(isLargeScreen);

  const toggleLeftMenu = () => {
    setIsLeftMenuOpen(!isLeftMenuOpen);
  };

  const navigateToHome = () => {
    navigate("/");
  };

  return (
    <div className="container-fluid App container_full">
      <button className="sandwich-button" onClick={toggleLeftMenu}>
        <i className="bi bi-list"></i>
      </button>
      <div className={`left_section ${isLeftMenuOpen ? "open" : "closed"}`}>
        <div className="left-menu-top">
          <button className="youtube-button" onClick={navigateToHome}>
            <i className="bi bi-youtube youtube-icon"></i>
            <span className="youtube-text">YouTube</span>
          </button>
        </div>
        <div className="left-menu-content">{isLargeScreen && <LeftMenu />}</div>
      </div>
      <div
        className={`right_section ${
          isLeftMenuOpen ? "with-menu" : "full-width"
        }`}
      >
        <div
          className={`col ${
            isLargeScreen && isVideoDetail ? "main-content" : "main-content"
          }`}
        >
          <div style={{ backgroundColor: bgColor, minHeight: "100vh" }}>
            <Search onSearch={setSearchQuery} setBgColor={setBgColor} />
            <div className="row bg-white">{!isVideoDetail && <MidMenu />}</div>
            <Outlet />
          </div>
        </div>
      </div>
    </div>
  );
}

export default AppLayout;
