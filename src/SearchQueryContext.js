import React, { createContext, useState } from "react";

// Create the context
export const SearchQueryContext = createContext();

// Create the provider component
export const SearchQueryProvider = ({ children }) => {
  const [searchQuery, setSearchQuery] = useState("");

  return (
    <SearchQueryContext.Provider value={{ searchQuery, setSearchQuery }}>
      {children}
    </SearchQueryContext.Provider>
  );
};
