const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');
const multer = require('multer');
const authMiddleware = require('../middleware/authMiddleware');
const videoController = require('.././controllers/videoController'); // You'll need to create this middleware

const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'uploads/');
    },
    filename: function (req, file, cb) {
        cb(null, Date.now() + '-' + file.originalname);
    }
});

const upload = multer({ storage: storage });

router.post('/register', upload.single('profilePhotoUrl'), authController.register);
router.post('/login', authController.login);
router.post('/tokens', authController.createToken);
router.get('/users/:userId', authController.getUserById);





router.put('/users/:userId', authController.updateUserById);
router.delete('/users/:userId', authController.deleteById);
router.get('/users/:userId/videos', videoController.getByUser);
router.get('/users/:userId/videos/:videoId', videoController.getByUserAndVideoId);
router.get('/profile-photo', authMiddleware, authController.getProfilePhoto);


router.get('/user/profile', authMiddleware, authController.getUserProfile);

module.exports = router;