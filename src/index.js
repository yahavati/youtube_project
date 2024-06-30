import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router } from 'react-router-dom';
import HomeScreen from './App Component/HomeScreen'; 

const root = createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router>
      <HomeScreen />
    </Router>
  </React.StrictMode>
);
