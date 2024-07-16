import React, { useContext } from "react";
import { DarkModeContext } from "../DarkModeContext";

const DarkModeToggle = () => {
  const { isDarkMode, setIsDarkMode } = useContext(DarkModeContext);

  return (
    <button onClick={() => setIsDarkMode((prevMode) => !prevMode)}>
      {isDarkMode ? "Switch to Light Mode" : "Switch to Dark Mode"}
    </button>
  );
};

export default DarkModeToggle;
