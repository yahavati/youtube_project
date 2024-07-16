import React, { createContext, useEffect, useState } from "react";
import Cookies from "js-cookie";
import api from "./api/api";

export const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const fetchUser = async () => {
    const token = Cookies.get("token");
    if (token) {
      try {
        const response = await api.get("/auth/me"); // Assuming you have an endpoint to get the current user
        setUser(response.data);
      } catch (error) {
        console.error("Error fetching user:", error);
        setUser(null);
      }
    }
  };

  useEffect(() => {
    fetchUser();
  }, []);

  const loginUser = (userData) => {
    setUser(userData);
  };

  const logoutUser = async () => {
    try {
      await api.post("/auth/logout"); // Assuming you have a logout endpoint
      Cookies.remove("token"); // Remove the token cookie
      setUser(null);
    } catch (error) {
      console.error("Error logging out:", error);
    }
  };

  return (
    <UserContext.Provider value={{ user, loginUser, logoutUser, fetchUser }}>
      {children}
    </UserContext.Provider>
  );
};
