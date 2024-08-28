const express = require("express");
const jwt = require("jsonwebtoken");
const router = express.Router();
const User = require("../models/User");  // Assuming you have a User model

const JWT_SECRET = "your_jwt_secret";

// POST /api/tokens - Create a new token
router.post("/", async (req, res) => {
  const { username } = req.body;
  if (!username) {
    return res.status(400).json({ message: "Username is required" });
  }

  try {
    // Find the user by username
    const user = await User.findOne({ username });
    if (!user) {
      console.log("user not found");
      return res.status(404).json({ message: "User not found" });
    }

    // Sign the token with the user's ID
    const token = jwt.sign({ id: user._id }, JWT_SECRET, { expiresIn: "1h" });

    res.status(201).json({ message: "Token created successfully", token });
  } catch (error) {
    console.error("Error creating token:", error);
    res.status(500).json({ message: "Server error" });
  }
});

module.exports = router;