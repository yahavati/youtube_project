const mongoose = require("mongoose");
const { faker } = require("@faker-js/faker");
const axios = require("axios");
const User = require("../models/User");
const Video = require("../models/Video");
const Comment = require("../models/Comment");

const PEXELS_API_KEY =
  "TvOYY0mAd1DCEzm9gz45r6XaWcRwO6pmPXN56P9QWsWDeDNHCjAA2F0b";

const connectDB = async () => {
  try {
    await mongoose.connect("mongodb://127.0.0.1:27017/Youtube", {
      useNewUrlParser: true,
      useUnifiedTopology: true,
      serverSelectionTimeoutMS: 30000, // Increase timeout to 30 seconds
    });
    console.log("MongoDB connected");
  } catch (error) {
    console.error("MongoDB connection error:", error);
    process.exit(1);
  }
};

const fetchRandomVideos = async (numVideos) => {
  const response = await axios.get(
    `https://api.pexels.com/videos/search?query=nature&per_page=${numVideos}`,
    {
      headers: {
        Authorization: PEXELS_API_KEY,
      },
    }
  );
  const videos = response.data.videos;
  return videos.map((video) => video.video_files[0].link);
};

const createRandomVideo = async (user, videoUrl) => {
  const title = faker.lorem.sentence();
  const description = faker.lorem.paragraph();

  const video = new Video({
    title,
    description,
    url: videoUrl,
    user: user._id,
    comments: [],
    likes: Math.floor(Math.random() * 100),
    dislikes: Math.floor(Math.random() * 100),
    views: Math.floor(Math.random() * 1000),
    likedBy: [],
    dislikedBy: [],
  });

  await video.save();
  user.videos.push(video._id);
  await user.save();

  return video;
};

const createRandomComment = async (user, video) => {
  const text = faker.lorem.sentence();

  const comment = new Comment({
    text,
    user: user._id,
    video: video._id,
    likes: Math.floor(Math.random() * 50),
    dislikes: Math.floor(Math.random() * 50),
    likedBy: [],
    dislikedBy: [],
  });

  await comment.save();
  user.comments.push(comment._id);
  video.comments.push(comment._id);
  await user.save();
  await video.save();

  return comment;
};

const populateVideosAndComments = async (numVideos, numCommentsPerVideo) => {
  try {
    await connectDB();

    const users = await User.find();
    if (users.length === 0) {
      throw new Error("No users found in the database.");
    }

    const videoUrls = await fetchRandomVideos(numVideos);

    const videos = [];
    for (let i = 0; i < numVideos; i++) {
      const randomUser = users[Math.floor(Math.random() * users.length)];
      const video = await createRandomVideo(randomUser, videoUrls[i]);
      videos.push(video);
    }

    for (const video of videos) {
      for (let j = 0; j < numCommentsPerVideo; j++) {
        const randomUser = users[Math.floor(Math.random() * users.length)];
        await createRandomComment(randomUser, video);
      }
    }

    console.log(
      `${numVideos} videos and ${
        numVideos * numCommentsPerVideo
      } comments populated successfully`
    );
    mongoose.disconnect();
  } catch (error) {
    console.error("Error populating videos and comments:", error);
    mongoose.disconnect();
  }
};

populateVideosAndComments(20, 5);
