// utils/logger.js

const winston = require('winston');
const path = require('path');

// Define log format
const logFormat = winston.format.combine(
    winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
    winston.format.errors({ stack: true }),
    winston.format.splat(),
    winston.format.json()
);

// Create logger instance
const logger = winston.createLogger({
    level: process.env.NODE_ENV === 'production' ? 'info' : 'debug',
    format: logFormat,
    defaultMeta: { service: 'user-service' },
    transports: [
        // Write all logs with level `error` and below to `error.log`
        new winston.transports.File({ filename: path.join(__dirname, '../logs/error.log'), level: 'error' }),
        // Write all logs with level `info` and below to `combined.log`
        new winston.transports.File({ filename: path.join(__dirname, '../logs/combined.log') }),
    ],
});

// If we're not in production, log to the console as well
if (process.env.NODE_ENV !== 'production') {
    logger.add(new winston.transports.Console({
        format: winston.format.combine(
            winston.format.colorize(),
            winston.format.simple()
        ),
    }));
}


const requestLogger = (req, res, next) => {
    // Store the start time to calculate the response time
    const startTime = Date.now();

    // Set up a listener to log details after the response is finished
    res.on('finish', () => {
        const duration = Date.now() - startTime;
        logger.info(`${req.method} ${req.originalUrl} ${res.statusCode} ${duration}ms`, {
            ip: req.ip,
            user: req.user ? req.user.username : 'anonymous',
            userAgent: req.get('User-Agent'),
            body: req.method !== 'GET' ? req.body : undefined,
            status: res.statusCode
        });
    });

    next();
};


module.exports = { logger, requestLogger };