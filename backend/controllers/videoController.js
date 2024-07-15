const Video = require("../models/Video");

const getVideos = async (req, res) => {
  try {
    // Fetch 10 random videos
    const randomVideos = await Video.aggregate([{ $sample: { size: 10 } }]);

    // Fetch 10 most viewed videos
    const mostViewedVideos = await Video.find({}).sort({ views: -1 }).limit(10);

    // Create a Set to ensure no duplicates
    const videoSet = new Set();

    // Add random videos to the set
    randomVideos.forEach((video) => videoSet.add(video._id.toString()));

    // Add most viewed videos to the set, ensuring no duplicates
    mostViewedVideos.forEach((video) => videoSet.add(video._id.toString()));

    // Fetch complete video details for the unique video IDs
    const uniqueVideoIds = Array.from(videoSet).slice(0, 20); // Ensure the total is 20
    const uniqueVideos = await Video.find({
      _id: { $in: uniqueVideoIds },
    }).populate("user", "displayName photo");

    res.json(uniqueVideos);
  } catch (error) {
    console.error("Error fetching videos:", error);
    res.status(500).json({ message: "Server error" });
  }
};

const getVideoById = async (req, res) => {
  try {
    const videoId = req.params.id;
    const video = await Video.findById(videoId)
      .populate({
        path: "comments",
        populate: {
          path: "user",
          select: "displayName photo",
        },
        select: "text createdAt likes dislikes likedBy dislikedBy",
      })
      .populate("user", "displayName photo");

    if (!video) {
      return res.status(404).json({ message: "Video not found" });
    }

    res.json(video);
  } catch (error) {
    console.error("Error fetching video:", error);
    res.status(500).json({ message: "Server error" });
  }
};

const toggleLike = async (req, res) => {
  const { videoId } = req.params;
  const userId = req.user._id;

  try {
    let video = await Video.findById(videoId);

    if (!video) {
      return res.status(404).json({ message: "Video not found" });
    }

    const likedIndex = video.likedBy.indexOf(userId);
    const dislikedIndex = video.dislikedBy.indexOf(userId);

    if (likedIndex !== -1) {
      video.likes -= 1;
      video.likedBy.splice(likedIndex, 1);
    } else {
      video.likes += 1;
      video.likedBy.push(userId);
      if (dislikedIndex !== -1) {
        video.dislikes -= 1;
        video.dislikedBy.splice(dislikedIndex, 1);
      }
    }

    await video.save();
    video = await Video.findById(videoId).populate("user", "displayName photo");

    res.status(200).json(video);
  } catch (error) {
    console.error("Error toggling like on video:", error);
    res.status(500).json({ message: "Server error" });
  }
};

const toggleDislike = async (req, res) => {
  const { videoId } = req.params;
  const userId = req.user._id;

  try {
    let video = await Video.findById(videoId);

    if (!video) {
      return res.status(404).json({ message: "Video not found" });
    }

    const dislikedIndex = video.dislikedBy.indexOf(userId);
    const likedIndex = video.likedBy.indexOf(userId);

    if (dislikedIndex !== -1) {
      video.dislikes -= 1;
      video.dislikedBy.splice(dislikedIndex, 1);
    } else {
      video.dislikes += 1;
      video.dislikedBy.push(userId);
      if (likedIndex !== -1) {
        video.likes -= 1;
        video.likedBy.splice(likedIndex, 1);
      }
    }

    await video.save();
    video = await Video.findById(videoId).populate("user", "displayName photo");

    res.status(200).json(video);
  } catch (error) {
    console.error("Error toggling dislike on video:", error);
    res.status(500).json({ message: "Server error" });
  }
};

module.exports = {
  getVideos,
  getVideoById,
  toggleLike,
  toggleDislike,
};
