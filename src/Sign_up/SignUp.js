// import React, { useState, useContext } from "react";
// import { Link, useNavigate } from "react-router-dom";
// import Camera from "./Camera";
// import Modal from "./Modal";
// import { UserContext } from "../UserContext";
// import "./SignUp.css";

// const SignUp = () => {
//   const [formData, setFormData] = useState({
//     username: "",
//     password: "",
//     confirmPassword: "",
//     picture: null,
//     picturePreview: "",
//   });
//   const [isCameraOpen, setIsCameraOpen] = useState(false);
//   const [validationMessages, setValidationMessages] = useState({
//     username: "",
//     password: "",
//     confirmPassword: "",
//     picture: "",
//     general: "",
//   });
//   const [isRegistered, setIsRegistered] = useState(false);

//   const { user, setUser } = useContext(UserContext);
//   const navigate = useNavigate();

//   const handleChange = (e) => {
//     const { name, value } = e.target;
//     setFormData({
//       ...formData,
//       [name]: value,
//     });
//     validateForm(name, value);
//   };

//   const validateForm = (name, value) => {
//     let messages = { ...validationMessages };

//     if (name === "username") {
//       const usernameValid = /^[a-zA-Z0-9]{5,}$/.test(value);
//       const hasNumber = /\d/.test(value);
//       if (!usernameValid) {
//         messages.username = "Username must be at least 5 characters long.";
//       } else if (!hasNumber) {
//         messages.username = "Username must include at least one number.";
//       } else {
//         messages.username = "";
//       }
//     }

//     if (name === "password") {
//       const hasLetter = /[a-zA-Z]/.test(value);
//       const hasNumber = /\d/.test(value);
//       const isValidLength = value.length >= 8;

//       if (!isValidLength) {
//         messages.password = "Password must be at least 8 characters long.";
//       } else if (!hasLetter) {
//         messages.password = "Password must contain at least one letter.";
//       } else if (!hasNumber) {
//         messages.password = "Password must contain at least one number.";
//       } else {
//         messages.password = "";
//       }

//       messages.confirmPassword = "";
//     }

//     if (name === "confirmPassword") {
//       if (value !== formData.password) {
//         messages.confirmPassword = "Passwords do not match.";
//       } else {
//         messages.confirmPassword = "Passwords match";
//       }
//     }

//     setValidationMessages(messages);
//   };

//   const handleFileChange = (e) => {
//     const file = e.target.files[0];
//     setFormData({
//       ...formData,
//       picture: file,
//       picturePreview: URL.createObjectURL(file),
//     });
//     validateForm("picture", file);
//   };

//   const handleCapture = (dataUrl) => {
//     fetch(dataUrl)
//       .then((res) => res.blob())
//       .then((blob) => {
//         const file = new File([blob], "camera_photo.png", {
//           type: "image/png",
//         });
//         setFormData({
//           ...formData,
//           picture: file,
//           picturePreview: dataUrl,
//         });
//         setIsCameraOpen(false);
//         validateForm("picture", file);
//       });
//   };

//   const handleSubmit = (e) => {
//     e.preventDefault();
//     const isValid = validateAllFields();

//     if (isValid) {
//       if (user && user.username === formData.username) {
//         setValidationMessages((prev) => ({
//           ...prev,
//           general: "Username already exists. Please choose another username.",
//         }));
//         return;
//       }

//       setUser(formData);
//       setIsRegistered(true);
//       setTimeout(() => {
//         console.log("User registered successfully:", formData);
//         navigate("/"); // Redirect to home screen
//       }, 2000); // Simulate a delay
//     }
//   };

//   const validateAllFields = () => {
//     const { username, password, confirmPassword, picture } = formData;
//     let isValid = true;

//     if (!/^[a-zA-Z0-9]{5,}$/.test(username) || !/\d/.test(username)) {
//       setValidationMessages((prev) => ({
//         ...prev,
//         username:
//           "Username must be at least 5 characters long and include at least one number.",
//       }));
//       isValid = false;
//     }

//     if (
//       password.length < 8 ||
//       !/[a-zA-Z]/.test(password) ||
//       !/\d/.test(password)
//     ) {
//       setValidationMessages((prev) => ({
//         ...prev,
//         password:
//           "Password must be at least 8 characters long and contain both letters and numbers.",
//       }));
//       isValid = false;
//     }

//     if (password !== confirmPassword) {
//       setValidationMessages((prev) => ({
//         ...prev,
//         confirmPassword: "Passwords do not match.",
//       }));
//       isValid = false;
//     }

//     if (!picture) {
//       setValidationMessages((prev) => ({
//         ...prev,
//         picture: "Please upload a picture.",
//       }));
//       isValid = false;
//     }

//     return isValid;
//   };

//   return (
//     <div className="container">
//       <div className="form-wrapper">
//         <h1 className="title">Sign Up</h1>
//         <p className="subtitle">Create your account</p>
//         <form onSubmit={handleSubmit}>
//           <div className="input-wrapper">
//             <input
//               type="text"
//               name="username"
//               placeholder="Username"
//               value={formData.username}
//               onChange={handleChange}
//               className="input"
//               required
//             />
//             {validationMessages.username && (
//               <div className="error-message">{validationMessages.username}</div>
//             )}
//           </div>
//           <div className="input-wrapper">
//             <input
//               type="password"
//               name="password"
//               placeholder="Password"
//               value={formData.password}
//               onChange={handleChange}
//               className="input"
//               required
//             />
//             {validationMessages.password && (
//               <div className="error-message">{validationMessages.password}</div>
//             )}
//           </div>
//           <div className="input-wrapper">
//             <input
//               type="password"
//               name="confirmPassword"
//               placeholder="Confirm Password"
//               value={formData.confirmPassword}
//               onChange={handleChange}
//               className="input"
//               required
//             />
//             {validationMessages.confirmPassword && (
//               <div
//                 className={`error-message ${
//                   validationMessages.confirmPassword === "Passwords match"
//                     ? "success-message"
//                     : ""
//                 }`}
//               >
//                 {validationMessages.confirmPassword}
//               </div>
//             )}
//           </div>
//           <div className="file-input-wrapper">
//             <span className="icon">📷</span>
//             <input
//               type="file"
//               name="picture"
//               accept="image/*"
//               onChange={handleFileChange}
//             />
//           </div>
//           {validationMessages.picture && (
//             <div className="error-message">{validationMessages.picture}</div>
//           )}
//           {formData.picturePreview && (
//             <div className="image-preview">
//               <img src={formData.picturePreview} alt="Selected" />
//             </div>
//           )}
//           <div className="validation-container">
//             <p className="validation-message">
//               Username must be at least 5 characters long and include at least
//               one number.
//             </p>
//             <p className="validation-message">
//               Password must be at least 8 characters long and contain both
//               letters and numbers.
//             </p>
//           </div>
//           <button
//             type="button"
//             className="button"
//             onClick={() => setIsCameraOpen(true)}
//           >
//             Open Camera
//           </button>
//           <button type="submit" className="button">
//             Register
//           </button>
//         </form>
//         <Link to="/login" className="link">
//           Already have an Account? Login
//         </Link>
//         <Modal isOpen={isCameraOpen} onClose={() => setIsCameraOpen(false)}>
//           <Camera
//             onCapture={handleCapture}
//             onClose={() => setIsCameraOpen(false)}
//           />
//         </Modal>
//         {isRegistered && (
//           <div className="success-message">
//             Registration successful! Redirecting to the home screen...
//           </div>
//         )}
//         {validationMessages.general && (
//           <div className="error-message">{validationMessages.general}</div>
//         )}
//       </div>
//     </div>
//   );
// };

// export default SignUp;

import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import Camera from "./Camera";
import Modal from "./Modal";
import { UserContext } from "../UserContext";
import "./SignUp.css";

const SignUp = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    picture: null,
    picturePreview: "",
  });
  const [isCameraOpen, setIsCameraOpen] = useState(false);
  const [validationMessages, setValidationMessages] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    picture: "",
    general: "",
  });
  const [isRegistered, setIsRegistered] = useState(false);

  const { user, setUser } = useContext(UserContext);
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
    setFormData({
      ...formData,
      picture: file,
      picturePreview: URL.createObjectURL(file),
    });
    validateForm("picture", file);
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
          picture: file,
          picturePreview: dataUrl,
        });
        setIsCameraOpen(false);
        validateForm("picture", file);
      });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const isValid = validateAllFields();

    if (isValid) {
      console.log("Form is valid, checking user context:", user);
      if (user && user.username === formData.username) {
        setValidationMessages((prev) => ({
          ...prev,
          general: "Username already exists. Please choose another username.",
        }));
        return;
      }

      setUser(formData);
      setIsRegistered(true);
      setTimeout(() => {
        console.log("User registered successfully:", formData);
        navigate("/login"); // Redirect to home screen
      }, 2000); // Simulate a delay
    } else {
      console.log("Form is invalid, validation messages:", validationMessages);
    }
  };

  const validateAllFields = () => {
    const { username, password, confirmPassword, picture } = formData;
    let isValid = true;

    if (!/^[a-zA-Z0-9]{5,}$/.test(username) || !/\d/.test(username)) {
      setValidationMessages((prev) => ({
        ...prev,
        username:
          "Username must be at least 5 characters long and include at least one number.",
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

    if (!picture) {
      setValidationMessages((prev) => ({
        ...prev,
        picture: "Please upload a picture.",
      }));
      isValid = false;
    }

    return isValid;
  };

  return (
    <div className="container">
      <div className="form-wrapper">
        <h1 className="title">Sign Up</h1>
        <p className="subtitle">Create your account</p>
        <form onSubmit={handleSubmit}>
          <div className="input-wrapper">
            <input
              type="text"
              name="username"
              placeholder="Username"
              value={formData.username}
              onChange={handleChange}
              className="input"
              required
            />
            {validationMessages.username && (
              <div className="error-message">{validationMessages.username}</div>
            )}
          </div>
          <div className="input-wrapper">
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              className="input"
              required
            />
            {validationMessages.password && (
              <div className="error-message">{validationMessages.password}</div>
            )}
          </div>
          <div className="input-wrapper">
            <input
              type="password"
              name="confirmPassword"
              placeholder="Confirm Password"
              value={formData.confirmPassword}
              onChange={handleChange}
              className="input"
              required
            />
            {validationMessages.confirmPassword && (
              <div
                className={`error-message ${
                  validationMessages.confirmPassword === "Passwords match"
                    ? "success-message"
                    : ""
                }`}
              >
                {validationMessages.confirmPassword}
              </div>
            )}
          </div>
          <div className="file-input-wrapper">
            <span className="icon">📷</span>
            <input
              type="file"
              name="picture"
              accept="image/*"
              onChange={handleFileChange}
            />
          </div>
          {validationMessages.picture && (
            <div className="error-message">{validationMessages.picture}</div>
          )}
          {formData.picturePreview && (
            <div className="image-preview">
              <img src={formData.picturePreview} alt="Selected" />
            </div>
          )}
          <div className="validation-container">
            <p className="validation-message">
              Username must be at least 5 characters long and include at least
              one number.
            </p>
            <p className="validation-message">
              Password must be at least 8 characters long and contain both
              letters and numbers.
            </p>
          </div>
          <button
            type="button"
            className="button"
            onClick={() => setIsCameraOpen(true)}
          >
            Open Camera
          </button>
          <button type="submit" className="button">
            Register
          </button>
        </form>
        <Link to="/login" className="link">
          Already have an Account? Login
        </Link>
        <Modal isOpen={isCameraOpen} onClose={() => setIsCameraOpen(false)}>
          <Camera
            onCapture={handleCapture}
            onClose={() => setIsCameraOpen(false)}
          />
        </Modal>
        {isRegistered && (
          <div className="success-message">
            Registration successful! Redirecting to the home screen...
          </div>
        )}
        {validationMessages.general && (
          <div className="error-message">{validationMessages.general}</div>
        )}
      </div>
    </div>
  );
};

export default SignUp;
