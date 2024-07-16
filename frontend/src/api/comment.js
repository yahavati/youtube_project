import api from "./api";

// Add a new comment
export const addComment = async (commentData) => {
  const response = await api.post("/comments", commentData);
  return response.data;
};

// Update a comment
export const updateComment = async (commentId, updates) => {
  const response = await api.put(`/comments/${commentId}`, updates);
  return response.data;
};

// Delete a comment
export const deleteComment = async (commentId) => {
  const response = await api.delete(`/comments/${commentId}`);
  return response.data;
};

// Toggle like on a comment
export const toggleLike = async (commentId) => {
  const response = await api.post(`/comments/${commentId}/like`);
  return response.data;
};

// Toggle dislike on a comment
export const toggleDislike = async (commentId) => {
  const response = await api.post(`/comments/${commentId}/dislike`);
  return response.data;
};
