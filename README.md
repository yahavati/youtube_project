
# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

How to run:
In the project directory, you can run:
### 'npm install' and then 'npm install bootstrap' in frontend folder
### `npm start` (in both backend and frontend folders, 
###  but first Connect to mongodb://localhost:27017
###  in MongoDB and create there a new data base named Youtube)






Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can't go back!**

If you aren't satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you're on your own.

You don't have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn't feel obligated to use this feature. However we understand that this tool wouldn't be useful if you couldn't customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

### Code Splitting

This section has moved here: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)



# YouTube Clone Project

Welcome to the YouTube Clone Project! This project aims to replicate the core functionalities of YouTube using modern web technologies.

## Prerequisites

- Download and install [MongoDB Compass](https://www.mongodb.com/products/compass)
- Connect to `localhost`
- Create a database named `Youtube`

## Getting Started

Follow these instructions to set up and run the project locally.

### Clone the Repository

First, clone the project repository:

```
git clone https://github.com/yahavati/youtube_project.git

```

### Checkout to the Specific Branch

Checkout to the part-2-branch branch:

```
cd youtube_project
git checkout part-2-final

```

### Setup and Run the Frontend

1. Open the first terminal.
2. Navigate to the frontend directory.
3. Install the dependencies.
4. Start the frontend server.

```
cd frontend
npm install
npm start

```
### Setup and Run the Backend

1. Open the second terminal.
2. Navigate to the backend directory.
3. Install the dependencies.
4. Populate the database with users and videos.
5. Start the backend server.

```

cd backend
npm install
node scripts/populateUser.js
node scripts/populateVideos.js
node index.js

```

## Project Structure

The project consists of two main parts:

- Frontend: This directory contains the client-side code of the YouTube clone.
- Backend: This directory contains the server-side code, including API endpoints and database interactions.

### Technologies Used
- Frontend: React, Context, and other modern JavaScript libraries
- Backend: Node.js, Express.js, MongoDB
- Database: MongoDB

Enjoy building your YouTube Clone!



