// Search.js
import React, { useContext, useState, useEffect, useRef } from "react";
import "./Search.css";
import { UserContext } from "../UserContext";
import { Link, useNavigate } from "react-router-dom";

function Search({ onSearch, setBgColor }) {
  const [query, setQuery] = useState("");
  const [showSearchInput, setShowSearchInput] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const { authenticatedUser, setFakeUser, logout } = useContext(UserContext);
  const menuRef = useRef(null);

  const navigate = useNavigate();

  useEffect(() => {
    if (authenticatedUser) {
      console.log("Authenticated user changed:", authenticatedUser);
    }
  }, [authenticatedUser]);

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
    logout();
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
        <button
          className="icon-button"
          onClick={
            authenticatedUser
              ? () => navigate("/your-videos")
              : () => navigate("/login")
          }
        >
          <i className="bi bi-plus-square"></i>
        </button>
        <button className="icon-button">
          <i className="bi bi-bell"></i>
        </button>

        {authenticatedUser ? (
          <div className="user-info" onClick={toggleMenu}>
            <img
              src={URL.createObjectURL(authenticatedUser.picture)}
              alt="User"
              className="user-avatar"
            />
            <span className="user-name">{authenticatedUser.nickname}</span>
          </div>
        ) : (
          <Link to="/login">
            <button className="icon-button">
              <i className="bi bi-person-circle"></i>
            </button>
          </Link>
        )}
        {authenticatedUser && isMenuOpen && (
          <div className="user-menu">
            <div className="user-menu-item user-info-row">
              <img
                src={URL.createObjectURL(authenticatedUser.picture)}
                alt="User Avatar"
                className="user-avatar-large"
              />
              <span>{authenticatedUser.nickname}</span>
            </div>
            <div className="user-menu-item">
              <button className="user-option-button">User Settings</button>
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
