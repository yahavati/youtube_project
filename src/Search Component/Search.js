import React, { useContext, useState, useEffect } from "react";
import "./Search.css"; // Ensure this file exists in the same directory as Search.js
import { UserContext } from "../UserContext";
import { Link } from "react-router-dom";

function Search({ onSearch, setBgColor }) {
  const [query, setQuery] = useState("");
  const [showSearchInput, setShowSearchInput] = useState(false);
  const { authenticatedUser } = useContext(UserContext);

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
      <div className="right_buttons extra-buttons">
        <button className="icon-button">
          <i className="bi bi-plus-square"></i>
        </button>
        <button className="icon-button">
          <i className="bi bi-bell"></i>
        </button>

        {authenticatedUser ? (
          <div className="user-info">
            <img
              src={URL.createObjectURL(authenticatedUser.picture)}
              alt="User"
              className="user-avatar"
            />
            <span className="user-name">{authenticatedUser.username}</span>
          </div>
        ) : (
          <Link to="/login">
            <button className="icon-button">
              <i className="bi bi-person-circle"></i>
            </button>
          </Link>
        )}
      </div>
    </div>
  );
}

export default Search;
