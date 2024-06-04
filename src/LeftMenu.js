import React, { useRef } from 'react';
import { useNavigate } from 'react-router-dom';

function LeftMenu() {
  const menuRef = useRef(null);
  const navigate = useNavigate();

  const scrollUp = () => {
    if (menuRef.current) {
      menuRef.current.scrollBy({ top: -100, behavior: 'smooth' });
    }
  };

  const scrollDown = () => {
    if (menuRef.current) {
      menuRef.current.scrollBy({ top: 100, behavior: 'smooth' });
    }
  };

  const navigateToHome = () => {
    navigate('/');
  };

  return (
    <nav className="col-3 col-lg-12 bg-light vh-100 d-flex flex-column">
      <div> 
      </div>
      <button onClick={scrollUp} className="btn btn-light">▲</button>
      <ul ref={menuRef} className="list-group list-group-flush flex-grow-1 overflow-auto">
        <li className="list-group-item" onClick={navigateToHome} style={{ cursor: 'pointer' }}>
          <i className="bi bi-house-door-fill"></i>
          <span className="ms-2">Home</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-person-video"></i>
          <span className="ms-2">Shorts</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-collection-play"></i>
          <span className="ms-2">Subscriptions</span>
        </li>
        <hr className="line" /> {/* Line separator */}
        <li className="list-group-item d-flex justify-content-between align-items-center">
          <span>You</span>
          <i className="bi bi-chevron-right"></i>
        </li>
        <li className="list-group-item">
          <i className="bi bi-images"></i>
          <span className="ms-2">Your channel</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-hourglass-split"></i>
          <span className="ms-2">History</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-music-note-list"></i>
          <span className="ms-2">Playlists</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-person-video2"></i>
          <span className="ms-2">Your videos</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-clock-history"></i>
          <span className="ms-2">Watch later</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-hand-thumbs-up"></i>
          <span className="ms-2">Liked videos</span>
        </li>
        <hr className="line" /> {/* Line separator */}
        <div className="list-group-item">Subscriptions</div>
        <li className="list-group-item">
          <span className="ms-2">Ido Cohen</span>
        </li>
        <li className="list-group-item">
          <span className="ms-2">Yahav Atias</span>
        </li>
        <li className="list-group-item">
          <span className="ms-2">Lian</span>
        </li>
        <hr className="line" /> {/* Line separator */}
        <div className="list-group-item">Explore</div>
        <li className="list-group-item">
          <i className="bi bi-fire"></i>
          <span className="ms-2">Trending</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-music-note-list"></i>
          <span className="ms-2">Music</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-controller"></i>
          <span className="ms-2">Gaming</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-newspaper"></i>
          <span className="ms-2">News</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-dribbble"></i>
          <span className="ms-2">Sports</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-broadcast-pin"></i>
          <span className="ms-2">Podcasts</span>
        </li>
        <hr className="line" /> {/* Line separator */}
        <li className="list-group-item">
          <i className="bi bi-gear"></i>
          <span className="ms-2">Setting</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-flag"></i>
          <span className="ms-2">Report history</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-patch-question"></i>
          <span className="ms-2">Help</span>
        </li>
        <li className="list-group-item">
          <i className="bi bi-send"></i>
          <span className="ms-2">Send feedback</span>
        </li>
      </ul>
      <button onClick={scrollDown} className="btn btn-light">▼</button>
    </nav>
  );
}

export default LeftMenu;
