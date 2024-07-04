import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import reportWebVitals from './reportWebVitals';

const bodyStyle = {
    margin: 0,
    fontFamily: 'Arial, sans-serif',
    backgroundColor: '#000000',
    color: 'white',
    WebkitFontSmoothing: 'antialiased',
    MozOsxFontSmoothing: 'grayscale',
};

document.body.style = `
  margin: 0;
  font-family: Arial, sans-serif;
  background-color: #000000;
  color: white;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
`;

ReactDOM.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
    document.getElementById('root')
);

reportWebVitals();
