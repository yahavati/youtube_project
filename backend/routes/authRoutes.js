const express = require("express");
const multer = require("multer");
const {
  register,
  login,
  logout,
  getCurrentUser,
} = require("../controllers/authController");
const authMiddleware = require("../services/authMiddleware");

const router = express.Router();

const upload = multer({ storage: multer.memoryStorage() });

router.post("/register", upload.single("photo"), register);
router.post("/login", login);
router.post("/logout", logout);

router.get("/me", authMiddleware, getCurrentUser);

module.exports = router;
