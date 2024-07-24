const authService = require('../services/authService');
const User = require('.././models/User');
const jwt = require('jsonwebtoken');
const config = require('../config');
const mongoose = require('mongoose');

const register = async (req, res) => {
    try {
        const { username, password } = req.body;
        const profilePhotoUrl = req.file ? req.file.path : '';

        const user = await authService.register(username, password, profilePhotoUrl );
        res.status(201).json({ success: true, user });
    } catch (error) {
        res.status(409).json({ success: false, message: error.message });
    }
};

const createToken = async (req, res) => {
    try {
        const { username, password } = req.body;

        // Find the user by username
        const user = await User.findOne({ username });
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        // Check if the password matches
        const isMatch = await user.matchPassword(password);
        if (!isMatch) {
            return res.status(401).json({ message: 'Invalid credentials' });
        }

        // Create a token
        const token = jwt.sign({ id: user._id }, config.jwtSecret, {
            expiresIn: '1h'
        });

        // Return the token
        res.status(200).json({ token });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};

const login = async (req, res) => {
    try {
        const { username, password } = req.body;
        console.log('Request body:', req.body);
        const { user, token } = await authService.login(username, password);
        res.status(200).json({ success: true, user, token });
    } catch (error) {
        res.status(404).json({ success: false, message: error.message });
    }
};
const getProfilePhoto = async (req, res) => {
    try {
        const userId = req.user.id;
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }
        res.status(200).json({ profilePhotoUrl: user.profilePhotoUrl });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};

const getUserProfile = async (req, res) => {
    try {

        const user = req.user;

        const profilePhotoUrl = user.profilePhotoUrl;

        const userProfile = {
            username: user.username,
            nickname: user.nickname,
            profilePhotoUrl: profilePhotoUrl,
        };

        res.status(200).json({
            success: true,
            message: 'User profile fetched successfully',
            data: userProfile
        });
    } catch (error) {
        console.error('Error in getUserProfile:', error);
        res.status(500).json({
            success: false,
            message: 'An error occurred while fetching the user profile',
            error: error.message
        });
    }
};

const getUserById = async (req, res) => {
    try {
        const { userId } = req.params;
        let user;

        // Check if the identifier is a valid ObjectId
        if (mongoose.Types.ObjectId.isValid(userId)) {
            // Try to find the user by ID
            user = await User.findById(userId);
        }

        // If no user found by ID or identifier is not a valid ObjectId, search by username
        if (!user) {
            user = await User.findOne({ username: userId });
        }

        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        res.status(200).json({ user });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};
const updateUserById = async (req, res) => {
    try {
        const { userId } = req.params;
        const { profilePhotoUrl, ...updateData } = req.body;

       

        // Find the user by ID and update with new data
        const updatedUser = await User.findByIdAndUpdate(
            userId,
            { profilePhotoUrl, ...updateData },
            { new: true, runValidators: true }
        );

        if (!updatedUser) {
            return res.status(404).json({ message: 'User not found' });
        }

        res.status(200).json({ message: 'User updated successfully', user: updatedUser });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};

const deleteById = async (req, res) => {
    try {
        const { userId } = req.params;

        // Find the user by ID and delete
        const deletedUser = await User.findByIdAndDelete(userId);

        if (!deletedUser) {
            return res.status(404).json({ message: 'User not found' });
        }

        res.status(200).json({ message: 'User deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};



module.exports = {
    register,
    login,
    getProfilePhoto,
    getUserProfile,
    getUserById,
    updateUserById,
    deleteById,
    createToken
};
