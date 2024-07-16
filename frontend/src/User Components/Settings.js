import React, { useState, useContext } from "react";
import { UserContext } from "../UserContext";
import api from "../api/api";
import "./Settings.css";

const Settings = () => {
  const { user, fetchUser } = useContext(UserContext);
  const [displayName, setDisplayName] = useState(user?.displayName || "");
  const [photo, setPhoto] = useState(null);
  const [photoPreview, setPhotoPreview] = useState(user?.photo || "");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handlePhotoChange = (e) => {
    const file = e.target.files[0];
    setPhoto(file);

    const reader = new FileReader();
    reader.onloadend = () => {
      setPhotoPreview(reader.result);
    };
    reader.readAsDataURL(file);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("displayName", displayName);
    if (photo) {
      formData.append("photo", photo);
    }

    try {
      await api.put("/users/me", formData);
      setSuccess("Profile updated successfully");
      fetchUser(); // Refresh user data
    } catch (error) {
      console.error("Error updating profile:", error);
      setError("Failed to update profile");
    }
  };

  return (
    <div className="settings-container">
      <h2>Settings</h2>
      {error && <p className="settings-error">{error}</p>}
      {success && <p className="settings-success">{success}</p>}
      <form onSubmit={handleSubmit} className="settings-form">
        <div className="settings-form-group">
          <label htmlFor="displayName">Display Name</label>
          <input
            type="text"
            id="displayName"
            value={displayName}
            onChange={(e) => setDisplayName(e.target.value)}
          />
        </div>
        <div className="settings-form-group">
          <label htmlFor="photo">Profile Photo</label>
          <input
            type="file"
            id="photo"
            accept="image/*"
            onChange={handlePhotoChange}
          />
          {photoPreview && (
            <img
              src={
                photoPreview.startsWith("data:image")
                  ? photoPreview
                  : `data:image;base64,${photoPreview}`
              }
              alt="Profile Preview"
              className="settings-photo-preview"
            />
          )}
        </div>
        <button type="submit" className="settings-save-button">
          Save Changes
        </button>
      </form>
    </div>
  );
};

export default Settings;
