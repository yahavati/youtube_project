// UserContext.js
import React, { createContext, useState } from 'react';

export const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [authenticated, setAuthenticated] = useState(false);

    return (
        <UserContext.Provider value={{ user, setUser, authenticated, setAuthenticated }}>
            {children}
        </UserContext.Provider>
    );
};
