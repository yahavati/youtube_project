const User = require("../models/User");
const Video = require("../models/Video");

// Fetch all videos for a user
const getUserVideos = async (req, res) => {
  try {
    const userId = req.params.id;
    const user = await User.findById(userId).populate({
      path: "videos",
      populate: {
        path: "user",
        select: "displayName photo",
      },
    });

    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    res.json({ videos: user.videos, user });
  } catch (error) {
    console.error("Error fetching user videos:", error);
    res.status(500).json({ message: "Server error" });
  }
};

// Create a new video for a user
const createUserVideo = async (req, res) => {
  try {
    const userId = req.params.id;
    const { title, description } = req.body;
    const file = req.file;

    if (!title || !file) {
      return res.status(400).json({ message: "Title and file are required" });
    }

    // Convert file buffer to base64
    const base64Video = file.buffer.toString("base64");

    const newVideo = new Video({
      title,
      description,
      url: base64Video, // Save the base64 string in the database
      user: userId,
    });

    const savedVideo = await newVideo.save();

    const user = await User.findById(userId);
    user.videos.push(savedVideo._id);
    await user.save();

    const populatedVideo = await Video.findById(savedVideo._id).populate(
      "user",
      "displayName photo"
    );

    res.status(201).json(populatedVideo);
  } catch (error) {
    console.error("Error creating new video:", error);
    res.status(500).json({ message: "Server error" });
  }
};

// Update a video for a user
const updateUserVideo = async (req, res) => {
  try {
    const { videoId } = req.params;
    const { title, description } = req.body;
    console.log("Received file:", req.file); // Debugging line
    let updateData = { title, description };

    if (req.file) {
      const thumbnailBase64 = req.file.buffer.toString("base64");
      updateData.thumbnail = thumbnailBase64;
    }

    const updatedVideo = await Video.findByIdAndUpdate(videoId, updateData, {
      new: true,
    }).populate("user", "displayName photo");

    if (!updatedVideo) {
      return res.status(404).json({ message: "Video not found" });
    }

    res.json(updatedVideo);
  } catch (error) {
    console.error("Error updating video:", error);
    res.status(500).json({ message: "Server error" });
  }
};

// Delete a video for a user
const deleteUserVideo = async (req, res) => {
  try {
    const { videoId } = req.params;

    const deletedVideo = await Video.findByIdAndDelete(videoId);

    if (!deletedVideo) {
      return res.status(404).json({ message: "Video not found" });
    }

    const user = await User.findById(req.params.id);
    user.videos.pull(videoId);
    await user.save();

    res.json({ message: "Video deleted successfully" });
  } catch (error) {
    console.error("Error deleting video:", error);
    res.status(500).json({ message: "Server error" });
  }
};

const updateUserDetails = async (req, res) => {
  const userId = req.user._id; // Assuming you have the user id in the token
  const { displayName } = req.body;
  let photo;

  if (req.file) {
    photo = req.file.buffer.toString("base64");
  }

  try {
    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { displayName, photo },
      { new: true }
    ).select("-password"); // Exclude the password field from the response

    if (!updatedUser) {
      return res.status(404).json({ message: "User not found" });
    }

    res.json(updatedUser);
  } catch (error) {
    console.error("Error updating user details:", error);
    res.status(500).json({ message: "Server error" });
  }
};

module.exports = {
  getUserVideos,
  createUserVideo,
  updateUserVideo,
  deleteUserVideo,
  updateUserDetails,
};
