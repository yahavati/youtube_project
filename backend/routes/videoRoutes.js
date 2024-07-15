const express = require("express");
const {
  getVideos,
  getVideoById,
  toggleLike,
  toggleDislike,
} = require("../controllers/videoController");
const authMiddleware = require("../services/authMiddleware");

const router = express.Router();

// GET /api/videos - Fetch 20 videos (10 random and 10 most viewed)
router.get("/", getVideos);

// GET /api/videos/:id - Fetch a video by ID
router.get("/:id", getVideoById);

// Toggle like on a video
router.post("/:videoId/like", authMiddleware, toggleLike);

// Toggle dislike on a video
router.post("/:videoId/dislike", authMiddleware, toggleDislike);

module.exports = router;
