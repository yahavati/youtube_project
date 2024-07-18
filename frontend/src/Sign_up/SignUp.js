import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Camera from "./Camera";
import Modal from "./Modal";
import "./SignUp.css";
import { register } from "../api/auth";

const SignUp = () => {
  const [formData, setFormData] = useState({
    username: "",
    displayName: "",
    password: "",
    confirmPassword: "",
    photo: null,
    photoPreview: "",
  });
  const [isCameraOpen, setIsCameraOpen] = useState(false);
  const [validationMessages, setValidationMessages] = useState({
    username: "",
    displayName: "",
    password: "",
    confirmPassword: "",
    photo: "",
    general: "",
  });
  const [isRegistered, setIsRegistered] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    validateForm(name, value);
  };

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

    if (name === "displayName") {
      const usernameValid = /^[a-zA-Z0-9]{5,}$/.test(value);
      const hasNumber = /\d/.test(value);
      if (!usernameValid) {
        messages.displayName =
          "Display name must be at least 5 characters long.";
      } else if (!hasNumber) {
        messages.displayName = "Display name must include at least one number.";
      } else {
        messages.displayName = "";
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

      messages.confirmPassword = "";
    }

    if (name === "confirmPassword") {
      if (value !== formData.password) {
        messages.confirmPassword = "Passwords do not match.";
      } else {
        messages.confirmPassword = "Passwords match";
      }
    }

    setValidationMessages(messages);
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    const reader = new FileReader();

    reader.onloadend = () => {
      setFormData({
        ...formData,
        photo: file,
        photoPreview: reader.result,
      });
      validateForm("photo", file);
    };

    if (file) {
      reader.readAsDataURL(file);
    }
  };

  const handleCapture = (dataUrl) => {
    fetch(dataUrl)
      .then((res) => res.blob())
      .then((blob) => {
        const file = new File([blob], "camera_photo.png", {
          type: "image/png",
        });
        setFormData({
          ...formData,
          photo: file,
          photoPrview: dataUrl,
        });
        setIsCameraOpen(false);
        validateForm("photo", file);
      });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const isValid = validateAllFields();

    if (isValid) {
      try {
        const formDataToSubmit = new FormData();
        formDataToSubmit.append("displayName", formData.displayName);
        formDataToSubmit.append("username", formData.username);
        formDataToSubmit.append("password", formData.password);
        formDataToSubmit.append("photo", formData.photo);

        await register(formDataToSubmit);
        setIsRegistered(true);
        setTimeout(() => {
          console.log("User registered successfully:", formData);
          navigate("/login"); // Redirect to login screen
        }, 2000); // Simulate a delay
      } catch (error) {
        console.error("Error during registration:", error);
        setValidationMessages((prev) => ({
          ...prev,
          general: error.response.data.message || "Registration failed.",
        }));
      }
    }
  };
  const validateAllFields = () => {
    const { username, displayName, password, confirmPassword, photo } =
      formData;
    let isValid = true;

    if (!/^[a-zA-Z0-9]{5,}$/.test(username) || !/\d/.test(username)) {
      setValidationMessages((prev) => ({
        ...prev,
        username:
          "Username must be at least 5 characters long and include at least one number.",
      }));
      isValid = false;
    }

    if (!/^[a-zA-Z0-9]{5,}$/.test(username) || !/\d/.test(username)) {
      setValidationMessages((prev) => ({
        ...prev,
        displayName:
          "Display name must be at least 5 characters long and include at least one number.",
      }));
      isValid = false;
    }

    if (
      password.length < 8 ||
      !/[a-zA-Z]/.test(password) ||
      !/\d/.test(password)
    ) {
      setValidationMessages((prev) => ({
        ...prev,
        password:
          "Password must be at least 8 characters long and contain both letters and numbers.",
      }));
      isValid = false;
    }

    if (password !== confirmPassword) {
      setValidationMessages((prev) => ({
        ...prev,
        confirmPassword: "Passwords do not match.",
      }));
      isValid = false;
    }

    if (!photo) {
      setValidationMessages((prev) => ({
        ...prev,
        photo: "Please upload a photo.",
      }));
      isValid = false;
    }

    return isValid;
  };

  return (
    <div className="signup-container">
      <div className="signup-form-wrapper">
        <h1 className="signup-title">Sign Up</h1>
        <p className="signup-subtitle">Create your account</p>
        <form onSubmit={handleSubmit}>
          <div className="signup-input-wrapper">
            <input
              type="text"
              name="username"
              placeholder="Username"
              value={formData.username}
              onChange={handleChange}
              className="signup-input"
              required
            />
            {validationMessages.username && (
              <div className="signup-error-message">
                {validationMessages.username}
              </div>
            )}
          </div>
          <div className="signup-input-wrapper">
            <input
              type="text"
              name="displayName"
              placeholder="Display Name"
              value={formData.displayName}
              onChange={handleChange}
              className="signup-input"
              required
            />
            {validationMessages.displayName && (
              <div className="signup-error-message">
                {validationMessages.displayName}
              </div>
            )}
          </div>
          <div className="signup-input-wrapper">
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              className="signup-input"
              required
            />
            {validationMessages.password && (
              <div className="signup-error-message">
                {validationMessages.password}
              </div>
            )}
          </div>
          <div className="signup-input-wrapper">
            <input
              type="password"
              name="confirmPassword"
              placeholder="Confirm Password"
              value={formData.confirmPassword}
              onChange={handleChange}
              className="signup-input"
              required
            />
            {validationMessages.confirmPassword && (
              <div
                className={`signup-error-message ${
                  validationMessages.confirmPassword === "Passwords match"
                    ? "signup-success-message"
                    : ""
                }`}
              >
                {validationMessages.confirmPassword}
              </div>
            )}
          </div>
          <div className="signup-file-input-wrapper">
            <span className="signup-icon">📷</span>
            <input
              type="file"
              name="photo"
              accept="image/*"
              onChange={handleFileChange}
            />
          </div>
          {formData.photoPreview && (
            <div className="signup-photo-preview">
              <img
                src={formData.photoPreview}
                alt="Preview"
                style={{
                  width: "150px",
                  height: "150px",
                  objectFit: "cover",
                  borderRadius: "10px",
                }}
              />
            </div>
          )}
          {validationMessages.photo && (
            <div className="signup-error-message">
              {validationMessages.photo}
            </div>
          )}
          <div className="signup-validation-container">
            <p className="signup-validation-message">
              Username must be at least 5 characters long and include at least
              one number.
            </p>
            <p className="signup-validation-message">
              Display name must be at least 5 characters long and include at
              least one number.
            </p>
            <p className="signup-validation-message">
              Password must be at least 8 characters long and contain both
              letters and numbers.
            </p>
          </div>
          <button
            type="button"
            className="signup-button"
            onClick={() => setIsCameraOpen(true)}
          >
            Open Camera
          </button>
          <button type="submit" className="signup-button">
            Register
          </button>
        </form>
        <a href="#" className="signup-link" onClick={() => navigate("/login")}>
          Already have an Account? Login
        </a>
        <Modal isOpen={isCameraOpen} onClose={() => setIsCameraOpen(false)}>
          <Camera
            onCapture={handleCapture}
            onClose={() => setIsCameraOpen(false)}
          />
        </Modal>
        {isRegistered && (
          <div className="signup-success-message">
            Registration successful! Redirecting to the login screen...
          </div>
        )}
        {validationMessages.general && (
          <div className="signup-error-message">
            {validationMessages.general}
          </div>
        )}
      </div>
    </div>
  );
};

export default SignUp;
