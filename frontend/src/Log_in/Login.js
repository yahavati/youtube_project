import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/auth";
import "./Login.css";
import { UserContext } from "../UserContext";

function Login() {
  const [username, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { loginUser } = useContext(UserContext); // Destructure loginUser from UserContext

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!username || !password) {
      setError("All fields are required.");
      return;
    }

    try {
      const response = await login({ username, password });
      loginUser(response?.user);
      navigate("/");
    } catch (error) {
      setError(error.response.data.message || "Invalid username or password.");
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
              placeholder="Username"
              value={username}
              onChange={(e) => setEmail(e.target.value)}
              autoComplete="off"
              className="login-input"
            />
          </div>
          <div className="login-form-group">
            <input
              type="password"
              id="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="off"
              className="login-input"
            />
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
        <div className="login-message">
          Username must be at least 5 characters long and has to contain at
          least 1 number and 1 letter
          <br />
          Password must be at least 8 characters long and has to contain at
          least 1 number and 1 letter
        </div>
      </div>
    </div>
  );
}

export default Login;
