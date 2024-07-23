const express = require('express');
const videoController = require('../controllers/videoController');
const authMiddleware = require('../middleware/authMiddleware');
const multer = require('multer');

const router = express.Router();

const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'uploads/');
    },
    filename: function (req, file, cb) {
        cb(null, Date.now() + '-' + file.originalname);
    }
});

const upload = multer({ storage: storage });

router.get('/videos', videoController.getAllVideos);
router.get('/allVideos', videoController.get20Videos);
router.get('/videos/:id', videoController.getVideoById);
router.post('/videos', upload.fields([
    { name: 'video', maxCount: 1 },
]), videoController.uploadVideo);
router.post('/videos/:id/like', videoController.likeVideo);
router.post('/videos/:id/dislike', videoController.dislikeVideo);
router.put('/videos/:id', upload.fields([
    { name: 'video', maxCount: 1 },
    { name: 'thumbnail', maxCount: 1 }
]), videoController.updateVideo);
router.delete('/videos/:id', videoController.deleteVideo);
router.post('/videos/:id/comments', videoController.addComment);
router.put('/videos/:id/comments/:commentId', videoController.editComment);
router.delete('/videos/:id/comments/:commentId', videoController.deleteComment);
router.get('/videos/:id/view', videoController.incrementViews);
router.post('/videos/:id/comments/:commentId/like', videoController.likeComment);
router.post('/videos/:id/comments/:commentId/dislike', videoController.dislikeComment);



module.exports = router;