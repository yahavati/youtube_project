// Search.js
import React, { useState } from 'react';

function Search({ onSearch, setBgColor }) { // Add setBgColor prop
  const [query, setQuery] = useState('');

  const handleInputChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    onSearch(value);
  };

  const handleColorChange = (color) => {
    setBgColor(color);
  };

  return (
    <div className="row bg-white justify-content-center align-items-center">
      <div className="col-5 search-container d-flex align-items-center">
        <div className="d-flex justify-content-around me-3">
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
        <form className="input-group" onSubmit={(e) => e.preventDefault()} style={{ flex: 1 }}>
          <input
            type="text"
            className="form-control"
            placeholder="Search"
            aria-label="Search"
            aria-describedby="search-button"
            value={query}
            onChange={handleInputChange}
          />
          <button className="btn btn-outline-secondary" type="submit" id="search-button">
            <i className="bi bi-search"></i>
          </button>
        </form>
        <button className="microphone-button ms-2" type="button">
          <i className="bi bi-mic-fill"></i>
        </button>
      </div>
    </div>
  );
}

export default Search;
