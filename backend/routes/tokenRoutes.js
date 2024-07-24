const express = require("express");
const jwt = require("jsonwebtoken");
const router = express.Router();

// Replace with your JWT secret key
const JWT_SECRET = "your_jwt_secret_key";

// POST /api/tokens - Create a new token
router.post("/", (req, res) => {
  const { username } = req.body;
  if (!username) {
    return res.status(400).json({ message: "Username is required" });
  }

  const token = jwt.sign({ username }, JWT_SECRET, { expiresIn: "1h" });

  res.status(201).json({ message: "Token created successfully", token });
});

module.exports = router;
