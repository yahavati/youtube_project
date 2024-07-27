import axios from "axios";
import Cookies from "js-cookie";

const API_URL = "http://localhost:5000/api/";


const api = axios.create({
    baseURL: API_URL,
    withCredentials: true, // Allows the client to send cookies with requests
});

// Add a request interceptor
api.interceptors.request.use(
    (config) => {
        const token = Cookies.get("token");

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Add a response interceptor
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (
            error.response &&
            (error.response.status === 401 || error.response.status === 403)
        ) {
            alert("You need to log in again.");
            Cookies.remove("token"); // Remove the token to prevent loop
            // Optional: Redirect to login page
            window.location.href = "/login"; // You can adjust the path as necessary
        }
        return Promise.reject(error);
    }
);

export default api;

