YouTube Clone Project
Welcome to the YouTube Clone Project! This project aims to replicate the core functionalities of YouTube using modern web technologies.

Prerequisites
Download and install MongoDB Compass
Connect to localhost
Create a database named Youtube
Getting Started
Follow these instructions to set up and run the project locally.

Clone the Repository
First, clone the project repository:

git clone https://github.com/yahavati/youtube_project.git

Checkout to the Specific Branch
Checkout to the part-2-branch branch:

cd youtube_project
git checkout part-2-final
git checkout part-2-branch
Setup and Run the Frontend
Open the first terminal.
Navigate to the frontend directory.
Install the dependencies.
Start the frontend server.
cd frontend
npm install
npm start

Setup and Run the Backend
Open the second terminal.
Navigate to the backend directory.
Install the dependencies.
Populate the database with users and videos.
Start the backend server.

cd backend
npm install
node scripts/populateUser.js
node scripts/populateVideos.js
node index.js

Project Structure
The project consists of two main parts:

Frontend: This directory contains the client-side code of the YouTube clone.
Backend: This directory contains the server-side code, including API endpoints and database interactions.
Technologies Used
Frontend: React, Context, and other modern JavaScript libraries
Backend: Node.js, Express.js, MongoDB
Database: MongoDB
Enjoy building your YouTube Clone!
