const express = require("express");
const {
  addComment,
  deleteComment,
  updateComment,
  toggleLike,
  toggleDislike,
  getCommentById, // Add this line
} = require("../controllers/commentController");
const authMiddleware = require("../services/authMiddleware");

const router = express.Router();

// Add a new comment
router.post("/", authMiddleware, addComment);

// Delete a comment
router.delete("/:id", authMiddleware, deleteComment);

// Update a comment
router.put("/:id", authMiddleware, updateComment);

// Toggle like on a comment
router.post("/:commentId/like", authMiddleware, toggleLike);

// Toggle dislike on a comment
router.post("/:commentId/dislike", authMiddleware, toggleDislike);

// Get a comment by ID
router.get("/:id", getCommentById); // Add this line

module.exports = router;