import React, { createContext, useState } from 'react';

export const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [users, setUsers] = useState([]);
    const [authenticatedUser, setAuthenticatedUser] = useState(null);

    const registerUser = (user) => {
        setUsers([...users, { ...user, videos: [] }]);
    };

    const loginUser = (username, password) => {
        const user = users.find(u => u.username === username && u.password === password);
        if (user) {
            setAuthenticatedUser(user);
            return true;
        }
        return false;
    };

    const addUploadedVideo = (video) => {
        setAuthenticatedUser(prevUser => {
            const updatedUser = {
                ...prevUser,
                videos: [...prevUser.videos, video]
            };
            updateUser(updatedUser);
            return updatedUser;
        });
    };

    const updateUploadedVideo = (id, updatedVideo) => {
        setAuthenticatedUser(prevUser => {
            const updatedUser = {
                ...prevUser,
                videos: prevUser.videos.map(video =>
                    video.id === id ? updatedVideo : video
                )
            };
            updateUser(updatedUser);
            return updatedUser;
        });
    };

    const deleteUploadedVideo = (id) => {
        setAuthenticatedUser(prevUser => {
            const updatedUser = {
                ...prevUser,
                videos: prevUser.videos.filter(video => video.id !== id)
            };
            updateUser(updatedUser);
            return updatedUser;
        });
    };

    const updateUser = (updatedUser) => {
        setUsers(prevUsers =>
            prevUsers.map(user => user.username === updatedUser.username ? updatedUser : user)
        );
    };

    return (
        <UserContext.Provider value={{
            users,
            registerUser,
            loginUser,
            authenticatedUser,
            addUploadedVideo,
            updateUploadedVideo,
            deleteUploadedVideo
        }}>
            {children}
        </UserContext.Provider>
    );
};
