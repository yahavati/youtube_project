// MidMenu.js
import React, { useRef } from 'react';

function MidMenu() {
  const menuRef = useRef(null);

  const scrollLeft = () => {
    if (menuRef.current) {
      menuRef.current.scrollBy({ left: -200, behavior: 'smooth' });
    }
  };

  const scrollRight = () => {
    if (menuRef.current) {
      menuRef.current.scrollBy({ left: 200, behavior: 'smooth' });
    }
  };

  return (
    <div className="mid-menu-container">
      <button className="scroll-button" onClick={scrollLeft}>&lt;</button>
      <div className="btn-group" role="group" aria-label="Basic radio toggle button group" ref={menuRef}>
        <input type="radio" className="btn-check" name="btnradio" id="btnradio1" autoComplete="off" defaultChecked />
        <label className="btn btn-outline-primary" htmlFor="btnradio1">All</label>
        <input type="radio" className="btn-check" name="btnradio" id="btnradio2" autoComplete="off" />
        <label className="btn btn-outline-primary" htmlFor="btnradio2">Music</label>
        <input type="radio" className="btn-check" name="btnradio" id="btnradio3" autoComplete="off" />
        <label className="btn btn-outline-primary" htmlFor="btnradio3">Gaming</label>
        <input type="radio" className="btn-check" name="btnradio" id="btnradio4" autoComplete="off" />
        <label className="btn btn-outline-primary" htmlFor="btnradio4">Computer</label>
        <input type="radio" className="btn-check" name="btnradio" id="btnradio5" autoComplete="off" />
        <label className="btn btn-outline-primary" htmlFor="btnradio5">Live</label>
        <input type="radio" className="btn-check" name="btnradio" id="btnradio6" autoComplete="off" />
        <label className="btn btn-outline-primary" htmlFor="btnradio6">News</label>
        <input type="radio" className="btn-check" name="btnradio" id="btnradio7" autoComplete="off" />
        <label className="btn btn-outline-primary" htmlFor="btnradio7">Basketball</label>
      </div>
      <button className="scroll-button" onClick={scrollRight}>&gt;</button>
    </div>
  );
}

export default MidMenu;
