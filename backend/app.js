const express = require('express');
const mongoose = require('mongoose');
const authRoutes = require('./routes/authRoutes');
const videoRoutes = require('./routes/videoRoutes');
const config = require('./config');
const cors = require('cors');
const path = require('path');
const Video = require('./models/Video'); 
  

const { logger, requestLogger } = require('./middleware/logger');

const app = express();


const corsOptions = {
    origin: '*', // Allow all origins
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS'], // Allow all methods
    allowedHeaders: ['Content-Type', 'Authorization'], // Allow these headers
    credentials: true, // Allow cookies and authentication headers
    optionsSuccessStatus: 200 // Some legacy browsers choke on 204
  };



// Middleware

app.use(requestLogger);
app.use(express.json());
app.use(cors(corsOptions));






app.use('/uploads', express.static(path.join(__dirname, 'uploads')));
// Routes
app.use('/api/', authRoutes);
app.use('/api/', videoRoutes);

const videoTitles = [
  'Exciting Adventure Part 1',
  'Exciting Adventure Part 2',
  'Exciting Adventure Part 3',
  'Exciting Adventure Part 4',
  'Exciting Adventure Part 5',
  'Exciting Adventure Part 6',
  'Exciting Adventure Part 7',
  'Exciting Adventure Part 8',
  'Exciting Adventure Part 9',
  'Exciting Adventure Part 10'
];

const authors = [
  'Author1',
  'Author2',
  'Author3',
  'Author4',
  'Author5',
  'Author6',
  'Author7',
  'Author8',
  'Author9',
  'Author10'
];


const generateVideos = async () => {
  try {
      // Fetch existing videos
      const existingVideos = await Video.find({});
      const videosToGenerate = 20 - existingVideos.length;

      if (videosToGenerate > 0) {
          // Create the necessary number of video documents
          const videos = [];
          for (let i = 0; i < videosToGenerate; i++) {
              const videoIndex = existingVideos.length + i + 1;
              const video = new Video({
                  title: videoIndex <= 10 ? videoTitles[videoIndex - 1] : `Exciting Adventure Part ${videoIndex}`,
                  author: videoIndex <= 10 ? authors[videoIndex - 1] : authors[(videoIndex - 1) % 10],
                  views: Math.floor(Math.random() * 1000), // Random views count
                  videoPath: `uploads/videoapp${(videoIndex - 1) % 10 + 1}.mp4`,
                  thumbnailUri: `uploads/thumbnail${(videoIndex - 1) % 10 + 1}.jpg`, // Assuming you have corresponding thumbnails
                  description: `Description for video ${videoIndex}`,
                  likes: [],
                  dislikes: [],
                  comments: []
              });
              videos.push(video);
          }

          // Save all videos to the database
          await Video.insertMany(videos);
          console.log(`${videosToGenerate} videos generated and saved successfully`);
      } else {
          console.log('There are already 20 or more videos in the database');
      }
  } catch (err) {
      console.error('Error generating videos', err);
  }
};


// Database Connection
mongoose.connect(config.mongoURI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
}).then(generateVideos())
  .catch(err => console.log(err));






// Start Server
const port = config.port || 5000;
app.listen(port, () => console.log(`Server running on port ${port}`));


