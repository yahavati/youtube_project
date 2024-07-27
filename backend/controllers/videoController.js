const Video = require("../models/Video");
const mongoose = require("mongoose");

const getVideos = async (req, res) => {
    try {
        // Fetch 10 most viewed videos
        const mostViewedVideos = await Video.find({}).sort({ views: -1 }).limit(10);

        // Extract IDs of the most viewed videos
        const mostViewedVideoIds = mostViewedVideos.map((video) =>
            video._id.toString()
        );

        // Fetch 10 random videos excluding the most viewed ones
        const randomVideos = await Video.aggregate([
            {
                $match: {
                    _id: {
                        $nin: mostViewedVideoIds.map(
                            (id) => new mongoose.Types.ObjectId(id)
                        ),
                    },
                },
            },
            { $sample: { size: 10 } },
        ]);

        // Combine the two lists
        const combinedVideos = [...mostViewedVideos, ...randomVideos];

        // Fetch complete video details for the combined video IDs
        const uniqueVideoIds = combinedVideos.map((video) => video._id);
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

const addView = async (req, res) => {
    try {
        const videoId = req.params.id;
        const userId = req.user._id;

        const video = await Video.findById(videoId);

        if (!video) {
            return res.status(404).json({ message: "Video not found" }); // Use 404 for not found
        }

        // Check if the user is the owner of the video
        if (video.user.toString() === userId) {
            return res
                .status(200)
                .json({ message: "User is the owner, view not counted" });
        }

        // Check if the user has already viewed the video
        if (video.viewedBy.includes(userId)) {
            return res
                .status(200)
                .json({
                    message: "User has already viewed the video",
                    views: video.views,
                });
        }

        // Increment view count and add user to viewedBy array
        video.views += 1;
        video.viewedBy.push(userId);

        await video.save();

        return res
            .status(200)
            .json({ message: "View count incremented", views: video.views });
    } catch (error) {
        console.error("Error incrementing view count:", error);
        return res.status(500).json({ message: "Server error" });
    }
};

module.exports = {
    getVideos,
    getVideoById,
    toggleLike,
    toggleDislike,
    addView,
};

