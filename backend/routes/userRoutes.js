const express = require("express");
const {
  getUserVideos,
  createUserVideo,
  updateUserVideo,
  deleteUserVideo,
  updateUserDetails,
  getUserById,
  updateUserById,
  createUser,
  deleteUserById,
  getUserVideo,
} = require("../controllers/userController");
const authMiddleware = require("../services/authMiddleware");
const multer = require("multer");

const router = express.Router();
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

// POST /api/users - Create a new user
router.post("/", upload.single("photo"), createUser);

// GET /api/users/:id - Fetch user by ID
router.get("/:id", getUserById);

// PUT /api/users/:id - Edit user by ID
router.put("/:id", updateUserById);

// DELETE /api/users/:id - Delete user by ID
router.delete("/:id", deleteUserById);

// GET /api/users/:id/videos - Fetch all videos for a user
router.get("/:id/videos", getUserVideos);

// POST /api/users/:id/videos - Create a new video for a user
router.post(
  "/:id/videos",
  authMiddleware,
  upload.single("url"),
  createUserVideo
);

// GET /api/users/:id/videos/:videoId - Fetch a user video
router.get("/:id/videos/:videoId", getUserVideo);

// PUT /api/users/:id/videos/:videoId - Update a video for a user
router.put(
  "/:id/videos/:videoId",
  authMiddleware,
  upload.single("thumbnail"),
  updateUserVideo
);

// DELETE /api/users/:id/videos/:videoId - Delete a video for a user
router.delete("/:id/videos/:videoId", authMiddleware, deleteUserVideo);

router.put("/me", authMiddleware, upload.single("photo"), updateUserDetails);

module.exports = router;
