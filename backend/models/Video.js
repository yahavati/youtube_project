const mongoose = require('mongoose');

const commentSchema = new mongoose.Schema({
    username: {
        type: String,
        required: true
    },
    text: {
        type: String,
        required: true
    },
    date: {
        type: Date,
        default: Date.now
    },
    userPhoto: {
        type: String
    },
    likes: {
        type: [String],
        default: []
    },
    dislikes: {
        type: [String],
        default: []
    }
});

const videoSchema = new mongoose.Schema({
    title: {
        type: String,
        required: true
    },
    author: {
        type: String,
        required: true
    },
    date: {
        type: Date,
        required: true,
        default: Date.now
    },
    views: {
        type: Number,
        default: 0
    },
    videoPath: {
        type: String,
        required: true
    },
    thumbnailUri: {
        type: String
    },
    description: {
        type: String
    },
    likes: {
        type: [String],
        default: []
    },
    dislikes: {
        type: [String],
        default: []
    },
    comments: {
        type: [commentSchema],
        default: []
    }
    
});

const Video = mongoose.model('Video', videoSchema);

module.exports = Video;