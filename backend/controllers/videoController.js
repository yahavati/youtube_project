const videoService = require('../services/videoService');
const User = require('.././models/User');
const Video = require('.././models/Video');

const getAllVideos = async (req, res) => {
    try {
        const videos = await videoService.getAllVideos();
        res.status(200).json({ success: true, data: videos });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const get20Videos = async (req, res) => {
    try {
        const videos = await videoService.getVideos();
        res.status(200).json({ success: true, data: videos });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};


const getVideoById = async (req, res) => {
    try {
        const video = await videoService.getVideoById(req.params.id);
        if (!video) {
            return res.status(404).json({ success: false, message: 'Video not found' });
        }
        res.status(200).json({ success: true, data: video });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const uploadVideo = async (req, res) => {
    try {
        if (!req.files || !req.files.video) {
            return res.status(400).json({ success: false, message: 'No video file uploaded' });
        }

        const videoData = {
            ...req.body,
            videoPath: req.files.video[0].path,
        };

        const video = await videoService.createVideo(videoData);
        res.status(201).json({ success: true, data: video });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const likeVideo = async (req, res) => {
    try {
        const video = await videoService.likeVideo(req.params.id, req.body.username);
        res.status(200).json({ success: true, data: video });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const dislikeVideo = async (req, res) => {
    try {
        const video = await videoService.dislikeVideo(req.params.id, req.body.username);
        res.status(200).json({ success: true, data: video });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const updateVideo = async (req, res) => {
    try {
        const updateData = { ...req.body };
        if (req.files.video) {
            updateData.videoPath = req.files.video[0].path;
        }
        if (req.files.thumbnail) {
            updateData.thumbnailPath = req.files.thumbnail[0].path;
        }

        const video = await videoService.updateVideo(req.params.id, updateData);
        if (!video) {
            return res.status(404).json({ success: false, message: 'Video not found' });
        }
        res.status(200).json({ success: true, data: video });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const deleteVideo = async (req, res) => {
    try {
        const result = await videoService.deleteVideo(req.params.id);
        if (!result) {
            return res.status(404).json({ success: false, message: 'Video not found' });
        }
        res.status(200).json({ success: true, message: 'Video deleted successfully' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const addComment = async (req, res) => {
    try {
        const comment = await videoService.addComment(req.params.id, req.body.username, req.body.text);
        if (!comment) {
            return res.status(404).json({ success: false, message: 'Video not found' });
        }
        res.status(201).json({ success: true, data: comment });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const editComment = async (req, res) => {
    try {
        const comment = await videoService.editComment(req.params.id, req.params.commentId, req.body.text);
        if (!comment) {
            return res.status(404).json({ success: false, message: 'Video or comment not found' });
        }
        res.status(200).json({ success: true, data: comment });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const incrementViews = async (req, res) => {
    try {
        const video = await videoService.incrementViews(req.params.id);
        if (!video) {
            return res.status(404).json({ success: false, message: 'Video not found' });
        }
        res.status(200).json({ success: true, data: video });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const deleteComment = async (req, res) => {
    try {
        const result = await videoService.deleteComment(req.params.id, req.params.commentId);
        if (!result) {
            return res.status(404).json({ success: false, message: 'Video or comment not found' });
        }
        res.status(200).json({ success: true, message: 'Comment deleted successfully' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};
const likeComment = async (req, res) => {
    try {
        const comment = await videoService.likeComment(req.params.id, req.params.commentId, req.body.username);
        if (!comment) {
            return res.status(404).json({ success: false, message: 'Video or comment not found' });
        }
        res.status(200).json({ success: true, data: comment });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const dislikeComment = async (req, res) => {
    try {
        const comment = await videoService.dislikeComment(req.params.id, req.params.commentId, req.body.username);
        if (!comment) {
            return res.status(404).json({ success: false, message: 'Video or comment not found' });
        }
        res.status(200).json({ success: true, data: comment });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
};

const getByUser = async (req, res) => {
    try {
        const { userId } = req.params;

        // Find the user by ID to get the username
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }
        
        const username = user.username;

        // Find videos by author's username
        const videos = await Video.find({ author: username });

        if (videos.length === 0) {
            return res.status(404).json({ message: 'No videos found for this user' });
        }

        res.status(200).json(videos);
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};

const getByUserAndVideoId = async (req, res) => {
    try {
        const { userId, videoId } = req.params;

        // Find the user by ID to get the username
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }
        
        const username = user.username;

        // Find the video by author's username and video ID
        const video = await Video.findOne({ author: username, _id: videoId });

        if (!video) {
            return res.status(404).json({ message: 'Video not found for this user' });
        }

        res.status(200).json(video);
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};

module.exports = {
    getAllVideos,
    getVideoById,
    uploadVideo,
    get20Videos,
    likeVideo,
    dislikeVideo,
    updateVideo,
    deleteVideo,
    addComment,
    editComment,
    deleteComment,
    incrementViews,
    likeComment,
    dislikeComment,
    getByUser,
    getByUserAndVideoId
};
