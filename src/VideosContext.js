import React, { createContext, useState, useEffect } from "react";

export const VideosContext = createContext();

export const VideosProvider = ({ children }) => {
  const [videos, setVideos] = useState([]);

  useEffect(() => {
    fetch(`${process.env.PUBLIC_URL}/videos.json`)
      .then((response) => response.json())
      .then((data) => {
        const initializedVideos = data.map((video) => ({
          ...video,
          likes: video.likes || 0,
          dislikes: video.dislikes || 0,
          comments: video.comments || [],
        }));
        setVideos(initializedVideos);
      })
      .catch((error) => console.error("Error fetching videos:", error));
  }, []);

  const updateVideo = (id, updates) => {
    setVideos((prevVideos) =>
      prevVideos.map((video) =>
        video.id === id ? { ...video, ...updates } : video
      )
    );
  };

  const addVideo = (newVideo) => {
    setVideos((prevVideos) => [...prevVideos, newVideo]);
  };

  const deleteVideo = (id) => {
    setVideos((prevVideos) => prevVideos.filter((video) => video.id !== id));
  };

  return (
    <VideosContext.Provider
      value={{ videos, updateVideo, addVideo, deleteVideo }}
    >
      {children}
    </VideosContext.Provider>
  );
};
