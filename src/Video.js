import React from 'react';
import { useNavigate } from 'react-router-dom';

function Video({ id, title, author, views, when, img }) {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/video/${id}`);
  };

  return (
    <div className="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-4">
      <div 
        className="card border-0 p-1 h-100" 
        onClick={handleClick} 
        style={{ cursor: 'pointer' }}
      >
        <img src={img} className="card-img-top" alt={title} />
        <div className="card-body">
          <h5 className="card-title">{title}</h5>
          <h6 className="card-subtitle mb-2 text-muted">{author}</h6>
          <p className="card-text">{views} - {when}</p>
        </div>
      </div>
    </div>
  );
}

export default Video;
