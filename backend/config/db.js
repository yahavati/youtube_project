const mongoose = require("mongoose");

const connectDB = async () => {
  try {
    // Replace 'youtubeDB' with your desired database name
    const conn = await mongoose.connect("mongodb://127.0.0.1:27017/Youtube", {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    });

    console.log(`MongoDB Connected: ${conn.connection.host}`);
  } catch (error) {
    console.error(`Error: ${error.message}`);
    process.exit(1); // Exit process with failure
  }
};

module.exports = connectDB;
