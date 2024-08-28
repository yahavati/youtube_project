const jwt = require("jsonwebtoken");
const User = require("../models/User");

const authMiddleware = async (req, res, next) => {
  try {
    let token = req.header("Authorization").replaceAll("Bearer ", "").trim();
    const decoded = jwt.verify(token, "your_jwt_secret");
    req.user = await User.findById(decoded.id).select("-password");
    next();
  } catch (error) {
    console.error("Invalid token:", error);
    res.status(401).json({ message: "Token is not valid" });
  }
};


module.exports = authMiddleware;
