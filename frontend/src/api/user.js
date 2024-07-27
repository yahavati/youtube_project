import api from "./api";


// Function to get all videos for a user
export const getUserVideos = async (userId) => {
    try {
        const response = await api.get(`/users/${userId}/videos`);
        return response.data;
    } catch (error) {
        console.error("Error fetching user videos:", error);
        throw error;
    }
};

// Function to create a new video for a user
export const createUserVideo = async (userId, videoData) => {
    try {
        const response = await api.post(`/users/${userId}/videos`, videoData, {
            headers: {
                "Content-Type": "multipart/form-data", // Assuming videoData is FormData
            },
        });
        return response.data;
    } catch (error) {
        console.error("Error creating new video:", error);
        throw error;
    }
};

// Function to update a video for a user
export const updateUserVideo = async (userId, videoId, data) => {
    const formData = new FormData();
    formData.append("title", data.title);
    formData.append("description", data.description);
    if (data.thumbnail) {
        formData.append("thumbnail", data.thumbnail);
    }

    const response = await api.put(
        `/users/${userId}/videos/${videoId}`,
        formData,
        {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        }
    );

    return response.data;
};

// Function to delete a video for a user
export const deleteUserVideo = async (userId, videoId) => {
    try {
        const response = await api.delete(`/users/${userId}/videos/${videoId}`);
        return response.data;
    } catch (error) {
        console.error("Error deleting video:", error);
        throw error;
    }
};

