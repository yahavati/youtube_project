// MidMenu.js
import React, {useRef} from 'react';
import './MidMenu.css';

function MidMenu() {
    const menuRef = useRef(null);

    const scrollLeft = () => {
        if (menuRef.current) {
            menuRef.current.scrollBy({ left: -100, behavior: 'smooth' });
        }
    };

    const scrollRight = () => {
        if (menuRef.current) {
            menuRef.current.scrollBy({ left: 100, behavior: 'smooth' });
        }
    };

    const menuItems = [
        {id: 'btnradio1', label: 'All', defaultChecked: true},
        {id: 'btnradio2', label: 'Music'},
        {id: 'btnradio3', label: 'Gaming'},
        {id: 'btnradio4', label: 'Computer'},
        {id: 'btnradio5', label: 'Live'},
        {id: 'btnradio6', label: 'News'},
        {id: 'btnradio7', label: 'Basketball'},
    ];

    return (
        <div className="mid-menu-container">
            <button className="scroll-button" onClick={scrollLeft}>&lt;</button>
            <div className="btn-group" role="group" aria-label="Basic radio toggle button group" ref={menuRef}>
                {menuItems.map((item) => (
                    <React.Fragment key={item.id}>
                        <input
                            type="radio"
                            className="btn-check"
                            name="btnradio"
                            id={item.id}
                            autoComplete="off"
                            defaultChecked={item.defaultChecked}
                        />
                        <label className="btn btn-outline-primary custom-btn" htmlFor={item.id}>
                            {item.label}
                        </label>
                    </React.Fragment>
                ))}
            </div>
            <button className="scroll-button" onClick={scrollRight}>&gt;</button>
        </div>
    );
}

export default MidMenu;