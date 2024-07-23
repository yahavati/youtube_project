const Video = require('../models/Video');

const getAllVideos = async () => {
    return await Video.find({});
};

const getVideos = async () => {
    // Get top 10 most viewed videos
    const top10MostViewedVideos = await Video.find({})
        .sort({ views: -1 })
        .limit(10);

    // Get 10 random videos
    const count = await Video.countDocuments();
    const randomIndexes = new Set();

    // Generate unique random indexes
    while (randomIndexes.size < 10) {
        const randomIndex = Math.floor(Math.random() * count);
        randomIndexes.add(randomIndex);
    }

    // Fetch videos based on the random indexes
    const random10Videos = await Promise.all(
        [...randomIndexes].map(async (index) => {
            return await Video.findOne().skip(index);
        })
    );

    // Create a set to store unique videos
    const uniqueVideosSet = new Set();

    // Add top 10 most viewed videos to the set
    top10MostViewedVideos.forEach(video => uniqueVideosSet.add(JSON.stringify(video)));

    // Add random 10 videos to the set
    random10Videos.forEach(video => uniqueVideosSet.add(JSON.stringify(video)));

    // Convert the set back to an array of video objects
    const uniqueVideosArray = [...uniqueVideosSet].map(videoStr => JSON.parse(videoStr));

    
    return uniqueVideosArray;
};

// Example usage
(async () => {
    const videos = await getVideos();
    console.log('Unique Videos:', videos);
})();


const getVideoById = async (id) => {
    return await Video.findById(id);
};

const createVideo = async (videoData) => {
    const video = new Video(videoData);
    return await video.save();
};

const likeVideo = async (videoId, username) => {
    const video = await Video.findById(videoId);
    if (video) {
        if (!video.likes.includes(username)) {
            video.likes.push(username);
            video.dislikes = video.dislikes.filter(user => user !== username);
        }
        await video.save();
    }
    return video;
};

const dislikeVideo = async (videoId, username) => {
    const video = await Video.findById(videoId);
    if (video) {
        if (!video.dislikes.includes(username)) {
            video.dislikes.push(username);
            video.likes = video.likes.filter(user => user !== username);
        }
        await video.save();
    }
    return video;
};

const updateVideo = async (id, videoData) => {
    const video = await Video.findByIdAndUpdate(id, videoData, { new: true });
    return video;
};

const deleteVideo = async (id) => {
    const video = await Video.findById(id);
    if (video) {
        await Video.findByIdAndDelete(id);
        return true;
    }
    return false;
};

const addComment = async (videoId, username, text) => {
    const video = await Video.findById(videoId);
    if (video) {
        video.comments.push({ username, text });
        await video.save();
        return video.comments[video.comments.length - 1];
    }
    return null;
};

const incrementViews = async (videoId) => {
    const video = await Video.findByIdAndUpdate(videoId, { $inc: { views: 1 } }, { new: true });
    return video;
};

const editComment = async (videoId, commentId, newText) => {
    const video = await Video.findById(videoId);
    if (video) {
        const comment = video.comments.id(commentId);
        if (comment) {
            comment.text = newText;
            await video.save();
            return comment;
        }
    }
    return null;
};

const deleteComment = async (videoId, commentId) => {
    const video = await Video.findById(videoId);
    if (video) {
        video.comments.pull(commentId);
        await video.save();
        return true;
    }
    return false;
};
const likeComment = async (videoId, commentId, username) => {
    const video = await Video.findById(videoId);
    if (video) {
        const comment = video.comments.id(commentId);
        if (comment) {
            if (!comment.likes.includes(username)) {
                comment.likes.push(username);
                comment.dislikes = comment.dislikes.filter(user => user !== username);
            }
            await video.save();
            return comment;
        }
    }
    return null;
};

const dislikeComment = async (videoId, commentId, username) => {
    const video = await Video.findById(videoId);
    if (video) {
        const comment = video.comments.id(commentId);
        if (comment) {
            if (!comment.dislikes.includes(username)) {
                comment.dislikes.push(username);
                comment.likes = comment.likes.filter(user => user !== username);
            }
            await video.save();
            return comment;
        }
    }
    return null;
};

module.exports = {
    getAllVideos,
    getVideos,
    getVideoById,
    createVideo,
    likeVideo,
    dislikeVideo,
    updateVideo,
    deleteVideo,
    addComment,
    editComment,
    deleteComment,
    incrementViews,
    likeComment, 
    dislikeComment
};
