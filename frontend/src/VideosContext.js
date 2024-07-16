import React, { createContext, useState, useEffect } from "react";
import api from "./api/api";

export const VideosContext = createContext();

export const VideosProvider = ({ children }) => {
  const [videos, setVideos] = useState([]);

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const response = await api.get("/videos");
        const data = response.data;
        const initializedVideos = data.map((video) => ({
          ...video,
          likes: video.likes || 0,
          dislikes: video.dislikes || 0,
          comments: video.comments || [],
        }));
        setVideos(initializedVideos);
      } catch (error) {
        console.error("Error fetching videos:", error);
      }
    };

    fetchVideos();
  }, []);

  const getVideoById = async (id) => {
    try {
      const response = await api.get(`/videos/${id}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching video by ID:", error);
      throw error;
    }
  };

  const updateVideo = (id, updates) => {
    setVideos((prevVideos) =>
      prevVideos.map((video) =>
        video._id === id ? { ...video, ...updates } : video
      )
    );
  };

  const addVideo = (newVideo) => {
    setVideos((prevVideos) => [...prevVideos, newVideo]);
  };

  const deleteVideo = (id) => {
    setVideos((prevVideos) => prevVideos.filter((video) => video._id !== id));
  };

  return (
    <VideosContext.Provider
      value={{ videos, getVideoById, updateVideo, addVideo, deleteVideo }}
    >
      {children}
    </VideosContext.Provider>
  );
};
