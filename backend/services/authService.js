const User = require('../models/User');
const jwt = require('jsonwebtoken');
const config = require('../config');

const register = async (username, password, profilePhotoUrl) => {
    const user = new User({ username, password, profilePhotoUrl });
    await user.save();
    return user;
};

const login = async (username, password) => {
    const user = await User.findOne({ username });
    if (!user || !(await user.matchPassword(password))) {
        throw new Error('Invalid username or password');
    }
    const token = jwt.sign({ id: user._id }, config.jwtSecret, {
        expiresIn: '1h'
    });
    return { user, token };
};

module.exports = {
    register,
    login
};
