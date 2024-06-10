// Search.js
import React, { useState } from 'react';
import './App.css'; // Ensure to create and import the CSS file if necessary

function Search({ onSearch, setBgColor }) {
  const [query, setQuery] = useState('');
  const [showSearchInput, setShowSearchInput] = useState(false);

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
    <div className={`search-container ${showSearchInput ? 'show-input' : ''}`}>
      <div className="color-buttons">
        <button
          className="btn btn-dark rounded-circle me-2"
          style={{ width: '40px', height: '40px' }}
          onClick={() => handleColorChange('black')}
        ></button>
        <button
          className="btn btn-success rounded-circle me-2"
          style={{ width: '40px', height: '40px' }}
          onClick={() => handleColorChange('green')}
        ></button>
        <button
          className="btn btn-primary rounded-circle me-2"
          style={{ width: '40px', height: '40px' }}
          onClick={() => handleColorChange('blue')}
        ></button>
        <button
          className="btn btn-light rounded-circle"
          style={{ width: '40px', height: '40px' }}
          onClick={() => handleColorChange('white')}
        ></button>
      </div>
      <form className="input-group" onSubmit={(e) => e.preventDefault()}>
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
      <button className="btn btn-outline-secondary" type="button" id="search-button" onClick={handleSearchButtonClick}>
        <i className="bi bi-search"></i>
      </button>
      <button className="microphone-button ms-2" type="button">
        <i className="bi bi-mic-fill"></i>
      </button>
       <div className="extra-buttons">
        <button className="icon-button" type="button">
          <i className="bi bi-plus-square"></i>
        </button>
        <button className="icon-button" type="button">
          <i className="bi bi-bell"></i>
        </button>
        <button className="icon-button" type="button">
          <i className="bi bi-person-circle"></i>
        </button>
      </div>
    </div>
  );
}

export default Search;
