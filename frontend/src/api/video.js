import api from "./api";


export const toggleVideoLike = async (videoId) => {
    const response = await api.post(`/videos/${videoId}/like`);
    return response.data;
};

export const toggleVideoDislike = async (videoId) => {
    const response = await api.post(`/videos/${videoId}/dislike`);
    return response.data;
};

export const updateVideoView = async (videoId) => {
    try {
        const response = await api.post(`videos/${videoId}/view`);
        return response.data;
    } catch (error) {
        console.error("Error updating video view count:", error);
        throw error;
    }
};

