const mongoose = require("mongoose");
const { faker } = require("@faker-js/faker");
const axios = require("axios");
const User = require("../models/User");

mongoose.connect("mongodb://127.0.0.1:27017/Youtube", {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

const createRandomUser = async () => {
  const username = faker.internet.userName();
  const displayName = faker.person.fullName();
  const password = faker.internet.password();

  // Fetch a random user photo from randomuser.me
  const response = await axios.get("https://randomuser.me/api/");
  const photo = response.data.results[0].picture.large;

  return {
    username,
    displayName,
    password,
    photo,
    createdAt: new Date(),
    videos: [],
    comments: [],
  };
};

const populateUsers = async () => {
  try {
    const users = [];
    for (let i = 0; i < 10; i++) {
      const user = await createRandomUser();
      users.push(user);
    }

    await User.insertMany(users);
    console.log("Users have been populated!");
    process.exit();
  } catch (error) {
    console.error("Error populating users:", error);
    process.exit(1);
  }
};

populateUsers();
