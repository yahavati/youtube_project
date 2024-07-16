import api from "./api";

export const toggleVideoLike = async (videoId) => {
  const response = await api.post(`/videos/${videoId}/like`);
  return response.data;
};

export const toggleVideoDislike = async (videoId) => {
  const response = await api.post(`/videos/${videoId}/dislike`);
  return response.data;
};
