import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { UserContext } from "./UserContext";

const ProtectedRoute = ({ children }) => {
  const { authenticatedUser } = useContext(UserContext);

  if (!authenticatedUser) {
    // If user is not authenticated, redirect to login page
    return <Navigate to="/login" />;
  }

  // If user is authenticated, render the children components
  return children;
};

export default ProtectedRoute;
