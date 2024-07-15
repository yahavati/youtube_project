const Comment = require("../models/Comment");
const User = require("../models/User");
const Video = require("../models/Video");

// Add a new comment
const addComment = async (req, res) => {
  const { text, user, video } = req.body;

  try {
    const comment = new Comment({
      text,
      user,
      video,
    });

    await comment.save();

    await User.findByIdAndUpdate(user, { $push: { comments: comment._id } });
    await Video.findByIdAndUpdate(video, {
      $push: { comments: comment._id },
    });

    res.status(201).json(comment);
  } catch (error) {
    console.error("Error adding comment:", error);
    res.status(500).json({ message: "Server error" });
  }
};

// Delete a comment
const deleteComment = async (req, res) => {
  const { id } = req.params;

  try {
    const comment = await Comment.findByIdAndDelete(id);

    if (!comment) {
      return res.status(404).json({ message: "Comment not found" });
    }

    await User.findByIdAndUpdate(comment.user, {
      $pull: { comments: comment._id },
    });
    await Video.findByIdAndUpdate(comment.video, {
      $pull: { comments: comment._id },
    });

    res.status(200).json({ message: "Comment deleted successfully" });
  } catch (error) {
    console.error("Error deleting comment:", error);
    res.status(500).json({ message: "Server error" });
  }
};

// Update a comment
const updateComment = async (req, res) => {
  const { id } = req.params;
  const { text, likes, dislikes } = req.body;

  try {
    const comment = await Comment.findByIdAndUpdate(
      id,
      { text, likes, dislikes },
      { new: true }
    );

    if (!comment) {
      return res.status(404).json({ message: "Comment not found" });
    }

    res.status(200).json(comment);
  } catch (error) {
    console.error("Error updating comment:", error);
    res.status(500).json({ message: "Server error" });
  }
};

// Toggle like on a comment
const toggleLike = async (req, res) => {
  const { commentId } = req.params;
  const userId = req.user._id;

  try {
    let comment = await Comment.findById(commentId);

    if (!comment) {
      return res.status(404).json({ message: "Comment not found" });
    }

    const likedIndex = comment.likedBy.indexOf(userId);
    const dislikedIndex = comment.dislikedBy.indexOf(userId);

    if (likedIndex !== -1) {
      comment.likes -= 1;
      comment.likedBy.splice(likedIndex, 1);
    } else {
      comment.likes += 1;
      comment.likedBy.push(userId);
      if (dislikedIndex !== -1) {
        comment.dislikes -= 1;
        comment.dislikedBy.splice(dislikedIndex, 1);
      }
    }

    await comment.save();
    comment = await Comment.findById(commentId).populate(
      "user",
      "displayName photo"
    );

    res.status(200).json(comment);
  } catch (error) {
    console.error("Error toggling like on comment:", error);
    res.status(500).json({ message: "Server error" });
  }
};

// Toggle dislike on a comment
const toggleDislike = async (req, res) => {
  const { commentId } = req.params;
  const userId = req.user._id;

  try {
    let comment = await Comment.findById(commentId);

    if (!comment) {
      return res.status(404).json({ message: "Comment not found" });
    }

    const dislikedIndex = comment.dislikedBy.indexOf(userId);
    const likedIndex = comment.likedBy.indexOf(userId);

    if (dislikedIndex !== -1) {
      comment.dislikes -= 1;
      comment.dislikedBy.splice(dislikedIndex, 1);
    } else {
      comment.dislikes += 1;
      comment.dislikedBy.push(userId);
      if (likedIndex !== -1) {
        comment.likes -= 1;
        comment.likedBy.splice(likedIndex, 1);
      }
    }

    await comment.save();
    comment = await Comment.findById(commentId).populate(
      "user",
      "displayName photo"
    );

    res.status(200).json(comment);
  } catch (error) {
    console.error("Error toggling dislike on comment:", error);
    res.status(500).json({ message: "Server error" });
  }
};

module.exports = {
  addComment,
  deleteComment,
  updateComment,
  toggleLike,
  toggleDislike,
};
