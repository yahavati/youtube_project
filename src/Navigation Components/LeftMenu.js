import React, { useContext, useRef } from "react";
import { useNavigate } from "react-router-dom";
import "./LeftMenu.css";
import { UserContext } from "../UserContext";
import { NavItem } from "react-bootstrap";

function LeftMenu() {
  const menuRef = useRef(null);
  const navigate = useNavigate();
  const { authenticatedUser } = useContext(UserContext);

  const navigateToHome = () => {
    navigate("/");
  };

  const navigateToYourVideos = () => {
    navigate("/your-videos");
  };

  const menuItems = [
    { icon: "bi bi-house-door-fill", text: "Home", onClick: navigateToHome },
    { icon: "bi bi-person-video", text: "Shorts" },
    { icon: "bi bi-collection-play", text: "Subscriptions" },
    { type: "separator" },
    {
      text: "You",
      icon: "bi bi-chevron-right",
      className: "d-flex align-items-center",
    },
    { icon: "bi bi-images", text: "Your channel" },
    { icon: "bi bi-hourglass-split", text: "History" },
    { icon: "bi bi-music-note-list", text: "Playlists" },
    {
      icon: "bi bi-person-video2",
      text: "Your videos",
      onClick: navigateToYourVideos,
    },
    { icon: "bi bi-clock-history", text: "Watch later" },
    { icon: "bi bi-hand-thumbs-up", text: "Liked videos" },
    { type: "separator" },
    { type: "header", text: "Subscriptions" },
    { text: "Ido Cohen" },
    { text: "Yahav Atias" },
    { text: "Lian" },
    { type: "separator" },
    { type: "header", text: "Explore" },
    { icon: "bi bi-fire", text: "Trending" },
    { icon: "bi bi-music-note-list", text: "Music" },
    { icon: "bi bi-controller", text: "Gaming" },
    { icon: "bi bi-newspaper", text: "News" },
    { icon: "bi bi-dribbble", text: "Sports" },
    { icon: "bi bi-broadcast-pin", text: "Podcasts" },
    { type: "separator" },
    { icon: "bi bi-gear", text: "Setting" },
    { icon: "bi bi-flag", text: "Report history" },
    { icon: "bi bi-patch-question", text: "Help" },
    { icon: "bi bi-send", text: "Send feedback" },
  ];

  const scrollUp = () => {
    if (menuRef.current) {
      menuRef.current.scrollBy({ top: -100, behavior: "smooth" });
    }
  };

  const scrollDown = () => {
    if (menuRef.current) {
      menuRef.current.scrollBy({ top: 100, behavior: "smooth" });
    }
  };

  return (
    <nav className="col-3 col-lg-12 bg-light vh-100 d-flex flex-column">
      <div></div>
      <button onClick={scrollUp} className="btn white btn-light">
        ▲
      </button>
      <ul
        ref={menuRef}
        className="list-group list-group-flush flex-grow-1 overflow-auto"
      >
        {menuItems.map((item, index) => {
          if (item.text === "Your videos" && !authenticatedUser) {
            return null;
          }
          if (item.type === "separator") {
            return (
              <div key={index} className="separator-container">
                <div className="lini" />
              </div>
            );
          }
          if (item.type === "header") {
            return (
              <div key={index} className="list-group-item text-left">
                {item.text}
              </div>
            );
          }
          return (
            <li
              key={index}
              className={`list-group-item my-group-item text-left ${
                item.className || ""
              }`}
              onClick={item.onClick}
              style={item.onClick ? { cursor: "pointer" } : {}}
            >
              {item.icon && <i className={item.icon}></i>}
              <span>{item.text}</span>
            </li>
          );
        })}
      </ul>
      <button onClick={scrollDown} className="btn btn-light">
        ▼
      </button>
    </nav>
  );
}

export default LeftMenu;
