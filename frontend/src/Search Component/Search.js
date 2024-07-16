import React, { useContext, useState, useEffect, useRef } from "react";
import "./Search.css"; // Ensure this file exists in the same directory as Search.js
import { UserContext } from "../UserContext";
import { Link, useNavigate } from "react-router-dom";
import DarkModeToggle from "../App Component/DarkModeToggle";
import { getMediaSource } from "../utils/mediaUtils";

function Search({ onSearch, setBgColor }) {
  const [query, setQuery] = useState("");
  const [showSearchInput, setShowSearchInput] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false); // State for controlling user menu
  const { user, logoutUser } = useContext(UserContext);
  const menuRef = useRef(null);

  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
    }
  }, [user]);

  const handleInputChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    onSearch(value);
  };

  const handleSearchButtonClick = () => {
    setShowSearchInput(!showSearchInput);
  };

  const handleColorChange = (color) => {
    setBgColor(color);
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const handleClickOutside = (event) => {
    if (menuRef.current && !menuRef.current.contains(event.target)) {
      setIsMenuOpen(false);
    }
  };

  const handleLogout = () => {
    logoutUser();
    setIsMenuOpen(false);
  };

  useEffect(() => {
    if (isMenuOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
    }
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isMenuOpen]);

  return (
    <div
      className={`my-search-container search-container ${
        showSearchInput ? "show-input" : ""
      }`}
    >
      <div className="left_buttons color-buttons">
        <button
          className="btn btn-dark rounded-circle me-2"
          style={{ width: "40px", height: "40px" }}
          onClick={() => handleColorChange("black")}
        ></button>
        <button
          className="btn btn-success rounded-circle me-2"
          style={{ width: "40px", height: "40px" }}
          onClick={() => handleColorChange("green")}
        ></button>
        <button
          className="btn btn-primary rounded-circle me-2"
          style={{ width: "40px", height: "40px" }}
          onClick={() => handleColorChange("blue")}
        ></button>
        <button
          className="btn btn-light rounded-circle"
          style={{ width: "40px", height: "40px" }}
          onClick={() => handleColorChange("white")}
        ></button>
      </div>
      <div className="middle_buttons">
        <form
          className="search-text input-group"
          onSubmit={(e) => e.preventDefault()}
        >
          <input
            type="text"
            className="form-control"
            placeholder="Search"
            aria-label="Search"
            aria-describedby="search-button"
            value={query}
            onChange={handleInputChange}
          />
        </form>
        <button
          className="btn btn-outline-secondary"
          type="button"
          id="search-button"
          onClick={handleSearchButtonClick}
        >
          <i className="bi bi-search"></i>
        </button>
        <button className="record-button icon-button">
          <i className="bi bi-mic-fill"></i>
        </button>
      </div>
      <div className="right_buttons extra-buttons" ref={menuRef}>
        {/* <DarkModeToggle /> */}
        <button
          className="icon-button"
          onClick={
            user ? () => navigate("/your-videos") : () => navigate("/login")
          }
        >
          <i className="bi bi-plus-square"></i>
        </button>
        <button className="icon-button">
          <i className="bi bi-bell"></i>
        </button>

        {user ? (
          <div className="user-info" onClick={toggleMenu}>
            <img
              src={getMediaSource(user.photo)}
              alt="User"
              className="user-avatar"
            />
            <span className="user-name">{user.displayName}</span>
          </div>
        ) : (
          <Link to="/login">
            <button className="icon-button">
              <i className="bi bi-person-circle"></i>
            </button>
          </Link>
        )}
        {user && isMenuOpen && (
          <div className="user-menu">
            <div className="user-menu-item user-info-row">
              <img
                src={getMediaSource(user.photo)}
                alt="User Avatar"
                className="user-avatar-large"
              />
              <span>{user.username}</span>
            </div>
            <div className="user-menu-item">
              <Link
                to="/settings"
                style={{ textDecoration: "none", color: "black" }}
                className="user-option-button"
              >
                User Settings
              </Link>
            </div>
            <div className="user-menu-item">
              <button className="logout-button" onClick={handleLogout}>
                Logout
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Search;
