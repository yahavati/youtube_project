const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const User = require("../models/User");

const register = async (req, res) => {
    const { username, displayName, password } = req.body;
    const photo = req.file;

    try {
        if (!username || !displayName || !password) {
            return res.status(400).json({ message: "All fields are required" });
        }

        const userExists = await User.findOne({ username });
        const displayNameExists = await User.findOne({ displayName });
        if (userExists) {
            return res.status(400).json({ message: "Username already exists" });
        }
        if (displayNameExists) {
            return res.status(400).json({ message: "Display name already exists" });
        }

        const hashedPassword = await bcrypt.hash(password, 10);

        let photoBase64 = null;
        if (photo) {
            photoBase64 = photo.buffer.toString("base64");
        }

        const user = new User({
            username,
            displayName,
            password: hashedPassword,
            photo: photoBase64,
        });

        await user.save();

        res.status(201).json({ message: "User registered successfully" });
    } catch (error) {
        console.error("Error during registration:", error);
        res.status(500).json({ message: "Server error" });
    }
};

const login = async (req, res) => {
    const { username, password } = req.body;

    try {
        console.log(`Login attempt with username: ${username}`);
        const user = await User.findOne({ username });

        if (!user) {
            console.log("User not found");
            return res.status(404).json({ message: "Invalid credentials" });
        }

        const isMatch = await bcrypt.compare(password, user.password);

        if (!isMatch) {
            console.log("Password does not match");
            return res.status(404).json({ message: "Invalid credentials" });
        }

        const token = jwt.sign({ id: user._id }, "your_jwt_secret", {
            expiresIn: "1h",
        });

        res
            .cookie("token", token, { httpOnly: false })
            .json({ message: "Logged in successfully", user });
    } catch (error) {
        console.error("Error during login:", error);
        res.status(500).json({ message: "Server error" });
    }
};


const logout = (req, res) => {
    res.clearCookie("token").json({ message: "Logged out successfully" });
};

const getCurrentUser = async (req, res) => {
    try {
        // Extract token from Authorization header
        const authHeader = req.header("Authorization");
    

        const token = authHeader.replace("Bearer ", "").trim();

        // Verify the token
        const decoded = jwt.verify(token, "your_jwt_secret");
        const user = await User.findById(decoded.id).select("-password");
        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        // Send back the user data
        res.json(user);
    } catch (error) {
        console.error("Error getting current user:", error);
        res.status(500).json({ message: "Server error" });
    }
};

module.exports = { register, login, logout, getCurrentUser };

