// Login.js
import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { UserContext } from "./UserContext";
import "./Login.css";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [validationMessages, setValidationMessages] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { loginUser } = useContext(UserContext);

  const validateForm = (name, value) => {
    let messages = { ...validationMessages };

    if (name === "username") {
      const usernameValid = /^[a-zA-Z0-9]{5,}$/.test(value);
      const hasNumber = /\d/.test(value);
      if (!usernameValid) {
        messages.username = "Username must be at least 5 characters long.";
      } else if (!hasNumber) {
        messages.username = "Username must include at least one number.";
      } else {
        messages.username = "";
      }
    }

    if (name === "password") {
      const hasLetter = /[a-zA-Z]/.test(value);
      const hasNumber = /\d/.test(value);
      const isValidLength = value.length >= 8;

      if (!isValidLength) {
        messages.password = "Password must be at least 8 characters long.";
      } else if (!hasLetter) {
        messages.password = "Password must contain at least one letter.";
      } else if (!hasNumber) {
        messages.password = "Password must contain at least one number.";
      } else {
        messages.password = "";
      }
    }

    setValidationMessages(messages);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "username") {
      setUsername(value);
    } else if (name === "password") {
      setPassword(value);
    }
    validateForm(name, value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!username || !password) {
      setError("All fields are required.");
      return;
    }
    if (loginUser(username, password)) {
      navigate("/");
    } else {
      setError("Invalid username or password.");
    }
  };

  const handleSignUp = () => {
    navigate("/signup");
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <img
          onClick={() => navigate("/")}
          src="/youtube-logo.png"
          alt="YouTube Logo"
          className="login-logo"
          style={{ cursor: "pointer" }}
        />
        <form onSubmit={handleSubmit}>
          <div className="login-form-group">
            <input
              type="text"
              id="username"
              name="username"
              placeholder="Username"
              value={username}
              onChange={handleChange}
              autoComplete="off"
              className="login-input"
            />
            {validationMessages.username && (
              <div className="login-error">{validationMessages.username}</div>
            )}
          </div>
          <div className="login-form-group">
            <input
              type="password"
              id="password"
              name="password"
              placeholder="Password"
              value={password}
              onChange={handleChange}
              autoComplete="off"
              className="login-input"
            />
            {validationMessages.password && (
              <div className="login-error">{validationMessages.password}</div>
            )}
          </div>
          {error && <p className="login-error">{error}</p>}
          <button type="submit" className="login-button">
            Login
          </button>
          <button
            type="button"
            className="login-signup-button"
            onClick={handleSignUp}
          >
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
